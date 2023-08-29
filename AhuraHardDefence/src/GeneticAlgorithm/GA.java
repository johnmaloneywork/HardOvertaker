package GeneticAlgorithm;

import ahooraDriver.MySensorModel;

import java.util.Arrays;

public class GA {

    private Population population;
    private int currentGeneration;
    private int currentDriver;
    private final int maxGenerations;
    private final Selection selection = new Selection();
    private final Mutation mutation = new Mutation();
    private final Crossover crossover = new Crossover();

    public GA(int maxGenerations){
        this.maxGenerations = maxGenerations;
    }

    public IndividualTest[] evolve(IndividualTest[] individuals){

        //AFTER EACH GENERATION IS COMPLETE
        System.out.println("\nGENERATION " + (GeneticDriverController.generationCounter - 1) + " FINISHED" +
                "\nEVOLVING POPULATION");

        Population population = new Population(40);

        System.out.println("\nADVANCING TOP 10 DRIVERS");
        //Returns best 10 drivers
        IndividualTest[] best10 = selection.bestTenDrivers(individuals);
        //System.out.println(Arrays.toString(best10));

        //Returns the 30 winners of tournament selection
        IndividualTest[] tournament30 = selection.tournamentSelection(individuals);
        //System.out.println(Arrays.toString(tournament30));

        IndividualTest[] crossover15 = new IndividualTest[15];
        int crossoverCounter = 0;

        System.out.println("PERFORMING CROSSOVER");

        //15 individuals' genes are crossed from the 30 tournament winners
        for (int i = 0; i < tournament30.length; i+=2) {
            IndividualTest individualTest = crossover.singlePointCrossover(individuals[i], individuals[i + 1]);
            crossover15[crossoverCounter] = individualTest;
            crossoverCounter++;
        }

        //The best 10 and crossed 15 are added onto the next generation
        IndividualTest[] nextGeneration = new IndividualTest[individuals.length];   //population.getPOPULATION_SIZE()];

        for (int i = 0; i < best10.length; i++){
            nextGeneration[i] = best10[i];
            //System.out.println(nextGeneration[i].getId());
        }

        //System.out.println(Arrays.toString(crossover15));

        for (int j = 0; j < crossover15.length; j++){
            nextGeneration[j + best10.length] = crossover15[j];
            //System.out.println(nextGeneration[j + best10.length].getId());
        }

        //The remaining 15 individuals in the next population are generated
        for (int k = crossover15.length + best10.length; k < population.getPOPULATION_SIZE(); k++){
//            double[] speedGenes = new double[2];
//            double[] opponentSensorGenes = new double[8];
//            double[] trackSensorGenes = new double[6];
            double[] allGenes = new double[16];
            IndividualTest individualTest = new IndividualTest(allGenes);
            nextGeneration[k] = individualTest;
            //System.out.println(individualTest.getId());
        }

        population.setChromosomes(nextGeneration);

        //Mutation occurs at rate of 2%
        mutation.otherMutation(nextGeneration);

        System.out.println("\nPOPULATION EVOLVED");
        System.out.println("\nNEXT GENERATION");
        System.out.println(Arrays.toString(nextGeneration));

        return nextGeneration;
    }

    public IndividualTest[] runFirstGeneration(){
        
        Population firstGeneration  = new Population(40);
        currentGeneration++;

        System.out.println("THIS IS THE FIRST GENERATION METHOD");
        System.out.println("\nGENERATION " + GeneticDriverController.generationCounter + " IS RUNNING");
        System.out.println("POPULATION CREATED");

        return firstGeneration.getChromosomes();
    }

