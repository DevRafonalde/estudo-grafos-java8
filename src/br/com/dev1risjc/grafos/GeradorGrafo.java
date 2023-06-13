package br.com.dev1risjc.grafos;

import br.com.dev1risjc.interfaceContrato.InterfaceContrato;
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

public class GeradorGrafo extends JFrame {
    private String numeroLivroEscolhido;
    List<JMenuItem> itensPopup;
    Forest<String, String> grafo;
    List<InterfaceContrato> listaVertices = new ArrayList<>();
    HashMap<InterfaceContrato, InterfaceContrato> filiacoes;

    public GeradorGrafo(List<JMenuItem> itensPopup, HashMap<InterfaceContrato, InterfaceContrato> filiacoes) {
        this.itensPopup = itensPopup;
        this.filiacoes = filiacoes;
    }

    public void init() {

        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.setFont(new Font("Trebuchet MS", Font.PLAIN, 12));
        if (!itensPopup.isEmpty()) {
            for (JMenuItem menuItem : itensPopup) {
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
//        JFrame frame = new JFrame("Graph Visualization");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().add(vv);
        setLocationRelativeTo(null);
        pack();
        setVisible(true);

    }

    public void criarVertices() {
        for (InterfaceContrato l : filiacoes.keySet()) {
            grafo.addVertex(l.getNumeroLivro());
            listaVertices.add(l);
            grafo.addVertex(filiacoes.get(l).getNumeroLivro());
            listaVertices.add(filiacoes.get(l));
        }
    }

    public void criarArestas() {
        for (InterfaceContrato l : filiacoes.keySet()) {
            grafo.addEdge("Edge" + l.getNumeroLivro() + filiacoes.get(l).getNumeroLivro(), l.getNumeroLivro(), filiacoes.get(l).getNumeroLivro());
        }
    }

    public void limparTela() {
        for (InterfaceContrato l : filiacoes.keySet()) {
            grafo.removeVertex(l.getNumeroLivro());
            grafo.removeVertex(filiacoes.get(l).getNumeroLivro());
        }
        listaVertices.clear();
    }
}
