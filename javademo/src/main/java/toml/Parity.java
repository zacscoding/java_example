package toml;

/**
 * @author zacconding
 * @Date 2018-07-10
 * @GitHub : https://github.com/zacscoding
 */
public class Parity {

    private String mode;
    private long mode_timeout;

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public long getMode_timeout() {
        return mode_timeout;
    }

    public void setMode_timeout(long mode_timeout) {
        this.mode_timeout = mode_timeout;
    }
}
