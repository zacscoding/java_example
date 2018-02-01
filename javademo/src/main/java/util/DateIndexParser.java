package util;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Period;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * ElasticSearch Date Index Parse Test
 * Support that elastic search`s indices is generated every day such as 180101, 180102, 180103
 * ## Search1)  170617 ~ 170617
 * => ["20170617"]
 *
 * ## Search2) 170617 ~ 171213
 * => ["1706*", "1707*", "1708*", "1709*", "1710*", "1711*", "1712*"]
 *
 * ## Search 3) 160811 ~ 180102
 * => [ "1611*", "1612*", "17*", "1801*" ]
 *
 * @author zacconding
 * @Date 2018-01-18
 * @GitHub : https://github.com/zacscoding
 */
public class DateIndexParser {
    public String[] getIndices(String fromVal, String toVal, String pattern, String defaultValue) {
        try {
            org.joda.time.format.DateTimeFormatter format = org.joda.time.format.DateTimeFormat.forPattern(pattern);
            DateTime from = format.parseDateTime(fromVal);
            DateTime to = format.parseDateTime(toVal);

            if(to.isBefore(from)) {
                throw new Exception();
            }

            if(from.getYear() == to.getYear()) {

            }
            else {

            }


            Interval interval = new Interval(from,to);

            return null;
        }
        catch(Throwable t) {
            t.printStackTrace();
            return new String[]{defaultValue};
        }
    }

    public static void main(String[] args) {
        String pattern = "yyMMdd";
        int lastYearIdx = pattern.lastIndexOf('y');
        String wildCardPattern = pattern.substring(0, lastYearIdx) + "*";
        SimpleDateFormat sdf = new SimpleDateFormat(wildCardPattern);
        System.out.println(sdf.format("2017"));
    }
}
