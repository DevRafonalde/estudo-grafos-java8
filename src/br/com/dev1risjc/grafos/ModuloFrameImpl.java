package br.com.dev1risjc.grafos;

import br.com.dev1risjc.grafos.contrato.InterfaceContrato;
import br.com.dev1risjc.grafos.contrato.MyJMenuItem;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;

public class ModuloFrameImpl implements IModuloFrame {

    GeradorGrafo telaGrafos;

    public ModuloFrameImpl(List<MyJMenuItem> itensPopup, HashMap<InterfaceContrato, List<InterfaceContrato>> filiacoes, List<String> filhosImediatos, Integer livroPesquisado) {
        this.telaGrafos = new GeradorGrafo(itensPopup, filiacoes, filhosImediatos, livroPesquisado);
//        telaGrafos.setLocationRelativeTo(null);
//        telaGrafos.setTitle("Grafo de filiação de TRAs");
//        telaGrafos.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

//    @Override
//    public void criarVertices() {
//        telaGrafos.criarVertices();
//    }
//
//    @Override
//    public void criarArestas() {
//        telaGrafos.criarArestas();
//    }

    @Override
    public void refazerTela() {
        limparTela();
        telaGrafos.init();
    }

    @Override
    public void criarGrafo() {
        telaGrafos.init();
        telaGrafos.setTitle("Grafo de filiação de TRAs");
        telaGrafos.setLocationRelativeTo(null);
        telaGrafos.pack();
        telaGrafos.setVisible(true);
        telaGrafos.pack();
    }
}
