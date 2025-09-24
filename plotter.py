import pandas as pd
import matplotlib.pyplot as plt

def plot_algorithm_performance(file_path):
    data = pd.read_csv(file_path)

    if (data['Dijkstra'] == 0).any():
        print("Warning: Dijkstra's algorithm has zero execution time for some entries. Ratio cannot be calculated.")
        data['Ratio'] = float('nan')
    else:
        data['Ratio'] = data['BMSSP'] / data['Dijkstra']

    fig, ax1 = plt.subplots(figsize=(12, 7))

    ax1.plot(data['Nodes'], data['Dijkstra'], marker='o', linestyle='-', label='Dijkstra', color='blue')
    ax1.plot(data['Nodes'], data['BMSSP'], marker='s', linestyle='--', label='BMSSP', color='orange')

    ax1.set_xlabel('Number of Nodes', fontsize=12)
    ax1.set_ylabel('Execution Time (nanoseconds)', fontsize=12, color='black')
    ax1.tick_params(axis='y', labelcolor='black')
    ax1.ticklabel_format(style='sci', axis='y', scilimits=(0,0))

    ax2 = ax1.twinx()
    ax2.plot(data['Nodes'], data['Ratio'], marker='^', linestyle=':', label='Ratio (BMSSP / Dijkstra)', color='green')
    
    ax2.set_ylabel('Ratio (BMSSP Time / Dijkstra Time)', fontsize=12, color='green')
    ax2.tick_params(axis='y', labelcolor='green')
    ax2.axhline(y=1, color='red', linestyle='--', linewidth=0.8, label='Ratio = 1')

    plt.title('Dijkstra vs BMSSP Comparison with Ratio', fontsize=16)
    ax1.grid(True, which='both', linestyle='--', linewidth=0.5)

    lines1, labels1 = ax1.get_legend_handles_labels()
    lines2, labels2 = ax2.get_legend_handles_labels()
    ax2.legend(lines1 + lines2, labels1 + labels2, loc='upper left')

    plt.tight_layout()

    print("Displaying the plot. Close the plot window to exit the script.")
    plt.show()

if __name__ == '__main__':
    csv_file = 'results.csv'
    plot_algorithm_performance(csv_file)