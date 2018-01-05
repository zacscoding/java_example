package org.jerseydemo.app;

import org.glassfish.jersey.server.ResourceConfig;

/**
 * @author zacconding
 * @Date 2018-01-06
 * @GitHub : https://github.com/zacscoding
 */
public class DemoApplication extends ResourceConfig{
    public DemoApplication() {
        packages("org.jerseydemo.rest");
    }
}
