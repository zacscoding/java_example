package org.jerseydemo.app;

import org.glassfish.jersey.server.ResourceConfig;

/**
 * @author zacconding
 * @Date 2018-01-31
 * @GitHub : https://github.com/zacscoding
 */
public class ResourceLoader extends ResourceConfig {

    public ResourceLoader() {
        System.out.println("## ResourceLoader is called");
        packages("org.jerseydemo.rest");
    }
}
