package MLP;

import Dados.Exemplo;
import Dados.Holdout;
import Dados.KFold;

import java.util.ArrayList;
import java.util.List;

public class MLP {
    //Constantes
    final int TAM_ENTRADA = 64;
    final int TAM_SAIDA = 10;

    //Parâmetros
    public double momentum;
    public double taxaDeAprendizado;

    public Camada camadaOculta;
    public Camada camadaSaida;

    public double erros_teste = 0;
    public double erros_valid = 0;
    public double erros_treino = 0;

    public double erro_geral_teste = 0;
    public double erro_geral_valid = 0;
    public double erro_geral_treino = 0;

    public double erro_final_teste = 0;
    public double erro_final_valid = 0;
    public double erro_final_treino = 0;

    public ArrayList<double[]> erros_quadraticos_teste = new ArrayList<>();//Usado para armazenar as epocas
    public ArrayList<double[]> erros_quadraticos_valid = new ArrayList<>();//Usado para armazenar as epocas
    public ArrayList<double[]> erros_quadraticos_treino = new ArrayList<>();//Usado para armazenar as epocas

    public ArrayList<double[]> erros_verdadeiros_folds = new ArrayList();

    //Sao inicializadas as camadas, chamando os construtores dos neuronios, onde sao inicializados os pesos
    public MLP(int neuroniosCamadaOculta, double taxaDeAprendizado, double momentum) {
        this.taxaDeAprendizado = taxaDeAprendizado;
        this.momentum = momentum;
        camadaOculta = new Camada(neuroniosCamadaOculta, TAM_ENTRADA);
        camadaSaida = new Camada(TAM_SAIDA, camadaOculta.tamanhoCamada);
    }

    //Propaga o exemplo ao longo da rede, o método de soma ponderada já calcula
    public void forwardPropagation(Double[] entrada) {
        for (int i = 0; i < camadaOculta.tamanhoCamada; i++) {
            camadaOculta.neuronios[i].propagaOculta(entrada);
        }
        for (int i = 0; i < camadaSaida.tamanhoCamada; i++) {
            camadaSaida.neuronios[i].propagaSaida(camadaOculta);
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

    //Calcula o erro quadratico armazenando resultados parciais em atributos da rede
    //Armazena em variaveis de acordo com o conjunto para qual o erro esta sendo calculado
    public void erroQuadratico(Exemplo exemplo, int conjunto) {
        int o_esperado = exemplo.retornaRotulo();
        for (int j = 0; j < TAM_SAIDA; j++) {
            if (o_esperado == j) {
                switch (conjunto) {
                    case (1):
                        erros_teste += Math.pow((1 - camadaSaida.neuronios[j].saida), 2);
                        break;
                    case (2):
                        erros_valid += Math.pow((1 - camadaSaida.neuronios[j].saida), 2);
                        break;
                    case (3):
                        erros_treino += Math.pow((1 - camadaSaida.neuronios[j].saida), 2);
                        break;
                }
            } else {
                switch (conjunto) {
                    case (1):
                        erros_teste += Math.pow((0 - camadaSaida.neuronios[j].saida), 2);
                        break;
                    case (2):
                        erros_valid += Math.pow((0 - camadaSaida.neuronios[j].saida), 2);
                        break;
                    case (3):
                        erros_treino += Math.pow((0 - camadaSaida.neuronios[j].saida), 2);
                        break;
                }
            }
        }
        switch (conjunto) {
            case (1):
                erro_geral_teste += erros_teste;
                break;
            case (2):
                erro_geral_valid += erros_valid;
                break;
            case (3):
                erro_geral_treino += erros_treino;
                break;
        }
    }

    public void ajustaPesosCamadaSaida() {
        for (int i = 0; i < TAM_SAIDA; i++) {
            double delta;
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
            double delta;
            for (int j = 0; j < TAM_ENTRADA; j++) {
                delta = taxaDeAprendizado * camadaOculta.neuronios[i].ultimo_erro * entrada[j] + momentum * camadaOculta.neuronios[i].ultimo_ajuste[j];
                camadaOculta.neuronios[i].ultimo_ajuste[j] = delta;

                camadaOculta.neuronios[i].pesos[j] = camadaOculta.neuronios[i].pesos[j] + delta;
            }
        }
    }

    public void treinaRedeKFold(MLP rede, List<Exemplo> conjunto) {
        //Passa por todos as entradas do conjunto de treinamento
        for (int i = 0; i < conjunto.size(); i++) {
            int classe_esperada = conjunto.get(i).retornaRotulo();

            //Propraga a entrada pela rede, calcula a soma ponderada, funções de ativação e armazena a saída nos neurônios
            rede.forwardPropagation(conjunto.get(i).vetorEntradas);

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
            rede.ajustaPesosCamadaOculta(conjunto.get(i).vetorEntradas);
            rede.ajustaPesosCamadaSaida();

            //Calcula o erro quadrático iterativamente, armazenando os somatórios na rede
            rede.erroQuadratico(conjunto.get(i), 3);
            rede.erros_treino = 0;
        }
        //Retorna o erro quadrático da época
        erro_final_treino = (0.5) * (rede.erro_geral_treino);
    }

    public void treinaRedeHoldout(MLP rede) {
        rede.erro_geral_teste = 0.0;
        rede.erro_geral_valid = 0.0;
        rede.erro_geral_treino = 0.0;
        //Passa por todos as entradas do conjunto de treinamento

        for (int i = 0; i < Holdout.conjTreinamento.size(); i++) {
            int classe_esperada = Holdout.conjTreinamento.get(i).retornaRotulo();

            if (i < Holdout.conjTeste.size()) {
                rede.forwardPropagation(Holdout.conjTeste.get(i).vetorEntradas);
                rede.erroQuadratico(Holdout.conjTeste.get(i), 1);
                rede.erros_teste = 0;
                erro_final_teste = (0.5) * (rede.erro_geral_teste);
            }

            if (i < Holdout.conjValidacao.size()) {
                rede.forwardPropagation(Holdout.conjValidacao.get(i).vetorEntradas);
                rede.erroQuadratico(Holdout.conjValidacao.get(i), 2);
                rede.erros_valid = 0;
                erro_final_valid = (0.5) * (rede.erro_geral_valid);
            }

            //Propraga a entrada pela rede, calcula a soma ponderada, funções de ativação e armazena a saída nos neurônios
            rede.forwardPropagation(Holdout.conjTreinamento.get(i).vetorEntradas);

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
            rede.erroQuadratico(Holdout.conjTreinamento.get(i), 3);
            rede.erros_treino = 0;
        }
        //Retorna o erro quadrático da época
        erro_final_treino = (0.5) * (rede.erro_geral_treino);
    }
}
