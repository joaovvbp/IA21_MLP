package MLP;

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

import java.util.Arrays;

import static java.lang.Math.*;

public class MLP {
    final int MIN_PESO = -1;
    final int MAX_PESO = 1;
    final int TAM_ENTRADA = 64;

    public Camada camadaOculta;
    public Camada camadaSaida;
    public double taxaDeAprendizado;

    //Sao inicializadas as camadas, chamando os construtores dos neuronios, onde sao inicializados os pesos
    public MLP(int neuroniosCamadaOculta, double taxaDeAprendizado) {
        this.taxaDeAprendizado = taxaDeAprendizado;
        camadaOculta = new Camada(neuroniosCamadaOculta, TAM_ENTRADA);
        camadaSaida = new Camada(10, camadaOculta.tamanho);
    }

    //Calcula a soma ponderada para cada neuronio, armazena o resultado dentro do neuronio no campo valor (Deve funcionar tanto pra camada oculta quanto de saida)
    public void somaPonderada(double[] entrada, Neuronio neuronio) {
        neuronio.valor[0] = 0;
        neuronio.normalizaPesos();
        for (int i = 0; i < entrada.length; i++) {
            neuronio.valor[0] += entrada[i] * neuronio.pesos[i];
            System.out.println("R("+neuronio.valor[0]+")IN("+entrada[i]+")"+"* PESO("+neuronio.pesos[i]+") ");
        }
    }

    //Calcula a saida da camada oculta e de saida
    public double sigmoide(double valorDoNeuronio) {
        double saida;
        saida = 1/(1+exp(valorDoNeuronio));
        return saida;
    }

    /* @param camada especifica a camada em que sera calculado o erro, 0 para oculta e 1 para saida*/

    public double forwardPropagation(double[] entrada, MLP rede) {
        double output = 0;
        for (int i = 0; i < rede.camadaOculta.tamanho; i++) {
            somaPonderada(entrada, camadaOculta.neuronios[i]);
        }
        for (int i = 0; i < camadaSaida.tamanho; i++) {
            somaPonderada(rede.camadaOculta.neuronios[i].valor,camadaSaida.neuronios[i]);
        }
        return output;
    }

}
