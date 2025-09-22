import pandas as pd
import matplotlib.pyplot as plt

def plot_algorithm_performance(file_path):
    """
    Reads algorithm performance data from a CSV file and plots the results,
    including a secondary axis for the ratio between the two algorithms.

    Args:
        file_path (str): The path to the CSV file containing the data.
                         Expected columns: 'Nodes', 'Dijkstra', 'BMSSP'.
    """
    try:
        data = pd.read_csv(file_path)

        # --- NEW: Calculate the ratio ---
        # Avoid division by zero if Dijkstra's time is 0 for some reason
        if (data['Dijkstra'] == 0).any():
            print("Warning: Dijkstra's algorithm has zero execution time for some entries. Ratio cannot be calculated.")
            # Create a column of NaNs (Not a Number) for the ratio
            data['Ratio'] = float('nan')
        else:
            data['Ratio'] = data['BMSSP'] / data['Dijkstra']

        # --- Create the Plot ---
        # We now use subplots to get access to the axis objects `ax1` and `ax2`
        fig, ax1 = plt.subplots(figsize=(12, 7))

        # --- Plot Original Data on the Primary Y-Axis (ax1) ---
        ax1.plot(data['Nodes'], data['Dijkstra'], marker='o', linestyle='-', label='Dijkstra', color='blue')
        ax1.plot(data['Nodes'], data['BMSSP'], marker='s', linestyle='--', label='BMSSP', color='orange')

        # --- Customize the Primary Y-Axis ---
        ax1.set_xlabel('Number of Nodes', fontsize=12)
        ax1.set_ylabel('Execution Time (nanoseconds)', fontsize=12, color='black')
        ax1.tick_params(axis='y', labelcolor='black')
        ax1.ticklabel_format(style='sci', axis='y', scilimits=(0,0))

        # --- NEW: Create and Plot Ratio Data on the Secondary Y-Axis (ax2) ---
        # Create a second y-axis that shares the same x-axis
        ax2 = ax1.twinx()
        ax2.plot(data['Nodes'], data['Ratio'], marker='^', linestyle=':', label='Ratio (BMSSP / Dijkstra)', color='green')
        
        # --- NEW: Customize the Secondary Y-Axis ---
        ax2.set_ylabel('Ratio (BMSSP Time / Dijkstra Time)', fontsize=12, color='green')
        ax2.tick_params(axis='y', labelcolor='green')
        # We can add a horizontal line at y=1 to see where one algorithm becomes faster than the other
        ax2.axhline(y=1, color='red', linestyle='--', linewidth=0.8, label='Ratio = 1')


        # --- Customize the Overall Plot ---
        plt.title('Algorithm Performance: Execution Time and Ratio', fontsize=16)
        ax1.grid(True, which='both', linestyle='--', linewidth=0.5)

        # --- NEW: Combine Legends from Both Axes ---
        # To display all labels in one legend box, we get handles/labels from both axes
        lines1, labels1 = ax1.get_legend_handles_labels()
        lines2, labels2 = ax2.get_legend_handles_labels()
        ax2.legend(lines1 + lines2, labels1 + labels2, loc='upper left')

        plt.tight_layout()

        # --- Show the Plot ---
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
    csv_file = 'results.csv'
    plot_algorithm_performance(csv_file)