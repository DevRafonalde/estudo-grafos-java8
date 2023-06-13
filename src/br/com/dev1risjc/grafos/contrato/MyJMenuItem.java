package br.com.dev1risjc.grafos.contrato;

import javax.swing.*;

public class MyJMenuItem <V> extends JMenuItem {

    IMyJMenuItem injecao;
    public MyJMenuItem(IMyJMenuItem injecao) {
        this.injecao = injecao;
    }

    public void executar(V object) {
        injecao.executar(object);
    }
}
