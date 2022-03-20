import java.io.FileNotFoundException;

public class Testes
{

    private static final String[] ARRAY_DIAS = {
            "2020-04-01",
            "2020-04-02",
            "2020-04-03",
            "2020-04-04",
            "2020-04-05",
            "2020-04-06"
    };

    // Balanco diario do dia 2020-04-01 a 2020-04-10 do ficheiro de exemplo
    private static final int[][] BALANCO_DIARIO = {
            {627, 783, 1042, 240, 22},
            {544, 852, 1058, 245, 37},
            {717, 638, 1075, 251, 20},
            {893, 754, 1084, 267, 29},
            {784, 452, 1099, 270, 16},
            {442, 712, 1180, 271, 34},
            {1025, 699, 1211, 245, 35},
            {800, 815, 1173, 241, 29},
            {775, 1516, 1179, 226, 26}
    };

    private static final double[][] matrizBTeste = {
            {627, 783, 1042, 240, 22},
            {544, 852, 1058, 245, 37},
            {717, 638, 1075, 251, 20},
            {893, 754, 1084, 267, 29},
            {784, 452, 1099, 270, 16}
    };

    private static final int[] arrayIntTeste = {627, 783, 1042, 240, 22};

    private static final double[][] matrizAdoubleTeste = {
            { 0.9995,	0.0300,	0.0020,	0.0020,	0.0000},
            {0.0005,	0.9600,	0.0040,	0.0040,	0.0000},
            {0.0000,	0.0070,	0.9800,	0.9800,	0.0000},
            {0.0000,	0.0030,	0.0100,	0.0100,	0.0000},
            {0.0000,	0.0000,	0.0040,	0.0040,	1.0000}
    };
    private static final double[][] matrizEsperadaTeste =  {{646.2265,810.9525,1077.537,248.266,23.197}, {528.9935,823.8795,1024.837,237.392,35.727}, {1581.608,1370.124,2123.226,509.355,48.279}, {17.732,16.476,24.764,5.915,0.601}, {790.44,457.568,1107.636,272.072,16.196}};
    private static final double[] arrayEsperadoTeste =  {652.7405,757.1215,1261.841,15.169,27.128};

    // Array medias para o periodo do dia 2020-04-01 a 2020-04-10 do ficheiro de exemplo
    private static final double[] MEDIAS = {6607 / 9.0, 7221 / 9.0, 10101 / 9.0, 2256 / 9.0, 248 / 9.0};

    // Testes para a decomposição LU
    public static final double [][] Teste1 = {
            { 1, -2, 3 },
            { 0, -1, 4 },
            { -2, 2, 0 }
    };

    public static final double [][] Teste1MatrizL = {
            { 1, 0, 0 },
            { 0, -1, 0 },
            { -2, -2, -2 }
    };

    public static final double [][] Teste1MatrizU = {
            { 1, -2, 3 },
            { 0, 1, -4 },
            { 0, 0, 1 }
    };

    public static final double [][] Teste1MatrizLInversa = {
            { 1, 0, 0 },
            { 0, -1, 0 },
            { -1, 1, -0.5 }
    };

    public static final double [][] Teste1MatrizUInversa = {
            { 1, 2, 5 },
            { 0, 1, 4 },
            { 0, 0, 1 }
    };

    public static final double [][] Teste1Inversa = {
            { -4, 3, -2.5 },
            { -4, 3, -2 },
            { -1, 1, -0.5 }
    };

    public static final boolean Teste1Flag = true;


    public static final double [][] Teste2= {
            { 60, 30, 20 },
            { 30, 20, 15 },
            { 20, 15, 12 }
    };

    public static final double [][] Teste2MatrizL = {
            { 60, 0, 0 },
            { 30, 5, 0 },
            { 20, 5, 0.33333333}
    };

    public static final double [][] Teste2MatrizU = {
            { 1, 0.5, 0.33333333},
            { 0, 1, 1 },
            { 0, 0, 1 }
    };

    public static final double [][] Teste2MatrizLInversa = {
            {0.016666666666666666666, 0, 0 },
            { -0.1, 0.2, 0 },
            { 0.5, -3.0, 3.0}
    };

    public static final double [][] Teste2MatrizUInversa = {
            { 1, -0.5, 0.1666666666667},
            { 0, 1, -1 },
            { 0, 0, 1 }
    };

    public static final double [][] Teste2Inversa = {
            { 0.15, -0.6, 0.5},
            { -0.6, 3.2, -3 },
            { 0.5, -3, 3 }
    };

    public static final boolean Teste2Flag = true;


    public static final double [][] Teste3 = {
            { 2, 4, -6 },
            { 1, 5, 3 },
            { 1, 3, 2 }
    };

    public static final double [][] Teste3MatrizL = {
            { 2, 0, 0 },
            { 1, 3, 0 },
            { 1, 1, 3 }
    };

    public static final double [][] Teste3MatrizU = {
            { 1, 2, -3 },
            { 0, 1, 2 },
            { 0, 0, 1 }
    };

    public static final double [][] Teste3MatrizLInversa = {
            { 0.5, 0, 0 },
            { -0.1666667, 0.333333, 0 },
            { -0.1111111, -0.111111, 0.333333 }
    };

    public static final double [][] Teste3MatrizUInversa = {
            { 1, -2, 7 },
            { 0, 1, -2 },
            { 0, 0, 1 }
    };

    public static final double [][] Teste3Inversa = {
            {0.0555555555555555556	,-1.4444444444444444443, 2.3333333333333333333},
            {0.05555555555555555554, 0.55555555555555555555, -0.66666666666666666666},
            {-0.1111111111111111111,-0.11111111111111111111, 0.33333333333333333333}
    };

    public static final boolean Teste3Flag = true;

    public static final double [][] Teste4 = {
            { 0.0005, -0.049, -0.005, -0.001 },
            { -0.0005, 0.05, -0.005, -0.018 },
            { 0, -0.0009, 0.02, -0.02 },
            {0, -0.0001, -0.0085, 0.053}
    };

    public static final double [][] Teste4MatrizL = {
            { 0.0005, 0, 0, 0 },
            { -0.0005, 0.001, 0, 0 },
            { 0, -0.0009, 0.011, 0 },
            {0, -0.0001, -0.0095, 0.01905909}
    };

