package org.groupaugmentation.model;

import org.groupaugmentation.model.types.FishType;
import org.groupaugmentation.model.types.GeneType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IndivudualTest {

    private Map<GeneType, Double> genes;

    @BeforeEach
    void setUp() {
        this.genes = new HashMap<>();
        genes.put(GeneType.BETA, 5d);
        genes.put(GeneType.BETA_AGE, 2d);
    }

    @Test
    void calculateDispersalTest() {
        //given
        Individual individual = new Individual(this.genes, FishType.HELPER);
        individual.setAge(10);
        //then
        double result = individual.calculateDispersal(true);

        //then
        assertEquals(3.059022269256247E-7, result);
    }

    @Test
    void calculateDispersalReactNormFalseTest() {
        //given
        genes.put(GeneType.BETA, 1d);
        Individual individual = new Individual(this.genes, FishType.HELPER);
        individual.setAge(10);
        //then
        double result = individual.calculateDispersal(false);

        //then
        assertEquals(1d, result);
    }

}
