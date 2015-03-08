import java.io.Serializable;

public class Message implements Serializable {
    private String type;
    private Date date;
    
    Message(String type, Date date) {
        this.type = type;
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public Date getDate() {
        return date;
    }
    
}
