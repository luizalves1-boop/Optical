import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SubstituicaoOtima {

    public static void main(String[] args) {
        String arquivo = "entrada.txt";
        int totalQuadros = 0;
        int[] paginas = null;
        //Leitura do arquivo para o recebimento dos dados
        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.startsWith("Quadros:")) {
                    totalQuadros = Integer.parseInt(linha.substring(9));
                } else if (linha.startsWith("Páginas:")) {
                    String p = linha.substring(9).trim();
                    String[] pSplit = p.split(",");
                    paginas = new int[pSplit.length];
                    for (int i = 0; i < paginas.length; i++) {
                        paginas[i] = Integer.parseInt(pSplit[i]);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        substituirOtima(paginas, totalQuadros);
    }

    static void substituirOtima(int[] paginas, int totalQuadros) {
        int[] quadros = new int[totalQuadros];
        for (int i = 0; i < quadros.length; i++) {
            // Como um vetor inicia todos os valores com 0, precisamos mudar esses valores para −1
            // Modificar todos os valores para −1 representaria o “quadro vazio”
            //Se ele iniciasse com todos os valores em 0 poderia causar algum erro com as páginas
            quadros[i] = -1;
        }
        int acertos = 0;
        int indiceQuadro = 0;
        for (int i = 0; i < paginas.length; i++) {
            if (buscar(paginas[i], quadros)) {
                acertos++;
                continue; //Ignora o restante do código do loop e prossegue para o próximo índice
            }
            if (indiceQuadro < totalQuadros)
                quadros[indiceQuadro++] = paginas[i];//Se o quadro ainda não estiver completo a página é adicionada ao quadro
            else {//Senão, ele envia a página para o algoritmo de previsão
                int posicao = prever(paginas, quadros, i + 1);
                quadros[posicao] = paginas[i];
            }
        }
        System.out.println("Nº de acertos = " + acertos);
        System.out.println("Nº de faltas = " + (paginas.length - acertos));
    }

    static boolean buscar(int pagina, int[] quadros) {
        for (int i = 0; i < quadros.length; i++) {
            if (quadros[i] == pagina)
                return true;
        }
        return false;
    }

    static int prever(int[] paginas, int[] quadros, int proximoIndice) {
        int indiceEscolhido = -1;
        int maisDistante = proximoIndice;
        for (int i = 0; i < quadros.length; i++) {
            int j;
            for (j = proximoIndice; j < paginas.length; j++) {//j começa a verificar a partir do próximo índice
                if (paginas[j] == quadros[i]) {//Se ele achar alguma página próxima que seja igual à página que está sendo verificada no momento ele entra no if
                    if (j > maisDistante) {//Se o índice que ele achou for maior do que o índice mais distante até o momento ele entra nesse if
                        maisDistante = j; //Aqui ele vai substituir o mais distante pelo que ele encontrou agora
                        indiceEscolhido = i; //E armazenar qual é o índice do quadro em que a repetição mais distante foi encontrada
                    }
                    //Aqui ele quebra, pois o próximo encontro daquela página já foi encontrado
                    break;
                }
            }
            // Se a página em quadros[i] não será usada novamente
            if (j == paginas.length)
                return i;
        }
        //Se nenhuma página futura foi encontrada, o código retorna 0
        //Se todos os números que estão no quadro atual forem ser utilizados depois ele chega aqui
        //Então, ele vai enviar qual foi o indice escolhido, levando em conta qual a utilização mais distante
        return (indiceEscolhido == -1) ? 0 : indiceEscolhido;
    }


}