import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.DayOfWeek;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Locale;

public class ProjetoLapr1
{

    /* CONSTANTES CONSIDERADAS NESTE TRABALHO */
    static final int MAX_DIAS = 912; // aproximadamente dois anos
    static final int NUMERO_PARAMETROS = 5;
    static final int MODO_INTERATIVO = 0;
    static final int MODO_NAO_INTERATIVO = 1;
    static final int MODO_TOTAL = 20;
    static final int MODO_SEM_PREVISOES = 16;
    static final int MODO_APENAS_PREVISOES = 5;
    static final int NUMERO_FICHEIROS_MODO_TOTAL = 4;
    static final int NUMERO_FICHEIROS_MODO_SEM_PREVISOES = 2;
    static final int NUMERO_FICHEIROS_MODO_PREVISOES = 3;
    static final int ANALISE_ACUMULADOS = 1;
    static final int ANALISE_TOTAIS = 2;
    static final int RESOLUCAO_DIARIA = 0;
    static final int RESOLUCAO_SEMANAL = 1;
    static final int RESOLUCAO_MENSAL = 2;
    static final String[] ESCOLHAS_MENU = {
            "Carregar ficheiro de dados",
            "Visualizar dados",
            "Análise comparativa",
            "Análise de previsão"
    };
    static final String[] ESCOLHAS_SUBMENU_FICHEIRO = {
            "Carregar ficheiro de dados acumulados",
            "Carregar ficheiros de dados totais",
            "Carregar ficheiro com Matriz de Transições"
    };
    static final String[] ESCOLHAS_SUBMENU_VISUALIZAR = {
            "Novos casos",
            "Casos totais"
    };

    static final String[] ESCOLHAS_SUBMENU_PREVISAO = {
            "Estimativa de casos totais num dia",
            "Número esperado de dias até ao óbito"
    };

    static final String[] ESCOLHAS_MENU_APOS = {
            "Gravar dados em ficheiro [*.csv]"
    };

    static final String[] CABECALHO_FICHEIRO_ACUMULADOS = {
            "data",
            "diario_nao_infetado",
            "acumulado_infetado",
            "acumulado_hospitalizado",
            "acumulado_internadoUCI",
            "acumulado_mortes"};

    static final String[] CABECALHO_FICHEIRO_TOTAIS = {
            "data",
            "naoInfetados",
            "Infetados",
            "hospitalizados",
            "internadosUCI",
            "obitos"};

    static final String[] ORDEM_ARGS_ENTRADA_MODO_TOTAL = {"-r", "-di", "-df", "-di1", "-df1", "-di2", "-df2", "-T"};
    static final String[] ORDEM_ARGS_ENTRADA_MODO_SEM_PREVISAO = {"-r", "-di", "-df", "-di1", "-df1", "-di2", "-df2"};
    static final String[] ORDEM_ARGS_ENTRADA_MODO_SO_PREVISAO = {"-T"};
    static boolean haDadosAcumulados = false;
    static boolean haDadosTotais = false;
    static boolean haDadosMarkov = false;
    static String ficheiroAcumuladosLido = "";
    static String ficheiroTotaisLido = "";
    static String ficheiroMarkovLido = "";

    static Scanner sc = new Scanner(System.in);
    static PrintWriter pw;


    public static void main(String[] args) throws FileNotFoundException
    {
        // Definir o Locale como US para garantir que os separador decimal é um ponto e não vírgula (se for vírgula dá
        // problemas quando se grava para ficheiro .csv
        Locale.setDefault(new Locale("en", "US"));

        // Correr testes
        //Testes.runTestes();

        // Se o array args do main() estiver vazia entao estamos no modo interativo
        if (args.length == 0)
        {
            // Executar programa em modo interactivo
            executarModoInterativo();
        }
        else
        {
            // Se tivermos apenas um argumento verificar se o utilizador esta a pedir ajuda para executar o programa
            if ((args.length == 1) && (args[0].compareTo("--help") == 0))
            {
                imprimirAjudaModoNaoInterativo();
            }
            else
            {
                // Executar programa em modo não interactivo
                executarModoNaoInterativo(args);
            }

        }

    }

    /**
     * Executa a aplicação no modo Interativo
     * @throws FileNotFoundException
     */
    public static void executarModoInterativo() throws FileNotFoundException
    {

        // Array e matriz de dados para armazenar dados do ficheiro de dados acumulados
        String[] arrDatasAcum = new String[MAX_DIAS];
        int[][] matrizDadosAcum = new int[MAX_DIAS][NUMERO_PARAMETROS];

        // Array e matriz de dados para armazenar dados do ficheiro de dados totais
        String[] arrDatasTotais = new String[MAX_DIAS];
        int[][] matrizDadosTotais = new int[MAX_DIAS][NUMERO_PARAMETROS];
        double[][] markov = new double[NUMERO_PARAMETROS][NUMERO_PARAMETROS];

        // Ciclo menu principal
        boolean sair = false;
        while (!sair)
        {
            imprimirMenuPrincipal();
            int escolhaMenu = obterEscolhaDoUtilizador();
            while (escolhaMenu < 0 || escolhaMenu > ESCOLHAS_MENU.length)
            {
                System.out.printf("Escolha inválida, por favor introduza um valor válido [%d - %d]\n", 0, ESCOLHAS_MENU.length);
                escolhaMenu = obterEscolhaDoUtilizador();
            }
            if (escolhaMenu != 0)
            {
                switch (escolhaMenu)
                {
                    case 1:
                        imprimirSubmenuLerFicheiroDados();
                        int escolhaSubMenuFicheiro = obterEscolhaDoUtilizador();
                        while (escolhaSubMenuFicheiro < 0 || escolhaSubMenuFicheiro > ESCOLHAS_SUBMENU_FICHEIRO.length)
                        {
                            System.out.printf("Escolha inválida, por favor introduza um valor válido [%d - %d]\n", 0, ESCOLHAS_SUBMENU_FICHEIRO.length);
                            escolhaSubMenuFicheiro = obterEscolhaDoUtilizador();
                        }
                        String[] arr = new String[MAX_DIAS];
                        int[][] matriz = new int[MAX_DIAS][NUMERO_PARAMETROS];
                        if (escolhaSubMenuFicheiro == 3)
                        {
                            System.out.print("@ Introduza o nome do ficheiro: ");
                            String nomeDoFicheiro = sc.next();
                            if (!matrizEstaVaziaZeros(lerDadosDoFicheiroMarkov(markov, nomeDoFicheiro)))
                            {
                                System.out.println("@ Ficheiro carregado com sucesso");
                                haDadosMarkov = true;
                                ficheiroMarkovLido = nomeDoFicheiro;
                            }
                        }
                        else if (escolhaSubMenuFicheiro != 0)
                        {

                            System.out.print("@ Introduza o nome do ficheiro: ");
                            String nomeDoFicheiro = sc.next();
                            int ficheiroLido = lerDadosDoFicheiro(arr, matriz, escolhaSubMenuFicheiro, nomeDoFicheiro);
                            if (ficheiroLido != 0)
                            {
                                System.out.printf("@ Ficheiro %s carregado com sucesso\n", nomeDoFicheiro);
                                System.out.println("@ " + ficheiroLido + " dias carregados");
                            }
                            System.out.print("@ Deseja carregar mais ficheiros do mesmo tipo? s/n: ");
                            String carregarMaisFicheiros = sc.next();


                            while (carregarMaisFicheiros.equalsIgnoreCase("s"))
                            {
                                System.out.print("@ Introduza o nome do ficheiro: ");
                                nomeDoFicheiro = sc.next();
                                String[] arrNova = new String[MAX_DIAS];
                                int[][] matrizNova = new int[MAX_DIAS][NUMERO_PARAMETROS];
                                String[] arrFinal = new String[MAX_DIAS];
                                int[][] matrizFinal = new int[MAX_DIAS][NUMERO_PARAMETROS];
                                int novoFicheiroLido = adicionarDadosDoFicheiro(arr, matriz, arrNova, matrizNova, escolhaSubMenuFicheiro, nomeDoFicheiro, arrFinal, matrizFinal);
                                System.out.printf("@ Ficheiro %s carregado com sucesso\n", nomeDoFicheiro);
                                System.out.println("@ " + novoFicheiroLido + " dias carregados");
                                int totalCarregado = obterIndiceDaUltimaLinhaPreenchidaDeArray(arrFinal);
                                System.out.println("@ " + (totalCarregado + 1) + " dias carregados no total de ficheiros");
                                arr = arrFinal;
                                matriz = matrizFinal;
                                System.out.print("@ Deseja carregar mais ficheiros do mesmo tipo? s/n: ");
                                carregarMaisFicheiros = sc.next();
                            }
                            if (ficheiroLido != 0)
                            {
                                if (escolhaSubMenuFicheiro == 1)
                                {
                                    arrDatasAcum = arr;
                                    matrizDadosAcum = matriz;
                                    haDadosAcumulados = true;
                                    ficheiroAcumuladosLido = nomeDoFicheiro;
                                }
                                else
                                {
                                    arrDatasTotais = arr;
                                    matrizDadosTotais = matriz;
                                    haDadosTotais = true;
                                    ficheiroTotaisLido = nomeDoFicheiro;
                                }
                            }
                        }
                        break;
                    case 2:

                        // imprimir submenu dos tipos de analise de dados
                        imprimirSubmenuVisualizar();

                        int escolhaSubMenuVisualizar = obterEscolhaDoUtilizador();
                        while (escolhaSubMenuVisualizar < 0 || escolhaSubMenuVisualizar > ESCOLHAS_SUBMENU_FICHEIRO.length)
                        {
                            System.out.printf("Escolha inválida, por favor introduza um valor válido [%d - %d]\n", 0, ESCOLHAS_SUBMENU_FICHEIRO.length);
                            escolhaSubMenuVisualizar = obterEscolhaDoUtilizador();
                        }

                        if (escolhaSubMenuVisualizar != 0)
                        {
                            // Executar analise de dados
                            String periodo = "";
                            int resolucaoAnalise = 0;
                            switch (escolhaSubMenuVisualizar)
                            {
                                case ANALISE_ACUMULADOS:
                                    if (haDadosAcumulados)
                                    {
                                        // Pedir periodo ao utilizador
                                        periodo = lerPeriodoDias();

                                        // Obter resolucao
                                        resolucaoAnalise = obterResolucaoAnalise();
                                        analiseDados(periodo, resolucaoAnalise, arrDatasAcum, matrizDadosAcum, ANALISE_ACUMULADOS, MODO_INTERATIVO, "");
                                    }
                                    else
                                    {
                                        System.out.println("! Não há dados carregados. Por favor carregue um ficheiro de dados acumulados.");
                                        System.out.println();
                                    }
                                    break;
                                case ANALISE_TOTAIS:
                                    if (haDadosTotais)
                                    {
                                        // Pedir periodo ao utilizador
                                        periodo = lerPeriodoDias();

                                        // Obter resolucao
                                        resolucaoAnalise = obterResolucaoAnalise();

                                        // Executar analise de dados
                                        analiseDados(periodo, resolucaoAnalise, arrDatasTotais, matrizDadosTotais, ANALISE_TOTAIS, MODO_INTERATIVO, "");
                                    }
                                    else
                                    {
                                        System.out.println("! Não há dados carregados. Por favor carregue um ficheiro de dados totais.");
                                        System.out.println();
                                    }
                                    break;
                            }

                        }

                        break;
                    case 3:
                        // imprimir submenu dos tipos de analise de dados
                        imprimirSubmenuComparativa();

                        int escolhaSubMenuComparativa = obterEscolhaDoUtilizador();
                        while (escolhaSubMenuComparativa < 0 || escolhaSubMenuComparativa > ESCOLHAS_SUBMENU_FICHEIRO.length)
                        {
                            System.out.printf("Escolha inválida, por favor introduza um valor válido [%d - %d]\n", 0, ESCOLHAS_SUBMENU_FICHEIRO.length);
                            escolhaSubMenuComparativa = obterEscolhaDoUtilizador();
                        }

                        if (escolhaSubMenuComparativa != 0)
                        {
                            // Executar analise de dados
                            String periodo1 = "", periodo2 = "";
                            switch (escolhaSubMenuComparativa)
                            {
                                case ANALISE_ACUMULADOS:
                                    if (haDadosAcumulados)
                                    {
                                        // Pedir periodo ao utilizador
                                        periodo1 = lerPeriodoDias();
                                        periodo2 = lerPeriodoDias();

                                        // Obter resolucao
                                        analiseComparativa(periodo1, periodo2, arrDatasAcum, matrizDadosAcum, ANALISE_ACUMULADOS, MODO_INTERATIVO, "");
                                    }
                                    else
                                    {
                                        System.out.println("! Não há dados carregados. Por favor carregue um ficheiro de dados acumulados.");
                                        System.out.println();
                                    }
                                    break;
                                case ANALISE_TOTAIS:
                                    if (haDadosTotais)
                                    {
                                        // Pedir periodo ao utilizador
                                        periodo1 = lerPeriodoDias();
                                        periodo2 = lerPeriodoDias();

                                        // Obter resolucao
                                        analiseComparativa(periodo1, periodo2, arrDatasTotais, matrizDadosTotais, ANALISE_TOTAIS, MODO_INTERATIVO, "");
                                    }
                                    else
                                    {
                                        System.out.println("! Não há dados carregados. Por favor carregue um ficheiro de dados totais.");
                                        System.out.println();
                                    }
                                    break;
                            }

                        }

                        break;
                    case 4:
                        imprimirSubmenuPrevisao();
                        int escolhaSubMenuPrevisao = obterEscolhaDoUtilizador();
                        while (escolhaSubMenuPrevisao < 0 || escolhaSubMenuPrevisao > ESCOLHAS_SUBMENU_PREVISAO.length)
                        {
                            System.out.printf("Escolha inválida, por favor introduza um valor válido [%d - %d]\n", 0, ESCOLHAS_SUBMENU_PREVISAO.length);
                            escolhaSubMenuPrevisao = obterEscolhaDoUtilizador();
                        }
                        if (escolhaSubMenuPrevisao != 0)
                        {
                            switch (escolhaSubMenuPrevisao)
                            {
                                case 1:
                                    // correr aqui metodo "Estimativa de casos totais num dia"
                                    if (obterIndiceDaUltimaLinhaPreenchidaDeArray(arrDatasTotais) != -1)
                                    {
                                        analisePrevisaoMarkov(markov, arrDatasTotais, matrizDadosTotais, "", MODO_INTERATIVO, "");
                                        //analisouOK();
                                    }
                                    else System.out.println("@ Por favor carregue os ficheiros necessários.");
                                    break;
                                case 2:
                                    if (!matrizEstaVaziaZeros(markov))
                                    {
                                        mostrarNumeroDiasAteAMorte(markov);
                                        //analisouOK();
                                    }
                                    else System.out.println("@ Por favor carregue Matriz de Transições.");
                                    break;
                            }
                        }
                        break;
                }
            }
            else
            {
                sair = true;
            }
        }
    }

