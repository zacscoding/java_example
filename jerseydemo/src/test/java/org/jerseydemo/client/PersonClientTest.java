package org.jerseydemo.client;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.google.gson.Gson;
import java.util.List;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.jerseydemo.domain.Person;
import org.jerseydemo.util.SimpleLogger;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author zacconding
 * @Date 2018-01-30
 * @GitHub : https://github.com/zacscoding
 */
public class PersonClientTest {

    String url;
    Client client;

    @Before
    public void setUp() {
        url = "http://localhost:8080/rest/person";
        client = ClientBuilder.newClient();
        WebTarget deleteAllTarget = client.target(url);
        Invocation.Builder invokeBuilder = deleteAllTarget.request();
        Response response = invokeBuilder.delete();
        assertTrue(response.getStatus() == Response.Status.OK.getStatusCode());
    }

    @After
    public void tearDown() {
        if (client != null) {
            client.close();
        }
    }

    @Test
    @Ignore
    public void test() {
        final String id = "id";
        final String name = "person";
        final String job = "job";

        /*  Save And Find all*/
        for (int i = 1; i <= 3; i++) {
            Person p = new Person();
            p.setId(id + i);
            p.setName(name + i);
            p.setJob(job + i);
            p.setAge(i);
            // when
            Response response = addPerson(p);
            // then
            assertTrue(response.getStatus() == Response.Status.OK.getStatusCode());
            SimpleLogger.println("## request save result : {}", response.readEntity(String.class));
        }

        /*  Find all  */
        Response response = findAll();
        List<Person> find = response.readEntity(new GenericType<List<Person>>() {
        });
        assertTrue(find.size() == 3);
        SimpleLogger.println("## request find all result : {}", new Gson().toJson(find));

        /*  Find by id  */
        response = findOneById(id + 1);
        Person findOne = response.readEntity(Person.class);
        assertThat(id + 1, is(findOne.getId()));
        SimpleLogger.println("## request find one result : {}", findOne);

        /*  Modify */
        // given
        Person p = new Person();
        p.setId(id + 2);
        String modifiedName = "modifiedName";
        p.setName(modifiedName);

        // when
        response = modify(p);
        // then
        assertTrue(response.getStatus() == Response.Status.OK.getStatusCode());
        SimpleLogger.println("## request modify result : {}", response.readEntity(String.class));
        response = findOneById(p.getId());
        findOne = response.readEntity(Person.class);
        assertThat(p.getName(), is(findOne.getName()));
        SimpleLogger.println("## request find one result : {}", findOne);

        // given
        p.setId(id + 10);
        // when
        response = modify(p);
        // then
        assertTrue(response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode());
        SimpleLogger.println("## request modify with bad request result : {}", response.readEntity(String.class));

        /*  DeletePerson */
        response = remove(id + 1);
        assertTrue(response.getStatus() == Response.Status.OK.getStatusCode());
        SimpleLogger.println("## request delete result : {}", response.readEntity(String.class));

        response = findAll();
        find = response.readEntity(new GenericType<List<Person>>() {
        });
        assertTrue(find.size() == 2);
        SimpleLogger.println("## request find all result : {}", new Gson().toJson(find));
    }

    public Response addPerson(Person p) {
        WebTarget target = client.target(url);
        Invocation.Builder invokeBuilder = target.request(MediaType.APPLICATION_JSON);
        return invokeBuilder.post(Entity.entity(p, MediaType.APPLICATION_JSON));
    }

    public Response findAll() {
        client.target(url).request().get();
        WebTarget target = client.target(url);
        Invocation.Builder invokeBuilder = target.request();
        return invokeBuilder.get();
    }

    public Response findOneById(String id) {
        WebTarget target = client.target(url).path(id);
        return target.request().get();
    }

    public Response modify(Person person) {
        WebTarget target = client.target(url).path(person.getId());
        return target.request(MediaType.APPLICATION_JSON).put(Entity.entity(person, MediaType.APPLICATION_JSON));
    }

    public Response remove(String id) {
        WebTarget target = client.target(url).path(id);
        return target.request().delete();
    }
}
