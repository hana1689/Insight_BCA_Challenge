import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class DataExport {

    private String border;
    private String date;
    private String measure;
    private Integer value;
    private int average;


    public void setBorder(String border) {
        this.border = border;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBorder() {
        return border;
    }

    public String getDate() {
        return date;
    }

    public String getMeasure() {
        return measure;
    }

    public Integer getValue() {
        return value;
    }

    public int getAverage() {
        return average;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    @Override
    public String toString() {
        return border + "," + date + "," + measure + "," + value + "," + average;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public void setAverage(int average) {
        this.average = average;
    }

}


class DataExportComparator implements Comparator<DataExport> {

    private static final String DATE_FORMAT = "dd/M/yyyy hh:mm:ss a";

    @Override
    public int compare(DataExport o1, DataExport o2) {
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = convertDate(o1.getDate());
            date2 = convertDate(o2.getDate());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (date1 == null || date2 == null) {
            return 0;
        }

        int date_compare = date1.compareTo(date2);
        int value_compare = o1.getValue().compareTo(o2.getValue());
        int measure_compare = o1.getMeasure().compareTo(o2.getMeasure());
        int border_compare = o1.getBorder().compareTo(o2.getBorder());
        if(date_compare == 0) {
            return ((value_compare == 0) ? ((measure_compare == 0) ? border_compare : measure_compare) : value_compare);
        } else {
            return date_compare;
        }


    }

    public Date convertDate(String date) throws Exception {
        Date d = new SimpleDateFormat(DATE_FORMAT).parse(date);
        return d;
    }
}