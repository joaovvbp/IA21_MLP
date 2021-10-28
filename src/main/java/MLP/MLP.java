package MLP;

import Processamento.Exemplo;

import java.util.ArrayList;
import java.util.List;

public class MLP {
    final int TAM_ENTRADA = 64;
    final int TAM_SAIDA = 10;
    public double momentum;

    public List<int[]> saidas_da_rede = new ArrayList<>();

    public Camada camadaOculta;
    public Camada camadaSaida;
    public double taxaDeAprendizado;

    //Construtor vazio para utilizacao nos testes
    public MLP() {
    }

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

    public double calculaErroTotal(List<Exemplo> conjunto) {
        double somatorio_saidas = 0;
        double somatorio_exemplos = 0;
        for (int i = 0; i < conjunto.size() - 1; i++) {
            int o_esperado = conjunto.get(i).retornaRotulo();
            for (int j = 0; j < TAM_SAIDA; j++) {
                if (o_esperado == j) {
                    somatorio_saidas += Math.pow((1 - camadaSaida.neuronios[j].saida), 2);
                } else {
                    somatorio_saidas += Math.pow((0 - camadaSaida.neuronios[j].saida), 2);
                }
            }
            somatorio_exemplos += somatorio_saidas;
            somatorio_saidas = 0;
        }
        return (0.5) * (somatorio_exemplos);
    }

    public void ajustaPesosCamadaSaida() {
        for (int i = 0; i < TAM_SAIDA; i++) {
            for (int j = 0; j < camadaOculta.tamanhoCamada; j++) {
                double delta = taxaDeAprendizado * camadaSaida.neuronios[i].ultimo_erro * camadaOculta.neuronios[i].saida + momentum * camadaSaida.neuronios[i].ultimo_ajuste;
                camadaSaida.neuronios[i].ultimo_ajuste = delta;

                camadaSaida.neuronios[i].pesos[j] = camadaSaida.neuronios[i].pesos[j] + delta;
            }
            camadaSaida.neuronios[i].normalizaPesos();
        }
    }

    public void ajustaPesosCamadaOculta(Double[] entrada) {
        for (int i = 0; i < camadaOculta.tamanhoCamada; i++) {
            for (int j = 0; j < TAM_ENTRADA; j++) {
                double delta = taxaDeAprendizado * camadaOculta.neuronios[i].ultimo_erro * entrada[i] + momentum * camadaOculta.neuronios[i].ultimo_ajuste;//Com uma entrada 0, isso resulta num ajuste de 0
                camadaOculta.neuronios[i].ultimo_ajuste = delta;

                camadaOculta.neuronios[i].pesos[j] = camadaOculta.neuronios[i].pesos[j] + delta;
            }
            camadaOculta.neuronios[i].normalizaPesos();
        }
    }

}
