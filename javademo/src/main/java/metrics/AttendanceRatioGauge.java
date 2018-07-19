package metrics;

import com.codahale.metrics.RatioGauge;

/**
 * http://www.baeldung.com/dropwizard-metrics
 */
public class AttendanceRatioGauge extends RatioGauge {

    private int attendanceCount;
    private int courceCount;

    @Override
    protected Ratio getRatio() {
        return Ratio.of(attendanceCount, courceCount);
    }

    public AttendanceRatioGauge(int attendanceCount, int courceCount) {
        this.attendanceCount = attendanceCount;
        this.courceCount = courceCount;
    }
}
