import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Request {
    protected Connection con;
    
    abstract public ResultSet select(int key) throws SQLException;
}
