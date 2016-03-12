import javax.swing.*;

/**
 * Created by kirill on 12.03.16.
 */
class BoardMain {
    public static void main(String[] a) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setBounds(20, 20, 1000, 1000);
        window.getContentPane().add(new Board());
        window.setVisible(true);
    }
}
