package GeneticAlgorithm;


import ahooraDriver.DriverControllerHelperE6;
import ahooraDriver.MySensorModel;
import static java.lang.Math.sqrt;

public class GAOpponentController {

	static double minBesideDistanceOvertake = 1.9;
	static double carWidth = 5;
	static double previousDistanceSpeed = -10;
	static double previousTimeSpeed = -10;
	static double previousDistanceSteer = -10;
	static double previousTimeSteer = -10;
	static double relativeSpeed = 0.0;
	static double besideAlter = 0.2;

	public static double minSomeoneInFront(boolean exact){

  		double [] opponentInfo = MySensorModel.getOpponentSensors();

  		double minDist = 200.0;

  		for(int i = 15; i<=21; ++i){
  			double width = Math.abs(DriverControllerHelperE6.sinAngO[i]*opponentInfo[i]);

  			if(width < carWidth/2.0){//it is in front of us

  				double distance = Math.abs(DriverControllerHelperE6.cosAngO[i]*opponentInfo[i]);

  				if(distance < minDist)
  					minDist = distance;
                         }else{
  				if(!exact){
  					double diff = width - minBesideDistanceOvertake - carWidth/2.0;
  					double c = ((0.0 - 1.0))*(diff - 0.0) + 1.0;
  					c = Math.max(0.0, c);
  					c = Math.min(1.0, c);

  					double distance = Math.abs(DriverControllerHelperE6.cosAngO[i]*opponentInfo[i])*(1.0-c)*5.0;

  					if(distance < minDist)
  						minDist = distance;
                 }
             }
		  }
  		return minDist;
  	}

	/*
	 *
	 * Defining the genes
	 * Speed Genes | Opponent Sensors Genes | Track Sensor Genes
	 *
	 * Speed Genes: MySensorModel.getSpeed() --> Speed of the Genetic Bot
	 * Speed Genes: MySensorMode.getRelativeSpeed() --> Speed of the Defending Bot
	 *
	 * Opponent Sensor Genes: opponentSensors[15] - opponentSensors[23]
	 *
	 * Track Sensor Genes: trackEdgeSensors[0], trackEdgeSensors[4], trackEdgeSensors[7],
	 * 					   trackEdgeSensors[8], trackEdgeSensors[9], trackEdgeSensors[10],
	 * 					   trackEdgeSensors[11], trackEdgeSensors[14], trackEdgeSensors[18]
	 *
	 * Total of 20 Genes
	 */


	public static double opponentSpeedReviser(){

		int current = GeneticDriverController.driverCounter;
		IndividualTest individualTest = GAClient.getIndividuals()[current - 1];

		/*
		 * RETURN A SIGMOID VALUE (POSSIBLY)
		 * TOP SPEED = 306
		 * LOW SPEED = 0
		 */

		//12 GENES
		double value = (//MySensorModel.getSpeed() * individualTest.getAllGenes()[0]) + (MySensorModel.getRelativeSpeed() * individualTest.getAllGenes()[1]) +
				//sqrt(MySensorModel.getOpponentSensors()[15] * individualTest.getAllGenes()[2]) +

				sqrt(MySensorModel.getOpponentSensors()[16] * individualTest.getAllGenes()[0]) +
				sqrt(MySensorModel.getOpponentSensors()[17] * individualTest.getAllGenes()[1]) +
				sqrt(MySensorModel.getOpponentSensors()[18] * individualTest.getAllGenes()[2]) +
				sqrt(MySensorModel.getOpponentSensors()[19] * individualTest.getAllGenes()[3]) +
				(MySensorModel.getOpponentSensors()[19] * individualTest.getAllGenes()[4]) +
				sqrt(MySensorModel.getOpponentSensors()[20] * individualTest.getAllGenes()[5]) +
				sqrt(MySensorModel.getOpponentSensors()[21] * individualTest.getAllGenes()[6]) +
				sqrt(MySensorModel.getOpponentSensors()[22] * individualTest.getAllGenes()[7]) +
				//sqrt(MySensorModel.getOpponentSensors()[23] * individualTest.getAllGenes()[10]) +


				//sqrt(MySensorModel.getTrackEdgeSensors()[7] * individualTest.getAllGenes()[11]) +
				sqrt(MySensorModel.getTrackEdgeSensors()[8] * individualTest.getAllGenes()[8]) +
				sqrt(MySensorModel.getTrackEdgeSensors()[9] * individualTest.getAllGenes()[9]) +
				(MySensorModel.getTrackEdgeSensors()[9] * individualTest.getAllGenes()[10]) +
				sqrt(MySensorModel.getTrackEdgeSensors()[10] * individualTest.getAllGenes()[11]));// +
				//sqrt(MySensorModel.getTrackEdgeSensors()[11] * individualTest.getAllGenes()[16]);

		return value;
	}

	private static double sigmoid(double x){
		return 1 / (1 + Math.exp(-x/200));
	}

