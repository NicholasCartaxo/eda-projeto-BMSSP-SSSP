import commom.dataStructures.NodeDist;
import commom.graph.Node;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import DQueue.DQueue;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de teste completa para DQueue
 * Testa os métodos insert, batchPrepend e pull em diversos cenários,
 * incluindo casos de limites e interações entre os métodos.
 */
class DQueueTest {

    private DQueue dQueue;
    private final int BLOCK_SIZE = 5;

    // Helper para criar nós tradicionaisl
    private Node n(int id) {
        return new Node(id);
    }

    // Helper para criar um nó unico (NodeDist)
    private NodeDist nd(Node node, int dist) {
        return new NodeDist(node, dist);
    }

    @BeforeEach
    void setUp() {
        dQueue = new DQueue(BLOCK_SIZE, Integer.MAX_VALUE);
    }

    @Test
    //  inserir e extrair um único elemento
    void testInsertAndPullSingleElement() {
        dQueue.insert(nd(n(1), 10));
        //assertEquals(1, 0););
        HashSet<Node> pulledNodes = dQueue.pull();

        assertEquals(1, pulledNodes.size());
        assertTrue(pulledNodes.contains(n(1)));
        assertFalse(pulledNodes.contains(n(12)));

        // A fila deve estar vazia após o pull
        assertTrue(dQueue.pull().isEmpty());
    }

    @Test
    // Deve inserir múltiplos elementos e extrair todos se for menor que o blockSize 
    void testInsertMultipleAndPullLessThanBlockSize() {
        dQueue.insert(nd(n(1), 20));
        dQueue.insert(nd(n(2), 10));
        dQueue.insert(nd(n(3), 30));

        HashSet<Node> pulledNodes = dQueue.pull();
        //Mapeia os IDs dos nodes para facilitar as buscas dentro do Set
        Set<Integer> pulledIds = pulledNodes.stream().map(node -> node.id).collect(Collectors.toSet());
        
        assertEquals(3, pulledNodes.size());
        assertTrue(pulledIds.containsAll(Set.of(1, 2, 3)));
        assertTrue(dQueue.pull().isEmpty());
    }
    
    @Test
    // Deve extrair os elementos na ordem correta de prioridade (menor distância)
    void testPullRespectsPriorityAndBlockSize() {
        // Insere 7 elementos, mais que o BLOCK_SIZE permite
        dQueue.insert(nd(n(1), 100));
        dQueue.insert(nd(n(2), 20)); // 2º menor
        dQueue.insert(nd(n(3), 30)); // 3º menor
        dQueue.insert(nd(n(4), 400));
        dQueue.insert(nd(n(5), 10)); // 1º menor
        dQueue.insert(nd(n(6), 50)); // 5º meno
        dQueue.insert(nd(n(7), 40)); // 4º menor

        // Primeiro pull: deve retornar os 5 menores
        HashSet<Node> firstPull = dQueue.pull();
        Set<Integer> firstPullIds = firstPull.stream().map(node -> node.id).collect(Collectors.toSet());
        
        assertEquals(BLOCK_SIZE, firstPull.size());
        assertTrue(firstPullIds.containsAll(Set.of(5, 2, 3, 7, 6)), "Deveria conter os nós com as 5 menores distâncias.");
        assertFalse(firstPullIds.containsAll(Set.of(1, 2, 3, 4, 6)), "Não deveria conter os nós 1 e 4, sendo os com as maiores distancias");

        // Segundo pull: deve retornar os 2 restantes
        HashSet<Node> secondPull = dQueue.pull();
        //
        Set<Integer> secondPullIds = secondPull.stream().map(node -> node.id).collect(Collectors.toSet());

        assertEquals(2, secondPull.size());
        assertTrue(secondPullIds.containsAll(Set.of(1, 4)));
        
        // Terceiro pull: a fila deve estar vazia
        assertTrue(dQueue.pull().isEmpty());
    }

    @Test
    // Deve inserir em lote e extrair corretamente
    void testBatchPrependAndPull() {
        HashSet<NodeDist> batch = new HashSet<>();
        batch.add(nd(n(1), 15));
        batch.add(nd(n(2), 5));
        batch.add(nd(n(3), 25));

        dQueue.batchPrepend(batch);
        HashSet<Node> pulledNodes = dQueue.pull();
        Set<Integer> pulledIds = pulledNodes.stream().map(node -> node.id).collect(Collectors.toSet());

        assertEquals(3, pulledNodes.size());
        assertTrue(pulledIds.containsAll(Set.of(1, 2, 3)));
    }