    public static final double [][] Teste4MatrizU = {
            { 1, -98, -10, -2 },
            { 0, 1, -10, -19 },
            { 0, 0, 1, -3.37272727},
            {0, 0, 0, 1}
    };

    public static final double [][] Teste4MatrizLInversa = {
            { 2000, 0, 0, 0 },
            { 1000, 1000, 0, 0 },
            { 81.818181818181818181, 81.818181818181818181, 90.90909090909090909, 0 },
            {46.029098308089592562, 46.029098308089592562, 45.313620096046749154, 52.468402216475183232}
    };

    public static final double [][] Teste4MatrizUInversa = {
            { 1, 98, 990, 5202.9999973},
            { 0, 1, 10, 52.7272727},
            { 0, 0, 1, 3.37272727},
            {0, 0, 0, 1}
    };

    public static final double [][] Teste4Inversa = {
            {420489.3983727116 ,418489.3983727116,325766.7652373845,272993.09659065574},
            {4245.170636807567 ,4245.170636807567, 3298.3545129193662, 2766.5157518013716},
            {237.06177689538646, 237.06177689538646, 243.73957310944778, 176.9616109688343},
            {46.029098308089594, 46.029098308089594, 45.31362009604675, 52.468402216475184}
    };

    public static final boolean Teste4Flag = true;

    public static final int[][] MATRIZ_DADOS = {
            {10170850, 9034, 6706, 1533, 209},
            {10171394, 9886, 7764, 1778, 246},
            {10172111, 10524, 8839, 2029, 266},
            {10173004, 11278, 9923, 2296, 295},
            {10173788, 11730, 11022, 2566, 311},
            {10174230, 12442, 12202, 2837, 345},
            {10175255, 13141, 13413, 3082, 380},
            {10176055, 13956, 14586, 3323, 409},
            {10176830, 15472, 15765, 3549, 435},
            {10177673, 15987, 16940, 3782, 470},
            {10178305, 16585, 18117, 4010, 504},
            {10179051, 16934, 19304, 4198, 535},
            {10179498, 17448, 20531, 4416, 567},
            {10180203, 18091, 21731, 4624, 599},
            {10180895, 18841, 23033, 4853, 629},
            {10181702, 19022, 24317, 5075, 657},
            {10183203, 19685, 25570, 5303, 687},
            {10183713, 20206, 26813, 5527, 714},
            {10184305, 20863, 28021, 5742, 735}
    };

    public static final String[] MATRIZ_DATAS = {
            "2020-04-01",
            "2020-04-02",
            "2020-04-03",
            "2020-04-04",
            "2020-04-05",
            "2020-04-06",
            "2020-04-07",
            "2020-04-08",
            "2020-04-09",
            "2020-04-10",
            "2020-04-11",
            "2020-04-12",
            "2020-04-13",
            "2020-04-14",
            "2020-04-15",
            "2020-04-16",
            "2020-04-17",
            "2020-04-18",
            "2020-04-19",
            "2020-04-20"
    };

    public static final int[] INDICES = {1,19};

    public static final int[] INDICES_MENSAL = {0,61};


    public static final int[][] MATRIZ_BALANCO_ESPERADO = {
            {544, 852, 1058, 245, 37},
            {717, 638, 1075, 251, 20},
            {893, 754, 1084, 267, 29},
            {784, 452, 1099, 270, 16},
            {442, 712, 1180, 271, 34},
            {1025, 699, 1211, 245, 35},
            {800, 815, 1173, 241, 29},
            {775, 1516, 1179, 226, 26},
            {843, 515, 1175, 233, 35},
            {632, 598, 1177, 228, 34},
            {746, 349, 1187, 188, 31},
            {447, 514, 1227, 218, 32},
            {705, 643, 1200, 208, 32},
            {692, 750, 1302, 229, 30},
            {807, 181, 1284, 222, 28},
            {1501, 663, 1253, 228, 30},
            {510, 521, 1243, 224, 27},
            {592, 657, 1208, 215, 21}
    };

    public static final int[][] MATRIZ_DADOS_ACC = {
            {10170223,8251,5664,1293,187},
            {10170850,9034,6706,1533,209},
            {10171394,9886,7764,1778,246},
            {10172111,10524,8839,2029,266},
            {10173004,11278,9923,2296,295},
            {10173788,11730,11022,2566,311},
            {10174230,12442,12202,2837,345},
            {10175255,13141,13413,3082,380},
            {10176055,13956,14586,3323,409},
            {10176830,15472,15765,3549,435},
            {10177673,15987,16940,3782,470},
            {10178305,16585,18117,4010,504},
            {10179051,16934,19304,4198,535},
            {10179498,17448,20531,4416,567},
            {10180203,18091,21731,4624,599},
            {10180895,18841,23033,4853,629},
            {10181702,19022,24317,5075,657},
            {10183203,19685,25570,5303,687},
            {10183713,20206,26813,5527,714},
            {10184305,20863,28021,5742,735},
            {10184651,21379,29193,5955,762},
            {10185160,21982,30339,6162,785},
            {10185797,22353,31434,6366,820},
            {10186540,22797,32502,6554,854},
            {10186719,23392,33542,6740,880},
            {10187375,23864,34547,6922,903},
            {10187891,24027,35542,7098,928},
            {10188541,24322,36478,7270,948},
            {10189052,24505,37458,7439,973},
            {10189649,25045,38426,7611,989},
            {10190016,25351,39318,7765,1007},
            {10190456,25190,40173,7915,1023},
            {10191045,25282,41029,8059,1043},
            {10191512,25524,41842,8202,1063},
            {10191673,25702,42660,8336,1074},
            {10191965,26182,43498,8472,1089},
            {10192146,26715,44372,8607,1105},
            {10192681,27268,45214,8734,1114},
            {10192984,27406,46029,8854,1126},
            {10192825,27581,46826,8966,1135},
            {10192916,27679,47631,9078,1144},
            {10193156,27913,48340,9191,1163},
            {10193332,28132,49032,9294,1175},
            {10193807,28319,49712,9402,1184},
            {10194335,28583,50385,9514,1190},
            {10194882,28810,51042,9629,1203},
            {10195019,29036,51691,9737,1218},
            {10195192,29209,52319,9842,1231},
            {10195289,29432,52948,9943,1247},
            {10195521,29660,53557,10036,1263},
            {10195738,29912,54165,10128,1277},
            {10195923,30200,54741,10212,1289},
            {10196184,30471,55291,10292,1302},
            {10196409,30623,55827,10370,1316},
            {10196633,30788,56358,10442,1330},
            {10196804,31007,56871,10513,1342},
            {10197025,31292,57381,10579,1356},
            {10197251,31596,57893,10644,1369},
            {10197500,31946,58422,10710,1383},
            {10197785,32203,58936,10773,1396},
            {10198053,32500,59410,10837,1410}
    };

