package parquecampismo;

/** Unidade rebocavel. Inclui eletricidade e WiFi no preco da diaria. */
public class Caravana extends Unidade {

    private static final long serialVersionUID = 1L;

    private final double dimensao;
    private final String marca;
    private final String matricula;

    public Caravana(String identificador, Utente responsavel, double dimensao, String marca, String matricula) {
        super(identificador, responsavel);
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

    @Override
    public TipoUnidade getTipo() {
        return dimensao > 9 ? TipoUnidade.CARAVANA_GRANDE : TipoUnidade.CARAVANA_PEQUENA;
    }
}
