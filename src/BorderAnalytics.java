import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.io.File;

/**
 * Given the file is exactly the same as the sample data
 * This file implements reading each row from the .csv file, then calculate the value by summing
 * the total number of crossings (Value) of each type of vehicle or equipment, or passengers or pedestrians,
 * that crossed the border that month, regardless of what port was used. Once the value is obtained, it continues
 * calculating the running monthly average of total crossings, rounded to the nearest whole number, by combination
 * of Border and Measure, or means of crossing
 */

public class BorderAnalytics {

    private static Scanner scan;

    /**
     * The main function is where the file will execute first. It calls other functions in the file
     */
    public static void main(String[] args) {
        String input_file = "";
        String output_file= "";
        for (int i = 0; i < args.length; i++) {
            input_file = args[0];
            output_file = args[1];
        }
        calValueAndAvg(readData(input_file), output_file);
    }

    /**
     * This function reads each row from the .csv file and then each row will be created as an object and saved in the list
     * @return list of data
     */

    public static List<DataEntry> readData(String input_file) {
//        Create the list to store data from the file as we have many rows
        List<DataEntry> list = new ArrayList<DataEntry>();
        int i = 0;
        try {
            scan = new Scanner(new File(input_file));
            while (scan.hasNext()) {
//                The first line which is header is not read. It starts reading from the second line
                if (i == 0) {
                    scan.nextLine();
                    i++;
                } else {
//                    Data in each row is separated by the comma as we want to obtain each value in the row
                    String[] data = scan.nextLine().split(",");
//                    Each row is stored in the object as it's easier for us to handle and retrieve data from object. This process will
//                    continue to the end of the file
                    DataEntry data_entry = new DataEntry();
                    data_entry.setBorder(data[3]);
                    data_entry.setDate(data[4]);
                    data_entry.setMeasure(data[5]);
                    data_entry.setValue(Integer.parseInt(data[6]));
                    list.add(data_entry);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            At the end, it is important to close the stream as it may cause some unexpected issues if it is still opened
            scan.close();
        }
//        Once we obtain the list of data, then this list is transferred to the next function to execute the calculation
        return list;
    }

    /**
     * This function is the most important one as it calculates the value and average. It receives the list result from the readData() and
     * then goes through each object to retrieve data needed to the calculation
     * @param list
     */

    public static void calValueAndAvg(List<DataEntry> list, String output_file) {
//        In order to calculate the value, three important keys are border, date, and measure. Therefore, the program will run faster
//        if we do not iterate
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        List<DataExport> ed = new ArrayList<>();
        DataExport export_data;

        for (DataEntry item : list) {
            String key = item.getBorder() + "," + item.getDate() + "," + item.getMeasure();
            if (!map.containsKey(key)) {
                map.put(key, item.getValue());
            } else {
                map.replace(key, item.getValue() + map.get(key));
            }
        }

        Iterator<Map.Entry<String, Integer>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Integer> data = it.next();
            export_data = new DataExport();
            String[] keys = data.getKey().split(",");
            export_data.setBorder(keys[0]);
            export_data.setDate(keys[1]);
            export_data.setMeasure(keys[2]);
            export_data.setValue(data.getValue());
            ed.add(export_data);
            Collections.sort(ed, new DataExportComparator());
            Collections.reverse(ed);
        }

        for(int i = 0; i < ed.size(); i++) {
            int month = 0;
            float totalValue = 0;
            for(int j = i + 1; j < ed.size(); j ++) {
                if(ed.get(i).getBorder().equals(ed.get(j).getBorder()) && ed.get(i).getMeasure().equals(ed.get(j).getMeasure())) {
                    month += 1;
                    totalValue += ed.get(j).getValue();
                }
            }
            if(month != 0) {
                int avg = (int)Math.ceil(totalValue / month);
                ed.get(i).setAverage(avg);
            }
        }
        writeData(ed, output_file);
    }

    public static void writeData(List<DataExport> list, String output_file) {
        FileWriter csvWriter = null;
        try {
            csvWriter = new FileWriter(output_file);
            csvWriter.append("Border");
            csvWriter.append(",");
            csvWriter.append("Date");
            csvWriter.append(",");
            csvWriter.append("Measure");
            csvWriter.append(",");
            csvWriter.append("Value");
            csvWriter.append(",");
            csvWriter.append("Average");
            csvWriter.append("\n");

            for(DataExport item : list) {
                csvWriter.append(item.toString());
                csvWriter.append("\n");
            }
        }catch(IOException e) {
            e.printStackTrace();
        } finally {
            try {
                csvWriter.flush();
                csvWriter.close();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