    public static String[] MATRIZ_DATAS_DADA = {
        "2020-04-01",
        "2020-04-02",
        "2020-04-03",
        "2020-04-04",
        "2020-04-05",
        "2020-04-06",
        "2020-04-07",
        "2020-04-08",
        "2020-04-09",
        "2020-04-10",
        "2020-04-11",
        "2020-04-12",
        "2020-04-13",
        "2020-04-14",
        "2020-04-15",
        "2020-04-16",
        "2020-04-17",
        "2020-04-18",
        "2020-04-19",
        "2020-04-20",
        "2020-04-21",
        "2020-04-22",
        "2020-04-23",
        "2020-04-24",
        "2020-04-25",
        "2020-04-26",
        "2020-04-27",
        "2020-04-28",
        "2020-04-29",
        "2020-04-30",
        "2020-05-01",
        "2020-05-02",
        "2020-05-03",
        "2020-05-04",
        "2020-05-05",
        "2020-05-06",
        "2020-05-07",
        "2020-05-08",
        "2020-05-09",
        "2020-05-10",
        "2020-05-11",
        "2020-05-12",
        "2020-05-13",
        "2020-05-14",
        "2020-05-15",
        "2020-05-16",
        "2020-05-17",
        "2020-05-18",
        "2020-05-19",
        "2020-05-20",
        "2020-05-21",
        "2020-05-22",
        "2020-05-23",
        "2020-05-24",
        "2020-05-25",
        "2020-05-26",
        "2020-05-27",
        "2020-05-28",
        "2020-05-29",
        "2020-05-30",
        "2020-05-31",
        "2020-06-01"
    };



    public static void main(String[] args) throws FileNotFoundException
    {
        runTestes();
    }

    /**
     * Teste do método obterIndiceDoDia
     *
     * @return boleano representativo do sucesso ou insucesso do teste
     */
    private static boolean testeObterIndiceDoDia(String dia, String[] arrayDias, int indiceEsperado)
    {
        if (ProjetoLapr1.obterIndiceDoDia(dia, arrayDias) == indiceEsperado)
        {
            return true;
        } else
        {
            return false;
        }
    }


    private static boolean testeCalcularBalanco(int[][] matrizDados, int[][] matrizEsperada)
    {
        boolean pass = true;
        int[][] dados = ProjetoLapr1.calcularBalanco(matrizDados);


        for (int i = 0; i < matrizDados.length-1 && pass; i++) {
            for (int j = 0; j < matrizDados[i].length && pass; j++) {
                if (dados[i][j] - matrizEsperada[i][j] != 0) {
                    pass = false;
                }
            }
        }
        return pass;
    }

    private static boolean testeObterDadosPeriodoDiario(String periodo, int[][] matrizDados, String[] arrayDatas, int[] indices, int resolucao, int statusEsperado)
    {
        int[][] matrizDadosObtidos = new int[19][5];
        String [] arrayDiasObtidos = new String[19];
        boolean pass = true;

        int status = ProjetoLapr1.obterDadosPeriodo(periodo, matrizDados, arrayDatas, matrizDadosObtidos, arrayDiasObtidos, indices, 0);

        if(status != statusEsperado){
            pass = false;
        }

        return pass;

    }

    private static boolean testeObterDadosPeriodoSemanal(String periodo, int[][] matrizDados, String[] arrayDatas, int[] indices, int resolucao, int statusEsperado)
    {
        int[][] matrizDadosObtidos = new int[3][5];
        String [] arrayDiasObtidos = new String[3];
        boolean pass = true;

        int status = ProjetoLapr1.obterDadosPeriodo(periodo, matrizDados, arrayDatas, matrizDadosObtidos, arrayDiasObtidos, indices, 1);

        if(status != statusEsperado){
            pass = false;
        }

        return pass;

    }

    private static boolean testeObterDadosPeriodoMensal(String periodo, int[][] matrizDados, String[] arrayDatas, int[] indices, int resolucao, int statusEsperado)
    {
        int[][] matrizDadosObtidos = new int[2][5];
        String [] arrayDiasObtidos = new String[2];
        boolean pass = true;

        int status = ProjetoLapr1.obterDadosPeriodo(periodo, matrizDados, arrayDatas, matrizDadosObtidos, arrayDiasObtidos, indices, 2);

        if(status != statusEsperado){
            pass = false;
        }

        return pass;

    }

    private static boolean testeObterDiaInicialFinalDoPeriodo(String periodo, String[] didfEsperado)
    {
        String[] didf = ProjetoLapr1.obterDiaInicialFinalDoPeriodo(periodo);
        if ((didf[0].compareTo(didfEsperado[0]) == 0) && (didf[1].compareTo(didfEsperado[1]) == 0))
        {
            return true;
        } else
        {
            return false;
        }

    }

    private static boolean testeCalcularNovoPeriodo(String diaInicial, int nDias, String periodoEsperado)
    {

        if (ProjetoLapr1.calcularNovoPeriodo(diaInicial, nDias).compareTo(periodoEsperado) == 0)
        {
            return true;
        } else
        {
            return false;
        }

    }

    private static boolean testeCalcularNumeroDiasDoPeriodo(String periodo, int numeroDiasEsperado)
    {
        if (ProjetoLapr1.calcularNumeroDiasDoPeriodo(periodo) == numeroDiasEsperado)
        {
            return true;
        } else
        {
            return false;
        }
    }

