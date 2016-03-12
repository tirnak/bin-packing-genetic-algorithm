import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by kirill on 12.03.16.
 */
class BoardMain extends JComponent {

    private static final Logger LOG = LogManager.getLogger(BoardMain.class);
    static int spacexd = 500;
    static int spaceyd = 500;
    public java.util.List<Box> boxes;
    int currentContainer = 1;

    public static void main(String[] a) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setBounds(20, 20, 1000, 1000);

        BoardMain myComponent = new BoardMain();
        window.getContentPane().add(myComponent);
        window.setVisible(true);

        myComponent.boxes = new ArrayList<>();
//        boxes.add(new Box(40,40));
//        boxes.add(new Box(50,10));
//        boxes.add(new Box(47,30));
//        boxes.add(new Box(80,40));
//        boxes.add(new Box(200,30));
        Random random = new Random();
        for (int i = 0; i < 300; i++) {
            int x = (random.nextInt(10)+1) * 10;
            int y = (random.nextInt(10)+1) * 10;
            LOG.debug("x is " + x + ", y is " + y);
            myComponent.boxes.add(new Box(x,y));
        }

        Calculator calculator = new Calculator(spacexd,spaceyd);
        int containers = calculator.calculate(myComponent.boxes);
        LOG.debug(containers + " containers needed");
        int delay = 5000; //milliseconds

        ActionListener taskPerformer = e -> {
            myComponent.repaint();
            LOG.debug("currentContainer " + myComponent.currentContainer);
            myComponent.currentContainer++;
            if (myComponent.currentContainer == containers) {
                System.exit(0);
            }
        };

        new Timer(delay, taskPerformer).start();

    }


    public void paint(Graphics g) {
        Random rand = new Random();
        Origin c = new Origin();

        g.drawRect (c.x, c.y, spacexd, spaceyd);
        for (Box box : boxes) {
            if (!(box.container == currentContainer)) {
                continue;
            }
            float red = rand.nextFloat();
            float green = rand.nextFloat();
            float blue = rand.nextFloat();
            Color randomColor = new Color(red, green, blue);
            g.setColor(randomColor);
            g.fillRect (box.x0 + c.x, box.y0 + c.y, box.xd, box.yd);
        }

        //        System.exit(0);
    }



}
