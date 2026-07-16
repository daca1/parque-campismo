package parquecampismo;

/**
 * Ponto de entrada da aplicação. Permite carregar um parque previamente
 * gravado ou arrancar com um dos dois conjuntos de dados de teste
 * (ver {@link ParqueCampismo#criarParqueDeTeste1()} e
 * {@link ParqueCampismo#criarParqueDeTeste2()}), e depois entra no
 * ciclo de menus do {@link FrontOffice}.
 */
public class Main {

    private static final String FICHEIRO_DADOS = "parque.dat";

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║   GESTÃO DE UM PARQUE DE CAMPISMO       ║");
        System.out.println("║   Fundamentos de Programação OO         ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        ParqueCampismo parque = carregarOuCriarParque();

        FrontOffice frontOffice = new FrontOffice(parque, FICHEIRO_DADOS);
        frontOffice.executar();
    }

    private static ParqueCampismo carregarOuCriarParque() {
        if (GestorFicheiros.existeFicheiro(FICHEIRO_DADOS)) {
            if (ConsoleUtils.lerConfirmacao("Foi encontrado um ficheiro de dados guardado ('" + FICHEIRO_DADOS + "'). Deseja carregá-lo?")) {
                try {
                    ParqueCampismo parque = GestorFicheiros.carregar(FICHEIRO_DADOS);
                    System.out.println("\n✓ Parque '" + parque.getNome() + "' carregado com sucesso.\n");
                    return parque;
                } catch (Exception e) {
                    System.out.println("\n>> Não foi possível carregar o ficheiro (" + e.getMessage() + "). A criar um novo parque...\n");
                }
            }
        }
        return escolherParqueDeTeste();
    }

    private static ParqueCampismo escolherParqueDeTeste() {
        System.out.println("Nenhum ficheiro de dados foi carregado. Escolha um cenário inicial:");
        System.out.println("  [1] Parque de Teste 1 (3 setores, 74 talhões)");
        System.out.println("  [2] Parque de Teste 2 (4 setores, 150 talhões)");
        System.out.println("  [3] Parque vazio (definir setores/talhões manualmente mais tarde)");
        int opcao = ConsoleUtils.lerInteiroEntre("Opção: ", 1, 3);
        switch (opcao) {
            case 1:
                return ParqueCampismo.criarParqueDeTeste1();
            case 2:
                return ParqueCampismo.criarParqueDeTeste2();
            default:
                return new ParqueCampismo("Parque de Campismo");
        }
    }
}
