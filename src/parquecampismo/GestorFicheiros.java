package parquecampismo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Responsavel por gravar e carregar o estado completo do parque num
 * ficheiro (pontos 61-63 do enunciado), usando serializacao Java.
 */
public final class GestorFicheiros {

    private GestorFicheiros() {
        // classe utilitária - não deve ser instanciada
    }

    public static void gravar(ParqueCampismo parque, String nomeFicheiro) throws IOException {
        try (ObjectOutputStream saida = new ObjectOutputStream(new FileOutputStream(nomeFicheiro))) {
            saida.writeObject(parque);
        }
    }

    public static ParqueCampismo carregar(String nomeFicheiro) throws IOException, ClassNotFoundException {
        try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(nomeFicheiro))) {
            return (ParqueCampismo) entrada.readObject();
        }
    }

    public static boolean existeFicheiro(String nomeFicheiro) {
        return new File(nomeFicheiro).isFile();
    }
}
