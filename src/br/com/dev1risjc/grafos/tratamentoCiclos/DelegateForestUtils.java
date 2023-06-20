package br.com.dev1risjc.grafos.tratamentoCiclos;

import edu.uci.ics.jung.graph.DelegateForest;

public class DelegateForestUtils {

    public static DelegateForest<String, String> copyDelegateForest(DelegateForest<String, String> originalDelegateForest) {
        DelegateForest<String, String> novoDelegateForest = new DelegateForest<>();

        for (String vertex : originalDelegateForest.getVertices()) {
            novoDelegateForest.addVertex(vertex);
        }

        for (String edge : originalDelegateForest.getEdges()) {
            String source = originalDelegateForest.getSource(edge);
            String target = originalDelegateForest.getDest(edge);
            novoDelegateForest.addEdge(edge, source, target);
        }

        return novoDelegateForest;
    }
}