    /**
     * Caso o ficheiro dado exista, adiciona os dados numa matriz
     * @param arr array String
     * @param matriz matriz int
     * @param arrNova array String
     * @param matrizNova matriz int
     * @param escolhaSubMenuFicheiro int
     * @param nomeDoFicheiro String
     * @param arrFinal array String
     * @param matrizFinal matriz int
     * @return
     * @throws FileNotFoundException
     */
    public static int adicionarDadosDoFicheiro(String[] arr, int[][] matriz, String[] arrNova,
                                               int[][] matrizNova, int escolhaSubMenuFicheiro, String nomeDoFicheiro,
                                               String[] arrFinal, int[][] matrizFinal) throws FileNotFoundException
    {
        int qtdDias = 0;
        File ficheiroDeDados = new File(nomeDoFicheiro);
        boolean exists = ficheiroDeDados.exists();
        if (exists)
        {
            Scanner in = new Scanner(ficheiroDeDados);
            String linhaCabecalho = in.nextLine();
            String[] cabecalho = linhaCabecalho.split(",");
            String[] cabecalhoValido;
            if (escolhaSubMenuFicheiro == 1)
            {
                cabecalhoValido = CABECALHO_FICHEIRO_ACUMULADOS;
            }
            else
            {
                cabecalhoValido = CABECALHO_FICHEIRO_TOTAIS;
            }
            boolean validacao = validacaoDoCabecalho(cabecalho, cabecalhoValido);
            if (validacao)
            {
                while (in.hasNextLine())
                {
                    String linha = in.nextLine();
                    String[] elementos = linha.split(",");
                    // Se for o ficheiro de dados totais trocar a data de dd-MM-yyyy para yyyy-mm-dd
                    if (escolhaSubMenuFicheiro == 2)
                    {
                        elementos[0] = formatarData(elementos[0]);
                    }
                    arrNova[qtdDias] = elementos[0];
                    for (int estadoSaude = 0, elemento = 1; elemento < elementos.length; estadoSaude++, elemento++)
                    {
                        matrizNova[qtdDias][estadoSaude] = Integer.parseInt(elementos[elemento]);
                    }
                    qtdDias++;
                }
                in.close();
                String primeiroDiaNovo = arrNova[0];
                //String primeiraDataNovoFicheiro = arrNova[1];
                //int diaExiste = obterIndiceDoDia(primeiraDataNovoFicheiro, arr);
                int diaExiste = obterIndiceDoDia(primeiroDiaNovo, arr);

                int indiceUltimoDiaEmEspera = obterIndiceDaUltimaLinhaPreenchidaDeArray(arr);
                String ultimoDiaEmEspera = arr[indiceUltimoDiaEmEspera];
                int indiceUltimoDiaDaNova = obterIndiceDaUltimaLinhaPreenchidaDeArray(arrNova);
                //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
                LocalDate dataEmEspera = LocalDate.parse(ultimoDiaEmEspera, formatter);
                LocalDate dataNova = LocalDate.parse(primeiroDiaNovo, formatter);
                if (diaExiste == -1)
                {
                    if (dataEmEspera.isBefore(dataNova))
                    {
                        for (int i = 0; i <= indiceUltimoDiaEmEspera; i++)
                        {
                            arrFinal[i] = arr[i];
                            matrizFinal[i] = matriz[i];
                        }
                        for (int j = 0; j <= indiceUltimoDiaDaNova; j++)
                        {
                            arrFinal[indiceUltimoDiaEmEspera + 1 + j] = arrNova[j];
                            matrizFinal[indiceUltimoDiaEmEspera + 1 + j] = matrizNova[j];
                        }

                    }
                    else
                    {
                        for (int i = 0; i <= indiceUltimoDiaDaNova; i++)
                        {
                            arrFinal[i] = arrNova[i];
                            matrizFinal[i] = matrizNova[i];
                        }
                        for (int j = 0; j <= indiceUltimoDiaEmEspera; j++)
                        {
                            arrFinal[indiceUltimoDiaDaNova + 1 + j] = arr[j];
                            matrizFinal[indiceUltimoDiaDaNova + 1 + j] = matriz[j];
                        }
                    }
                }
                else
                {

                    for (int i = 0; i <= diaExiste; i++)
                    {
                        arrFinal[i] = arr[i];
                        matrizFinal[i] = matriz[i];
                    }
                    for (int j = 0; j <= indiceUltimoDiaDaNova; j++)
                    {
                        //arrFinal[diaExiste - 1 + j] = arrNova[j];
                        //matrizFinal[diaExiste - 1 + j] = matrizNova[j];
                        arrFinal[diaExiste + j] = arrNova[j];
                        matrizFinal[diaExiste + j] = matrizNova[j];
                    }
                    //int elementosEmFalta = diaExiste + indiceUltimoDiaEmEspera - indiceUltimoDiaDaNova;
                    int elementosEmFalta = indiceUltimoDiaEmEspera - indiceUltimoDiaDaNova - diaExiste;
                    int indiceConcatena = indiceUltimoDiaDaNova + diaExiste + 1;
                    for (int l = 0; l <= elementosEmFalta; l++)
                    {
                        arrFinal[indiceConcatena + l] = arr[indiceConcatena + l];
                        matrizFinal[indiceConcatena + l] = matriz[indiceConcatena + l];
                    }


                }
            }
            else if (escolhaSubMenuFicheiro == 1)
            {
                System.out.println("@ O ficheiro indicado não é de dados acumulados");
                imprimirSubmenuLerFicheiroDados();
            }
            else
            {
                System.out.println("@ O ficheiro indicado não é de dados totais");
                imprimirSubmenuLerFicheiroDados();
            }
        }
        else
        {
            System.out.println("@ O ficheiro indicado não existe");
        }
        return qtdDias;
    }

    /**
     * Lê qual a resolução desejada pelo utilizador
     * 0 - Diária
     * 1 - Semanal
     * 2- Mensal
     * @return
     */
    public static int obterResolucaoAnalise()
    {
        // Pedir resolução ao utilizador
        System.out.println("## Introduza a resolução que pretende (0 - diaria | 1 - semanal | 2 - mensal): ");
        int resolucao = obterEscolhaDoUtilizador();
        while (resolucao < 0 || resolucao > 2)
        {
            System.out.printf("Escolha inválida, por favor introduza um valor válido [%d - %d]\n", 0, 2);
            resolucao = obterEscolhaDoUtilizador();
        }
        return resolucao;
    }

    /**
     * Valida se a análise foi bem sucedida
     * @return
     */
    public static int analisouOK()
    {
        System.out.println("[SUCESSO] Análise concluída");
        imprimirmenuAposAnalise();
        int escolhaMenuAposAnalise = obterEscolhaDoUtilizador();
        while (escolhaMenuAposAnalise < 0 || escolhaMenuAposAnalise > ESCOLHAS_MENU_APOS.length)
        {
            System.out.printf("Escolha inválida, por favor introduza um valor válido [%d - %d]\n", 0, ESCOLHAS_MENU_APOS.length);
            escolhaMenuAposAnalise = obterEscolhaDoUtilizador();
        }

        return escolhaMenuAposAnalise;
    }

    /**
     * Imprime o Menu Principal
     */
    public static void imprimirMenuPrincipal()
    {
        System.out.println("|-----------------------------------------------------------------------------|");
        System.out.println("|                      MONITORIZAÇÃO DA SAÚDE EM PORTUGAL                     |");
        System.out.println("|                                    ***                                      |");
        System.out.println("|           APLICAÇÃO DE ANÁLISE E PREVISÃO DA PANDEMIA COVID-19              |");
        System.out.println("|-----------------------------------------------------------------------------|");
        System.out.println("|                                                                             |");
        System.out.println("|                               MENU PRINCIPAL                                |");
        System.out.println("|                                                                             |");

        for (int i = 0; i < ESCOLHAS_MENU.length; i++)
        {
            System.out.printf("|                 [%d].%-40s                |\n", i + 1, ESCOLHAS_MENU[i]);
        }
        System.out.printf("|                 [0].%-40s                |\n", "Sair da aplicação");
        System.out.println("|                                                                             |");
        System.out.println("|-----------------------------------------------------------------------------|");
        System.out.printf("| Ficheiro dados acumulados: %-49s|\n", haDadosAcumulados == true ? ficheiroAcumuladosLido : "N/A");
        System.out.printf("| Ficheiro dados totais: %-53s|\n", haDadosTotais == true ? ficheiroTotaisLido : "N/A");
        System.out.printf("| Ficheiro dados Markov: %-53s|\n", haDadosMarkov == true ? ficheiroMarkovLido : "N/A");
        System.out.println("|-----------------------------------------------------------------------------|");
    }

    /**
     * Imprime sub-menu da visualização de dados
     */
    public static void imprimirSubmenuVisualizar()
    {
        System.out.println("|-----------------------------------------------------------------------------|");
        System.out.println("|                           SUBMENU - VISUALIZAR DADOS                        |");
        System.out.println("|                                                                             |");
        for (int i = 0; i < ESCOLHAS_SUBMENU_VISUALIZAR.length; i++)
        {
            System.out.printf("|                 [%d].%-40s                |\n", i + 1, ESCOLHAS_SUBMENU_VISUALIZAR[i]);
        }
        System.out.printf("|                 [0].%-40s                |\n", "Sair para o menu principal");
        System.out.println("|-----------------------------------------------------------------------------|");
    }

    /**
     * Imprime sub-menu da análise comparativa
     */
    public static void imprimirSubmenuComparativa()
    {
        System.out.println("|-----------------------------------------------------------------------------|");
        System.out.println("|                         SUBMENU - ANALISE COMPARATIVA                       |");
        System.out.println("|                                                                             |");
        for (int i = 0; i < ESCOLHAS_SUBMENU_VISUALIZAR.length; i++)
        {
            System.out.printf("|                 [%d].%-40s                |\n", i + 1, ESCOLHAS_SUBMENU_VISUALIZAR[i]);
        }
        System.out.printf("|                 [0].%-40s                |\n", "Sair para o menu principal");
        System.out.println("|-----------------------------------------------------------------------------|");
    }

    /**
     * Imprime sub-menu da leitura do ficheiro de dados
     */
    public static void imprimirSubmenuLerFicheiroDados()
    {
        System.out.println("|-----------------------------------------------------------------------------|");
        System.out.println("|                   SUBMENU - CARREGAR FICHEIRO DE DADOS                      |");
        System.out.println("|                                                                             |");
        for (int i = 0; i < ESCOLHAS_SUBMENU_FICHEIRO.length; i++)
        {
            System.out.printf("|                 [%d].%-55s |\n", i + 1, ESCOLHAS_SUBMENU_FICHEIRO[i]);
        }
        System.out.printf("|                 [0].%-40s                |\n", "Sair para o menu principal");
        System.out.println("|-----------------------------------------------------------------------------|");
    }

    /**
     * Imprime o sub-menu da análise de previsão
     */
    public static void imprimirSubmenuPrevisao()
    {
        System.out.println("|-----------------------------------------------------------------------------|");
        System.out.println("|                       SUBMENU - ANÁLISE DE PREVISÃO                         |");
        System.out.println("|                                                                             |");
        for (int i = 0; i < ESCOLHAS_SUBMENU_PREVISAO.length; i++)
        {
            System.out.printf("|                 [%d].%-40s                |\n", i + 1, ESCOLHAS_SUBMENU_PREVISAO[i]);
        }
        System.out.printf("|                 [0].%-40s                |\n", "Sair para o menu principal");
        System.out.println("|-----------------------------------------------------------------------------|");
    }

    /**
     * Imprime sub-menu após análise dos dados
     */
    public static void imprimirmenuAposAnalise()
    {
        System.out.println("|-----------------------------------------------------------------------------|");
        System.out.println("|                           SUBMENU - APÓS ANÁLISE                            |");
        System.out.println("|                                                                             |");
        for (int i = 0; i < ESCOLHAS_MENU_APOS.length; i++)
        {
            System.out.printf("|                 [%d].%-40s                |\n", i + 1, ESCOLHAS_MENU_APOS[i]);
        }
        System.out.printf("|                 [0].%-40s                |\n", "Sair para o menu principal");
        System.out.println("|-----------------------------------------------------------------------------|");
    }

    public static int obterEscolhaDoUtilizador()
    {
        int escolha;
        System.out.print("@ Insira aqui a sua escolha: ");
        // Validar escolha do utilizador
        try
        {
            escolha = sc.nextInt();
        } catch (InputMismatchException e)
        {
            // Limpar buffer
            sc.nextLine();
            escolha = -1;
        }
        return escolha;
    }

    /**
     * Imprime o menu de ajuda do modo não interativo
     * flag "--help"
     */
    public static void imprimirAjudaModoNaoInterativo()
    {
        System.out.println();
        System.out.println(" [LISTA DE COMANDOS VÁLIDOS]:");
        System.out.println();
        System.out.println("   - Modo completo: ");
        System.out.println("      java -jar nome_programa.jar -r X -di DD-MM-AAAA -df DD-MM-AAAA -di1 DD-MM-AAAA\n" +
                "            -df1 DD-MM-AAAA -di2 DD-MM-AAAA -df2 DD-MM-AAAA -T DD-MM-AAAA\n" +
                "            registoTotaisCovid.csv registoAcumulados.csv matrizTransicao.txt ficheiroDeSaida.txt");
        System.out.println();
        System.out.println("   - Modo sem previsões: ");
        System.out.println("      java -jar nome_programa.jar -r X -di DD-MM-AAAA -df DD-MM-AAAA -di1 DD-MM-AAAA\n" +
                "            -df1 DD-MM-AAAA -di2 DD-MM-AAAA -df2 DD-MM-AAAA registoAcumulados.csv ficheiroDesaida.txt\n");
        System.out.println();
        System.out.println("   - Modo de apenas previsões: ");
        System.out.println("      java -jar nome_programa.jar -T DD-MM-AAAA registoTotaisCovid.csv matrizTransicao.txt ficheiroDeSaida.txt");

    }

    /**
     * Verifica a ordem dos argumentos do modo não interativo
     * @param modoAnalise int
     * @param validacao array String
     * @return boolean
     */
    public static boolean verificarOrdemArgumentosNoModo(int modoAnalise, String[] validacao)
    {

        boolean verifica = false;

        switch (modoAnalise)
        {
            case MODO_TOTAL - NUMERO_FICHEIROS_MODO_TOTAL:
                verifica = verificarOrdemArgumentosEntrada(validacao, ORDEM_ARGS_ENTRADA_MODO_TOTAL);
                break;

            case MODO_SEM_PREVISOES - NUMERO_FICHEIROS_MODO_SEM_PREVISOES:
                verifica = verificarOrdemArgumentosEntrada(validacao, ORDEM_ARGS_ENTRADA_MODO_SEM_PREVISAO);
                break;

            case MODO_APENAS_PREVISOES - NUMERO_FICHEIROS_MODO_PREVISOES:
                verifica = verificarOrdemArgumentosEntrada(validacao, ORDEM_ARGS_ENTRADA_MODO_SO_PREVISAO);
                break;

            default:
                System.out.println("Não existe nenhum modo com essa especificação");
        }
        return verifica;
    }

    /**
     * Verifica os argumentos de entrada do modo não interativo
     * @param validacao array String
     * @param argumentos array String
     * @return
     */
    public static boolean verificarOrdemArgumentosEntrada(String[] validacao, String[] argumentos)
    {
        boolean flag = true;
        int i = 0;
        int j = 0;
        int numeroArgs = validacao.length;
        while (i < numeroArgs && flag && j < argumentos.length)
        {
            if (!validacao[i].equalsIgnoreCase(argumentos[j]))
            {
                flag = false;
            }
            i += 2;
            j++;
        }
        return flag;
    }

