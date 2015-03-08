import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MonthRequest extends Request{

    MonthRequest(Connection con) {
        super.con = con;
    }

    public ResultSet select(int month) throws SQLException {
        Statement st = con.createStatement();
        String or = "";
        boolean flex = false;
        switch (month) {
            case 2: {
                or = "or changing in(-2)";
                flex = true;
            }
            ;
            break;
            case 3: {
                or = "or changing in(-2";
                or += ", -3)";
                flex = true;
            }
            ;
            break;
            case 4: {
                or = "or changing in(-3";
                or += ", -4)";
                flex = true;
            }
            ;
            break;
            case 5: {
                or = "or changing in(-1";
                or += ", -5)";
                flex = true;
            }
            ;
            break;
            case 6: {
                or = "or changing in(-5";
                or += ", -6)";
                flex = true;
            }
            ;
            break;
            default:
                break;
        }
        String query;
        if (flex) {
            query = "SELECT * FROM mydb.holidays WHERE month=" + month + " " + or + ";";
        } else {
            query = "SELECT * FROM mydb.holidays WHERE month=" + month + "; ";
        }
        ResultSet rs = st.executeQuery(query);
        return rs;
    }
}
