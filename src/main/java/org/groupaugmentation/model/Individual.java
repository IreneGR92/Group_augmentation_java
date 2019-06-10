package org.groupaugmentation.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.groupaugmentation.model.types.FishType;
import org.groupaugmentation.model.types.GeneType;

import java.util.Map;


//@Data generates getters + setters
@Data
@Slf4j
public class Individual {

    // other attributes
    private int age;

    private double survival;

    private double dispersal;

    private double help;

    private boolean expressHelp;

    private Map<GeneType, Double> genes;

    private FishType fishType;

    public Individual(Map<GeneType, Double> genes, FishType fishType) {
        this.genes = genes;
        this.age = 1;
        this.survival = Double.NaN;
        this.dispersal = Double.NaN;
        this.help = Double.NaN;
        this.fishType = fishType;
        this.expressHelp = false;

        //TODO implement inherit
    }

    public double calculateDispersal(boolean isReactinNormDisperal) {
        final double beta = this.genes.get(GeneType.BETA);
        final double betaAge = this.genes.get(GeneType.BETA_AGE);


        double dispersal;
        if (!isReactinNormDisperal) {
            dispersal = beta;
        } else {
            dispersal = 1 / (1 + Math.exp(betaAge * age - beta));
        }

        if (dispersal < 0 || dispersal > 1) {
            log.error("Dispersal has to be within 0 and 1");
            throw new ArithmeticException("Dispersal has to be within 0 and 1");
        }

        if (this.fishType != FishType.HELPER) {
            log.error("dispersal calculated for non helper");
            throw new IllegalArgumentException("dispersal calculated for non helper");
        }

        return dispersal;
    }

}
