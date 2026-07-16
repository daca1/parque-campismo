package parquecampismo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Unidade aglutinadora de utentes, unidades e talhoes para os quais sera
 * emitida uma unica fatura (ponto 24). Uma entrada com data de entrada
 * futura representa uma reserva (Cenario 4 do relatorio da Fase 1).
 */
public class Entrada implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int id;
    private Utente responsavel;
    private final List<Utente> utentes = new ArrayList<>();
    private final List<Unidade> unidades = new ArrayList<>();
    private final List<Talhao> talhoes = new ArrayList<>();
    private final LocalDate dataEntrada;
    private LocalDate dataSaida;
    private EstadoEntrada estado;

    public Entrada(int id, Utente responsavel, LocalDate dataEntrada) {
        if (responsavel == null) {
            throw new IllegalArgumentException("Uma entrada tem sempre de ter um responsável.");
        }
        if (dataEntrada == null) {
            throw new IllegalArgumentException("A data de entrada é obrigatória.");
        }
        this.id = id;
        this.responsavel = responsavel;
        this.dataEntrada = dataEntrada;
        this.estado = dataEntrada.isAfter(LocalDate.now()) ? EstadoEntrada.RESERVA : EstadoEntrada.ABERTA;
        adicionarUtente(responsavel);
    }

    public int getId() {
        return id;
    }

    public Utente getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(Utente responsavel) {
        this.responsavel = responsavel;
    }

    public List<Utente> getUtentes() {
        return Collections.unmodifiableList(utentes);
    }

    public void adicionarUtente(Utente utente) {
        if (!utentes.contains(utente)) {
            utentes.add(utente);
        }
    }

    /**
     * Remove um utente da entrada. O responsável só pode ser removido
     * quando for o último utente a sair (o que esvazia a entrada e a
     * torna elegível para eliminação, ponto 26); enquanto existirem
     * outros utentes associados, o responsável tem de ser mantido ou
     * substituído primeiro através de {@link #setResponsavel(Utente)}.
     */
    public boolean removerUtente(Utente utente) {
        if (utente == responsavel && utentes.size() > 1) {
            throw new IllegalStateException("Não é possível remover o responsável enquanto existirem outros utentes associados a esta entrada.");
        }
        return utentes.remove(utente);
    }

    public List<Unidade> getUnidades() {
        return Collections.unmodifiableList(unidades);
    }

    public List<Talhao> getTalhoes() {
        return Collections.unmodifiableList(talhoes);
    }

    /** Regista uma unidade (e o respetivo talhão) nesta entrada. */
    public void adicionarUnidade(Unidade unidade, Talhao talhao) {
        talhao.instalarUnidade(unidade, this);
        unidades.add(unidade);
        if (!talhoes.contains(talhao)) {
            talhoes.add(talhao);
        }
    }

    public boolean removerUnidade(Unidade unidade) {
        Talhao talhao = unidade.getTalhao();
        boolean removida = unidades.remove(unidade);
        if (removida && talhao != null) {
            talhao.removerUnidade(unidade);
            if (talhao.getUnidades().isEmpty()) {
                talhoes.remove(talhao);
            }
        }
        return removida;
    }

    public LocalDate getDataEntrada() {
        return dataEntrada;
    }

    public LocalDate getDataSaida() {
        return dataSaida;
    }

    public EstadoEntrada getEstado() {
        return estado;
    }

    public boolean isReserva() {
        return estado == EstadoEntrada.RESERVA;
    }

    /** Fecha a entrada (chamado ao emitir a fatura correspondente). */
    public void fechar(LocalDate dataSaida) {
        this.dataSaida = dataSaida;
        this.estado = EstadoEntrada.FECHADA;
        for (Talhao talhao : talhoes) {
            talhao.libertar();
        }
    }

    public long getNumeroDeDias() {
        LocalDate fim = dataSaida != null ? dataSaida : LocalDate.now();
        long dias = ChronoUnit.DAYS.between(dataEntrada, fim);
        return Math.max(dias, 1);
    }

    /** Só pode ser eliminada se estiver fechada e sem utentes/unidades associadas (ponto 26). */
    public boolean podeSerEliminada() {
        return estado == EstadoEntrada.FECHADA && utentes.isEmpty() && unidades.isEmpty();
    }

    @Override
    public String toString() {
        String periodo = dataSaida != null ? dataEntrada + " a " + dataSaida : dataEntrada + " (em curso)";
        return "Entrada #" + id + " [" + estado + "] " + periodo + " - responsável: " + responsavel.getIdentificacao().getNome();
    }
}
