package io.github.jokoframework.wilson.scheduler;

import java.text.SimpleDateFormat;
import java.util.List;

import io.github.jokoframework.wilson.cache.entities.ReadOperationEntity;
import io.github.jokoframework.wilson.cache.entities.ResponseCacheEntity;
import io.github.jokoframework.wilson.cache.entities.WriteCacheEntity;
import io.github.jokoframework.wilson.cache.enums.WriteCacheStateEnum;
import io.github.jokoframework.wilson.cache.service.ReadOperationService;
import io.github.jokoframework.wilson.cache.service.WriteCacheService;
import io.github.jokoframework.wilson.exceptions.ReadOperationException;
import io.github.jokoframework.wilson.scheduler.dto.LoginRequestDTO;
import io.github.jokoframework.wilson.scheduler.dto.LoginResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.UnavailableException;

@Component
public class ScheduledTasks {

    @Autowired
    private ReadOperationService readOperationService;

    @Autowired
    private WriteCacheService writeCacheService;

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

    private RestTemplate restTemplate = new RestTemplate();

    @Scheduled(cron = "${scheduler.login.cron.timer}")
    public void refreshLogin() {
        // Login request is built and called to obtain/refresh the Refresh Token
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        LoginRequestDTO credentials = new LoginRequestDTO();
        credentials.setUsername(loginUsername);
        credentials.setPassword(loginPassword);

        try {
            LOGGER.info("REFRESH TOKEN - ATTEMPTING TO OBTAIN/RENEW REFRESH TOKEN");
            ResponseEntity<LoginResponseDTO> response = restTemplate.exchange(refreshTokenUrl,
                    HttpMethod.POST,
                    new HttpEntity<>(credentials, headers),
                    LoginResponseDTO.class);

            // Refresh Token is updated
            if (response.getBody().getSecret() != null) {
                setRefreshToken(response.getBody().getSecret());
                String expiration = dateFormat.format(response.getBody().getExpiration());
                LOGGER.info("REFRESH TOKEN - SERVICE WAS REACHED - TOKEN WAS RENEWED AND EXPIRES ON = {}", expiration);
            } else {
                LOGGER.info("REFRESH TOKEN - SERVICE WAS REACHED - SECRET WAS NOT RETURNED BY SERVICE?");
            }
        } catch (ResourceAccessException e) {
            LOGGER.info("REFRESH TOKEN - SERVICE COULD NOT BE REACHED");
        }

        // With the Refresh Token request is built and called to obtain/refresh the Access Token
        try {
            LOGGER.info("REFRESH TOKEN - ATTEMPTING TO OBTAIN/RENEW ACCESS TOKEN");
            headers.set("X-JOKO-AUTH", refreshToken);
            ResponseEntity<LoginResponseDTO> response = restTemplate.exchange(accessTokenUrl,
                    HttpMethod.POST,
                    new HttpEntity<>(headers),
                    LoginResponseDTO.class);

            // Access Token is updated
            if (response.getBody().getSecret() != null) {
                setAccessToken(response.getBody().getSecret());
                String expiration = dateFormat.format(response.getBody().getExpiration());
                LOGGER.info("ACCESS TOKEN - SERVICE WAS REACHED - TOKEN WAS RENEWED AND EXPIRES ON = {}", expiration);
            } else {
                LOGGER.info("ACCESS TOKEN - SERVICE WAS REACHED - SECRET WAS NOT RETURNED BY SERVICE?");
            }
        } catch (ResourceAccessException e) {
            LOGGER.info("ACCESS TOKEN - SERVICE COULD NOT BE REACHED");
        }
    }

    // TODO apply individual timers on each Read Operation
    @Scheduled(cron = "${scheduler.update.read.cache.cron.timer}")
    public void refreshReadCache() {
        // Worker obtains all read operations and tries to refresh their Caches
        LOGGER.info("READ CACHE - WORKER WOKE UP - ATTEMPTING TO REFRESH READ CACHE ENTRIES");
        List<ReadOperationEntity> readOperations = readOperationService.listReadOperations();
        if (readOperations.isEmpty()) {
            LOGGER.info("READ CACHE - THERE ARE NO READ OPERATIONS IN THE DATABASE");
        }
        for (ReadOperationEntity readOperation : readOperations) {
            try {
                readOperationService.updateReadOperationCache(readOperation.getResource());
                LOGGER.info("READ CACHE - SUCCESSFUL UPDATE FOR RESOURCE = {}", readOperation.getResource());
            } catch (ReadOperationException | ResourceAccessException | HttpClientErrorException e) {
                LOGGER.info("READ CACHE - FAILED UPDATE FOR RESOURCE = {}", readOperation.getResource());
                LOGGER.error(e.toString());
            }
        }

        LOGGER.info("READ CACHE - WORKER GOES TO SLEEP");
    }

    @Scheduled(cron = "${scheduler.execute.write.cache.cron.timer}")
    public void executeWriteCache() {
        LOGGER.info("");
        LOGGER.info("STARTING EXECUTE WRITE CACHE METHOD");

        // Worker obtains all write caches with AWAITING state and tries to execute all of them
        LOGGER.info("WRITE CACHE - WORKER WOKE UP - ATTEMPTING TO RUN REQUESTS IN AWAITING STATE");
        List<WriteCacheEntity> writeCaches = writeCacheService.listWriteCachesByState(WriteCacheStateEnum.AWAITING);
        if (writeCaches.isEmpty()) {
            LOGGER.info("WRITE CACHE - THERE ARE NO AWAITING REQUESTS IN THE DATABASE");
        }
        for (WriteCacheEntity writeCache : writeCaches) {
            try {
                // Write Cache request is executed, on success request state changes to SUCCESS
                writeCacheService.executeWriteCache(writeCache);
                LOGGER.info("WRITE CACHE - SUCCESSFUL WRITE OPERATION FOR PAIR (RESOURCE, ID) = ({}, {})", writeCache.getResource(), writeCache.getId());
            } catch (ResourceAccessException e) {
                // Connection to Backend is unavailable, Worker goes to sleep and skips calling other cache requests,
                // request state stays as AWAITING
                LOGGER.info("WRITE CACHE - COULD NOT REACH SERVER - WORKER STOPS AND GOES TO SLEEP");
                return;
            } catch (RestClientResponseException e) {
                // Request error, response is built and saved and Cache state changes to ERROR
                LOGGER.info("WRITE CACHE - FAILED WRITE OPERATION FOR PAIR (RESOURCE, ID) = ({}, {})", writeCache.getResource(), writeCache.getId());
                LOGGER.error(e.toString());
                ResponseEntity<String> response = new ResponseEntity<>(e.getResponseBodyAsString(),
                                                                             e.getResponseHeaders(),
                                                                             HttpStatus.resolve(e.getRawStatusCode()));
                ResponseCacheEntity responseCache = new ResponseCacheEntity(response);

                writeCacheService.updateWriteCacheResponse(writeCache.getId(), responseCache, WriteCacheStateEnum.ERROR);
            }
        }
        LOGGER.info("WRITE CACHE - WORKER GOES TO SLEEP");
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