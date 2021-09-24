package MLP;

public class MLP {
    final int MIN_PESO = -1;
    final int MAX_PESO = 1;
    final int TAM_ENTRADA = 64;

    public Camada camadaOculta;
    public Camada camadaSaida;
    public double taxaDeAprendizado;

    public MLP(int neuroniosCamadaOculta, double taxaDeAprendizado){
        this.taxaDeAprendizado = taxaDeAprendizado;
        camadaOculta = new Camada(neuroniosCamadaOculta,TAM_ENTRADA);
        camadaSaida = new Camada(10, camadaOculta.tamanho);
    }



}
