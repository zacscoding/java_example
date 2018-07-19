package basic;

import util.SimpleLogger;

/**
 * @author zacconding
 * @Date 2018-07-19
 * @GitHub : https://github.com/zacscoding
 */
public class MySqlDatabaseTransactionLog extends DatabaseTransactionLog {

    public MySqlDatabaseTransactionLog() {
        SimpleLogger.println("MySqlDatabaseTransactionLog is created : {}", this);
    }
}
