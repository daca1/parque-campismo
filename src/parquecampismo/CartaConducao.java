package parquecampismo;

import java.time.LocalDate;

/** Carta de conducao usada como documento de identificacao. */
public class CartaConducao extends Identificacao {

    private static final long serialVersionUID = 1L;

    private final String tipoCartaConducao;

    public CartaConducao(String identificador, String nome, LocalDate dataDeNascimento, String nacionalidade, String tipoCartaConducao) {
        super(identificador, nome, dataDeNascimento, nacionalidade);
        this.tipoCartaConducao = tipoCartaConducao;
    }

    public String getTipoCartaConducao() {
        return tipoCartaConducao;
    }

    @Override
    public String getTipoDocumento() {
        return "Carta de Condução";
    }
}
