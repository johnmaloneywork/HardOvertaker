package GeneticAlgorithm;

import ahooraDriver.*;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static GeneticAlgorithm.GAOpponentController.*;

public class GeneticDriverController extends GAController {

    SpeedControllerE6 speedController = new SpeedControllerE6();
    DirectionControllerE6 directionController = new DirectionControllerE6();
    List<SensorModel> sensorList = new ArrayList<>();
    double totalDistance = 0.0;
    double prevLapTime = 0.0;
    double totalTime = 0.0;
    double damage = 0.0;
    float clutch = DriverControllerHelperE6.clutchMax;
    int lapCounter = 1;
    boolean lastLapTimeCounted= false;
    MySensorModel noiseCan = new MySensorModel();
    Action action = new Action();
    int episodeCounter = 1;
    static int driverCounter = 1;
    static int generationCounter = 1;
    double damageAverage = 0;
    double fitnessAverage = 0;
    double timeTakenAverage = 0;
    double topSpeedAverage = 0;
    double distanceAverage = 0;

    IndividualTest bestOverall = new IndividualTest();

    private IndividualTest individualTest;

    private final GA ga = new GA(1000);

    DecimalFormat decimalFormat = new DecimalFormat("#.###");
    static boolean overtakingMode = false;
    private static double topSpeed = 0;

    int successfulOvertakesCounter;
    double timeAtOvertakeAverage;

    @Override
    public Action control(SensorModel sensors) {

        if (MySensorModel.getSpeed() > topSpeed){
            topSpeed = MySensorModel.getSpeed();
            MySensorModel.setTopSpeed(topSpeed);
        }

        boolean inBounds = false;

        if (sensors.getDamage() >= 9000){
            action.restartRace = true;
        }

        totalDistance += sensors.getDistanceRaced();
        if(sensors.getDistanceFromStartLine() < 10.0f && sensors.getDistanceFromStartLine() > 0.0f && !lastLapTimeCounted){
            lastLapTimeCounted = true;
            prevLapTime += sensors.getLastLapTime();
            lapCounter++;
        }

        if(sensors.getDistanceFromStartLine() > 10.0f)
            lastLapTimeCounted = false;

        totalTime = prevLapTime + sensors.getCurrentLapTime();

        StuckTypes isStuck = StuckHandler.isStuck(sensors);
        boolean isOut = StuckHandler.isOutTrack(sensors);
        myPara.updatePenalty(isOut, sensors.getDamage(), lapCounter, sensors.getRacePosition());

        // TODO Auto-generated method stub
        speedController.setMyPara(myPara);
        directionController.setMyPara();

        sensorList.add(sensors);
        damage = sensors.getDamage();
        if(sensorList.size() > DriverControllerHelperE6.memorySensorLength){
            sensorList.remove(0);
        }

        noiseCan = NoiseCanceller.cancelNoise(sensorList);

        double estimatedTurn = DriverControllerHelperE6.turnDirectionCalculator(noiseCan, 9);
        speedController.setEstimatedTurn(estimatedTurn);
        directionController.setEstimatedTurn();


        int gear = speedController.calculateGear(noiseCan, isStuck);
        double steer = directionController.calcSteer(noiseCan, isStuck, isOut);

        myPara.frictionUpdater(gear, noiseCan, steer);
        myPara.trackWidthUpdater(noiseCan, steer, isOut);

        float [] accelBrake = speedController.calcBrakeAndAccelPedals(noiseCan, steer, isStuck, isOut);

        if(gear == 1) {
            clutch = DriverControllerHelperE6.clutchMax;
        }
        if(clutch > 0.0) {
            clutch = speedController.clutching(noiseCan, this.clutch);
        }

        for (int i = 0; i < MySensorModel.getTrackEdgeSensors().length; i++){
            if (MySensorModel.getTrackEdgeSensors()[i] > 0){
                inBounds = true;
            }
        }
        if (!MySensorModel.isOvertakeComplete() && MySensorModel.getCurrentLapTime() > 4.00 && inBounds) {
            for (int i = 0; i < MySensorModel.getOvertakeSensors().length; i++) {
                if (MySensorModel.getOvertakeSensors()[i] < 200) {
                    MySensorModel.setOvertakeComplete(true);
                    MySensorModel.setTimeAtOvertake(MySensorModel.getCurrentLapTime());
                }
            }
        }

        if (!MySensorModel.isAlongside() && MySensorModel.getCurrentLapTime() > 4.00){
            for (int i = 0; i < MySensorModel.getAlongsideSensors().length; i++) {
                if (MySensorModel.getAlongsideSensors()[i] < 200) {
                    MySensorModel.setAlongside(true);
                }
            }
        }

        action.gear = gear;
        action.steering = steer;
        action.accelerate = accelBrake[1];
        action.brake = accelBrake[0];
        action.clutch = clutch;

        GAOpponentController.calculateRelativeSpeed();

        return action;
    }

