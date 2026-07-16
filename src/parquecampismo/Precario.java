package parquecampismo;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;

/**
 * Tabela de precos por epoca, indexada por TipoUnidade e TipoUtente
 * (Tabela 1 do enunciado).
 *
 * Nota sobre Caravanas/Autocaravanas: o enunciado rotula as linhas da
 * tabela como "Pequena (>9m2)" / "Grande (<=9m2)", o que e contraditorio
 * com o significado usual de pequeno/grande. Conforme documentado na
 * Fase 1 (secao 2.6 do relatorio), optou-se por manter os precos tal
 * como impressos, associando-os à condição de dimensão (>9m2 / <=9m2)
 * e não ao rótulo, pelo que "Grande" corresponde sempre à maior dimensão.
 */
public class Precario implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Map<TipoUnidade, double[]> precosUnidades = new EnumMap<>(TipoUnidade.class);
    private final Map<TipoUtente, double[]> precosUtentes = new EnumMap<>(TipoUtente.class);

    private Precario() {
    }

    /** Cria o preçário oficial descrito na Tabela 1 do enunciado. */
    public static Precario criarPrecarioOficial() {
        Precario precario = new Precario();

        precario.definirPrecoUnidade(TipoUnidade.CARAVANA_GRANDE, 25.0, 20.0);
        precario.definirPrecoUnidade(TipoUnidade.CARAVANA_PEQUENA, 15.0, 10.0);
        precario.definirPrecoUnidade(TipoUnidade.AUTOCARAVANA_GRANDE, 30.0, 25.0);
        precario.definirPrecoUnidade(TipoUnidade.AUTOCARAVANA_PEQUENA, 20.0, 15.0);
        precario.definirPrecoUnidade(TipoUnidade.TENDA_INDIVIDUAL, 2.0, 1.0);
        precario.definirPrecoUnidade(TipoUnidade.TENDA_CASAL, 4.0, 2.0);
        precario.definirPrecoUnidade(TipoUnidade.TENDA_FAMILIAR, 9.0, 6.0);

        precario.definirPrecoUtente(TipoUtente.ADULTO, 6.0, 4.0);
        precario.definirPrecoUtente(TipoUtente.CRIANCA, 3.0, 1.0);
        precario.definirPrecoUtente(TipoUtente.CRIANCA_GRATIS, 0.0, 0.0);

        return precario;
    }

    public void definirPrecoUnidade(TipoUnidade tipo, double precoAlta, double precoBaixa) {
        precosUnidades.put(tipo, new double[]{precoAlta, precoBaixa});
    }

    public void definirPrecoUtente(TipoUtente tipo, double precoAlta, double precoBaixa) {
        precosUtentes.put(tipo, new double[]{precoAlta, precoBaixa});
    }

    public double getPrecoDiarioUnidade(TipoUnidade tipo, TipoEpoca epoca) {
        double[] precos = precosUnidades.get(tipo);
        if (precos == null) {
            throw new IllegalStateException("Não existe preço definido para " + tipo);
        }
        return epoca == TipoEpoca.ALTA ? precos[0] : precos[1];
    }

    public double getPrecoDiarioUtente(TipoUtente tipo, TipoEpoca epoca) {
        double[] precos = precosUtentes.get(tipo);
        if (precos == null) {
            throw new IllegalStateException("Não existe preço definido para " + tipo);
        }
        return epoca == TipoEpoca.ALTA ? precos[0] : precos[1];
    }

    /** Serviços disponíveis com os preços oficiais da Tabela 1 (Carro, Eletricidade, WiFi). */
    public static Servico criarServicoOficial(TipoServico tipo) {
        switch (tipo) {
            case CARRO:
                return new Servico(TipoServico.CARRO, 2.0, 1.0);
            case ELETRICIDADE:
                return new Servico(TipoServico.ELETRICIDADE, 1.0, 1.0);
            case WIFI:
                return new Servico(TipoServico.WIFI, 1.0, 1.0);
            default:
                throw new IllegalArgumentException("Tipo de serviço desconhecido: " + tipo);
        }
    }
}
