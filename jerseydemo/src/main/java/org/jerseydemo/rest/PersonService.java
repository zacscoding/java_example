package org.jerseydemo.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.jerseydemo.domain.Person;
import org.jerseydemo.util.SimpleLogger;

/**
 * @author zacconding
 * @Date 2018-01-30
 * @GitHub : https://github.com/zacscoding
 */
@Path("/person")
@Singleton
public class PersonService {
    private static List<Person> persons = new ArrayList<>();
    private Gson gson = new GsonBuilder().serializeNulls().create();

    public PersonService() {
        System.out.println("## PersonService insteance created");
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPersons() {
        SimpleLogger.println("## request person all size : " + persons.size());
        GenericEntity<List<Person>> personEntity = new GenericEntity<List<Person>>(persons){};
        return Response.status(Response.Status.OK).entity(personEntity).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPerson(@PathParam("id") String id) {
        SimpleLogger.println("## request find person id : {}", id);
        Person find = null;

        for (Person p : persons) {
            if (p.getId().equals(id)) {
                find = p;
            }
        }

        return Response.status(Response.Status.OK).entity(gson.toJson(find)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPerson(Person person) {
        SimpleLogger.println("## [request add person] person : {}", person);
        if (person == null || person.getId() == null) {
            return Response.status(Status.BAD_REQUEST).entity("Please add Person").build();
        } else {
            Person exist = findById(person.getId());
            if (exist != null) {
                return Response.status(Status.BAD_REQUEST).entity("Please add another person id").build();
            }

            persons.add(person);
            return Response.status(Response.Status.OK).entity(person.toString()).build();
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response modifyPerson(@PathParam("id") String id, Person person) {
        if (id == null) {
            return Response.status(Status.BAD_REQUEST).entity("Please add person id").build();
        } else {
            Person saved = findById(id);
            if (saved == null) {
                return Response.status(Status.BAD_REQUEST).entity("There is not person`s id : " + id).build();
            }
            String name = person.getName();
            int age = person.getAge();
            if (name != null) {
                saved.setName(name);
            }
            if (age != 0) {
                saved.setAge(age);
            }
            String job = person.getJob();
            if (job != null) {
                saved.setJob(job);
            }
            return Response.status(Response.Status.OK).entity(saved).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response removePerson(@PathParam("id") String id) {
        SimpleLogger.println("## request remove person id : {}", id);
        if (id == null) {
            return Response.status(Status.BAD_REQUEST).entity("Please add person id").build();
        } else {
            Person deleted = removePersonById(id);
            if(deleted == null) {
                return Response.status(Status.BAD_REQUEST).entity("There is no person where id : "+ id).build();
            } else {
                return Response.status(Status.OK).entity("Success to remove person : " + deleted.toString()).build();
            }
        }
    }

    @DELETE
    public Response removePersonAll() {
        int size = persons.size();
        if(size > 0) {
            persons.clear();
        }
        return Response.status(Status.OK).entity("Success to remove all size : " + size).build();
    }


    private Person removePersonById(String id) {
        for(int i=0; i<persons.size(); i++) {
            Person p = persons.get(i);
            if(p.getId().equals(id)) {
                persons.remove(i);
                return p;
            }
        }

        return null;
    }

    private Person findById(String id) {
        for (Person p : persons) {
            if (p.getId().equals(id)) {
                return p;
            }
        }

        return null;
    }
}
