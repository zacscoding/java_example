package rpc;

import demo.protocol.IRpcService;
import demo.protocol.http.HttpRpcService;
import demo.protocol.ipc.IpcService;
import demo.protocol.ipc.WindowsIpcService;
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
    private IpcService ipcService;

    @Before
    public void setUp() throws Exception {
        String url = "http://192.168.5.78";
        httpRpcService = new HttpRpcService(url + ":8540");
        /*webSocketService = new WebSocketService(url + ":9540");
        webSocketService.connect();*/
        ipcService = new WindowsIpcService("\\\\.\\pipe\\node.ipc");
    }

    @After
    public void tearDown() throws IOException {
        IRpcService[] services = {webSocketService, httpRpcService, ipcService};

        for (IRpcService service : services) {
            if (service != null) {
                service.close();
            }
        }
    }

    @Test
    public void sendTestByConsole() {
        // websocket test
        String request = "{\"method\":\"eth_blockNumber\",\"params\":[],\"id\":1,\"jsonrpc\":\"2.0\"}";
        sendTest(webSocketService, request);

        // http
        request = "{\"method\":\"eth_blockNumber\",\"params\":[],\"id\":\"@@!!id!!@@\",\"jsonrpc\":\"2.0\"}";
        sendTest(httpRpcService, request);

        // ipc
        request = "{\"jsonrpc\":\"2.0\",\"method\":\"admin_nodeInfo\",\"params\":[],\"id\":0}";
        sendTest(ipcService, request);
    }

    private void sendTest(IRpcService service, String request) {
        try {
            System.out.println("> Try to send service. " + request.getClass().getName() + "\n" + request);
            String reply = service.send(request);
            System.out.println(">> Success to reply.\n" + reply);
        } catch (Exception e) {
            System.out.println(">> Failed to response. " + e.getMessage());
        }

    }
}