    /**
     * Executa a aplicação no modo não interativo
     * @param inputs array String
     * @return int
     * @throws FileNotFoundException
     */
    public static int executarModoNaoInterativo(String[] inputs) throws FileNotFoundException
    {

        // Inicializar estruturas de dados
        int status = 0;

        boolean inputValido = true;
        String[] arrDatasDadosAcumulados = new String[MAX_DIAS];
        String[] arrDatasDadosTotais = new String[MAX_DIAS];
        int[][] matDadosAcumulados = new int[MAX_DIAS][NUMERO_PARAMETROS];
        int[][] matDadosTotais = new int[MAX_DIAS][NUMERO_PARAMETROS];
        double[][] markov = new double[NUMERO_PARAMETROS][NUMERO_PARAMETROS];
        int resolucao = -1;

        String diaInicial = "", diaFinal = "";
        String diaInicialPeriodo1 = "", diaFinalPeriodo1 = "";
        String diaInicialPeriodo2 = "", diaFinalPeriodo2 = "";
        String diaPrevisao = "";
        String ficheiroDadosAcumulados = "", ficheiroDadosTotais = "";
        String ficheiroMatrizTransicoes = "";
        String ficheiroDeSaida = "";

        // Validar argumentos de input e atribuir variaveis
        int codigoDeErro = 0;
        int numeroOpcoes = obterNumeroOpcoes(inputs, MODO_TOTAL, MODO_SEM_PREVISOES, MODO_APENAS_PREVISOES);
        if (numeroOpcoes == -1)
        {
            inputValido = false;
            codigoDeErro = -4;
        }
        else
        {
            boolean ordemValida = verificarOrdemArgumentosNoModo(numeroOpcoes, inputs);
            if (!ordemValida)
            {
                inputValido = false;
                codigoDeErro = -3;
            }
        }

        int k = 0;
        while (inputValido && (k < numeroOpcoes))
        {
            switch (inputs[k])
            {
                case "-r":
                    try
                    {
                        resolucao = Integer.parseInt(inputs[k + 1]);

                    } catch (NumberFormatException e)
                    {
                        inputValido = false;
                        codigoDeErro = -1;
                    }
                    break;

                case "-di":
                    diaInicial = inputs[k + 1];
                    codigoDeErro = validarData(diaInicial);
                    if (codigoDeErro == -1)
                    {
                        inputValido = false;
                    }
                    break;

                case "-df":
                    diaFinal = inputs[k + 1];
                    codigoDeErro = validarData(diaFinal);
                    if (codigoDeErro == -1)
                    {
                        inputValido = false;
                    }
                    else
                    {
                        String periodo = formatarData(diaInicial) + "/" + formatarData(diaFinal);
                        if (!validarPeriodo(periodo))
                        {
                            codigoDeErro = -5;
                            inputValido = false;
                        }
                    }
                    break;

                case "-di1":
                    diaInicialPeriodo1 = inputs[k + 1];
                    codigoDeErro = validarData(diaInicialPeriodo1);
                    if (codigoDeErro == -1)
                    {
                        inputValido = false;
                    }

                    break;

                case "-df1":
                    diaFinalPeriodo1 = inputs[k + 1];
                    codigoDeErro = validarData(diaFinalPeriodo1);
                    if (codigoDeErro == -1)
                    {
                        inputValido = false;
                    }
                    else
                    {
                        String periodo = formatarData(diaInicialPeriodo1) + "/" + formatarData(diaFinalPeriodo1);
                        if (!validarPeriodo(periodo))
                        {
                            codigoDeErro = -5;
                            inputValido = false;
                        }
                    }
                    break;

                case "-di2":
                    diaInicialPeriodo2 = inputs[k + 1];
                    codigoDeErro = validarData(diaInicialPeriodo2);
                    if (codigoDeErro == -1)
                    {
                        inputValido = false;
                    }
                    break;

                case "-df2":
                    diaFinalPeriodo2 = inputs[k + 1];
                    codigoDeErro = validarData(diaFinalPeriodo2);
                    if (codigoDeErro == -1)
                    {
                        inputValido = false;
                    }
                    else
                    {
                        String periodo = formatarData(diaInicialPeriodo2) + "/" + formatarData(diaFinalPeriodo2);
                        if (!validarPeriodo(periodo))
                        {
                            codigoDeErro = -5;
                            inputValido = false;
                        }
                    }
                    break;

                case "-T":
                    diaPrevisao = inputs[k + 1];
                    codigoDeErro = validarData(diaPrevisao);
                    if (codigoDeErro == -1)
                    {
                        inputValido = false;
                    }
                    break;

                default:
                    inputValido = false;
                    codigoDeErro = -2; // 2: argumento não existe
                    break;
            }

            k += 2;

        }

        if (inputValido)
        {
            int numeroDiasAnalise;
            String periodo, periodo1, periodo2;
            // Executar modos
            switch (inputs.length)
            {
                case MODO_TOTAL:

                    // Ler nomes dos ficheiros
                    ficheiroDadosTotais = inputs[k];
                    ficheiroDadosAcumulados = inputs[k + 1];
                    ficheiroMatrizTransicoes = inputs[k + 2];
                    ficheiroDeSaida = inputs[k + 3];

                    // Abrir ficheiro de output
                    pw = new PrintWriter(new File(ficheiroDeSaida));

                    // Ler ficheiro de dados acumulados e totais
                    lerDadosDoFicheiro(arrDatasDadosAcumulados, matDadosAcumulados, ANALISE_ACUMULADOS, ficheiroDadosAcumulados);
                    lerDadosDoFicheiro(arrDatasDadosTotais, matDadosTotais, ANALISE_TOTAIS, ficheiroDadosTotais);

                    // Formatar
                    periodo = formatarData(diaInicial) + "/" + formatarData(diaFinal);
                    periodo1 = formatarData(diaInicialPeriodo1) + "/" + formatarData(diaFinalPeriodo1);
                    periodo2 = formatarData(diaInicialPeriodo2) + "/" + formatarData(diaFinalPeriodo2);

                    // Executar analise diaria
                    analiseDados(periodo, resolucao, arrDatasDadosAcumulados, matDadosAcumulados, ANALISE_ACUMULADOS, MODO_NAO_INTERATIVO, ficheiroDeSaida);
                    analiseDados(periodo, resolucao, arrDatasDadosTotais, matDadosTotais, ANALISE_TOTAIS, MODO_NAO_INTERATIVO, ficheiroDeSaida);

                    // Executar analise comparativa
                    analiseComparativa(periodo1, periodo2, arrDatasDadosAcumulados, matDadosAcumulados, ANALISE_ACUMULADOS, MODO_NAO_INTERATIVO, ficheiroDeSaida);
                    analiseComparativa(periodo1, periodo2, arrDatasDadosTotais, matDadosTotais, ANALISE_TOTAIS, MODO_NAO_INTERATIVO, ficheiroDeSaida);

                    // Executar analise de previsoes
                    markov = lerDadosDoFicheiroMarkov(markov, ficheiroMatrizTransicoes);
                    lerDadosDoFicheiro(arrDatasDadosTotais, matDadosTotais, ANALISE_TOTAIS, ficheiroDadosTotais);
                    analisePrevisaoMarkov(markov, arrDatasDadosTotais, matDadosTotais, diaPrevisao, MODO_NAO_INTERATIVO, ficheiroDeSaida);

                    break;

                case MODO_SEM_PREVISOES:

                    // Ler nomes dos ficheiros
                    ficheiroDadosAcumulados = inputs[k];
                    ficheiroDeSaida = inputs[k + 1];

                    // Abrir ficheiro de saida
                    pw = new PrintWriter(new File(ficheiroDeSaida));

                    // Ler ficheiro de dados
                    lerDadosDoFicheiro(arrDatasDadosAcumulados, matDadosAcumulados, ANALISE_ACUMULADOS, ficheiroDadosAcumulados);

                    // Formatar
                    periodo = formatarData(diaInicial) + "/" + formatarData(diaFinal);
                    periodo1 = formatarData(diaInicialPeriodo1) + "/" + formatarData(diaFinalPeriodo1);
                    periodo2 = formatarData(diaInicialPeriodo2) + "/" + formatarData(diaFinalPeriodo2);

                    // Executar analise diaria para dados acumulados
                    analiseDados(periodo, resolucao, arrDatasDadosAcumulados, matDadosAcumulados, ANALISE_ACUMULADOS, MODO_NAO_INTERATIVO, ficheiroDeSaida);

                    // Executar analise comparativa para dados acumulados
                    analiseComparativa(periodo1, periodo2, arrDatasDadosAcumulados, matDadosAcumulados, ANALISE_ACUMULADOS, MODO_NAO_INTERATIVO, ficheiroDeSaida);

                    break;

                case MODO_APENAS_PREVISOES:

                    diaPrevisao = inputs[1];
                    ficheiroDadosTotais = inputs[2];
                    ficheiroMatrizTransicoes = inputs[3];
                    ficheiroDeSaida = inputs[4];

                    // Abrir ficheiro de dados
                    pw = new PrintWriter(new File(ficheiroDeSaida));

                    // Ler dados da matriz de transições e dados totais
                    markov = lerDadosDoFicheiroMarkov(markov, ficheiroMatrizTransicoes);
                    lerDadosDoFicheiro(arrDatasDadosTotais, matDadosTotais, ANALISE_TOTAIS, ficheiroDadosTotais);
                    // Executar analise de previsoes
                    analisePrevisaoMarkov(markov, arrDatasDadosTotais, matDadosTotais, diaPrevisao, MODO_NAO_INTERATIVO, ficheiroDeSaida);

                    break;

            }
            // Close file
            try
            {
                pw.close();
            } catch (NullPointerException e)
            {
            }
        }
        else
        {
            switch (codigoDeErro)
            {
                case -1:
                    System.out.printf("[ERRO]: Parametro %s invalido.\n", inputs[k - 2]);
                    break;
                case -2:
                    System.out.printf("[ERRO]: Parametro %s nao existe. Execute o comando $java -jar nome_do_programa.jar --help para ver quais os parâmetros válidos.\n", inputs[k - 2]);
                    break;

                case -3:
                    System.out.println("[ERRO]: Os argumentos fornecidos e/ou a sua ordem não estão correctos.");
                    System.out.println(" - Execute o comando $java -jar nome_do_programa.jar --help para ver qual a ordem correcta dos parâmetros de entrada.");
                    break;

                case -4:
                    System.out.println("[ERRO]: Numero de argumentos invalido.");
                    System.out.println(" - Execute o comando $java -jar nome_do_programa.jar --help para ver quais os argumentos necessários para cada modo de funcionamento.");
                    break;

                case -5:
                    System.out.printf("[ERRO]: A data do paramatro %s não pode ser posterior à data inicial.\n", inputs[k - 2]);
                    break;

                default:
                    System.out.println("[ERRO]: Codigo de erro inválido.");
                    break;
            }
        }

        return status;

    }

    /**
     * Valida o formato da data inserido
     * @param data String
     * @return int
     * (0 - válido,
     * -1 - inválido)
     */
    public static int validarData(String data)
    {
        int status = 0;
        try
        {
            LocalDate.parse(data, DateTimeFormatter.ofPattern("dd-MM-uuuu"));
        } catch (DateTimeParseException e)
        {
            status = -1;
        }
        return status;
    }

    public static boolean validarPeriodo(String periodo)
    {
        String[] diDf = periodo.split("/");
        LocalDate diaInicial = LocalDate.parse(diDf[0]);
        LocalDate diaFinal = LocalDate.parse(diDf[1]);
        return diaInicial.isBefore(diaFinal);
    }

    public static int obterNumeroOpcoes(String[] opts, int nOptsTotal, int nOptsSemPrevisoes, int nOptsSoPrevisoes)
    {
        int numeroInputs = opts.length;
        int nOpts = -1;

        if (numeroInputs == nOptsTotal)
        {
            nOpts = nOptsTotal - NUMERO_FICHEIROS_MODO_TOTAL;
        }
        else if (numeroInputs == nOptsSemPrevisoes)
        {
            nOpts = nOptsSemPrevisoes - NUMERO_FICHEIROS_MODO_SEM_PREVISOES;
        }
        else if (numeroInputs == nOptsSoPrevisoes)
        {
            nOpts = nOptsSoPrevisoes - NUMERO_FICHEIROS_MODO_PREVISOES;
        }

        return nOpts;

    }
    /**
     * Dadas duas matrizes, calcula a diferença entre os indicies na mesma posição
     * @param matriz1 matriz int
     * @param matriz2 matriz int
     * @return matriz de diferenças
     */
    public static int[][] subtrairMatrizes(int[][] matriz1, int[][] matriz2)
    {
        int nLinhas = matriz1.length;
        int nColunas = matriz1[0].length;
        int[][] matriz = new int[nLinhas][nColunas];

        for (int linha = 0; linha < nLinhas; linha++)
        {
            for (int coluna = 0; coluna < nColunas; coluna++)
            {
                matriz[linha][coluna] = matriz2[linha][coluna] - matriz1[linha][coluna];
            }

        }

        return matriz;

    }

    /**
     * Calcula a diferença entre duas dadas matrizes no mesmo índice
     * @param m1 matriz double
     * @param m2 matriz double
     * @return matriz double
     */
    public static double[][] calcularDiferencaMatrizesAeB(double m1[][], double m2[][])
    {
        int linhas, colunas;
        linhas = m1.length;
        colunas = m2[0].length;
        double diferencaDuasMatrizes[][] = new double[linhas][colunas];
        for (int i = 0; i < linhas; i++)
        {
            for (int j = 0; j < colunas; j++)
            {
                diferencaDuasMatrizes[i][j] = m1[i][j] - m2[i][j];
            }
        }
        return diferencaDuasMatrizes;
    }

    /**
     * Analisa os dados consoante o periodo dado e o tipo de análise desejada (Diária, Semana, Mensal)
     * @param periodo String
     * @param resolucao int
     * @param dias array String
     * @param dados matriz int
     * @param tipoAnalise int
     * @param modoExecucao int
     * @param ficheiroDeSaida String
     * @return int (0 - Bem sucedido, -1 - Falhou)
     * @throws FileNotFoundException
     */
    public static int analiseDados(String periodo, int resolucao, String[] dias, int[][] dados, int tipoAnalise, int modoExecucao, String ficheiroDeSaida) throws FileNotFoundException
    {
        int status = 0;
        int[] indicesDoPeriodo;
        int[] colunas;
        int[][] balanco;
        int nLinhas = 0;
        String[] arrayDias;
        int[][] matrizDados;
        boolean existemDadosPrimeiroDia = true;
        boolean executarAnalise = true;
        int escolhaUtilizador = 0;

        if (tipoAnalise == ANALISE_ACUMULADOS)
        {
            // Obter dia anterior
            String[] diDfPeriodo = periodo.split("/");
            existemDadosPrimeiroDia = recalcularPeriodoAnaliseNovosCasos(diDfPeriodo, dias, resolucao);
            String novoPeriodo = diDfPeriodo[0] + "/" + diDfPeriodo[1];

            nLinhas = calcularNumeroDeLinhas(novoPeriodo, dias, resolucao);
            if (nLinhas > 1)
            {

                arrayDias = new String[nLinhas];
                matrizDados = new int[nLinhas][NUMERO_PARAMETROS];

                // Obter indice do dia inicial e final do periodo
                indicesDoPeriodo = obterIndice(novoPeriodo, dias);

                status = obterDadosPeriodo(novoPeriodo, dados, dias, matrizDados, arrayDias, indicesDoPeriodo, resolucao);
                balanco = calcularBalanco(matrizDados);

                colunas = obterColunasDesejadas(modoExecucao);


                if (modoExecucao == MODO_INTERATIVO)
                {
                    // Visualizar dados
                    visualizarDados(arrayDias, balanco, colunas, resolucao, tipoAnalise, existemDadosPrimeiroDia);

                    escolhaUtilizador = analisouOK();
                    if (escolhaUtilizador == 1)
                    {
                        // Pedir nome do ficheiro ao utilizador
                        String ficheiroOutput = obterNomeFicheiroUtilizador();
                        escreverCsvResultadosAnaliseDados(arrayDias, balanco, colunas, resolucao, tipoAnalise, existemDadosPrimeiroDia, ficheiroOutput);
                    }
                }
                else
                {
                    escreverTxtResultadosAnaliseDados(arrayDias, balanco, colunas, resolucao, tipoAnalise, existemDadosPrimeiroDia);
                }

            }
            else
            {

                if (modoExecucao == MODO_INTERATIVO)
                {
                    imprimirCabecalhoAnaliseDados(novoPeriodo, resolucao, ANALISE_ACUMULADOS, MODO_INTERATIVO);
                    System.out.println(" Não existem dados para o período solicitado");
                    System.out.println();
                }
                else
                {
                    imprimirCabecalhoAnaliseDados(novoPeriodo, resolucao, ANALISE_ACUMULADOS, MODO_NAO_INTERATIVO);
                    pw.println(" Não existem dados para o período solicitado");
                    pw.println();
                }
            }
        }
        else
        {

            nLinhas = calcularNumeroDeLinhas(periodo, dias, RESOLUCAO_DIARIA);
            arrayDias = new String[nLinhas];
            matrizDados = new int[nLinhas][NUMERO_PARAMETROS];

            if (nLinhas > 0)
            {
                // Obter indice do dia inicial e final do periodo
                indicesDoPeriodo = obterIndice(periodo, dias);

                // Obter array de datas e matriz de dados relativo ao periodo
                status = obterDadosPeriodo(periodo, dados, dias, matrizDados, arrayDias, indicesDoPeriodo, RESOLUCAO_DIARIA);

                colunas = obterColunasDesejadas(modoExecucao);

                // TODO
                //  NESTE MODO APENAS A RESOLUÇÃO DIARIA PODE SER FORNECIDA
                if (modoExecucao == MODO_INTERATIVO)
                {
                    // Visualizar dados com a resolução pretendida
                    //visualizarDados(arrayDias, matrizDados, colunas, resolucao, tipoAnalise, true);
                    visualizarDados(arrayDias, matrizDados, colunas, RESOLUCAO_DIARIA, tipoAnalise, true);

                    escolhaUtilizador = analisouOK();
                    if (escolhaUtilizador == 1)
                    {
                        // Pedir nome do ficheiro ao utilizador
                        String ficheiroOutput = obterNomeFicheiroUtilizador();
                        //escreverCsvResultadosAnaliseDados(arrayDias, matrizDados, colunas, resolucao, tipoAnalise, true, ficheiroOutput);
                        escreverCsvResultadosAnaliseDados(arrayDias, matrizDados, colunas, RESOLUCAO_DIARIA, tipoAnalise, true, ficheiroOutput);
                    }
                }
                else
                {
                    //escreverTxtResultadosAnaliseDados(arrayDias, matrizDados, colunas, resolucao, tipoAnalise, true);
                    escreverTxtResultadosAnaliseDados(arrayDias, matrizDados, colunas, RESOLUCAO_DIARIA, tipoAnalise, true);

                }

            }
            else
            {
                if (modoExecucao == MODO_INTERATIVO)
                {
                    imprimirCabecalhoAnaliseDados(periodo, resolucao, ANALISE_TOTAIS, MODO_INTERATIVO);
                    System.out.println(" Não existem dados para o período solicitado");
                    System.out.println();
                }
                else
                {
                    imprimirCabecalhoAnaliseDados(periodo, resolucao, ANALISE_TOTAIS, MODO_NAO_INTERATIVO);
                    pw.println(" Não existem dados para o período solicitado");
                    pw.println();

                }
            }

        }

        return status;

    }

