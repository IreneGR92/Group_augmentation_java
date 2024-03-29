package org.groupaugmentation;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.groupaugmentation.model.DataModel;
import org.groupaugmentation.model.Individual;
import org.groupaugmentation.model.StatisticalSum;
import org.groupaugmentation.model.types.DriftType;
import org.groupaugmentation.model.types.FishType;
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

    private boolean breederAlive;

    private double fecundity;

    private double realFecundity;

    private Individual breeder;

    private List<Individual> helpers;

    public Group(Parameters parameters, RandomNumberGenerator randomNumberGenerator) {

        final Map<GeneType, Double> genes = new HashMap<>();
        genes.put(GeneType.DRIFT, randomNumberGenerator.getNextInitDriftUniform());
        genes.put(GeneType.ALPHA, parameters.getInitAlpha());
        genes.put(GeneType.ALPHA_AGE, parameters.getInitAlphaAge());
        genes.put(GeneType.ALPHA_AGE2, parameters.getInitAlphaAge2());
        genes.put(GeneType.BETA, parameters.getInitBeta());
        genes.put(GeneType.BETA_AGE, parameters.getInitBetaAge());

        this.breeder = new Individual(genes, FishType.BREEDER);
        this.breederAlive = true;

        this.fecundity = Double.NaN;
        this.realFecundity =  Double.NaN;;

        this.helpers = new ArrayList<>();

        for (int i = 0; i < parameters.getInitNumberOfHelpers(); i++) {
            Individual individual = new Individual(genes, FishType.HELPER);
            helpers.add(individual);
        }
    }

    private int getGroupSize() {
        if (breederAlive) {
            return this.getHelperSize() + 1;
        } else {
            return this.getHelperSize();
        }
    }

    public int getHelperSize() {
        return this.helpers.size();
    }

    public DataModel getStatisticalSums() {
        log.debug("calculating statistical sums");

        List<Individual> individuals = new ArrayList<>(helpers);

        Map<DriftType, StatisticalSum> driftStats = this.calculateDriftTypeAttributes(individuals, this.breeder.getGenes().get(GeneType.DRIFT));

        if (breederAlive) {
            individuals.add(this.breeder);
        }

        StatisticalSum age = this.calculateAge(individuals);
        Map<GeneType, StatisticalSum> geneAttributes = this.calculateGeneAttributes(individuals);
        Map<PhenoTypes, StatisticalSum> phenotypeAttributes = this.calculatePhenoTypes(individuals);


        DataModel model = new DataModel();
        model.setPopulationSize(this.getGroupSize());
        model.setAgeStats(age);
        model.setDriftStats(driftStats);
        model.setGeneAttributes(geneAttributes);
        model.setPhenotypeAttributes(phenotypeAttributes);
        return model;
    }

    private Map<DriftType, StatisticalSum> calculateDriftTypeAttributes(List<Individual> helpers, double driftBreeder) {

        double drift;

        StatisticalSum driftH = new StatisticalSum();

        for (Individual individual : helpers) {
            drift = individual.getGenes().get(GeneType.DRIFT);
            driftH.addToSum(drift);
        }

        StatisticalSum driftB = new StatisticalSum();
        driftB.addToSum(driftBreeder);

        StatisticalSum driftBH = new StatisticalSum();
        driftBH.addToSumOfSquare(driftH.getSum(), driftB.getSum());

        StatisticalSum driftBB = new StatisticalSum();
        driftBB.addToSumOfSquare(driftB.getSum());

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
            age.addToSum(individual.getAge());
            age.addToSumOfSquare(individual.getAge());
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
                    statsAttributes.addToSum(geneValue);
                    statsAttributes.addToSumOfSquare(geneValue);
                }

                if (breederAlive && individual.getFishType() == FishType.HELPER) {
                    statsAttributes.addToSum(geneValue);
                    statsAttributes.addToSumOfSquare(geneValue);
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

            if (individual.getFishType() == FishType.HELPER) {
                dispersal.addToSum(individual.getDispersal());
                dispersal.addToSumOfSquare(individual.getDispersal());

                if (individual.isExpressHelp()) {
                    help.addToSum(individual.getHelp());
                    help.addToSumOfSquare(individual.getHelp());

                    helpDispersalProduct.addToSumOfSquare(individual.getHelp(), individual.getDispersal());

                }
            }
            survival.addToSum(individual.getSurvival());
            survival.addToSumOfSquare(individual.getSurvival());
        }


        Map<PhenoTypes, StatisticalSum> phenoTypeMap = new HashMap<>();
        phenoTypeMap.put(PhenoTypes.HELP, help);
        phenoTypeMap.put(PhenoTypes.SURVIVAL, survival);
        phenoTypeMap.put(PhenoTypes.DISPERSAL, dispersal);
        phenoTypeMap.put(PhenoTypes.HELP_DISPERSAL_PRODUCT, helpDispersalProduct);

        return phenoTypeMap;
    }


}
