package org;

import org.genetic.Optimizer;
import org.model.Origin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.TimerTask;

/**
 * Created by kirill on 12.03.16.
 */
class BoardMain extends JComponent {

    private static final Logger LOG = LogManager.getLogger(BoardMain.class);
    private static final BoardMain myComponent = new BoardMain();
    static int spacexd = 500;
    static int spaceyd = 500;
    public java.util.List<org.model.Box> boxes;
    int currentContainer = 0;

    public static void main(String[] a) {
        genAlg();
    }

    private static void genAlg() {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setBounds(20, 20, 1000, 1000);

        window.getContentPane().add(myComponent);
        window.setVisible(true);

        myComponent.boxes = new ArrayList<>();
//        myComponent.boxes.add(new org.model.Box(40,40));
//        myComponent.boxes.add(new org.model.Box(50,10));
//        myComponent.boxes.add(new org.model.Box(47,30));
//        myComponent.boxes.add(new org.model.Box(80,40));
//        myComponent.boxes.add(new org.model.Box(200,30));
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            int x = (random.nextInt(10)+1) * 10;
            int y = (random.nextInt(10)+1) * 10;
            LOG.debug(() -> "x is " + x + ", y is " + y);
            myComponent.boxes.add(new org.model.Box(x,y));
        }

        Calculator._instance = new Calculator(spacexd, spacexd);
        myComponent.boxes = Optimizer.main(myComponent.boxes);
        int containers = Calculator._instance.calculate(myComponent.boxes);
        LOG.debug(() -> containers + " containers needed");
        int delay = 1000; //milliseconds

        TimerTask taskPerformer = new TimerTask() {
            @Override
            public void run() {
                if (myComponent.currentContainer > containers * 4) {
                    System.exit(0);
                }
                myComponent.repaint();
                LOG.debug(() ->"currentContainer " + myComponent.currentContainer);
            }
        };

        new java.util.Timer().schedule(taskPerformer, delay, delay);

    }

    private static void justDraw() {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setBounds(20, 20, 1000, 1000);

        window.getContentPane().add(myComponent);
        window.setVisible(true);

        myComponent.boxes = new ArrayList<>();
//        myComponent.boxes.add(new org.model.Box(40,40));
//        myComponent.boxes.add(new org.model.Box(50,10));
//        myComponent.boxes.add(new org.model.Box(47,30));
//        myComponent.boxes.add(new org.model.Box(80,40));
//        myComponent.boxes.add(new org.model.Box(200,30));
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            int x = (random.nextInt(10)+1) * 10;
            int y = (random.nextInt(10)+1) * 10;
            LOG.debug(() -> "x is " + x + ", y is " + y);
            myComponent.boxes.add(new org.model.Box(x,y));
        }

        Calculator calculator = new Calculator(spacexd,spaceyd);
        int containers = calculator.calculate(myComponent.boxes);
        LOG.debug(() -> containers + " containers needed");
        int delay = 1000; //milliseconds

        TimerTask taskPerformer = new TimerTask() {
            @Override
            public void run() {
                if (myComponent.currentContainer > containers * 4) {
                    System.exit(0);
                }
                myComponent.repaint();
                LOG.debug(() -> "currentContainer " + myComponent.currentContainer);
            }
        };

        new java.util.Timer().schedule(taskPerformer, delay, delay);
    }


    public void paint(Graphics g) {
        Random rand = new Random();
        Origin c = new Origin();

        g.drawRect (c.x, c.y, spacexd, spaceyd);
        for (org.model.Box box : boxes) {
            if ((box.container != currentContainer / 4)) {
                continue;
            }
            float red = rand.nextFloat();
            float green = rand.nextFloat();
            float blue = rand.nextFloat();
            Color randomColor = new Color(red, green, blue);
            g.setColor(randomColor);
            g.fillRect(box.x0 + c.x, box.y0 + c.y, box.xd, box.yd);
        }
        myComponent.currentContainer++;
    }



}
