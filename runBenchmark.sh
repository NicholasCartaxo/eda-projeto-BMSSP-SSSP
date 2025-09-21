# --- Configuration ---
# Set the range of graph sizes you want to test
START_SIZE=1000
END_SIZE=1000000
STEP=1000

# Set the names of your files
PYTHON_SCRIPT="graphGenerator.py"
# NOTE: Use the name of your main class, NOT the .java file
JAVA_CLASS="main.java.Test" 
OUTPUT_FILE="results.csv"

# --- Script Starts Here ---

echo "Starting benchmark..."

# Create the output file and write the header row for the CSV
echo "Nodes,Dijkstra,BMSSP,Mistakes" > $OUTPUT_FILE

# Loop from START_SIZE to END_SIZE with the specified STEP
for size in $(seq $START_SIZE $STEP $END_SIZE)
do
    echo "Processing graph with $size nodes..."
    
    # 1. Run the Python script to generate the graph.
    # 2. Pipe (|) its output directly as standard input to the Java program.
    # 3. Capture the Java program's output into the 'result' variable.
    result=$(python $PYTHON_SCRIPT $size | java -cp bin $JAVA_CLASS)
    
    # 4. Java prints "time1 time2 mistakes". Replace spaces with commas.
    formatted_result=$(echo $result | tr ' ' ',')
    
    # 5. Append the current size and the formatted result to the CSV file.
    echo "$size,$formatted_result" >> $OUTPUT_FILE
done

echo "Benchmark finished! Results are in $OUTPUT_FILE"