    /**
     * Lê o nome que o utilizador deseja para o ficheiro de saída
     * @return String
     */
    public static String obterNomeFicheiroUtilizador()
    {
        sc.nextLine();
        System.out.println("@ Por favor insira nome para o ficheiro de saída.");
        String ficheiroSaida = sc.next();
        if (!ficheiroSaida.toLowerCase().endsWith(".csv"))
        {
            ficheiroSaida = ficheiroSaida + ".csv";
        }
        return ficheiroSaida;
    }

    /**
     * Devolve quais as colunas que o utilizador deseja obter na análise
     * @param modoExecucao int
     * @return array int
     */
    public static int[] obterColunasDesejadas(int modoExecucao)
    {
        int[] colunas = new int[NUMERO_PARAMETROS - 1];

        if (modoExecucao == MODO_INTERATIVO)
        {
            // Pedir colunas a visualizar
            escolhaColunasUtilizador(colunas);
        }
        else
        {
            for (int col = 0; col < colunas.length; col++)
            {
                colunas[col] = 1;
            }
        }

        return colunas;
    }

    /**
     * Lê quais as colunas o utilizador escolheu
     * @param colunas array int
     */
    public static void escolhaColunasUtilizador(int[] colunas)
    {
        // Limpar buffer
        int maxCol = colunas.length;
        int c = 0;
        sc.nextLine();
        System.out.println("# Indique as colunas que pretende visualizar.");
        System.out.println("  [0].Todas [1].Num.Inf. [2].Num.Hosp [3].Num.UCI [4].Num.Mortes [-1].Terminar");
        int escolha;
        boolean sair = false;
        while (!sair && c < maxCol)
        {
            // Limpar buffer
            escolha = obterEscolhaDoUtilizador();
            switch (escolha)
            {
                case 1:
                case 2:
                case 3:
                case 4:
                    colunas[escolha - 1] = 1;
                    c++;
                    break;
                case 0:
                    for (int i = 0; i < maxCol; i++)
                    {
                        colunas[i] = 1;
                    }
                    sair = true;
                    break;
                case -1:
                    sair = true;
                    break;
                default:
                    // Limpar buffer
                    System.out.println("! Coluna invalida. Introduza novamente a coluna [0-4].");
                    break;
            }
        }
    }

    /**
     * Recalcula o periodo da analise de novos casos
     * @param diDf array String
     * @param arrayDatas array String
     * @param resolucao int
     * @return
     */
    public static boolean recalcularPeriodoAnaliseNovosCasos(String[] diDf, String[] arrayDatas, int resolucao)
    {
        boolean diaInicialExiste = true;
        String novoDiaInicial = "";
        int indiceNovoDiaInicial = -1;

        switch (resolucao)
        {
            case RESOLUCAO_DIARIA:
                novoDiaInicial = LocalDate.parse(diDf[0]).plusDays(-1).toString();
                indiceNovoDiaInicial = obterIndiceDoDia(novoDiaInicial, arrayDatas);
                // Se o dia anterior existir então há que substituí-lo no array do dia inicial e final do período
                if (indiceNovoDiaInicial != -1)
                {
                    diDf[0] = novoDiaInicial;
                }
                else
                {
                    diaInicialExiste = false;

                }
                break;

            case RESOLUCAO_SEMANAL:
                // Na resolução semanal apenas interessa obter o dia anterior se estivermos a uma segunda-feira
                int dia = obterDiaSemana(diDf[0]);
                if (dia == 1)
                {
                    novoDiaInicial = LocalDate.parse(diDf[0]).plusDays(-1).toString();
                    indiceNovoDiaInicial = obterIndiceDoDia(novoDiaInicial, arrayDatas);
                    if (indiceNovoDiaInicial != -1)
                    {
                        diDf[0] = novoDiaInicial;
                    }
                }

                break;

            case RESOLUCAO_MENSAL:


                // Na resolução mensal apenas interessa obter o dia anterior se estivermos no dia 1 do mês
                if (diDf[0].split("-")[2].equals("01"))
                {
                    novoDiaInicial = LocalDate.parse(diDf[0]).plusDays(-1).toString();
                    indiceNovoDiaInicial = obterIndiceDoDia(novoDiaInicial, arrayDatas);
                    if (indiceNovoDiaInicial != -1)
                    {
                        diDf[0] = novoDiaInicial;
                    }
                }
                break;

        }


        return diaInicialExiste;
    }

    /**
     * Calcula o número de linhas que a matriz deverá ter consoante o tipo de análise desejada
     * @param periodo String
     * @param arrayDatas array String
     * @param resolucao int
     * @return int
     */
    public static int calcularNumeroDeLinhas(String periodo, String[] arrayDatas, int resolucao)
    {
        int nLinhas = 0;
        int[] indicesDoPeriodo = obterIndice(periodo, arrayDatas);
        if (calcularNumeroDiasDoPeriodo(periodo) == (indicesDoPeriodo[1] - indicesDoPeriodo[0] + 1))
        {
            switch (resolucao)
            {
                case RESOLUCAO_DIARIA:
                    nLinhas = indicesDoPeriodo[1] - indicesDoPeriodo[0] + 1;
                    break;
                case RESOLUCAO_SEMANAL:

                    for (int i = indicesDoPeriodo[0]; i <= indicesDoPeriodo[1]; i++)
                    {
                        int dia = obterDiaSemana(arrayDatas[i]);
                        if (dia == 7)
                        {
                            nLinhas++;
                        }
                    }

                    break;
                case RESOLUCAO_MENSAL:
                    String ultimoDiaPeriodo = LocalDate.parse(arrayDatas[indicesDoPeriodo[1]]).with(TemporalAdjusters.lastDayOfMonth()).toString();

                    for (int i = indicesDoPeriodo[0]; i <= indicesDoPeriodo[1]; i++)
                    {
                        if (arrayDatas[i].split("-")[2].equals("01") && i != 0)
                        {
                            nLinhas++;
                        }
                    }

                    if (arrayDatas[indicesDoPeriodo[1]].compareTo(ultimoDiaPeriodo) == 0)
                    {
                        nLinhas++;
                    }

                    break;
            }
        }
        return nLinhas;

    }

    /**
     * Escreve para um ficheiro csv o resultado da análise dos dados
     * @param arrayDatas array String
     * @param matrizDados matriz Dados
     * @param colunasDesejadas array int
     * @param resolucao int
     * @param tipoAnalise int
     * @param haDadosPrimeiraLinha boolean
     * @param ficheiroDeSaida String
     * @return int (0 - Bem sucedido, -1 - Falhou)
     * @throws FileNotFoundException
     */
    public static int escreverCsvResultadosAnaliseDados(String[] arrayDatas, int[][] matrizDados, int[] colunasDesejadas, int resolucao, int tipoAnalise, Boolean haDadosPrimeiraLinha, String ficheiroDeSaida) throws FileNotFoundException
    {
        final String[] CABECALHO_OUTPUT_FICHEIRO_ACUMULADOS = {"data", "novos_infetados", "novos_hospitalizados", "novos_internadoUCI", "novas_mortes"};
        final String[] CABECALHO_OUTPUT_FICHEIRO_TOTAIS = {"data", "total_infetados", "total_hospitalizados", "total_internadosUCI", "total_obitos"};
        int status = 0;

        int nLinhas = matrizDados.length;
        int nColunas = CABECALHO_OUTPUT_FICHEIRO_ACUMULADOS.length;
        String periodo = "";

        PrintWriter saida = new PrintWriter(new File(ficheiroDeSaida));

        //
        if (tipoAnalise == ANALISE_ACUMULADOS)
        {
            saida.printf("%s,", CABECALHO_OUTPUT_FICHEIRO_ACUMULADOS[0]);
            // Escrever cabeçalho novos casos
            for (int i = 1; i < nColunas; i++)
            {
                if (colunasDesejadas[i - 1] == 1)
                {
                    saida.printf("%s,", CABECALHO_OUTPUT_FICHEIRO_ACUMULADOS[i]);
                }
            }
            saida.println();

        }
        else
        {
            // Escrever cabeçalho casos totais
            saida.printf("%s,", CABECALHO_OUTPUT_FICHEIRO_TOTAIS[0]);
            for (int i = 1; i < nColunas; i++)
            {
                //saida.printf("%s,", CABECALHO_OUTPUT_FICHEIRO_TOTAIS[0]);
                if (colunasDesejadas[i - 1] == 1)
                {
                    saida.printf("%s,", CABECALHO_OUTPUT_FICHEIRO_TOTAIS[i]);
                }
            }
            saida.println();

        }

        // Escrever resultados
        // Imprimir primeira linha
        if (resolucao == RESOLUCAO_DIARIA && !haDadosPrimeiraLinha)
        {
            saida.printf("%s,", arrayDatas[0]);

            for (int coluna = 1; coluna < NUMERO_PARAMETROS; coluna++)
            {
                if (colunasDesejadas[coluna - 1] == 1)
                {
                    saida.printf("%s,", "sem dados");
                }
            }
            saida.println();
        }


        for (int linha = 0; linha < nLinhas; linha++)
        {
            // Imprimir primeira coluna (tem formato diferente dependendo se e analise diaria ou semanal/mensal)
            if (resolucao == RESOLUCAO_DIARIA)
            {
                if (tipoAnalise == ANALISE_ACUMULADOS)
                {
                    saida.printf("%s,", arrayDatas[linha + 1]);
                }
                else
                {
                    saida.printf("%s,", arrayDatas[linha]);
                }
            }
            else
            {
                saida.printf("%s a %s,", arrayDatas[linha], arrayDatas[linha + 1]);
            }
            for (int coluna = 1; coluna < NUMERO_PARAMETROS; coluna++)
            {
                if (colunasDesejadas[coluna - 1] == 1)
                {
                    saida.printf("%d,", matrizDados[linha][coluna]);
                }

            }
            saida.println();
        }

        // Fechar ficheiros de saida
        saida.close();

        return status;

    }

    /**
     * Escreve num ficheiro ".txt" o resultado da analise dos dados
     * @param arrayDatas array String
     * @param matrizDados matriz int
     * @param colunasDesejadas array int
     * @param resolucao int
     * @param tipoAnalise int
     * @param haDadosPrimeiraLinha boolean
     * @return int (0 - Bem Sucedido, -1 - Falhou)
     */
    public static int escreverTxtResultadosAnaliseDados(String[] arrayDatas, int[][] matrizDados, int[] colunasDesejadas, int resolucao, int tipoAnalise, Boolean haDadosPrimeiraLinha)
    {

        //PrintWriter saida = new PrintWriter(new File(ficheiroDeSaida));
        int status = 0;
        int nLinhas = matrizDados.length;
        String periodo = "";

        // constante usada para saltar a primeira linha das datas caso estejamos no tipo de analise de novos casos
        // uma vez que a matriz de dados tem menos uma linha que o array de datas
        int c = 0;
        if (tipoAnalise == ANALISE_ACUMULADOS && haDadosPrimeiraLinha)
        {
            c = 1;
        }

        switch (resolucao)
        {
            case RESOLUCAO_DIARIA:
                // o primeiro dia do periodo vem em função de c porque se for uma analise de novos casos o dia dado é o anterior
                periodo = arrayDatas[c] + "/" + arrayDatas[arrayDatas.length - 1];
                break;
            case RESOLUCAO_SEMANAL:
            case RESOLUCAO_MENSAL:
                periodo = arrayDatas[0] + "/" + arrayDatas[arrayDatas.length - 1];
                break;
        }

        // Imprimir cabecalho
        pw.println("+-----------------------------------------------------------------------------+");
        pw.println("|                                                                             |");
        switch (tipoAnalise)
        {
            case ANALISE_ACUMULADOS:
                pw.println("|                        VISUALIZACAO DE NOVOS CASOS                          |");
                break;
            case ANALISE_TOTAIS:
                pw.println("|                      VISUALIZACAO DE CASOS TOTAIS                          |");
                break;
        }
        pw.println("|                                                                             |");
        pw.printf("| Período em análise: %-56s|\n", periodo);
        pw.printf("| Resolução: %-65s|\n", resolucao);
        pw.println("|                                                                             |");
        pw.println("+-----------------------------------------------------------------------------+");


        // Imprimir cabeçalho dependendo das colunas pretendidas pelo utilizador
        pw.printf(" %-27s", "Data");
        String[] cabecalho = {"Num_Inf.", "Num_Hosp.", "Num_UCI", "Num_Mortes"};
        for (int coluna = 1; coluna < NUMERO_PARAMETROS; coluna++)
        {
            if (colunasDesejadas[coluna - 1] == 1)
            {
                pw.printf("%-12s", cabecalho[coluna - 1]);
            }
        }
        pw.println();

        // Imprimir dados
        if (resolucao == RESOLUCAO_DIARIA && !haDadosPrimeiraLinha)
        {
            pw.printf(" %-27s", arrayDatas[0]);

            for (int coluna = 1; coluna < NUMERO_PARAMETROS; coluna++)
            {
                if (colunasDesejadas[coluna - 1] == 1)
                {
                    pw.printf("%-12s", "sem dados");
                }
            }
            pw.println();
        }

        for (int linha = 0; linha < nLinhas; linha++)
        {
            // Imprimir primeira coluna (tem formato diferente dependendo se e analise diaria ou semanal/mensal)
            if (resolucao == RESOLUCAO_DIARIA)
            {
                if (tipoAnalise == ANALISE_ACUMULADOS)
                {
                    pw.printf(" %-27s", arrayDatas[linha + 1]);
                }
                else
                {
                    pw.printf(" %-27s", arrayDatas[linha]);
                }
            }
            else
            {
                pw.printf(" %-10s a %-14s", arrayDatas[linha], arrayDatas[linha + 1]);
            }

            for (int coluna = 1; coluna < NUMERO_PARAMETROS; coluna++)
            {
                if (colunasDesejadas[coluna - 1] == 1)
                {
                    pw.printf("%-12d", matrizDados[linha][coluna]);
                }

            }
            pw.println();
        }
        // Linha em branco no final
        pw.println();

        // Fechar ficheiro de output
        //saida.close();

        return status;

    }

    /**
     * Troca ordem da data: 2020-01-05 para 05-01-2020 e vice-versa
     * @param data
     * @return
     */
    public static String formatarData(String data)
    {
        String[] arrayData;
        arrayData = data.split("-");
        return arrayData[2] + "-" + arrayData[1] + "-" + arrayData[0];
    }

    /**
     * Imprime o cabeçalho da analise de dados
     * @param periodo String
     * @param res String
     * @param analise int
     * @param modoExecucao int
     */
    public static void imprimirCabecalhoAnaliseDados(String periodo, int res, int analise, int modoExecucao)
    {
        String resolucao = "";
        switch (res)
        {
            case 0:
                resolucao = "DIÁRIA";
                break;
            case 1:
                resolucao = "SEMANAL";
                break;
            case 2:
                resolucao = "MENSAL";
                break;
        }

        switch (modoExecucao)
        {
            case MODO_INTERATIVO:
                // Imprimir cabecalho
                System.out.println("+-----------------------------------------------------------------------------+");
                System.out.println("|                                                                             |");
                switch (analise)
                {
                    case ANALISE_ACUMULADOS:
                        System.out.println("|                        VISUALIZACAO DE NOVOS CASOS                          |");
                        break;
                    case ANALISE_TOTAIS:
                        System.out.println("|                      VISUALIZACAO DE CASOS TOTAIS                          |");
                        break;
                }
                System.out.println("|                                                                             |");
                System.out.printf("| Período em análise: %-56s|\n", periodo);
                System.out.printf("| Resolução: %-65s|\n", resolucao);
                System.out.println("|                                                                             |");
                System.out.println("+-----------------------------------------------------------------------------+");
                break;

            case MODO_NAO_INTERATIVO:
                // Imprimir cabecalho
                pw.println("+-----------------------------------------------------------------------------+");
                pw.println("|                                                                             |");
                switch (analise)
                {
                    case ANALISE_ACUMULADOS:
                        pw.println("|                        VISUALIZACAO DE NOVOS CASOS                          |");
                        break;
                    case ANALISE_TOTAIS:
                        pw.println("|                      VISUALIZACAO DE CASOS TOTAIS                         |");
                        break;
                }
                pw.println("|                                                                             |");
                pw.printf("| Período em análise: %-56s|\n", periodo);
                pw.printf("| Resolução: %-65s|\n", resolucao);
                pw.println("|                                                                             |");
                pw.println("+-----------------------------------------------------------------------------+");
                break;
        }

    }

