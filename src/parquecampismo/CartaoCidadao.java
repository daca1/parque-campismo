package parquecampismo;

import java.time.LocalDate;

/** Cartao do Cidadao portugues - exclusivo de utentes nacionais. */
public class CartaoCidadao extends Identificacao {

    private static final long serialVersionUID = 1L;

    public CartaoCidadao(String numero, String nome, LocalDate dataDeNascimento) {
        super(numero, nome, dataDeNascimento, "PT");
    }

    @Override
    public String getTipoDocumento() {
        return "Cartão de Cidadão";
    }
}
