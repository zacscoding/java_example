package org.jerseydemo.filter;

import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MediaType;

/**
 * @author zacconding
 * @Date 2018-01-31
 * @GitHub : https://github.com/zacscoding
 */
public class CharsetResponseFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext request, ContainerResponseContext response) throws IOException {
        MediaType contentType = response.getMediaType();
        response.getHeaders().putSingle("Content-Type", contentType.toString() + ";charset=UTF-8");
    }
}
