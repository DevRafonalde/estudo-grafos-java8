package br.com.dev1risjc.grafos.tratamentoCiclos;

import edu.uci.ics.jung.graph.DelegateForest;

public class MyDelegateForest extends DelegateForest<String, String> {
    public MyDelegateForest() {
        super();
    }

    @Override
    public boolean addEdge(String edge, String source, String target) {
        // Cria uma cópia temporária do grafo para fazer a verificação de ciclos
        DelegateForest<String, String> tempForest = DelegateForestUtils.copyDelegateForest(this);

        // Adiciona a aresta ao grafo cópia
        tempForest.addEdge(edge, source, target);

        // Utiliza o algoritmo de detecção de ciclos para verificar se o grafo temporário possui ciclos e lança uma exceção em caso positivo
        DetectorCiclo cycleDetector = new DetectorCiclo(tempForest);
        if (!cycleDetector.detectarCiclos().equals("")) {
            throw new IllegalArgumentException("A adição dessa aresta criaria um ciclo no grafo.");
        }

        // Se não criaria um ciclo, adiciona a aresta normalmente
        return super.addEdge(edge, source, target);
    }

}
