package GeneticAlgorithm;

import ahooraDriver.*;

import java.sql.DriverManager;
import java.util.Arrays;
import java.util.StringTokenizer;

public class GAClient{

        private static int UDP_TIMEOUT = 1000;
        private static int port;
        private static String host;
        private static String clientId;
        private static boolean verbose;
        private static int maxEpisodes;
        private static int maxSteps;
        private static int totalGenerations;
        private GeneticDriverController geneticDriverController;
        private static IndividualTest[] individuals;

        /**
         * @param args
         *            is used to define all the options of the client.
         *            <port:N> is used to specify the port for the connection (default is 3001)
         *            <host:ADDRESS> is used to specify the address of the host where the server is running (default is localhost)
         *            <id:ClientID> is used to specify the ID of the client sent to the server (default is championship2009)
         *            <verbose:on> is used to set verbose mode on (default is off)
         *            <maxEpisodes:N> is used to set the number of episodes (default is 1)
         *            <maxSteps:N> is used to set the max number of steps for each episode (0 is default value, that means unlimited number of steps)
         *            <stage:N> is used to set the current stage: 0 is WARMUP, 1 is QUALIFYING, 2 is RACE, others value means UNKNOWN (default is UNKNOWN)
         *            <trackName:name> is used to set the name of current track
         */
        public static void main(String[] args) throws Exception {
            GA ga = new GA(1000);

            //RUN GENETIC ALGORITHM
            System.out.println("\nSTARTING GENETIC ALGORITHM");

            if (ga.getCurrentGeneration() == 0){
                individuals = ga.runFirstGeneration();
            }

            parseParameters(args);
            ParametersContainerE6 para = new ParametersContainerE6();

            runClient(args, para, ga);

            GAClient client = new GAClient();
            client.parseAndLoad(args);

        }

        public static boolean runClient(String[] args, ParametersContainerE6 para, GA ga) throws Exception {
            SocketHandler mySocket = new SocketHandler(host, port, verbose);
            String inMsg;
            int syncRepair = 0;

            GAController driver = load(args[0]);

            /* Build init string */
            float[] angles = driver.initAngles();
            driver.initializePara(para);

            String initStr = clientId + "(init";
            for (int i = 0; i < angles.length; i++) {
                initStr = initStr + " " + angles[i];
            }
            initStr = initStr + ")";
            Action action;

            String s = "(angle 0)(curLapTime -0.982)(damage 0)(distFromStart 6184.46)(distRaced 0)(fuel 94)(gear 0)(lastLapTime 0)(opponents 17 200 200 200 200 200 200 200 200 200 200 200 200 200 200 200 200 200 16.9999 200 10.3077 200 200 200 200 200 200 200 200 200 200 200 200 8.5 200 24.8244)(racePos 3)(rpm 942.478)(speedX 0)(speedY 0)(speedZ -0.000605693)(track 4 4.14111 5.22163 6.97379 11.6952 15.4548 23.0351 45.8949 200 200 200 91.7897 46.0701 30.9096 23.3904 13.9476 10.4433 8.2822 8)(trackPos 0.333333)(wheelSpinVel 0 0 0 0)(z 0.345263)(focus -1 -1 -1 -1 -1)";
            action = driver.control(new MessageBasedSensorModel(s));

            long curEpisode = 0;
            boolean shutdownOccurred = false;

            do {
                do {
                    mySocket.send(initStr);
                    inMsg = mySocket.receive(UDP_TIMEOUT);

                } while (inMsg == null || inMsg.indexOf("***identified***") < 0);

                /*
                 * Start to drive
                 */
                long currStep = 0;

                while (true) {

                    /*
                     * Receives from TORCS the game state
                     */

                    if (GAOpponentController.minSomeoneInFront(true) < 15 && MySensorModel.getCurrentLapTime() > 4.00 && !GeneticDriverController.isOvertakingMode()){
                        GeneticDriverController.setOvertakingMode(true);
                        ga.runIndividual(individuals[GeneticDriverController.driverCounter - 1]);
                    }
                    else {
                        GeneticDriverController.setOvertakingMode(false);
                    }

                    inMsg = mySocket.receive(UDP_TIMEOUT);

                    long t = System.nanoTime();

                    if (inMsg != null) {

                        /*
                         * Check if race is ended (shutdown)
                         */
                        if (inMsg.indexOf("***shutdown***") >= 0) {
                            shutdownOccurred = true;
                            if (verbose){
                                System.out.println("Server shutdown!");
                            }
                            break;
                        }

                        /*
                         * Check if race is restarted
                         */
                        if (inMsg.indexOf("***restart***") >= 0) {
                            driver.reset();
                            if (verbose)
                                System.out.println("Server restarting!");
                            break;
                        }

                        if (currStep < maxSteps || maxSteps == 0){

                            action = driver.control(new MessageBasedSensorModel(inMsg));
                        }
                        else
                            action.restartRace = true;

                        currStep++;

                        t = System.nanoTime() - t;
                        if(t/1000000.0>=5.0){

                            syncRepair ++;
                        }
                        if(syncRepair > 0){//This is to be sure that we are in sync with server
                            syncRepair--;
                            continue;
                        }

                        if(currStep%300 == 0){//this is a delay to be sure if we are not sync with the server and we didnt detect that, we restart ourself every 1000 ticks
                            syncRepair++;
                        }

                        mySocket.send(action.toString());
                    }
                }
            } while (++curEpisode < maxEpisodes && !shutdownOccurred);
            /*
             * Shutdown the controller
             */
            driver.shutdown();
            mySocket.close();
            return true;
        }

