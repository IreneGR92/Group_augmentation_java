package org.groupaugmentation;

import ch.qos.logback.classic.Logger;
import lombok.extern.slf4j.Slf4j;
import org.groupaugmentation.Group;
import org.groupaugmentation.model.DataModel;
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

    public DataModel printStatistics(final List<Group> groupList, int poulationSize) {
        return this.printStatistics(new DataModel(), groupList, poulationSize);
    }

    public DataModel printStatistics(DataModel dataModel, final List<Group> groupList, int poulationSize) {


        for (Group group : groupList) {
            dataModel.merge(group.getStatisticalSums());
        }
        return dataModel;
    }


}

