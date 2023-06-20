package br.com.dev1risjc.grafos.tratamentoCiclos;

import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Graph;

import java.util.HashSet;
import java.util.Set;

public class DetectorCiclo {

    private Forest<String, String> graph;
    private Set<String> visited;
    private Set<String> recursionStack;

    public DetectorCiclo(Forest<String, String> graph) {
        this.graph = graph;
        visited = new HashSet<>();
        recursionStack = new HashSet<>();
    }

    public String detectarCiclos() {
        for (String vertex : graph.getVertices()) {
            return detectCyclesDFS(vertex);
        }
        return "nada";
    }

    private String detectCyclesDFS(String vertex) {
        // Verifica se o vértice está na pilha de recursão, indicando a presença de um ciclo
        if (recursionStack.contains(vertex)) {
            return vertex + "1";
        }
        // Verifica se o vértice já foi visitado em iterações anteriores, se sim, não há necessidade de explorá-lo novamente
        if (visited.contains(vertex)) {
            return "";
        }

        // Marca o vértice como visitado e o adiciona à pilha de recursão
        visited.add(vertex);
        recursionStack.add(vertex);

        // Percorre todas as arestas de saída do vértice atual
        for (String edge : graph.getOutEdges(vertex)) {
            // Obtém o vértice vizinho oposto à aresta
            String neighbor = getOppositeVertex(graph, edge, vertex);
            // Realiza a chamada recursiva para explorar o vértice vizinho
            if (!detectCyclesDFS(neighbor).equals("")) {
                // Se o vértice vizinho retornar a si mesmo, há um ciclo no grafo
                return neighbor;
            }
        }

        // Remove o vértice atual da pilha de recursão, pois todas as arestas foram exploradas
        recursionStack.remove(vertex);
        // Retorna vazio, indicando que não foram encontrados ciclos a partir do vértice atual
        return "";
    }

    private String getOppositeVertex(Graph<String, String> graph, String edge, String vertex) {
        // Obtém os vértices de origem e destino da aresta
        String source = graph.getSource(edge);
        String target = graph.getDest(edge);
        // Verifica qual dos vértices é diferente do vértice atual e retorna o vértice oposto
        return source.equals(vertex) ? target : source;
    }
}