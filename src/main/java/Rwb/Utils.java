package Rwb;

import java.util.Collection;
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

    public static String joinToString(Collection<?> collection, CharSequence separator) {

        if (collection.isEmpty()) {
            return "";
        } else {
            StringBuilder sepValueBuilder = new StringBuilder();

            for (Object obj : collection) {
                sepValueBuilder.append(obj).append(separator);
            }
            sepValueBuilder.setLength(sepValueBuilder.length() - separator.length());

            return sepValueBuilder.toString();
        }
    }
}
