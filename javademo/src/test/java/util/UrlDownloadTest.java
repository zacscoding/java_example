package util;

import console.PercentagePrinter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;

/**
 * @author zacconding
 * @Date 2018-12-28
 * @GitHub : https://github.com/zacscoding
 */
public class UrlDownloadTest {

    static PrintStream ps; // TEMP

    public static void main(String[] args) throws Exception {
        URL url = new URL("https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-6.5.4.zip");
        File destination = new File("E:\\temp\\elasticsearch-6.5.4.zip");
        // ps = new PrintStream(new FileOutputStream(new File("E:\\temp\\percent.txt"))); // TEMP

        copyURLToFile(url, destination,
            (int) TimeUnit.SECONDS.toMillis(10L), (int) TimeUnit.MINUTES.toMillis(10L), null
        );
    }

    private static void copyURLToFile(URL source, File destination, int connectionTimeout, int readTimeout,
        Proxy proxy) throws IOException {

        final URLConnection connection = proxy != null ? source.openConnection(proxy) : source.openConnection();
        connection.setConnectTimeout(connectionTimeout);
        connection.setReadTimeout(readTimeout);

        /*if (connection instanceof HttpURLConnection) {
            System.out.println("POSSIBLE TO CAST HttpURLConnection");
            ((HttpURLConnection) connection).setRequestMethod("HEAD");
        }*/

        final double fileSize = (double) connection.getContentLength();
        int downloadedSize = 0;

        PercentagePrinter percentagePrinter = new PercentagePrinter();

        try (InputStream in = connection.getInputStream();
            OutputStream out = FileUtils.openOutputStream(destination)) {

            byte[] buffer = new byte[1024 * 4];
            int n;

            while (-1 != (n = in.read(buffer))) {
                out.write(buffer, 0, n);
                downloadedSize += n;
                // ps.println(downloadedSize / fileSize); // TEMP
                percentagePrinter.updateProgress(downloadedSize / fileSize);
            }
        }
    }
}
