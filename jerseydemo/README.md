# Jersey + Jetty Demo

### index

- <a href="#project">Init Project (echo server)</a>
- <a href="#rest-api"> Rest API Server And Client</a>


### Ref

- https://jersey.github.io/documentation/latest/index.html
- https://howtodoinjava.com/jersey-jax-rs-tutorials/
- https://www.mkyong.com/webservices/jax-rs/jersey-hello-world-example/


<div id="init"></div>

#### Init Project

> Maven Dependency : pom.xml

```
...
<properties>
    <jersey.version>2.7</jersey.version>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
</properties>
...

<dependencies>
      <!-- jetty -->
      <dependency>
          <groupId>org.eclipse.jetty</groupId>
          <artifactId>jetty-server</artifactId>
          <version>9.2.3.v20140905</version>
      </dependency>
      <dependency>
          <groupId>org.eclipse.jetty</groupId>
          <artifactId>jetty-servlet</artifactId>
          <version>9.2.3.v20140905</version>
      </dependency>
      <!-- jersey -->
      <dependency>
          <groupId>org.glassfish.jersey.core</groupId>
          <artifactId>jersey-server</artifactId>
          <version>${jersey.version}</version>
      </dependency>
      <dependency>
          <groupId>org.glassfish.jersey.containers</groupId>
          <artifactId>jersey-container-servlet-core</artifactId>
          <version>${jersey.version}</version>
      </dependency>
      <dependency>
          <groupId>org.glassfish.jersey.containers</groupId>
          <artifactId>jersey-container-jetty-http</artifactId>
          <version>${jersey.version}</version>
      </dependency>
      <dependency>
          <groupId>org.glassfish.jersey.media</groupId>
          <artifactId>jersey-media-moxy</artifactId>
          <version>${jersey.version}</version>
      </dependency>
      <dependency>
          <groupId>org.glassfish.jersey.media</groupId>
          <artifactId>jersey-media-json-jackson</artifactId>
          <version>${jersey.version}</version>
      </dependency>
      ....
</dependency>
```

> Build  : pom.xml  

1. $mvn clean package  
2. $cd target/  
3. $java -jar jersey-demo.jar  

```
<build>
    <finalName>jersey-demo</finalName>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>2.5.1</version>
            <configuration>
                <source>1.8</source>
                <target>1.8</target>
                <compilerArgument>-Xlint:all</compilerArgument>
                <showWarnings>true</showWarnings>
                <showDeprecation>true</showDeprecation>
            </configuration>
        </plugin>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-shade-plugin</artifactId>
            <version>1.6</version>
            <configuration>
                <createDependencyReducedPom>true</createDependencyReducedPom>
                <filters>
                    <filter>
                        <artifact>*:*</artifact>
                        <excludes>
                            <exclude>META-INF/*.SF</exclude>
                            <exclude>META-INF/*.DSA</exclude>
                            <exclude>META-INF/*.RSA</exclude>
                        </excludes>
                    </filter>
                </filters>
            </configuration>

            <executions>
                <execution>
                    <phase>package</phase>
                    <goals>
                        <goal>shade</goal>
                    </goals>
                    <configuration>
                        <transformers>
                            <transformer
                                implementation="org.apache.maven.plugins.shade.resource.ServicesResourceTransformer"/>
                            <transformer
                                implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                                <manifestEntries>
                                    <Main-Class>org.jerseydemo.app.App</Main-Class>
                                </manifestEntries>
                            </transformer>
                        </transformers>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

> App.java(Main class for jetty servlet container & jersey servlet)  

```
package org.jerseydemo.app;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class App {

    private static final int DEFAULT_PORT = 8090;
    private static final String CONTEXT_PATH = "/";

    public static void main(String[] args) throws Exception {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath(CONTEXT_PATH);

        Server jettyServer = new Server(DEFAULT_PORT);
        jettyServer.setHandler(context);

        ServletHolder jerseyServlet = context.addServlet(org.glassfish.jersey.servlet.ServletContainer.class, "/*");
        jerseyServlet.setInitOrder(1);
        jerseyServlet.setInitParameter("javax.ws.rs.Application", ResourceLoader.class.getCanonicalName());

        try {
            jettyServer.start();
            jettyServer.join();
        } finally {
            jettyServer.destroy();
        }
    }
}
```

> ResourceLoader (for Jersey config)  

```
package org.jerseydemo.app;

import org.glassfish.jersey.server.ResourceConfig;

public class ResourceLoader extends ResourceConfig {

