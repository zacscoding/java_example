import health.HealthCheckMain;
import lombok.extern.slf4j.Slf4j;
import metrics1.Metrics1Main;

/**
 * @author zacconding
 * @Date 2019-01-14
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
public class Main {

    public static void main(String[] args) throws Exception {
        // Metrics1Main.run();

        HealthCheckMain.run();
    }
}
