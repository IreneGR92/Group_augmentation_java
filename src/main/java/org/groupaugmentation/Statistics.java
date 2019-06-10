package org.groupaugmentation;

import ch.qos.logback.classic.Logger;
import lombok.extern.slf4j.Slf4j;
import org.decimal4j.util.DoubleRounder;
import org.groupaugmentation.model.DataModel;
import org.groupaugmentation.model.types.GeneType;
import org.groupaugmentation.model.types.PhenoTypes;
import org.groupaugmentation.util.LoggerUtils;
import org.groupaugmentation.util.Parameters;

import java.util.List;

@Slf4j
public final class Statistics {

    private final Logger resultsFileLogger;

    private final Logger lastGenerationLogger;

    private final Parameters parameters;

    public Statistics(Parameters parameters) {
        this.parameters = parameters;
        resultsFileLogger = LoggerUtils.createLoggerFor("group_augmentation_.txt", "results/" + parameters.getParametersFileName() + "/group_augmentation_.txt");
        lastGenerationLogger = LoggerUtils.createLoggerFor("group_augmentation_last_generation_.txt", "results/" + parameters.getParametersFileName() + "/group_augmentation_last_generation_.txt");
    }

    public static double getCorrelation(double product, double meanA, double meanB, int count) {
        return product / count - meanA * meanB;
    }

    public void printHeadlines() {
        // column headings in output file 1
        String headlineResultsFile = "Generation\tPopulation\tDeaths\tFloaters\tGroup_size\tAge\tmeanAlpha\tmeanAlphaAge\t" +
                "meanAlphaAge2\tmeanBeta\tmeanBetaAge\tmeanHelp\tmeanCumHelp\tmeanDispersal\tmeanSurvival\t" +
                "Relatedness\tSD_GroupSize\tSD_Age\tSD_Alpha\tSD_AlphaAge\tSD_AlphaAge2\tSD_Beta\tSD_BetaAge\t" +
                "SD_Help\tSD_CumHelp\tSD_Dispersal\tSD_Survival\tcorr_Help_Disp\tcorr_Help_Group\tnewbreederFloater\tnewbreederHelper\tinheritance"
                + System.getProperty("line.separator");

        String headlineLastGenerationFile = "replica\tgroupID\ttype\tageStats\talpha\talphaAge\talphaAge2\tbeta\tbetaAge\tdrift\thelp\tdispersal\tsurvival"
                + System.getProperty("line.separator");


        this.resultsFileLogger.info(headlineResultsFile);
        this.lastGenerationLogger.info(headlineLastGenerationFile);
        log.info(headlineResultsFile);
    }

    public void printParameters() {

        Logger[] fileLoggers = {this.resultsFileLogger, this.lastGenerationLogger};

        for (Logger log : fileLoggers) {
            log.info("PARAMETER VALUES");
            log.info("Reaction_norm_help: \t" + parameters.isReactionNormHelp());
            log.info("Reaction_norm_dispersal: \t" + parameters.isReactionNormDispersal());
            log.info("Initial_population: \t" + parameters.getMaxColonies() * (parameters.getInitNumberOfHelpers() + 1));
            log.info("Number_of_colonies: \t" + parameters.getMaxColonies());
            log.info("Number_generations: \t" + parameters.getNumGenerations());
            log.info("Number_replicates: \t" + parameters.getMaxNumReplicates());
            log.info("Bias_float_breeder: \t" + parameters.getBiasFloadBreeder());
            log.info("Base_survival: \t" + parameters.getX0());
            log.info("Cost_help: \t" + parameters.getXsh());
            log.info("Benefit_group_size: \t" + parameters.getXsn());
            log.info("K0: \t" + parameters.getK0());
            log.info("K1: \t" + parameters.getK1());
            log.info("initAlpha: \t" + parameters.getInitAlpha());
            log.info("initAlphaAge: \t" + parameters.getInitAlphaAge());
            log.info("initAlphaAge2: \t" + parameters.getInitAlphaAge2());
            log.info("initBeta: \t" + parameters.getInitBeta());
            log.info("initBetaAge: \t" + parameters.getInitBetaAge());
            log.info("mutAlpha: \t" + parameters.getMutationAlpha());
            log.info("mutAlphaAge: \t" + parameters.getMutationAlphaAge());
            log.info("mutAlphaAge2: \t" + parameters.getMutationAlphaAge2());
            log.info("mutBeta: \t" + parameters.getMutationBeta());
            log.info("mutBetaAge: \t" + parameters.getMutationBetaAge());
            log.info("mutDrift: \t" + parameters.getMutationDrift());
            log.info("stepAlpha: \t" + parameters.getStepAlpha());
            log.info("stepBeta: \t" + parameters.getStepBeta());
            log.info("stepDrift: \t" + parameters.getStepDrift() + System.getProperty("line.separator"));
        }

    }