    /**
     * Imprime o cabeçalho da analise comparativa
     * @param periodo1 String
     * @param periodo2 String
     * @param analise int
     * @param modoExecucao int
     */
    public static void imprimirCabecalhoAnaliseComparativa(String periodo1, String periodo2, int analise, int modoExecucao)
    {
        switch (modoExecucao)
        {
            case MODO_INTERATIVO:
                // Imprimir cabecalho
                System.out.println("+-----------------------------------------------------------------------------+");
                System.out.println("|                                                                             |");
                switch (analise)
                {
                    case ANALISE_ACUMULADOS:
                        System.out.println("|                     ANALISE COMPARATIVA DE NOVOS CASOS                      |");
                        break;
                    case ANALISE_TOTAIS:
                        System.out.println("|                      ANALISE COMPARATIVA DE CASOS TOTAIS                    |");
                        break;
                }
                System.out.println("|                                                                             |");
                System.out.printf("| Período 1: %-65s|\n", periodo1);
                System.out.printf("| Período 2: %-65s|\n", periodo2);
                System.out.println("|                                                                             |");
                System.out.println("+-----------------------------------------------------------------------------+");
                break;

            case MODO_NAO_INTERATIVO:
                // Imprimir cabecalho
                pw.println("+-----------------------------------------------------------------------------+");
                pw.println("|                                                                             |");
                switch (analise)
                {
                    case ANALISE_ACUMULADOS:
                        pw.println("|                     ANALISE COMPARATIVA DE NOVOS CASOS                      |");
                        break;
                    case ANALISE_TOTAIS:
                        pw.println("|                      ANALISE COMPARATIVA DE CASOS TOTAIS                    |");
                        break;
                }
                pw.println("|                                                                             |");
                pw.printf("| Período 1: %-65s|\n", periodo1);
                pw.printf("| Período 2: %-65s|\n", periodo2);
                pw.println("|                                                                             |");
                pw.println("+-----------------------------------------------------------------------------+");
                break;

        }

    }

    /**
     * Imprime os dados
     * @param arrayDatas array String
     * @param matrizDados matriz int
     * @param colunasDesejadas array int
     * @param resolucao int
     * @param tipoAnalise int
     * @param haDadosPrimeiraLinha boolean
     */
    public static void visualizarDados(String[] arrayDatas, int[][] matrizDados, int[] colunasDesejadas, int resolucao, int tipoAnalise, Boolean haDadosPrimeiraLinha)
    {
        int nLinhas = matrizDados.length;
        String periodo = "";

        // constante usada para saltar a primeira linha das datas caso estejamos no tipo de analise de novos casos
        // uma vez que a matriz de dados tem menos uma linha que o array de datas
        int c = 0;
        if (tipoAnalise == ANALISE_ACUMULADOS && haDadosPrimeiraLinha)
        {
            c = 1;
        }


        switch (resolucao)
        {
            case RESOLUCAO_DIARIA:
                // o primeiro dia do periodo vem em função de c porque se for uma analise de novos casos o dia dado é o anterior
                periodo = arrayDatas[c] + "/" + arrayDatas[arrayDatas.length - 1];
                imprimirCabecalhoAnaliseDados(periodo, RESOLUCAO_DIARIA, tipoAnalise, MODO_INTERATIVO);
                break;
            case RESOLUCAO_SEMANAL:
                periodo = arrayDatas[0] + "/" + arrayDatas[arrayDatas.length - 1];
                imprimirCabecalhoAnaliseDados(periodo, RESOLUCAO_SEMANAL, tipoAnalise, MODO_INTERATIVO);
                break;

            case RESOLUCAO_MENSAL:
                periodo = arrayDatas[0] + "/" + arrayDatas[arrayDatas.length - 1];
                imprimirCabecalhoAnaliseDados(periodo, RESOLUCAO_MENSAL, tipoAnalise, MODO_INTERATIVO);
                break;
        }

        // Imprimir cabeçalho dependendo das colunas pretendidas pelo utilizador
        System.out.printf(" %-27s", "Data");
        String[] cabecalho = {"Num_Inf.", "Num_Hosp.", "Num_UCI", "Num_Mortes"};
        for (int coluna = 1; coluna < NUMERO_PARAMETROS; coluna++)
        {
            if (colunasDesejadas[coluna - 1] == 1)
            {
                System.out.printf("%-12s", cabecalho[coluna - 1]);
            }
        }
        System.out.println();

        // Imprimir dados
        if (resolucao == RESOLUCAO_DIARIA && !haDadosPrimeiraLinha)
        {
            System.out.printf(" %-27s", arrayDatas[0]);

            for (int coluna = 1; coluna < NUMERO_PARAMETROS; coluna++)
            {
                if (colunasDesejadas[coluna - 1] == 1)
                {
                    System.out.printf("%-12s", "sem dados");
                }
            }
            System.out.println();
        }

        for (int linha = 0; linha < nLinhas; linha++)
        {
            // Imprimir primeira coluna (tem formato diferente dependendo se e analise diaria ou semanal/mensal)
            if (resolucao == RESOLUCAO_DIARIA)
            {
                if (tipoAnalise == ANALISE_ACUMULADOS)
                {
                    System.out.printf(" %-27s", arrayDatas[linha + 1]);
                }
                else
                {
                    System.out.printf(" %-27s", arrayDatas[linha]);
                }
            }
            else
            {
                System.out.printf(" %-10s a %-14s", arrayDatas[linha], arrayDatas[linha + 1]);
            }

            for (int coluna = 1; coluna < NUMERO_PARAMETROS; coluna++)
            {
                if (colunasDesejadas[coluna - 1] == 1)
                {
                    System.out.printf("%-12d", matrizDados[linha][coluna]);
                }

            }
            System.out.println();
        }
        // Linha em branco no final
        System.out.println();

    }

    /**
     * Faz a análise comparativa dos dados dentro de um dado periodo
     * @param periodo1 String
     * @param periodo2 String
     * @param dias array String
     * @param dados matriz int
     * @param tipoAnalise int
     * @param modoExecucao int
     * @param ficheiroDeSaida String
     * @return int (0- Bem sucedido, -1 - Falhou)
     * @throws FileNotFoundException
     */
    public static int analiseComparativa(String periodo1, String periodo2, String[] dias, int[][] dados, int tipoAnalise, int modoExecucao, String ficheiroDeSaida) throws FileNotFoundException
    {
        int status = 0;
        int[][] matrizDados1 = new int[0][], matrizDados2 = new int[0][], matrizDiferencas;
        double[] mediasPeriodo1, mediasPeriodo2, mediasDiferencas;
        double[] desviosPadraoPeriodo1, desviosPadraoPeriodo2, desviosPadraoDiferencas;

        String[] arrayDias1 = new String[0], arrayDias2 = new String[0];
        int[][] matrizDadosPeriodo1, matrizDadosPeriodo2;
        int[] indicesPeriodo1, indicesPeriodo2;

        int[] colunas;

        boolean existemDadosPrimeiroDia1 = true, existemDadosPrimeiroDia2 = true;
        boolean executarAnalise = true;

        // Calcular dias de cada periodo
        int numeroDiasPeriodo1 = calcularNumeroDeLinhas(periodo1, dias, RESOLUCAO_DIARIA);
        int numeroDiasPeriodo2 = calcularNumeroDeLinhas(periodo2, dias, RESOLUCAO_DIARIA);

        // Recalcular periodo de acordo com o periodo com menor numero de dias
        if (numeroDiasPeriodo1 < numeroDiasPeriodo2)
        {
            String[] didf = obterDiaInicialFinalDoPeriodo(periodo2);
            String diaInicialPeriodo2 = didf[0];

            periodo2 = calcularNovoPeriodo(diaInicialPeriodo2, numeroDiasPeriodo1);

        }
        else
        {
            if (numeroDiasPeriodo2 < numeroDiasPeriodo1)
            {
                String[] didf = obterDiaInicialFinalDoPeriodo(periodo1);
                String diaInicialPeriodo1 = didf[0];

                periodo1 = calcularNovoPeriodo(diaInicialPeriodo1, numeroDiasPeriodo2);
            }

        }

        switch (tipoAnalise)
        {
            case ANALISE_ACUMULADOS:
                // Obter dia anterior
                String[] diDfPeriodo1 = periodo1.split("/");
                String[] diDfPeriodo2 = periodo2.split("/");

                existemDadosPrimeiroDia1 = recalcularPeriodoAnaliseNovosCasos(diDfPeriodo1, dias, RESOLUCAO_DIARIA);
                existemDadosPrimeiroDia2 = recalcularPeriodoAnaliseNovosCasos(diDfPeriodo2, dias, RESOLUCAO_DIARIA);

                if (!existemDadosPrimeiroDia1 || !existemDadosPrimeiroDia2)
                {
                    diDfPeriodo1 = periodo1.split("/");
                    diDfPeriodo2 = periodo2.split("/");
                }

                String novoPeriodo1 = diDfPeriodo1[0] + "/" + diDfPeriodo1[1];
                String novoPeriodo2 = diDfPeriodo2[0] + "/" + diDfPeriodo2[1];

                // Obter indice do dia inicial e final do periodo
                indicesPeriodo1 = obterIndice(novoPeriodo1, dias);
                indicesPeriodo2 = obterIndice(novoPeriodo2, dias);

                numeroDiasPeriodo1 = indicesPeriodo1[1] - indicesPeriodo1[0] + 1;
                numeroDiasPeriodo2 = indicesPeriodo2[1] - indicesPeriodo2[0] + 1;

                if (numeroDiasPeriodo1 <= 1 || numeroDiasPeriodo2 <= 1)
                {
                    executarAnalise = false;
                }
                else
                {
                    arrayDias1 = new String[numeroDiasPeriodo1];
                    arrayDias2 = new String[numeroDiasPeriodo2];

                    matrizDadosPeriodo1 = new int[numeroDiasPeriodo1][NUMERO_PARAMETROS];
                    matrizDadosPeriodo2 = new int[numeroDiasPeriodo2][NUMERO_PARAMETROS];

                    obterDadosPeriodo(novoPeriodo1, dados, dias, matrizDadosPeriodo1, arrayDias1, indicesPeriodo1, RESOLUCAO_DIARIA);
                    obterDadosPeriodo(novoPeriodo2, dados, dias, matrizDadosPeriodo2, arrayDias2, indicesPeriodo2, RESOLUCAO_DIARIA);

                    // Calcular resumo dos periodos 1 e 2
                    matrizDados1 = calcularBalanco(matrizDadosPeriodo1);
                    matrizDados2 = calcularBalanco(matrizDadosPeriodo2);

                }

                break;

            case ANALISE_TOTAIS:

                //TODO
                // bug: numero de linhas tem de ser recalculado
                if (numeroDiasPeriodo1 != numeroDiasPeriodo2)
                {
                    numeroDiasPeriodo1 = calcularNumeroDiasDoPeriodo(periodo1);
                    numeroDiasPeriodo2 = calcularNumeroDiasDoPeriodo(periodo2);
                }

                arrayDias1 = new String[numeroDiasPeriodo1];
                arrayDias2 = new String[numeroDiasPeriodo2];

                matrizDados1 = new int[numeroDiasPeriodo1][NUMERO_PARAMETROS];
                matrizDados2 = new int[numeroDiasPeriodo2][NUMERO_PARAMETROS];

                indicesPeriodo1 = obterIndice(periodo1, dias);
                indicesPeriodo2 = obterIndice(periodo2, dias);

                obterDadosPeriodo(periodo1, dados, dias, matrizDados1, arrayDias1, indicesPeriodo1, RESOLUCAO_DIARIA);
                obterDadosPeriodo(periodo2, dados, dias, matrizDados2, arrayDias2, indicesPeriodo2, RESOLUCAO_DIARIA);

                if (numeroDiasPeriodo1 < 1 || numeroDiasPeriodo2 < 1)
                {
                    executarAnalise = false;
                }

                break;
            default:
                throw new IllegalStateException("Valor inesperado [tipo de analise comparativa]: " + tipoAnalise);
        }

        if (executarAnalise)
        {
            // Calcular medias dos periodos 1 e 2
            mediasPeriodo1 = calcularMediasDoPeriodo(matrizDados1);
            mediasPeriodo2 = calcularMediasDoPeriodo(matrizDados2);

            // Calcular desvios padrao dos periodos1 e 2
            desviosPadraoPeriodo1 = calcularDesviosPadraoDoPeriodo(mediasPeriodo1, matrizDados1);
            desviosPadraoPeriodo2 = calcularDesviosPadraoDoPeriodo(mediasPeriodo2, matrizDados2);

            // Calcular matriz de diferenças
            //matrizDiferencas = calcularMatrizDeDiferencas(periodo1, periodo2, dias, dados);
            matrizDiferencas = subtrairMatrizes(matrizDados1, matrizDados2);
            mediasDiferencas = calcularMediasDoPeriodo(matrizDiferencas);
            desviosPadraoDiferencas = calcularDesviosPadraoDoPeriodo(mediasDiferencas, matrizDiferencas);

            // Obter colunas a visualizar
            colunas = obterColunasDesejadas(modoExecucao);


            if (modoExecucao == MODO_INTERATIVO)
            {
                // Imprimir informaçao
                imprimirResumoAnaliseComparativa(periodo1, periodo2, arrayDias1, matrizDados1, arrayDias2, matrizDados2,
                        matrizDiferencas, mediasPeriodo1, mediasPeriodo2, mediasDiferencas, desviosPadraoPeriodo1, desviosPadraoPeriodo2, desviosPadraoDiferencas,
                        tipoAnalise, colunas, existemDadosPrimeiroDia1, existemDadosPrimeiroDia2);

                int escolhaUtilizador = analisouOK();
                if (escolhaUtilizador == 1)
                {
                    String ficheiroOutput = obterNomeFicheiroUtilizador();

                    escreverCsvResultadosAnaliseComparativa(periodo1, periodo2, arrayDias1, matrizDados1, arrayDias2, matrizDados2,
                            matrizDiferencas, mediasPeriodo1, mediasPeriodo2, mediasDiferencas, desviosPadraoPeriodo1, desviosPadraoPeriodo2, desviosPadraoDiferencas,
                            tipoAnalise, colunas, existemDadosPrimeiroDia1, existemDadosPrimeiroDia2, ficheiroOutput);
                }

                // retornar o valor resultante do submenu
                status = 1;
            }
            else
            {
                // Guardar dados em ficheiro txt
                escreverTxtResultadosAnaliseComparativa(periodo1, periodo2, arrayDias1, matrizDados1, arrayDias2, matrizDados2,
                        matrizDiferencas, mediasPeriodo1, mediasPeriodo2, mediasDiferencas, desviosPadraoPeriodo1, desviosPadraoPeriodo2, desviosPadraoDiferencas,
                        tipoAnalise, colunas, existemDadosPrimeiroDia1, existemDadosPrimeiroDia2);
                // Retornar codigo de execução
                status = 2;
            }
        }
        else
        {
            if (modoExecucao == MODO_INTERATIVO)
            {
                imprimirCabecalhoAnaliseComparativa(periodo1, periodo2, tipoAnalise, MODO_INTERATIVO);
                System.out.println(" Não existem dados para o período solicitado");
                System.out.println();
                ;

            }
            else
            {
                imprimirCabecalhoAnaliseComparativa(periodo1, periodo2, tipoAnalise, MODO_NAO_INTERATIVO);
                pw.println(" Não existem dados para o período solicitado");
                pw.println();
            }

        }

        return status;

    }

