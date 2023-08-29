package ahooraDriver;


import GeneticAlgorithm.GAOpponentController;

public class SpeedControllerE6 {
	private ParametersContainerE6 myPara;
	private double estimatedTurn = 0.0;
	private double pSpeed = 0.0;

	public SpeedControllerE6(){
		
	}
	
	public float [] calcBrakeAndAccelPedals(MySensorModel sensors, double currentSteer, StuckTypes isStuck, boolean isOut){
		float accel_and_brake = calcAccel(sensors, currentSteer, isOut, isStuck);
		float accel,brake;
        if (accel_and_brake>0) {
            accel = filterASR(sensors, accel_and_brake);
            brake = 0;
        }
        else {
            accel = 0;
            // apply ABS to brake
            brake = filterABS(sensors,-accel_and_brake);
        }

		return new float[]{brake, accel};
	}
	
	public float clutching(MySensorModel sensors, float clutch)
	{
		  	 
	  float maxClutch = DriverControllerHelperE6.clutchMax;
	  
	  // Check if the current situation is the race start
	  if ((MySensorModel.getCurrentLapTime()<DriverControllerHelperE6.clutchDeltaTime  &&
			  sensors.getDistanceRaced()<DriverControllerHelperE6.clutchDeltaRaced)) {
		  clutch = maxClutch;
	    }

	  // Adjust the current value of the clutch
	  if(clutch > 0)
	  {
	    double delta = DriverControllerHelperE6.clutchDelta;
	    if (sensors.getGear() < 2)
		{
	      // Apply a stronger clutch output when the gear is one and the race is just started
		  delta /= 2.0;
	      maxClutch *= DriverControllerHelperE6.clutchMaxModifier;
	      if (MySensorModel.getCurrentLapTime() < DriverControllerHelperE6.clutchMaxTime)
	        clutch = maxClutch;
		}

	    // check clutch is not bigger than maximum values
		clutch = Math.min(maxClutch,clutch);

		// if clutch is not at max value decrease it quite quickly
		if (clutch!=maxClutch)
		{
		  clutch -= delta;
		  clutch = Math.max((float) 0.0,clutch);
		}
		// if clutch is at max value decrease it very slowly
		else
			clutch -= DriverControllerHelperE6.clutchDec;
	  }
	  return clutch;
	}

	
	private float filterASR(MySensorModel sensors,float accel){
		float asrSlip = (float) DriverControllerHelperE6.asrSlip;
		float asrRange = (float) DriverControllerHelperE6.asrRange;
		float asrMinSpeed = (float) DriverControllerHelperE6.asrMinSpeed;
		
		// convert speed to m/s
		float speed = (float) (MySensorModel.getSpeed() / 3.6);
		// when spedd lower than min speed for abs do nothing
	    if (speed > asrMinSpeed)
	        return accel;
	    
	    // compute the speed of wheels in m/s
	    float slip = 0.0f;
	    for (int i = 0; i < 4; i++) {
	    	double wheelsSpeed = sensors.getWheelSpinVelocity()[i] * DriverControllerHelperE6.wheelRadius[i];
	        slip += wheelsSpeed;
	    }
	    	    	    
	    // slip is the difference between actual speed of car and average speed of wheels
	    slip = speed - slip/4.0f;
	    
	    // when slip too high applu ABS
	    if (-slip > asrSlip) {
	        accel = accel + (slip + asrSlip)/asrRange;
	    }
	    // check brake is not negative, otherwise set it to zero
	    if (accel<0)
	    	return 0;
	    else
	    	return accel;
	}

	private float filterABS(MySensorModel sensors,float brake){
		float absSlip = (float) getMyPara().getParameterByName("absSlip");//DriverControllerHelperE5.absSlip;
		float absRange = (float) getMyPara().getParameterByName("absRange");//DriverControllerHelperE5.absRange;
		float absMinSpeed = (float) getMyPara().getParameterByName("absMinSpeed");//DriverControllerHelperE5.absMinSpeed;
		
		// convert speed to m/s
		float speed = (float) (MySensorModel.getSpeed() / 3.6);
		// when spedd lower than min speed for abs do nothing
	    if (speed < absMinSpeed)
	        return brake;
	    
	    // compute the speed of wheels in m/s
	    float slip = 0.0f;
	    for (int i = 0; i < 4; i++) {
	    	double wheelsSpeed = sensors.getWheelSpinVelocity()[i] * DriverControllerHelperE6.wheelRadius[i];
	        slip += wheelsSpeed;
	    }

	    // slip is the difference between actual speed of car and average speed of wheels
	    slip = speed - slip/4.0f;
	    // when slip too high applu ABS
	    if (slip > absSlip) {
	        brake = brake - (slip - absSlip)/absRange;
	    }
	    
	    // check brake is not negative, otherwise set it to zero
	    if (brake<0)
	    	return 0;
	    else
	    	return brake;
	}
	
