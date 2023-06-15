package br.com.dev1risjc.grafos.contrato;

public interface InterfaceContrato <V> {
//    public void visualizarImagem(String numeroLivro);
//    public void visualizadorPaginas(String numeroLivro);
//    public void operarLivro(String numeroLivro);
//    public void encerrarLivro(String numeroLivro);
//    public void editarDataAto0(String numeroLivro);
//    public void filiacoesAnteriores(String numeroLivro);
//    public void filiacoesPosteriores(String numeroLivro);
//    public void visualizarInscricoes(String numeroLivro);
//    public void navegacaoAvancada(String numeroLivro);

    public V getObjeto();
    public boolean isPai();
    public boolean isFilho();
    public String toString();

}
