/**
 * Created by kirill on 12.03.16.
 */
class Box {
    int xd, yd;
    int x0,y0;

    public void setContainer(int container) {
        this.container = container;
    }

    int container = 0;

    public boolean alreadyPlaced () {
        return container != 0;
    }

    public boolean isUnplaced() {
        return container == 0;
    }
    public Box(int xd, int yd) {
        this.xd = xd;
        this.yd = yd;
    }

    public void rotate() {
        int temp = xd;
        xd = yd;
        yd = temp;
    }

    public void setCoord( int x, int y) {
        x0 = x; y0 = y;
    }

    public boolean intersects (Box box) {
        if ((this.x0 + this.xd <= box.x0)
         || (this.y0 >= box.y0 + box.yd)
         || (this.x0 >= box.x0 + box.xd)
         || (this.y0 + this.yd <= box.y0)) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public String toString() {
        return "Box{" +
                "xd=" + xd +
                ", yd=" + yd +
                ", x0=" + x0 +
                ", y0=" + y0 +
                '}';
    }
}
