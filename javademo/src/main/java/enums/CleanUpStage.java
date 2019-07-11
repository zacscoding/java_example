package enums;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 *
 */
public enum CleanUpStage implements Stage<CleanUpStage> {

    SETUP_PARAMETERS, STOP_PROCESS, REMOVE_FILES;

    private static List<CleanUpStage> STAGES = new ArrayList<>(EnumSet.allOf(CleanUpStage.class));

    @Override
    public CleanUpStage getFirstStage() {
        return STAGES.get(0);
    }

    @Override
    public CleanUpStage getLastStage() {
        return STAGES.get(STAGES.size() - 1);
    }

    @Override
    public List<CleanUpStage> getSortedStage() {
        return new ArrayList<>(STAGES);
    }

    @Override
    public CleanUpStage upgradeTo() {
        if (getLastStage() == this) {
            return this;
        }

        return getStage(STAGES, 1);
    }

    @Override
    public CleanUpStage downgrade() {
        if (getFirstStage() == this) {
            return this;
        }

        return getStage(STAGES, -1);
    }
}
