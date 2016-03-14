package org.model;

/**
 * Created by kirill on 12.03.16.
 */
public class Box extends Area implements Cloneable {

    public void setContainer(int container) {
        this.container = container;
    }

    public int container = -1;

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
        return "org.model.Box{" +
                "xd=" + xd +
                ", yd=" + yd +
                ", x0=" + x0 +
                ", y0=" + y0 +
                '}';
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public Box CloneNonApi() {
        try {
            return (Box) this.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
