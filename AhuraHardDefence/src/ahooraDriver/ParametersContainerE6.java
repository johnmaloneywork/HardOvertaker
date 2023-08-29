package ahooraDriver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ParametersContainerE6 {
	String param = "maxv: 107.9075418536763, maxAggV: 7.0, -absSlip: 1.0, MaximumSoar: 95.30302118020252, midv2: 114.1158700288616, midv1: 90.47991440926471, -absMinSpeed: 3.0, minv: 50.95124925780534, minAggV: 0.01956501913690144, maxAggTurn: 5.382771708803534, Maxsensors: 6.230728885167587, MinimumSoar: -0.2300585760015009, -absRange: 3.0, Minsensors: 2.011735390446281";//default-slowest
	private Map<String, Double> parameters = new HashMap<String, Double>();
	private List<String> parametersNames = new ArrayList<String>();
	//outputs
	private double totalTime = 0.0;
	private double damage = 0.0;
	private int numberOfParameters = 0;
	private double penaltyCoef = 1.0;
	boolean pIsOut = false;
	
	private double friction = -1.08;
	double slipSampler = 0.0;
	double slipSamplerNumber = 0.0;
	double pRPM = 0.0;
	int lastLapOut = 1;
	double lastdamage = 0.0;
	private double trackWidth = 15.0;
	
	public ParametersContainerE6(){
		readInitialization();		
	}
	
	public void readInitialization(){
		String [] parameters = param.split(", ");
		for(String s : parameters){
			String [] currentLine = s.split(": ");
			numberOfParameters++;

			parametersNames.add(currentLine[0]);
			System.out.println(currentLine[0] + " " + currentLine[1]);
			getParameters().put(currentLine[0], Double.parseDouble(currentLine[1]));
		}
	}

	public double getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(double totalTime) {
		this.totalTime = totalTime;
	}
	public double getDamage() {
		return damage;
	}
	public void setDamage(double damage) {
		this.damage = damage;
	}
	public Map<String, Double> getParameters() {
		return parameters;
	}
	public void setParameters(Map<String, Double> parameters) {
		this.parameters = parameters;
	}
		
	public double getParameterByName(String name){
		Double res = parameters.get(name);
		if(res == null){
			String s = "-" + name;
			res = parameters.get(s);
		}
		return res;		
	}

	public int getNumberOfParameters() {
		return numberOfParameters;
	}
	public void setNumberOfParameters(int numberOfParameters) {
		this.numberOfParameters = numberOfParameters;
	}

	public String getParametersNames(int i) {
		String s = parametersNames.get(i);
		if(s.charAt(0) == '-')
			return s.substring(1);
		
		return s;
	}

	public void updatePenalty(boolean isOut, double damage, int lap, int pos){
		if(pIsOut != isOut){
			if(isOut){
				lastLapOut = lap;
				setPenaltyCoef(getPenaltyCoef() / 1.03);
				setPenaltyCoef(Math.max(getPenaltyCoef(), 0.6));
			}
		}
		if(lastLapOut < lap){
			if(lastdamage >= damage){				
				if(pos != 1){
					setPenaltyCoef(getPenaltyCoef() * 1.03);
					setPenaltyCoef(Math.min(getPenaltyCoef(), 1.4));
				}
			}
			lastLapOut = lap;
			lastdamage = damage;
		}
		pIsOut = isOut;
	}

	public double getPenaltyCoef() {
		return penaltyCoef;
	}
	public void setPenaltyCoef(double penaltyCoef) {
		this.penaltyCoef = penaltyCoef;
	}
	
	public void frictionUpdater(int gear, MySensorModel sensors, double steer){
		double RPM = sensors.getRPM();
		if(RPM > 7000.0 && RPM < 8000.0){
			if(gear == 3 || gear == 2){// || gear == 4){
				if(RPM > pRPM && Math.abs(steer) < 0.06){
			    	double slip = 0.0;
			    	slip=(sensors.getWheelSpinVelocity()[3] * DriverControllerHelperE6.wheelRadius[3] + sensors.getWheelSpinVelocity()[2] * DriverControllerHelperE6.wheelRadius[2])/2.0;
			    	slip = (sensors.getSpeed()/3.6)-slip;
					slipSampler += slip;
					slipSamplerNumber++;
				}else{
					slipSampler = 0.0;
					slipSamplerNumber = 0.0;	
				}
				
			}
		}else{
			slipSampler = 0.0;
			slipSamplerNumber = 0.0;					
		}
		if(slipSampler != 0.0 && slipSamplerNumber > 15){
			setFriction(slipSampler/slipSamplerNumber);
			
			slipSampler = 0.0;
			slipSamplerNumber = 0.0;
			if(gear == 2)
				getParameters().put("maxAggV", getFriction()*3.5);
			if(gear == 3)
				getParameters().put("maxAggV", getFriction()*4.5);			
			
			double widthCoef =  ((2.0-1.0)/(30.0-16.0)) * (getTrackWidth()-16.0) + 1.0;
			widthCoef = Math.max(widthCoef, 1.0);
			double maxAggV = getParameterByName("maxAggV");
			maxAggV /= widthCoef;
			maxAggV = Math.max(maxAggV, 4.50);
			if(sensors.getDamage() > 7000 && widthCoef <= 1){
				maxAggV = Math.max(maxAggV, 7.0);
			}
			getParameters().put("maxAggV", maxAggV);
		}
		pRPM = RPM;
	}

	public void trackWidthUpdater(MySensorModel sensors, double steer, boolean isOut){
		if(Math.abs(steer) < 0.06 && !isOut){
			trackWidth = (sensors.getTrackEdgeSensors()[0] + sensors.getTrackEdgeSensors()[sensors.getTrackEdgeSensors().length - 1]);
			
		}
	}
	
	public double getFriction() {
		return Math.abs(friction);
	}
	public void setFriction(double friction) {
		this.friction = friction;
	}
	public double getTrackWidth() {
		return trackWidth;
	}
	public void setTrackWidth(double trackWidth) {
		this.trackWidth = trackWidth;
	}

}
