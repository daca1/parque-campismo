package parquecampismo;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * Interface de consola (front-office) que permite a um funcionario gerir
 * o dia a dia do parque: utentes, unidades, entradas/reservas e
 * faturacao (pontos 38-60 do enunciado).
 */
public class FrontOffice {

    private final ParqueCampismo parque;
    private final String ficheiroDados;
    private boolean emExecucao = true;

    public FrontOffice(ParqueCampismo parque, String ficheiroDados) {
        this.parque = parque;
        this.ficheiroDados = ficheiroDados;
    }

    public void executar() {
        while (emExecucao) {
            mostrarLogo();
            System.out.println("Parque atual: " + parque.getNome());
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║ MENU PRINCIPAL                          ║");
            System.out.println("╠════════════════════════════════════════╣");
            System.out.println("║ [1] Gerir Utentes                       ║");
            System.out.println("║ [2] Gerir Unidades                      ║");
            System.out.println("║ [3] Gerir Entradas e Reservas            ║");
            System.out.println("║ [4] Faturação                           ║");
            System.out.println("║ [5] Consultar Setores e Talhões         ║");
            System.out.println("║ [6] Gravar Parque                       ║");
            System.out.println("║ [0] Sair (grava automaticamente)        ║");
            System.out.println("╚════════════════════════════════════════╝");
            int opcao = ConsoleUtils.lerInteiroEntre("\nOpção: ", 0, 6);
            switch (opcao) {
                case 1: menuUtentes(); break;
                case 2: menuUnidades(); break;
                case 3: menuEntradas(); break;
                case 4: menuFaturacao(); break;
                case 5: consultarSetores(); break;
                case 6: gravarComFeedback(); break;
                case 0: sair(); break;
                default: break;
            }
        }
    }

