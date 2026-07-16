package parquecampismo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Espaco fisico do parque, identificado univocamente por letra+numero
 * (ponto 21). Um talhao so pode estar ocupado por unidades de uma unica
 * Entrada de cada vez (pontos 27-28).
 *
 * Nota de implementacao: a capacidade maxima por tipo de talhao (Tabela 2
 * do enunciado) foi simplificada para um numero de "lugares" por tipo de
 * unidade, conforme permitido pelo enunciado ("...deixado ao criterio do
 * programador qualquer pormenor de implementacao que nao seja referido no
 * mesmo e que deve ser devidamente documentado").
 */
public class Talhao implements Serializable {

    private static final long serialVersionUID = 1L;

    private final char letra;
    private final int numero;
    private final TipoTalhao tipo;
    private final boolean temWifi;
    private final boolean temEletricidade;
    private final List<Unidade> unidades = new ArrayList<>();
    private Entrada entradaAtual;

    public Talhao(char letra, int numero, TipoTalhao tipo, boolean temWifi, boolean temEletricidade) {
        this.letra = letra;
        this.numero = numero;
        this.tipo = tipo;
        this.temWifi = temWifi;
        this.temEletricidade = temEletricidade;
    }

    public char getLetra() {
        return letra;
    }

    public int getNumero() {
        return numero;
    }

    public String getCodigo() {
        return letra + String.valueOf(numero);
    }

    public TipoTalhao getTipo() {
        return tipo;
    }

    public boolean temWifi() {
        return temWifi;
    }

    public boolean temEletricidade() {
        return temEletricidade;
    }

    public List<Unidade> getUnidades() {
        return Collections.unmodifiableList(unidades);
    }

    public Entrada getEntradaAtual() {
        return entradaAtual;
    }

    /** Numero maximo de unidades que o talhao comporta, de acordo com o seu tipo. */
    public int getCapacidade() {
        switch (tipo) {
            case GRANDE:
                return 4;
            case MEDIO:
                return 2;
            case PEQUENO:
            default:
                return 1;
        }
    }

    /** Um talhao esta disponivel se estiver livre, ou ja ocupado pela mesma entrada. */
    public boolean isDisponivelPara(Entrada entrada) {
        return entradaAtual == null || entradaAtual == entrada;
    }

    public boolean temEspacoLivre() {
        return unidades.size() < getCapacidade();
    }

    /** Instala uma unidade no talhao, associando-o a entrada indicada. */
    public void instalarUnidade(Unidade unidade, Entrada entrada) {
        if (!isDisponivelPara(entrada)) {
            throw new IllegalStateException("O talhão " + getCodigo() + " já está ocupado por outra entrada.");
        }
        if (!temEspacoLivre()) {
            throw new IllegalStateException("O talhão " + getCodigo() + " atingiu a sua capacidade máxima.");
        }
        unidades.add(unidade);
        unidade.setTalhao(this);
        entradaAtual = entrada;
    }

    public boolean removerUnidade(Unidade unidade) {
        boolean removida = unidades.remove(unidade);
        if (removida) {
            unidade.setTalhao(null);
        }
        if (unidades.isEmpty()) {
            entradaAtual = null;
        }
        return removida;
    }

    /** Liberta completamente o talhao (chamado quando a entrada e fechada). */
    public void libertar() {
        for (Unidade unidade : unidades) {
            unidade.setTalhao(null);
        }
        unidades.clear();
        entradaAtual = null;
    }

    @Override
    public String toString() {
        String estado = entradaAtual == null ? "livre" : "ocupado";
        return "Talhão " + getCodigo() + " [" + tipo + ", " + estado
                + (temWifi ? ", WiFi" : "") + (temEletricidade ? ", Eletricidade" : "") + "]";
    }
}