        public void parseAndLoad(String[] args){
            GAClient.parseParameters(args);

            geneticDriverController = (GeneticDriverController) load(args[0]);
        }

        private static void parseParameters(String[] args) {
            /*
             * Set default values for the options
             */
            port = 3001;
            host = "localhost";
            clientId = "SCR";
            verbose = false;
            maxEpisodes = 1;
            maxSteps = 0;
            totalGenerations = 0;

            for (int i = 1; i < args.length; i++) {
                StringTokenizer st = new StringTokenizer(args[i], ":");
                String entity = st.nextToken();
                String value = st.nextToken();
                if (entity.equals("port")) {
                    port = Integer.parseInt(value);
                }
                if (entity.equals("host")) {
                    host = value;
                }
                if (entity.equals("id")) {
                    clientId = value;
                }
                if (entity.equals("verbose")) {
                    if (value.equals("on"))
                        verbose = true;
                    else if (value.equals(false))
                        verbose = false;
                    else {
                        System.out.println(entity + ":" + value
                                + " is not a valid option");
                        System.exit(0);
                    }
                }
                if (entity.equals("id")) {
                    clientId = value;
                }
                if (entity.equals("maxEpisodes")) {
                    maxEpisodes = Integer.parseInt(value);
                    if (maxEpisodes <= 0) {
                        System.out.println(entity + ":" + value
                                + " is not a valid option");
                        System.exit(0);
                    }
                }
                if (entity.equals("maxSteps")) {
                    maxSteps = Integer.parseInt(value);
                    if (maxSteps < 0) {
                        System.out.println(entity + ":" + value
                                + " is not a valid option");
                        System.exit(0);
                    }
                }
                if (entity.equals("totalGenerations")){
                    totalGenerations = Integer.parseInt(value);
                    if (totalGenerations < 1){
                        System.out.println(entity + ":" + value
                                + "is not a valid option");
                        System.exit(0);
                    }
                }
            }
        }
        private static GAController load(String name) {
            GAController gacontroller=null;
            try {
                gacontroller = (GAController) (Object) Class.forName(name).newInstance();
            } catch (ClassNotFoundException e) {
                System.out.println(name	+ " is not a class name");
                System.exit(0);
            } catch (InstantiationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return gacontroller;
        }

    public static IndividualTest[] getIndividuals() {
        return individuals;
    }

    public static void setIndividuals(IndividualTest[] individuals) {
        GAClient.individuals = individuals;
    }
}