    private void mostrarLogo() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║        GESTÃO DE PARQUE DE CAMPISMO     ║");
        System.out.println("╚════════════════════════════════════════╝");
    }

    private void sair() {
        gravarComFeedback();
        System.out.println("\n✓ Até breve!");
        emExecucao = false;
    }

    private void gravarComFeedback() {
        try {
            parque.gravar(ficheiroDados);
            System.out.println("\n✓ Estado do parque gravado em '" + ficheiroDados + "'.");
        } catch (IOException e) {
            System.out.println("\n>> Erro ao gravar o parque: " + e.getMessage());
        }
        ConsoleUtils.pausa();
    }

    // ===================== UTENTES =====================

    private void menuUtentes() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n┌─ Gerir Utentes ───────────────┐");
            System.out.println("│ [1] Listar                    │");
            System.out.println("│ [2] Procurar (nome ou ID)      │");
            System.out.println("│ [3] Adicionar                  │");
            System.out.println("│ [4] Alterar contactos           │");
            System.out.println("│ [5] Eliminar                   │");
            System.out.println("│ [0] Voltar                     │");
            System.out.println("└────────────────────────────────┘");
            int opcao = ConsoleUtils.lerInteiroEntre("Opção: ", 0, 5);
            switch (opcao) {
                case 1: listarUtentes(); break;
                case 2: procurarUtentes(); break;
                case 3: adicionarUtente(); break;
                case 4: alterarUtente(); break;
                case 5: eliminarUtente(); break;
                case 0: voltar = true; break;
                default: break;
            }
        }
    }

    private void listarUtentes() {
        List<Utente> utentes = parque.listarUtentes();
        System.out.println("\n--- Utentes (" + utentes.size() + ") ---");
        if (utentes.isEmpty()) {
            System.out.println("(nenhum utente registado)");
        }
        for (Utente utente : utentes) {
            String localizacao = utente.estaNoParque()
                    ? " -> unidade " + utente.getUnidadeAtual().getIdentificador()
                    : " -> fora do parque";
            System.out.println("  " + utente + localizacao);
        }
        ConsoleUtils.pausa();
    }

    private void procurarUtentes() {
        String termo = ConsoleUtils.lerTexto("\nNome ou identificação a procurar: ");
        Utente porId = parque.procurarUtentePorId(termo);
        List<Utente> porNome = parque.procurarUtentePorNome(termo);
        System.out.println("\n--- Resultados ---");
        if (porId != null) {
            System.out.println("  " + porId);
        }
        for (Utente utente : porNome) {
            if (porId == null || !utente.getIdentificacao().getIdentificador().equals(porId.getIdentificacao().getIdentificador())) {
                System.out.println("  " + utente);
            }
        }
        if (porId == null && porNome.isEmpty()) {
            System.out.println("(nenhum resultado encontrado)");
        }
        ConsoleUtils.pausa();
    }

    private void adicionarUtente() {
        System.out.println("\n--- Adicionar Utente ---");
        Identificacao identificacao = criarIdentificacao();
        String telemovel = ConsoleUtils.lerTextoObrigatorio("Telemóvel: ");
        String email = ConsoleUtils.lerTextoObrigatorio("Email: ");
        Utente utente = new Utente(identificacao, telemovel, email);
        if (ConsoleUtils.lerConfirmacao("É o responsável por uma unidade/entrada (tem cartão de crédito)?")) {
            String empresa = ConsoleUtils.lerTextoObrigatorio("Empresa do cartão: ");
            String numero = ConsoleUtils.lerTextoObrigatorio("Número do cartão: ");
            utente.setCartaoCredito(new CartaoCredito(empresa, numero));
        }
        try {
            parque.adicionarUtente(utente);
            System.out.println("\n✓ Utente adicionado com sucesso.");
        } catch (IllegalArgumentException e) {
            System.out.println("\n>> " + e.getMessage());
        }
        ConsoleUtils.pausa();
    }

    private Identificacao criarIdentificacao() {
        System.out.println("Tipo de identificação:");
        System.out.println("  [1] Cartão de Cidadão (só nacionais)");
        System.out.println("  [2] Passaporte");
        System.out.println("  [3] Carta de Condução");
        System.out.println("  [4] Documento Genérico");
        int tipo = ConsoleUtils.lerInteiroEntre("Opção: ", 1, 4);
        String identificador = ConsoleUtils.lerTextoObrigatorio("Número/identificador: ");
        String nome = ConsoleUtils.lerTextoObrigatorio("Nome: ");
        LocalDate dataNascimento = ConsoleUtils.lerData("Data de nascimento");
        switch (tipo) {
            case 1:
                return new CartaoCidadao(identificador, nome, dataNascimento);
            case 2: {
                String nacionalidade = ConsoleUtils.lerTextoObrigatorio("Nacionalidade: ");
                String paisEmissor = ConsoleUtils.lerTextoObrigatorio("País emissor: ");
                return new Passaporte(identificador, nome, dataNascimento, nacionalidade, paisEmissor);
            }
            case 3: {
                String nacionalidade = ConsoleUtils.lerTextoObrigatorio("Nacionalidade: ");
                String tipoCarta = ConsoleUtils.lerTextoObrigatorio("Tipo de carta: ");
                return new CartaConducao(identificador, nome, dataNascimento, nacionalidade, tipoCarta);
            }
            default: {
                String nacionalidade = ConsoleUtils.lerTextoObrigatorio("Nacionalidade: ");
                return new DocumentoGenerico(identificador, nome, dataNascimento, nacionalidade, "Documento Genérico");
            }
        }
    }

    private void alterarUtente() {
        Utente utente = escolherUtente();
        if (utente == null) {
            return;
        }
        String telemovel = ConsoleUtils.lerTexto("Novo telemóvel (Enter para manter '" + utente.getTelemovel() + "'): ");
        if (!telemovel.isEmpty()) {
            utente.setTelemovel(telemovel);
        }
        String email = ConsoleUtils.lerTexto("Novo email (Enter para manter '" + utente.getEmail() + "'): ");
        if (!email.isEmpty()) {
            utente.setEmail(email);
        }
        System.out.println("\n✓ Dados atualizados.");
        ConsoleUtils.pausa();
    }

    private void eliminarUtente() {
        Utente utente = escolherUtente();
        if (utente == null) {
            return;
        }
        if (parque.eliminarUtente(utente)) {
            System.out.println("\n✓ Utente eliminado.");
        } else {
            System.out.println("\n>> Não é possível eliminar: o utente já frequentou o parque ou está atualmente associado a uma unidade.");
        }
        ConsoleUtils.pausa();
    }

    private Utente escolherUtente() {
        String identificador = ConsoleUtils.lerTextoObrigatorio("\nIdentificação do utente: ");
        Utente utente = parque.procurarUtentePorId(identificador);
        if (utente == null) {
            System.out.println(">> Utente não encontrado.");
            ConsoleUtils.pausa();
        }
        return utente;
    }

    // ===================== UNIDADES =====================

    private void menuUnidades() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n┌─ Gerir Unidades ──────────────┐");
            System.out.println("│ [1] Listar                    │");
            System.out.println("│ [2] Procurar por talhão        │");
            System.out.println("│ [3] Adicionar                  │");
            System.out.println("│ [4] Eliminar                   │");
            System.out.println("│ [0] Voltar                     │");
            System.out.println("└────────────────────────────────┘");
            int opcao = ConsoleUtils.lerInteiroEntre("Opção: ", 0, 4);
            switch (opcao) {
                case 1: listarUnidades(); break;
                case 2: procurarUnidadePorTalhao(); break;
                case 3: adicionarUnidade(); break;
                case 4: eliminarUnidade(); break;
                case 0: voltar = true; break;
                default: break;
            }
        }
    }

    private void listarUnidades() {
        List<Unidade> unidades = parque.listarUnidades();
        System.out.println("\n--- Unidades (" + unidades.size() + ") ---");
        if (unidades.isEmpty()) {
            System.out.println("(nenhuma unidade registada)");
        }
        for (Unidade unidade : unidades) {
            String talhao = unidade.getTalhao() != null ? unidade.getTalhao().getCodigo() : "sem talhão";
            System.out.println("  " + unidade + " [talhão: " + talhao + ", " + unidade.getUtentes().size() + " utente(s)]");
        }
        ConsoleUtils.pausa();
    }

    private void procurarUnidadePorTalhao() {
        String codigo = ConsoleUtils.lerTextoObrigatorio("\nCódigo do talhão (ex: A3): ");
        List<Unidade> unidades = parque.procurarUnidadePorTalhao(codigo);
        System.out.println("\n--- Unidades no talhão " + codigo + " ---");
        if (unidades.isEmpty()) {
            System.out.println("(nenhuma unidade encontrada)");
        }
        for (Unidade unidade : unidades) {
            System.out.println("  " + unidade);
        }
        ConsoleUtils.pausa();
    }

    private void adicionarUnidade() {
        System.out.println("\n--- Adicionar Unidade ---");
        Utente responsavel = escolherUtente();
        if (responsavel == null) {
            return;
        }
        String identificador = ConsoleUtils.lerTextoObrigatorio("Identificador único da unidade: ");
        System.out.println("Tipo de unidade:");
        System.out.println("  [1] Autocaravana");
        System.out.println("  [2] Caravana");
        System.out.println("  [3] Tenda");
        int tipo = ConsoleUtils.lerInteiroEntre("Opção: ", 1, 3);
        double dimensao = ConsoleUtils.lerDouble("Dimensão (m²): ");
        Unidade unidade;
        switch (tipo) {
            case 1: {
                String marca = ConsoleUtils.lerTextoObrigatorio("Marca: ");
                String matricula = ConsoleUtils.lerTextoObrigatorio("Matrícula: ");
                unidade = new Autocaravana(identificador, responsavel, dimensao, marca, matricula);
                break;
            }
            case 2: {
                String marca = ConsoleUtils.lerTextoObrigatorio("Marca: ");
                String matricula = ConsoleUtils.lerTextoObrigatorio("Matrícula: ");
                unidade = new Caravana(identificador, responsavel, dimensao, marca, matricula);
                break;
            }
            default:
                unidade = new Tenda(identificador, responsavel, dimensao);
                break;
        }
        try {
            parque.adicionarUnidade(unidade);
            System.out.println("\n✓ Unidade " + unidade.getTipo() + " adicionada com sucesso.");
        } catch (IllegalArgumentException e) {
            System.out.println("\n>> " + e.getMessage());
        }
        ConsoleUtils.pausa();
    }

    private void eliminarUnidade() {
        String identificador = ConsoleUtils.lerTextoObrigatorio("\nIdentificador da unidade: ");
        Unidade unidade = encontrarUnidade(identificador);
        if (unidade == null) {
            System.out.println(">> Unidade não encontrada.");
            ConsoleUtils.pausa();
            return;
        }
        if (parque.eliminarUnidade(unidade)) {
            System.out.println("\n✓ Unidade eliminada.");
        } else {
            System.out.println("\n>> Não é possível eliminar: a unidade tem utentes atualmente associados.");
        }
        ConsoleUtils.pausa();
    }

    private Unidade encontrarUnidade(String identificador) {
        for (Unidade unidade : parque.listarUnidades()) {
            if (unidade.getIdentificador().equalsIgnoreCase(identificador)) {
                return unidade;
            }
        }
        return null;
    }

    // ===================== ENTRADAS / RESERVAS =====================

    private void menuEntradas() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n┌─ Entradas e Reservas ─────────────┐");
            System.out.println("│ [1] Listar todas                   │");
            System.out.println("│ [2] Registar entrada / reserva      │");
            System.out.println("│ [3] Adicionar unidade a entrada     │");
            System.out.println("│ [4] Remover unidade de entrada      │");
            System.out.println("│ [5] Remover utente de entrada       │");
            System.out.println("│ [6] Eliminar entrada fechada e vazia│");
            System.out.println("│ [0] Voltar                          │");
            System.out.println("└──────────────────────────────────────┘");
            int opcao = ConsoleUtils.lerInteiroEntre("Opção: ", 0, 6);
            switch (opcao) {
                case 1: listarEntradas(); break;
                case 2: registarEntrada(); break;
                case 3: adicionarUnidadeAEntrada(); break;
                case 4: removerUnidadeDeEntrada(); break;
                case 5: removerUtenteDeEntrada(); break;
                case 6: eliminarEntrada(); break;
                case 0: voltar = true; break;
                default: break;
            }
        }
    }

    private void listarEntradas() {
        List<Entrada> entradas = parque.listarEntradas();
        System.out.println("\n--- Entradas (" + entradas.size() + ") ---");
        if (entradas.isEmpty()) {
            System.out.println("(nenhuma entrada registada)");
        }
        for (Entrada entrada : entradas) {
            System.out.println("  " + entrada);
        }
        ConsoleUtils.pausa();
    }

    private void registarEntrada() {
        System.out.println("\n--- Registar Entrada / Reserva ---");
        Utente responsavel = escolherUtente();
        if (responsavel == null) {
            return;
        }
        LocalDate dataEntrada = ConsoleUtils.lerData("Data de entrada");
        Entrada entrada = parque.registarEntrada(responsavel, dataEntrada);
        System.out.println("\n✓ " + (entrada.isReserva() ? "Reserva registada" : "Entrada registada") + ": " + entrada);
        if (ConsoleUtils.lerConfirmacao("Deseja associar já uma unidade a esta entrada?")) {
            associarUnidadeAEntradaAberta(entrada);
        }
        ConsoleUtils.pausa();
    }

    private void adicionarUnidadeAEntrada() {
        Entrada entrada = escolherEntradaAberta();
        if (entrada == null) {
            return;
        }
        associarUnidadeAEntradaAberta(entrada);
        ConsoleUtils.pausa();
    }

    private void associarUnidadeAEntradaAberta(Entrada entrada) {
        String identificadorUnidade = ConsoleUtils.lerTextoObrigatorio("Identificador da unidade a associar: ");
        Unidade unidade = encontrarUnidade(identificadorUnidade);
        if (unidade == null) {
            System.out.println(">> Unidade não encontrada.");
            return;
        }
        String codigoTalhao = ConsoleUtils.lerTextoObrigatorio("Código do talhão (ex: A3): ");
        Talhao talhao = parque.getTalhaoPorCodigo(codigoTalhao);
        if (talhao == null) {
            System.out.println(">> Talhão não encontrado.");
            return;
        }
        try {
            entrada.adicionarUnidade(unidade, talhao);
            entrada.adicionarUtente(unidade.getResponsavel());
            unidade.adicionarUtente(unidade.getResponsavel());
            unidade.getResponsavel().darEntrada(unidade);
            System.out.println("\n✓ Unidade " + unidade.getIdentificador() + " instalada no talhão " + talhao.getCodigo() + ".");
        } catch (IllegalStateException e) {
            System.out.println("\n>> " + e.getMessage());
        }
    }

    private void removerUnidadeDeEntrada() {
        Entrada entrada = escolherEntradaAberta();
        if (entrada == null) {
            return;
        }
        String identificadorUnidade = ConsoleUtils.lerTextoObrigatorio("Identificador da unidade a remover: ");
        Unidade unidade = encontrarUnidade(identificadorUnidade);
        if (unidade == null || !entrada.getUnidades().contains(unidade)) {
            System.out.println(">> Essa unidade não está associada a esta entrada.");
            ConsoleUtils.pausa();
            return;
        }
        entrada.removerUnidade(unidade);
        for (Utente utente : unidade.getUtentes()) {
            utente.sair();
        }
        System.out.println("\n✓ Unidade removida da entrada. O talhão foi libertado se ficou vazio.");
        ConsoleUtils.pausa();
    }

    private void removerUtenteDeEntrada() {
        Entrada entrada = escolherEntradaAberta();
        if (entrada == null) {
            return;
        }
        String identificadorUtente = ConsoleUtils.lerTextoObrigatorio("Identificação do utente a remover: ");
        Utente utente = parque.procurarUtentePorId(identificadorUtente);
        if (utente == null || !entrada.getUtentes().contains(utente)) {
            System.out.println(">> Esse utente não está associado a esta entrada.");
            ConsoleUtils.pausa();
            return;
        }
        if (utente == entrada.getResponsavel() && entrada.getUtentes().size() > 1) {
            System.out.println(">> O responsável só pode ser removido quando for o último utente da entrada.");
            System.out.println("   Utilize primeiro outra operação para reatribuir a responsabilidade, se necessário.");
            ConsoleUtils.pausa();
            return;
        }
        try {
            entrada.removerUtente(utente);
            utente.sair();
            System.out.println("\n✓ Utente removido da entrada.");
        } catch (IllegalStateException e) {
            System.out.println("\n>> " + e.getMessage());
        }
        ConsoleUtils.pausa();
    }

        private void eliminarEntrada() {
        int id = ConsoleUtils.lerInteiro("\nID da entrada: ");
        Entrada entrada = parque.getEntradaPorId(id);
        if (entrada == null) {
            System.out.println(">> Entrada não encontrada.");
        } else if (parque.eliminarEntrada(entrada)) {
            System.out.println("\n✓ Entrada eliminada.");
        } else {
            System.out.println("\n>> Só é possível eliminar entradas fechadas e sem utentes/unidades associados.");
        }
        ConsoleUtils.pausa();
    }

    private Entrada escolherEntradaAberta() {
        listarEntradas();
        int id = ConsoleUtils.lerInteiro("ID da entrada: ");
        Entrada entrada = parque.getEntradaPorId(id);
        if (entrada == null || entrada.getEstado() == EstadoEntrada.FECHADA) {
            System.out.println(">> Entrada não encontrada ou já fechada.");
            ConsoleUtils.pausa();
            return null;
        }
        return entrada;
    }

    // ===================== FATURAÇÃO =====================

    private void menuFaturacao() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n┌─ Faturação ────────────────────┐");
            System.out.println("│ [1] Emitir fatura (check-out)   │");
            System.out.println("│ [2] Listar faturas               │");
            System.out.println("│ [3] Anular fatura                │");
            System.out.println("│ [0] Voltar                       │");
            System.out.println("└───────────────────────────────────┘");
            int opcao = ConsoleUtils.lerInteiroEntre("Opção: ", 0, 3);
            switch (opcao) {
                case 1: emitirFatura(); break;
                case 2: listarFaturas(); break;
                case 3: anularFatura(); break;
                case 0: voltar = true; break;
                default: break;
            }
        }
    }

    private void emitirFatura() {
        Entrada entrada = escolherEntradaAberta();
        if (entrada == null) {
            return;
        }
        LocalDate dataSaida = ConsoleUtils.lerData("Data de saída");
        try {
            Fatura fatura = parque.emitirFatura(entrada, dataSaida);
            System.out.println("\n" + fatura.gerarTextoFatura());
            String nomeFicheiro = "fatura_" + fatura.getId() + ".txt";
            fatura.imprimirParaFicheiro(nomeFicheiro);
            System.out.println("✓ Fatura gravada em '" + nomeFicheiro + "'.");
        } catch (IllegalStateException e) {
            System.out.println("\n>> " + e.getMessage());
        } catch (IOException e) {
            System.out.println("\n>> Erro ao gravar o ficheiro da fatura: " + e.getMessage());
        }
        ConsoleUtils.pausa();
    }

    private void listarFaturas() {
        List<Fatura> faturas = parque.listarFaturas();
        System.out.println("\n--- Faturas (" + faturas.size() + ") ---");
        if (faturas.isEmpty()) {
            System.out.println("(nenhuma fatura emitida)");
        }
        for (Fatura fatura : faturas) {
            System.out.println("  " + fatura);
        }
        ConsoleUtils.pausa();
    }

    private void anularFatura() {
        String id = ConsoleUtils.lerTextoObrigatorio("\nNúmero da fatura a anular: ");
        Fatura fatura = parque.listarFaturas().stream()
                .filter(f -> f.getId().equals(id))
                .findFirst()
                .orElse(null);
        if (fatura == null) {
            System.out.println(">> Fatura não encontrada.");
        } else {
            try {
                parque.anularFatura(fatura);
                System.out.println("\n✓ Fatura anulada.");
            } catch (IllegalStateException e) {
                System.out.println("\n>> " + e.getMessage());
            }
        }
        ConsoleUtils.pausa();
    }

    // ===================== CONSULTA =====================

    private void consultarSetores() {
        System.out.println("\n--- Setores do " + parque.getNome() + " ---");
        for (Setor setor : parque.getSetores()) {
            System.out.println(setor);
            for (Talhao talhao : setor.getTalhoes()) {
                System.out.println("    " + talhao);
            }
        }
        ConsoleUtils.pausa();
    }
}
