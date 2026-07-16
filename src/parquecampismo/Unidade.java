package parquecampismo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Superclasse abstrata de Autocaravana, Caravana e Tenda. Toda a unidade
 * tem um identificador unico, um utente responsavel e esta associada a
 * um talhao (ponto 18 do enunciado).
 */
public abstract class Unidade implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String identificador;
    private Utente responsavel;
    private final List<Utente> utentes = new ArrayList<>();
    private Talhao talhao;
    private final List<Servico> servicos = new ArrayList<>();

    protected Unidade(String identificador, Utente responsavel) {
        if (identificador == null || identificador.isBlank()) {
            throw new IllegalArgumentException("A unidade tem de ter um identificador único.");
        }
        if (responsavel == null) {
            throw new IllegalArgumentException("Toda a unidade tem de ter um utente responsável.");
        }
        this.identificador = identificador;
        this.responsavel = responsavel;
    }

    public String getIdentificador() {
        return identificador;
    }

    public Utente getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(Utente responsavel) {
        if (responsavel == null) {
            throw new IllegalArgumentException("Uma unidade tem sempre de ter um responsável.");
        }
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

    /** Remove o utente da unidade; a unidade só pode ser eliminada se ficar sem utentes. */
    public boolean removerUtente(Utente utente) {
        return utentes.remove(utente);
    }

    public boolean temUtentes() {
        return !utentes.isEmpty();
    }

    public Talhao getTalhao() {
        return talhao;
    }

    public void setTalhao(Talhao talhao) {
        this.talhao = talhao;
    }

    public List<Servico> getServicos() {
        return Collections.unmodifiableList(servicos);
    }

    /**
     * Adiciona um serviço adicional. Eletricidade e Wifi já estão
     * incluídos no preço de Autocaravanas e Caravanas (nota (1) da
     * Tabela 1), pelo que não podem voltar a ser cobrados a essas
     * unidades.
     */
    public void adicionarServico(Servico servico) {
        boolean incluiServicosDeSerie = this instanceof Autocaravana || this instanceof Caravana;
        boolean servicoJaIncluido = servico.getTipo() == TipoServico.ELETRICIDADE || servico.getTipo() == TipoServico.WIFI;
        if (incluiServicosDeSerie && servicoJaIncluido) {
            throw new IllegalStateException(getTipo() + " já inclui " + servico.getTipo() + " no preço da diária.");
        }
        servicos.add(servico);
    }

    public void removerServico(Servico servico) {
        servicos.remove(servico);
    }

    public abstract TipoUnidade getTipo();

    @Override
    public String toString() {
        return getTipo() + " " + identificador + " (responsável: " + responsavel.getIdentificacao().getNome() + ")";
    }
}
