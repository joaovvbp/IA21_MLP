package MLP;

import Dados.Exemplo;
import Dados.Holdout;

import java.util.ArrayList;

public class MLP {
    //Constantes
    final int TAM_ENTRADA = 64;
    final int TAM_SAIDA = 10;

    //Parâmetros
    public double momentum;
    public double taxaDeAprendizado;

    public Camada camadaOculta;
    public Camada camadaSaida;

    public ArrayList<double[]> erros_quadraticos = new ArrayList<double[]>();
    public double erros_exemplo = 0;//Somatório dos erros quadrático para cada exemplo
    public double erro_geral = 0;//Somatório dos erros de todos os exemplos

    //Sao inicializadas as camadas, chamando os construtores dos neuronios, onde sao inicializados os pesos
    public MLP(int neuroniosCamadaOculta, double taxaDeAprendizado, double momentum) {
        this.taxaDeAprendizado = taxaDeAprendizado;
        this.momentum = momentum;
        camadaOculta = new Camada(neuroniosCamadaOculta, TAM_ENTRADA);
        camadaSaida = new Camada(TAM_SAIDA, camadaOculta.tamanhoCamada);
    }

    //Propaga o exemplo ao longo da rede, o método de soma ponderada já calcula
    public void forwardPropagation(Double[] entrada, MLP rede) {
        for (int i = 0; i < rede.camadaOculta.tamanhoCamada; i++) {
            rede.camadaOculta.neuronios[i].propagaOculta(entrada);
        }
        for (int i = 0; i < rede.camadaSaida.tamanhoCamada; i++) {
            rede.camadaSaida.neuronios[i].propagaSaida(rede.camadaOculta);
        }
    }

    //Itera
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

    public void calculaErroNeuronioSaida(Neuronio neuronio, double saida, double esperado) {
        neuronio.ultimo_erro = saida * (1 - saida) * (esperado - saida);
    }

    public void calculaErroNeuronioOculto(Neuronio neuronio, double saida) {
        double somatoriaSaida = 0.0;
        for (int i = 0; i < TAM_SAIDA; i++) {
            somatoriaSaida += (camadaSaida.neuronios[i].ultimo_erro * camadaSaida.neuronios[i].pesos[neuronio.ID]);
        }
        neuronio.ultimo_erro = saida * ((1 - saida) * somatoriaSaida);
    }

    public void erroQuadratico(Exemplo exemplo) {
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
                camadaSaida.neuronios[i].ultimo_ajuste[j] = delta;

                camadaSaida.neuronios[i].pesos[j] = camadaSaida.neuronios[i].pesos[j] + delta;
            }
        }
    }

    //A função utiliza uma entrada Xij para o cálculo do ajuste dos pesos, portanto este método precisa receber como entrada um dos vetores do conjunto de treinamento.
    public void ajustaPesosCamadaOculta(Double[] entrada) {
        for (int i = 0; i < camadaOculta.tamanhoCamada; i++) {
            double delta = 0;
            for (int j = 0; j < TAM_ENTRADA; j++) {
                delta = taxaDeAprendizado * camadaOculta.neuronios[i].ultimo_erro * entrada[j] + momentum * camadaOculta.neuronios[i].ultimo_ajuste[j];
                camadaOculta.neuronios[i].ultimo_ajuste[j] = delta;

                camadaOculta.neuronios[i].pesos[j] = camadaOculta.neuronios[i].pesos[j] + delta;
            }
        }
    }

    public double treinaRede(MLP rede) {
        rede.erro_geral = 0.0;
        //Passa por todos as entradas do conjunto de treinamento
        for (int i = 0; i < Holdout.conjTreinamento.size(); i++) {
            int classe_esperada = Holdout.conjTreinamento.get(i).retornaRotulo();

            //Propraga a entrada pela rede, calcula a soma ponderada, funções de ativação e armazena a saída nos neurônios
            rede.forwardPropagation(Holdout.conjTreinamento.get(i).vetorEntradas, rede);

            //Calcula o erro de cada um dos neurônios da camada de saída
            for (int j = 0; j < rede.camadaSaida.tamanhoCamada; j++) {
                if (j == classe_esperada) {
                    rede.calculaErroNeuronioSaida(rede.camadaSaida.neuronios[j], rede.camadaSaida.neuronios[j].saida, 1);
                } else {
                    rede.calculaErroNeuronioSaida(rede.camadaSaida.neuronios[j], rede.camadaSaida.neuronios[j].saida, 0);
                }
            }

            //Calcula o erro de cada um dos neurônios da camada oculta
            for (int k = 0; k < rede.camadaOculta.tamanhoCamada; k++) {
                rede.calculaErroNeuronioOculto(rede.camadaOculta.neuronios[k], rede.camadaOculta.neuronios[k].saida);
            }

            //Ajusta os pesos da camada oculta e de saída
            rede.ajustaPesosCamadaOculta(Holdout.conjTreinamento.get(i).vetorEntradas);
            rede.ajustaPesosCamadaSaida();

            //Calcula o erro quadrático iterativamente, armazenando os somatórios na rede
            rede.erroQuadratico(Holdout.conjTreinamento.get(i));
            rede.erros_exemplo = 0;
        }

        //Retorna o erro quadrático da época
        return (0.5) * (rede.erro_geral);
    }
}
