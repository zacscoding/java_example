package enums;

import java.util.List;

/**
 *
 */
public interface Stage<T extends Enum> {

    /**
     * Check this enum is whether greater than other or not
     */
    default boolean isGraterThan(T other) {
        return ((Enum) this).compareTo(other) > 0;
    }

    /**
     * Check this enum is whether less than other or not
     */
    default boolean isLessThan(T other) {
        return ((Enum) this).compareTo(other) < 0;
    }

    default T getStage(List<T> stages, int delta) {
        if (stages == null || stages.isEmpty()) {
            throw new IllegalArgumentException("stages must be not empty");
        }

        int targetIdx = stages.indexOf(this) + delta;

        if (targetIdx < 0 || targetIdx >= stages.size()) {
            throw new IllegalArgumentException("Invalid index " + targetIdx + ". it is out of bounds stages");
        }

        return stages.get(targetIdx);
    }

    /**
     * Getting first stage
     */
    T getFirstStage();

    /**
     * Getting last stage
     */
    T getLastStage();

    /**
     * Getting sorted stage enums (asc)
     */
    List<T> getSortedStage();

    /**
     * Getting next stage
     */
    T upgradeTo();

    /**
     * Getting prev stage
     */
    T downgrade();
}
