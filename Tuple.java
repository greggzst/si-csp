/**
 * Created by GreggJakubiak on 30.03.2017.
 */
public class Tuple<X,Y> {
    public final X x;
    public final Y y;
    public Tuple(X x, Y y) {
        this.x = x;
        this.y = y;
    }

    public boolean equals(Object T){
        Tuple t = (Tuple) T;
        return (x == t.x || x == t.y) && (y == t.x || y == t.y);
    }
}
