package MLP;

import java.sql.SQLOutput;

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
        camadaSaida = new Camada(10, camadaOculta.tamanhoCamada);
    }

    public void forwardPropagation(double[] entrada, MLP rede) {
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

    public void ajustaPesosCamadaSaida(int esperado, int saida) {
        // Ajuste do peso entre i e j = taxa de aprendizado * o erro do neuronio j * a jesima entrada do neuronio i
        for (int i = 0; i < camadaSaida.tamanhoCamada; i++) {
            for (int j = 0; j < camadaOculta.tamanhoCamada; j++) {
                double delta = taxaDeAprendizado * camadaSaida.neuronios[i].ultimo_erro * camadaSaida.neuronios[i].soma_ponderada;

                //double buffer = camadaSaida.neuronios[i].pesos[j]; //APENAS PARA TESTES!!
                //System.out.println("Peso original do neuronio de saida "+ i +"("+ j +")"+" = "+ camadaSaida.neuronios[i].pesos[j]);

                camadaSaida.neuronios[i].pesos[j] = camadaSaida.neuronios[i].pesos[j] + delta;

                //System.out.println("Peso ajustado do neuronio de saida "+ i +"("+ j +")"+" = "+ (camadaSaida.neuronios[i].pesos[j]));
                //System.out.println("Ajuste do neuronio de saida "+ i +"("+ j +")"+" = "+ (buffer - camadaSaida.neuronios[i].pesos[j]));
            }
            //System.out.println();
        }
    }

    public void ajustaPesosCamadaOculta(double[] entrada){
        for (int i = 0; i < camadaOculta.tamanhoCamada; i++) {
            for (int j = 0; j < TAM_ENTRADA; j++) {
                double delta = taxaDeAprendizado * camadaOculta.neuronios[i].ultimo_erro * camadaOculta.neuronios[i].soma_ponderada;//Essa parte me causou sofrimento

                double buffer = camadaOculta.neuronios[i].pesos[j]; //APENAS PARA TESTES!!
                System.out.println("Peso original do neuronio oculto "+ i +"("+ j +")"+" = "+ camadaOculta.neuronios[i].pesos[j]);

                camadaOculta.neuronios[i].pesos[j] = camadaOculta.neuronios[i].pesos[j] + delta;

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

    public double calculaErroQuadratico() {
        //O que fazer com isso?
        return 0.0;
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
    //Para cada neuronio h, da camada oculta faça:
    // ErroNeuronioOculto = SaidaNeuronioOculto ( 1 - SaidaNeuronioOculto) + somatoria dos (PesoOcultaSaida * ErroSaida)

    //Como conectar os neuronios da camada de saida com os neuronios da camada oculta??
    //Nao existe conexao entre os neuronios, apenas o vetor de pesos, que nao liga de fato um neuronio ao outro

    //Tentei resolver isso usando um valor ID no neuronio, correspondente a sua posicao no vetor de pesos dos neuronios que recebem sua saida.
    //Colocar em um loop pra testar
}
