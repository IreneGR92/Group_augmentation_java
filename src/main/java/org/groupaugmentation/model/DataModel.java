package org.groupaugmentation.model;

import lombok.Data;
import org.groupaugmentation.model.types.DriftType;
import org.groupaugmentation.model.types.GeneType;
import org.groupaugmentation.model.types.PhenoTypes;

import java.util.HashMap;
import java.util.Map;

@Data
public final class DataModel {

    private int generation = 0;

    private int deaths = 0;

    private int floatersGenerated = 0;

    private int newBreederFloater = 0;

    private int newBreederHelper = 0;


    private StatisticalSum groupSize = new StatisticalSum();

    private StatisticalSum cumulativeHelp = new StatisticalSum();

    private StatisticalSum ageStats = new StatisticalSum();

    private Map<DriftType, StatisticalSum> driftStats = new HashMap<>();

    private Map<GeneType, StatisticalSum> geneAttributes = new HashMap<>();

    private Map<PhenoTypes, StatisticalSum> phenotypeAttributes = new HashMap<>();


    public double getRelatedness() {

        double upper = this.driftStats.get(DriftType.BH).getMean() - this.driftStats.get(DriftType.B).getMean() * this.driftStats.get(DriftType.H).getMean();
        double lower = driftStats.get(DriftType.BB).getMean() - Math.pow(driftStats.get(DriftType.B).getMean(), 2);

        return lower == 0 ? 2 : upper / lower;
    }


    public DataModel merge(DataModel toMerge) {

        this.groupSize.merge(toMerge.getGroupSize());
        this.ageStats.merge(toMerge.getAgeStats());
        this.groupSize.merge(toMerge.getGroupSize());
        this.cumulativeHelp.merge(toMerge.getCumulativeHelp());

        //merge drift
        toMerge.getDriftStats().forEach((geneType, statsSum) -> {
            StatisticalSum stats = this.driftStats.get(geneType);
            if (stats == null) {
                this.getDriftStats().put(geneType, statsSum);
            } else {
                this.getDriftStats().put(geneType, stats.merge(statsSum));
            }
        });

        //merge genes
        toMerge.geneAttributes.forEach((geneType, statsSum) -> {
            StatisticalSum stats = this.geneAttributes.get(geneType);
            if (stats == null) {
                this.geneAttributes.put(geneType, statsSum);
            } else {
                this.geneAttributes.put(geneType, stats.merge(statsSum));
            }
        });

        //merge phenotypes
        toMerge.getPhenotypeAttributes().forEach((geneType, statsSum) -> {
            StatisticalSum stats = this.geneAttributes.get(geneType);
            if (stats == null) {
                this.getPhenotypeAttributes().put(geneType, statsSum);
            } else {
                this.getPhenotypeAttributes().put(geneType, stats.merge(statsSum));
            }
        });

        return this;
    }

    public void increaseGeneration() {
        this.generation++;
    }

    public void increaseDeath() {
        this.deaths++;
    }

    public void increaseFloatersInmplemented() {

    }

}
