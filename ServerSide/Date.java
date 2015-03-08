import java.io.Serializable;

public class Date implements Serializable {

    private int day, month, year;
    private String name, info;

    Date() {}

    Date(int y, int m, int d, String n, String i) {
        year = y;
        day = d;
        month = m;
        name = n;
        info = i;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public String getName() {
        return name;
    }

    public String getInfo() {
        return info;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setInfo(String info) {
        this.info = info;
    }

}
