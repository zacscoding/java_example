import java.util.List;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.util.TablesNamesFinder;
import util.SimpleLogger;

/**
 * @author zacconding
 * @Date 2018-02-09
 * @GitHub : https://github.com/zacscoding
 */
public class TableNameFindTest {

    public static void main(String[] args) {
        String[] sqls = {
            "SELECT * FROM table1",
            "UPDATE table1 SET name = 1 where id = 2",
            "select table1.*, table2.seq as table2_seq, table2.name as table2_name, table2.parent_seq as\ttable2_parent_seq\t\n"
                + "\t\t\tfrom table1 table1 \n"
                + "\t\tLEFT JOIN table1_table2_manage_relation relation \n"
                + "\t\t\tON table1.seq = relation.table1_fk\n"
                + "\t\tLEFT JOIN table2ment table2 \n"
                + "\t\t\tON table2.seq = relation.dep_fk"
        };

        try {
            for (String sql : sqls) {
                SimpleLogger.println("## Check " + sql);
                Statement stmt = CCJSqlParserUtil.parse(sql);
                TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
                if (stmt instanceof Select) {
                    SimpleLogger.println("can cast to Select");
                } else if (stmt instanceof Update) {
                    SimpleLogger.println("can cast to Update");
                } else if (stmt instanceof Delete) {
                    SimpleLogger.println("can cast to Delete");
                } else if (stmt instanceof Insert) {
                    SimpleLogger.println("can cast to Insert");
                }

                displayList(tablesNamesFinder.getTableList(stmt));
            }
        } catch (JSQLParserException e) {
            e.printStackTrace();
        }
    }

    private static <T> void displayList(List<T> list) {
        if (list == null || list.size() == 0) {
            SimpleLogger.println("list is empty");
        }

        for (int i = 0; i < list.size(); i++) {
            SimpleLogger.println("{} : {}", i, list.get(i));
        }
    }
}
