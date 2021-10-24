package MLP;

import Processamento.Exemplo;

import java.util.ArrayList;
import java.util.List;

public class MLP {
    final int MIN_PESO = -1;
    final int MAX_PESO = 1;
    final int TAM_ENTRADA = 64;
    final int TAM_SAIDA = 10;

    public List<int[]> saidas_da_rede = new ArrayList<>();

    public Camada camadaOculta;
    public Camada camadaSaida;
    public double taxaDeAprendizado;

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


    public void ajustaPesosCamadaSaida(int esperado, int saida) {
        for (int i = 0; i < camadaSaida.tamanhoCamada; i++) {
            for (int j = 0; j < camadaOculta.tamanhoCamada; j++) {
                double delta = taxaDeAprendizado * camadaSaida.neuronios[i].ultimo_erro * camadaOculta.neuronios[i].saida;

                double buffer = camadaSaida.neuronios[i].pesos[j]; //APENAS PARA TESTES!!
                System.out.println("Peso original do neuronio de saida "+ i +"("+ j +")"+" = "+ camadaSaida.neuronios[i].pesos[j]);

                camadaSaida.neuronios[i].pesos[j] = camadaSaida.neuronios[i].pesos[j] + delta;

                double somapesos = 0;
                for (double d : camadaSaida.neuronios[i].pesos) {
                    somapesos += d;
                }
                camadaSaida.neuronios[i].pesos[j] = camadaSaida.neuronios[i].pesos[j] / somapesos;


                System.out.println("Peso ajustado do neuronio de saida "+ i +"("+ j +")"+" = "+ (camadaSaida.neuronios[i].pesos[j]));
                System.out.println("Ajuste do neuronio de saida "+ i +"("+ j +")"+" = "+ (buffer - camadaSaida.neuronios[i].pesos[j]));
            }
            System.out.println();
        }
    }

    public void ajustaPesosCamadaOculta(Double[] entrada){
        for (int i = 0; i < camadaOculta.tamanhoCamada; i++) {
            for (int j = 0; j < TAM_ENTRADA; j++) {
                double delta = taxaDeAprendizado * camadaOculta.neuronios[i].ultimo_erro * entrada[i];//Com uma entrada 0, isso resulta num ajuste de 0
                System.out.println("\n\n ENTRADA"+i+" = "+ entrada[i]+"\n\n");

                double buffer = camadaOculta.neuronios[i].pesos[j]; //APENAS PARA TESTES!!
                System.out.println("Peso original do neuronio oculto "+ i +"("+ j +")"+" = "+ camadaOculta.neuronios[i].pesos[j]);

                camadaOculta.neuronios[i].pesos[j] = camadaOculta.neuronios[i].pesos[j] + delta;

                double somapesos = 0;
                for (double d : camadaOculta.neuronios[i].pesos) {
                    somapesos += d;
                }
                camadaOculta.neuronios[i].pesos[j] =  camadaOculta.neuronios[i].pesos[j] / somapesos;

                System.out.println("Peso ajustado do neuronio oculto "+ i +"("+ j +")"+" = "+ (camadaOculta.neuronios[i].pesos[j]));
                System.out.println("Ajuste do neuronio oculto "+ i +"("+ j +")"+" = "+ (buffer - camadaOculta.neuronios[i].pesos[j]));
            }
            System.out.println("\n\n\n");
        }
    }

    public double derivadaSigmoide(int entrada) { //Isso vai ser usado?
        return (double) ((1 - Neuronio.sigmoide(entrada)) * Neuronio.sigmoide(entrada));
    }

    public double calculaErroNeuronioSaida(Neuronio neuronio, double saida, int esperado) {//Seria interessante armazenar esse erro em algum local, pra nao calcular duas vezes
        neuronio.ultimo_erro = saida * (1 - saida) * (esperado - saida);
        return neuronio.ultimo_erro;
    }

    public double calculaErroTotal(List<Exemplo> conjunto, MLP rede) {
        // 1/2 * somatoria de e exemplos * ( somatoria de k saidas * (saida esperada de k - saida obtida de k)**2)
        double erro_total = 0;
        for (int i = 0; i < saidas_da_rede.size(); i++) {
            erro_total += Math.pow(conjunto.get(i).retornaRotulo() - retornaRotulo(rede.saidas_da_rede.get(i)),2);
            System.out.println("Erro total na iteracao("+i+") = "+ erro_total);
        }
        System.out.println("\n"+(0.5) * conjunto.size() * (erro_total));
        return (0.5) * conjunto.size() * (erro_total);
    }

    public double calculaErroNeuronioOculto(Neuronio neuronio, double saida) {//Nao calcula
        //Somatoria dos erros de saida
        double somatoriaSaida = 0.0;
        for (int i = 0; i < camadaSaida.neuronios.length; i++) {
            somatoriaSaida += (camadaSaida.neuronios[i].ultimo_erro * camadaSaida.neuronios[i].pesos[neuronio.ID]);
        }
        neuronio.ultimo_erro = saida * (1 - saida) * somatoriaSaida;
        return neuronio.ultimo_erro;
    }
}
