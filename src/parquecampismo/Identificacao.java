package parquecampismo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;

/**
 * Superclasse abstrata de todos os documentos de identificacao. Serve
 * como identificador unico de um Utente (ponto 8 do enunciado).
 */
public abstract class Identificacao implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String identificador;
    private final String nome;
    private final LocalDate dataDeNascimento;
    private final String nacionalidade;

    protected Identificacao(String identificador, String nome, LocalDate dataDeNascimento, String nacionalidade) {
        if (identificador == null || identificador.isBlank()) {
            throw new IllegalArgumentException("O identificador nao pode ser vazio.");
        }
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome nao pode ser vazio.");
        }
        if (dataDeNascimento == null) {
            throw new IllegalArgumentException("A data de nascimento e obrigatoria.");
        }
        this.identificador = identificador;
        this.nome = nome;
        this.dataDeNascimento = dataDeNascimento;
        this.nacionalidade = nacionalidade;
    }

    public String getIdentificador() {
        return identificador;
    }

    public String getNome() {
        return nome;
    }

    public LocalDate getDataNascimento() {
        return dataDeNascimento;
    }

    public String getNacionalidade() {
        return nacionalidade;
    }

    public int getIdade() {
        return Period.between(dataDeNascimento, LocalDate.now()).getYears();
    }

    /** Cria automaticamente um documento generico para um menor sem identificacao (ponto 10). */
    public static DocumentoGenerico criarParaMenor(Identificacao adultoResponsavel, String identificadorCrianca, String nomeCrianca, LocalDate dataNascimentoCrianca) {
        return new DocumentoGenerico(identificadorCrianca, nomeCrianca, dataNascimentoCrianca, adultoResponsavel.getNacionalidade(), "Documento Genérico (menor)");
    }

    public abstract String getTipoDocumento();

    @Override
    public String toString() {
        return getTipoDocumento() + " " + identificador + " - " + nome;
    }
}
