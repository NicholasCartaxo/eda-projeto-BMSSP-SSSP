import random
import argparse

def generate_and_print_graph(num_nodes):

    num_edges = 2 * num_nodes
    max_weight = 1000000000

    print(f"{num_nodes} {num_edges}")

    connected_nodes = [1]
    
    for i in range(2, num_nodes + 1):
        u = random.choice(connected_nodes)
        v = i
        
        weight = random.randint(1, max_weight)
        print(f"{u} {v} {weight}")
        
        connected_nodes.append(v)

    edges_to_add = num_edges - (num_nodes - 1)
    
    for _ in range(edges_to_add):
        u = random.randint(1, num_nodes)
        v = random.randint(1, num_nodes)
        weight = random.randint(1, max_weight)
        
        print(f"{u} {v} {weight}")

if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument(
        "nodes", 
        type=int, 
    )
    
    args = parser.parse_args()
    
    generate_and_print_graph(args.nodes)