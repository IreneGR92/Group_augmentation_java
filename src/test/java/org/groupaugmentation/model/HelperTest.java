package org.groupaugmentation.model;

import org.groupaugmentation.model.fishtype.Helper;
import org.groupaugmentation.model.types.GeneType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HelperTest {

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
        Helper helper = new Helper(this.genes);
        helper.setAge(10);
        //then
        double result = helper.calculateDispersal(true);

        //then
        assertEquals(3.059022269256247E-7, result);
    }

    @Test
    void calculateDispersalReactNormFalseTest() {
        //given
        genes.put(GeneType.BETA, 1d);
        Helper helper = new Helper(this.genes);
        helper.setAge(10);
        //then
        double result = helper.calculateDispersal(false);

        //then
        assertEquals(1d, result);
    }

}
