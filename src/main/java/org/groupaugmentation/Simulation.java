package org.groupaugmentation;

import lombok.extern.slf4j.Slf4j;
import org.groupaugmentation.model.DataModel;
import org.groupaugmentation.model.fishtype.Floater;
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


    private List<Floater> floaters;

    private List<Group> groups;

    public Simulation(Parameters parameters) {
        this.parameters = parameters;
        this.statistics = new Statistics(parameters);
        this.randomNumberGenerator = new RandomNumberGenerator(parameters);
        this.groups = this.initGroups();
        this.floaters = new ArrayList<>();
    }

    public void simulate() {
        log.trace("calculateHelp()");
        statistics.printParameters();
        statistics.printHeadlines();

        DataModel dataModel = statistics.printStatistics(this.groups);

        dataModel.increaseGeneration();
        for (; dataModel.getGeneration() <= parameters.getNumGenerations(); dataModel.increaseGeneration()) {

            for (Group group : groups) {
                this.floaters.addAll(group.disperse());
            }

            for (Group group : groups) {
                group.calculateCumulativeHelp();
            }

            for (Group group : groups) {
                dataModel.setDeaths(dataModel.getDeaths() + group.groupSurvival());
            }
            dataModel.setDeaths(dataModel.getDeaths() + this.floaterSurvival());


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


    private int floaterSurvival() {

        int deaths = 0;
        //calculate survival
        for (Floater floater : floaters) {
            floater.calculateSurvival(parameters, 0);
        }

        //kill them
        for (Floater floater : floaters) {
            if (randomNumberGenerator.getNextRealUniform() > floater.getSurvival()) {
                floaters.remove(floater);
                deaths++;

            }
        }
        return deaths;
    }

}