    public IndividualTest[] runContinuedGeneration(){
        Population firstGeneration  = new Population(40);
        //Population continuedGeneration = new Population(10);
        IndividualTest driverOne = new IndividualTest();
        driverOne.setId("B5GfkstAZm");
        double[] driverOneGenes = {0.47744163338500145, 0.03199583032369324, 0.6253084336311459, 0.10944622028305706, 0.899611657869226, 0.2911707100224735,
                0.09064713587690176, 0.6588361925739753, 0.5478529184173143, 0.845375115184752, 0.3554079506363832, 0.035017729994906155, 0.4946104237347294,
                0.6995110031075025, 0.6619211301442367, 0.15029978139400213, 0.8458413575143383, 0.3538670522669821, 0.940618940839289, 0.3627518504063856,
                0.360647154511508, 0.5613109677244758, 0.6649718900751493, 0.3195040508575103, 0.7833128218548975, 0.6494356324196002};
        driverOne.setAllGenes(driverOneGenes);

        IndividualTest driverTwo = new IndividualTest();
        driverTwo.setId("LnQMol2T3F");
        double[] driverTwoGenes = {0.5867950959965765, 0.9025798849311639, 0.3266307265016316, 0.49588448086548975, 0.8549752730014795, 0.6291644196061675,
                0.6771306965507634, 0.3512100240267908, 0.7761419453603778, 0.6195230104077447, 0.007189999275472925, 0.7149092379017868, 0.5163226555114558,
                0.7354907723202989, 0.7807670462051811, 0.3241913832991825, 0.32453749966462997, 0.2776038183533599, 0.2853561043208752, 0.6216664989910804,
                0.4972337902255186, 0.14126847311233648, 0.2607386462137258, 0.315398229768041, 0.3564510157829094, 0.5635116133137549};
        driverTwo.setAllGenes(driverTwoGenes);

        IndividualTest driverThree = new IndividualTest();
        driverThree.setId("n1XhyWK8HT");
        double[] driverThreeGenes ={0.3971657844992137, 0.5708985766949055, 0.8222984350040592, 0.5404285909452157, 0.06015847576396227, 0.7865474619219813,
                0.7144141128937731, 0.16326636543231132, 0.07031505144075212, 0.4887899469164456, 0.8928761949294225, 0.7816236827067974, 0.6509834725543651,
                0.2972207133293837, 0.6481983944629007, 0.13594062861885736, 0.668224391362189, 0.1433065070873688, 0.06433649098041028, 0.1910586956325181,
                0.6408873882929695, 0.06094583915133456, 0.2977336869896725, 0.7293227086682859, 0.7946448859425129, 0.12432661555370195};
        driverThree.setAllGenes(driverThreeGenes);

        IndividualTest driverFour = new IndividualTest();
        driverFour.setId("FxpiBJeUp5");
        double[] driverFourGenes = {0.5731738249489635, 0.2851954210837955, 0.8882250184063463, 0.955083614263481, 0.11836297941942386, 0.4270607073389092,
                0.18149103151087798, 0.05396379681119834, 0.05255841520370452, 0.5378343047326464, 0.547138707584223, 0.9688612338886952, 0.39260996027390416,
                0.6669874327452303, 0.4896182115710316, 0.9726136949711492, 0.11315918022773175, 0.46303984730551384, 0.7962991264061426, 0.24627329782324447,
                0.9921905121365955, 0.7545915990687125, 0.674159808553282, 0.3433668691480447, 0.5524159515999111, 0.5283738133032354};
        driverFour.setAllGenes(driverFourGenes);

        IndividualTest driverFive = new IndividualTest();
        driverFive.setId("3dW4tqvv6V");
        double[] driverFiveGenes = {0.7084262461384876, 0.5013735297602081, 0.4421399505643939, 0.9326167301178957, 0.06291258504546238, 0.7707159504223531,
                0.9690976930432023, 0.9380832073908162, 0.3252808223232281, 0.7072120541711172, 0.8264616793730462, 0.2439688481083343, 0.36468153871944453,
                0.6366009173085, 0.34897092998279744, 0.9916903453743079, 0.15509708088712038, 0.9156377996573607, 0.23074364596121433, 0.6913370096954917,
                0.6030869392162714, 0.31094718942753796, 0.10600431684760991, 0.29665507245592504, 0.3182410986856983, 0.6947368826024404};
        driverFive.setAllGenes(driverFiveGenes);

        IndividualTest driverSix = new IndividualTest();
        driverSix.setId("SOVT7hkYSj");
        double[] driverSixGenes = {0.2723502379795616, 0.9093279245262855, 0.7495554962329039, 0.5802042626361459, 0.3299007156517739, 0.5108484626271097,
                0.07287801021620632, 0.290361048458457, 0.7720013299855073, 0.23660730476762848, 0.9112181451952683, 0.7311513093341352, 0.5586059729862635,
                0.8875205107174711, 0.8054676085400573, 0.675351607615051, 0.8472386699195527, 0.9433296363842145, 0.5675940354122297, 0.4360432466541909,
                0.6274177615115223, 0.7403820399563479, 0.0026414249555337665, 0.43547005289481777, 0.6642431196905275, 0.569055464112402};
        driverSix.setAllGenes(driverSixGenes);

        IndividualTest driverSeven = new IndividualTest();
        driverSeven.setId("xyx8HGIaFr");
        double[] driverSevenGenes = {0.3918126159228734, 0.3814237075351671, 0.40999689055197985, 0.1167597556862956, 0.175939241010441, 0.2739749548622019,
                0.6071688289466056, 0.88875694443985, 0.08648419817366537, 0.36421262587260905, 0.9479927494381462, 0.8424705529743464, 0.7958867777368669,
                0.5376660021053524, 0.37623487878165185, 0.07378569304837768, 0.36878493345071084, 0.37184529234070984, 0.6191443227391177, 0.36166533460587247,
                0.8950468493186039, 0.10273517365129214, 0.3301763046288628, 0.46306469609382717, 0.8747679911051519, 0.5794064125449859};
        driverSeven.setAllGenes(driverSevenGenes);

        IndividualTest driverEight = new IndividualTest();
        driverEight.setId("loc34iUpYt");
        double[] driverEightGenes = {0.5195568214990905, 0.9822005918263285, 0.5484234189099529, 0.2723209445456465, 0.7389972371501632, 0.2651801534584005,
                0.24427156596167776, 0.2266873965945383, 0.9681520300002944, 0.0729587363479055, 0.509288529637471, 0.21145656176127847, 0.37395509718011266,
                0.36393252301079215, 0.4136674804438154, 0.7841405886364293, 0.5445046214988961, 0.18488755184753103, 0.5687907053043229, 0.9253650401276194,
                0.8664085957894514, 0.734183987627392, 0.4511830395908212, 0.812550713193169, 0.3242541432580601, 0.3851581065608495};
        driverEight.setAllGenes(driverEightGenes);

        IndividualTest driverNine = new IndividualTest();
        driverNine.setId("FKjNjmc6gr");
        double[] driverNineGenes = {0.4471520087919796, 0.6700564119310024, 0.939240405587479, 0.5209431673799595, 0.02608656349951466, 0.004740903899273952,
                0.01571216992218416, 0.19068591714503935, 0.6629051996348246, 0.14233194315995712, 0.7949636451465324, 0.6009187655357484, 0.4872550434549242,
                0.6460916308514819, 0.3473712893362769, 0.9825514500408333, 0.8731419203112007, 0.7776015139501784, 0.4191536852200569, 0.38699074687052537,
                0.44203329002812886, 0.7035669097976841, 0.0030275526221950066, 0.4218051235488782, 0.04693052421046895, 0.986149376813884};
        driverNine.setAllGenes(driverNineGenes);

        IndividualTest driverTen = new IndividualTest();
        driverTen.setId("2xYK4GvWNC");
        double[] driverTenGenes = {0.9929917643593686, 0.5709052221122044, 0.6419531696926667, 0.07377371744613359, 0.4620018847511309, 0.43434254631126934,
                0.12035664877715169, 0.41750669519127015, 0.034691531880545856, 0.5826201459483574, 0.29111347538401733, 0.2696896402400494, 0.348912908755562,
                0.05479026602226267, 0.6898055138919266, 0.49302131113161285, 0.7307485793109223, 0.00640251244042489, 0.4718086165750349, 0.9023648786693302,
                0.14729864993825592, 0.824549318050274, 0.44593841792814404, 0.8682485141313828, 0.9736891670854975, 0.14289485752079545};
        driverTen.setAllGenes(driverTenGenes);

        IndividualTest driverEleven = new IndividualTest();
        driverEleven.setId("kHtVw6PyWu");
        double[] driverElevenGenes = {0.06100143472490638, 0.018884764598125714, 0.6393524603667932, 0.789085169766231, 0.9028512096406325,
                0.4837642812345818, 0.4652445377468465, 0.48130089837720025, 0.7299372774732084, 0.5974770715646934, 0.8521461281121072,
                0.48919776174798923, 0.33740212540666736, 0.6461919078508054, 0.07896397799700561, 0.29842023264850803, 0.7229960820143817,
                0.5358069905699017, 0.3452642619016423, 0.8406787969032452, 0.961175713160256, 0.201819002906915, 0.5099364657231611,
                0.9703368930355076, 0.7442374024573828, 0.667741349625141};
        driverEleven.setAllGenes(driverElevenGenes);

        IndividualTest driverTwelve = new IndividualTest();
        driverTwelve.setId("otPhrYA3Gm");
        double[] driverTwelveGenes = {0.6451124386028122, 0.9461016614273324, 0.007604767243934107, 0.737782206768228, 0.8344215087728788, 0.08621750759543856,
                0.8012663190916363, 0.2764155281420785, 0.48688023920028634, 0.1672933691993883, 0.9590298758248174, 0.8015764130547594, 0.029273669586646234,
                0.027545935747137262, 0.5515242199560808, 0.05655903417677621, 0.52369128781997, 0.9488888392080673, 0.630889469138809, 0.08838307627490738,
                0.9586491674579898, 0.4348522492721755, 0.8242238322130705, 0.7212304055055706, 0.395509952694296, 0.3577853008818517};
        driverTwelve.setAllGenes(driverTwelveGenes);

        IndividualTest driverThirteen = new IndividualTest();
        driverThirteen.setId("3IZTwUjEoi");
        double[] driverThirteenGenes = {0.41135485669835814, 0.4122748413195022, 0.6308141576031476, 0.1566780982982987, 0.576823340501418, 0.9986009101362293,
                0.4148763028783923, 0.677775709761059, 0.3842826022431849, 0.5439197526827471, 0.8255818377402906, 0.7777369638311185, 0.17165904157515577,
                0.9695356369621326, 0.8294682917087554, 0.7621486905410708, 0.7712427271916705, 0.3691246767399078, 0.5666100280655665, 0.6994200515661181,
                0.08361782219926905, 0.2211017706277959, 0.4964067430303015, 0.6345312470314189, 0.9048747753330828, 0.5964340313091517};
        driverThirteen.setAllGenes(driverThirteenGenes);

        IndividualTest driverFourteen = new IndividualTest();
        driverFourteen.setId("Sd7ZL4kJsI");
        double[] driverFourteenGenes = {0.44143672729910266, 0.24676971968822559, 0.3906605939472887, 0.9319824213347168, 0.7323247805168593, 0.31535412042210065,
                0.6238568202796111, 0.13854027644079203, 0.6762088053632194, 0.9702967474781156, 0.9763099977799412, 0.8920531420138347, 0.34874520504080386,
                0.8142042830036529, 0.3256267398932363, 0.721977301217719, 0.7784947280498778, 0.3205415155715915, 0.3765944773849965, 0.5690218965379648,
                0.3167676472225395, 0.1585406328200435, 0.7318059361171109, 0.6872604078534958, 0.9524313381055619, 0.8431770075688593};
        driverFourteen.setAllGenes(driverFourteenGenes);

        IndividualTest driverFifteen = new IndividualTest();
        driverFifteen.setId("rj17cDKhyW");
        double[] driverFifteenGenes = {0.5942839083797279, 0.4368535353943489, 0.8427340521319497, 0.6897895535474802, 0.7100239566523463, 0.8926657359610788,
                0.45072055274030043, 0.5596587270969531, 0.27435849950883373, 0.3390609486379873, 0.9608299267980708, 0.2510981787780101, 0.22797827007299887,
                0.644311086131763, 0.9164491400801427, 0.7348030776888309, 0.8564142123889411, 0.43167953357704747, 0.2786529759138685, 0.4691164258267737,
                0.7358733315838474, 0.41948440671663456, 0.8819318780510887, 0.6022372699259887, 0.8052504354750357, 0.019882945099109706};
        driverFifteen.setAllGenes(driverFifteenGenes);

        IndividualTest driverSixteen = new IndividualTest();
        driverSixteen.setId("VgtcMCDiCj");
        double[] driverSixteenGenes = {0.5658547026298357, 0.8729704571631224, 0.2116127760577774, 0.313592161383742, 0.45436553289915593, 0.09659470685273941,
                0.9939391896703759, 0.9402374434025328, 0.8926350743049019, 0.8380991120020391, 0.9400534056454974, 0.3125730891242249, 0.012441693949647337,
                0.5450472540908021, 0.015943349554035535, 0.5691009059883234, 0.4884930138267992, 0.22579511124625673, 0.4002729141344893, 0.25285213243609217,
                0.48624269818276544, 0.8186545699359126, 0.24304171804246966, 0.5771217168121938, 0.7814190860468302, 0.17332848430930936};
        driverSixteen.setAllGenes(driverSixteenGenes);

        IndividualTest driverSeventeen = new IndividualTest();
        driverSeventeen.setId("iw1eCDX4Z0");
        double[] driverSeventeenGenes = {0.8301455946234961, 0.900724393010001, 0.7574932721674098, 0.40245636561567666, 0.4650562612257818, 0.10170240298042499,
                0.15178708487495096, 0.48097004938573584, 0.18015646031703703, 0.327098376781497, 0.6044363130943117, 0.8376290875580936, 0.0540824728065783,
                0.2870859219233223, 0.7412901607211609, 0.3320971396454272, 0.4542515555767569, 0.43171503555498647, 0.5192159268663458, 0.8449011120087722,
                0.3778320038748634, 0.5739640452399566, 0.09726128495543596, 0.8618922119793363, 0.4781354718591081, 0.16227163805211242};
        driverSeventeen.setAllGenes(driverSeventeenGenes);

        IndividualTest driverEighteen = new IndividualTest();
        driverEighteen.setId("MmpcYTa85a");
        double[] driverEighteenGenes = {0.2391059347281751, 0.14152246351694486, 0.3489666956588645, 0.07951576965727458, 0.738560780348995, 0.41606208694107427,
                0.5395395458888573, 0.19742643398620474, 0.5355933095425416, 0.0928249700777487, 0.9925541875318602, 0.1794660862265699, 0.6502462694519521,
                0.25223846231553415, 0.11721472569270674, 0.7748817734173559, 0.5358278503463957, 0.8437982751410779, 0.15351448416985858, 0.9714422677319845,
                0.7884575777429794, 0.06365782971495759, 0.3854196132244483, 0.5677396468633087, 0.2681620058705031, 0.511816831855176};
        driverEighteen.setAllGenes(driverEighteenGenes);

        IndividualTest driverNineteen = new IndividualTest();
        driverNineteen.setId("G79CwUI8Nj");
        double[] driverNineteenGenes = {0.35470755714417856, 0.9501896002842944, 0.5396810459020652, 0.8705498640522558, 0.4052523444720457, 0.6895886051801327,
                0.2862728957563293, 0.2622858532789065, 0.17812354546951759, 0.08312104647293472, 0.7709557355656295, 0.7007034254825143, 0.46152691749829977,
                0.5152033231364226, 0.3726867166679497, 0.5584588487283397, 0.5466821324464134, 0.5956807345724848, 0.04138121939948347, 0.09895522476806895,
                0.7117416546799635, 0.49128955571192756, 0.8314728880738322, 0.10142228341833437, 0.24658904255440928, 0.7003189654366995};
        driverNineteen.setAllGenes(driverNineteenGenes);

        IndividualTest driverTwenty = new IndividualTest();
        driverTwenty.setId("m4HasoOSpi");
        double[] driverTwentyGenes = {0.004375203184085241, 0.2938295061197814, 0.0581131868476672, 0.06183866174940167, 0.5970513281866361, 0.581901761186141,
                0.8423198947819934, 0.5931933763376157, 0.7967201004919878, 0.807218919821064, 0.9296993794556061, 0.7556039338609682, 0.34624295457475307,
                0.9934073851614699, 0.8963420886508422, 0.15821607164807183, 0.9093492117108847, 0.07169690169079135, 0.5480047905862364, 0.48454952933864315,
                0.7922895161692849, 0.025292373558214454, 0.10871600466889642, 0.06354259193644274, 0.2145437436264489, 0.26304448081261256};
        driverTwenty.setAllGenes(driverTwentyGenes);

        IndividualTest driverTwentyOne = new IndividualTest();
        driverTwentyOne.setId("iWNnhWTJFx");
        double[] driverTwentyOneGenes = {0.7681714673220774, 0.32904709455246617, 0.5282294720924632, 0.7224539842961012, 0.5880583795447744, 0.8779073626193669,
                0.7623898849141817, 0.7798526014910775, 0.5805153888228975, 0.8798399917556695, 0.2112658615511841, 0.47054363695302914, 0.010710873641412322,
                0.950154676289048, 0.010846814730404053, 0.666613518010193, 0.27298621198382067, 0.5342013331478324, 0.6726609450982342, 0.4784796190403724,
                0.8255257838618184, 0.9904234822502985, 0.10853344130495224, 0.42344277789716367, 0.07071426632819211, 0.9300501627521173};
        driverTwentyOne.setAllGenes(driverTwentyOneGenes);

        IndividualTest driverTwentyTwo = new IndividualTest();
        driverTwentyTwo.setId("ut9dPlI4gP");
        double[] driverTwentyTwoGenes = {0.818066311840338, 0.3139681131846177, 0.848577772281489, 0.4782776193856618, 0.2819097710563444, 0.6297968750538215,
                0.6197032478237212, 0.8408757840602286, 0.49082285114032154, 0.49414996291193825, 0.0842296166922828, 0.6909970766412497, 0.9723989114440617,
                0.8068714720536919, 0.45904435818914957, 0.1657578572370978, 0.9725769481330143, 0.9428291330120808, 0.7176811718098667, 0.7922760988970178,
                0.5035394289931168, 0.5871728077212308, 0.2411236693496912, 0.06675403548911762, 0.28676026465240956, 0.14583466616496588};
        driverTwentyTwo.setAllGenes(driverTwentyTwoGenes);

        IndividualTest driverTwentyThree = new IndividualTest();
        driverTwentyThree.setId("7b5UBBsC4I");
        double[] driverTwentyThreeGenes = {0.5329934912723666, 0.7257027754054576, 0.5469356531789163, 0.14102293765238494, 0.260240066846534, 0.8196858598210347,
                0.6942776469218667, 0.5663518855309035, 0.4426832297447164, 0.568132610343831, 0.6846297764393032, 0.9638355320589834, 0.5242676402617779,
                0.23661878612267473, 0.0626476079539724, 0.7699331268995834, 0.4727695421323286, 0.3265219855789995, 0.3690315705072327, 0.19905549932013766,
                0.9246516395116492, 0.300600550627909, 0.9955682930379444, 0.6161170012233324, 0.22350397630525665, 0.7156854410010212};
        driverTwentyThree.setAllGenes(driverTwentyThreeGenes);

        IndividualTest driverTwentyFour = new IndividualTest();
        driverTwentyFour.setId("DIY6i5gLDT");
        double[] driverTwentyFourGenes = {0.34307048556644903, 0.7840370088181056, 0.34978277924480794, 0.434338265425643, 0.842996356943824, 0.5007099580646961,
                0.3617882679187594, 0.964815479864178, 0.6874298094361272, 0.8989122108023904, 0.48096863775167487, 0.3155720329742273, 0.5908212078744933,
                0.1887161887649722, 0.25755640828353465, 0.5825822332866853, 0.8005374270608663, 0.5273612029851787, 0.5255127224380794, 0.8169000945986501,
                0.7789557914721119, 0.48696635909325425, 0.044929763750271245, 0.7146626688113603, 0.25009130839270866, 0.0038194349736612176};
        driverTwentyFour.setAllGenes(driverTwentyFourGenes);

        IndividualTest driverTwentyFive = new IndividualTest();
        driverTwentyFive.setId("eL6Nemqv9J");
        double[] driverTwentyFiveGenes = {0.4856930483290838, 0.08546411607182502, 0.9979851235785805, 0.7051591913775582, 0.6540373660974934, 0.3176815610319692,
                0.08804094519467154, 0.7808553737772493, 0.06334745095443095, 0.2772237905849494, 0.685922169051715, 0.24304014525051842, 0.1642253160265158,
                0.24705279976711803, 0.2372899766845713, 0.8314324642972292, 0.6820595528283664, 0.29889083186906606, 0.8882467730655219, 0.6627839444417534,
                0.7573963972115054, 0.31942159229228584, 0.7489242888132115, 0.6306671460395753, 0.8192332318864557, 0.27509782454083576};
        driverTwentyFive.setAllGenes(driverTwentyFiveGenes);

        IndividualTest driverTwentySix = new IndividualTest();
        driverTwentySix.setId("hywpmfEvic");
        double[] driverTwentySixGenes = {0.3929837080571785, 0.41328373272348773, 0.23249613229061206, 0.4675796978547292, 0.04669985026327106, 0.09976949956285686,
                0.2481165715294258, 0.5020925276852803, 0.2523549662852921, 0.4238196087355739, 0.5978899822277024, 0.07673706115797008, 0.863973027235483,
                0.652871105290835, 0.5524523230590056, 0.7564259010047965, 0.19309746650591753, 0.06848393768894812, 0.10402343493244981, 0.9453756491861729,
                0.22434766505903114, 0.11168143510500728, 0.486266112697489, 0.9991227936186386, 0.32490331882586243, 0.8462926116605513};
        driverTwentySix.setAllGenes(driverTwentySixGenes);

        IndividualTest driverTwentySeven = new IndividualTest();
        driverTwentySeven.setId("JZbP1JejoR");
        double[] driverTwentySevenGenes = {0.2991188072049352, 0.8280770894020586, 0.5292170622377697, 0.6936762661675238, 0.3968922577222994, 0.5559071583391009,
                0.49185979876133756, 0.2582409941689102, 0.7743793562141154, 0.5192259334159012, 0.3344434257355, 0.1999511792703519, 0.25973232461999785,
                0.4014246201104571, 0.9419867181977697, 0.414633244667968, 0.0017502468840733254, 0.5951164411437339, 0.7585233624350188, 0.22709573099694502,
                0.08775630215892605, 0.2828915138209641, 0.9364262629774802, 0.7779626245871992, 0.3919487719714333, 0.7299981888306262};
        driverTwentySeven.setAllGenes(driverTwentySevenGenes);

        IndividualTest driverTwentyEight = new IndividualTest();
        driverTwentyEight.setId("g8ABGS0cAs");
        double[] driverTwentyEightGenes = {0.32753660653076455, 0.8396647800269429, 0.9927690967551609, 0.4675128295250124, 0.6015385870598231, 0.7146829353432677,
                0.239742555537783, 0.18219071605382275, 0.9162383648906834, 0.021667680317737137, 0.049231611748879356, 0.11961334652641109, 0.19144008982917426,
                0.4618047571512891, 0.4685796686090675, 0.8447413825323054, 0.9610022576562982, 0.9311465074274168, 0.5893439072092884, 0.8361249216690666,
                0.2931027735697137, 0.21494341847235365, 0.628075792629519, 0.9535318230266747, 0.7070797248139075, 0.5552979058088144};
        driverTwentyEight.setAllGenes(driverTwentyEightGenes);

        IndividualTest driverTwentyNine = new IndividualTest();
        driverTwentyNine.setId("UV2BbGuZi7");
        double[] driverTwentyNineGenes = {0.19573450573139672, 0.7095287881329787, 0.18544876990743708, 0.09786459204550679, 0.5104875712119555, 0.5405060022923039,
                0.9705047537500604, 0.894187009270771, 0.2421833032815559, 0.11046629430647681, 0.6970391813970678, 0.7127196499172643, 0.662685346136688,
                0.062312833625174924, 0.5681692261132176, 0.1502247134931336, 0.6334387846592593, 0.45921749979719517, 0.2854710187454025, 0.8508799871841286,
                0.07962512583918568, 0.8448221048481738, 0.42298962340753044, 0.6804199654649802, 0.013638241087776892, 0.24437022493505645};
        driverTwentyNine.setAllGenes(driverTwentyNineGenes);

        IndividualTest driverThirty = new IndividualTest();
        driverThirty.setId("rMjekVGpwC");
        double[] driverThirtyGenes = {0.2249357833999266, 0.8096100933870998, 0.4777526123181566, 0.16362085171038998, 0.4966091525100148, 0.35546431703347636,
                0.030135041887498715, 0.3993345072376546, 0.22936469652063218, 0.17389160686580984, 0.10812556029938292, 0.933372567535657, 0.1506579702963029,
                0.9094899393468332, 0.33917484102919715, 0.2714126665947424, 0.43777831164920045, 0.9962967900901738, 0.3613882031908835, 0.5146827220216929,
                0.5324218055608798, 0.4322279693279001, 0.14034377589266267, 0.6184603580240883, 0.4794465343812141, 0.4993982022109136};
        driverThirty.setAllGenes(driverThirtyGenes);

        IndividualTest driverThirtyOne = new IndividualTest();
        driverThirtyOne.setId("rlRkUNHgTQ");
        double[] driverThirtyOneGenes = {0.2730729712808846, 0.9275436315543236, 0.1296250094555207, 0.07690280086236934, 0.6258302393100962, 0.751692446819928,
                0.8866574267477111, 0.3002085394623858, 0.9724234041826374, 0.02321303750212489, 0.19002672640528695, 0.6409724178506966, 0.8268663961016396,
                0.1845244595149288, 0.17152129605048216, 0.5734442100606304, 0.27384765833783875, 0.7109725468007398, 0.1950219720253813, 0.7526154006469945,
                0.775724899218745, 0.18954001238851514, 0.5567602444200652, 0.6397304523459072, 0.09768869601738062, 0.31749101224982357};
        driverThirtyOne.setAllGenes(driverThirtyOneGenes);

        IndividualTest driverThirtyTwo = new IndividualTest();
        driverThirtyTwo.setId("HtduRW83cb");
        double[] driverThirtyTwoGenes = {0.4557679983805597, 0.4919495544858037, 0.9193215414444655, 0.3430316118851121, 0.8903600095902168, 0.5027589396331686,
                0.9917181927521515, 0.11314824484949759, 0.9867473229678537, 0.1857163180830873, 0.3543830736855387, 0.7342931780253332, 0.7343838825618556,
                0.6298264879349365, 0.9476983908693452, 0.3933227094374553, 0.12039610820263869, 0.7036370072437962, 0.34784823034504386, 0.8903116248444857,
                0.4821959679739445, 0.7067064199481617, 0.36703847300450376, 0.16202155610547864, 0.98828035595789, 0.6373644100707546};
        driverThirtyTwo.setAllGenes(driverThirtyTwoGenes);

        IndividualTest driverThirtyThree = new IndividualTest();
        driverThirtyThree.setId("QQij3rJD95");
        double[] driverThirtyThreeGenes = {0.026466552461212323, 0.5198464382267729, 0.544295614583643, 0.11510879572401256, 0.32355393546658684, 0.37236297658283635,
                0.04728432809035921, 0.8540707207961009, 0.09592781518687499, 0.42576419451588776, 0.8230248221938627, 0.1928542917246382, 0.45198020963061036,
                0.16664909876166367, 0.6619543847920757, 0.544991423243872, 0.9769057566837255, 0.9850845357874068, 0.6803343328405465, 0.9633953121989025,
                0.6347128573212986, 0.7219156205314862, 0.08991839387868195, 0.9940677146263054, 0.08579365094225444, 0.6803180709551467};
        driverThirtyThree.setAllGenes(driverThirtyThreeGenes);

        IndividualTest driverThirtyFour = new IndividualTest();
        driverThirtyFour.setId("CHh2IZt3Tr");
        double[] driverThirtyFourGenes = {0.1400270762413821, 0.937080857183547, 0.6773228105381189, 0.7736223231460413, 0.10467265004072912,
                0.12262734225664096, 0.5030270466353083, 0.564659632593563, 0.7330200066210633, 0.9428983710468595, 0.8563117187321844,
                0.7709036863790543, 0.4069502623467258, 0.9092721006762781, 0.9729581377823092, 0.4608918768551016, 0.5656424567501938, 0.9177491453435035,
                0.09008309054924724, 0.41650342457319933, 0.9053830710262611, 0.17866840989784494, 0.07619891233869702, 0.6806431928717912, 0.8945657948890313,
                0.12861296858702542};
        driverThirtyFour.setAllGenes(driverThirtyFourGenes);

        IndividualTest driverThirtyFive = new IndividualTest();
        driverThirtyFive.setId("EVu8ArnPPZ");
        double[] driverThirtyFiveGenes = {0.6697510289267855, 0.023090853474507678, 0.26992241955976337, 0.06075381745219155, 0.1783468465941428,
                0.873769627916899, 0.10860719274824238, 0.7542452909544556, 0.011966099156706211, 0.21056255806849122, 0.36604927424858413,
                0.38464332094669973, 0.1851966122733143, 0.43244383438418244, 0.5042541105392888, 0.8430396669514209, 0.17570296013643238,
                0.5203155691911747, 0.477298726847981, 0.092223205991259, 0.15714201423027452, 0.6944496506224753, 0.25177089742816894, 0.07490638326075005,
                0.8600205538956607, 0.537587224201313};
        driverThirtyFive.setAllGenes(driverThirtyFiveGenes);

        IndividualTest driverThirtySix = new IndividualTest();
        driverThirtySix.setId("6vn0LLiaWS");
        double[] driverThirtySixGenes = {0.10946673556890929, 0.29449768500956774, 0.9568428283090054, 0.8392506150607656, 0.1766556937460827, 0.45296323245056114,
                0.19003812881608406, 0.48390399226662917, 0.3508873463994837, 0.3659485918566565, 0.5293455292916687, 0.17033562284211, 0.03867118749867415,
                0.012912535761930632, 0.9124625984629433, 0.36822265354290906, 0.882200469509331, 0.4531314256722524, 0.1453611157826884, 0.6994593038835967,
                0.30523324230735716, 0.5359138114635079, 0.061497588899295796, 0.17125057419765666, 0.14516731500205404, 0.317927217900013};
        driverThirtySix.setAllGenes(driverThirtySixGenes);

        IndividualTest driverThirtySeven = new IndividualTest();
        driverThirtySeven.setId("HOgMi7sU6D");
        double[] driverThirtySevenGenes = {0.3641909164707463, 0.1814785202425162, 0.23034208064217854, 0.13831053189886844, 0.8993663698042877, 0.7396808534080865,
                0.5572043936084395, 0.48229809092283915, 0.3831739588180453, 0.9502149230341961, 0.9202521861202226, 0.6927854636092245, 0.8294168432076646,
                0.33963214227779825, 0.7433215297007882, 0.6580868842236758, 0.7824669269106486, 0.8826135636872748, 0.3703237939292582, 0.9683087326530417,
                0.685876811971208, 0.2903477189421567, 0.02924266069281589, 0.904951110680139, 0.6079377236123362, 0.5599756233461438};
        driverThirtySeven.setAllGenes(driverThirtySevenGenes);

        IndividualTest driverThirtyEight = new IndividualTest();
        driverThirtyEight.setId("k6Q6FWfvZx");
        double[] driverThirtyEightGenes = {0.05990419200808217, 0.6596414953998108, 0.13304367509094306, 0.5470544717443212, 0.12286268838734937, 0.17436356413468235,
                0.3540267490060809, 0.5680311115680833, 0.5087643146207523, 0.4190095424915795, 0.284169604137369, 0.5973722411640424, 0.1705804280325397,
                0.6757001272780633, 0.6576432657219782, 0.0828786005951746, 0.07797163641600724, 0.7874295196661758, 0.7548806495805264, 0.18800934692530213,
                0.8424561795446417, 0.4192270272544182, 0.08572875610668373, 0.5800712812235409, 0.9291347025113608, 0.4922169861993191};
        driverThirtyEight.setAllGenes(driverThirtyEightGenes);

        IndividualTest driverThirtyNine = new IndividualTest();
        driverThirtyNine.setId("hhG5VVE4Id");
        double[] driverThirtyNineGenes = {0.057360426309534196, 0.33266526249806516, 0.5648800964800739, 0.6274701587269639, 0.5682440425541496, 0.8942650473937859,
                0.6556272682669341, 0.31429700014336426, 0.6179449406086098, 0.17043414796840028, 0.9193141567289382, 0.821504509504857, 0.6123944189992533,
                0.8711997350413445, 0.1137140161614274, 0.29427224497322146, 0.009038699820072105, 0.21827358503826888, 0.22009636452726133, 0.8135110716894149,
                0.4168938118457821, 0.27915146246420686, 0.005421451114880238, 0.332013664895472, 0.6253430960196331, 0.39973169808951103};
        driverThirtyNine.setAllGenes(driverThirtyNineGenes);

        IndividualTest driverForty = new IndividualTest();
        driverForty.setId("g8DYiQbIkF");
        double[] driverFortyGenes = {0.8494585968924443, 0.9568649730703593, 0.7229024643735695, 0.49762456914426956, 0.3851830933523043, 0.9768628047860014,
                0.034357482123073324, 0.9880594038403593, 0.4419448479834345, 0.544877690511181, 0.41688978842659374, 0.11924560356566183, 0.7892621969762651,
                0.4965510531435323, 0.47265698822353075, 0.5845075710225517, 0.026793999397439427, 0.9743746878769853, 0.714133745776825, 0.2732682318316344,
                0.47973830028569664, 0.6029015306451981, 0.9385625991712038, 0.12001789531046203, 0.1384331303385662, 0.46188911095084395};
        driverForty.setAllGenes(driverFortyGenes);

        firstGeneration.getChromosomes()[0] = driverOne;
        firstGeneration.getChromosomes()[1] = driverTwo;
        firstGeneration.getChromosomes()[2] = driverThree;
        firstGeneration.getChromosomes()[3] = driverFour;
        firstGeneration.getChromosomes()[4] = driverFive;
        firstGeneration.getChromosomes()[5] = driverSix;
        firstGeneration.getChromosomes()[6] = driverSeven;
        firstGeneration.getChromosomes()[7] = driverEight;
        firstGeneration.getChromosomes()[8] = driverNine;
        firstGeneration.getChromosomes()[9] = driverTen;
        firstGeneration.getChromosomes()[10] = driverEleven;
        firstGeneration.getChromosomes()[11] = driverTwelve;
        firstGeneration.getChromosomes()[12] = driverThirteen;
        firstGeneration.getChromosomes()[13] = driverFourteen;
        firstGeneration.getChromosomes()[14] = driverFifteen;
        firstGeneration.getChromosomes()[15] = driverSixteen;
        firstGeneration.getChromosomes()[16] = driverSeventeen;
        firstGeneration.getChromosomes()[17] = driverEighteen;
        firstGeneration.getChromosomes()[18] = driverNineteen;
        firstGeneration.getChromosomes()[19] = driverTwenty;
        firstGeneration.getChromosomes()[20] = driverTwentyOne;
        firstGeneration.getChromosomes()[21] = driverTwentyTwo;
        firstGeneration.getChromosomes()[22] = driverTwentyThree;
        firstGeneration.getChromosomes()[23] = driverTwentyFour;
        firstGeneration.getChromosomes()[24] = driverTwentyFive;
        firstGeneration.getChromosomes()[25] = driverTwentySix;
        firstGeneration.getChromosomes()[26] = driverTwentySeven;
        firstGeneration.getChromosomes()[27] = driverTwentyEight;
        firstGeneration.getChromosomes()[28] = driverTwentyNine;
        firstGeneration.getChromosomes()[29] = driverThirty;
        firstGeneration.getChromosomes()[30] = driverThirtyOne;
        firstGeneration.getChromosomes()[31] = driverThirtyTwo;
        firstGeneration.getChromosomes()[32] = driverThirtyThree;
        firstGeneration.getChromosomes()[33] = driverThirtyFour;
        firstGeneration.getChromosomes()[34] = driverThirtyFive;
        firstGeneration.getChromosomes()[35] = driverThirtySix;
        firstGeneration.getChromosomes()[36] = driverThirtySeven;
        firstGeneration.getChromosomes()[37] = driverThirtyEight;
        firstGeneration.getChromosomes()[38] = driverThirtyNine;
        firstGeneration.getChromosomes()[39] = driverForty;

        currentGeneration++;

        System.out.println("THIS IS THE FIRST GENERATION METHOD");
        System.out.println("\nGENERATION " + GeneticDriverController.generationCounter + " IS RUNNING");
        System.out.println("POPULATION CREATED");

        System.out.println(Arrays.toString(firstGeneration.getChromosomes()));
        return firstGeneration.getChromosomes();
    }

    public void runIndividual(IndividualTest driver){
        if (GAOpponentController.minSomeoneInFront(true) < 40 && MySensorModel.getCurrentLapTime() > 5.00){
            GeneticDriverController.setOvertakingMode(true);
        }

//        //Calculate these values - Randomise these values
//        if (GeneticDriverController.isOvertakingMode()){
//            GAOpponentController.opponentSteerReviser();
//            GAOpponentController.opponentSpeedReviser();
//        }
        currentDriver++;

        if (currentDriver == 40){
            currentDriver -= currentDriver;
        }
    }

    public Population getPopulation() {
        return population;
    }

    public void setPopulation(Population population) {
        this.population = population;
    }

    public int getCurrentGeneration() {
        return currentGeneration;
    }

    public void setCurrentGeneration(int currentGeneration) {
        this.currentGeneration = currentGeneration;
    }

    public int getCurrentDriver() {
        return currentDriver;
    }

    public void setCurrentDriver(int currentDriver) {
        this.currentDriver = currentDriver;
    }

    public int getMaxGenerations() {
        return maxGenerations;
    }

    public Selection getSelection() {
        return selection;
    }

    public Mutation getMutation() {
        return mutation;
    }

    public Crossover getCrossover() {
        return crossover;
    }
}