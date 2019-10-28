import io.github.jokoframework.wilson.scheduler.ScheduledTasks;
import io.github.jokoframework.wilson.scheduler.dto.LoginResponseDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ScheduledTasksTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTasksTest.class);

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ScheduledTasks scheduledTasks = new ScheduledTasks();

    @Before
    public void setUp() {
        // Values obtained from the application.properties file with @Value must be set in tests
        // Login
        ReflectionTestUtils.setField(scheduledTasks, "refreshTokenUrl", "http://localhost:9090/api/login");
        ReflectionTestUtils.setField(scheduledTasks, "loginUsername", "admin");
        ReflectionTestUtils.setField(scheduledTasks, "loginPassword", "123456");
        ReflectionTestUtils.setField(scheduledTasks, "accessTokenUrl", "http://localhost:9090/api/token/user-access");
    }

    @Test
    public void refreshTokensTest() {
        String refreshToken = "This is a mocked refresh token";
        String accessToken = "This is a mocked refresh token";
        LoginResponseDTO refreshTokenResponse = new LoginResponseDTO("true", null, null, refreshToken, new Date());
        LoginResponseDTO accessTokenResponse = new LoginResponseDTO("true", null, null, accessToken, new Date());

        Mockito.when(restTemplate.exchange(
                Mockito.eq("http://localhost:9090/api/login"),
                Mockito.eq(HttpMethod.POST),
                Mockito.any(HttpEntity.class),
                Mockito.eq(LoginResponseDTO.class)
        )).thenReturn(new ResponseEntity(refreshTokenResponse, HttpStatus.OK));

        Mockito.when(restTemplate.exchange(
                Mockito.eq("http://localhost:9090/api/token/user-access"),
                Mockito.eq(HttpMethod.POST),
                Mockito.any(HttpEntity.class),
                Mockito.eq(LoginResponseDTO.class)
        )).thenReturn(new ResponseEntity(accessTokenResponse, HttpStatus.OK));

        LOGGER.info("Testing Refresh Token and Access Token Service");
        scheduledTasks.refreshLogin();

        assertThat(ScheduledTasks.getRefreshToken().equals(refreshToken));
        LOGGER.info("Refresh Token = " + ScheduledTasks.getRefreshToken());
        assertThat(ScheduledTasks.getAccessToken().equals(accessToken));
        LOGGER.info("Access Token = " + ScheduledTasks.getAccessToken());
    }
}
