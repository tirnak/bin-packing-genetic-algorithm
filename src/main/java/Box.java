/**
 * Created by kirill on 12.03.16.
 */
class Box extends Area{

    public void setContainer(int container) {
        this.container = container;
    }

    int container = -1;

    public boolean alreadyPlaced () {
        return container != -1;
    }

    public boolean isUnplaced() {
        return container == -1;
    }

    public Box(int xd, int yd) {
        this.xd = xd;
        this.yd = yd;
    }

    public boolean intersects(Box box) {
        return container == box.container && super.intersects(box);
    }

    public void rotate() {
        int temp = xd;
        xd = yd;
        yd = temp;
    }

    public void setCoord( int x, int y) {
        x0 = x; y0 = y;
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
