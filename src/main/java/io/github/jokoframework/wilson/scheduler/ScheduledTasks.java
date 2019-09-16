package io.github.jokoframework.wilson.scheduler;

import java.text.SimpleDateFormat;

import io.github.jokoframework.wilson.scheduler.dto.LoginRequestDTO;
import io.github.jokoframework.wilson.scheduler.dto.LoginResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Component
public class ScheduledTasks {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTasks.class);
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Value("${scheduler.login.url}")
    private String loginUrl;
    @Value("${scheduler.login.username}")
    private String loginUsername;
    @Value("${scheduler.login.password}")
    private String loginPassword;

    private static String secret;

    @Scheduled(cron = "${scheduler.login.cron.timer}")
    public void refreshLogin() {
        // We build the request we want to send
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        LoginRequestDTO credentials = new LoginRequestDTO();
        credentials.setUsername(loginUsername);
        credentials.setPassword(loginPassword);


        HttpEntity<LoginRequestDTO> request = new HttpEntity<>(credentials, headers);

        // We make the request, logging response data and managing some types of exceptions
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<LoginResponseDTO> response = restTemplate.exchange(loginUrl,
                    HttpMethod.POST,
                    request,
                    LoginResponseDTO.class);

            // Secret is updated
            if (response.getBody().getSecret() != null) {
                setSecret(response.getBody().getSecret());
                String expiration = dateFormat.format(response.getBody().getExpiration());
                LOGGER.info("LOGIN SERVICE WAS REACHED - REFRESHED SECRET - EXPIRES ON = {}", expiration);
            } else {
                LOGGER.info("LOGIN SERVICE WAS REACHED - SECRET WAS NOT RETURNED BY SERVICE?");
            }
        } catch (ResourceAccessException e) {
            LOGGER.info("LOGIN SERVICE COULD NOT BE REACHED");
        }
    }

    public static void setSecret(String secret) {
        ScheduledTasks.secret = secret;
    }

    public static String getSecret() {
        return secret;
    }
}