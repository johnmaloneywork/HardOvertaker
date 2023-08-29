package ahooraDriver;


public class MySensorModel {
    // basic information about your car and the track (you probably should take care of these somehow)
	private static double speed = 0, trackPosition = 0, angleToTrackAxis = 0, lateralSpeed = 0, currentLapTime = 0, damage = 0,
			distanceFromStartLine = 0, distanceRaced = 0, fuelLevel = 0, lastLapTime = 0, RPM = 0, ZSpeed = 0, Z = 0;
	private static double [] trackEdgeSensors;
	private static double [] focusSensors;
	private static double [] opponentSensors;
	private static int gear = 0, racePosition = 0;
	private static double [] wheelSpinVelocity;
	private static String message = "";

	/**
	 * Added On Start
	 */
	private static double [] overtakeSensors;
	private static double [] alongsideSensors;
	private static double timeAtOvertake = 0;
	private static boolean overtakeComplete;
	private static boolean alongside;
	private static double topSpeed = 0;
	private static double relativeSpeed = 0;
	/**
	 * Added On End
	 */
	
	public MySensorModel(){

	}

	/**
	 * Added On Start
	 */
	public static boolean isOvertakeComplete() {
		return overtakeComplete;
	}
	public static void setOvertakeComplete(boolean overtakeComplete) {
		MySensorModel.overtakeComplete = overtakeComplete;
	}
	public static boolean isAlongside(){
		return alongside;
	}
	public static void setAlongside(boolean alongside) {
		MySensorModel.alongside = alongside;
	}
	public static double getTimeAtOvertake() {
		return timeAtOvertake;
	}
	public static void setTimeAtOvertake(double timeAtOvertake) {
		MySensorModel.timeAtOvertake = timeAtOvertake;
	}

	public static double[] getOvertakeSensors() {
		overtakeSensors = new double[]{opponentSensors[4], opponentSensors[3], opponentSensors[2], opponentSensors[1],
				opponentSensors[0], opponentSensors[35], opponentSensors[34], opponentSensors[33], opponentSensors[32], opponentSensors[31]};
		return overtakeSensors;
	}
	public static void setOvertakeSensors(double[] overtakeSensors) {
		MySensorModel.overtakeSensors = overtakeSensors;
	}

	public static double[] getAlongsideSensors(){
		alongsideSensors = new double[]{opponentSensors[8], opponentSensors[9], opponentSensors[10], opponentSensors[28],
				opponentSensors[27], opponentSensors[26]};
		return alongsideSensors;
	}
	public static void setAlongsideSensors(double[] alongsideSensors) {
		MySensorModel.alongsideSensors = alongsideSensors;
	}

	public static double getTopSpeed(){
		return topSpeed;
	}
	public static void setTopSpeed(double topSpeed) {
		MySensorModel.topSpeed = topSpeed;
	}
	public static double getRelativeSpeed() {
		return relativeSpeed;
	}
	public static void setRelativeSpeed(double relativeSpeed) {
		MySensorModel.relativeSpeed = relativeSpeed;
	}
	/**
	 * Added On End
	 */

	public static double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		MySensorModel.speed = speed;
	}

	public double getAngleToTrackAxis() {
		return angleToTrackAxis;
	}

	public void setAngleToTrackAxis(double angleToTrackAxis) {
		MySensorModel.angleToTrackAxis = angleToTrackAxis;
	}

	public static double [] getTrackEdgeSensors() {
		return trackEdgeSensors;
	}

	public void setTrackEdgeSensors(double [] trackEdgeSensors) {
		MySensorModel.trackEdgeSensors = trackEdgeSensors;
	}

	public double [] getFocusSensors() {
		return focusSensors;
	}

	public void setFocusSensors(double [] focusSensors) {
		MySensorModel.focusSensors = focusSensors;
	}

	public static double getTrackPosition() {
		return trackPosition;
	}

	public void setTrackPosition(double trackPosition) {
		MySensorModel.trackPosition = trackPosition;
	}

	public int getGear() {
		return gear;
	}

	public void setGear(int gear) {
		MySensorModel.gear = gear;
	}

	public static double [] getOpponentSensors() {
		return opponentSensors;
	}

	public void setOpponentSensors(double [] opponentSensors) {
		MySensorModel.opponentSensors = opponentSensors;
	}

	public int getRacePosition() {
		return racePosition;
	}

	public void setRacePosition(int racePosition) {
		MySensorModel.racePosition = racePosition;
	}

	public double [] getWheelSpinVelocity() {
		return wheelSpinVelocity;
	}

	public void setWheelSpinVelocity(double [] wheelSpinVelocity) {
		MySensorModel.wheelSpinVelocity = wheelSpinVelocity;
	}

	public static double getDistanceFromStartLine() {
		return distanceFromStartLine;
	}

	public static void setDistanceFromStartLine(double distanceFromStartLine) {
		MySensorModel.distanceFromStartLine = distanceFromStartLine;
	}

	public double getDistanceRaced() {
		return distanceRaced;
	}

	public void setDistanceRaced(double distanceRaced) {
		MySensorModel.distanceRaced = distanceRaced;
	}

	public double getLateralSpeed() {
		return lateralSpeed;
	}

	public void setLateralSpeed(double lateralSpeed) {
		MySensorModel.lateralSpeed = lateralSpeed;
	}

	public static double getCurrentLapTime() {
		return currentLapTime;
	}

	public static void setCurrentLapTime(double currentLapTime) {
		MySensorModel.currentLapTime = currentLapTime;
	}

	public static double getDamage() {
		return damage;
	}

	public void setDamage(double damage) {
		MySensorModel.damage = damage;
	}

	public double getFuelLevel() {
		return fuelLevel;
	}

	public void setFuelLevel(double fuelLevel) {
		MySensorModel.fuelLevel = fuelLevel;
	}

	public double getLastLapTime() {
		return lastLapTime;
	}

	public void setLastLapTime(double lastLapTime) {
		MySensorModel.lastLapTime = lastLapTime;
	}

	public double getRPM() {
		return RPM;
	}

	public void setRPM(double rPM) {
		RPM = rPM;
	}

	public double getZSpeed() {
		return ZSpeed;
	}

	public void setZSpeed(double zSpeed) {
		ZSpeed = zSpeed;
	}

	public double getZ() {
		return Z;
	}

	public void setZ(double z) {
		Z = z;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		MySensorModel.message = message;
	}
}
