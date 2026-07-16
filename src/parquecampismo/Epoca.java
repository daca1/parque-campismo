package parquecampismo;

import java.time.LocalDate;
import java.time.MonthDay;

/**
 * Determina se uma data cai na epoca alta (15/6 a 15/9) ou baixa
 * (restante periodo), conforme a Tabela 1 do enunciado.
 */
public final class Epoca {

    private static final MonthDay INICIO_EPOCA_ALTA = MonthDay.of(6, 15);
    private static final MonthDay FIM_EPOCA_ALTA = MonthDay.of(9, 15);

    private Epoca() {
        // classe utilitária - não deve ser instanciada
    }

    public static TipoEpoca determinarEpoca(LocalDate data) {
        MonthDay diaAtual = MonthDay.from(data);
        boolean dentroDaEpocaAlta = !diaAtual.isBefore(INICIO_EPOCA_ALTA) && !diaAtual.isAfter(FIM_EPOCA_ALTA);
        return dentroDaEpocaAlta ? TipoEpoca.ALTA : TipoEpoca.BAIXA;
    }

    public static boolean isEpocaAlta(LocalDate data) {
        return determinarEpoca(data) == TipoEpoca.ALTA;
    }
}
