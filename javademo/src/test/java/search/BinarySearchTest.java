package search;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

/**
 *
 * @GitHub : https://github.com/zacscoding
 */
public class BinarySearchTest {

    @Test
    public void runTests() {
        /////////////////
        // Case1
        // stored   :
        // new      : A A A
        runTests0(
                Arrays.asList(),
                Arrays.asList("A", "A", "A"),
                0
        );

        runTests0(
                Arrays.asList("A", "A", "A"),
                Arrays.asList("A", "A", "A", "B"),
                3
        );

        runTests0(
                Arrays.asList("A", "A", "A"),
                Arrays.asList("A", "A", "A", "B", "B"),
                3
        );

        runTests0(
                Arrays.asList("A", "A", "A"),
                Arrays.asList("A", "A", "A", "B", "B", "B"),
                3
        );

        runTests0(
                Arrays.asList("A", "B", "B", "C"),
                Arrays.asList("A", "B1", "B1", "B1", "B1", "B1"),
                1
        );

        runTests0(
                Arrays.asList("A", "A", "A"),
                Arrays.asList("A", "A", "A"),
                3
        );

        runTests0(
                Arrays.asList("A", "A", "A", "B", "D", "E"),
                Arrays.asList("A", "A", "A", "C"),
                3
        );

        runTests0(
                Arrays.asList("A", "B", "C", "D", "E", "F"),
                Arrays.asList("B", "B1", "B2"),
                0
        );

    }

    private void runTests0(List<String> stored, List<String> newEvents, int expected) {
        System.out.println("================================================");
        int diffStartIdx = binarySearch(newEvents, stored);

        for (int i = 0; i < stored.size(); i++) {
            if (i == diffStartIdx) {
                System.out.print("\'");
            }
            System.out.print(stored.get(i));
            if (i == diffStartIdx) {
                System.out.print("\'");
            }
            System.out.print(" ");
        }
        System.out.println();

        for (int i = 0; i < newEvents.size(); i++) {
            if (i == diffStartIdx) {
                System.out.print("\'");
            }
            System.out.print(newEvents.get(i));
            if (i == diffStartIdx) {
                System.out.print("\'");
            }
            System.out.print(" ");
        }
        System.out.println();
        System.out.println(">> " + diffStartIdx);

        if (expected != diffStartIdx) {
            System.err.printf("Expected : %d but %d\n", expected, diffStartIdx);
        }
        System.out.println("================================================");
    }

    private int binarySearch(List<String> stored, List<String> newEvents) {
        int low = 0;
        int high = newEvents.size() - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;

            if (mid >= stored.size()) {
                high = mid - 1;
            } else {
                String exist = stored.get(mid);
                String newEvent = newEvents.get(mid);

                if (newEvent.equals(exist)) {
                    if (mid == newEvents.size() - 1) {
                        return newEvents.size();
                    }

                    low = mid + 1;
                } else {
                    high = mid - 1;
                }
            }
        }

        return low;
    }
}
