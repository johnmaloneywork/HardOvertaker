package GeneticAlgorithm;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class IndividualTest {

    /*
     * 26 Genes to fill
     * Defining the genes
     * Speed Genes | Opponent Sensors Genes | Track Sensor Genes
     *
     * Speed Genes: MySensorModel.getSpeed() --> Speed of the Genetic Bot
     * Speed Genes: MySensorMode.getRelativeSpeed() --> Speed of the Defending Bot
     *
     * Opponent Sensor Genes: opponentSensors[16] - opponentSensors[22] (inclusive)
     *
     * Track Sensor Genes: trackEdgeSensors[8], trackEdgeSensors[9], trackEdgeSensors[10] (speedReviser)
     *					   trackEdgeSensors[0], trackEdgeSensors[17] (steerReviser)
     *
     * Total of 20 Genes
     */

    /* Genes that make up each chromosome */
    private double[] speedGenes; //2 Genes
    private double[] opponentSensorGenes; //7 Genes
    private double[] trackSensorGenes; //5 Genes
    private double[] allGenes; //26 Genes
    private Population population;

    /* Randomising evolution of genes */
    private static final Random rand = new Random();

    /* Assigning initial fitness of each chromosome */
    private Fitness fitnessObject;
    private double fitnessTotal;

    /* ID for each Individual */
    private String id;

    public IndividualTest(){

        this.id = generateIdentification();
        this.fitnessTotal = 0;
    }

    /* Constructor */
    public IndividualTest(double[] allGenes){
//        this.speedGenes = new double[2];
//        this.opponentSensorGenes = new double[9];
//        this.trackSensorGenes = new double[5];
        this.allGenes = new double[26];

//        int x = 0;
//
//        for (int i = 0; i < this.speedGenes.length; i++){
//            this.speedGenes[i] = rand.nextDouble();
//            this.allGenes[x] = this.speedGenes[i];
//            x++;
//        }
//
//        for (int j = 0; j < this.opponentSensorGenes.length; j++){
//            this.opponentSensorGenes[j] = rand.nextDouble();
//            this.allGenes[x] = this.opponentSensorGenes[j];
//            x++;
//        }
//
//        for (int k = 0; k < this.trackSensorGenes.length; k++){
//            this.trackSensorGenes[k] = rand.nextDouble();
//            this.allGenes[x] = this.trackSensorGenes[k];
//            x++;
//        }

        for (int i = 0; i < this.allGenes.length; i++){
            this.allGenes[i] = rand.nextDouble();
        }

        this.id = generateIdentification();

        this.fitnessTotal = 0;
    }

    public String toString(){
        String chromosomeAsString = "";

        chromosomeAsString += "\nDRIVER: " + getId();
        chromosomeAsString += "\nGENES: " + Arrays.toString(getAllGenes());

        return chromosomeAsString;
    }

    public String generateIdentification(){
        int leftLimit = 48;
        int rightLimit = 122;
        int identificationStringLength = 10;

        String id = rand.ints(leftLimit, rightLimit)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(identificationStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return id;
    }

    public double[] getAllGenes() {
        return allGenes;
    }

    public void setAllGenes(double[] allGenes) {
        this.allGenes = allGenes;
    }

//    public double[] getSpeedGenes() {
//        return speedGenes;
//    }
//
//    public void setSpeedGenes(double[] speedGenes) {
//        this.speedGenes = speedGenes;
//    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Population getPopulation() {
        return population;
    }

    public void setPopulation(Population population) {
        this.population = population;
    }

//    public double[] getOpponentSensorGenes() {
//        return opponentSensorGenes;
//    }
//
//    public void setOpponentSensorGenes(double[] opponentSensorGenes) {
//        this.opponentSensorGenes = opponentSensorGenes;
//    }

    public Fitness getFitnessObject() {
        return fitnessObject;
    }

    public void setFitnessObject(Fitness fitnessObject) {
        this.fitnessObject = fitnessObject;
    }

    public double getFitnessTotal() {
        return fitnessTotal;
    }

    public void setFitnessTotal(double fitnessTotal) {
        this.fitnessTotal = fitnessTotal;
    }

//    public double[] getTrackSensorGenes() {
//        return trackSensorGenes;
//    }
//
//    public void setTrackSensorGenes(double[] trackSensorGenes) {
//        this.trackSensorGenes = trackSensorGenes;
//    }
}
