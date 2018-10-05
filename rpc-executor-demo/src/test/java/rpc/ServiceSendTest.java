package rpc;

import demo.protocol.http.HttpRpcService;
import demo.protocol.websocket.WebSocketService;
import java.io.IOException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author zacconding
 * @Date 2018-10-05
 * @GitHub : https://github.com/zacscoding
 */
public class ServiceSendTest {

    private HttpRpcService httpRpcService;
    private WebSocketService webSocketService;

    @Before
    public void setUp() throws Exception {
        String url = "http://192.168.5.78";
        httpRpcService = new HttpRpcService(url + ":8540");
        webSocketService = new WebSocketService(url + ":9540");
        webSocketService.connect();
    }

    @After
    public void tearDown() throws IOException {
        webSocketService.close();
    }

    @Test
    public void sendTest() throws Exception {
        System.out.println(">> start");
        String request = "{\"method\":\"eth_blockNumber\",\"params\":[],\"id\":1,\"jsonrpc\":\"2.0\"}";

        System.out.println(">> Try to send websocket \n" + request);
        String result = webSocketService.send(request);
        System.out.println(">> Success to reply : " + result);

        request = "{\"method\":\"eth_blockNumber\",\"params\":[],\"id\":\"@@!!id!!@@\",\"jsonrpc\":\"2.0\"}";
        System.out.println(">> Try to send http service \n" + request);
        result = httpRpcService.send(request);
        System.out.println(">> Success to reply : " + result);
    }
}