    @Override
    public void reset() {
        // TODO Auto-generated method stub

        //DECLARATIONS AND OUTPUT FOR EFFECT
        Fitness fitness = new Fitness();
        individualTest = GAClient.getIndividuals()[driverCounter - 1];
        individualTest.setFitnessObject(fitness);
        System.out.println("\n===== Gen."+generationCounter+ " - Dr." + driverCounter + " =====");

        //OVERTAKE FITNESS
        if(MySensorModel.isOvertakeComplete()){
            fitness.setOvertakeComplete(true);

            if (MySensorModel.getDamage() < 3750){
                fitness.setOvertakeTimeTaken(MySensorModel.getTimeAtOvertake());
                System.out.println("Overtake success: " + individualTest.getFitnessObject().isOvertakeComplete());
                successfulOvertakesCounter++;

                //OVERTAKE TIME TAKEN FITNESS
                System.out.println("Overtake time: " + individualTest.getFitnessObject().getOvertakeTimeTaken());
                System.out.println("Overtake time reward: " + individualTest.getFitnessObject().getOvertakeTimeReward());
                timeTakenAverage += individualTest.getFitnessObject().getOvertakeTimeTaken();
            }
            if (MySensorModel.getDamage() >= 3750){
                System.out.println("Overtake success: " + individualTest.getFitnessObject().isOvertakeComplete());
                System.out.println("INCURRED TOO MUCH DAMAGE DURING OVERTAKE");
            }

        }
        else if (MySensorModel.isAlongside()){
            fitness.setAlongside(true);
            System.out.println("Overtake success: " + individualTest.getFitnessObject().isOvertakeComplete());
            System.out.println("Alongside success: " + individualTest.getFitnessObject().isAlongside());
        }
        else {
            fitness.setOvertakeComplete(false);
            fitness.setAlongside(false);
            fitness.setOvertakeTimeTaken(-1);
            System.out.println("Overtake success: " + individualTest.getFitnessObject().isOvertakeComplete());
            System.out.println("Alongside success: " + individualTest.getFitnessObject().isAlongside());
        }

        //INSERT A TIME TAKEN REWARD??

        //DAMAGE FITNESS
        individualTest.getFitnessObject().setDamage(MySensorModel.getDamage());
        System.out.println("\nDamage incurred: " + individualTest.getFitnessObject().getDamage());
        System.out.println("Damage fitness: " + individualTest.getFitnessObject().getDamageReward());
        damageAverage += individualTest.getFitnessObject().getDamage();

        //FITNESS IF DESTROYED
        int maxDamage = 9950;
        if (MySensorModel.getDamage() > maxDamage) {
            individualTest.getFitnessObject().setTotalTimeAlive(MySensorModel.getCurrentLapTime());
            System.out.println("Time lasted: " + individualTest.getFitnessObject().getTotalTimeAlive());
        }

        //DISTANCE TRAVELLED FITNESS
        individualTest.getFitnessObject().setDistanceTravelled(MySensorModel.getDistanceFromStartLine());
        System.out.println("\nTotal distance: " + individualTest.getFitnessObject().getDistanceTravelled());
        System.out.println("Distance fitness: " + individualTest.getFitnessObject().getDistanceReward());
        distanceAverage += individualTest.getFitnessObject().getDistanceTravelled();

        //OUTPUT DRIVER ID
        System.out.println("\nDRIVER " + individualTest.getId());

        //OVERALL FITNESS
        individualTest.setFitnessTotal(fitness.getOverallFitness());
        System.out.println("Total fitness: " + fitness.getOverallFitness());
        fitnessAverage += fitness.getOverallFitness();

        double currentFitness = fitness.getOverallFitness();

        //OUTPUTTING ALL DRIVER INFO TO A FILE FOR EACH GENERATION
        try {
            FileWriter fileWriter = new FileWriter("C:\\Hard Defender\\drivers-generation"+generationCounter+".txt", true);
            FileReader fileReader = new FileReader("C:\\Hard Defender\\drivers-generation"+generationCounter+".txt");

            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            if(bufferedReader.readLine() == null){
                bufferedWriter.write("***************Drivers List***************\n");
            }

            if(driverCounter == 1){
                bufferedWriter.write("\n<><><><><><>Generation " + generationCounter + "<><><><><><>");
            }

            bufferedWriter.write("\n===============Driver " + driverCounter + "===============" +
                    "\n" + "Driver ID: " + individualTest.getId() +
                    "\n" + "Driver Genes: " + Arrays.toString(individualTest.getAllGenes()) +
                    "\n" + "Damage incurred: " + individualTest.getFitnessObject().getDamage() +
                    "\n" + "Damage fitness: " + individualTest.getFitnessObject().getDamageReward() +

                    "\n" + "Distance travelled: " + individualTest.getFitnessObject().getDistanceTravelled() +
                    "\n" + "Distance reward: " + individualTest.getFitnessObject().getDistanceReward() +

                    "\n" + "Overtake success: " + individualTest.getFitnessObject().isOvertakeComplete() +
                    "\n" + "Overtake time: " + individualTest.getFitnessObject().getOvertakeTimeTaken() +
                    "\n" + "Overtake time reward: " + individualTest.getFitnessObject().getOvertakeTimeReward() +

                    "\n" + "Total fitness: " + decimalFormat.format(currentFitness) + "\n");

            if (driverCounter == 40){
                bufferedWriter.write("\nGeneration " + generationCounter + " Average Damage: " + (damageAverage / 40) +
                        "\nGeneration " + generationCounter + " Average Distance Travelled: " + (distanceAverage / 40) +
                        "\nGeneration " + generationCounter + " Average Overtake Time: " + (timeTakenAverage / successfulOvertakesCounter) +
                        "\nGeneration " + generationCounter + " Number of Successful Overtakes: " + (successfulOvertakesCounter) +
                        "\nGeneration " + generationCounter + " Average Fitness: " + (fitnessAverage / 40));
            }
            bufferedWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //RESETTING INFORMATION WHEN A GENERATION ENDS
        driverCounter++;
        if(driverCounter == 41) {
            try {
                FileWriter bestFileWriter = new FileWriter("C:\\Hard Defender\\best of generation " + generationCounter + ".txt", true);
                FileReader bestFileReader = new FileReader("C:\\Hard Defender\\best of generation " + generationCounter + ".txt");

                BufferedWriter bestBufferedWriter = new BufferedWriter(bestFileWriter);
                BufferedReader bestBufferedReader = new BufferedReader(bestFileReader);

                if (bestBufferedReader.readLine() == null) {
                    bestBufferedWriter.write("***************Elite of Generation " + generationCounter + "***************\n");
                }

                for (int i = 0; i < GAClient.getIndividuals().length; i++){
                    if (GAClient.getIndividuals()[i].getFitnessTotal() > bestOverall.getFitnessTotal()){
                        bestOverall = GAClient.getIndividuals()[i];
                    }
                }

                bestBufferedWriter.write("\n" + "Driver ID: " + bestOverall.getId() +
                        "\n" + "Driver Fitness: " + bestOverall.getFitnessTotal() +
                        "\n" + "Driver Genes: " + Arrays.toString(bestOverall.getAllGenes()));
                bestBufferedWriter.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            generationCounter++;
            setDriverCounter(1);
            damageAverage = 0;
            timeTakenAverage = 0;
            fitnessAverage = 0;
            distanceAverage = 0;
            topSpeedAverage = 0;
            successfulOvertakesCounter = 0;
            GAClient.setIndividuals(ga.evolve(GAClient.getIndividuals()));
        }

        MySensorModel.setTopSpeed(0);
        MySensorModel.setAlongside(false);
        topSpeed = 0;
        episodeCounter++;
        MySensorModel.setOvertakeComplete(false);
        action.restartRace = false;
        GeneticDriverController.setOvertakingMode(false);
    }

    @Override
    public void shutdown() {
        // TODO Auto-generated method stub
        myPara.setDamage(damage);
        myPara.setTotalTime(totalTime);
    }
    public float[] initAngles()	{
        float[] angles = DriverControllerHelperE6.angles;

        /* set angles as {-90,-75,-60,-45,-30,-20,-15,-10,-5,0,5,10,15,20,30,45,60,75,90} */
        return angles;
    }
    public static int getDriverCounter() {
        return driverCounter;
    }
    public static void setDriverCounter(int driverCounter) {
        GeneticDriverController.driverCounter = driverCounter;
    }
    public static boolean isOvertakingMode() {
        return overtakingMode;
    }
    public static void setOvertakingMode(boolean overtakingMode) {
        GeneticDriverController.overtakingMode = overtakingMode;
    }
}