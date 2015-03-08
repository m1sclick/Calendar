import java.net.Socket;
import java.sql.ResultSet;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class ClientThread implements Runnable {

    private Socket socket = new Socket();

    public ClientThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            communicate();
        } catch (SQLException ex) {
           JOptionPane.showMessageDialog(null, "Немає з'єднання з базою даних.");
        }
    }
    public void communicate() throws SQLException {
        try {
            InputStream sin = socket.getInputStream();
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(sin);
            ArrayList<Date> dates = new ArrayList<>();
            Message message;
            while (true) {
                message = (Message) in.readObject();
                Date date = message.getDate();
                int year, month, day;
                year = date.getYear();
                month = date.getMonth();
                ResultSet result;
                if (message.getType().equals("month")) {
                    MonthRequest req = new MonthRequest(Server.con.getConnection());
                    result = req.select(month);
                    dates = selectMonth(date, result);
                } else {
                    CalendarRequest req = new CalendarRequest(Server.con.getConnection());
                    result = req.select(year);
                    dates = selectCalendar(date, result);
                }
                out.writeObject(dates);
                out.reset();
                dates.clear();
            }
        } catch (IOException | ClassNotFoundException e) {}
    }

    public ArrayList<Date> selectMonth(Date date, ResultSet result) throws SQLException {
        int year, month, day;
        String name, info;
        year = date.getYear();
        month = date.getMonth();
        day = date.getDay();
        ArrayList<Date> dates = new ArrayList<>();
        Flexible flex = new Flexible(year);
        while (result.next()) {
            if (result.getInt("changing") < 0) {
                int[] arr = flex.get(result.getInt("changing"));
                if (arr[1] == month - 1) {
                    day = arr[0];
                    month = arr[1] + 1;
                } else {
                    continue;
                }
            } else {
                day = result.getInt("date");
                month = result.getInt("month");
            }
            name = result.getString("name");
            info = result.getString("info");
            dates.add(new Date(year, month, day, name, info));
        }
        return dates;
    }

    public ArrayList<Date> selectCalendar(Date date, ResultSet result) throws SQLException {
        int year, month, day;
        String name, info;
        year = date.getYear();
        month = date.getMonth();
        day = date.getDay();
        ArrayList<Date> dates = new ArrayList<>();
        Flexible flex = new Flexible(year);
        while (result.next()) {
            if (result.getInt("changing") < 0) {
                int[] arr = flex.get(result.getInt("changing"));
                    day = arr[0];
                    month = arr[1] + 1;
            } else {
                day = result.getInt("date");
                month = result.getInt("month");
            }
            name = result.getString("name");
            info = result.getString("info");
            dates.add(new Date(year, month, day, name, info));
        }
        return dates;
    }
}
