/**
 * Created by GreggJakubiak on 30.03.2017.
 */
public class Tuple {
    public final int x;
    public final int y;
    public Tuple(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean equals(Object T){
        Tuple t = (Tuple) T;
        return (x == t.x || x == t.y) && (y == t.x || y == t.y);
    }
}
