package MLP;

public class Backpropagation {

    static void atualizaPesosCamadaNormal(){

    }

    static void atualizaPesosCamadaOculta(){

    }

    static int derivadaSigmoide(int entrada){
        return (1-Camada.sigmoide(entrada))*Camada.sigmoide(entrada);
    }

    static double calculaErroCamadaNormal(int obtido, int esperado){
        return obtido*(1-obtido)*(esperado-obtido);
    }

    static double calculaErroCamadaOculta(int obtido, int esperado){

        return 0;
    }

}
