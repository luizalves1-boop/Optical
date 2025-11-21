import java.io.*;
import java.util.*;

class SubstituicaoOtima {

    static boolean buscar(int pagina, int[] quadros) {
        for (int i = 0; i < quadros.length; i++)
            if (quadros[i] == pagina)
                return true;
        return false;
    }

    static int prever(int paginas[], int[] quadros, int indiceAtual) {
        int indiceEscolhido = -1;
        int maisDistante = indiceAtual;

        for (int i = 0; i < quadros.length; i++) {
            int j;
            for (j = indiceAtual; j < paginas.length; j++) {
                if (quadros[i] == paginas[j]) {
                    if (j > maisDistante) {
                        maisDistante = j;
                        indiceEscolhido = i;
                    }
                    break;
                }
            }

            // Se a página em quadros[i] não será usada novamente
            if (j == paginas.length)
                return i;
        }

        return (indiceEscolhido == -1) ? 0 : indiceEscolhido;
    }

    // Executa o algoritmo de substituição ótima
    static void substituirOtima(int paginas[], int totalQuadros) {
        int[] quadros = new int[totalQuadros];
        for (int i = 0; i < quadros.length; i++) {
            quadros[i] = -1;
        }
        int acertos = 0;
        int indiceQuadro = 0;
        for (int i = 0; i < paginas.length; i++) {
            if (buscar(paginas[i], quadros)) {
                acertos++;
                continue;
            }

            if (indiceQuadro < totalQuadros)
                quadros[indiceQuadro++] = paginas[i];
            else {
                int posicao = prever(paginas, quadros, i + 1);
                quadros[posicao] = paginas[i];
            }
        }

        System.out.println("Nº de acertos = " + acertos);
        System.out.println("Nº de faltas = " + (paginas.length - acertos));
    }

    public static void main(String[] args) {
        String arquivo = "6.txt";
        int totalQuadros = 0;
        int[] paginas = null;
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
}
