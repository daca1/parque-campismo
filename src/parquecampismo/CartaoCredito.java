package parquecampismo;

import java.io.Serializable;

/** Dados de pagamento do utente responsavel por uma unidade/entrada. */
public class CartaoCredito implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String empresa;
    private final String numero;

    public CartaoCredito(String empresa, String numero) {
        if (empresa == null || empresa.isBlank() || numero == null || numero.isBlank()) {
            throw new IllegalArgumentException("Empresa e número do cartão de crédito são obrigatórios.");
        }
        this.empresa = empresa;
        this.numero = numero;
    }

    public String getEmpresa() {
        return empresa;
    }

    public String getNumero() {
        return numero;
    }

    @Override
    public String toString() {
        return empresa + " •••• " + numero.substring(Math.max(0, numero.length() - 4));
    }
}
