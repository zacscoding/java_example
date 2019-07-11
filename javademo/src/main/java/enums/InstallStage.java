package enums;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 *
 */
public enum InstallStage implements Stage<InstallStage> {

    SETUP, INSTALL, JOIN;

    private static List<InstallStage> STAGES = new ArrayList<>(EnumSet.allOf(InstallStage.class));

    public static List<InstallStage> getStages() {
        return new ArrayList<>(STAGES);
    }

    @Override
    public InstallStage getFirstStage() {
        return STAGES.get(0);
    }

    @Override
    public InstallStage getLastStage() {
        return STAGES.get(STAGES.size() - 1);
    }

    @Override
    public List<InstallStage> getSortedStage() {
        return getStages();
    }

    @Override
    public InstallStage upgradeTo() {
        if (getLastStage() == this) {
            return this;
        }

        return getStage(STAGES, 1);
    }

    @Override
    public InstallStage downgrade() {
        if (getFirstStage() == this) {
            return this;
        }

        return getStage(STAGES, -1);
    }
}
