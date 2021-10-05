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

    static double calculaErroCamadaSaida(int obtido, int esperado) {
        return obtido * (1 - obtido) * (esperado - obtido);
    }

    //Para cada neuronio h, da camada oculta faça:
    // ErroNeuronioOculto = SaidaNeuronioOculto ( 1 - SaidaNeuronioOculto) + somatoria dos (PesoOcultaSaida * ErroSaida)
    static double calculaErroNeuronioOculto(int obtido, int esperado) {

        double ErroNeuronioOculto =  obtido * (1 - obtido);
        return 0;
    }
}
