package org.groupaugmentation.util;


import java.util.Random;


public class RandomNumberGenerator {


    private Random randomNumberGenerator;

    private Parameters parameters;

    public RandomNumberGenerator(Parameters parameters) {
        randomNumberGenerator = new Random(0);
        this.parameters = parameters;
    }


    public double getNextRealUniform() {
        return randomNumberGenerator.nextDouble();
    }

    public double getNextInitDriftUniform() {
        return randomNumberGenerator.nextDouble() * 100;
    }

    public double getNextGaussianAlpha() {
        return randomNumberGenerator.nextGaussian() * parameters.getStepAlpha();
    }

    public double getNextGaussianBeta() {
        return randomNumberGenerator.nextGaussian() * parameters.getStepBeta();
    }

    public double getNextGaussianDrift() {
        return randomNumberGenerator.nextGaussian() * parameters.getStepDrift();
    }

    public int getNextPoissonAverageFloater() {
        //TODO not sure what value comes here
        return this.getNextPoisson(1);
    }

    public int getNextPoisson(double lambda) {
        double L = Math.exp(-lambda);
        double p = 1.0;
        int k = 0;
        do {
            k++;
            p *= randomNumberGenerator.nextDouble();
        } while (p > L);

        return k - 1;
    }

}
