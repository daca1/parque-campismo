package parquecampismo;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Representa um cliente do parque. O tipo (Adulto/Crianca/CriancaGratis)
 * e determinado pela idade da identificacao associada.
 */
public class Utente implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Identificacao identificacao;
    private String telemovel;
    private String email;
    private CartaoCredito cartaoCredito;
    private Unidade unidadeAtual;
    private LocalDate dataEntradaAtual;

    public Utente(Identificacao identificacao, String telemovel, String email) {
        if (identificacao == null) {
            throw new IllegalArgumentException("Um utente tem de ter uma identificação.");
        }
        this.identificacao = identificacao;
        this.telemovel = telemovel;
        this.email = email;
    }

    public Identificacao getIdentificacao() {
        return identificacao;
    }

    public String getTelemovel() {
        return telemovel;
    }

    public void setTelemovel(String telemovel) {
        this.telemovel = telemovel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public CartaoCredito getCartaoCredito() {
        return cartaoCredito;
    }

    /** Só é obrigatório para o utente responsável por uma entrada/unidade (ponto 7). */
    public void setCartaoCredito(CartaoCredito cartaoCredito) {
        this.cartaoCredito = cartaoCredito;
    }

    public Unidade getUnidadeAtual() {
        return unidadeAtual;
    }

    public boolean estaNoParque() {
        return unidadeAtual != null;
    }

    public boolean isEstrangeiro() {
        return identificacao.getNacionalidade() != null && !"PT".equalsIgnoreCase(identificacao.getNacionalidade());
    }

    public int getIdade() {
        return identificacao.getIdade();
    }

    public TipoUtente getTipo() {
        int idade = getIdade();
        if (idade < 5) {
            return TipoUtente.CRIANCA_GRATIS;
        }
        if (idade < 15) {
            return TipoUtente.CRIANCA;
        }
        return TipoUtente.ADULTO;
    }

    /** Associa o utente a uma unidade e regista a data de entrada (ponto 41). */
    public void darEntrada(Unidade unidade) {
        this.unidadeAtual = unidade;
        this.dataEntradaAtual = LocalDate.now();
    }

    /** Remove a associação a uma unidade quando o utente sai do parque. */
    public void sair() {
        this.unidadeAtual = null;
        this.dataEntradaAtual = null;
    }

    public LocalDate getDataEntradaAtual() {
        return dataEntradaAtual;
    }

    @Override
    public String toString() {
        return identificacao.getNome() + " (" + identificacao.getTipoDocumento() + " " + identificacao.getIdentificador() + ", " + getTipo() + ")";
    }
}
