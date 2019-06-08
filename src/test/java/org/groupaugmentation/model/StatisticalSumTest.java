package org.groupaugmentation.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class StatisticalSumTest {


    @Test
    void mergeTest() {
        //given
        StatisticalSum toMerge = new StatisticalSum();
        toMerge.addToSum(2);
        toMerge.addToSumOfSquare(1);

        StatisticalSum toMergeWith = new StatisticalSum();
        toMergeWith.addToSum(4);
        toMergeWith.addToSumOfSquare(2);

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
        instanceToTest.addToSum(2);
        instanceToTest.addToSum(3);
        //then
        assertNotNull(instanceToTest);
        assertEquals(5, instanceToTest.getSum());
        assertEquals(2, instanceToTest.getSumCounter());
        assertEquals(0, instanceToTest.getSumOfSquares());
    }

}
