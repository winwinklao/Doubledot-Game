import javax.swing.JFrame;

public class Doubledot extends JFrame {

    public Doubledot() {

        initUI();
    }

    private void initUI() {

        add(new Board());
        setTitle("DOUBLE DOT :)");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 545);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
            var doub = new Doubledot();
            doub.setVisible(true);
    }
}
