package org.groupaugmentation.util;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;


@Data
@Slf4j
public final class Parameters {


    private String parametersFileName;

    private boolean reactionNormDispersal;

    private boolean reactionNormHelp;

    private boolean oldSurvivalFormula;

    private int maxColonies;

    private int numGenerations;

    private int maxNumReplicates;

    private int skip;

    private int initNumberOfHelpers;

    private double biasFloadBreeder;

    private double x0;

    private double xsh;

    private double xsn;

    private double k0;

    private double k1;

    private double initAlpha;

    private double initAlphaAge;

    private double initAlphaAge2;

    private double mutationAlpha;

    private double mutationAlphaAge;

    private double mutationAlphaAge2;

    private double stepAlpha;

    private double initBeta;

    private double initBetaAge;

    private double mutationBeta;

    private double mutationBetaAge;

    private double stepBeta;

    private double mutationDrift;

    private double stepDrift;

}
