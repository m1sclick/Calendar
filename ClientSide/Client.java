import java.net.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;

class Client {

    Socket socket;
    ObjectInputStream in;
    ObjectOutputStream out;

    Client() {
        int serverPort = 1234;
        String address = "127.0.0.1";
        try {
            InetAddress ipAddress = InetAddress.getByName(address);
            socket = new Socket(ipAddress, serverPort);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Не можу з'єднатись з сервером. Будь ласка спробуйте пізніше.");
        }
    }

    public ArrayList<Date> reqMonth(int y, int m) {
        Date date = new Date();
        date.setDay(1);
        date.setMonth(m);
        date.setYear(y);
        Message request = new Message("month", date);
        try {
            out.writeObject(request);
            ArrayList<Date> dates = new ArrayList<>();
            dates = (ArrayList<Date>) in.readObject();
            out.reset();
            return sort(dates);
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    public ArrayList<Date> reqCalendar(int y) {
        Date date = new Date();
        date.setDay(1);
        date.setMonth(1);
        date.setYear(y);
        Message request = new Message("calendar", date);
        try {
            out.writeObject(request);
            ArrayList<Date> dates = new ArrayList<>();
            dates = (ArrayList<Date>) in.readObject();
            out.reset();
            return sort(dates);
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    private ArrayList<Date> sort(ArrayList<Date> dates) {
        for (int i = 0; i < dates.size(); i++) {
            for (int j = 0; j < dates.size()-1; j++) {
                if (dates.get(j).getDay() > dates.get(j+1).getDay()) {
                    dates.add(j, dates.remove(j+1));
                }
            }
        }
        return dates;
    }
}
