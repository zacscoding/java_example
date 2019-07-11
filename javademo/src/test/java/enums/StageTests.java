package enums;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.stream.IntStream;
import org.junit.Test;

/**
 *
 */
public class StageTests {

    @Test
    public void testStageEnums() {
        List<InstallStage> stages = InstallStage.getStages();

        InstallStage prevStage = null;
        int remain = stages.size();

        for (int i = 0; i < stages.size(); i++) {
            InstallStage currentStage = stages.get(i);

            if (i == 0) {
                assertTrue(currentStage == currentStage.getFirstStage());
                assertTrue(currentStage == currentStage.downgrade());
            } else if (i == stages.size() - 1) {
                assertTrue(currentStage == currentStage.getLastStage());
                assertTrue(currentStage == currentStage.upgradeTo());
            } else {
                assertTrue(currentStage.isGraterThan(prevStage));
                assertTrue(currentStage.downgrade() == prevStage);
            }
            prevStage = currentStage;
            remain--;
        }

        assertTrue(remain == 0);
    }
}
