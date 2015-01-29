package Rwb;

import java.util.List;

/**
 *
 * @author niki
 */
public final class Utils {

    private Utils() {
    }

    public static int[] toIntArray(List<Integer> list) {
        if (list.isEmpty()) {
            return new int[0];
        }
        int[] ret = new int[list.size()];
        int i = 0;
        for (Integer e : list) {
            ret[i++] = e;
        }
        return ret;
    }

}
