import java.util.List;

public class Utils {
    public static <T extends Comparable<T>> int indexOfMinElem(List<T> l) {
        T min = l.get(0);
        int ind = 0;
        for (int i = 1; i < l.size(); i++) {
            if (min.compareTo(l.get(i)) > 0) {
                ind = i;
                min = l.get(i);
            }
        }
        return ind;
    }
}