	public static double[] opponentSteerReviser(){

		/*
		 * RETURN A SIGMOID VALUE
		 * POSITIVE TURN: 0.95
		 * NEGATIVE TURN -0.95
		 */
		int current = GeneticDriverController.driverCounter;
		IndividualTest individualTest = GAClient.getIndividuals()[current - 1];
		double value = (MySensorModel.getSpeed() * individualTest.getAllGenes()[12]) + (MySensorModel.getRelativeSpeed() * individualTest.getAllGenes()[13]) +

				//sqrt(MySensorModel.getOpponentSensors()[15] * individualTest.getAllGenes()[14]) +
				sqrt(MySensorModel.getOpponentSensors()[16] * individualTest.getAllGenes()[14]) +
				sqrt(MySensorModel.getOpponentSensors()[17] * individualTest.getAllGenes()[15]) +

				(MySensorModel.getOpponentSensors()[18] * individualTest.getAllGenes()[16]) +
				sqrt(MySensorModel.getOpponentSensors()[18] * individualTest.getAllGenes()[17]) +

				(MySensorModel.getOpponentSensors()[19] * individualTest.getAllGenes()[18]) +
				sqrt(MySensorModel.getOpponentSensors()[19] * individualTest.getAllGenes()[19]) +

				sqrt(MySensorModel.getOpponentSensors()[20] * individualTest.getAllGenes()[20]) +
				(MySensorModel.getOpponentSensors()[20] * individualTest.getAllGenes()[21]) +

				sqrt(MySensorModel.getOpponentSensors()[21] * individualTest.getAllGenes()[22]) +
				sqrt(MySensorModel.getOpponentSensors()[22] * individualTest.getAllGenes()[23]) +
				//sqrt(MySensorModel.getOpponentSensors()[23] * individualTest.getAllGenes()[25]) +


//				sqrt(MySensorModel.getTrackEdgeSensors()[7] * individualTest.getAllGenes()[14]) +
//				sqrt(MySensorModel.getTrackEdgeSensors()[8] * individualTest.getAllGenes()[15]) +
//				sqrt(MySensorModel.getTrackEdgeSensors()[9] * individualTest.getAllGenes()[16]) +
//				sqrt(MySensorModel.getTrackEdgeSensors()[10] * individualTest.getAllGenes()[17]) +
//				sqrt(MySensorModel.getTrackEdgeSensors()[11] * individualTest.getAllGenes()[18]);

				sqrt(MySensorModel.getTrackEdgeSensors()[0] * individualTest.getAllGenes()[24]) +
				sqrt(MySensorModel.getTrackEdgeSensors()[17] * individualTest.getAllGenes()[25]);

//		System.out.println("OPPONENT STEER: " + (sigmoid(value) * -1.9 - 1) / 10);
		/*
		 *
		 *
		 *
		 */

		//RETURNS THE DISTANCE IN FRONT
		double distanceInFront = minSomeoneInFront(false);

		//RETURNS THE ANGLE OF OPPONENT IN FRONT (15 - 21)
		int ang = maxSomeoneInFront();

		//FURTHEST ANGLE (18) minus ANGLE OF OPPONENT AHEAD
		double direction = Math.abs((18 - ang)*10.0);

		//DIRECTION ALTERATION BASED ON OPPONENT AHEAD
		double directionAlt = direction/value;

		if(distanceInFront < 30){
			directionAlt = Math.max(1.0, directionAlt);
		}

		//TRACK POSITION IN NEGATIVE
		double trackPosition = -MySensorModel.getTrackPosition();




		if(ang > 18){
			trackPosition += directionAlt;
		}else{
			if(ang < 18)
				trackPosition -= directionAlt;
		}

		trackPosition = Math.min(0.95, trackPosition);
		trackPosition = Math.max(-0.95, trackPosition);

		//System.out.println("OVERTAKE TURN: " + overTakeTurn);
		return new double [] {trackPosition, 10};
	}

	public static void calculateRelativeSpeed(){

		double [] opponentInfo = MySensorModel.getOpponentSensors();

		for (int i = 15; i < 22; i++){
			if (opponentInfo[i] < 50){
				if (previousDistanceSpeed < 0){
					previousDistanceSpeed = opponentInfo[i];
					previousTimeSpeed = MySensorModel.getCurrentLapTime();
				}
				else {
					double currentTime = MySensorModel.getCurrentLapTime();
					relativeSpeed = (opponentInfo[i] - previousDistanceSpeed) / (currentTime - previousTimeSpeed);
					relativeSpeed = -relativeSpeed;
					previousTimeSpeed = MySensorModel.getCurrentLapTime();
					previousDistanceSpeed = opponentInfo[i];
					MySensorModel.setRelativeSpeed(relativeSpeed);
				}
			}
		}
	}

	private static int maxSomeoneInFront(){
		double [] opponentInfo = MySensorModel.getOpponentSensors();
		int ang = 18;
		int finalAng = 18;

		double avr = (opponentInfo[ang] + opponentInfo[ang + 1] + opponentInfo[ang - 1])/3.0;
		double width = Math.abs(avr);

		for(int i = 0; i<=8; ++i){
			int j = (int) Math.ceil((double) i/2.0);
			j *= ((i%2)==0 ? 1 : -1);

			avr = (opponentInfo[j + ang] + opponentInfo[j + ang + 1] + opponentInfo[j + ang - 1])/3.0;

			if(width < Math.abs(avr)){
				finalAng = ang + j;
				width = Math.abs(avr);
			}
		}
		return finalAng;
	}

	private static int maxSomeoneBeside(){
		double [] opponentInfo = MySensorModel.getOpponentSensors();
		int ang = 18;
		int finalAng = 18;
		for(int i = 10; i<=30; ++i){
			int j = (int) Math.ceil((double) i/2.0);
			j *= ((i%2)==0 ? 1 : -1);
			double avr = (opponentInfo[j + ang]);

			double width = Math.abs(avr);

			if(width < minBesideDistanceOvertake + carWidth/2.0){
				finalAng = ang + j;
				break;
			}
		}
		return finalAng;
	}
}