    /**
     * Imprime os resultados da analise comparativa
     * @param periodo1 String
     * @param periodo2 String
     * @param arrayDias1 array String
     * @param matrizDados1 matriz int
     * @param arrayDias2 array String
     * @param matrizDados2 matriz int
     * @param diferencas matriz int
     * @param medias1 array double
     * @param medias2 array double
     * @param mediasDif array double
     * @param desviosPadrao1 array double
     * @param desviosPadrao2 array  double
     * @param desviosDif array double
     * @param tipoAnalise int
     * @param colunasDesejadas array int
     * @param haDadosDia1P1 boolean
     * @param haDadosDia1P2 boolean
     * @param ficheiroDeSaida String
     * @return
     * @throws FileNotFoundException
     */
    public static int escreverCsvResultadosAnaliseComparativa(String periodo1, String periodo2, String[] arrayDias1, int[][] matrizDados1, String[] arrayDias2,
                                                              int[][] matrizDados2, int[][] diferencas, double[] medias1, double[] medias2, double[] mediasDif,
                                                              double[] desviosPadrao1, double[] desviosPadrao2, double[] desviosDif, int tipoAnalise, int[] colunasDesejadas,
                                                              boolean haDadosDia1P1, boolean haDadosDia1P2, String ficheiroDeSaida) throws FileNotFoundException
    {

        // Abrir ficheiro de saida
        PrintWriter saida = new PrintWriter(new File(ficheiroDeSaida));
        int status = 0;
        int nLinhas = matrizDados1.length;

        String[][] cabecalhoNovosCasos = {{"Novos_Inf.P1", "Novos_Inf.P2", "Dif."}, {"Novos_Hosp.P1", "Novos_Hosp.P2", "Dif."}, {"Novos_UCI.P1", "Novos_UCI.P2", "Dif."}, {"Novos_MortesP1", "Novos_MortesP2", "Dif."}};
        String[][] cabecalhoCasosTotais = {{"Totais_Inf.P1", "Totais_Inf.P2", "Dif."}, {"Totais_Hosp.P1", "Totais_Hosp.P2", "Dif."}, {"Totais_UCI.P1", "Totais_UCI.P2", "Dif."}, {"Totais_MortesP1", "Totais_MortesP2", "Dif."}};
        //
        saida.printf("%s,%s,", "DataP1", "DataP2");
        if (tipoAnalise == ANALISE_ACUMULADOS)
        {
            // Cabeçalho novos casos
            for (int i = 1; i < NUMERO_PARAMETROS; i++)
            {
                if (colunasDesejadas[i - 1] == 1)
                {
                    for (int j = 0; j < 3; j++)
                    {
                        saida.printf("%s,", cabecalhoNovosCasos[i - 1][j]);
                    }
                }
            }
            saida.println();
        }
        else
        {
            // Cabeçalho casos totais
            for (int i = 1; i < NUMERO_PARAMETROS; i++)
            {
                if (colunasDesejadas[i - 1] == 1)
                {
                    for (int j = 0; j < 3; j++)
                    {
                        saida.printf("%s,", cabecalhoCasosTotais[i - 1][j]);
                    }
                }
            }
            saida.println();

        }

        // Imprimir dados dos periodos
        for (int linha = 0; linha < nLinhas; linha++)
        {
            if (tipoAnalise == ANALISE_ACUMULADOS)
            {
                saida.printf(" %s,%s,", arrayDias1[linha + 1], arrayDias2[linha + 1]);
            }
            else
            {
                saida.printf(" %s,%s,", arrayDias1[linha], arrayDias2[linha]);
            }


            for (int coluna = 1; coluna < NUMERO_PARAMETROS; coluna++)
            {
                if (colunasDesejadas[coluna - 1] == 1)
                {

                    saida.printf("%d,", matrizDados1[linha][coluna]);
                    saida.printf("%d,", matrizDados2[linha][coluna]);
                    saida.printf("%d,", diferencas[linha][coluna]);
                }
            }

            saida.println();
        }

        // Imprimir medias e desvios padrao
        saida.printf("%s, ,", "Media");
        for (int coluna = 1; coluna < NUMERO_PARAMETROS; coluna++)
        {
            if (colunasDesejadas[coluna - 1] == 1)
            {
                saida.printf("%.4f,", medias1[coluna]);
                saida.printf("%.4f,", medias2[coluna]);
                saida.printf("%.4f,", mediasDif[coluna]);
            }
        }
        saida.println();

        saida.printf("%s, ,", "Desvio Padrao");
        for (int coluna = 1; coluna < NUMERO_PARAMETROS; coluna++)
        {
            if (colunasDesejadas[coluna - 1] == 1)
            {
                saida.printf("%.4f,", desviosPadrao1[coluna]);
                saida.printf("%.4f,", desviosPadrao2[coluna]);
                saida.printf("%.4f,", desviosDif[coluna]);

            }
        }
        saida.println();

        // Linha em branco no final
        saida.println();
        // Fechar ficheiros de saida
        saida.close();

        return status;

    }

    /**
     * Escreve os resultados da analise comparativa para um ficheiro ".txt"
     * @param periodo1 String
     * @param periodo2 String
     * @param arrayDias1 array String
     * @param matrizDados1 matriz int
     * @param arrayDias2 array String
     * @param matrizDados2 matriz int
     * @param diferencas matriz int
     * @param medias1 array double
     * @param medias2 array double
     * @param mediasDif array double
     * @param desviosPadrao1 array double
     * @param desviosPadrao2 array double
     * @param desviosDif array double
     * @param tipoAnalise int
     * @param colunasDesejadas array int
     * @param haDadosDia1P1 boolean
     * @param haDadosDia1P2 boolean
     * @return
     */
    public static int escreverTxtResultadosAnaliseComparativa(String periodo1, String periodo2, String[] arrayDias1, int[][] matrizDados1, String[] arrayDias2,
                                                              int[][] matrizDados2, int[][] diferencas, double[] medias1, double[] medias2, double[] mediasDif,
                                                              double[] desviosPadrao1, double[] desviosPadrao2, double[] desviosDif, int tipoAnalise, int[] colunasDesejadas,
                                                              boolean haDadosDia1P1, boolean haDadosDia1P2)
    {

        // Abrir ficheiro de saida
        //PrintWriter saida = new PrintWriter(new File(ficheiroDeSaida));
        int status = 0;
        int nLinhas = matrizDados1.length;

        // Imprimir cabecalho
        pw.println("+-----------------------------------------------------------------------------+");
        pw.println("|                                                                             |");
        switch (tipoAnalise)
        {
            case ANALISE_ACUMULADOS:
                pw.println("|                     ANALISE COMPARATIVA DE NOVOS CASOS                      |");
                break;
            case ANALISE_TOTAIS:
                pw.println("|                      ANALISE COMPARATIVA DE CASOS TOTAIS                    |");
                break;
        }
        pw.println("|                                                                             |");
        pw.printf("| Período 1: %-65s|\n", periodo1);
        pw.printf("| Período 2: %-65s|\n", periodo2);
        pw.println("|                                                                             |");
        pw.println("+-----------------------------------------------------------------------------+");
        if (tipoAnalise == ANALISE_ACUMULADOS && (!haDadosDia1P1 || !haDadosDia1P2))
        {
            pw.println();
            pw.println(" [AVISO]! Não existem dados para calcular o balanço do primeiro dia de um dos períodos, serão apresentados dados apenas para os dias subsequentes.");
            pw.println();
        }

        // Imprimir cabeçalho dependendo das colunas pretendidas pelo utilizador
        String[][] cabecalho = {{"Inf.P1", "Inf.P2", "Dif."}, {"Hosp.P1", "Hosp.P2", "Dif."}, {"UCI.P1", "UCI.P2", "Dif."}, {"MortesP1", "MortesP2", "Dif."}};
        pw.printf(" %-11s%-13s", "DataP1", "DataP2");
        for (int i = 1; i < NUMERO_PARAMETROS; i++)
        {
            if (colunasDesejadas[i - 1] == 1)
            {
                for (int j = 0; j < 3; j++)
                {
                    pw.printf("%-11s", cabecalho[i - 1][j]);
                }
            }
        }
        pw.println();

        // Imprimir dados dos periodos
        for (int linha = 0; linha < nLinhas; linha++)
        {
            if (tipoAnalise == ANALISE_ACUMULADOS)
            {
                pw.printf(" %-11s%-13s", arrayDias1[linha + 1], arrayDias2[linha + 1]);
            }
            else
            {
                pw.printf(" %-11s%-13s", arrayDias1[linha], arrayDias2[linha]);
            }


            for (int coluna = 1; coluna < NUMERO_PARAMETROS; coluna++)
            {
                if (colunasDesejadas[coluna - 1] == 1)
                {

                    pw.printf("%-11d", matrizDados1[linha][coluna]);
                    pw.printf("%-11d", matrizDados2[linha][coluna]);
                    pw.printf("%-11d", diferencas[linha][coluna]);
                }
            }

            pw.println();
        }

        // Imprimir medias e desvios padrao
        pw.printf(" %21s   ", "Média");
        for (int coluna = 1; coluna < NUMERO_PARAMETROS; coluna++)
        {
            if (colunasDesejadas[coluna - 1] == 1)
            {
                pw.printf("%-11.4f", medias1[coluna]);
                pw.printf("%-11.4f", medias2[coluna]);
                pw.printf("%-11.4f", mediasDif[coluna]);
            }
        }
        pw.println();

        pw.printf(" %21s   ", "Desvio Padrão");
        for (int coluna = 1; coluna < NUMERO_PARAMETROS; coluna++)
        {
            if (colunasDesejadas[coluna - 1] == 1)
            {
                pw.printf("%-11.4f", desviosPadrao1[coluna]);
                pw.printf("%-11.4f", desviosPadrao2[coluna]);
                pw.printf("%-11.4f", desviosDif[coluna]);

            }
        }
        pw.println();

        // Linha em branco no final
        pw.println();
        // Fechar ficheiro
        //saida.close();

        return status;

    }

    /**
     * Imprime o resumo obtido da analise comparativa
     * @param periodo1 String
     * @param periodo2 String
     * @param arrayDias1 array String
     * @param matrizDados1 matriz int
     * @param arrayDias2 array String
     * @param matrizDados2 matriz int
     * @param diferencas matriz int
     * @param medias1 array double
     * @param medias2 array double
     * @param mediasDif array double
     * @param desviosPadrao1 array double
     * @param desviosPadrao2 array double
     * @param desviosDif array double
     * @param tipoAnalise int
     * @param colunasDesejadas array int
     * @param haDadosDia1P1 boolean
     * @param haDadosDia1P2 boolean
     */
    public static void imprimirResumoAnaliseComparativa(String periodo1, String periodo2, String[] arrayDias1, int[][] matrizDados1, String[] arrayDias2,
                                                        int[][] matrizDados2, int[][] diferencas, double[] medias1, double[] medias2, double[] mediasDif,
                                                        double[] desviosPadrao1, double[] desviosPadrao2, double[] desviosDif, int tipoAnalise, int[] colunasDesejadas,
                                                        boolean haDadosDia1P1, boolean haDadosDia1P2)
    {
        int nLinhas = matrizDados1.length;

        // Imprimir cabecalho
        imprimirCabecalhoAnaliseComparativa(periodo1, periodo2, tipoAnalise, MODO_INTERATIVO);
        if (tipoAnalise == ANALISE_ACUMULADOS && (!haDadosDia1P1 || !haDadosDia1P2))
        {
            System.out.println();
            System.out.println(" [AVISO]! Não existem dados para calcular o balanço do primeiro dia de um dos períodos, serão apresentados dados apenas para os dias subsequentes.");
            System.out.println();
        }

        // Imprimir cabeçalho dependendo das colunas pretendidas pelo utilizador
        String[][] cabecalho = {{"Inf.P1", "Inf.P2", "Dif."}, {"Hosp.P1", "Hosp.P2", "Dif."}, {"UCI.P1", "UCI.P2", "Dif."}, {"MortesP1", "MortesP2", "Dif."}};

        System.out.printf(" %-11s%-13s", "DataP1", "DataP2");
        for (int i = 1; i < NUMERO_PARAMETROS; i++)
        {
            if (colunasDesejadas[i - 1] == 1)
            {
                for (int j = 0; j < 3; j++)
                {
                    System.out.printf("%-11s", cabecalho[i - 1][j]);
                }
            }
        }
        System.out.println();

        // Imprimir dados dos periodos
        for (int linha = 0; linha < nLinhas; linha++)
        {
            if (tipoAnalise == ANALISE_ACUMULADOS)
            {
                System.out.printf(" %-11s%-13s", arrayDias1[linha + 1], arrayDias2[linha + 1]);
            }
            else
            {
                System.out.printf(" %-11s%-13s", arrayDias1[linha], arrayDias2[linha]);
            }


            for (int coluna = 1; coluna < NUMERO_PARAMETROS; coluna++)
            {
                if (colunasDesejadas[coluna - 1] == 1)
                {

                    System.out.printf("%-11d", matrizDados1[linha][coluna]);
                    System.out.printf("%-11d", matrizDados2[linha][coluna]);
                    System.out.printf("%-11d", diferencas[linha][coluna]);
                }
            }

            System.out.println();
        }

        // Imprimir medias e desvios padrao
        System.out.printf(" %21s   ", "Média");
        for (int coluna = 1; coluna < NUMERO_PARAMETROS; coluna++)
        {
            if (colunasDesejadas[coluna - 1] == 1)
            {
                System.out.printf("%-11.4f", medias1[coluna]);
                System.out.printf("%-11.4f", medias2[coluna]);
                System.out.printf("%-11.4f", mediasDif[coluna]);
            }
        }
        System.out.println();

        System.out.printf(" %21s   ", "Desvio Padrão");
        for (int coluna = 1; coluna < NUMERO_PARAMETROS; coluna++)
        {
            if (colunasDesejadas[coluna - 1] == 1)
            {
                System.out.printf("%-11.4f", desviosPadrao1[coluna]);
                System.out.printf("%-11.4f", desviosPadrao2[coluna]);
                System.out.printf("%-11.4f", desviosDif[coluna]);

            }
        }
        System.out.println();

        // Linha em branco no final
        System.out.println();

    }

    /**
     * Calcula as medias de um determinado periodo dada uma matriz de dados
     * @param dados matriz int
     * @return
     */
    public static double[] calcularMediasDoPeriodo(int[][] dados)
    {

        int nColunas = dados[0].length;
        int nLinhas = dados.length;
        //int[] resumoDoPeriodo = calcularResumoDoPeriodo(periodo, dias, dados);
        double[] medias = new double[nColunas];

        for (int parametro = 0; parametro < nColunas; parametro++)
        {
            for (int linha = 0; linha < nLinhas; linha++)
            {
                medias[parametro] += dados[linha][parametro];
            }

            medias[parametro] = (double) medias[parametro] / nLinhas;
        }

        return medias;

    }

    /**
     * Calcula o numero de dias de um dado periodo
     * @param periodo String
     * @return
     */
    public static int calcularNumeroDiasDoPeriodo(String periodo)
    {

        String[] didf = obterDiaInicialFinalDoPeriodo(periodo);
        return (int) ChronoUnit.DAYS.between(LocalDate.parse(didf[0]), LocalDate.parse(didf[1])) + 1;

    }

    /**
     * Determina o dia inicial e final de um dado periodo
     * @param periodo String
     * @return
     */
    public static String[] obterDiaInicialFinalDoPeriodo(String periodo)
    {
        String sep = "/";
        return periodo.split(sep);
    }

    /**
     * Calcula o devido padrao de um dado periodo
     * @param medias matriz double
     * @param dados matriz int
     * @return
     */
    public static double[] calcularDesviosPadraoDoPeriodo(double[] medias, int[][] dados)
    {
        int nColunas = dados[0].length;
        int nLinhas = dados.length;
        double[] desviosPadrao = new double[nColunas];

        for (int parametro = 0; parametro < nColunas; parametro++)
        {
            for (int linha = 0; linha < nLinhas; linha++)
            {
                desviosPadrao[parametro] += Math.pow((dados[linha][parametro] - medias[parametro]), 2.0);
            }

            desviosPadrao[parametro] = Math.sqrt(desviosPadrao[parametro] / nLinhas);
        }

        return desviosPadrao;
    }

    /**
     * Obtém o indice de um dado dia dentro de uma dada matriz de datas
     * @param dia String
     * @param dias array String
     * @return
     */
    public static int obterIndiceDoDia(String dia, String[] dias)
    {
        int contador = 0;
        int indice = -1;

        while (contador < dias.length)
        {
            if (dias[contador] != null && dias[contador].compareTo(dia) == 0)
            {
                indice = contador;
            }
            contador++;
        }

        return indice;

    }

    /**
     * Calcula um novo periodo dado um dia inicial e um numero de dias
     * @param diaInicial String
     * @param numeroDias int
     * @return
     */
    public static String calcularNovoPeriodo(String diaInicial, int numeroDias)
    {
        String sep = "/";

        return diaInicial + sep + LocalDate.parse(diaInicial).plusDays(numeroDias - 1);
    }

