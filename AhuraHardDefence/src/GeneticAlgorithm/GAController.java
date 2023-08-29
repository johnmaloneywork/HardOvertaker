package GeneticAlgorithm;

import ahooraDriver.Action;
import ahooraDriver.ParametersContainerE6;
import ahooraDriver.SensorModel;

public abstract class GAController {

    public ParametersContainerE6 myPara;

    public void initializePara(ParametersContainerE6 para){
        myPara = para;
    }

    public float[] initAngles()	{
        float[] angles = new float[19];
        for (int i = 0; i < 19; ++i)
            angles[i]=-90+i*10;
        return angles;
    }

    public abstract Action control(SensorModel sensors);

    public abstract void reset(); // called at the beginning of each new trial

    public abstract void shutdown();
}