    private static boolean testeCalcularMediasDoPeriodo(int[][] balanco, double[] mediasEsperadas)
    {
        double[] medias = ProjetoLapr1.calcularMediasDoPeriodo(balanco);
        boolean pass = true;
        int c = 0;

        while (pass && (c < medias.length))
        {
            if (medias[c] != mediasEsperadas[c])
            {
                pass = false;
            }
            c++;
        }

        return pass;
    }

    private static boolean testeCalcularDesviosPadraoDoPeriodo(double[] medias, int[][] balanco, double[] desviosEsperados)
    {
        double[] stdev = ProjetoLapr1.calcularDesviosPadraoDoPeriodo(medias, balanco);
        boolean pass = true;
        double tolerancia = 1.0e-3;
        int c = 0;

        while (pass && (c < stdev.length))
        {
            if (Math.abs(stdev[c] - desviosEsperados[c]) > tolerancia)
            {
                pass = false;
            }

            c++;
        }

        return pass;

    }

    private static boolean testeMultiplicarMatrizesAxB(double[][] mA, double[][] mB, double[][] mEsperado)  {
        double matrizCalculada[][] = ProjetoLapr1.multiplicarMatrizesAxB(mA, mB);
            double tolerancia = 1.0e-3;
            boolean pass = true;

            for (int i = 0; i < 5 && pass; i++) {
                for (int j = 0; j < 5 && pass; j++) {
                    if ((Math.abs(Math.abs(matrizCalculada[i][j]) - Math.abs(mEsperado[i][j])) > tolerancia)) {
                        pass = false;
                    }
                }
            }
        return pass;
    }

    private static boolean testeMultiplicarMatrizEArrayComoColuna(double mA[][], int arrB[], double[] arrayEsperadoTeste) {

        double[] matrizCalculada = ProjetoLapr1.multiplicarMatrizEArrayComoColuna(mA, arrB);
        double tolerancia = 1.0e-3;
        boolean pass = true;
        for (int i = 0; i < 5; i++) {
            if (Math.abs(Math.abs(matrizCalculada[i])-(Math.abs(arrayEsperadoTeste[i])) ) > tolerancia ) { pass = false; }
        }
        return pass;
    }

    private static boolean testeParaMatrizes(double [][] matrizDeTeste, double [][] matrizEsperada) {
        int n = matrizDeTeste.length;
        boolean pass = true;
        double tolerancia = 1.0e-4;
        int i = 0;
        int j = 0;
        while (pass && i < n) {
            while (pass && j < n) {
                if (Math.abs(matrizDeTeste[i][j] - matrizEsperada[i][j]) > tolerancia) {
                    pass = false;
                }
                j++;
            }
            i++;
        }
        return pass;
    }

    private static boolean testeDecomposicaoLU(double[][] matriz, double[][] expectedL, double[][] expectedU) {
        int n = matriz.length;
        double[][] matrizL = new double[n][n];
        double[][] matrizU = new double[n][n];

        ProjetoLapr1.decomposicaoLU(matriz, n, matrizL, matrizU);
        boolean matrizTesteL = testeParaMatrizes(matrizL, expectedL);
        boolean matrizTesteU = testeParaMatrizes(matrizU, expectedU);

        return matrizTesteL && matrizTesteU;
    }

    private static boolean testeCalcularInversaDaMatrizUeDaMatrizL(double[][] matrizL, double [][] matrizU, double[][] expectedLInv, double[][] expectedUInv) {
        int n = matrizL.length;
        double[][] matrizLInv = new double[n][n];
        double[][] matrizUInv = new double[n][n];

        ProjetoLapr1.calcularInversaDaMatrizUeDaMatrizL(matrizL, matrizU,n , matrizLInv, matrizUInv);
        boolean matrizTesteInvL = testeParaMatrizes(matrizLInv, expectedLInv);
        boolean matrizTesteInvU = testeParaMatrizes(matrizUInv, expectedUInv);

        return matrizTesteInvL && matrizTesteInvU;
    }

    private static boolean testeCalcularInversaMatrizInicial(double[][] matrizUInv, double[][] matrizLInv, double [][] expectedMatrix) {

        double [][] obtainedMatrix = ProjetoLapr1.calcularInversaMatrizInicial(matrizLInv, matrizUInv);

        return testeParaMatrizes(obtainedMatrix, expectedMatrix);
    }

    private static boolean testeVerificarSingularidadeMatriz(double [][] matriz, boolean expectedFlag) {
        int n = matriz.length;
        boolean pass = true;
        boolean obtainedFlag = ProjetoLapr1.verificarSingularidadeMatriz(matriz, n);
        if (obtainedFlag != expectedFlag) {
            pass = false;
        }
        return pass;
    }

    private static final double[][] subtrMatrizA = {{1,2,3},{5.8, 0.1, 9}};
    private static final double[][] subtrMatrizB = {{0.5, 0.1, 0.3},{1,0.15,4.5}};
    private static final double[][] subtrEsperado = {{0.5, 1.9, 2.7},{4.8, -0.05, 4.5}};

    public static boolean testeCalcularDiferencaMatrizesAeB(double[][] mA, double[][] mB, double[][] subtrEsperado) {
        boolean pass = true;
        double tolerancia = 1.0e-4;
        double[][] matrizCalculada = ProjetoLapr1.calcularDiferencaMatrizesAeB(subtrMatrizA,subtrMatrizB);

        for (int i = 0; i < mA.length && pass; i++) {
            for (int j = 0; j < mA[0].length && pass; j++) {
                if ((Math.abs(Math.abs(matrizCalculada[i][j]) - Math.abs(subtrEsperado[i][j])) > tolerancia)) {
                    pass = false;
                }
            }
        }
        return pass;
    }

