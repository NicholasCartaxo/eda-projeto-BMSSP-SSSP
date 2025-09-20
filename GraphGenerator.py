import random

# --- Configuration ---
NUM_NODES = 1_000_000
# A sparse graph has |E| on the order of |V|. We'll use 2 * |V|.
NUM_EDGES = 2_000_000
MAX_WEIGHT = 100000
OUTPUT_FILE = "1e6_sparse_graph.txt"

print(f"Generating a 1-indexed sparse graph with {NUM_NODES} nodes and {NUM_EDGES} edges...")

try:
    with open(OUTPUT_FILE, "w") as f:
        # Write the header line: n m
        f.write(f"{NUM_NODES} {NUM_EDGES}\n")

        # Keep track of generated edges to avoid duplicates
        existing_edges = set()

        # 1. Ensure connectivity by creating a long path (1->2->3->...->N)
        # This uses up (NUM_NODES - 1) edges.
        for i in range(1, NUM_NODES):
            u, v = i, i + 1
            weight = random.randint(1, MAX_WEIGHT)
            f.write(f"{u} {v} {weight}\n")
            existing_edges.add((u, v))

        # 2. Add the remaining edges randomly
        edges_to_add = NUM_EDGES - (NUM_NODES - 1)
        
        for i in range(edges_to_add):
            # Find a random edge that doesn't exist yet
            while True:
                # Generate nodes from 1 to NUM_NODES (inclusive)
                u = random.randint(1, NUM_NODES)
                v = random.randint(1, NUM_NODES)
                
                # Ensure it's not a self-loop and not a duplicate
                if u != v and (u, v) not in existing_edges:
                    weight = random.randint(1, MAX_WEIGHT)
                    f.write(f"{u} {v} {weight}\n")
                    existing_edges.add((u, v))
                    break
        
    print(f"✅ Successfully created the graph in '{OUTPUT_FILE}'")

except IOError as e:
    print(f"❌ Error: Could not write to file '{OUTPUT_FILE}'. Reason: {e}")