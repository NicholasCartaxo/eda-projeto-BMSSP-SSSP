import pandas as pd
import matplotlib.pyplot as plt

def plot_algorithm_performance(file_path):
    """
    Reads algorithm performance data from a CSV file and plots the results.

    Args:
        file_path (str): The path to the CSV file containing the data.
                         Expected columns: 'Nodes', 'Dijkstra', 'BMSSP'.
    """
    try:
        # Read the data from the CSV file into a pandas DataFrame.
        # A DataFrame is like a spreadsheet or a SQL table.
        data = pd.read_csv(file_path)

        # --- Create the Plot ---

        # Set the size of the plot figure for better visibility
        plt.figure(figsize=(10, 6))

        # Plot the 'Nodes' column on the x-axis and 'Dijkstra' on the y-axis.
        # We add a label for the legend and a marker for each data point.
        plt.plot(data['Nodes'], data['Dijkstra'], marker='o', linestyle='-', label='Dijkstra')

        # Plot the 'Nodes' column on the x-axis and 'BMSSP' on the y-axis.
        plt.plot(data['Nodes'], data['BMSSP'], marker='s', linestyle='--', label='BMSSP')

        # --- Customize the Plot ---

        # Add a title to the plot
        plt.title('Algorithm Performance Comparison: Dijkstra vs. BMSSP', fontsize=16)

        # Add a label to the x-axis
        plt.xlabel('Number of Nodes', fontsize=12)

        # Add a label to the y-axis (assuming the time is in nanoseconds from your example)
        plt.ylabel('Execution Time (nanoseconds)', fontsize=12)

        # Display a legend to identify which line corresponds to which algorithm
        plt.legend()

        # Add a grid for easier reading of values
        plt.grid(True, which='both', linestyle='--', linewidth=0.5)
        
        # Use scientific notation for the y-axis to keep it clean if numbers are large
        plt.ticklabel_format(style='sci', axis='y', scilimits=(0,0))
        
        # Adjust layout to make sure everything fits without overlapping
        plt.tight_layout()

        # --- Show the Plot ---

        # Display the plot in a new window
        print("Displaying the plot. Close the plot window to exit the script.")
        plt.show()

    except FileNotFoundError:
        print(f"Error: The file '{file_path}' was not found.")
        print("Please make sure the CSV file is in the same directory as the script, or provide the full path.")
    except KeyError as e:
        print(f"Error: A required column is missing from the CSV file: {e}")
        print("Please ensure the CSV file has the columns 'Nodes', 'Dijkstra', and 'BMSSP'.")
    except Exception as e:
        print(f"An unexpected error occurred: {e}")

if __name__ == '__main__':
    # The name of your data file.
    # Make sure this file is in the same folder as your Python script.
    csv_file = 'results.csv'
    plot_algorithm_performance(csv_file)