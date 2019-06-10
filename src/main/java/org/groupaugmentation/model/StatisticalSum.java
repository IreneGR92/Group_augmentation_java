package org.groupaugmentation.model;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class StatisticalSum {

    protected int sumCounter = 0;

    protected double sum = 0;

    protected double sumOfSquares = 0;

    public double getMean() {
        return this.sum / this.sumCounter;
    }


    public double getStandardDeviation() {
        double variance = this.sumOfSquares / sumCounter - Math.pow(this.getMean(), 2);

        if (variance < 0) {
            log.error("Variance is negative!!!");
            throw new ArithmeticException("Variance is negative!");
        }

        return Math.sqrt(variance);
    }

    public void addSum(double toAdd) {
        this.sumOfSquares += Math.pow(toAdd, 2);
        this.sum += toAdd;
        this.sumCounter++;
    }


    public StatisticalSum merge(StatisticalSum statisticalSum) {
        this.sum += statisticalSum.getSum();
        this.sumOfSquares += statisticalSum.getSumOfSquares();
        this.sumCounter += statisticalSum.sumCounter;
        return this;
    }
}

