import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;

class Calculator {
    private static final Logger LOG = LogManager.getLogger(Calculator.class);
    public int spacex;
    public int spacey;

    public Calculator(int spacex, int spacey) {
        this.spacex = spacex;
        this.spacey = spacey;
    }

    private static boolean debug = true;

    public int calculate (List<Box> boxesToPack) {

        List<Box> localBoxesToPack = new CopyOnWriteArrayList<>(boxesToPack);

        if (boxesDontFit(localBoxesToPack)) {
            throw new RuntimeException("some of the boxes don't fit");
        }

        List<Space> containers = new ArrayList<>();
        containers.add(new Space(0,0,spacex,spacey));
        int spaceIndex = 0;
        do {
            Space currentContainer = new Space(0,0,spacex,spacey);
            containers.add(currentContainer);
            spaceIndex++;

            List<Space> tempSpaces = new LinkedList<>();
            tempSpaces.add(currentContainer);

            while (anyBoxFit(tempSpaces, localBoxesToPack)) {
                for (Box box: localBoxesToPack) {
                    try {
                        Space fittest = findFittestSpace(tempSpaces, box);
                        tempSpaces.remove(fittest);
                        List<Space> remainings = placeBox(fittest, box);
                        box.setContainer(spaceIndex);
                        localBoxesToPack.remove(box);
                        tempSpaces.addAll(remainings);
                    } catch (RuntimeException e) {
                        continue;
                    }
                }
            }
        } while (localBoxesToPack.size() > 0);

        if (debug) {
            for (Box boxi : localBoxesToPack) {
                for (Box boxj : localBoxesToPack) {
                    if (boxi.equals(boxj)) {
                        continue;
                    }
                    if (boxi.intersects(boxj)) {
                        LOG.debug("Achtung! " + boxi + " intersects with " + boxj);
                    }
                }
            }
        }

        return containers.size();
    }

    private boolean boxesDontFit(List<Box> boxes) {
        Space tempSpace = new Space(0,0,spacex,spacey);
        return !boxes.stream().allMatch(tempSpace::fitAnyhow);
    }

    private static boolean anyBoxFit (List<Space> spaces, List<Box> boxes) {
        for (Space space : spaces) {
            for (Box box : boxes) {
                if (box.alreadyPlaced()) {
                    continue;
                }
                if (space.fitAnyhow(box)) {
                    return  true;
                }
            }
        }
        return false;
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
        if (debug) {
            int areaExpected = space.xd * space.yd;
            int areaActual = spaces.get(0).xd * spaces.get(0).yd +
                    spaces.get(1).xd * spaces.get(1).yd +
                    box.xd * box.yd;
            LOG.debug("expected: " + areaExpected + ", actual: " + areaActual);
        }
        return spaces;
    }

    private static Space findFittestSpace(List<Space> spaces, Box box) {
        Space fittest = spaces.get(0);
        boolean fittestFit = fittest.fitAnyhow(box);
        for (int i = 1; i < spaces.size(); i++) {
            Space space = spaces.get(i);

            if (!fittestFit) {
                if (space.fitAnyhow(box)) {
                    fittest = space;
                    fittestFit = true;
                    continue;
                }
            }
            if (!space.fitAnyhow(box)) {
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
            throw new RuntimeException("box with x " + box.xd + " and y " + box.yd + " doesn't fitAnyhow");
        }
        return fittest;
    }
}