    /**
     * Lê os dados de um dado ficheiro e retorna o número de dias
     * @param arr array String
     * @param matriz matriz int
     * @param escolhaSubMenuFicheiro int
     * @param nomeDoFicheiro String
     * @return int
     * @throws FileNotFoundException
     */
    public static int lerDadosDoFicheiro(String[] arr, int[][] matriz, int escolhaSubMenuFicheiro, String nomeDoFicheiro) throws FileNotFoundException
    {
        int qtdDias = 0;
        File ficheiroDeDados = new File(nomeDoFicheiro);
        boolean exists = ficheiroDeDados.exists();
        if (exists)
        {
            Scanner in = new Scanner(ficheiroDeDados);
            String linhaCabecalho = in.nextLine();
            String[] cabecalho = linhaCabecalho.split(",");
            String[] cabecalhoValido;
            if (escolhaSubMenuFicheiro == 1)
            {
                cabecalhoValido = CABECALHO_FICHEIRO_ACUMULADOS;
            }
            else
            {
                cabecalhoValido = CABECALHO_FICHEIRO_TOTAIS;
            }
            boolean validacao = validacaoDoCabecalho(cabecalho, cabecalhoValido);
            if (validacao)
            {
                while (in.hasNextLine())
                {
                    String linha = in.nextLine();
                    String[] elementos = linha.split(",");
                    // Se for o ficheiro de dados totais trocar a data de dd-MM-yyyy para yyyy-mm-dd
                    if (escolhaSubMenuFicheiro == 2)
                    {
                        elementos[0] = formatarData(elementos[0]);
                    }
                    arr[qtdDias] = elementos[0];
                    for (int estadoSaude = 0, elemento = 1; elemento < elementos.length; estadoSaude++, elemento++)
                    {
                        matriz[qtdDias][estadoSaude] = Integer.parseInt(elementos[elemento]);
                    }
                    qtdDias++;
                }
                in.close();
            }
            else if (escolhaSubMenuFicheiro == 1)
            {
                System.out.printf("@ O ficheiro %s não é de dados acumulados\n", nomeDoFicheiro);
                imprimirSubmenuLerFicheiroDados();
            }
            else
            {
                System.out.printf("@ O ficheiro %s não é de dados totais\n", nomeDoFicheiro);
                imprimirSubmenuLerFicheiroDados();
            }
        }
        else
        {
            System.out.printf("@ O ficheiro %s não existe\n", nomeDoFicheiro);
        }
        return qtdDias;
    }

    /**
     * Valida o cabeçalho
     * @param cabecalho array String
     * @param cabecalhoValido array String
     * @return
     */
    public static boolean validacaoDoCabecalho(String[] cabecalho, String[] cabecalhoValido)
    {
        boolean validacao = true;
        int i = cabecalhoValido.length - 1;
        while (i > 0 && validacao)
        {
            if (!cabecalho[i].equals(cabecalhoValido[i]))
            {
                validacao = false;
            }
            i--;
        }
        return validacao;
    }

    /**
     * Lê os dados de um dado ficheiro com matrizes de markov
     * @param markov matriz double
     * @param nomeDoFicheiro String
     * @return
     * @throws FileNotFoundException
     */
    public static double[][] lerDadosDoFicheiroMarkov(double[][] markov, String nomeDoFicheiro) throws FileNotFoundException
    {

        File ficheiroDeDados = new File(nomeDoFicheiro);
        boolean exists = ficheiroDeDados.exists();
        if (exists)
        {
            Scanner in = new Scanner(ficheiroDeDados);
            String linha = "";
            for (int j = 0; j < 5; j++)
            {
                for (int i = 0; i < 5; i++)
                {
                    linha = in.nextLine();
                    linha = linha.trim();
                    String[] itensDaLinha = linha.split("=");
                    markov[j][i] = Double.parseDouble(itensDaLinha[1]);
                }
                if (in.hasNextLine()) linha = in.nextLine();
            }
            in.close();
        }
        else
        {
            System.out.println("@ O ficheiro indicado não existe");
        }
        return markov;
    }

    /**
     * Calcula o balanco entre as linhas de uma dada matriz
     * @param matrizDados matriz int
     * @return matriz int
     */
    public static int[][] calcularBalanco(int[][] matrizDados)
    {
        int[][] dadosBalanco = new int[matrizDados.length - 1][NUMERO_PARAMETROS];

        for (int i = 1; i < matrizDados.length; i++)
        {

            for (int j = 0; j < NUMERO_PARAMETROS; j++)
            {
                dadosBalanco[i - 1][j] = matrizDados[i][j] - matrizDados[i - 1][j];
            }

        }

        return dadosBalanco;
    }

    /**
     * Dada uma determinada data, determina em que dia da semana calha esse dia
     * @param data string
     * @return
     */
    public static int obterDiaSemana(String data)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(data, formatter); // LocalDate = 2010-02-23
        DayOfWeek dow = date.getDayOfWeek();  // Extracts a `DayOfWeek` enum object.
        int output = dow.getValue();

