package org.groupaugmentation.model.fishtype;

import lombok.extern.slf4j.Slf4j;
import org.groupaugmentation.model.types.GeneType;

import java.util.Map;

@Slf4j
public class Helper extends Individual {


    public Helper(Map<GeneType, Double> genes) {
        super(genes);
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

        return dispersal;
    }
}
