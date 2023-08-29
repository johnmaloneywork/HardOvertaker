package ahooraDriver;

import GeneticAlgorithm.Fitness;
import GeneticAlgorithm.IndividualTest;
import GeneticAlgorithm.Population;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class DriverControllerE6 extends Controller{
//	private float clutch = 0;

    SpeedControllerE6 speedController = new SpeedControllerE6();
    DirectionControllerE6 directionController = new DirectionControllerE6();
    List<SensorModel> sensorList = new ArrayList<SensorModel>();
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
    int driverCounter = 1;
    int generationCounter = 1;

    double damageAverage = 0;
    double fitnessAverage = 0;
    double timeTakenAverage = 0;

    private IndividualTest bestOverall = null;
    private Population population;

    DecimalFormat decimalFormat = new DecimalFormat("#.###");

    @Override
    public Action control(SensorModel sensors) {
//		long t = System.nanoTime();

        if (sensors.getDamage() >= 9950){
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
//		System.out.println(prevLapTime + " " + totalTime);

        StuckTypes isStuck = StuckHandler.isStuck(sensors);
        boolean isOut = StuckHandler.isOutTrack(sensors);
//		System.out.println(isStuck);
        myPara.updatePenalty(isOut, sensors.getDamage(), lapCounter, sensors.getRacePosition());

        // TODO Auto-generated method stub
        speedController.setMyPara(myPara);
        directionController.setMyPara();

        sensorList.add(sensors);
//		totalTime = sensors.getCurrentLapTime();
        damage = sensors.getDamage();
        if(sensorList.size() > DriverControllerHelperE6.memorySensorLength){
            sensorList.remove(0);
        }

        noiseCan = NoiseCanceller.cancelNoise(sensorList);

        double estimatedTurn = DriverControllerHelperE6.turnDirectionCalculator(noiseCan, 9);
        speedController.setEstimatedTurn(estimatedTurn);
        directionController.setEstimatedTurn();


//		System.out.println(isStuck + " " + isOut);
        int gear = speedController.calculateGear(noiseCan, isStuck);
        double steer = directionController.calcSteer(noiseCan, isStuck, isOut);

        myPara.frictionUpdater(gear, noiseCan, steer);
        myPara.trackWidthUpdater(noiseCan, steer, isOut);

        float [] accelBrake = speedController.calcBrakeAndAccelPedals(noiseCan, steer, isStuck, isOut);
        if(gear == 1)
            clutch = DriverControllerHelperE6.clutchMax;
        if(clutch > 0.0)
            clutch = speedController.clutching(noiseCan, this.clutch);

//		System.out.println(clutch);
//		float clutch = 0.5f;
        action.gear = gear;
        action.steering = steer;
        action.accelerate = accelBrake[1];
        action.brake = accelBrake[0];
        action.clutch = clutch;
//        t = System.nanoTime() - t;

//        System.out.print(t/1000000.0 + " ");
        return action;
    }

    @Override
    public void reset() {
        // TODO Auto-generated method stub
//		counter++;
//		double agg = myEvo.aggressionAlter(myPara.getAggression());
        //
//		myPara.setAggression(agg);

        /**
         * Added on Start
         */
        MySensorModel sensors = new MySensorModel();
        Fitness fitness = new Fitness();

        if(sensors.isOvertakeComplete()){
            fitness.setOvertakeComplete(true);
        }
        else {
            fitness.setOvertakeComplete(false);
        }
        /**
         * Added on End
         */

        fitness.setDamage(sensors.getDamage());

        System.out.println("Damage incurred: " + fitness.getDamage());
        damageAverage += fitness.getDamage();
        System.out.println("Overtake success: " + fitness.isOvertakeComplete());
        //System.out.println("Overtake time: " + fitness.getTimeTaken());
        //timeTakenAverage += fitness.getTimeTaken();
        System.out.println("Total fitness: " + fitness.getOverallFitness());
        fitnessAverage += (double) fitness.getOverallFitness();

        double currentFitness = (double) fitness.getOverallFitness();

        try {
            FileWriter fileWriter = new FileWriter("C:\\AhuraEvolution\\drivers-generation"+generationCounter+".txt", true);
            FileReader fileReader = new FileReader("C:\\AhuraEvolution\\drivers-generation"+generationCounter+".txt");

            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            if(bufferedReader.readLine() == null){
                bufferedWriter.write("***************Drivers List***************\n");
            }

            if(driverCounter == 1){
                bufferedWriter.write("<><><><><><>Generation " + generationCounter + "<><><><><><>\n");
            }

            bufferedWriter.write("===============Driver " + driverCounter + "===============" +
                    "\n" + "Damage incurred: " + fitness.getDamage() +
                    "\n" + "Overtake success: " + fitness.isOvertakeComplete() +
                    //"\n" + "Overtake time: " + fitness.getTimeTaken() +
                    "\n" + "Total fitness: " + decimalFormat.format(currentFitness));
//			for (int i = 0; i < population.getPopulationSize(); i++){
//				bufferedWriter.write(individual.toString());
//			}

            if (driverCounter == 40){
                bufferedWriter.write("\nGeneration " + generationCounter + " Average Damage: " + (damageAverage / 40) +
                        "\nGneration " + generationCounter + " Average Overtake Time: " + (timeTakenAverage / 40) +
                        "\nGeneration " + generationCounter + " Average Fitness: " + (fitnessAverage / 40));
            }
            bufferedWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        driverCounter++;
        if(driverCounter == 41){
            generationCounter++;
            driverCounter = 1;
            damageAverage = 0;
            timeTakenAverage = 0;
            fitnessAverage = 0;
        }


        System.out.println("Restarting the race!");
        episodeCounter++;
        //Arrays.fill(sensors.getOfficialTime(), 0);
        sensors.setOvertakeComplete(false);
        action.restartRace = false;

    }


    @Override
    public void shutdown() {
        // TODO Auto-generated method stub
//		System.out.println("Bye bye!");
        myPara.setDamage(damage);
        myPara.setTotalTime(totalTime);

//		myPara.writeToFile(s);

//		System.out.format("Shift: %.5f, Soar: %.2f, Total Time: %.3f, Damage: %.2f\n", this.myPara.getParameterByName("Speed Slope"),
//				this.myPara.getParameterByName("Speed Shift"), this.myPara.getParameterByName("Minimum Soar") , totalTime, damage);
    }

    public float[] initAngles()	{

        float[] angles = DriverControllerHelperE6.angles;

        /* set angles as {-90,-75,-60,-45,-30,-20,-15,-10,-5,0,5,10,15,20,30,45,60,75,90} */
        return angles;
    }
}