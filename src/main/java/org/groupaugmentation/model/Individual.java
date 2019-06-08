package org.groupaugmentation.model;

import lombok.Data;
import org.groupaugmentation.model.types.FishType;
import org.groupaugmentation.model.types.GeneType;

import java.util.Map;


//@Data generates getters + setters
@Data
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
        this.survival =  Double.NaN;
        this.dispersal = Double.NaN;
        this.help =  Double.NaN;
        this.fishType = fishType;
        this.expressHelp = false;

        //TODO implement inherit
    }

}