    public ResourceLoader() {
        System.out.println("## ResourceLoader is called");
        packages("org.jerseydemo.rest");
    }
}
```

> EchoService

```$xslt
package org.jerseydemo.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("/echo")
public class EchoService {
    @GET
    @Path("/{message}")
    public Response getMsg(@PathParam("message") String msg) {
        String output = "echo : " + msg;
        return Response.status(Response.Status.OK).entity(output).build();
    }
}
```

> Result

![echo-server-result](./pics/1.echo-result.png)

---

<div id="rest-api"></div>

#### Rest API  

[server-source-code](https://github.com/zacscoding/java_example/blob/master/jerseydemo/src/main/java/org/jerseydemo/rest/PersonService.java)  

[client-source-code](https://github.com/zacscoding/java_example/blob/master/jerseydemo/src/test/java/org/jerseydemo/client/PersonClientTest.java)



> Rest API  

<table>
  <tr>
    <td>Method</td> <td>URI</td> <td>Description</td>
  </tr>
  <tr>
    <td>GET</td>
    <td>/person</td>
    <td>get person all</td>
  </tr>
  <tr>
    <td>GET</td>
    <td>/person/{id}</td>
    <td>get one person with id</td>
  </tr>
  <tr>
    <td>POST</td>
    <td>/person</td>
    <td>add person</td>
  </tr>
  <tr>
    <td>PUT</td>
    <td>/person/{id}</td>
    <td>modify person</td>
  </tr>
  <tr>
    <td>DELETE</td>
    <td>/person/{id}</td>
    <td>delete person with id</td>
  </tr>
  <tr>
    <td>DELETE</td>
    <td>/person</td>
    <td>delete person all</td>
  </tr>
</table>  

> pom.xml for json

```
<!-- https://mvnrepository.com/artifact/org.glassfish.jersey.media/jersey-media-json-jackson -->
<dependency>
    <groupId>org.glassfish.jersey.media</groupId>
    <artifactId>jersey-media-json-jackson</artifactId>
    <version>2.26</version>
</dependency>
```

> Domain : Person.java

```
package org.jerseydemo.domain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlRootElement
public class Person {
    private static final Gson gson = new GsonBuilder().serializeNulls().create();
    private String id;
    private String name;
    private int age;
    private String job;

    @Override
    public String toString() {
        return gson.toJson(this);
    }
}
```  

> Person Rest Api Service  

```
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

// path
@Path("/person")
// for Singleton
@Singleton
public class PersonService {
    private static List<Person> persons = new ArrayList<>();
    private Gson gson = new GsonBuilder().serializeNulls().create();

    public PersonService() {
        System.out.println("## PersonService insteance created");
    }
    ...
}
```

> Find all  

```
@GET
@Produces(MediaType.APPLICATION_JSON)
public Response getPersons() {
    SimpleLogger.println("## request person all size : " + persons.size());
    GenericEntity<List<Person>> personEntity = new GenericEntity<List<Person>>(persons){};
    return Response.status(Response.Status.OK).entity(personEntity).build();
}
```

> Find one by id  

```
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
```

> Add Person

```
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
```  

> Modify person

```
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
 ```

 > Delete one person with id

 ```
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
```

> Delete person all

```
@DELETE
public Response removePersonAll() {
   int size = persons.size();
   if(size > 0) {
       persons.clear();
   }
   return Response.status(Status.OK).entity("Success to remove all size : " + size).build();
}

```

#### Client side  

> Before setUp

```
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
import org.junit.Before;
import org.junit.Test;

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
    ...
}
```

> Add Person Client  

```
@Test
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
  ...
}

public Response addPerson(Person p) {
  WebTarget target = client.target(url);
  Invocation.Builder invokeBuilder = target.request(MediaType.APPLICATION_JSON);
  return invokeBuilder.post(Entity.entity(p, MediaType.APPLICATION_JSON));
}
```

> Find All Client  

```
@Test
public void test() {
  ...
  Response response = findAll();
  List<Person> find = response.readEntity(new GenericType<List<Person>>(){});
  assertTrue(find.size() == 3);
  SimpleLogger.println("## request find all result : {}", new Gson().toJson(find));
  ...
}

public Response findAll() {
  WebTarget target = client.target(url);
  Invocation.Builder invokeBuilder = target.request();
  return invokeBuilder.get();
  // client.target(url).request().get();
}
```

> Find One Client  

```
@Test
public void test() {
  ...
  response = findOneById(id + 1);
  Person findOne = response.readEntity(Person.class);
  assertThat(id+1, is(findOne.getId()));
  SimpleLogger.println("## request find one result : {}", findOne);
  ...
}

public Response findOneById(String id) {
  return client.target(url).path(id).request().get();  
}
```

> Modify Client  

```
@Test
public void test() {
  ...
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
  ...
}

public Response modify(Person person) {
  WebTarget target = client.target(url).path(person.getId());
  return target.request(MediaType.APPLICATION_JSON).put(Entity.entity(person, MediaType.APPLICATION_JSON));
}
```

> Delete Person Client  

```
@Test
public void test() {
  ...
  response = remove(id + 1);
  assertTrue(response.getStatus() == Response.Status.OK.getStatusCode());
  SimpleLogger.println("## request delete result : {}", response.readEntity(String.class));
  ...
}

public Response remove(String id) {
  WebTarget target = client.target(url).path(id);
  return target.request().delete();
}
```
---
























<br /><br /><br /><br /><br /><br />

---
