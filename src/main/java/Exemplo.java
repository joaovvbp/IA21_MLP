public class Exemplo {
    Double[] vetorEntradas = new Double[64]; //Representa as entradas do arquivo.
    int[] rotulo = new int[10]; //Representa o rótulo. O número do índice com valor 1 é o rótulo.

 Exemplo() { //Inicializa o vetor rótulo com 0.
     for (int i = 0; i < 10; i++) {
         rotulo[i] = 0;
     }
 }

 int retornaRotulo(){
     int index = -1;
     for (int i = 0; i < rotulo.length; i++)
         if (rotulo[i] == 1) {
             index = i;
             break;
         }
     return index;
 }

}