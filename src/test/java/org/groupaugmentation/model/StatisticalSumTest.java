package org.groupaugmentation.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class StatisticalSumTest {


    @Test
    void mergeTest() {
        //given
        StatisticalSum toMerge = new StatisticalSum();
        toMerge.addSum(2);

        StatisticalSum toMergeWith = new StatisticalSum();
        toMergeWith.addSum(4);

        //when
        toMerge.merge(toMergeWith);


        //then
        assertNotNull(toMerge);
        assertNotNull(toMergeWith);

        assertEquals(6, toMerge.getSum());
        assertEquals(2, toMerge.getSumCounter());
        assertEquals(5, toMerge.getSumOfSquares());

        assertEquals(4, toMergeWith.getSum());
        assertEquals(4, toMergeWith.getSumOfSquares());
        assertEquals(1, toMergeWith.getSumCounter());

    }

    @Test
    void addToSumTest() {
        //given
        StatisticalSum instanceToTest = new StatisticalSum();
        //when
        instanceToTest.addSum(2);
        instanceToTest.addSum(3);
        //then
        assertNotNull(instanceToTest);
        assertEquals(5, instanceToTest.getSum());
        assertEquals(2, instanceToTest.getSumCounter());
        assertEquals(0, instanceToTest.getSumOfSquares());
    }

}
