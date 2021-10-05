package MLP;

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

    static void atualizaPesosCamadaNormal() {

    }

    static void atualizaPesosCamadaOculta() {

    }

    static double derivadaSigmoide(int entrada) {
        return (double) ((1 - Neuronio.sigmoide(entrada)) * Neuronio.sigmoide(entrada));
    }

    static double calculaErroNeuronioSaida(int saida, int esperado) {
        return saida * (1 - saida) * (esperado - saida);
    }

    static double calculaTreshold(){
        return 0.0;
    }

    //Para cada neuronio h, da camada oculta faça:
    // ErroNeuronioOculto = SaidaNeuronioOculto ( 1 - SaidaNeuronioOculto) + somatoria dos (PesoOcultaSaida * ErroSaida)

    //Como conectar os neuronios da camada de saida com os neuronios da camada oculta??
    //Nao existe conexao entre os neuronios, apenas o vetor de pesos, que nao liga de fato um neuronio ao outro

    //Tentei resolver isso usando um valor ID no neuronio, correspondente a sua posicao no vetor de pesos dos neuronios que recebem sua saida.
    //Colocar em um loop pra testar
    static double calculaErroNeuronioOculto(int idNeuronio, Camada camadaSaida, int saida) {
        //Somatoria dos erros de saida
        double somatoriaSaida = 0.0;
        for (int i = 0; i < camadaSaida.neuronios.length; i++) {
            somatoriaSaida += (camadaSaida.neuronios[i].saida * camadaSaida.neuronios[i].pesos[idNeuronio]);
        }
        return saida * (1 - saida) + somatoriaSaida;
    }
}
