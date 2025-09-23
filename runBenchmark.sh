START_SIZE=50000
END_SIZE=1000000
STEP=50000

PYTHON_SCRIPT="graphGenerator.py"
JAVA_CLASS="main.java.Test" 
OUTPUT_FILE="results.csv"

echo "Starting benchmark..."
echo "Nodes,Dijkstra,BMSSP,Mistakes" > $OUTPUT_FILE

for size in $(seq $START_SIZE $STEP $END_SIZE)
do
    echo "Processing graph with $size nodes..."
    
    result=$(python $PYTHON_SCRIPT $size | java -cp bin $JAVA_CLASS)
    
    formatted_result=$(echo $result | tr ' ' ',')
    
    echo "$size,$formatted_result" >> $OUTPUT_FILE
done

echo "Benchmark finished! Results are in $OUTPUT_FILE"