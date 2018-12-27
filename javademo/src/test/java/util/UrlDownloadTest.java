package util;

import java.io.File;
import java.io.IOException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import org.apache.commons.io.FileUtils;

/**
 * @author zacconding
 * @Date 2018-12-28
 * @GitHub : https://github.com/zacscoding
 */
public class UrlDownloadTest {

    public static void main(String[] args) {

    }

    private static void copyURLToFile(URL source, File destination, int connectionTimeout, int readTimeout,
            Proxy proxy) throws IOException {

        final URLConnection connection = proxy != null ? source.openConnection(proxy) : source.openConnection();
        connection.setConnectTimeout(connectionTimeout);
        connection.setReadTimeout(readTimeout);

        FileUtils.copyInputStreamToFile(connection.getInputStream(), destination);
    }
}