    private static final String[] datas = {"2020-12-29", "2021-12-30", "2021-12-31", null, null};
    private static final String dataFutura = "2022-05-01";
    private static final int indiceEsperado = 2;
    private static final String[] datas1 = {"27-12-2021", "28-12-2021", "29-12-2021", "30-12-2021", "31-12-2021"};
    private static final String dataFutura1 = "2022-12-31";
    private static final int indiceEsperado1 = 2;
    private static final String[] datas2 = {"29-12-2021", "30-12-2021", "31-12-2021", null, null};
    private static final String dataFutura2 = "2021-12-29";
    private static final int indiceEsperado2 = 0;
    private static final String[] datas3 = {"29-12-2021", "30-12-2021", "31-12-2021", null, null};
    private static final String dataFutura3 = "1990-06-03";
    private static final int indiceEsperado3 = -1;
    private static final String[] datas4 = {"29-12-2021", "30-12-2021" , "31-12-2021", null, null};
    private static final String dataFutura4 = "2021-12-31";
    private static final int indiceEsperado4 = 1;



    public static boolean testeEncontrarVesperaOuDataMaisProxima(String dataFutura, String[] arrayDatasTotais, int indiceEsperado) {
        boolean pass = true;
        int indiceObtido = ProjetoLapr1.encontrarVesperaOuDataMaisProximaVersao2(dataFutura, arrayDatasTotais);
        if (indiceObtido != indiceEsperado){pass=false;}
        return pass;}

    public static boolean testeObterIndiceDaUltimaLinhaPreenchidaDeArray(String[] datas, int indiceEsperadoUltimaLinha){
        boolean pass = true;
        int indiceObtido = ProjetoLapr1.obterIndiceDaUltimaLinhaPreenchidaDeArray(datas);
        if (indiceObtido != indiceEsperadoUltimaLinha ) {pass=false;}
        return pass;}

    private static final String[] dias = {"29-12-2021", "30-12-2021", "31-12-2021", null, null};
    private static final String dia = "29-12-2021";
    private static final int indiceDiaEsperado = 0;
    private static final int indiceEsperadoUltimaLinha=2;
    private static final String[] dias1 = {"29-12-2021", "30-12-2021", "31-12-2021", null, null};
    private static final String dia1 = "31-12-2021";
    private static final int indiceDiaEsperado1 = 2;
    private static final int indiceEsperadoUltimaLinha1=2;
    private static final String[] dias2 = {"29-12-2021", "30-12-2021", "31-12-2021", null, null};
    private static final String dia2 = "01-01-2022";
    private static final int indiceDiaEsperado2 = -1;
    private static final int indiceEsperadoUltimaLinha2=2;
    private static final String[] dias3 = {"27-12-2021", "28-12-2021", "29-12-2021", "30-12-2021", "31-12-2021"};
    private static final String dia3 = "06-01-2022";
    private static final int indiceDiaEsperado3 = -1;
    private static final int indiceEsperadoUltimaLinha3=4;
    private static final String[] dias4 = {null, null, null, null, null};
    private static final String dia4 = "06-01-2022";
    private static final int indiceDiaEsperado4 = -1;
    private static final int indiceEsperadoUltimaLinha4=-1;

    //FIXME existe um metodo exatamente com o mesmo nome deste só muda o "upper case" de uma das letras
    public static boolean testecalcularNumeroDiasDoPeriodo(String periodo, int nrDiasEsperado) {
        boolean pass = true;
        int nrDiasObtido = ProjetoLapr1.calcularNumeroDiasDoPeriodo(periodo);
        if (nrDiasObtido != nrDiasEsperado){pass=false;}
        return pass;}

    public static final int MAX_DIAS = 912; // aproximadamente dois anos
    public static final int NUMERO_PARAMETROS = 5;
    public static final String nomeDoFicheiroAcum = "registoAcumuladoCasoEstudo.csv";
    public static final int valorEsperadoAcum = 93;
    public static final int escolhaSubmenuFicheiroAcum = 1;
    public static boolean validacaoDaLeituraDoFicheiroDeDadosAcum(String nomeDoFicheiroAcum, int valorEsperadoAcum) throws FileNotFoundException {
        String[] arr = new String[MAX_DIAS];
        int[][] matriz = new int[MAX_DIAS][NUMERO_PARAMETROS];
        int valorObtido = ProjetoLapr1.lerDadosDoFicheiro(arr, matriz, escolhaSubmenuFicheiroAcum, nomeDoFicheiroAcum);
        return valorEsperadoAcum == valorObtido;
    }

    public static final String nomeDoFicheiroTot = "TotaisSemJaneiro.csv";
    public static final int valorEsperadoTot = 426;
    public static final int escolhaSubmenuFicheiroTot = 2;
    public static boolean validacaoDaLeituraDoFicheiroDeDadosTot(String nomeDoFicheiroTot, int valorEsperadoTot) throws FileNotFoundException {
        String[] arr = new String[MAX_DIAS];
        int[][] matriz = new int[MAX_DIAS][NUMERO_PARAMETROS];
        int valorObtido = ProjetoLapr1.lerDadosDoFicheiro(arr, matriz, escolhaSubmenuFicheiroTot, nomeDoFicheiroTot);
        return valorEsperadoTot == valorObtido;
    }

    public static final String nomeDoFicheiroMarkov = "Markov.txt";
    public static final double[][] valorEsperadoMarkov = {
        {0.9995, 0.03, 0.002, 0.001, 0},
        {0.0005, 0.96, 0.004, 0.015, 0},
        {0, 0.007, 0.98, 0.02, 0},
        {0, 0.003, 0.01, 0.95, 0},
        {0, 0, 0.004, 0.014, 1}
    };
    public static boolean validacaoDaLeituraDoFicheiroDeDadosMarkov(String nomeDoFicheiroMarkov, double[][] valorEsperadoMarkov) throws FileNotFoundException {
        boolean validacao = true;
        double[][] valorObtido = ProjetoLapr1.lerDadosDoFicheiroMarkov(valorEsperadoMarkov, nomeDoFicheiroMarkov);
        boolean pass = testeParaMatrizes(valorObtido, valorEsperadoMarkov);
        return pass == validacao;
    }

    private static boolean testeValidarData(String data, int statusEsperado)
    {
        boolean pass = false;
        if (ProjetoLapr1.validarData(data) == statusEsperado)
        {
            pass = true;
        }

        return pass;
    }

    private static boolean testeValidarPeriodo(String periodo, boolean booleanoEsperado)
    {
        boolean pass = false;
        if(ProjetoLapr1.validarPeriodo(periodo) == booleanoEsperado){
            pass = true;
        };
        return pass;

    }

