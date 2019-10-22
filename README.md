# Insight_BCA_Challenge
This GitHub repository contains my solution to the Border Crossing Analysis challenge given by Insight Data Science. For this challenge, the solution must be able to calculate the total number of times vehicles, equipment, passengers and pedestrians cross the U.S.-Canadian and U.S.-Mexican borders each month, and also the running monthly average of total number of crossings for that type of crossing and border.

# Data Input
Given an input file `Border_Crossing_Entry_Data.csv` with the format below
```
Port Name,State,Port Code,Border,Date,Measure,Value,Location
Derby Line,Vermont,209,US-Canada Border,03/01/2019 12:00:00 AM,Truck Containers Full,6483,POINT (-72.09944 45.005)
Norton,Vermont,211,US-Canada Border,03/01/2019 12:00:00 AM,Trains,19,POINT (-71.79528000000002 45.01)
Calexico,California,2503,US-Mexico Border,03/01/2019 12:00:00 AM,Pedestrians,346158,POINT (-115.49806000000001 32.67889)
Hidalgo,Texas,2305,US-Mexico Border,02/01/2019 12:00:00 AM,Pedestrians,156891,POINT (-98.26278 26.1)
Frontier,Washington,3020,US-Canada Border,02/01/2019 12:00:00 AM,Truck Containers Empty,1319,POINT (-117.78134000000001 48.910160000000005)
Presidio,Texas,2403,US-Mexico Border,02/01/2019 12:00:00 AM,Pedestrians,15272,POINT (-104.37167 29.56056)
Eagle Pass,Texas,2303,US-Mexico Border,01/01/2019 12:00:00 AM,Pedestrians,56810,POINT (-100.49917 28.70889)
```
# Data Output
The program will calculate 2 key values:
- Value = sum of total number of crossings (`Value`) of each type of vehicle or equipment, or passengers or pedestrians, that crossed the border that month, regardless of what port was used
- Average = monthly average of total crossings for that border and means of crossing in all previous months. The number is then rounded to the nearest whole number.
After executing the program, the ouput file `report.csv` would be like
```
Border,Date,Measure,Value,Average
US-Mexico Border,03/01/2019 12:00:00 AM,Pedestrians,346158,114487
US-Canada Border,03/01/2019 12:00:00 AM,Truck Containers Full,6483,0
US-Canada Border,03/01/2019 12:00:00 AM,Trains,19,0
US-Mexico Border,02/01/2019 12:00:00 AM,Pedestrians,172163,56810
US-Canada Border,02/01/2019 12:00:00 AM,Truck Containers Empty,1319,0
US-Mexico Border,01/01/2019 12:00:00 AM,Pedestrians,56810,0
```
# Solution
This solution is written in Java language. The `BorderAnalytics.java` is designed to be the main file that executes all the calculation work. This main java file receives two arguments from the execution file, which are the input_file directory and the out_put file directory. Then, it reads the input file line by line and create an object (`DataEntry.java`) for each row to keep track of the data. The `DataEntry.java` performs only getter and setter functions for each value in each row. Once each row is obtained, they will be added into the `List` for the purpose of calculating.
- Value: As the value is calculated based on `Border`, `Date`, and `Measure`, the Hashmap or Dictionay is needed to perform the caluation. However, `HashMap` is choosen in this program since it performs faster for big data. The `Boder`, `Date`, and `Measure` will be the key for each object. As I iterate through each object, I then compare the key. If the key is the same, I obtain all the values and sum it up. Once the final list is obtained, I then create another object (`DataExport.java`) and `List` to store the value of each row and the collection of rows, respectively, for the average calculation. Another object is used since it'd easier to manipulate and write data to the report.csv file.
- Average: Once I got the list of objects, I also perform descending sorting the list based on `Date`, `Value`, `Measure`, and `Border`. According to the instructions, the average of each row in the list is calculated based on the `Border` and `Measure` of all previous months, so it'd be better to perform sorting before calculating the average. Once the list is sorted, I then iterate each object. Each object is then compared with the rest of the objects. I then sum up the total of values obtained from each object having the same `Border` and `Measure` with the current one. The number of months will be increased based on the number of these rows found through iteration. Then, the average will be calculated based on the number of months and the total of values, and updated back to the current record stored in `DataExport.java`. This keeps going until the end of the list. At this time, we export all these objects to the `report.csv` file. Each object has `toString()` function to define the desired structure with comma for each row. I then just call this function in the writing process without performing any data formatting there.

# Instructions
- Step 1: Clone the project from my GitHub
- Step 2: Ensure your computer has Java setup to run Java program
- Step 3: Navigate to the main directory of this project. For example: `cd Insight_BCA_Challenge`
- Step 4: Run the run.sh file in the terminal. I run this on Macbook using this command: `sh run.sh`

Open the `run.sh` file if you want to configure the input and output file. The following command below is to set up the configuration

Last two arguments should be the directory of input and output file, respectively. You can create a test folder in testsuite and specify the directory here to run the test you want. 




