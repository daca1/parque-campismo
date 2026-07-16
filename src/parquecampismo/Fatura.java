package parquecampismo;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * Documento financeiro com identificador unico (ponto 30). Uma fatura
 * fechada nunca pode ser editada ou eliminada, apenas anulada (pontos
 * 58-59).
 */
public class Fatura implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String id;
    private final Entrada entrada;
    private double valorSemIva;
    private double valorIva;
    private double valorComIva;
    private EstadoFatura estado;
    private final LocalDate dataEmissao;

    private static final double TAXA_IVA = 0.25;

    private Fatura(Entrada entrada) {
        this.id = UUID.randomUUID().toString();
        this.entrada = entrada;
        this.dataEmissao = LocalDate.now();
        this.estado = EstadoFatura.FECHADA;
    }

    /**
     * Emite a fatura de uma entrada: calcula os valores, fecha a entrada
     * (ponto 57) e liberta os respetivos talhões.
     */
    public static Fatura emitir(Entrada entrada, Precario precario, LocalDate dataSaida) {
        if (entrada.getEstado() == EstadoEntrada.FECHADA) {
            throw new IllegalStateException("A entrada #" + entrada.getId() + " já está fechada.");
        }
        Fatura fatura = new Fatura(entrada);
        fatura.calcular(precario, dataSaida);
        entrada.fechar(dataSaida);
        return fatura;
    }

    /** Calcula valor sem IVA, IVA (25%) e valor com IVA para a entrada associada. */
    private void calcular(Precario precario, LocalDate dataSaida) {
        TipoEpoca epoca = Epoca.determinarEpoca(entrada.getDataEntrada());
        long dias = java.time.temporal.ChronoUnit.DAYS.between(entrada.getDataEntrada(), dataSaida);
        dias = Math.max(dias, 1);

        double totalUnidades = 0;
        double totalServicos = 0;
        for (Unidade unidade : entrada.getUnidades()) {
            totalUnidades += precario.getPrecoDiarioUnidade(unidade.getTipo(), epoca) * dias;
            for (Servico servico : unidade.getServicos()) {
                totalServicos += servico.getPreco(epoca) * dias;
            }
        }

        double totalUtentes = 0;
        double totalTaxaTuristica = 0;
        for (Utente utente : entrada.getUtentes()) {
            totalUtentes += precario.getPrecoDiarioUtente(utente.getTipo(), epoca) * dias;
            totalTaxaTuristica += TaxaTuristica.calcular(utente, dias);
        }

        // A taxa turística está isenta de IVA (nota (2) da Tabela 1).
        double baseTributavel = totalUnidades + totalServicos + totalUtentes;
        this.valorIva = baseTributavel * TAXA_IVA;
        this.valorSemIva = baseTributavel + totalTaxaTuristica;
        this.valorComIva = baseTributavel + valorIva + totalTaxaTuristica;
    }

    public String getId() {
        return id;
    }

    public Entrada getEntrada() {
        return entrada;
    }

    public double getValorSemIva() {
        return valorSemIva;
    }

    public double getValorIva() {
        return valorIva;
    }

    public double getValorComIva() {
        return valorComIva;
    }

    public EstadoFatura getEstado() {
        return estado;
    }

    public LocalDate getDataEmissao() {
        return dataEmissao;
    }

    /** Anula a fatura. Não pode ser eliminada nem alterada (ponto 58). */
    public void anular() {
        if (estado == EstadoFatura.ANULADA) {
            throw new IllegalStateException("A fatura " + id + " já está anulada.");
        }
        this.estado = EstadoFatura.ANULADA;
    }

    public String gerarTextoFatura() {
        StringBuilder texto = new StringBuilder();
        texto.append("========================================\n");
        texto.append("            FATURA - PARQUE DE CAMPISMO\n");
        texto.append("========================================\n");
        texto.append("Fatura Nº: ").append(id).append('\n');
        texto.append("Estado: ").append(estado).append('\n');
        texto.append("Data de emissão: ").append(dataEmissao).append('\n');
        texto.append("----------------------------------------\n");
        texto.append("Entrada #").append(entrada.getId()).append('\n');
        texto.append("Responsável: ").append(entrada.getResponsavel().getIdentificacao().getNome()).append('\n');
        texto.append("Período: ").append(entrada.getDataEntrada()).append(" a ").append(entrada.getDataSaida()).append('\n');
        texto.append("Nº de dias: ").append(entrada.getNumeroDeDias()).append('\n');
        texto.append("----------------------------------------\n");
        texto.append("Utentes:\n");
        for (Utente utente : entrada.getUtentes()) {
            texto.append("  - ").append(utente).append('\n');
        }
        texto.append("Unidades:\n");
        List<Unidade> unidades = entrada.getUnidades();
        for (Unidade unidade : unidades) {
            texto.append("  - ").append(unidade).append('\n');
        }
        texto.append("----------------------------------------\n");
        texto.append(String.format(Locale.US, "Valor sem IVA: %.2f €%n", valorSemIva));
        texto.append(String.format(Locale.US, "IVA (25%%): %.2f €%n", valorIva));
        texto.append(String.format(Locale.US, "Valor com IVA: %.2f €%n", valorComIva));
        texto.append("========================================\n");
        return texto.toString();
    }

    /** Grava a fatura num ficheiro de texto (ponto 31: "impressão" = gravação de ficheiro). */
    public void imprimirParaFicheiro(String nomeFicheiro) throws IOException {
        try (Writer writer = new FileWriter(nomeFicheiro)) {
            writer.write(gerarTextoFatura());
        }
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "Fatura %s [%s] - %.2f € (entrada #%d)", id, estado, valorComIva, entrada.getId());
    }
}
