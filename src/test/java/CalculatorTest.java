import org.model.Box;
import org.model.Space;
import org.Calculator;
import org.junit.Before;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.Assert.*;

/**
 * Created by kirill on 13.03.16.
 */
public class CalculatorTest {


    @Before
    public void setUp() {
    }

    @org.junit.Test
    public void testFit() throws Exception {
        Box box = new Box(20, 30);
        Space space = new Space(0, 0, 40, 40);
        assertTrue(space.fitAnyhow(box));
        assertTrue(space.fit(box));
        assertTrue(space.fitRotated(box));
        assertEquals(10, space.findMinSpace(box));
    }

    @org.junit.Test
    public void testPlaceBox() throws Exception {
        Box box = new Box(20, 30);
        Space space = new Space(0, 0, 50, 40);
        assertEquals(10, space.findMinSpace(box));
        List<Space> remainings = Calculator.placeBox(space, box);
        Space s1 = new Space(0, 30, 20, 10);
        Space s2 = new Space(20, 0, 30, 40);
        assertTrue(remainings.get(0).equals(s1) && remainings.get(1).equals(s2));
        assertFalse(remainings.get(0).intersects(remainings.get(1)));
        assertFalse(remainings.get(0).intersects(box));
        assertFalse(box.intersects(remainings.get(1)));

        box = new Box(20, 30);
        space = new Space(40, 40, 50, 50);
        assertEquals(20, space.findMinSpace(box));
        remainings = Calculator.placeBox(space, box);
        s1 = new Space(40, 60, 50, 30);
        s2 = new Space(70, 40, 20, 20);
        assertTrue(remainings.get(0).equals(s1) && remainings.get(1).equals(s2));
        assertFalse(remainings.get(0).intersects(remainings.get(1)));
        assertFalse(remainings.get(0).intersects(box));
        assertFalse(box.intersects(remainings.get(1)));

    }

    @org.junit.Test
    public void testCalculate() {

        List<Box> boxes = new ArrayList<>();
        for (int i = 0; i < 35; i++) {
            boxes.add(new Box(10, 10));
        }
        List<Box> localBoxesToPack = new CopyOnWriteArrayList<>(boxes);

        Space currentContainer = new Space(0,0,60,60);
            List<Space> tempSpaces = new LinkedList<>();
            tempSpaces.add(currentContainer);

            while (Calculator.anyBoxFit(tempSpaces, localBoxesToPack)) {
                for (Box box: localBoxesToPack) {
                    try {
                        Space fittest = Calculator.findFittestSpace(tempSpaces, box);
                        tempSpaces.remove(fittest);
                        List<Space> remainings = Calculator.placeBox(fittest, box);
                        box.setContainer(0);
                        localBoxesToPack.remove(box);
                        tempSpaces.addAll(remainings);
                    } catch (RuntimeException e) {
                        continue;
                    }
                }
            }
        for (Box boxi : boxes) {
            for (Box boxj : boxes) {
                if (boxi.equals(boxj)) {continue;}
                assertFalse(boxi.intersects(boxj));
            }
        }
    }

    @org.junit.Test
    public void testFindFittestSpace() {
        List<Space> spaces = new ArrayList<>();
        spaces.add(new Space(0,0, 10, 100));
        spaces.add(new Space(0,0, 20, 100));
        spaces.add(new Space(0,0, 30, 100));
        spaces.add(new Space(0,0, 40, 100));
        spaces.add(new Space(0,0, 50, 100));
        spaces.add(new Space(0,0, 60, 100));
        assertEquals(spaces.get(0), Calculator.findFittestSpace(spaces, new Box(8, 90)));
        assertEquals(spaces.get(1), Calculator.findFittestSpace(spaces, new Box(18,90)));
        assertEquals(spaces.get(2), Calculator.findFittestSpace(spaces, new Box(28,90)));
        assertEquals(spaces.get(3), Calculator.findFittestSpace(spaces, new Box(38,90)));
        assertEquals(spaces.get(0), Calculator.findFittestSpace(spaces, new Box(90,8)));
        assertEquals(spaces.get(1), Calculator.findFittestSpace(spaces, new Box(90,18)));
        assertEquals(spaces.get(2), Calculator.findFittestSpace(spaces, new Box(90,28)));
        assertEquals(spaces.get(3), Calculator.findFittestSpace(spaces, new Box(90,38)));
    }


}