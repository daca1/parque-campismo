package parquecampismo;

import java.time.LocalDate;

/**
 * Documento generico atribuido automaticamente a criancas com menos de
 * 5 anos que nao possuam identificacao propria (ponto 10 do enunciado).
 */
public class DocumentoGenerico extends Identificacao {

    private static final long serialVersionUID = 1L;

    private final String tipoDocumento;

    public DocumentoGenerico(String identificador, String nome, LocalDate dataDeNascimento, String nacionalidade, String tipoDocumento) {
        super(identificador, nome, dataDeNascimento, nacionalidade);
        this.tipoDocumento = tipoDocumento;
    }

    @Override
    public String getTipoDocumento() {
        return tipoDocumento;
    }
}
