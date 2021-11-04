package mlp;

import dados.Exemplo;
import dados.Holdout;

import java.util.ArrayList;
import java.util.List;

public class MLP {
    //Constantes
    static final int TAM_ENTRADA = 64;
    static final int TAM_SAIDA = 10;

    //Parâmetros
    public double momentum;
    public double taxaDeAprendizado;

    public Camada camadaOculta;
    public Camada camadaSaida;

    public double errosTeste = 0;
    public double errosValid = 0;
    public double errosTreino = 0;

    public double erroGeralTeste = 0;
    public double erroGeralValid = 0;
    public double erroGeralTreino = 0;

    public double erroFinalTeste = 0;
    public double erroFinalValid = 0;
    public double erroFinalTreino = 0;

    public ArrayList<double[]> errosQuadraticosTeste = new ArrayList<>();
    public ArrayList<double[]> errosQuadraticosValid = new ArrayList<>();
    public ArrayList<double[]> errosQuadraticosTreino = new ArrayList<>();
    public ArrayList<double[]> errosVerdadeirosFolds = new ArrayList<>();
    public ArrayList<Double> errosMedioFolds = new ArrayList<>();

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
        double maiorSaida = -1.0;
        int classe = -1;

        for (int i = 0; i < camadaSaida.tamanhoCamada; i++) {
            if (camadaSaida.neuronios[i].saida > maiorSaida) {
                maiorSaida = camadaSaida.neuronios[i].saida;
                classe = camadaSaida.neuronios[i].Id;
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
        neuronio.ultimoErro = saida * (1 - saida) * (esperado - saida);
    }

    public void calculaErroNeuronioOculto(Neuronio neuronio, double saida) {
        double somatoriaSaida = 0.0;
        for (int i = 0; i < TAM_SAIDA; i++) {
            somatoriaSaida += (camadaSaida.neuronios[i].ultimoErro * camadaSaida.neuronios[i].pesos[neuronio.Id]);
        }
        neuronio.ultimoErro = saida * ((1 - saida) * somatoriaSaida);
    }

    //Calcula o erro quadratico armazenando resultados parciais em atributos da rede
    //Armazena em variaveis de acordo com o conjunto para qual o erro esta sendo calculado
    public void erroQuadratico(Exemplo exemplo, int conjunto) {
        int oEsperado = exemplo.retornaRotulo();
        for (int j = 0; j < TAM_SAIDA; j++) {
            if (oEsperado == j) {
                switch (conjunto) {
                    case (1):
                        errosTeste += Math.pow((1 - camadaSaida.neuronios[j].saida), 2);
                        break;
                    case (2):
                        errosValid += Math.pow((1 - camadaSaida.neuronios[j].saida), 2);
                        break;
                    case (3):
                        errosTreino += Math.pow((1 - camadaSaida.neuronios[j].saida), 2);
                        break;
                }
            } else {
                switch (conjunto) {
                    case (1):
                        errosTeste += Math.pow((0 - camadaSaida.neuronios[j].saida), 2);
                        break;
                    case (2):
                        errosValid += Math.pow((0 - camadaSaida.neuronios[j].saida), 2);
                        break;
                    case (3):
                        errosTreino += Math.pow((0 - camadaSaida.neuronios[j].saida), 2);
                        break;
                }
            }
        }
        switch (conjunto) {
            case (1):
                erroGeralTeste += errosTeste;
                break;
            case (2):
                erroGeralValid += errosValid;
                break;
            case (3):
                erroGeralTreino += errosTreino;
                break;
        }
    }

    public void ajustaPesosCamadaSaida() {
        for (int i = 0; i < TAM_SAIDA; i++) {
            double delta;
            for (int j = 0; j < camadaOculta.tamanhoCamada; j++) {
                delta = taxaDeAprendizado * camadaSaida.neuronios[i].ultimoErro * camadaOculta.neuronios[j].saida + momentum * camadaSaida.neuronios[i].ultimoAjuste[j];
                camadaSaida.neuronios[i].ultimoAjuste[j] = delta;

                camadaSaida.neuronios[i].pesos[j] = camadaSaida.neuronios[i].pesos[j] + delta;
            }
        }
    }

    //A função utiliza uma entrada Xij para o cálculo do ajuste dos pesos, portanto este método precisa receber como entrada um dos vetores do conjunto de treinamento.
    public void ajustaPesosCamadaOculta(Double[] entrada) {
        for (int i = 0; i < camadaOculta.tamanhoCamada; i++) {
            double delta;
            for (int j = 0; j < TAM_ENTRADA; j++) {
                delta = taxaDeAprendizado * camadaOculta.neuronios[i].ultimoErro * entrada[j] + momentum * camadaOculta.neuronios[i].ultimoAjuste[j];
                camadaOculta.neuronios[i].ultimoAjuste[j] = delta;

                camadaOculta.neuronios[i].pesos[j] = camadaOculta.neuronios[i].pesos[j] + delta;
            }
        }
    }

    public void treinaRedeKFold(MLP rede, List<Exemplo> conjunto) {
        //Passa por todos as entradas do conjunto de treinamento
        for (Exemplo exemplo : conjunto) {
            int classeEsperada = exemplo.retornaRotulo();

            //Propraga a entrada pela rede, calcula a soma ponderada, funções de ativação e armazena a saída nos neurônios
            rede.forwardPropagation(exemplo.vetorEntradas);

            //Calcula o erro de cada um dos neurônios da camada de saída
            for (int j = 0; j < rede.camadaSaida.tamanhoCamada; j++) {
                if (j == classeEsperada) {
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
            rede.ajustaPesosCamadaOculta(exemplo.vetorEntradas);
            rede.ajustaPesosCamadaSaida();

            //Calcula o erro quadrático iterativamente, armazenando os somatórios na rede
            rede.erroQuadratico(exemplo, 3);
            rede.errosTreino = 0;
        }
        //Retorna o erro quadrático da época
        erroFinalTreino = (0.5) * (rede.erroGeralTreino);
    }

    public void treinaRedeHoldout(MLP rede) {
        rede.erroGeralTeste = 0.0;
        rede.erroGeralValid = 0.0;
        rede.erroGeralTreino = 0.0;
        //Passa por todos as entradas do conjunto de treinamento

        for (int i = 0; i < Holdout.conjTreinamento.size(); i++) {
            int classeEsperada = Holdout.conjTreinamento.get(i).retornaRotulo();

            if (i < Holdout.conjTeste.size()) {
                rede.forwardPropagation(Holdout.conjTeste.get(i).vetorEntradas);
                rede.erroQuadratico(Holdout.conjTeste.get(i), 1);
                rede.errosTeste = 0;
                erroFinalTeste = (0.5) * (rede.erroGeralTeste);
            }

            if (i < Holdout.conjValidacao.size()) {
                rede.forwardPropagation(Holdout.conjValidacao.get(i).vetorEntradas);
                rede.erroQuadratico(Holdout.conjValidacao.get(i), 2);
                rede.errosValid = 0;
                erroFinalValid = (0.5) * (rede.erroGeralValid);
            }

            //Propraga a entrada pela rede, calcula a soma ponderada, funções de ativação e armazena a saída nos neurônios
            rede.forwardPropagation(Holdout.conjTreinamento.get(i).vetorEntradas);

            //Calcula o erro de cada um dos neurônios da camada de saída
            for (int j = 0; j < rede.camadaSaida.tamanhoCamada; j++) {
                if (j == classeEsperada) {
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
            rede.errosTreino = 0;
        }
        //Retorna o erro quadrático da época
        erroFinalTreino = (0.5) * (rede.erroGeralTreino);
    }
}