    private static String[] optsTotal = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20"};
    private static String[] optsSemPrevisoes = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16"};
    private static String[] optsSoPrevisoes = {"1","2","3","4","5"};
    private static boolean testeObterNumeroOpcoes(String[] opts, int nEsperado)
    {
        boolean pass = false;

        if (ProjetoLapr1.obterNumeroOpcoes(opts, ProjetoLapr1.MODO_TOTAL, ProjetoLapr1.MODO_SEM_PREVISOES, ProjetoLapr1.MODO_APENAS_PREVISOES) == nEsperado)
        {
            pass = true;
        }

        return pass;
    }

    private static int[][] matA = {{5,4},{3,2}};
    private static int[][] matB = {{1,2},{3,4}};
    private static boolean testeSubtrairMatrizes(int[][] m1, int[][] m2, int[][] mEsperada)
    {
        boolean pass = true;
        int[][] mCalculada = ProjetoLapr1.subtrairMatrizes(m1, m2);

        for (int i = 0; i < m2.length && pass; i++)
        {
            for (int j = 0; j < m2[0].length && pass; j++)
            {
                if (mCalculada[i][j] != mEsperada[i][j] )
                {
                    pass = false;
                }

            }

        }
        return pass;

    }

    private static boolean testeObterColunasDesejadas(int modo, int[] vetorEsperado)
    {
        boolean pass = true;
        int[] colunasCalculadas = ProjetoLapr1.obterColunasDesejadas(modo);

        for (int i = 0; i < colunasCalculadas.length; i++)
        {
            if(colunasCalculadas[i] != vetorEsperado[i])
            {
                pass = false;
            }
        }
        return pass;
    }

    private static boolean testeRecalcularPeriodoAnaliseNovosCasos(String[] diDf, String[] datas, int res, boolean booleanoEsperado)
    {

        boolean booleanoCalculado = ProjetoLapr1.recalcularPeriodoAnaliseNovosCasos(diDf, datas, res);

        return booleanoCalculado == booleanoEsperado;

    }

    private static boolean testeCalcularNumeroDeLinhas(String periodo, String[] datas, int res, int linhasEsperadas)
    {
        int linhasCalculadas = ProjetoLapr1.calcularNumeroDeLinhas(periodo, datas, res);

        return linhasCalculadas == linhasEsperadas;

    }

    private static boolean testeFormatarData(String data, String dataEsperada)
    {
        String dataCalculada = ProjetoLapr1.formatarData(data);
        return dataCalculada.equals(dataEsperada);

    }


