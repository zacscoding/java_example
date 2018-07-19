package basic;

import util.SimpleLogger;

/**
 * @author zacconding
 * @Date 2018-07-19
 * @GitHub : https://github.com/zacscoding
 */
public class DatabaseTransactionLog implements TransactionLog {

    public DatabaseTransactionLog() {
        SimpleLogger.println("DatabaseTransactionLog is created : {}", this);
    }
}