    /**
     * In case no DataModel exists yet use this function.
     */
    public DataModel printStatistics(final List<Group> groupList) {
        return this.printStatistics(new DataModel(), groupList);
    }

    public DataModel printStatistics(DataModel dataModel, final List<Group> groupList) {

        final String TAB = "\t";

        for (Group group : groupList) {
            dataModel.merge(group.getDataModel());
        }


        String line = dataModel.getGeneration() + TAB
                + dataModel.getGroupSize().getSum() + TAB
                + dataModel.getDeaths() + TAB
                + dataModel.getFloatersGenerated() + TAB
                + formatDouble(dataModel.getGroupSize().getMean()) + TAB
                + formatDouble(dataModel.getAgeStats().getMean()) + TAB
                + formatDouble(dataModel.getGeneAttributes().get(GeneType.ALPHA).getMean()) + TAB
                + formatDouble(dataModel.getGeneAttributes().get(GeneType.ALPHA_AGE).getMean()) + TAB
                + formatDouble(dataModel.getGeneAttributes().get(GeneType.ALPHA_AGE2).getMean()) + TAB
                + formatDouble(dataModel.getGeneAttributes().get(GeneType.BETA).getMean()) + TAB
                + formatDouble(dataModel.getGeneAttributes().get(GeneType.BETA_AGE).getMean()) + TAB
                + formatDouble(dataModel.getPhenotypeAttributes().get(PhenoTypes.HELP).getMean()) + TAB
                + formatDouble(dataModel.getCumulativeHelp().getMean()) + TAB
                + formatDouble(dataModel.getPhenotypeAttributes().get(PhenoTypes.DISPERSAL).getMean()) + TAB
                + formatDouble(dataModel.getPhenotypeAttributes().get(PhenoTypes.SURVIVAL).getMean()) + TAB
                + formatDouble(dataModel.getRelatedness()) + TAB
                + formatDouble(dataModel.getGroupSize().getStandardDeviation()) + TAB
                + formatDouble(dataModel.getAgeStats().getStandardDeviation()) + TAB
                + formatDouble(dataModel.getGeneAttributes().get(GeneType.ALPHA).getStandardDeviation()) + TAB
                + formatDouble(dataModel.getGeneAttributes().get(GeneType.ALPHA_AGE).getStandardDeviation()) + TAB
                + formatDouble(dataModel.getGeneAttributes().get(GeneType.ALPHA_AGE2).getStandardDeviation()) + TAB
                + formatDouble(dataModel.getGeneAttributes().get(GeneType.BETA).getStandardDeviation()) + TAB
                + formatDouble(dataModel.getGeneAttributes().get(GeneType.BETA_AGE).getStandardDeviation()) + TAB
                + formatDouble(dataModel.getPhenotypeAttributes().get(PhenoTypes.HELP).getStandardDeviation()) + TAB
                + formatDouble(dataModel.getCumulativeHelp().getStandardDeviation()) + TAB
                + formatDouble(dataModel.getPhenotypeAttributes().get(PhenoTypes.DISPERSAL).getStandardDeviation()) + TAB
                + formatDouble(dataModel.getPhenotypeAttributes().get(PhenoTypes.SURVIVAL).getStandardDeviation()) + TAB

                + formatDouble(
                Statistics.getCorrelation(dataModel.getPhenotypeAttributes().get(PhenoTypes.HELP_DISPERSAL_PRODUCT).getSum(),
                        dataModel.getPhenotypeAttributes().get(PhenoTypes.HELP).getMean(),
                        dataModel.getPhenotypeAttributes().get(PhenoTypes.DISPERSAL).getMean(),
                        dataModel.getPhenotypeAttributes().get(PhenoTypes.HELP_DISPERSAL_PRODUCT).getSumCounter())
        ) + TAB
                + 0 + TAB //TODO implement correlation between groupsize and cumulative help
                + dataModel.getNewBreederFloater() + TAB
                + dataModel.getNewBreederHelper() + TAB
                + "inheritance";


        this.resultsFileLogger.info(line);
        log.info(line);
        return dataModel;
    }


    private String formatDouble(double toFormat) {
        final int precision = 4;
        return String.format("%.0" + precision + "f", DoubleRounder.round(toFormat, precision));
    }
}

