package eu.panosc.portal.cloud;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.panosc.portal.cloud.http.HttpClient;
import eu.panosc.portal.cloud.http.HttpException;
import eu.panosc.portal.cloud.http.HttpMethod;
import eu.panosc.portal.cloud.http.HttpResponse;
import eu.panosc.portal.cloud.http.clients.OkHttpClientAdapter;
import eu.panosc.portal.core.domain.InstanceAuthorisation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.io.IOException;

import static java.lang.String.format;

@Singleton
public class CloudService {

    private final static Logger logger = LoggerFactory.getLogger(CloudService.class);

    private final HttpClient httpClient = new OkHttpClientAdapter();
    private ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private final String cloudServiceEndpoint;

    public CloudService(String cloudServiceEndpoint) {
        this.cloudServiceEndpoint = cloudServiceEndpoint;
    }

    public InstanceAuthorisation validateToken(Integer instanceId, String token) {

        try {
            final String url = format("%s/instances/%d/token/%s/validate", this.cloudServiceEndpoint, instanceId, token);

            final HttpResponse response = httpClient.sendRequest(url, HttpMethod.GET);
            if (response.isSuccessful()) {
                final InstanceAuthorisation instanceAuthorisation = this.mapper.readValue(response.getBody(), InstanceAuthorisation.class);
                return instanceAuthorisation;

            } else {
                logger.error("Response from Cloud Service for instance authorisation was not successful");
            }

        } catch (IOException e) {
            logger.error("Cannot deserialize Cloud Service response for instance authorisation: {}", e.getMessage());

        } catch (HttpException e) {
            logger.error("Caught HttpException while getting instance authorisation from Cloud Service: {}", e.getMessage());
        }

        return null;
    }

}
