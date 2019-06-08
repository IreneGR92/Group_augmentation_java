package org.groupaugmentation.model;

import org.groupaugmentation.model.types.GeneType;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;


public class DataModelTest {

    @Test
    @Disabled
    //TODO implement that test
    public void test() {
        //given
        DataModel dataModel = new DataModel();
        Map<GeneType, StatisticalSum> geneStats = new HashMap<>();
        StatisticalSum beta = new StatisticalSum();
        beta.addToSum(5);
        geneStats.put(GeneType.BETA, beta);
        dataModel.setGeneAttributes(geneStats);


        DataModel dataModelToMerge = new DataModel();
        Map<GeneType, StatisticalSum> geneStatsToMerge = new HashMap<>();
        StatisticalSum betaToMerge = new StatisticalSum();
        betaToMerge.addToSum(6);
        geneStatsToMerge.put(GeneType.BETA, betaToMerge);
        geneStatsToMerge.put(GeneType.ALPHA, betaToMerge);
        dataModelToMerge.setGeneAttributes(geneStatsToMerge);


        DataModel result = dataModel.merge(dataModelToMerge);


        System.out.println("t");


    }

}
