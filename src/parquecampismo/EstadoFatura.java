package parquecampismo;

/** Uma fatura fechada nunca pode voltar a ser aberta; so pode ser anulada. */
public enum EstadoFatura {
    FECHADA,
    ANULADA
}
