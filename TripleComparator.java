import java.util.Comparator;

/**
 * Created by GreggJakubiak on 04.04.2017.
 */
public class TripleComparator implements Comparator<Triple>{
    @Override
    public int compare(Triple o1, Triple o2) {
        return Integer.compare(o2.n,o1.n);
    }
}