    @Test
    // Deve misturar insert e batchPrepend e extrair os menores caminhos globais
    void testMixedInsertAndBatchPrepend() {
        // Inserção individual (vai para a InsertTree)
        dQueue.insert(nd(n(1), 5));
        dQueue.insert(nd(n(2), 50));
        dQueue.insert(nd(n(3), 25));

        // Inserção em lote (vai para a BatchList)
        HashSet<NodeDist> batch = new HashSet<>();
        batch.add(nd(n(4), 10));  
        batch.add(nd(n(5), 100));
        batch.add(nd(n(6), 30));

        dQueue.batchPrepend(batch);

        // O pull deve combinar resultados da InsertTree e da BatchList
        HashSet<Node> pulledNodes = dQueue.pull();
        Set<Integer> pulledIds = pulledNodes.stream().map(node -> node.id).collect(Collectors.toSet());

        assertEquals(BLOCK_SIZE, pulledNodes.size());
        // Esperados: 1 (dist 5), 4 (dist 10), 3 (dist 25), 6 (dist 30), 2 (dist 50)
        assertTrue(pulledIds.containsAll(Set.of(1, 2, 3, 4, 6))); 
    }

    @Test
    // Deve atualizar um nó se a nova distância for MAIOR
    void testNoUpdateWithHigherDistance() {
        Node targetNode = n(1);
        dQueue.insert(nd(targetNode, 50));
        // Inserir o mesmo nó com distância MAIOR. Não deve fazer nada.
        dQueue.insert(nd(targetNode, 100));

        dQueue.insert(nd(n(2), 10)); 
        HashSet<Node> pulled = dQueue.pull();
        // O pull deve retornar ambos, mas o importante é que o nó 1 está presente
        assertTrue(pulled.contains(targetNode));

        // Teste de que o nó com distância menor foi ignorado
        dQueue = new DQueue(BLOCK_SIZE, Integer.MAX_VALUE);
        dQueue.insert(nd(targetNode, 100));
        // Tenta inserir o mesmo nó com distância MENOR. Devendo atualizar ele.
        dQueue.insert(nd(targetNode, 50));
        dQueue.insert(nd(n(2), 200)); // Outro elementoque nao deve fazer nada

        pulled = dQueue.pull();
        Set<Integer> pulledIds = pulled.stream().map(node -> node.id).collect(Collectors.toSet());
        
        // O pull deve conter o nó 1 (com dist 50) e o nó 2 (com dist 10)
        assertTrue(pulledIds.contains(1));
        assertTrue(pulledIds.contains(2));
    }

    @Test
    // Deve lidar com duplicatas na inserção, mantendo o correto conforme a regra
    void testDuplicateNodesFiltering() {
        Node duplicateNode = n(1);
        dQueue.insert(nd(duplicateNode, 50));
        dQueue.insert(nd(duplicateNode, 30)); // Será ignorado
        dQueue.insert(nd(duplicateNode, 80)); // Irá substituir

        HashSet<NodeDist> batch = new HashSet<>();
        batch.add(nd(duplicateNode, 20)); // Será ignorado
        batch.add(nd(duplicateNode, 100)); // Irá substituir
        batch.add(nd(n(2), 10)); // Outro nó

        dQueue.batchPrepend(batch);

        HashSet<Node> pulled = dQueue.pull();

        // No final, apenas uma instância do nó 1 (a última, com dist 100) e o nó 2 devem estar na fila.
        assertEquals(2, pulled.size());
        assertTrue(pulled.contains(duplicateNode));
        assertTrue(pulled.contains(n(2)));
    }

    @Test
    // Deve retornar um conjunto vazio ao chamar pull em uma fila vazia
    void testPullFromEmptyQueue() {
        HashSet<Node> pulledNodes = dQueue.pull();
        assertNotNull(pulledNodes);
        assertTrue(pulledNodes.isEmpty());
    }

    @Test
    // Deve testar com batchPrepend de um conjunto vazio
    void testBatchPrependEmptySet() {
        dQueue.batchPrepend(new HashSet<>());
        assertTrue(dQueue.pull().isEmpty());
        //rretornar valore true para um pull vazio
    }
}
