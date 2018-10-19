package rpc;

import demo.protocol.RpcService;
import demo.protocol.ipc.WindowsIpcService;
import java.io.IOException;
import org.junit.Test;

/**
 * @author zacconding
 * @Date 2018-10-19
 * @GitHub : https://github.com/zacscoding
 */
public class IpcServiceTest {

    @Test
    public void sendByIpcService() throws IOException {
        String ipcPath = "\\\\.\\pipe\\node.ipc";

        RpcService service = new WindowsIpcService(ipcPath);
        String rawRequeste = "{\"jsonrpc\":\"2.0\",\"method\":\"admin_nodeInfo\",\"params\":[],\"id\":0}";

        String rawResponse = service.send(rawRequeste);
        System.out.println(rawResponse);
        // {"jsonrpc":"2.0","id":0,"result":{"id":"edb68f29e6350682550e4f2011e6dcf6ce4118d824c6344ed557c6418852fc36","name":"Geth/v1.8.17-stable-8bbe7207/windows-amd64/go1.11.1","enode":"enode://9e93ecf837f67ed0db9e01cabb59d3b776f42fb6aef47a3187a39a4b96e77bdffa435259efadce1e3050a3793580edd5ef627585ccdfc4fa234a1590a3975988@[::]:30311","ip":"::","ports":{"discovery":30311,"listener":30311},"listenAddr":"[::]:30311","protocols":{"eth":{"network":1234,"difficulty":689,"genesis":"0xedfc0ba8fd49af9ed497a753f028581f483d13d612690706109b61bba7c3b395","config":{"chainId":1234,"homesteadBlock":1,"eip150Block":2,"eip150Hash":"0x0000000000000000000000000000000000000000000000000000000000000000","eip155Block":3,"eip158Block":3,"byzantiumBlock":4,"clique":{"period":15,"epoch":30000}},"head":"0x73db0a5e9a6f419f3b0b185ceb06336758bcaf6b84f654d2fd2d7f61c6ed743b"}}}}
    }
}