	public int calculateGear(MySensorModel sensors, StuckTypes stuck){
		
	    int currentGear = sensors.getGear();
	    double rpm  = sensors.getRPM();

	    if(stuck == StuckTypes.AngularStuck || stuck == StuckTypes.WallStuck || stuck == StuckTypes.OpponentStuck){
	    	int gear;
	    	if(stuck == StuckTypes.WallStuck || stuck == StuckTypes.OpponentStuck)
	        	gear = -1;
	    	else
				gear = 1;

	        return gear;
	    }else{
		    if (currentGear<1)
		        return 1;
		    if (currentGear <6 && rpm >= DriverControllerHelperE6.gearUp[currentGear-1])
		        return currentGear + 1;
		    else
		        if (currentGear > 1 && rpm <= DriverControllerHelperE6.gearDown[currentGear-1])
		            return currentGear - 1;
		        else
		            return currentGear;
	    }
	}

	public void setEstimatedTurn(double estimatedTurn) {
		this.estimatedTurn = estimatedTurn;
	}

	public ParametersContainerE6 getMyPara() {
		return myPara;
	}

	public void setMyPara(ParametersContainerE6 myPara) {
		this.myPara = myPara;
	}

	public float calcAccel(MySensorModel sensors, double currentSteer, boolean isOut, StuckTypes isStuck){

		double targetSpeed;
		double coef = 1.0;
		double coefT = .5;
		if(isStuck != StuckTypes.NoStuck){
			coef = coefT;
			if(sensors.getGear() < 0){
				targetSpeed = DriverControllerHelperE6.minSpeed;

			}else
				targetSpeed = DriverControllerHelperE6.minSpeed*2.0;

			if(isOut){
				if(Math.abs(currentSteer)<0.1){
					coef = 0.9;
				}
			}

		}else{
			if(isOut){
				if(sensors.getGear() < 0){
					targetSpeed = DriverControllerHelperE6.minSpeed;
					coef = coefT;
				}else {
					if(Math.abs(currentSteer)<0.1){
						targetSpeed = Math.max(DriverControllerHelperE6.minSpeed*4.0, MySensorModel.getSpeed());
						coef = 0.6;
					}else{
						targetSpeed = DriverControllerHelperE6.minSpeed*3.0;
						coef = 0.03;
					}

				}
			}else{
				targetSpeed = targetSpeed(sensors, currentSteer);
			}
		}

		return (float) (2.0/(1.0+Math.exp(coef*(Math.abs(MySensorModel.getSpeed()) - targetSpeed))) - 1.0);

	}

	private double targetSpeed(MySensorModel sensors, double currentSteer){

		double minSpeed = DriverControllerHelperE6.minSpeed;//if -90 or 90 has the maximum distances, we need to go slow
		double maxSpeed = DriverControllerHelperE6.maxSpeed;//if 0 has the maximum distance, it depends on the distance
		double maxRange = DriverControllerHelperE6.maxSensorRangeProximity;
		float distInfront = (float) DriverControllerHelperE6.maximumDistanceInfront(sensors.getTrackEdgeSensors());
//
		if (MySensorModel.getCurrentLapTime() < 5.00){
			return 0;
		}

//		else if (MySensorModel.getTrackEdgeSensors()[8] >= 70){
//			return 200;
//		}

		if(sensors.getGear() < 0){
			return DriverControllerHelperE6.minSpeed;
		}

		double minAggTurn = -getMyPara().getParameterByName("maxAggTurn");
		double maxAggTurn = getMyPara().getParameterByName("maxAggTurn");
		double minAggV = getMyPara().getParameterByName("minAggV");
		double maxAggV = getMyPara().getParameterByName("maxAggV");

		double aggressionSpeed = DriverControllerHelperE6.logSig(minAggV, maxAggV, minAggTurn, maxAggTurn, 0.99, Math.abs(getEstimatedTurn()));

		double res = ((Math.pow(distInfront/maxRange, aggressionSpeed))*((maxSpeed-minSpeed)))
				+minSpeed;

		res = GAOpponentController.opponentSpeedReviser();

		double speed = Math.min(res, maxSpeed);

		if(sensors.getZSpeed() > 15.0 && myPara.getTrackWidth() < 13){

			speed = Math.min(speed, Math.max(170.0, MySensorModel.getSpeed() * 0.99));
		}

		pSpeed = speed;

		return speed*myPara.getPenaltyCoef();
	}

	public double getEstimatedTurn() {
		return estimatedTurn;
	}
}
