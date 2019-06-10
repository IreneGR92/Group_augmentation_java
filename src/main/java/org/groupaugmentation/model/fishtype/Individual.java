package org.groupaugmentation.model.fishtype;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.groupaugmentation.model.types.GeneType;

import java.util.Map;


//@Data generates getters + setters
@Data
@Slf4j
public abstract class Individual {

    // other attributes
    protected int age;

    protected double survival;

    protected double dispersal;

    protected double help;

    protected boolean expressHelp;

    protected Map<GeneType, Double> genes;

    public Individual(Map<GeneType, Double> genes) {
        this.genes = genes;
        this.age = 1;
        this.survival = Double.NaN;
        this.dispersal = Double.NaN;
        this.help = Double.NaN;
        this.expressHelp = false;

        //TODO implement inherit
    }


}
