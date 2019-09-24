package io.github.jokoframework.wilson.scheduler;

import java.text.SimpleDateFormat;
import java.util.List;

import io.github.jokoframework.wilson.cache.entities.ReadOperationEntity;
import io.github.jokoframework.wilson.cache.service.ReadOperationService;
import io.github.jokoframework.wilson.exceptions.ReadOperationException;
import io.github.jokoframework.wilson.scheduler.dto.LoginRequestDTO;
import io.github.jokoframework.wilson.scheduler.dto.LoginResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Component
public class ScheduledTasks {

    @Autowired
    private ReadOperationService readOperationService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTasks.class);
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Value("${scheduler.login.url}")
    private String refreshTokenUrl;
    @Value("${scheduler.login.username}")
    private String loginUsername;
    @Value("${scheduler.login.password}")
    private String loginPassword;
    @Value("${scheduler.access.token.url}")
    private String accessTokenUrl;

    private static String refreshToken;
    private static String accessToken;

    @Scheduled(cron = "${scheduler.login.cron.timer}")
    public void refreshLogin() {
        // Login request is built to obtain/refresh the Refresh Token
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        LoginRequestDTO credentials = new LoginRequestDTO();
        credentials.setUsername(loginUsername);
        credentials.setPassword(loginPassword);

        // We make the request, logging response data and managing some types of exceptions
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<LoginResponseDTO> response = restTemplate.exchange(refreshTokenUrl,
                    HttpMethod.POST,
                    new HttpEntity<>(credentials, headers),
                    LoginResponseDTO.class);

            // Refresh Token is updated
            if (response.getBody().getSecret() != null) {
                setRefreshToken(response.getBody().getSecret());
                String expiration = dateFormat.format(response.getBody().getExpiration());
                LOGGER.info("REFRESH TOKEN SERVICE WAS REACHED - TOKEN RENEWED - EXPIRES ON = {}", expiration);
            } else {
                LOGGER.info("REFRESH TOKEN SERVICE WAS REACHED - SECRET WAS NOT RETURNED BY SERVICE?");
            }
        } catch (ResourceAccessException e) {
            LOGGER.info("REFRESH TOKEN SERVICE COULD NOT BE REACHED");
        }
        try {
            headers.add("X-JOKO-AUTH", refreshToken);
            ResponseEntity<LoginResponseDTO> response = restTemplate.exchange(accessTokenUrl,
                    HttpMethod.POST,
                    new HttpEntity<>(headers),
                    LoginResponseDTO.class);

            // Access Token is updated
            if (response.getBody().getSecret() != null) {
                setAccessToken(response.getBody().getSecret());
                String expiration = dateFormat.format(response.getBody().getExpiration());
                LOGGER.info("ACCESS TOKEN SERVICE WAS REACHED - TOKEN RENEWED - EXPIRES ON = {}", expiration);
            } else {
                LOGGER.info("ACCESS TOKEN SERVICE WAS REACHED - SECRET WAS NOT RETURNED BY SERVICE?");
            }
        } catch (ResourceAccessException e) {
            LOGGER.info("ACCESS TOKEN SERVICE COULD NOT BE REACHED");
        }
    }

    @Scheduled(cron = "${scheduler.update.read.cache.cron.timer}")
    public void refreshReadCache() {
        System.out.println("");
        LOGGER.info("STARTING REFRESH READ CACHE METHOD");
        List<ReadOperationEntity> readOperations = readOperationService.listReadOperations();
        if (readOperations.size() == 0) {
            LOGGER.info("THERE ARE NO READ OPERATIONS IN THE DATABASE");
        }
        for (ReadOperationEntity readOperation : readOperations) {
            try {
                readOperationService.updateReadOperationCache(readOperation.getResource());
                LOGGER.info("SUCCESS - UPDATE FOR RESOURCE = {}", readOperation.getResource());
            } catch (ReadOperationException | ResourceAccessException | HttpClientErrorException e) {
                LOGGER.info("FAILURE - UPDATE FOR RESOURCE = {}", readOperation.getResource());
                LOGGER.error(e.toString());
            }
        }

        LOGGER.info("ENDING REFRESH READ CACHE METHOD");
        System.out.println("");
    }

    // Private Setters and Public Getters
    public static String getRefreshToken() {
        return refreshToken;
    }

    private static void setRefreshToken(String refreshToken) {
        ScheduledTasks.refreshToken = refreshToken;
    }

    public static String getAccessToken() {
        return accessToken;
    }

    private static void setAccessToken(String accessToken) {
        ScheduledTasks.accessToken = accessToken;
    }
}