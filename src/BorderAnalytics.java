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

    private static String input_file = "";
    private static String output_file = "";
    private static Scanner scan;

    /**
     * The main function is where the file will execute first. It calls other functions in the file
     */
    public static void main(String[] args) {
//        Read the argument from the execution file, in which the first and second args are the directory of input and output file, respectively
        for (int i = 0; i < args.length; i++) {
            input_file = args[0];
            output_file = args[1];
        }
        writeData(calValueAndAvg(readData()));
    }


    /**
     * This function checks the valid file and then reads each row from the .csv file and then each row will be created as an
     * object and saved in the list
     *
     * @return list of data
     */

    public static List<DataEntry> readData() {
//      Create the list to store data from the file as we have many rows
        List<DataEntry> list = new ArrayList<DataEntry>();
        int i = 0;
        try {
//          Read the input file
            scan = new Scanner(new File(input_file));
            {
                while (scan.hasNext()) {
                    if (i == 0) {
//                  Check the header data. If Border, Measure, Date, and Value are not in the right place, inform an error message.
//                  If it's correct, we go to the next line to obtain data.
                        String[] header_data = scan.nextLine().split(",");
                        if (header_data[3].equalsIgnoreCase("Border") && header_data[4].equalsIgnoreCase("Date") &&
                                header_data[5].equalsIgnoreCase("Measure") && header_data[6].equalsIgnoreCase("Value")) {
                            i++;
                        } else {
                            throw new IOException("The file is incorrect format");
                        }
                    } else {
//                      This line below gets each value of each row by splitting the comma
                        String[] data = scan.nextLine().split(",");
                        // Each row is stored in the object as it's easier for us to handle and retrieve data from object. This process
                        // will continue to the end of the file
                        DataEntry data_entry = new DataEntry();
                        data_entry.setBorder(data[3]);
                        data_entry.setDate(data[4]);
                        data_entry.setMeasure(data[5]);
                        data_entry.setValue(Integer.parseInt(data[6]));
//                      Once each row is added into DataEntry, it will be added into the list as an object
                        list.add(data_entry);
                    }
                }
            }
//          This code checks if the list is null, which means there is no data. Then, an error message is thrown
            if (list.size() == 0) {
                throw new Exception("No data is found");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
//          At the end, it is important to close the stream as it may cause some unexpected issues if it is still opened
            scan.close();
        }
//      Once we obtain the list of data, then this list is transferred to the next function to execute the calculation
        return list;
    }

    /**
     * This function is the most important one as it calculates the value and average. It receives the data_entry_list result
     * from the readData() and then goes through each object to retrieve data needed to the calculation. First, it calculates
     * the Value of each object in the list. Then, it starts calculating the average based on this result
     *
     * @param data_entry_list
     */

    public static List<DataExport> calValueAndAvg(List<DataEntry> data_entry_list) {
//      In order to calculate the value, three important keys are border, date, and measure. Therefore, the HashMap is created to store
//      this set of keys. The list of DataExport objects is also needed to store and export data
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        List<DataExport> data_export_list = new ArrayList<>();
        DataExport data_export;

//      This code goes through each item of the list and compare the key, which is the combination of border, date, and measure, to sum
//      up the value of all objects having the same key. The HashMap then stores the updated data
        for (DataEntry item : data_entry_list) {
            String value_key = item.getBorder() + "," + item.getDate() + "," + item.getMeasure();
            if (!map.containsKey(value_key)) {
                map.put(value_key, item.getValue());
            } else {
                map.replace(value_key, item.getValue() + map.get(value_key));
            }
        }

//      Iterate each item in the HashMap to save it into DataExport object.
        Iterator<Map.Entry<String, Integer>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Integer> data = it.next();
            data_export = new DataExport();
            String[] keys = data.getKey().split(",");
            data_export.setBorder(keys[0]);
            data_export.setDate(keys[1]);
            data_export.setMeasure(keys[2]);
            data_export.setValue(data.getValue());
            data_export_list.add(data_export);
//          This code is used to sort the list in descending order based on date, value, measure, and border. The sort function is
//          defined in the DataExport file
            Collections.sort(data_export_list, new DataExportComparator());
            Collections.reverse(data_export_list);
        }

//      This code iterates through the list of DataExport objects to calculate the avg for each one and update it back to this object
        for (int i = 0; i < data_export_list.size(); i++) {
            int month = 0;
            float totalValue = 0;
//          As the avg is calculated based on value from all previous month. Each object will compare with the rest of the list.
//          Since data is already sorted, only border and measure needs to be compared. If any objects having the same border and measure,
//          then, the value will be added to the current one and number of month is increased
            for (int j = i + 1; j < data_export_list.size(); j++) {
                if (data_export_list.get(i).getBorder().equals(data_export_list.get(j).getBorder()) && data_export_list.get(i).getMeasure().equals(data_export_list.get(j).getMeasure())) {
                    month += 1;
                    totalValue += data_export_list.get(j).getValue();
                }
            }
//          If no month is found, it means this record is unique, then the avg is still 0. Otherwise, the avg will be calculated by the
//          formula below. Once the result is obtained, the avg of this record will be updated.
            if (month != 0) {
                int avg = (int) Math.ceil(totalValue / month);
                data_export_list.get(i).setAverage(avg);
            }
        }
//      Return the final set along with the results to the next function to write data
        return data_export_list;
    }

    /**
     * This function is used to write data from the list of DataExport objects obtained from the calValueAndAvg function
     *
     * @param data_export_list
     */

    public static void writeData(List<DataExport> data_export_list) {
        FileWriter csvWriter = null;
        try {
            csvWriter = new FileWriter(output_file);
//          Write the header
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

//          Write each DataExport object by iterating through the list obtained from the function above
            for (DataExport item : data_export_list) {
                csvWriter.append(item.toString());
                csvWriter.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
//              At the end, it is important to flush and close the stream as it may cause some unexpected issues if it is still opened
                csvWriter.flush();
                csvWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}