    /**
     * executa todos os testes
     */
    public static void runTestes() throws FileNotFoundException {
        //System.out.println("soma_dois_numeros: " + (test_soma_dois_numeros(1, 2, 3) ? "OK" : "NOT OK" + "\n"));


        System.out.println("calcularNumeroDiasDoPeriodo: " + (testecalcularNumeroDiasDoPeriodo("2020-01-01/" + "2020-01-01", 1) ? "OK" : "NOT OK" + "\n"));
        System.out.println("calcularNumeroDiasDoPeriodo: " + (testecalcularNumeroDiasDoPeriodo("2020-01-01/" + "2020-01-02", 2) ? "OK" : "NOT OK" + "\n"));
        System.out.println("calcularNumeroDiasDoPeriodo: " + (testecalcularNumeroDiasDoPeriodo("2020-01-01/" + "2019-12-31", 0) ? "OK" : "NOT OK" + "\n"));
        System.out.println("calcularNumeroDiasDoPeriodo: " + (testecalcularNumeroDiasDoPeriodo("2020-01-01/" + "2020-01-15", 15) ? "OK" : "NOT OK" + "\n"));
        System.out.println("calcularNumeroDiasDoPeriodo: " + (testecalcularNumeroDiasDoPeriodo("2020-01-01/" + "2019-12-30", -1) ? "OK" : "NOT OK" + "\n"));
        System.out.println("calcularNumeroDiasDoPeriodo: " + (testecalcularNumeroDiasDoPeriodo("2020-01-01/" + "2019-12-29", -2) ? "OK" : "NOT OK" + "\n"));
        System.out.println("ObterIndiceDaUltimaLinhaPreenchidaDeArray: " + (testeObterIndiceDaUltimaLinhaPreenchidaDeArray(dias, indiceEsperadoUltimaLinha) ? "OK" : "NOT OK" + "\n"));
        System.out.println("ObterIndiceDaUltimaLinhaPreenchidaDeArray: " + (testeObterIndiceDaUltimaLinhaPreenchidaDeArray(dias1, indiceEsperadoUltimaLinha1) ? "OK" : "NOT OK" + "\n"));
        System.out.println("ObterIndiceDaUltimaLinhaPreenchidaDeArray: " + (testeObterIndiceDaUltimaLinhaPreenchidaDeArray(dias2, indiceEsperadoUltimaLinha2) ? "OK" : "NOT OK" + "\n"));
        System.out.println("ObterIndiceDaUltimaLinhaPreenchidaDeArray: " + (testeObterIndiceDaUltimaLinhaPreenchidaDeArray(dias3, indiceEsperadoUltimaLinha3) ? "OK" : "NOT OK" + "\n"));
        System.out.println("ObterIndiceDaUltimaLinhaPreenchidaDeArray: " + (testeObterIndiceDaUltimaLinhaPreenchidaDeArray(dias4, indiceEsperadoUltimaLinha4) ? "OK" : "NOT OK" + "\n"));
        System.out.println("obterIndiceDoDia: " + (testeObterIndiceDoDia("2020-04-04", ARRAY_DIAS, 3) ? "OK" : "NOT OK" + "\n"));
        System.out.println("obterIndiceDoDia: " + (testeObterIndiceDoDia(dia, dias, indiceDiaEsperado) ? "OK" : "NOT OK" + "\n"));
        System.out.println("obterIndiceDoDia: " + (testeObterIndiceDoDia(dia1, dias1, indiceDiaEsperado1) ? "OK" : "NOT OK" + "\n"));
        System.out.println("obterIndiceDoDia: " + (testeObterIndiceDoDia(dia2, dias2, indiceDiaEsperado2) ? "OK" : "NOT OK" + "\n"));
        System.out.println("obterIndiceDoDia: " + (testeObterIndiceDoDia(dia3, dias3, indiceDiaEsperado3) ? "OK" : "NOT OK" + "\n"));
        System.out.println("obterDiaInicialFinalDoPeriodo: " + (testeObterDiaInicialFinalDoPeriodo("2020-05-04/2020-05-24", new String[]{"2020-05-04", "2020-05-24"}) ? "OK" : "NOT OK" + "\n"));
        System.out.println("calcularNovoPeriodo: " + (testeCalcularNovoPeriodo("2020-04-01", 15, "2020-04-01/2020-04-15") ? "OK" : "NOT OK" + "\n"));
        System.out.println("calcularNumeroDiasDoPeriodo: " + (testeCalcularNumeroDiasDoPeriodo("2020-05-01/2020-05-15", 15) ? "OK" : "NOT OK" + "\n"));
        System.out.println("calcularMediasDoPeriodo: " + (testeCalcularMediasDoPeriodo(BALANCO_DIARIO, new double[]{6607 / 9.0, 7221 / 9.0, 10101 / 9.0, 2256 / 9.0, 248 / 9.0}) ? "OK" : "NOT OK" + "\n"));
        System.out.println("calcularDesviosPadraoDoPeriodo: " + (testeCalcularDesviosPadraoDoPeriodo(MEDIAS, BALANCO_DIARIO, new double[]{167.199, 275.526, 59.453, 14.659, 6.784}) ? "OK": "NOT OK" + "\n"));
        System.out.println("MultiplicarMatrizesAxB: " + (testeMultiplicarMatrizesAxB(matrizAdoubleTeste, matrizBTeste, matrizEsperadaTeste) ? "OK" : "NOT OK" + "\n"));
        System.out.println("MultiplicarMatrizEArray: " + (testeMultiplicarMatrizEArrayComoColuna(matrizAdoubleTeste, arrayIntTeste, arrayEsperadoTeste) ? "OK" : "NOT OK" + "\n"));
        System.out.println("testeDecomposiçãoLU 1: "+ (testeDecomposicaoLU(Teste1, Teste1MatrizL, Teste1MatrizU) ? "OK" : "NOT OK" + "\n"));
        System.out.println("testeDecomposiçãoLU 2: "+ (testeDecomposicaoLU(Teste2, Teste2MatrizL, Teste2MatrizU) ? "OK" : "NOT OK" + "\n"));
        System.out.println("testeDecomposiçãoLU 3: "+ (testeDecomposicaoLU(Teste3, Teste3MatrizL, Teste3MatrizU) ? "OK" : "NOT OK" + "\n"));
        System.out.println("testeDecomposiçãoLU 4: "+ (testeDecomposicaoLU(Teste4, Teste4MatrizL, Teste4MatrizU) ? "OK" : "NOT OK" + "\n"));
        System.out.println("testeCalcularInversaDaMatrizUeDaMatrizL 1: "+(testeCalcularInversaDaMatrizUeDaMatrizL(Teste1MatrizL,Teste1MatrizU,Teste1MatrizLInversa,Teste1MatrizUInversa) ? "OK" : "NOT OK" + "\n"));
        System.out.println("testeCalcularInversaDaMatrizUeDaMatrizL 2: "+(testeCalcularInversaDaMatrizUeDaMatrizL(Teste2MatrizL,Teste2MatrizU,Teste2MatrizLInversa,Teste2MatrizUInversa) ? "OK" : "NOT OK" + "\n"));
        System.out.println("testeCalcularInversaDaMatrizUeDaMatrizL 3: "+(testeCalcularInversaDaMatrizUeDaMatrizL(Teste3MatrizL,Teste3MatrizU,Teste3MatrizLInversa,Teste3MatrizUInversa) ? "OK" : "NOT OK" + "\n"));
        System.out.println("testeCalcularInversaDaMatrizUeDaMatrizL 4: "+(testeCalcularInversaDaMatrizUeDaMatrizL(Teste4MatrizL,Teste4MatrizU,Teste4MatrizLInversa,Teste4MatrizUInversa) ? "OK" : "NOT OK" + "\n"));
        System.out.println("testeCalcularInversaMatrizInicial 1: " +(testeCalcularInversaMatrizInicial(Teste1MatrizUInversa, Teste1MatrizLInversa, Teste1Inversa) ? "OK" : "NOT OK" + "\n"));
        System.out.println("testeCalcularInversaMatrizInicial 2: " +(testeCalcularInversaMatrizInicial(Teste2MatrizUInversa, Teste2MatrizLInversa, Teste2Inversa) ? "OK" : "NOT OK" + "\n"));
        System.out.println("testeCalcularInversaMatrizInicial 3: " +(testeCalcularInversaMatrizInicial(Teste3MatrizUInversa, Teste3MatrizLInversa, Teste3Inversa) ? "OK" : "NOT OK" + "\n"));
        System.out.println("testeCalcularInversaMatrizInicial 4: " +(testeCalcularInversaMatrizInicial(Teste4MatrizUInversa, Teste4MatrizLInversa, Teste4Inversa) ? "OK" : "NOT OK" + "\n"));
        System.out.println("testeVerificarSingularidadeMatriz 1: " +(testeVerificarSingularidadeMatriz(Teste1MatrizL, Teste1Flag) ? "OK" : "NOT OK" + "\n"));
        System.out.println("testeVerificarSingularidadeMatriz 2: " +(testeVerificarSingularidadeMatriz(Teste2MatrizL, Teste2Flag) ? "OK" : "NOT OK" + "\n"));
        System.out.println("testeVerificarSingularidadeMatriz 3: " +(testeVerificarSingularidadeMatriz(Teste3MatrizL, Teste3Flag) ? "OK" : "NOT OK" + "\n"));
        System.out.println("testeVerificarSingularidadeMatriz 4: " +(testeVerificarSingularidadeMatriz(Teste4MatrizL, Teste4Flag) ? "OK" : "NOT OK" + "\n"));
        System.out.println("CalcularDiferencaMatrizesAeB: " + (testeCalcularDiferencaMatrizesAeB(subtrMatrizA, subtrMatrizB, subtrEsperado) ? "OK" : "NOT OK" + "\n"));
        System.out.println("EncontrarVesperaOuDataMaisProxima: " + (testeEncontrarVesperaOuDataMaisProxima(dataFutura, datas, indiceEsperado) ? "OK" : "NOT OK" + "\n"));
        System.out.println("EncontrarVesperaOuDataMaisProxima: " + (testeEncontrarVesperaOuDataMaisProxima(dataFutura1, datas, indiceEsperado1) ? "OK" : "NOT OK" + "\n"));
        System.out.println("EncontrarVesperaOuDataMaisProxima: " + (testeEncontrarVesperaOuDataMaisProxima(dataFutura2, datas, indiceEsperado2) ? "OK" : "NOT OK" + "\n"));
        System.out.println("EncontrarVesperaOuDataMaisProxima: " + (testeEncontrarVesperaOuDataMaisProxima(dataFutura3, datas, indiceEsperado3) ? "OK" : "NOT OK" + "\n"));
        System.out.println("EncontrarVesperaOuDataMaisProxima: " + (testeEncontrarVesperaOuDataMaisProxima(dataFutura4, datas, indiceEsperado4) ? "OK" : "NOT OK" + "\n"));
        System.out.println("CalcularBalanco: " + (testeCalcularBalanco(MATRIZ_DADOS, MATRIZ_BALANCO_ESPERADO) ? "OK" : "NOT OK" + "\n"));
        System.out.println("ObterDadosPeriodoDiario: " + (testeObterDadosPeriodoDiario("2020-04-02/2020-04-20", MATRIZ_DADOS_ACC, MATRIZ_DATAS_DADA, INDICES, 0, 0) ? "OK" : "NOT OK" + "\n"));
        System.out.println("ObterDadosPeriodoSemanal: " + (testeObterDadosPeriodoSemanal("2020-04-02/2020-04-20", MATRIZ_DADOS_ACC, MATRIZ_DATAS_DADA, INDICES, 1, 0) ? "OK" : "NOT OK" + "\n"));
        System.out.println("ObterDadosPeriodoMensal: " + (testeObterDadosPeriodoMensal("2020-04-01/2020-06-01", MATRIZ_DADOS_ACC, MATRIZ_DATAS_DADA, INDICES_MENSAL, 2, 0) ? "OK" : "NOT OK" + "\n"));
        System.out.println("validacaoDaLeituraDoFicheiroDeDadosAcum: " + (validacaoDaLeituraDoFicheiroDeDadosAcum(nomeDoFicheiroAcum,valorEsperadoAcum) ? "OK" : "NOT OK" + "\n"));
        System.out.println("validacaoDaLeituraDoFicheiroDeDadosTot: " + (validacaoDaLeituraDoFicheiroDeDadosTot(nomeDoFicheiroTot,valorEsperadoTot) ? "OK" : "NOT OK" + "\n"));
        System.out.println("validacaoDaLeituraDoFicheiroDeMatrizTransicao: " + (validacaoDaLeituraDoFicheiroDeDadosMarkov(nomeDoFicheiroMarkov,valorEsperadoMarkov) ? "OK" : "NOT OK" + "\n"));
        System.out.println("testeValidarData: " + (testeValidarData("01-01-2020", 0) ? "OK" : "NOT OK" + "\n"));
        System.out.println("testeValidarData: " + (testeValidarData("2020-01-05", -1) ? "OK" : "NOT OK" + "\n"));
        System.out.println("testeValidarPeriodo: " + (testeValidarPeriodo("2020-01-01/2020-05-01",true) ? "OK" : "NOT OK" + "\n"));
        System.out.println("testeValidarPeriodo: " + (testeValidarPeriodo("2020-05-01/2020-01-01",false) ? "OK" : "NOT OK" + "\n"));
        System.out.println("testeObterNumeroOpcoes: " + (testeObterNumeroOpcoes(optsTotal, 16) ? "OK" : "NOT OK" + "\n"));
        System.out.println("testeObterNumeroOpcoes: " + (testeObterNumeroOpcoes(optsSemPrevisoes, 14) ? "OK" : "NOT OK" + "\n"));
        System.out.println("testeObterNumeroOpcoes: " + (testeObterNumeroOpcoes(optsSoPrevisoes, 2) ? "OK" : "NOT OK" + "\n"));
        System.out.println("testeSubtrairMatrizes: " + (testeSubtrairMatrizes(matA, matB, new int[][] {{-4,-2},{0,2}}) ? "OK" : "NOT OK" + "\n"));
        System.out.println("testeObterColunasDesejadas: " + (testeObterColunasDesejadas(ProjetoLapr1.MODO_NAO_INTERATIVO, new int[] {1,1,1,1}) ? "OK" : "NOT OK" + "\n"));
        System.out.println("testeRecalcularPeriodoAnaliseNovosCasos: " + (testeRecalcularPeriodoAnaliseNovosCasos(new String[]{"2020-04-01", "2020-04-20"}, MATRIZ_DATAS, 0, false) ? "OK" : "NOT OK" + "\n"));
        System.out.println("testeCalcularNumeroDeLinhas: " + (testeCalcularNumeroDeLinhas("2020-04-01/2020-04-20", MATRIZ_DATAS, 0,20) ? "OK" : "NOT OK" + "\n"));
        System.out.println("testeCalcularNumeroDeLinhas: " + (testeCalcularNumeroDeLinhas("2020-04-01/2020-04-20", MATRIZ_DATAS, 1,3) ? "OK" : "NOT OK" + "\n"));
        System.out.println("testeCalcularNumeroDeLinhas: " + (testeCalcularNumeroDeLinhas("2020-04-01/2020-04-20", MATRIZ_DATAS, 2,0) ? "OK" : "NOT OK" + "\n"));
        System.out.println("testeFormatarData: " + (testeFormatarData("01-02-2020", "2020-02-01") ? "OK" : "NOT OK" + "\n"));
        System.out.println("testeFormatarData: " + (testeFormatarData("2020-02-01", "01-02-2020") ? "OK" : "NOT OK" + "\n"));

    }
}
