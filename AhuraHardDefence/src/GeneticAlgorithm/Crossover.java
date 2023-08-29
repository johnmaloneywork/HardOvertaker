package GeneticAlgorithm;

public class Crossover {


    /* Single point crossover where the genes are split 50/50 down the middle */
    public IndividualTest singlePointCrossover(IndividualTest parentOne, IndividualTest parentTwo){
//        double[] parentOneSpeedGenes = parentOne.getSpeedGenes();
//        double[] parentOneOpponentSensorGenes = parentOne.getOpponentSensorGenes();
//        double[] parentOneTrackSensorGenes = parentOne.getTrackSensorGenes();
//        double[] parentTwoSpeedGenes = parentTwo.getSpeedGenes();
//        double[] parentTwoOpponentSensorGenes = parentTwo.getOpponentSensorGenes();
//        double[] parentTwoTackSensorGenes = parentTwo.getTrackSensorGenes();
//        double[] offspringSpeedGenes = new double[parentOneSpeedGenes.length];
//        double[] offspringOpponentSensorGenes = new double[parentOneOpponentSensorGenes.length];
//        double[] offSpringTrackSensorGenes = new double[parentOneTrackSensorGenes.length];

        double[] parentOneGenes = parentOne.getAllGenes();
        double[] parentTwoGenes = parentTwo.getAllGenes();
        double[] offspringGenes = new double[parentOneGenes.length];

        System.arraycopy(parentOneGenes, 0, offspringGenes, 0, parentOneGenes.length / 2);

//        System.arraycopy(parentOneSpeedGenes, 0, offspringSpeedGenes, 0, parentOneSpeedGenes.length / 2);
//        System.arraycopy(parentOneOpponentSensorGenes, 0, offspringOpponentSensorGenes, 0, parentOneOpponentSensorGenes.length / 2);
//        System.arraycopy(parentOneTrackSensorGenes, 0, offSpringTrackSensorGenes, 0, parentOneTrackSensorGenes.length / 2);



        if (offspringGenes.length + 1 - parentTwoGenes.length / 2 >= 0) {
            System.arraycopy(parentTwoGenes, parentTwoGenes.length / 2 - 1, offspringGenes, parentTwoGenes.length / 2 - 1,
                    offspringGenes.length + 1 - parentTwoGenes.length / 2 - 1);
        }
//        if (offspringOpponentSensorGenes.length + 1 - parentTwoOpponentSensorGenes.length / 2 >= 0) {
//            System.arraycopy(parentTwoOpponentSensorGenes, parentTwoOpponentSensorGenes.length / 2 - 1, offspringOpponentSensorGenes, parentTwoOpponentSensorGenes.length / 2 - 1,
//                    offspringOpponentSensorGenes.length + 1 - parentTwoOpponentSensorGenes.length / 2 - 1);
//        }
//        if (offSpringTrackSensorGenes.length + 1 - parentTwoTackSensorGenes.length / 2 >= 0) {
//            System.arraycopy(parentTwoTackSensorGenes, parentTwoTackSensorGenes.length / 2 - 1, offspringOpponentSensorGenes, parentTwoTackSensorGenes.length / 2 - 1,
//                    offSpringTrackSensorGenes.length + 1 - parentTwoTackSensorGenes.length / 2 - 1);
//        }

        return new IndividualTest(offspringGenes);
    }
}
