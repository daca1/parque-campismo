package parquecampismo;

/**
 * Taxa turistica de 1 euro por dia, aplicavel apenas a utentes adultos
 * estrangeiros e isenta de IVA (nota (2) da Tabela 1 do enunciado).
 *
 * Implementada como classe utilitária sem estado próprio, uma vez que o
 * valor e a regra de aplicabilidade são fixos para todo o sistema.
 */
public final class TaxaTuristica {

    private static final double VALOR_DIARIO = 1.0;

    private TaxaTuristica() {
        // classe utilitária - não deve ser instanciada
    }

    public static boolean aplicavelA(Utente utente) {
        return utente.getTipo() == TipoUtente.ADULTO && utente.isEstrangeiro();
    }

    public static double calcular(Utente utente, long numeroDeDias) {
        return aplicavelA(utente) ? VALOR_DIARIO * numeroDeDias : 0.0;
    }
}
