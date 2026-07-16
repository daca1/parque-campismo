package parquecampismo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/** Area do parque identificada por uma letra e uma cor, com varios talhoes. */
public class Setor implements Serializable {

    private static final long serialVersionUID = 1L;

    private final char letra;
    private final String cor;
    private final List<Talhao> talhoes = new ArrayList<>();

    public Setor(char letra, String cor) {
        this.letra = letra;
        this.cor = cor;
    }

    public char getLetra() {
        return letra;
    }

    public String getCor() {
        return cor;
    }

    public void adicionarTalhao(Talhao talhao) {
        talhoes.add(talhao);
    }

    public List<Talhao> getTalhoes() {
        return Collections.unmodifiableList(talhoes);
    }

    public List<Talhao> getTalhoesDisponiveis() {
        return talhoes.stream()
                .filter(t -> t.getEntradaAtual() == null)
                .collect(Collectors.toList());
    }

    public Talhao getTalhaoPorCodigo(String codigo) {
        for (Talhao talhao : talhoes) {
            if (talhao.getCodigo().equalsIgnoreCase(codigo)) {
                return talhao;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Setor " + letra + " (" + cor + ") - " + talhoes.size() + " talhões";
    }
}
