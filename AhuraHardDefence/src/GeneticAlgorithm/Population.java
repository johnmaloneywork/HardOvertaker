package GeneticAlgorithm;

public class Population {

    private final int POPULATION_SIZE = 40;
    private IndividualTest[] chromosomes;

    public Population(int POPULATION_SIZE){
        this.chromosomes = new IndividualTest[POPULATION_SIZE];

        for (int i = 0; i < POPULATION_SIZE; i++){
//            double[] opponentSensorGenes = new double[9];
//            double[] trackSensorGenes = new double[5];
//            double[] speedGenes = new double[2];
            double[] allGenes = new double[16];
            IndividualTest individualTest = new IndividualTest(allGenes);
            this.chromosomes[i] = individualTest;
        }
    }

    public int getPOPULATION_SIZE() {
        return POPULATION_SIZE;
    }
    public IndividualTest[] getChromosomes() {
        return chromosomes;
    }
    public void setChromosomes(IndividualTest[] chromosomes) {
        this.chromosomes = chromosomes;
    }
}
