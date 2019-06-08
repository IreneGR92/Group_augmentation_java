package org.groupaugmentation;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.groupaugmentation.model.DataModel;
import org.groupaugmentation.model.Individual;
import org.groupaugmentation.util.Parameters;
import org.groupaugmentation.util.RandomNumberGenerator;

import java.util.ArrayList;
import java.util.List;

//@Slf4j initializes the Logger
@Slf4j
public class Simulation {

    private final Parameters parameters;

    private final Statistics statistics;

    private final RandomNumberGenerator randomNumberGenerator;

    @Getter
    private int generation = 0;

    @Getter
    private int deaths = 0;

    @Getter
    private int populationSize = 0;

    @Getter
    private int populationSizeBeforeSurvival = 0;

    @Getter
    private int floatersGenerated = 0;

    @Getter
    private int newBreederFloater = 0;

    @Getter
    private int newBreederHelper = 0;

    @Getter
    private int inheritance = 0;

    private List<Individual> floaters;

    private List<Group> groups;

    public Simulation(Parameters parameters) {
        this.parameters = parameters;
        this.statistics = new Statistics(parameters);
        this.randomNumberGenerator = new RandomNumberGenerator(parameters);
        this.groups = this.initGroups();
    }

    public void simulate() {
        log.trace("simulate()");
        statistics.printParameters();
        statistics.printHeadlines();

        DataModel dataModel = statistics.printStatistics(this.groups, 3);

        for (generation = 1; generation <= parameters.getNumGenerations(); generation++) {


        }


    }

    private List<Group> initGroups() {
        List<Group> groups = new ArrayList<>();

        for (int i = 0; i < parameters.getMaxColonies(); i++) {
            Group group = new Group(parameters, randomNumberGenerator);
            groups.add(group);
            log.trace(group.toString());
        }
        return groups;
    }


}
