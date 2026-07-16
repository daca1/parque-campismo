package parquecampismo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/** Funções auxiliares de leitura e validação de input na consola. */
public final class ConsoleUtils {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private ConsoleUtils() {
        // classe utilitária - não deve ser instanciada
    }

    public static String lerTexto(String mensagem) {
        System.out.print(mensagem);
        return SCANNER.nextLine().trim();
    }

    public static String lerTextoObrigatorio(String mensagem) {
        String texto;
        do {
            texto = lerTexto(mensagem);
            if (texto.isEmpty()) {
                System.out.println("  >> Este campo é obrigatório.");
            }
        } while (texto.isEmpty());
        return texto;
    }

    public static int lerInteiro(String mensagem) {
        while (true) {
            try {
                return Integer.parseInt(lerTexto(mensagem));
            } catch (NumberFormatException e) {
                System.out.println("  >> Introduza um número inteiro válido.");
            }
        }
    }

    public static int lerInteiroEntre(String mensagem, int min, int max) {
        int valor;
        do {
            valor = lerInteiro(mensagem);
            if (valor < min || valor > max) {
                System.out.println("  >> Introduza um valor entre " + min + " e " + max + ".");
            }
        } while (valor < min || valor > max);
        return valor;
    }

    public static double lerDouble(String mensagem) {
        while (true) {
            try {
                return Double.parseDouble(lerTexto(mensagem).replace(',', '.'));
            } catch (NumberFormatException e) {
                System.out.println("  >> Introduza um número válido.");
            }
        }
    }

    public static LocalDate lerData(String mensagem) {
        while (true) {
            String texto = lerTexto(mensagem + " (dd/MM/yyyy): ");
            try {
                return LocalDate.parse(texto, FORMATO_DATA);
            } catch (DateTimeParseException e) {
                System.out.println("  >> Data inválida. Use o formato dd/MM/yyyy.");
            }
        }
    }

    public static boolean lerConfirmacao(String mensagem) {
        String resposta = lerTexto(mensagem + " (S/N): ");
        return resposta.equalsIgnoreCase("S");
    }

    public static void pausa() {
        System.out.print("\n>>> Prima ENTER para continuar...");
        SCANNER.nextLine();
    }
}
