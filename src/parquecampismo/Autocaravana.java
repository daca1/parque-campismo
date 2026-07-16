package parquecampismo;

/**
 * Unidade motorizada. O condutor e simultaneamente o responsavel pela
 * unidade (ponto 15). Inclui eletricidade e WiFi no preco da diaria.
 */
public class Autocaravana extends Unidade {

    private static final long serialVersionUID = 1L;

    private final double dimensao;
    private final String marca;
    private final String matricula;

    public Autocaravana(String identificador, Utente condutor, double dimensao, String marca, String matricula) {
        super(identificador, condutor);
        if (dimensao <= 0) {
            throw new IllegalArgumentException("A dimensão tem de ser positiva.");
        }
        this.dimensao = dimensao;
        this.marca = marca;
        this.matricula = matricula;
    }

    public double getDimensao() {
        return dimensao;
    }

    public String getMarca() {
        return marca;
    }

    public String getMatricula() {
        return matricula;
    }

    public Utente getCondutor() {
        return getResponsavel();
    }

    @Override
    public TipoUnidade getTipo() {
        return dimensao > 9 ? TipoUnidade.AUTOCARAVANA_GRANDE : TipoUnidade.AUTOCARAVANA_PEQUENA;
    }
}
