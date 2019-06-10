package org.groupaugmentation.model.fishtype;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.groupaugmentation.model.types.GeneType;
import org.groupaugmentation.util.Parameters;

import java.util.Map;

import static java.lang.Math.exp;
import static java.lang.Math.pow;

@Slf4j
@Getter
@Setter
public class Helper extends Individual {

    private double dispersal;

    private double help;


    public Helper(Map<GeneType, Double> genes) {
        super(genes);
        this.dispersal = Double.NaN;
        this.help = Double.NaN;

    }

    public double calculateDispersal(boolean isReactinNormDisperal) {
        final double beta = this.genes.get(GeneType.BETA);
        final double betaAge = this.genes.get(GeneType.BETA_AGE);


        double dispersal;
        if (!isReactinNormDisperal) {
            dispersal = beta;
        } else {
            dispersal = 1 / (1 + Math.exp(betaAge * this.age - beta));
        }

        if (dispersal < 0 || dispersal > 1) {
            log.error("Dispersal has to be within 0 and 1");
            throw new ArithmeticException("Dispersal has to be within 0 and 1");
        }

        return dispersal;
    }


    public double calculateHelp(boolean isReactinNormHelp) {
        final double alpha = this.genes.get(GeneType.ALPHA);
        final double alphaAge = this.genes.get(GeneType.ALPHA_AGE);
        final double alphaAge2 = this.genes.get(GeneType.ALPHA_AGE2);
        double help;

        if (!isReactinNormHelp) {
            help = alpha;
        } else {
            help = alpha + alphaAge * this.age + alphaAge2 * pow(this.age, 2);
            if (help < 0) {
                help = 0;
            }

        }
        this.setExpressHelp(true);
        return help;
    }

    @Override
    public double calculateSurvival(Parameters params, int totalHelpers) {
        int groupSize = totalHelpers + 1;
        double survival;

        if (params.isOldSurvivalFormula()) {
            survival = (1 - params.getX0()) / (1 + exp(params.getXsh() * this.help - params.getXsn() * groupSize));
        } else {
            survival = params.getX0() + params.getXsn() / (1 + exp(-groupSize)) - params.getXsh() / (1 + exp(-this.help));
        }

        if (survival > 0.95) {
            log.error("survival is greater than 0.95");
            throw new ArithmeticException("survival is greater than 0.95");

        }
        return survival;
    }
}
