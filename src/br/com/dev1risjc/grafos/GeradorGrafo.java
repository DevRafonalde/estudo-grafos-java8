package br.com.dev1risjc.grafos;

import br.com.dev1risjc.grafos.contrato.InterfaceContrato;
import br.com.dev1risjc.grafos.contrato.MyJMenuItem;
import com.google.common.base.Functions;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.GraphMouseListener;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.renderers.DefaultVertexLabelRenderer;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import org.apache.commons.collections4.Transformer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class GeradorGrafo extends JFrame {
    private boolean grafoGerado = false;
    private String numeroLivroEscolhido;
    List<MyJMenuItem> itensPopup;
    Forest<String, String> grafo;
    List<InterfaceContrato> listaVertices = new ArrayList<>();
    HashMap<InterfaceContrato, List<InterfaceContrato>> filiacoes;
    List<String> filhosImediatos;

    public GeradorGrafo(List<MyJMenuItem> itensPopup, HashMap<InterfaceContrato, List<InterfaceContrato>> filiacoes, List<String> filhosImediatos) {
        this.itensPopup = itensPopup;
        this.filiacoes = filiacoes;
        this.filhosImediatos = filhosImediatos;
    }

    public void init() {
        Transformer<String, Paint> vertexColor = new Transformer<String, Paint>() {
            @Override
            public Paint transform(String input) {
                Color cor;
                if (!filhosImediatos.isEmpty()) {
                    cor = Color.black;
                    for (String filho : filhosImediatos) {
                        if (filho.equalsIgnoreCase(input)) {
                            cor = Color.RED;
                        }
                    }
                } else {
                    cor = Color.black;
                }
                return cor;
            }
        };

        Color cor = Color.BLACK;
        DefaultVertexLabelRenderer vertexLabelRenderer =
                new DefaultVertexLabelRenderer(cor) {
                    @Override
                    public <V> Component getVertexLabelRendererComponent(
                            JComponent vv, Object value, Font font,
                            boolean isSelected, V vertex)
                    {
                        super.getVertexLabelRendererComponent(
                                vv, value, font, isSelected, vertex);
                        Color cor;
                        if (!filhosImediatos.isEmpty()) {
                            cor = Color.WHITE;
                            for (String filho : filhosImediatos) {
                                if (filho.equalsIgnoreCase(vertex.toString())) {
                                    cor = Color.BLACK;
                                }
                            }
                        } else {
                            cor = Color.WHITE;
                        }
                        setForeground(cor);
                        return this;
                    }
                };


        Container content = getContentPane();
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.setFont(new Font("Trebuchet MS", Font.PLAIN, 12));

        if (!itensPopup.isEmpty()) {
            for (MyJMenuItem menuItem : itensPopup) {
                popupMenu.add(menuItem);
            }
        }

        grafo = new DelegateForest<>();
        criarVertices();
        criarArestas();
//        grafo.getEdges().stream().forEach(System.out::println);
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
        final DefaultModalGraphMouse<String, Integer> graphMouse = new DefaultModalGraphMouse<String, Integer>();
        final GraphZoomScrollPane panel = new GraphZoomScrollPane(vv);
        vv.getRenderContext().setVertexLabelTransformer(String::toString);
        vv.getRenderContext().setVertexLabelRenderer(vertexLabelRenderer);
        vv.getRenderContext().setEdgeShapeTransformer(EdgeShape.line(grafo));
        vv.getRenderContext().setVertexFillPaintTransformer(vertexColor::transform);
        vv.getRenderContext().setArrowFillPaintTransformer(Functions.<Paint>constant(Color.lightGray));
        vv.setGraphMouse(graphMouse);
        vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);

        final ScalingControl scaler = new CrossoverScalingControl();

        JButton plus = new JButton("+");
        plus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                scaler.scale(vv, 1.1f, vv.getCenter());
            }
        });
        JButton minus = new JButton("-");
        minus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                scaler.scale(vv, 1/1.1f, vv.getCenter());
            }
        });

        JPanel scaleGrid = new JPanel(new GridLayout(1,0));
        scaleGrid.setBorder(BorderFactory.createTitledBorder("Zoom"));

        JPanel controls = new JPanel();
        scaleGrid.add(plus);
        scaleGrid.add(minus);
        controls.add(scaleGrid);
        content.add(controls, BorderLayout.SOUTH);


        // Criação do frame para exibir o visualizador
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setTitle("Grafo de filiação de TRAs");
//        setLocationRelativeTo(null);
//        pack();
//        setVisible(true);
        content.add(panel);
        grafoGerado = true;
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
                String nomeAresta = "Filiação " + key.toString() + " -> " + value.toString();
                if (!grafo.getEdges().parallelStream().anyMatch(aresta -> nomeAresta.equalsIgnoreCase(aresta))) {
                    grafo.addEdge(nomeAresta , key.toString(), value.toString());
//                    System.out.println("Origem: " + key + " -> Destino: " + value);
                }

            }
        }
    }

    public void limparTela() {
        if (grafoGerado) {
            List<InterfaceContrato> distinct = getDistinctKeys();
            for (InterfaceContrato key : distinct) {
                grafo.removeVertex(key.toString());
            }
        }
    }
}
