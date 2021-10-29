package MLP;

import Processamento.Exemplo;

import java.util.ArrayList;
import java.util.List;

public class MLP {
    final int TAM_ENTRADA = 64;
    final int TAM_SAIDA = 10;
    public double momentum;

    public Camada camadaOculta;
    public Camada camadaSaida;
    public double taxaDeAprendizado;

    public double erros_exemplo = 0;
    public double erro_geral = 0;

    //Sao inicializadas as camadas, chamando os construtores dos neuronios, onde sao inicializados os pesos
    public MLP(int neuroniosCamadaOculta, double taxaDeAprendizado) {
        this.taxaDeAprendizado = taxaDeAprendizado;
        camadaOculta = new Camada(neuroniosCamadaOculta, TAM_ENTRADA);
        camadaSaida = new Camada(TAM_SAIDA, camadaOculta.tamanhoCamada);
    }

    public void forwardPropagation(Double[] entrada, MLP rede) {
        for (int i = 0; i < rede.camadaOculta.tamanhoCamada; i++) {
            rede.camadaOculta.neuronios[i].somaPonderadaOculta(entrada);
        }
        for (int i = 0; i < rede.camadaSaida.tamanhoCamada; i++) {
            rede.camadaSaida.neuronios[i].somaPonderadaSaida(rede.camadaOculta);
        }
    }

    public int[] converteSaida(Camada camadaSaida) {
        double maior_saida = -1.0;
        int classe = -1;

        for (int i = 0; i < camadaSaida.tamanhoCamada; i++) {
            if (camadaSaida.neuronios[i].saida > maior_saida) {
                maior_saida = camadaSaida.neuronios[i].saida;
                classe = camadaSaida.neuronios[i].ID;
            }
        }
        //Setando o array
        int[] saida = new int[10];
        saida[classe] = 1;

        return saida;
    }

    public int retornaRotulo(int[] saida) {
        int rotulo = -1;
        for (int i = 0; i < saida.length; i++)
            if (saida[i] == 1) {
                rotulo = i;
                break;
            }
        return rotulo;
    }

    public void calculaErroNeuronioSaida(Neuronio neuronio, double saida, double esperado) {//Seria interessante armazenar esse erro em algum local, pra nao calcular duas vezes
        neuronio.ultimo_erro = saida * (1 - saida) * (esperado - saida);
    }

    public void calculaErroNeuronioOculto(Neuronio neuronio, double saida) {//Nao calcula
        //Somatoria dos erros de saida
        double somatoriaSaida = 0.0;
        for (int i = 0; i < TAM_SAIDA; i++) {
            somatoriaSaida += (camadaSaida.neuronios[i].ultimo_erro * camadaSaida.neuronios[i].pesos[neuronio.ID]);
        }
        neuronio.ultimo_erro = saida * ((1 - saida) * somatoriaSaida);
    }

    public void calculaErroTotal(Exemplo exemplo) {
        int o_esperado = exemplo.retornaRotulo();
        for (int j = 0; j < TAM_SAIDA; j++) {
            if (o_esperado == j) {
                erros_exemplo += Math.pow((1 - camadaSaida.neuronios[j].saida), 2);
            } else {
                erros_exemplo += Math.pow((0 - camadaSaida.neuronios[j].saida), 2);
            }
        }
        erro_geral += erros_exemplo;
    }

    public void ajustaPesosCamadaSaida() {

        for (int i = 0; i < TAM_SAIDA; i++) {
            double delta = 0;
            for (int j = 0; j < camadaOculta.tamanhoCamada; j++) {
                delta = taxaDeAprendizado * camadaSaida.neuronios[i].ultimo_erro * camadaOculta.neuronios[j].saida + momentum * camadaSaida.neuronios[i].ultimo_ajuste[j];
                //Alterei o ultimo ajuste pra representar o ajuste de cada peso, tambem alterei o indice do parametro correspondente ao Xij na funcao
                camadaSaida.neuronios[i].ultimo_ajuste[j] = delta;

                camadaSaida.neuronios[i].pesos[j] = camadaSaida.neuronios[i].pesos[j] + delta;
            }
            camadaSaida.neuronios[i].normalizaPesos();
        }
    }

    public void ajustaPesosCamadaOculta(Double[] entrada) {

        for (int i = 0; i < camadaOculta.tamanhoCamada; i++) {
            double delta = 0;
            for (int j = 0; j < TAM_ENTRADA; j++) {
                delta = taxaDeAprendizado * camadaOculta.neuronios[i].ultimo_erro * entrada[j] + momentum * camadaOculta.neuronios[i].ultimo_ajuste[j];//Com uma entrada 0, isso resulta num ajuste de 0
                camadaOculta.neuronios[i].ultimo_ajuste[j] = delta;

                camadaOculta.neuronios[i].pesos[j] = camadaOculta.neuronios[i].pesos[j] + delta;
            }
            camadaOculta.neuronios[i].normalizaPesos();
        }
    }

}
