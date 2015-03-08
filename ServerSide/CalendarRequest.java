import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CalendarRequest extends Request {
    
    CalendarRequest(Connection con) {
        super.con = con;
    }
    
    public ResultSet select(int year) throws SQLException {
        Statement st = con.createStatement();
        String query = "SELECT * FROM mydb.holidays;";
        ResultSet rs = st.executeQuery(query);
        return rs;
    }
}
