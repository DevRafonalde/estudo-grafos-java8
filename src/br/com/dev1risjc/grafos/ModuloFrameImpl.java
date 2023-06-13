package br.com.dev1risjc.grafos;

import br.com.dev1risjc.interfaceContrato.InterfaceContrato;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;

public class ModuloFrameImpl implements IModuloFrame {

    GeradorGrafo telaGrafos;

    public ModuloFrameImpl(List<JMenuItem> itensPopup, HashMap<InterfaceContrato, InterfaceContrato> filiacoes) {
        this.telaGrafos = new GeradorGrafo(itensPopup, filiacoes);
        telaGrafos.init();
        telaGrafos.setLocationRelativeTo(null);
        telaGrafos.setTitle("Grafo de filiação de TRAs");
        telaGrafos.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        telaGrafos.pack();
    }

    @Override
    public void setVisible(boolean visible) {
        telaGrafos.setVisible(visible);
    }

    @Override
    public void setSize(int x, int y) {
        telaGrafos.setSize(x, y);
    }

    @Override
    public void limparTela() {
        telaGrafos.limparTela();
    }

    @Override
    public void criarVertices() {
        telaGrafos.criarVertices();
    }

    @Override
    public void criarArestas() {
        telaGrafos.criarArestas();
    }

    @Override
    public void refazerTela() {
        limparTela();
        criarVertices();
        criarArestas();
    }
}
