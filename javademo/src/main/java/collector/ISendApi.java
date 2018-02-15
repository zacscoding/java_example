package collector;

import java.util.List;

/**
 * Rest API interface
 *
 * @author zacconding
 * @Date 2018-02-11
 * @GitHub : https://github.com/zacscoding
 */
public interface ISendApi {

    public void sendData(String data);

    public void sendDatas(List<String> data);
}
