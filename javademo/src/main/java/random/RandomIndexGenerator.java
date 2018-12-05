package random;

import java.util.Random;

/**
 * @author zacconding
 * @Date 2018-11-19
 * @GitHub : https://github.com/zacscoding
 */
public class RandomIndexGenerator {

    private int[] indices;
    private final int start;
    private final int last;
    private int offset;

    public RandomIndexGenerator(int start, int last) {
        this.start = Math.min(start, last);
        this.last = Math.max(start, last);
        reset();
    }

    public int nextIndex() throws NotRemainIndexException {
        if (!isRemain()) {
            throw new NotRemainIndexException();
        }

        return indices[offset++];
    }

    public boolean isRemain() {
        return offset + 1 <= indices.length;
    }

    private void reset() {
        int range = last - start + 1;
        indices = new int[range];
        for (int i = 0; i < range; i++) {
            indices[i] = start + i;
        }

        // shuffle
        Random rnd = new Random();
        for (int i = range; i > 1; i--) {
            swap(i - 1, rnd.nextInt(i));
        }
    }

    private void swap(int idx1, int idx2) {
        int helper = indices[idx1];
        indices[idx1] = indices[idx2];
        indices[idx2] = helper;
    }

    public static class NotRemainIndexException extends RuntimeException {

        public NotRemainIndexException() {
            super();
        }

        public NotRemainIndexException(String message) {
            super(message);
        }
    }
}
