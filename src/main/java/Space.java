/**
 * Created by kirill on 12.03.16.
 */
class Space {
    int x0, y0;
    int xd, yd;

    public Space(int x0, int y0, int xd, int yd) {
        this.x0 = x0;
        this.y0 = y0;
        this.xd = xd;
        this.yd = yd;
    }

    public Space(int xd, int yd) {
        this.xd = xd;
        this.yd = yd;
    }

    public boolean fitAny(Box box) {
        if (((xd < box.xd) || (yd < box.yd)) &&
                ((yd < box.xd) || (xd < box.yd))) {
            return false;
        } else {
            return true;
        }
    }

    public boolean fit(Box box) {
        if ((xd < box.xd) || (yd < box.yd)) {
            return false;
        } else {
            return true;
        }
    }

    public boolean fitRotated(Box box) {
        if ((xd < box.yd) || (yd < box.xd)) {
            return false;
        } else {
            return true;
        }
    }
}
