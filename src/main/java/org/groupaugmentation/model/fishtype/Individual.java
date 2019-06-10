package org.groupaugmentation.model.fishtype;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.groupaugmentation.model.types.GeneType;
import org.groupaugmentation.util.Parameters;

import java.util.Map;

import static java.lang.Math.exp;


//@Data generates getters + setters
@Data
@Slf4j
public abstract class Individual {

    // other attributes
    protected int age;

    protected double survival;


    protected boolean expressHelp;

    protected Map<GeneType, Double> genes;

    public Individual(Map<GeneType, Double> genes) {
        this.genes = genes;
        this.age = 1;
        this.survival = Double.NaN;
        this.expressHelp = false;

        //TODO implement inherit
    }

    public double calculateSurvival(final Parameters params, int totalHelpers) {
        int groupSize = totalHelpers + 1;
        double survival;

        if (params.isOldSurvivalFormula()) {
            survival = (1 - params.getX0()) / (1 + exp(-params.getXsn() * groupSize));
        } else {
            survival = params.getX0() + params.getXsn() / (1 + exp(-groupSize));
        }

        if (survival > 0.95) {
            log.error("survival is greater than 0.95");
            throw new ArithmeticException("survival is greater than 0.95");

        }
        return survival;

    }


}
