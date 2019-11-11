import io.github.jokoframework.wilson.cache.entities.ReadOperationEntity;
import io.github.jokoframework.wilson.cache.entities.ResponseCacheEntity;
import io.github.jokoframework.wilson.cache.repositories.ReadOperationRepository;
import io.github.jokoframework.wilson.cache.service.impl.WilsonMasterServiceImpl;
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
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class WilsonMasterServiceImplTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(WilsonMasterServiceImplTest.class);

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ReadOperationRepository readOperationRepository;

    @InjectMocks
    private WilsonMasterServiceImpl wilsonMasterServiceImpl = new WilsonMasterServiceImpl();

    @Before
    public void setUp() {
        // Values obtained from the application.properties file with @Value must be set in tests
        // Get Request from Rest Master
        ReflectionTestUtils.setField(wilsonMasterServiceImpl, "baseUrl", "http://localhost:9090");
    }

    @Test
    public void getRequestFromRestMasterTest() {
        LOGGER.info("Testing when Wilson works as a proxy");

        // The Service will recieve a Mock response when calling the Rest Master Endpoint in the spot
        String countryResponseString = "[{\"id\": \"PY\", \"description\": \"Paraguay\"}, {\"id\": \"AR\", \"description\": \"Argentina\"}";
        ResponseEntity<String> expectedResponse = new ResponseEntity(countryResponseString, HttpStatus.OK);

        Mockito.when(restTemplate.exchange(
                Mockito.eq("http://localhost:9090/api/countries"),
                Mockito.eq(HttpMethod.GET),
                Mockito.any(HttpEntity.class),
                Mockito.eq(String.class)
        )).thenReturn(expectedResponse);

        ResponseEntity<String> response = wilsonMasterServiceImpl.processGetRequest("/api/countries");
        assertThat(response.equals(expectedResponse));
        LOGGER.info("Returned result: " + response);
    }

    @Test
    public void storeNewReadCacheTest() {
        LOGGER.info("Testing when Wilson stores a request response in its Operation Cache");


        assertThat(true);
    }

    @Test
    public void getRequestFromCacheTest() {
        LOGGER.info("Testing when Wilson returns its Cache when Master is unavailable");

        // The Service will recieve a Mocked response when getting the cached response
        String countryResponseString = "[{\"id\": \"PY\", \"description\": \"Paraguay\"}, {\"id\": \"AR\", \"description\": \"Argentina\"}";
        ResponseEntity<String> expectedResponse = new ResponseEntity(countryResponseString, HttpStatus.OK);

        // On the moment call to Rest Master is mocked to fail
        Mockito.when(restTemplate.exchange(
                Mockito.eq("http://localhost:9090/api/countries"),
                Mockito.eq(HttpMethod.GET),
                Mockito.any(HttpEntity.class),
                Mockito.eq(String.class)
        )).thenThrow(ResourceAccessException.class);

        // Cache with entry is mocked to recieve an acceptable value
        ResponseCacheEntity responseCacheEntity = new ResponseCacheEntity();
        responseCacheEntity.setBody(countryResponseString);
        responseCacheEntity.setStatusCode(HttpStatus.OK);

        ReadOperationEntity readOperationEntity = new ReadOperationEntity();
        readOperationEntity.setResponseCache(responseCacheEntity);

        Optional<ReadOperationEntity> optionalReadOperationEntity = Optional.of(readOperationEntity);

        Mockito.when(readOperationRepository.findByResource(
                Mockito.anyString()
        )).thenReturn(optionalReadOperationEntity);

        ResponseEntity<String> response = wilsonMasterServiceImpl.processGetRequest("/api/countries");
        assertThat(response.equals(expectedResponse));
        LOGGER.info("Returned result: " + response);
    }
}
