package MLP;

public class MLP {
    final int MIN_PESO = -1;
    final int MAX_PESO = 1;
    final int TAM_ENTRADA = 64;

    public Camada camadaOculta;
    public Camada camadaSaida;
    public double taxaDeAprendizado;

    public MLP(int neuroniosCamadaOculta, double taxaDeAprendizado) {
        this.taxaDeAprendizado = taxaDeAprendizado;
        camadaOculta = new Camada(neuroniosCamadaOculta, TAM_ENTRADA);
        camadaSaida = new Camada(10, camadaOculta.tamanho);
    }

    // INSTRUCOES PARA A ETAPA DE PROPAGACAO
//    ◼ Passo 1.1) Dado o exemplo E como entrada da
//    rede, determine:
//            – as saídas dos neurônios ocultos (que serão os
//            sinais de entrada para as unidades de saída).
//            – as saídas dos neurônios de saída (que indicam a
//            estimativa de classe para o exemplo E).
//    Passo 1.2) Registre os valores desejado e obtido para o exemplo E.
//            – considere ti
//            (E) o valor desejado para a unidade de saída i, para o
//    exemplo E.
//            – considere oi
//            (E) o valor obtido para a unidade de saída i, para o
//    exemplo E.
//            ◼ Para tarefas de classificação, por exemplo:
//            – Cada ti
//            (E) será igual a 0, exceto para um único tj
//            (E), que será igual
//    a 1.
//            – Mas, oi
//            (E) será, na verdade, um valor real.
//            ◼ Os valores de saída dos neurônios ocultos hi
//    também devem ser
//    registrados.

    public double forwardPropagation(){
        double output = 0;
        //Passar os valores pela rede toda, ate receber um output
            //
        return output;
    }

}
