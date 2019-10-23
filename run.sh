

javac -Xlint:none ./src/*.java

export CLASSPATH=${CLASSPATH}:.

java -cp $CLASSPATH/src BorderAnalytics "./input/Border_Crossing_Entry_Data.csv" "./output/report.csv"

