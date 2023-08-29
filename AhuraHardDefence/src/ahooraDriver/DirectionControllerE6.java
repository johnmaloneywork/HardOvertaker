package ahooraDriver;

import GeneticAlgorithm.GAOpponentController;

import java.util.ArrayList;
import java.util.List;


public class DirectionControllerE6 {
    boolean jump = false;
    double jumpTime = 0.0;
    double pSteer = 0.0;

    public DirectionControllerE6 (){

    }

    public float calcSteer(MySensorModel sensors, StuckTypes stuck, boolean isOut){
        float steer = calculateSteer(sensors, stuck, isOut);
        if(isOut)
            if(steer > 0.8 || steer < -0.8)
                steer = (float) (Math.signum(steer)*0.8);

        if (steer < -1)
            steer = -1;
        if (steer > 1)
            steer = 1;

        pSteer = steer;

        return steer;
    }

    private float calculateSteer(MySensorModel sensors, StuckTypes stuck, boolean isOut){

        float steer;
        if(stuck != StuckTypes.NoStuck){

            if(isOut){
                if(DriverControllerHelperE6.isOnTheLeftHandSide(sensors)){//left hand side of the track
                    if(DriverControllerHelperE6.isTowardsInsideTheTrack(sensors)){//is towards the track, try to enter the track slowly (with a smooth direction)
                        if(DriverControllerHelperE6.isInTheCorrectDirection(sensors)){
                            steer = (float) 0.0;//g=1
                        }else{
                            steer = (float) 0.2;//g=1
                        }

                    }else{
                        if(DriverControllerHelperE6.isInTheCorrectDirection(sensors)){
                            steer = (float) 0.2;//g=-1
                        }else{
                            steer = (float) 0.2;//g=-1
                        }
                    }

                }else{//right hand side of the track
                    if(DriverControllerHelperE6.isTowardsInsideTheTrack(sensors)){//is towards the track, try to enter the track slowly (with a smooth direction)
                        if(DriverControllerHelperE6.isInTheCorrectDirection(sensors)){
                            steer = (float) 0.0;//g=1
                        }else{
                            steer = (float) -0.2;//g=1
                        }

                    }else{
                        if(DriverControllerHelperE6.isInTheCorrectDirection(sensors)){
                            steer = (float) -0.2;
                        }else{
                            steer = (float) -0.2;
                        }
                    }
                }
            }else{
                if(sensors.getGear() == -1){
                    steer = -(float) Math.signum(sensors.getAngleToTrackAxis());
                }else{
                    steer = (float) Math.signum(sensors.getAngleToTrackAxis());
                }

            }
            steer *= 5.0;

        }else{
            if(!isOut){
                int maxDistanceSensorIndx = DriverControllerHelperE6.extermumIndexAngle(MySensorModel.getTrackEdgeSensors(), extermumTypes.maximization);

                double[] opponentInfo = GAOpponentController.opponentSteerReviser();

                double soarDirection = (float) opponentInfo[0];

                int correctionSensors =  (int) Math.round(opponentInfo[1]);

                double steerToTurn = -weightedMean2(DriverControllerHelperE6.angles, MySensorModel.getTrackEdgeSensors(),
                        maxDistanceSensorIndx, correctionSensors, soarDirection, sensors);
                steer = (float) (steerToTurn);

            }else{
                steer = -(float) ((float) ((-sensors.getAngleToTrackAxis()*2.0)+Math.signum(MySensorModel.getTrackPosition())*13.0*Math.PI/180));
            }
        }

        if(sensors.getZ() > 0.65){
            jumpTime = MySensorModel.getCurrentLapTime();
            jump = true;
        }
        if(jump && jumpTime + 0.5 > MySensorModel.getCurrentLapTime()){
            steer = steer/5.0f;
        }else
            jump = false;

        return steer;
    }

    public double weightedMean2(float[] angles, double[] distances, int baseAngle,
                                int count, double soarDirection, MySensorModel sensors){
        soarDirection = Math.pow(2.0, 2.0*soarDirection);
        double resx=0;
        double resy=0.0;

        for(int i=baseAngle - count; i <= baseAngle + count; ++i){
            int indx = Math.min(i, angles.length - 1);
            indx = Math.max(indx, 0);

            if(Math.abs(angles[indx]) < 2 && angles[indx] != 0)
                continue;

            double dis = distances[indx];
            if(i<baseAngle){
                dis /= soarDirection;
            }
            if(i>=baseAngle){
                dis *= soarDirection;
            }
            if(i == baseAngle){
                dis *= 2.0;
            }
            if(Math.abs((sensors.getAngleToTrackAxis() + angles[indx]*Math.PI/180.0)) > Math.PI/2 ){
                continue;
            }
            resx += (DriverControllerHelperE6.cosAng[indx]*(dis));
            resy += (DriverControllerHelperE6.sinAng[indx]*(dis));
        }
        double res = Math.atan2(resy, resx);

        return res;
    }

    public void setEstimatedTurn() {
    }

    public void setMyPara() {
    }
}
