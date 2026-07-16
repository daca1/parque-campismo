package parquecampismo;

/**
 * Tenda. Tipo determinado pela dimensao: Individual (<2m2), Casal
 * (2 a 6m2) ou Familiar (>=6m2). Servicos adicionais sao pagos a parte.
 */
public class Tenda extends Unidade {

    private static final long serialVersionUID = 1L;

    private final double dimensao;

    public Tenda(String identificador, Utente responsavel, double dimensao) {
        super(identificador, responsavel);
        if (dimensao <= 0) {
            throw new IllegalArgumentException("A dimensão tem de ser positiva.");
        }
        this.dimensao = dimensao;
    }

    public double getDimensao() {
        return dimensao;
    }

    @Override
    public TipoUnidade getTipo() {
        if (dimensao < 2) {
            return TipoUnidade.TENDA_INDIVIDUAL;
        }
        if (dimensao < 6) {
            return TipoUnidade.TENDA_CASAL;
        }
        return TipoUnidade.TENDA_FAMILIAR;
    }
}
