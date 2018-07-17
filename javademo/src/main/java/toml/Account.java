package toml;

import java.util.List;

/**
 * @author zacconding
 * @Date 2018-07-10
 * @GitHub : https://github.com/zacscoding
 */
public class Account {

    private List<String> unlock;
    // private int keys_iterations;
    private int keysIterations;

    public List<String> getUnlock() {
        return unlock;
    }

    public void setUnlock(List<String> unlock) {
        this.unlock = unlock;
    }

    public int getKeysIterations() {
        return keysIterations;
    }

    public void setKeysIterations(int keysIterations) {
        this.keysIterations = keysIterations;
    }
}
