package br.com.dev1risjc.grafos;

import br.com.dev1risjc.grafos.contrato.InterfaceContrato;
import br.com.dev1risjc.grafos.contrato.MyJMenuItem;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.GraphMouseListener;
import edu.uci.ics.jung.visualization.renderers.Renderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class GeradorGrafo extends JFrame {
    private String numeroLivroEscolhido;
    List<MyJMenuItem> itensPopup;
    Forest<String, String> grafo;
    List<InterfaceContrato> listaVertices = new ArrayList<>();
    HashMap<InterfaceContrato, List<InterfaceContrato>> filiacoes;

    public GeradorGrafo(List<MyJMenuItem> itensPopup, HashMap<InterfaceContrato, List<InterfaceContrato>> filiacoes) {
        this.itensPopup = itensPopup;
        this.filiacoes = filiacoes;
    }

    public void init() {
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.setFont(new Font("Trebuchet MS", Font.PLAIN, 12));

        if (!itensPopup.isEmpty()) {
            for (MyJMenuItem menuItem : itensPopup) {
                popupMenu.add(menuItem);
            }
        }

        grafo = new DelegateForest<>();
        TreeLayout<String, String> layout = new TreeLayout<>(grafo);

        // Criação do visualizador do grafo
        VisualizationViewer<String, String> vv = new VisualizationViewer<>(layout, new Dimension(300, 300));

        // Configurações visuais do visualizador
        vv.addGraphMouseListener(new GraphMouseListener<String>() {
            @Override
            public void graphClicked(String s, MouseEvent mouseEvent) {
                System.out.println(s);
            }

            private void showPopupMenu(MouseEvent e) {
                popupMenu.show(vv, e.getX(), e.getY());
            }

            @Override
            public void graphPressed(String s, MouseEvent mouseEvent) {
                if (mouseEvent.isPopupTrigger()) {
                    showPopupMenu(mouseEvent);
                    numeroLivroEscolhido = s;
                }
            }

            @Override
            public void graphReleased(String s, MouseEvent mouseEvent) {
                if (mouseEvent.isPopupTrigger()) {
                    showPopupMenu(mouseEvent);
                    numeroLivroEscolhido = s;
                }
            }
        });
        vv.getRenderContext().setVertexLabelTransformer(String::toString);
        vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);

        // Criação do frame para exibir o visualizador
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().add(vv);
        setLocationRelativeTo(null);
        pack();
        setVisible(true);

    }

    private List<InterfaceContrato> getDistinctKeys() {
        HashMap<InterfaceContrato, InterfaceContrato> distinct = new HashMap<>();
        for (InterfaceContrato key : filiacoes.keySet()) {
            distinct.putIfAbsent(key, null);
            for (InterfaceContrato value : filiacoes.get(key)) {
                distinct.putIfAbsent(value, null);
            }
        }
        return distinct.keySet().stream().collect(Collectors.toList());
    }

    public void criarVertices() {
        List<InterfaceContrato> distinct = getDistinctKeys();
        for (InterfaceContrato key : distinct) {
            grafo.addVertex(key.toString());
        }
    }

    public void criarArestas() {
        for (InterfaceContrato key : filiacoes.keySet()) {
            for (InterfaceContrato value : filiacoes.get(key)) {
                grafo.addEdge("Filiação", key.toString(), value.toString());
            }
        }
    }

    public void limparTela() {
        List<InterfaceContrato> distinct = getDistinctKeys();
        for (InterfaceContrato key : distinct) {
            grafo.removeVertex(key.toString());
        }
    }
}
