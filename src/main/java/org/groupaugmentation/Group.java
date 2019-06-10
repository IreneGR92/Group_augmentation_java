package org.groupaugmentation;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.groupaugmentation.model.DataModel;
import org.groupaugmentation.model.StatisticalSum;
import org.groupaugmentation.model.fishtype.Breeder;
import org.groupaugmentation.model.fishtype.Floater;
import org.groupaugmentation.model.fishtype.Helper;
import org.groupaugmentation.model.fishtype.Individual;
import org.groupaugmentation.model.types.DriftType;
import org.groupaugmentation.model.types.GeneType;
import org.groupaugmentation.model.types.PhenoTypes;
import org.groupaugmentation.util.Parameters;
import org.groupaugmentation.util.RandomNumberGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//TODO reset individual if expressed help
@Getter
@ToString
@Slf4j
public class Group {

    private final Parameters parameters;

    private final RandomNumberGenerator randomNumberGenerator;

    private boolean breederAlive;

    private double fecundity;

    private double realFecundity;

    private Breeder breeder;

    private List<Helper> helpers;

    public Group(Parameters parameters, RandomNumberGenerator randomNumberGenerator) {

        final Map<GeneType, Double> genes = new HashMap<>();
        genes.put(GeneType.DRIFT, randomNumberGenerator.getNextInitDriftUniform());
        genes.put(GeneType.ALPHA, parameters.getInitAlpha());
        genes.put(GeneType.ALPHA_AGE, parameters.getInitAlphaAge());
        genes.put(GeneType.ALPHA_AGE2, parameters.getInitAlphaAge2());
        genes.put(GeneType.BETA, parameters.getInitBeta());
        genes.put(GeneType.BETA_AGE, parameters.getInitBetaAge());
        this.parameters = parameters;
        this.randomNumberGenerator = randomNumberGenerator;
        this.breeder = new Breeder(genes);
        this.breederAlive = true;

        this.fecundity = Double.NaN;
        this.realFecundity = Double.NaN;
        ;

        this.helpers = new ArrayList<>();

        for (int i = 0; i < parameters.getInitNumberOfHelpers(); i++) {
            Helper helper = new Helper(genes);
            helpers.add(helper);
        }
    }


    public DataModel getDataModel() {

        List<Individual> helpersAndBreeder = new ArrayList<>(this.helpers);

        Map<DriftType, StatisticalSum> driftStats = this.calculateDriftTypeAttributes(helpersAndBreeder, this.breeder.getGenes().get(GeneType.DRIFT));

        if (breederAlive) {
            helpersAndBreeder.add(this.breeder);
        }

        StatisticalSum age = this.calculateAge(helpersAndBreeder);
        Map<GeneType, StatisticalSum> geneAttributes = this.calculateGeneAttributes(helpersAndBreeder);
        Map<PhenoTypes, StatisticalSum> phenotypeAttributes = this.calculatePhenoTypes(helpersAndBreeder);


        DataModel model = new DataModel();

        model.setAgeStats(age);
        model.setGroupSize(this.calculateGroupSize());
        model.setDriftStats(driftStats);
        model.setGeneAttributes(geneAttributes);
        model.setPhenotypeAttributes(phenotypeAttributes);
        model.setCumulativeHelp(this.calculateCumulativeHelp(model.getPhenotypeAttributes().get(PhenoTypes.HELP).getSum()));
        return model;
    }

    public List<Floater> disperse() {
        List<Floater> floaters = new ArrayList<>();

        for (Helper helper : helpers) {
            helper.setDispersal(helper.calculateDispersal(parameters.isReactionNormDispersal()));
            if (helper.getDispersal() > randomNumberGenerator.getNextRealUniform()) {
                this.helpers.remove(helper);
                Floater floater = new Floater(helper);
                floaters.add(floater);
            }
        }
        return floaters;
    }


    private Map<DriftType, StatisticalSum> calculateDriftTypeAttributes(List<Individual> helpers, double driftBreeder) {

        double drift;

        StatisticalSum driftH = new StatisticalSum();

        StatisticalSum driftBH = new StatisticalSum();


        for (Individual individual : helpers) {
            drift = individual.getGenes().get(GeneType.DRIFT);
            driftH.addSum(drift);

            driftBH.addSum(drift * driftBreeder);
        }

        StatisticalSum driftB = new StatisticalSum();
        driftB.addSum(driftBreeder);


        StatisticalSum driftBB = new StatisticalSum();

        driftBB.addSum(driftBreeder * driftBreeder);


        Map<DriftType, StatisticalSum> driftAttributes = new HashMap<>();
        driftAttributes.put(DriftType.H, driftH);
        driftAttributes.put(DriftType.B, driftB);
        driftAttributes.put(DriftType.BH, driftBH);
        driftAttributes.put(DriftType.BB, driftBB);

        return driftAttributes;
    }

    private StatisticalSum calculateAge(List<Individual> individuals) {

        StatisticalSum age = new StatisticalSum();
        for (Individual individual : individuals) {
            age.addSum(individual.getAge());
        }
        return age;
    }


    private Map<GeneType, StatisticalSum> calculateGeneAttributes(List<Individual> individuals) {
        Map<GeneType, StatisticalSum> geneAttributes = new HashMap<>();

        for (Individual individual : individuals) {
            //iterate trough all genes
            GeneType.stream().forEach(geneType -> {
                StatisticalSum statsAttributes = geneAttributes.get(geneType);
                if (statsAttributes == null) {
                    statsAttributes = new StatisticalSum();
                }
                double geneValue = individual.getGenes().get(geneType);

                if (GeneType.DRIFT != geneType) {
                    statsAttributes.addSum(geneValue);
                }

                if (breederAlive && individual instanceof Helper) {
                    statsAttributes.addSum(geneValue);
                }

                geneAttributes.put(geneType, statsAttributes);
            });
        }

        return geneAttributes;
    }

    private Map<PhenoTypes, StatisticalSum> calculatePhenoTypes(List<Individual> individuals) {
        StatisticalSum dispersal = new StatisticalSum();
        StatisticalSum help = new StatisticalSum();
        StatisticalSum survival = new StatisticalSum();
        StatisticalSum helpDispersalProduct = new StatisticalSum();

        for (Individual individual : individuals) {

            if (individual instanceof Helper) {
                dispersal.addSum(individual.getDispersal());

                if (individual.isExpressHelp()) {
                    help.addSum(individual.getHelp());

                    helpDispersalProduct.addSum(individual.getHelp() * individual.getDispersal());

                }
            }
            survival.addSum(individual.getSurvival());
        }


        Map<PhenoTypes, StatisticalSum> phenoTypeMap = new HashMap<>();
        phenoTypeMap.put(PhenoTypes.HELP, help);
        phenoTypeMap.put(PhenoTypes.SURVIVAL, survival);
        phenoTypeMap.put(PhenoTypes.DISPERSAL, dispersal);
        phenoTypeMap.put(PhenoTypes.HELP_DISPERSAL_PRODUCT, helpDispersalProduct);

        return phenoTypeMap;
    }

    private StatisticalSum calculateGroupSize() {

        StatisticalSum statisticalSum = new StatisticalSum();
        int groupSize;
        if (breederAlive) {
            groupSize = this.helpers.size() + 1;
        } else {
            groupSize = this.helpers.size();
        }
        statisticalSum.addSum(groupSize);
        return statisticalSum;
    }

    private StatisticalSum calculateCumulativeHelp(double sumOfGroupHelp) {
        StatisticalSum statisticalSum = new StatisticalSum();
        statisticalSum.addSum(sumOfGroupHelp);
        return statisticalSum;
    }

}
