package test.action;

/**
 * @author zacconding
 * @Date 2018-08-14
 * @GitHub : https://github.com/zacscoding
 */
public class Action {

    private String id;
    private String serviceName;
    private String actionType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    @Override
    public String toString() {
        return "Action{" + "id='" + id + '\'' + ", serviceName='" + serviceName + '\'' + ", actionType='" + actionType + '\'' + '}';
    }
}
