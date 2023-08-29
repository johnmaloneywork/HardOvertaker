package GeneticAlgorithm;

import ahooraDriver.MySensorModel;

import static ahooraDriver.MySensorModel.getCurrentLapTime;

public class Fitness {

    private boolean overtakeComplete;
    private boolean alongside;
    private double damage;
    private double damageReward;
    private double overtakeTimeTaken;
    private double overtakeTimeReward;
    private double totalTimeAlive;
    private double distanceTravelled;
    private double distanceReward;
    private double topSpeed;
    private double speedReward;

    public Fitness(){
        overtakeComplete = false;
        damage = 0;
        overtakeTimeTaken = 0;
        overtakeTimeReward = 0;
        totalTimeAlive = 0;
    }

    public double getOverallFitness() {
        double fitness = 0;

        int success = 1500;
        int alongside = 800;
        totalTimeAlive = MySensorModel.getCurrentLapTime();

        //calculate fitness
        if(MySensorModel.getDamage() > 4000){
            fitness += totalTimeAlive;
        }
        else if (MySensorModel.isOvertakeComplete()){
            fitness += (success + getDamageReward() + getOvertakeTimeReward());
        }
        else if(MySensorModel.isAlongside()){
            fitness += (getDamageReward() + alongside);
        }
        else {
            fitness += (getDamageReward());
        }

        int maxDamage = 9950;

        if (MySensorModel.getDamage() >= maxDamage){
            fitness += totalTimeAlive;
        }

        fitness+=getDistanceReward();

        return fitness;
    }

    public double getOvertakeTimeTaken() {
        return overtakeTimeTaken;
    }
    public void setOvertakeTimeTaken(double overtakeTimeTaken) {
        this.overtakeTimeTaken = overtakeTimeTaken;
    }
    public double getDamage() {
        return damage;
    }
    public void setDamage(double damage) {
        this.damage = damage;
    }
    public double getTotalTimeAlive() {
        return totalTimeAlive;
    }
    public void setTotalTimeAlive(double totalTimeAlive) {
        this.totalTimeAlive = totalTimeAlive;
    }

    public boolean isOvertakeComplete() {
        double[] array = MySensorModel.getOvertakeSensors();

        for (int i = 0; i < array.length; i++) {
            if (array[i] < 200 && getCurrentLapTime() > 5.00 && !MySensorModel.isOvertakeComplete()) {
                for (int j = 0; i < MySensorModel.getTrackEdgeSensors().length; i++){
                    if (MySensorModel.getTrackEdgeSensors()[j] < 0){
                        overtakeComplete = false;
                    }
                    else {
                        overtakeComplete = true;
                    }
                }
                break;
            }
        }
        return overtakeComplete;
    }

    public void setOvertakeComplete(boolean overtakeComplete) {
        this.overtakeComplete = overtakeComplete;
    }

    public boolean isAlongside() {
        double[] array = MySensorModel.getAlongsideSensors();

        for (int i = 0; i < array.length; i++){
            if (array[i] < 200 && getCurrentLapTime() > 5.00 && !alongside){
                alongside = true;
                break;
            }
        }
        return alongside;
    }

    public void setAlongside(boolean alongside) {
        this.alongside = alongside;
    }
    public double getDistanceTravelled() {
        return distanceTravelled;
    }
    public void setDistanceTravelled(double distanceTravelled) {
        this.distanceTravelled = distanceTravelled;
    }
    public double getDistanceReward(){
        return distanceTravelled / 10;
    }

    public void setDistanceReward(double distanceReward) {
        this.distanceReward = distanceReward;
    }
//    public double getSpeedReward() {
//        return topSpeed;
//    }
//    public void setSpeedReward(double speedReward) {
//        this.speedReward = speedReward;
//    }
//    public double getTopSpeed() {
//        return topSpeed;
//    }
//    public void setTopSpeed(double topSpeed) {
//        this.topSpeed = topSpeed;
//    }
    public double getDamageReward() {
        if (MySensorModel.getDamage() >= 5000 && MySensorModel.getDamage() <= 7500){
            damageReward = 5;
        }
        else if (MySensorModel.getDamage() >= 3000 && MySensorModel.getDamage() < 5000){
            damageReward = 10;
        }
        else if (MySensorModel.getDamage() >= 2000 && MySensorModel.getDamage() < 3000){

            damageReward = 25;
        }
        else if (MySensorModel.getDamage() >= 1000 && MySensorModel.getDamage() < 2000){
            damageReward = 100;
        }
        else if (MySensorModel.getDamage() >= 200 && MySensorModel.getDamage() < 1000){
            damageReward = 300;
        }
        else if (MySensorModel.getDamage() <= 199 && MySensorModel.getDamage() > 0){
            damageReward = 600;
        }
        else if (MySensorModel.getDamage() == 0){
            damageReward = 750;
        }
        return damageReward;
    }

    public void setDamageReward(double damageReward) {
        this.damageReward = damageReward;
    }

    public double getOvertakeTimeReward() {
        if (MySensorModel.getTimeAtOvertake() < 26 && MySensorModel.getDamage() < 3000){
            overtakeTimeReward = 350;
        }
        else if (MySensorModel.getTimeAtOvertake() < 28 && MySensorModel.getDamage() < 3000){
            overtakeTimeReward = 325;
        }
        else if (MySensorModel.getTimeAtOvertake() < 30 && MySensorModel.getDamage() < 3000){
            overtakeTimeReward = 300;
        }
        else if (MySensorModel.getTimeAtOvertake() <32 && MySensorModel.getDamage() < 3000){
            overtakeTimeReward = 275;
        }
        else if (MySensorModel.getTimeAtOvertake() < 34 && MySensorModel.getDamage() < 3000){
            overtakeTimeReward = 250;
        }
        else if (MySensorModel.getTimeAtOvertake() < 36 && MySensorModel.getDamage() < 3000){
            overtakeTimeReward = 225;
        }
        else if (MySensorModel.getTimeAtOvertake() < 38 && MySensorModel.getDamage() < 3000){
            overtakeTimeReward = 200;
        }
        else if (MySensorModel.getTimeAtOvertake() < 40 && MySensorModel.getDamage() < 3000){
            overtakeTimeReward = 175;
        }
        else if (MySensorModel.getTimeAtOvertake() < 42 && MySensorModel.getDamage() < 3000){
            overtakeTimeReward = 150;
        }
        else if (MySensorModel.getTimeAtOvertake() < 44 && MySensorModel.getDamage() < 3000){
            overtakeTimeReward = 125;
        }
        else if (MySensorModel.getTimeAtOvertake() < 46 && MySensorModel.getDamage() < 3000){
            overtakeTimeReward = 100;
        }
        else if (MySensorModel.getTimeAtOvertake() < 48 && MySensorModel.getDamage() < 3000){
            overtakeTimeReward = 75;
        }
        else if (MySensorModel.getTimeAtOvertake() < 50 && MySensorModel.getDamage() < 3000){
            overtakeTimeReward = 50;
        }
        else if (MySensorModel.getTimeAtOvertake() < 52 && MySensorModel.getDamage() < 3000){
            overtakeTimeReward = 25;
        }
        else if (MySensorModel.getTimeAtOvertake() < 54 && MySensorModel.getDamage() < 3000){
            overtakeTimeReward = 0;
        }
        else {
            overtakeTimeReward = 0;
        }

        return overtakeTimeReward;
    }

    public void setOvertakeTimeReward(double overtakeTimeReward) {
        this.overtakeTimeReward = overtakeTimeReward;
    }
}
