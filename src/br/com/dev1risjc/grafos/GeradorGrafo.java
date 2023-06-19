package br.com.dev1risjc.grafos;

import br.com.dev1risjc.grafos.contrato.InterfaceContrato;
import br.com.dev1risjc.grafos.contrato.MyJMenuItem;
import com.google.common.base.Function;
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
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.DefaultVertexLabelRenderer;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import edu.uci.ics.jung.visualization.renderers.VertexLabelAsShapeRenderer;
import org.apache.commons.collections4.Transformer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
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
        // Esse código abaixo somente é usado no caso de não possuir uma implementação de ícone personalizado para o livro correspondente
        //<editor-fold desc="Mudar cor dos vértices">
        final DefaultModalGraphMouse<String, Integer> graphMouse = new DefaultModalGraphMouse<String, Integer>();
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
        //</editor-fold>

        //<editor-fold desc="Tamanho dos vértices">
        Transformer<String,Shape> vertexSize = new Transformer<String,Shape>(){
            public Shape transform(String i){
                Ellipse2D circle = new Ellipse2D.Double(-15, -15, 30, 30);
                // in this case, the vertex is twice as large
                return AffineTransform.getScaleInstance(2.5, 2.5).createTransformedShape(circle);
            }
        };
        //</editor-fold>

        //<editor-fold desc="Ícones nos vértices">
        Transformer<String, Icon> vertexIcon = new Transformer<String,Icon>() {
            public Icon transform(String vertice) {

                ImageIcon iconLivro2 = new ImageIcon("src\\Fontes\\livro2.png");
                ImageIcon iconLivro3 = new ImageIcon("src\\Fontes\\livro3.png");
                ImageIcon iconLivro8 = new ImageIcon("src\\Fontes\\livro8.png");
                ImageIcon iconLivro9 = new ImageIcon("src\\Fontes\\livro9.png");
                ImageIcon iconMatricula = new ImageIcon("src\\Fontes\\matricula.png");

                if (vertice.contains("TRA")) {
                    return iconLivro3;
                } else if (vertice.contains("MAT")) {
                    return iconMatricula;
                } else if (vertice.contains("LV8")) {
                    return iconLivro8;
                } else if (vertice.contains("2RI")) {
                    return iconLivro2;
                } else if (vertice.contains("RIC")) {
                    return iconLivro9;
                } else if (vertice.contains("Transcricao Antiga")) {
                    return iconLivro3;
                } else {
                    return null;
                }
            }
        };
        //</editor-fold>

        //<editor-fold desc="Mudar layout das arestas">
        Transformer<String, Stroke> edgeStrokeTransformer = new Transformer<String, Stroke>() {
            float test[] = {20.0f};
            final Stroke linhaMaior = new BasicStroke(1.0f, BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_MITER, 20.0f, test, 0.0f);
            public Stroke transform(String s) {
                return linhaMaior;
            }
        };
        //</editor-fold>

        //<editor-fold desc="Cor do texto dos vértices">
        Color cor = Color.WHITE;
        DefaultVertexLabelRenderer vertexLabelRenderer = new DefaultVertexLabelRenderer(cor) {
                    @Override
                    public <V> Component getVertexLabelRendererComponent(JComponent vv, Object value, Font font, boolean isSelected, V vertex) {
                        super.getVertexLabelRendererComponent(vv, value, font, isSelected, vertex);
//                        if (!filhosImediatos.isEmpty()) {
//                            for (String filho : filhosImediatos) {
//                                if (filho.equalsIgnoreCase(vertex.toString())) {
//                                    cor = Color.BLACK;
//                                }
//                            }
//                        }
                        setForeground(cor);
                        setFont(new Font("Trebuchet MS", Font.BOLD, 12));
                        return this;
                    }
                };
        //</editor-fold>


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

        TreeLayout<String, String> layout = new TreeLayout<>(grafo, 75, 75);

        // Criação do visualizador do grafo
        VisualizationViewer<String, String> vv = new VisualizationViewer<>(layout, new Dimension(500, 500));
        final GraphZoomScrollPane panel = new GraphZoomScrollPane(vv);

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

        vv.getRenderContext().setVertexLabelTransformer(
                // Esse código serve para criar o texto que aparecerá na visualização dos ícones
                // Ele divide a String no primeiro espaço que achar e quebra em 2 (troca um espaço por um enter)
                Functions.<Object,String,String>compose(
                        new Function<String,String>(){
                            public String apply(String input) {
                                return "<html><center><span style=\"color: white;\"><br>" + input.substring(0, input.indexOf(" ")) + "<br>" + input.substring(input.indexOf(" ")) + "</span>";
                            }
                        }, new ToStringLabeller()));
        vv.getRenderContext().setVertexIconTransformer(vertexIcon::transform);
//        vv.getRenderContext().setVertexLabelTransformer(String::toString);
//        vv.getRenderContext().setVertexLabelRenderer(vertexLabelRenderer);
//        vv.getRenderContext().setVertexLabelRenderer(new DefaultVertexLabelRenderer(Color.WHITE));
        vv.getRenderContext().setEdgeStrokeTransformer(edgeStrokeTransformer::transform);
        vv.getRenderContext().setEdgeShapeTransformer(EdgeShape.line(grafo));
        vv.getRenderContext().setVertexFillPaintTransformer(vertexColor::transform);
        vv.getRenderContext().setVertexShapeTransformer(vertexSize::transform);
        vv.getRenderContext().setArrowFillPaintTransformer(Functions.<Paint>constant(Color.lightGray));
        vv.setGraphMouse(graphMouse);
        vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);


        //<editor-fold desc="Implementação do Zoom">
        final ScalingControl scaler = new CrossoverScalingControl();
        scaler.scale(vv, 1.1f, vv.getLocation());
        scaler.scale(vv, 1.1f, vv.getLocation());
        scaler.scale(vv, 1.1f, vv.getLocation());

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
        //</editor-fold>


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
