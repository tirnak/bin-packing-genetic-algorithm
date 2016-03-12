import java.awt.Graphics;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import javax.swing.JComponent;

class Board extends JComponent {

    private static boolean check = true;

    public void paint(Graphics g) {
        Origin c = new Origin();
        Space outerBox = new Space(0,0,500,500);
        g.drawRect (c.x, c.y, outerBox.xd, outerBox.yd);
        List<Space> spaces = new LinkedList<>();
        spaces.add(outerBox);

        List<Box> boxes = new ArrayList<>();
//        boxes.add(new Box(40,40));
//        boxes.add(new Box(50,10));
//        boxes.add(new Box(47,30));
//        boxes.add(new Box(80,40));
//        boxes.add(new Box(200,30));
        Random random = new Random();
        for (int i = 0; i < 40; i++) {
            int x = (random.nextInt(10)+1) * 10;
            int y = (random.nextInt(10)+1) * 10;
            System.out.println("x is "+ x + ", y is " + y);
            boxes.add(new Box(x,y));
        }
        for (Box box: boxes) {
            Space fittest = findFittestSpace(spaces, box);
            spaces.remove(fittest);
            List<Space> remainings = placeBox(fittest, box);
            spaces.addAll(remainings);
            g.drawRect (box.x0 + c.x, box.y0 + c.y, box.xd, box.yd);
        }

        for (Box box: boxes) {
            for (Box boxPrime : boxes) {
                if (box.equals(boxPrime)) {
                    continue;
                }
                if (box.intersects(boxPrime)) {
                    System.out.println("Achtung! " + box + " intersects with " + boxPrime);
                }
            }
        }


//        System.exit(0);
    }

    private static List<Space> placeBox(Space space, Box box) {
        List<Space> spaces = new ArrayList<>();
        int sx1 = space.xd - box.xd;
        int sy1 = space.yd - box.yd;
        int sx2 = space.xd - box.yd;
        int sy2 = space.yd - box.xd;
        int minc = Stream.of(sx1, sx2, sy1, sy2).filter(i -> i>0).min(Integer::compare).get();
        if ((minc == sx2 || minc == sy2) && space.fitRotated(box)) {
            box.rotate();
        }
        box.setCoord(space.x0, space.y0);
        int xdd = space.xd - box.xd;
        int ydd = space.yd - box.yd;
        if (xdd > ydd) {
            spaces.add(new Space(space.x0, space.y0 + box.yd, box.xd, space.yd - box.yd));
            spaces.add(new Space(space.x0 + box.xd, space.y0, space.xd - box.xd, space.yd));
        } else {
            spaces.add(new Space(space.x0, space.y0 + box.yd, space.xd, space.yd - box.yd));
            spaces.add(new Space(space.x0 + box.xd, space.y0, space.xd - box.xd, box.yd));
        }
        if (check) {
            int areaExpected = space.xd * space.yd;
            int areaActual = spaces.get(0).xd * spaces.get(0).yd +
                    spaces.get(1).xd * spaces.get(1).yd +
                    box.xd * box.yd;
            System.out.println("expected: "+ areaExpected + ", actual: "+ areaActual);
        }
        return spaces;
    }

    private static Space findFittestSpace(List<Space> spaces, Box box) {
        Space fittest = spaces.get(0);
        boolean fittestFit = fittest.fitAny(box);
        for (int i = 1; i < spaces.size(); i++) {
            Space space = spaces.get(i);

            if (!fittestFit) {
                if (space.fitAny(box)) {
                    fittest = space;
                    fittestFit = true;
                    continue;
                }
            }
            if (!space.fitAny(box)) {
                continue;
            }

            // gap between size and box minimized
            int fx1 = fittest.xd - box.xd;
            int fy1 = fittest.yd - box.yd;
            int sx1 = space.xd - box.xd;
            int sy1 = space.yd - box.yd;

            int fx2 = fittest.xd - box.yd;
            int fy2 = fittest.yd - box.xd;
            int sx2 = space.xd - box.yd;
            int sy2 = space.yd - box.xd;
            int minf = Stream.of(fx1, fx2, fy1, fy2).filter(j -> j > 0).min(Integer::compare).orElse(-1);
            int minc = Stream.of(sx1, sx2, sy1, sy2).filter(j -> j > 0).min(Integer::compare).orElse(-1);
            if (minc >= 0 && minf >= 0 && minc < minf) {
                fittest = space;
            }
        }
        if (!fittestFit) {
            throw new RuntimeException("box with x " + box.xd + " and y " + box.yd + " doesn't fitAny");
        }
        return fittest;
    }

}

