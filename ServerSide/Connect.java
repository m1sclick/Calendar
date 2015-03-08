import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {

    private Connection con;

    public boolean connect() {
        String url = "jdbc:mysql://localhost/mydb"
                + "?autoReconnect=true&useUnicode=true&characterEncoding=utf8";
        String name = "root";
        String pass = "1234";
        try {
            con = DriverManager.getConnection(url, name, pass);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public Connection getConnection() {
        return con;
    }
}
