package GeneticAlgorithm;

import java.util.Random;

public class Selection {

    /* Sorts the array by fitness */
    /* Using bubble sort to sort the array of individuals by fitness */
    public void sortFitnessArray(IndividualTest[] chromosomes) {
        for (int i = 0; i < chromosomes.length; i++) {
            for (int j = i + 1; j < chromosomes.length; j++) {
                IndividualTest tmp = null;
                if (chromosomes[i].getFitnessTotal()
                        < chromosomes[j].getFitnessTotal()) {
                    tmp = chromosomes[i];
                    chromosomes[i] = chromosomes[j];
                    chromosomes[j] = tmp;
                }
            }
        }
    }

    /* Returns the top 10 contenders */
    /* Top 10 are passed directly onto the next generation */
    public IndividualTest[] bestTenDrivers(IndividualTest[] chromosomes){

        sortFitnessArray(chromosomes);

        IndividualTest[] bestTenDrivers = new IndividualTest[10];

        for (int i = 0; i < 10; i++) {
            bestTenDrivers[i] = chromosomes[i];
        }
        return bestTenDrivers;
    }

    /* Tournament selection used to return the 20 winners by comparing the fitness of each pair */
    /* Tournament size is 2 */
    /* Tournament selection is then used to return the best 10 of the bottom 20 */
    /* Test to evolve population */
    public IndividualTest[] tournamentSelection(IndividualTest[] population){

        IndividualTest[] highFitnessPopulation = new IndividualTest[20];
        IndividualTest[] lowFitnessPopulation = new IndividualTest[20];
        IndividualTest[] tournamentPopulation = new IndividualTest[30];

        IndividualTest contestantOne;
        IndividualTest contestantTwo;

        int j = 0;

        for (int i = 0; i < population.length; i+=2){

            contestantOne = population[i];
            contestantTwo = population[i + 1];

            if(contestantOne.getFitnessTotal() > contestantTwo.getFitnessTotal()){
                highFitnessPopulation[j] = contestantOne;
                lowFitnessPopulation[j] = contestantTwo;
            }
            else if(contestantTwo.getFitnessTotal() > contestantOne.getFitnessTotal()) {
                highFitnessPopulation[j] = contestantTwo;
                lowFitnessPopulation[j] = contestantOne;
            }
            else {
                double randomNumber = Math.ceil(Math.random() * 100);
                //System.out.println(randomNumber);

                if (randomNumber % 2 != 0){
                    highFitnessPopulation[j] = contestantOne;
                    lowFitnessPopulation[j] = contestantTwo;
                }
                else {
                    highFitnessPopulation[j] = contestantTwo;
                    lowFitnessPopulation[j] = contestantOne;
                }
            }
            j++;
        }

        IndividualTest[] tempPop = new IndividualTest[10];
        j = 0;

        for (int i = 0; i < lowFitnessPopulation.length; i+=2){

            contestantOne = lowFitnessPopulation[i];
            contestantTwo = lowFitnessPopulation[i + 1];

            if (contestantOne.getFitnessTotal() > contestantTwo.getFitnessTotal()){
                tempPop[j] = contestantOne;
            } else if (contestantTwo.getFitnessTotal() > contestantOne.getFitnessTotal()) {
                tempPop[j] = contestantTwo;
            }
            else {
                double randomNumber = Math.ceil(Math.random() * 100);
                System.out.println(randomNumber);

                if (randomNumber % 2 != 0){
                    tempPop[j] = contestantOne;
                }
                else {
                    tempPop[j] = contestantTwo;
                }
            }
            j++;
        }

        System.arraycopy(highFitnessPopulation, 0, tournamentPopulation, 0, highFitnessPopulation.length);
        j = 0;
        //System.arraycopy(tempPop, 0, tournamentPopulation, highFitnessPopulation.length, tempPop.length);
        for (int i = highFitnessPopulation.length; i < tournamentPopulation.length; i++){
            tournamentPopulation[i] = tempPop[j];
            j++;
        }

        return tournamentPopulation;
    }
}
