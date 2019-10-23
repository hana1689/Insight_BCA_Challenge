/**
 * This class is used to create an object to store the data read from the input file. This simply includes getter, setter, and toString() methods
 */

public class DataEntry {

    private String border;
    private String date;
    private String measure;
    private Integer value;


    public String getBorder() {
        return border;
    }

    public void setBorder(String border) {
        this.border = border;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "DataEntry{" +
                "border='" + border + '\'' +
                ", date=" + date + '\'' +
                ", measure='" + measure + '\'' +
                ", value=" + value +
                '}';
    }
}
