package org.jerseydemo.client;

import javax.ws.rs.Path;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.jerseydemo.util.SimpleLogger;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author zacconding
 * @Date 2018-01-30
 * @GitHub : https://github.com/zacscoding
 */
public class PostRequestTest {

    @Test
    @Ignore
    public void postRequest() {
        String message = "test-message";
        String url = "http://localhost:8080/rest";

        // Client client = ClientBuilder.newClient(new ClientConfig().register(LoggingFilter.class));
        Client client = ClientBuilder.newClient();

        WebTarget webTarget = client.target(url).path("echo").path(message);
        SimpleLogger.println("## request uri : {}", webTarget.getUri());

        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.post(Entity.entity("Test Body", MediaType.APPLICATION_JSON));
        String result = response.readEntity(String.class);
        // SimpleLogger.println("## response : {}, result : {}", new Gson().toJson(response), result);
        SimpleLogger.println("## result : {}", result);
    }
}
