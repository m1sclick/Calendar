import javax.swing.JOptionPane;


public class Main {
    
    private static Ui u = new Ui(1);
    private static Connect c = new Connect();
    private static Server s = new Server(c); 

    public static void main(String[] args) {
        Thread t = new Thread(u);
        t.start();
        if (!c.connect()) {
            JOptionPane.showMessageDialog(null, "Нема з'єднання з базою даних.");
        } else {
            if (!s.start()) {
                JOptionPane.showMessageDialog(null, "Помилка сокета.");
            }
        }
    }
}
