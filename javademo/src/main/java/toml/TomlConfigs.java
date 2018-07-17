package toml;

/**
 * @author zacconding
 * @Date 2018-07-10
 * @GitHub : https://github.com/zacscoding
 */
public class TomlConfigs {

    private Parity parity;
    private Account account;

    public Parity getParity() {
        return parity;
    }

    public void setParity(Parity parity) {
        this.parity = parity;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
