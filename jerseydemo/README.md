# Jersey Demo

### index

- <a href="#echo-demo> echo server (basic) </a>

### Ref

- https://howtodoinjava.com/jersey-jax-rs-tutorials/
- https://www.mkyong.com/webservices/jax-rs/jersey-hello-world-example/


<div id="echo-demo"></div>

#### echo server

> Maven Repository

```$xslt
    <!-- https://mvnrepository.com/artifact/com.sun.jersey/jersey-server -->
    <dependency>
      <groupId>com.sun.jersey</groupId>
      <artifactId>jersey-server</artifactId>
      <version>${jersey-version}</version>
    </dependency>

    <!-- https://mvnrepository.com/artifact/com.sun.jersey/jersey-core -->
    <dependency>
      <groupId>com.sun.jersey</groupId>
      <artifactId>jersey-core</artifactId>
      <version>${jersey-version}</version>
    </dependency>

    <dependency>
      <groupId>com.sun.jersey</groupId>
      <artifactId>jersey-servlet</artifactId>
      <version>${jersey-version}</version>
    </dependency>
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

> web.xml

```$xslt
<!DOCTYPE web-app PUBLIC
 "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN"
 "http://java.sun.com/dtd/web-app_2_3.dtd" >

<web-app id="WebApp_ID" version="2.4"
         xmlns="http://java.sun.com/xml/ns/j2ee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://java.sun.com/xml/ns/j2ee
	http://java.sun.com/xml/ns/j2ee/web-app_2_4.xsd">
  <display-name>Restful Web Application</display-name>

  <servlet>
    <servlet-name>jersey-serlvet</servlet-name>
    <servlet-class>com.sun.jersey.spi.container.servlet.ServletContainer</servlet-class>
    <init-param>
      <param-name>com.sun.jersey.config.property.packages</param-name>
      <param-value>org.jerseydemo.rest</param-value>
    </init-param>
    <load-on-startup>1</load-on-startup>
  </servlet>

  <servlet-mapping>
    <servlet-name>jersey-serlvet</servlet-name>
    <url-pattern>/rest/*</url-pattern>
  </servlet-mapping>

</web-app>
```

> Result

![echo-server-result](./pics/1.echo-result.png)


