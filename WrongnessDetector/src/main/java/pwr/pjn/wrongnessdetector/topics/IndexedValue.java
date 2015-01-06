package pwr.pjn.wrongnessdetector.topics;

/**
 *
 * @author KonradOliwer
 */
public class IndexedValue implements Comparable {

    public final int id;
    public int value;

    public IndexedValue(int id) {
        this.id = id;
    }

    public IndexedValue(int id, int v) {
        this.id = id;
        this.value = v;
    }

    @Override
    public int compareTo(Object o) {
        int ov = ((IndexedValue) o).value;
        return value == ov ? 0 : (value > ov ? 1 : -1);
    }
}