        return output;
    }

    /**
     * Dado um determinado periodo e um array de datas, determina qual o indice da data dada, no array
     * @param periodo string
     * @param arrayDatas array strings
     * @return
     */
    public static int[] obterIndice(String periodo, String[] arrayDatas)
    {
        int contador = 0;
        boolean encontrado = false;
        int[] indices = {0, -1};

        String[] arrayPeriodo = periodo.split("/");

        for (int i = 0; i < arrayPeriodo.length; i++)
        {

            while (!encontrado && arrayDatas[contador] != null)
            {

                if (arrayDatas[contador].equals(arrayPeriodo[i]))
                {
                    encontrado = true;
                    indices[i] = contador;
                }
                contador++;
            }
            contador = 0;
            encontrado = false;
        }
        //ordenarArrayIndicesPeriodo(indices);

        return indices;
    }

    /**
     * Dada uma determinada resolução, preenche uma matriz de dados e um array de datas para esse periodo
     * @param periodo string
     * @param matrizDados matriz inteiros
     * @param arrayDatas array strings
     * @param matrizDadosObtidos matriz inteiros
     * @param arrayDiasObtidos array strings
     * @param indices array inteiros
     * @param resolucao inteiro
     * @return
     */
    public static int obterDadosPeriodo(String periodo, int[][] matrizDados, String[] arrayDatas, int[][] matrizDadosObtidos, String[] arrayDiasObtidos, int[] indices, int resolucao)
    {
        int status = 0, contador = 0;

        if (calcularNumeroDiasDoPeriodo(periodo) != (indices[1] - indices[0] + 1))
        {
            status = -1;
        }
        else
        {
            switch (resolucao)
            {
                case RESOLUCAO_DIARIA:
                    for (int i = indices[0]; i <= indices[1]; i++)
                    {
                        for (int j = 0; j < NUMERO_PARAMETROS; j++)
                        {
                            matrizDadosObtidos[contador][j] = matrizDados[i][j];

                        }
                        arrayDiasObtidos[contador] = arrayDatas[i];
                        contador++;
                    }
                    break;
                case RESOLUCAO_SEMANAL:

                    for (int i = indices[0]; i <= indices[1]; i++)
                    {
                        int dia = obterDiaSemana(arrayDatas[i]);
                        if (dia == 7)
                        {
                            for (int j = 0; j < NUMERO_PARAMETROS; j++)
                            {
                                matrizDadosObtidos[contador][j] = matrizDados[i][j];
                            }
                            arrayDiasObtidos[contador] = arrayDatas[i];
                            contador++;
                        }
                    }

                    break;

                case RESOLUCAO_MENSAL:
                    String ultimoDiaPeriodo = LocalDate.parse(arrayDatas[indices[1]]).with(TemporalAdjusters.lastDayOfMonth()).toString();

                    for (int i = indices[0]; i <= indices[1]; i++)
                    {
                        if (arrayDatas[i].split("-")[2].equals("01") && i != 0)
                        {
                            arrayDiasObtidos[contador] = arrayDatas[i - 1];

                            for (int j = 0; j < NUMERO_PARAMETROS; j++)
                            {
                                matrizDadosObtidos[contador][j] = matrizDados[i - 1][j];

                            }
                            contador++;
                        }
                    }

                    // A busca é feita pelo dia 01 para obter os dados do dia anterior, contudo, se o ultimo dia
                    // for o ultimo do mes tambem temos de ir buscar essa linha
                    if (arrayDatas[indices[1]].compareTo(ultimoDiaPeriodo) == 0)
                    {

                        arrayDiasObtidos[contador] = arrayDatas[indices[1]];
                        for (int j = 0; j < NUMERO_PARAMETROS; j++)
                        {
                            matrizDadosObtidos[contador][j] = matrizDados[indices[1]][j];

                        }
                    }

                    break;
            }
        }

        return status;
    }

    /**
     * Dado um determinado array com o calculo previsto de casos para uma determinada data, calcula o erro entre a previsão obtida e os dados reais
     * @param previsto matriz double
     * @throws FileNotFoundException
     */
    public static void calcularErro(double[][] previsto) throws FileNotFoundException
    {
        String[] datas = new String[MAX_DIAS];
        int[][] dadosTotais = new int[MAX_DIAS][NUMERO_PARAMETROS];
        //lerDadosDoFicheiro(datas, dadosTotais, "totalPorEstadoCovid19EmCadaDia.csv");
        double[][] erro = new double[7][5];

        int contador = 0, indicePrimeiroDiaJaneiro = obterIndiceDoDia("01-01-2022", datas);

        for (int i = indicePrimeiroDiaJaneiro; i < indicePrimeiroDiaJaneiro + 7; i++)
        {
            //System.out.print("data _> " + datas[i] + "  ");
            for (int j = 0; j < NUMERO_PARAMETROS; j++)
            {
                erro[contador][j] += Math.abs(dadosTotais[i][j] - previsto[contador][j]);

                //System.out.print(erro[contador][j] + "\t");

            }
            //System.out.println();
            contador++;
        }
    }

    /**
     * Lê e valida o período inserido pelo utilizador
     * @return String
     */
    public static String lerPeriodoDias()
    {
        System.out.println("# Insira o periodo que deseja: ");
        String diaInicial = obterData();
        String diaFinal = obterData();
        String periodo = diaInicial + "/" + diaFinal;
        while(!validarPeriodo(periodo))
        {
            System.out.println("! A data final não pode ser anterior à data inicial.");
            System.out.println("# Insira o periodo que deseja: ");
            diaInicial = obterData();
            diaFinal = obterData();
            periodo = diaInicial + "/" + diaFinal;
        }
        return diaInicial + "/" + diaFinal;
    }

    /**
     * Lê e analisa a data inserida pelo utilizador
     * @return String
     */
    public static String obterData()
    {
        // Limpar buffer
        sc.nextLine();
        System.out.print("@ Introduza data [DD-MM-YYYY]:");
        String data = sc.next();
        while (validarData(data) == -1)
        {
            System.out.println("! Data inválida. Introduza novamente a data.");
            System.out.print("@ Introduza data [DD-MM-YYYY]:");
            data = sc.next();
        }
        // converter para o formato YYYY-MM-DD
        data = formatarData(data);
        return data;
    }

    /**
     * Dadas duas matrizes, calcula o somatório da multiplicação das matrizes em cada indice.
     * @param mA matriz double
     * @param mB matriz double
     * @return
     */
    public static double[][] multiplicarMatrizesAxB(double[][] mA, double[][] mB)
    {
        int colunasA = mA[0].length;
        int linhasA = mA.length;
        int linhasB = mB.length;
        int colunasB = mB[0].length;
        if (colunasA != linhasB)
        {
            throw new IllegalStateException("Erro: Tamanho Matrizes");
        }

        double[][] resultado = new double[linhasA][colunasB];
        for (int i = 0; i < linhasA; i++)
        {
            for (int j = 0; j < colunasB; j++)
            {
                resultado[i][j] = 0;
                for (int k = 0; k < colunasA; k++)
                {
                    resultado[i][j] = resultado[i][j] + mA[i][k] * mB[k][j];
                }
            }
        }
        return (resultado);
    }

    public static double[] multiplicarMatrizEArrayComoColuna(double[][] mA, int[] arrB)
    {
        double[][] mArrayParaColunas = new double[arrB.length][1];
        for (int i = 0; i < arrB.length; i++)
        {
            mArrayParaColunas[i][0] = arrB[i];
        }

        double[][] resultado = multiplicarMatrizesAxB(mA, mArrayParaColunas);

        double[] arrayResultado = new double[resultado.length];
        for (int i = 0; i < resultado.length; i++)
        {
            arrayResultado[i] = resultado[i][0];
        }
        return arrayResultado;
    }

    public static double[] multiplicarMatrizEArrayComoColuna(double[][] mA, double[] arrB)
    {
        double[][] mArrayParaColunas = new double[arrB.length][1];
        for (int i = 0; i < arrB.length; i++)
        {
            mArrayParaColunas[i][0] = arrB[i];
        }

        double[][] resultado = multiplicarMatrizesAxB(mA, mArrayParaColunas);

        double[] arrayResultado = new double[resultado.length];
        for (int i = 0; i < resultado.length; i++)
        {
            arrayResultado[i] = resultado[i][0];
        }
        return arrayResultado;
    }

    /**
     * Dependendo do modo de execução, imprime o array dado para o ecra ou para um ficheiro
     * @param array array double
     * @param modoExecucao int
     */
    public static void imprimirArray(double[] array, int modoExecucao)
    {

        switch (modoExecucao)
        {
            case MODO_INTERATIVO:

                for (int j = 0; j < array.length; j++)
                {
                    System.out.printf("%14.1f|", array[j]);
                }
                break;

            case MODO_NAO_INTERATIVO:

                for (int j = 0; j < array.length; j++)
                {
                    pw.printf("%14.1f|", array[j]);
                }

                break;
        }
    }

    /**
     * Imprime a matriz dada para o ecra
     * @param matriz matriz double
     * @param format String
     */
    public static void imprimirMatriz(double[][] matriz, String format)
    {

        //format= "%16.4f"
        for (int i = 0; i < matriz.length; i++)
        {
            for (int j = 0; j < matriz[0].length; j++)
            {
                System.out.printf(format, matriz[i][j]);
            }
            System.out.println();
        }
    }

    /**
     * Imprime a matriz dada para um ficheiro
     * @param matriz matriz double
     * @param format String
     */
    public static void imprimirMatrizPrintWriter(double[][] matriz, String format)
    {

        //format= "%16.4f"
        for (int i = 0; i < matriz.length; i++)
        {
            for (int j = 0; j < matriz[0].length; j++)
            {
                pw.printf(format, matriz[i][j]);
            }
            pw.println();
        }
    }

    /**
     * Dada uma matriz, determina a matriz identidade
     * @param matriz matriz double
     * @return matriz double
     */
    public static double[][] obterMatrizIdentidade(double[][] matriz)
    {
        int linhas = matriz.length;
        int colunas = matriz[0].length;
        if (linhas != colunas)
        {
            System.out.println("ERRO: Matriz não quadrada");
        }
        double[][] identidade = new double[matriz.length][matriz[0].length];
        for (int i = 0; i < matriz.length; i++)
        {
            identidade[i][i] = 1;
        }
        return identidade;
    }

    /**
     * Verifica se uma dada matriz está vazia
     * @param matriz matriz double
     * @return
     */
    public static boolean matrizEstaVaziaZeros(double[][] matriz)
    {
        int zeros = 0;
        boolean flag = false;

        for (int i = 0; i < matriz.length; i++)
        {
            for (int j = 0; j < matriz[0].length; j++)
            {
                if (matriz[i][j] == 0)
                {
                    zeros++;
                }
            }
        }
        if (zeros == matriz.length * matriz[0].length) flag = true;

        return flag;
    }

    /**
     * Elimina a ultima linha e a ultima coluna de uma dada matriz
     * @param matriz matriz double
     * @return matriz double
     */
    public static double[][] eliminarUltimaLinhaeUltimaColunaDeUmaMatriz(double[][] matriz)
    {
        double[][] matrizQ = new double[matriz.length - 1][matriz[0].length - 1];
        for (int j = 0; j < matrizQ.length; j++)
        {
            for (int i = 0; i < matrizQ[0].length; i++)
            {
                matrizQ[j][i] = matriz[j][i];
            }
        }
        return matrizQ;
    }

    /**
     * Faz uma previsao para uma data utilizando as cadeias de Markov
     * @param markov matriz double
     * @param arrDatasTotais array String
     * @param matrizDadosTotais matriz int
     * @param dataFutura String
     * @param modoExecucao int
     * @param ficheiroSaida String
     * @throws FileNotFoundException
     */
    public static void analisePrevisaoMarkov(double[][] markov, String[] arrDatasTotais, int[][] matrizDadosTotais, String dataFutura, int modoExecucao, String ficheiroSaida) throws FileNotFoundException
    {

        if (modoExecucao == MODO_INTERATIVO)
        {
            do
            {
                System.out.print("@ Insira data p/ obter Previsão (DD-MM-AAAA): ");
                dataFutura = sc.next();

                if (validarData(dataFutura) < 0)
                {
                    System.out.println("Erro Data!");
                }
            } while (validarData(dataFutura) < 0);
        }
        int indiceVespera = encontrarVesperaOuDataMaisProximaVersao2(formatarData(dataFutura), arrDatasTotais);

        if (modoExecucao == MODO_INTERATIVO)
        {
            System.out.print("| A Mostrar previsão da data: ");
            System.out.printf("%10s", dataFutura);
            System.out.print(" com base na data: ");
            System.out.printf("%10s", formatarData(arrDatasTotais[indiceVespera]));
            System.out.print("---------|");
            System.out.println();
        }
        String intervalo = arrDatasTotais[indiceVespera] + "/" + formatarData(dataFutura);
        int diasAnalise = calcularNumeroDiasDoPeriodo(intervalo);

        //double[][] matrizParaCasoDeEstudo = new double[diasAnalise - 1][5];
        double[] previsto = multiplicarMatrizEArrayComoColuna(markov, matrizDadosTotais[indiceVespera]);
        //matrizParaCasoDeEstudo[0] = previsto;
        for (int i = 1; i < diasAnalise - 1; i++)
        {
            previsto = multiplicarMatrizEArrayComoColuna(markov, previsto);
            //matrizParaCasoDeEstudo[i] = previsto;
        }

        double[][] matrizM = retornarNumeroDiasAteAMorte(markov);


        switch (modoExecucao)
        {
            case MODO_INTERATIVO:

                System.out.println("|-----------------------------------------------------------------------------|");
                System.out.println("|                                                                             |");
                System.out.println("|                      ANÁLISE DE PREVISÃO DA PANDEMIA                        |");
                System.out.println("|                                                                             |");
                System.out.println("|-----------------------------------------------------------------------------|");
                System.out.println("|--- Previsão de dia " + dataFutura + ", baseada nos casos totais do dia " + formatarData(arrDatasTotais[indiceVespera]) + ". -|");

                imprimirCabecalhoComParametros("%14s|", MODO_INTERATIVO);

                System.out.println();
                System.out.print("|-");
                imprimirArray(previsto, MODO_INTERATIVO);
                System.out.println("-|");

                imprimirLegenda(MODO_INTERATIVO);
                System.out.println();

                int escolhaUtilizador = analisouOK();
                if (escolhaUtilizador == 1)
                {
                    ficheiroSaida = obterNomeFicheiroUtilizador();
                    imprimirPrevisoesEmCsv(ficheiroSaida, CABECALHO_FICHEIRO_TOTAIS, dataFutura, arrDatasTotais[indiceVespera], matrizDadosTotais[indiceVespera], previsto);

                }

                break;

            case MODO_NAO_INTERATIVO:


                imprimirPrevisoesEmTxt(ficheiroSaida, dataFutura, arrDatasTotais[indiceVespera], previsto, matrizM);

                break;

            default:

                throw new IllegalStateException("Valor inesperado [RESOLUCAO]: Análise De Previsão");
        }

    }

    /**
     * Imprime as previsões para um ficheiro ".txt"
     * @param ficheiroSaida String
     * @param dataFutura String
     * @param dataVespera String
     * @param previsto array double
     * @param matrizM matriz double
     * @throws FileNotFoundException
     */
    public static void imprimirPrevisoesEmTxt(String ficheiroSaida, String dataFutura, String dataVespera, double[] previsto, double[][] matrizM) throws FileNotFoundException
    {

        //File file = new File(ficheiroSaida);
        //PrintWriter saidaParaTxt = new PrintWriter(file);

        pw.println("|-----------------------------------------------------------------------------|");
        pw.println("|                                                                             |");
        pw.println("|                      ANÁLISE DE PREVISÃO DA PANDEMIA                        |");
        pw.println("|                                                                             |");
        pw.println("|-----------------------------------------------------------------------------|");

        //mostrarUltimosFicheirosLidos(nomeDoFicheiro);

        pw.println("|--- Previsão de dia " + dataFutura + ", baseada nos casos totais do dia " + dataVespera + ". -|");

        imprimirCabecalhoComParametros("%14s|", MODO_NAO_INTERATIVO);

        pw.println();
        pw.print("|-");
        imprimirArray(previsto, MODO_NAO_INTERATIVO);
        pw.println("-|");

        imprimirLegenda(MODO_NAO_INTERATIVO);
        pw.println();
        pw.println();
        pw.println("|-----------------------------------------------------------------------------|");
        pw.println("|        Matriz de Transições: Estimativa - Número de Dias Até à Morte        |");
        pw.println("|-----------------------------------------------------------------------------|");
        pw.println("|-           NNI|            NI|            NH|          NUCI|               -|");

        pw.print("|-");

        imprimirMatrizPrintWriter(matrizM, "%14.1f|");

        imprimirLegenda(MODO_NAO_INTERATIVO);
        //saidaParaTxt.close();

    }

    /**
     * Imprime as previsões para um ficheiro ".csv"
     * @param ficheiroSaida String
     * @param cabecalho array String
     * @param dataFutura String
     * @param dataVespera String
     * @param real array int
     * @param previsto array double
     * @throws FileNotFoundException
     */
    public static void imprimirPrevisoesEmCsv(String ficheiroSaida, String[] cabecalho, String dataFutura, String dataVespera, int[] real, double[] previsto) throws FileNotFoundException
    {

        File file = new File(ficheiroSaida);
        PrintWriter saidaParaCsv = new PrintWriter(file);

        for (int i = 0; i < cabecalho.length - 1; i++)
        {
            saidaParaCsv.printf("%s,", cabecalho[i]);
        }
        saidaParaCsv.print(cabecalho[cabecalho.length - 1]);
        saidaParaCsv.println();

        saidaParaCsv.print("Real_" + dataVespera + ",");
        for (int i = 0; i < real.length - 1; i++)
        {
            saidaParaCsv.print(real[i] + ",");
        }
        saidaParaCsv.printf("%d,", real[real.length - 1]);
        saidaParaCsv.println();

        saidaParaCsv.print("Previsto_" + dataFutura + ",");
        for (int i = 0; i < previsto.length - 1; i++)
        {
            saidaParaCsv.printf("%.1f,", previsto[i]);
        }
        saidaParaCsv.printf("%.1f", previsto[previsto.length - 1]);

        saidaParaCsv.close();
    }

    /**
     * Imprime para um ficheiro ".csv" as previsoes de mortes
     * @param ficheiroSaida String
     * @param cabecalho array String
     * @param matrizM matriz double
     * @throws FileNotFoundException
     */
    public static void imprimirPrevisoesEmCsvMorte(String ficheiroSaida, String[] cabecalho, double[][] matrizM) throws FileNotFoundException
    {

        File file = new File(ficheiroSaida);
        PrintWriter saidaParaCsv = new PrintWriter(file);

        cabecalho = CABECALHO_FICHEIRO_TOTAIS;

        for (int i = 1; i < cabecalho.length - 1; i++)
        {
            saidaParaCsv.printf("%s,", cabecalho[i]);
        }
        saidaParaCsv.println();

        for (int i = 0; i < matrizM[0].length; i++)
        {
            saidaParaCsv.printf("%.1f,", matrizM[0][i]);
        }

        saidaParaCsv.close();
    }

    public static void mostrarNumeroDiasAteAMorte(double[][] markov) throws FileNotFoundException
    {

        double[][] matrizM = retornarNumeroDiasAteAMorte(markov);
        System.out.println("|-----------------------------------------------------------------------------|");
        System.out.println("|        Matriz de Transições: Estimativa - Número de Dias Até à Morte        |");
        System.out.println("|-----------------------------------------------------------------------------|");
        System.out.println("|-           NNI|            NI|            NH|          NUCI|               -|");

        System.out.print("|-");
        imprimirMatriz(matrizM, "%14.1f|");

        imprimirLegenda(MODO_INTERATIVO);

        int escolhaUtilizador = analisouOK();
        if (escolhaUtilizador == 1)
        {
            String ficheiroSaida = obterNomeFicheiroUtilizador();
            imprimirPrevisoesEmCsvMorte(ficheiroSaida, CABECALHO_FICHEIRO_TOTAIS, matrizM);
        }
    }

    /**
     * Retorna o numero de dias até à morte
     * @param markov matriz double
     * @return
     */
    public static double[][] retornarNumeroDiasAteAMorte(double[][] markov)
    {
        //System.out.println("Isto é da segunda Parte do enunciado:");
        double[][] vetorUnitarioL = {{1, 1, 1, 1}};
        double[][] matrizQ = eliminarUltimaLinhaeUltimaColunaDeUmaMatriz(markov);
        double[][] matrizDiferenca = calcularDiferencaMatrizesAeB(obterMatrizIdentidade(matrizQ), matrizQ);
        //double[][] matrizN = ObterInversadeUmaMatriz(matrizDiferenca);
///////////////////PROCEDIMENTO MATRIZ LU VAI AQUI ---JOAO MAIN////////////////////////////
        // metodo implementado para a decomposição LU
        int dim = matrizDiferenca.length;
        double[][] matrizL = new double[dim][dim];
        double[][] matrizU = new double[dim][dim];
        decomposicaoLU(matrizDiferenca, dim, matrizL, matrizU);
        // calculo das inversas
        double[][] LInversa = new double[dim][dim];
        double[][] UInversa = new double[dim][dim];
        calcularInversaDaMatrizUeDaMatrizL(matrizL, matrizU, dim, LInversa, UInversa);
        // calcular a inversa da matriz inicial
        double[][] matrizN = calcularInversaMatrizInicial(LInversa, UInversa);
/////////////////////////////////////////////////////////////////////////////////////////
        double[][] matrizM = multiplicarMatrizesAxB(vetorUnitarioL, matrizN);

        return matrizM;
    }

    /**
     * Procura uma data num array de datas (Strings), COM VAZIOS OU SEM
     * e retorna a localização da véspera ou dia mais próximo.
     * @param dataFutura Data a procurar
     * @param arrayDatasTotais String com Datas ordenadas.
     * @return Retorna o índice do array de datas que contém a véspera ou a data mais próxima.
     */
    public static int encontrarVesperaOuDataMaisProximaVersao2(String dataFutura, String[] arrayDatasTotais)
    {
        int indiceVespera = obterIndiceDoDia(dataFutura, arrayDatasTotais);

        int menorIntervalo = 10000;
        for (int i = 0; i < arrayDatasTotais.length; i++)
        {
            if (arrayDatasTotais[i] != null)
            {
                int intervalo = calcularNumeroDiasDoPeriodo(arrayDatasTotais[i] + "/" + dataFutura);
                if (intervalo < menorIntervalo && intervalo > 1)
                {
                    menorIntervalo = intervalo;
                    indiceVespera = obterIndiceDoDia(arrayDatasTotais[i], arrayDatasTotais);
                }
            }
        }

        //Se não houver dias anteriores na matriz, devia criar uma excepção aqui. Mas vou retornar a data do proprio dia.
        if (indiceVespera == 0) indiceVespera = 0;

        return indiceVespera;
    }

    /**
     * Retorna o índice da última linha preenchida de Array. Se estiver vazio, retorna -1.
     * @param array de strings com nulos ou não
     * @return indice
     */
    public static int obterIndiceDaUltimaLinhaPreenchidaDeArray(String[] array)
    {
        int indice = 0;

        if (array[array.length - 1] != null)
        {
            indice = array.length - 1;
        }

        else
        {
            for (int i = array.length - 1; i >= 0; i--)
            {
                while (array[i--] == null && i >= 0)
                {
                    indice = i;
                }
            }

            int qtd = 0;
            for (int i = 0; i < array.length; i++)
            {
                if (array[i] == null)
                {
                    qtd++;
                }
            }
            if (qtd == array.length)
            {
                indice = -1;
            }
        }
        return indice;
    }

    /**
     * Imprime legendas
     * @param modoExecucao int
     */
    public static void imprimirLegenda(int modoExecucao)
    {

        switch (modoExecucao)
        {
            case MODO_INTERATIVO:
                System.out.println("|-----------------------------------------------------------------------------|");
                System.out.println("| Sigla      Legenda                                                          |");
                System.out.println("|-----------------------------------------------------------------------------|");
                System.out.println("| NNI       Número de não infetados                                           |");
                System.out.println("| NI        Número de infetados                                               |");
                System.out.println("| NH        Número de hospitalizados                                          |");
                System.out.println("| NUCI      Número de hospitalizados em unidade de cuidados intensivos        |");
                System.out.println("| NM        Número de mortes                                                  |");
                System.out.println("|-----------------------------------------------------------------------------|");
                break;

            case MODO_NAO_INTERATIVO:

                pw.println("|-----------------------------------------------------------------------------|");
                pw.println("| Sigla      Legenda                                                          |");
                pw.println("|-----------------------------------------------------------------------------|");
                pw.println("| NNI       Número de não infetados                                           |");
                pw.println("| NI        Número de infetados                                               |");
                pw.println("| NH        Número de hospitalizados                                          |");
                pw.println("| NUCI      Número de hospitalizados em unidade de cuidados intensivos        |");
                pw.println("| NM        Número de mortes                                                  |");
                pw.println("|-----------------------------------------------------------------------------|");
                break;

        }


    }

    /**
     * Imprime os cabeçalhos dependendo do modo de Execução pretendido
     * @param format String
     * @param modoExecucao int
     */
    public static void imprimirCabecalhoComParametros(String format, int modoExecucao)
    {
        String[] header = {"NNI", "NI", "NH", "NUCI", "NM"};


        switch (modoExecucao)
        {
            case MODO_INTERATIVO:
                //format = "%14s|"
                System.out.print("|-");

                for (int i = 0; i < header.length; i++)
                {
                    System.out.printf(format, header[i]);
                }
                System.out.print("-|");
                break;
            case MODO_NAO_INTERATIVO:
                pw.print("|-");
                for (int i = 0; i < header.length; i++)
                {
                    pw.printf(format, header[i]);
                }
                pw.print("-|");

        }
    }

// DECOMPOSICAO LU E METODOS PARA OBTER INVERSA

    /**
     *
     * @param matriz - matriz inicial que vem da analise Markvov
     * @param n - dimensão da matriz inicial
     * @param l - matriz triangular inferior
     * @param u - matriz triangular superior
     */
    public static void decomposicaoLU(double[][] matriz, int n, double[][] l, double[][] u)
    {

        // obter as restantes linhas e colunas das matrizes L e U respetivamente
        for (int i = 0; i < n; i++)
        {
            u[i][i] = 1;
            for (int j = i; j < n; j++)
            {
                // Somatorio l*u que integra a matriz L
                double somaL = 0;
                for (int k = 0; k <= i - 1; k++)
                {
                    somaL += (l[j][k] * u[k][i]);
                }
                // elementos l = a - somatorio
                l[j][i] = matriz[j][i] - somaL;
            }

            for (int j = i + 1; j < n; j++)
            {
                // Somatorio l*u que integra a matriz U
                double somaU = 0;
                for (int k = 0; k <= i - 1; k++)
                {
                    somaU += (l[i][k] * u[k][j]);
                }
                // elementos u = (a - somatorio)/l
                u[i][j] = (matriz[i][j] - somaU) / (l[i][i]);
            }
        }
    }

    /**
     *
     * @param matriz - matriz que queremos verificar, neste caso a matriz L
     * @param n - dimensao da matriz
     * @return um booleano que verifica se é ou não singular
     */
    // verificar a singularidade da matriz para verificar se é invertivel atraves da verificação das diagonais diferentes de zero
    public static boolean verificarSingularidadeMatriz(double[][] matriz, int n)
    {
        boolean flag = true;
        int i = 0;
        while (flag && i < n)
        {
            if (matriz[i][i] == 0)
            {
                flag = false;
            }
            i++;
        }
        return flag;
    }

    /**
     *
     * @param matrizL - matriz triangular inferior
     * @param matrizU - matriz triangular superior
     * @param n - dimensão da matriz
     * @param x - matriz inversa L que queremos obter
     * @param y - matriz inversa U que queremos obter
     */
    public static void calcularInversaDaMatrizUeDaMatrizL(double[][] matrizL, double[][] matrizU, int n, double[][] x, double[][] y)
    {
        // verificação apenas da matrizL uma vez que a matrizU tem como diagonal U. Det(A) = Det (U)* Det (L) => Det(A) = Det(L)
        if (!verificarSingularidadeMatriz(matrizL, n))
        {
            System.out.println("A matriz é singular! Não é possível inverter");
        }
        else
        {
            for (int i = 0; i < n; i++)
            {
                //matriz X é a inversa da matriz L
                x[i][i] = 1 / matrizL[i][i];
                //matriz Y é a inversa da matriz U
                y[i][i] = 1 / matrizU[i][i];
                for (int j = i + 1; j < n; j++)
                {
                    double somaX = 0;
                    double somaY = 0;
                    for (int k = i; k <= j - 1; k++)
                    {
                        somaX += matrizL[j][k] * x[k][i];
                        somaY += matrizU[k][j] * y[i][k];
                    }
                    x[j][i] = -somaX / matrizL[j][j];
                    y[i][j] = -somaY / matrizU[i][i];
                }
            }
        }
    }

    /**
     *
     * @param x - matriz inversa L
     * @param y - matriz inversa U
     * @return - a multiplicação de U^-1 * L^-1 (nesta ordem)
     */

    public static double[][] calcularInversaMatrizInicial(double[][] x, double[][] y)
    {
        return multiplicarMatrizesAxB(y, x);
    }

}