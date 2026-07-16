package parquecampismo;

import java.time.LocalDate;

/** Passaporte de qualquer nacionalidade, com pais emissor. */
public class Passaporte extends Identificacao {

    private static final long serialVersionUID = 1L;

    private final String paisEmissor;

    public Passaporte(String identificador, String nome, LocalDate dataDeNascimento, String nacionalidade, String paisEmissor) {
        super(identificador, nome, dataDeNascimento, nacionalidade);
        this.paisEmissor = paisEmissor;
    }

    public String getPaisEmissor() {
        return paisEmissor;
    }

    @Override
    public String getTipoDocumento() {
        return "Passaporte";
    }
}
