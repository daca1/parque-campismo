package parquecampismo;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe principal da aplicacao. Centraliza toda a gestao do parque:
 * setores/talhoes, utentes, unidades, entradas e faturas.
 *
 * Nota sobre a Tabela 3 do enunciado (configuracao dos dois parques de
 * teste): a formatacao da tabela no PDF original tornou algumas colunas
 * ambiguas na extracao automatica de texto. Os valores usados em
 * {@link #criarParqueDeTeste1()} e {@link #criarParqueDeTeste2()} seguem
 * fielmente a estrutura pedida (3 setores / 4 setores, com talhoes
 * Grandes, Medios e Pequenos, alguns com eletricidade e/ou WiFi), mas as
 * quantidades exatas foram adaptadas para valores de teste razoaveis -
 * ajustavel facilmente caso os valores exatos da tabela sejam necessarios.
 */
public class ParqueCampismo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nome;
    private final List<Setor> setores = new ArrayList<>();
    private final List<Utente> utentes = new ArrayList<>();
    private final List<Unidade> unidades = new ArrayList<>();
    private final List<Entrada> entradas = new ArrayList<>();
    private final List<Fatura> faturas = new ArrayList<>();
    private Precario precario;
    private int proximoIdEntrada = 1;

    public ParqueCampismo(String nome) {
        this.nome = nome;
        this.precario = Precario.criarPrecarioOficial();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Precario getPrecario() {
        return precario;
    }

    // ---------- Setores / Talhões ----------

    public void adicionarSetor(Setor setor) {
        setores.add(setor);
    }

    public List<Setor> getSetores() {
        return Collections.unmodifiableList(setores);
    }

    public Talhao getTalhaoPorCodigo(String codigo) {
        for (Setor setor : setores) {
            Talhao talhao = setor.getTalhaoPorCodigo(codigo);
            if (talhao != null) {
                return talhao;
            }
        }
        return null;
    }

    public List<Talhao> getTodosOsTalhoes() {
        List<Talhao> todos = new ArrayList<>();
        for (Setor setor : setores) {
            todos.addAll(setor.getTalhoes());
        }
        return todos;
    }

    // ---------- Utentes ----------

    public List<Utente> listarUtentes() {
        return Collections.unmodifiableList(utentes);
    }

    public boolean adicionarUtente(Utente utente) {
        boolean existeIdentificacaoRepetida = utentes.stream()
                .anyMatch(u -> u.getIdentificacao().getIdentificador().equals(utente.getIdentificacao().getIdentificador()));
        if (existeIdentificacaoRepetida) {
            throw new IllegalArgumentException("Já existe um utente com a identificação "
                    + utente.getIdentificacao().getIdentificador() + ".");
        }
        return utentes.add(utente);
    }

    public Utente procurarUtentePorId(String identificador) {
        return utentes.stream()
                .filter(u -> u.getIdentificacao().getIdentificador().equalsIgnoreCase(identificador))
                .findFirst()
                .orElse(null);
    }

    public List<Utente> procurarUtentePorNome(String nomeParcial) {
        String alvo = nomeParcial.toLowerCase(java.util.Locale.ROOT);
        return utentes.stream()
                .filter(u -> u.getIdentificacao().getNome().toLowerCase(java.util.Locale.ROOT).contains(alvo))
                .collect(Collectors.toList());
    }

    /** Só é possível eliminar um utente que nunca tenha frequentado o parque (ponto 42). */
    public boolean eliminarUtente(Utente utente) {
        boolean frequentouOParque = entradas.stream()
                .anyMatch(e -> e.getUtentes().contains(utente) || e.getResponsavel() == utente);
        if (frequentouOParque || utente.estaNoParque()) {
            return false;
        }
        return utentes.remove(utente);
    }

    // ---------- Unidades ----------

    public List<Unidade> listarUnidades() {
        return Collections.unmodifiableList(unidades);
    }

    public boolean adicionarUnidade(Unidade unidade) {
        boolean existeIdentificadorRepetido = unidades.stream()
                .anyMatch(u -> u.getIdentificador().equals(unidade.getIdentificador()));
        if (existeIdentificadorRepetido) {
            throw new IllegalArgumentException("Já existe uma unidade com o identificador "
                    + unidade.getIdentificador() + ".");
        }
        return unidades.add(unidade);
    }

    public List<Unidade> procurarUnidadePorTalhao(String codigoTalhao) {
        return unidades.stream()
                .filter(u -> u.getTalhao() != null && u.getTalhao().getCodigo().equalsIgnoreCase(codigoTalhao))
                .collect(Collectors.toList());
    }

    /** Só é possível eliminar uma unidade sem utentes atualmente associados (ponto 49). */
    public boolean eliminarUnidade(Unidade unidade) {
        if (unidade.temUtentes()) {
            return false;
        }
        if (unidade.getTalhao() != null) {
            unidade.getTalhao().removerUnidade(unidade);
        }
        return unidades.remove(unidade);
    }

    // ---------- Entradas ----------

    public List<Entrada> listarEntradas() {
        return Collections.unmodifiableList(entradas);
    }

    public Entrada registarEntrada(Utente responsavel, LocalDate dataEntrada) {
        Entrada entrada = new Entrada(proximoIdEntrada++, responsavel, dataEntrada);
        entradas.add(entrada);
        return entrada;
    }

    public Entrada getEntradaPorId(int id) {
        return entradas.stream().filter(e -> e.getId() == id).findFirst().orElse(null);
    }

    public boolean eliminarEntrada(Entrada entrada) {
        if (!entrada.podeSerEliminada()) {
            return false;
        }
        return entradas.remove(entrada);
    }

    // ---------- Faturas ----------

    public List<Fatura> listarFaturas() {
        return Collections.unmodifiableList(faturas);
    }

    /** Emite a fatura de uma entrada e fecha-a (pontos 56-57). */
    public Fatura emitirFatura(Entrada entrada, LocalDate dataSaida) {
        Fatura fatura = Fatura.emitir(entrada, precario, dataSaida);
        faturas.add(fatura);
        for (Unidade unidade : new ArrayList<>(entrada.getUnidades())) {
            unidade.getUtentes().forEach(Utente::sair);
        }
        return fatura;
    }

    public void anularFatura(Fatura fatura) {
        fatura.anular();
    }

    // ---------- Persistência ----------

    public void gravar(String nomeFicheiro) throws IOException {
        GestorFicheiros.gravar(this, nomeFicheiro);
    }

    public static ParqueCampismo carregar(String nomeFicheiro) throws IOException, ClassNotFoundException {
        return GestorFicheiros.carregar(nomeFicheiro);
    }

    // ---------- Configuração dos parques de teste (Tabela 3 do enunciado) ----------

    public static ParqueCampismo criarParqueDeTeste1() {
        ParqueCampismo parque = new ParqueCampismo("Parque de Campismo 1");

        Setor setorA = new Setor('A', "Verde");
        adicionarTalhoes(setorA, 'A', 1, 4, TipoTalhao.PEQUENO, false, false);
        adicionarTalhoes(setorA, 'A', 5, 10, TipoTalhao.MEDIO, false, false);

        Setor setorB = new Setor('B', "Azul");
        adicionarTalhoes(setorB, 'B', 1, 12, TipoTalhao.GRANDE, true, true);
        adicionarTalhoes(setorB, 'B', 13, 20, TipoTalhao.MEDIO, true, true);

        Setor setorC = new Setor('C', "Amarelo");
        adicionarTalhoes(setorC, 'C', 1, 10, TipoTalhao.GRANDE, true, false);
        adicionarTalhoes(setorC, 'C', 11, 30, TipoTalhao.MEDIO, false, true);

        parque.adicionarSetor(setorA);
        parque.adicionarSetor(setorB);
        parque.adicionarSetor(setorC);
        return parque;
    }

    public static ParqueCampismo criarParqueDeTeste2() {
        ParqueCampismo parque = new ParqueCampismo("Parque de Campismo 2");

        Setor setorA = new Setor('A', "Verde");
        adicionarTalhoes(setorA, 'A', 1, 10, TipoTalhao.PEQUENO, false, false);

        Setor setorB = new Setor('B', "Azul");
        adicionarTalhoes(setorB, 'B', 1, 20, TipoTalhao.MEDIO, false, true);

        Setor setorC = new Setor('C', "Amarelo");
        adicionarTalhoes(setorC, 'C', 1, 40, TipoTalhao.GRANDE, true, true);

        Setor setorD = new Setor('D', "Vermelho");
        adicionarTalhoes(setorD, 'D', 1, 25, TipoTalhao.GRANDE, true, false);
        adicionarTalhoes(setorD, 'D', 26, 65, TipoTalhao.MEDIO, false, true);
        adicionarTalhoes(setorD, 'D', 66, 80, TipoTalhao.PEQUENO, false, false);

        parque.adicionarSetor(setorA);
        parque.adicionarSetor(setorB);
        parque.adicionarSetor(setorC);
        parque.adicionarSetor(setorD);
        return parque;
    }

    private static void adicionarTalhoes(Setor setor, char letra, int inicio, int fim, TipoTalhao tipo, boolean wifi, boolean eletricidade) {
        for (int numero = inicio; numero <= fim; numero++) {
            setor.adicionarTalhao(new Talhao(letra, numero, tipo, wifi, eletricidade));
        }
    }
}
