package org.groupaugmentation.model;

import org.groupaugmentation.model.types.DriftType;
import org.groupaugmentation.model.types.GeneType;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class DataModelTest {

    @Test
    void mergeTest() {
        //given
        DataModel dataModel = new DataModel();
        dataModel.increaseGeneration();
        dataModel.increaseDeath();
        dataModel.setFloatersGenerated(1);
        dataModel.setNewBreederFloater(2);
        dataModel.setNewBreederHelper(3);


        Map<GeneType, StatisticalSum> geneStats = new HashMap<>();
        StatisticalSum beta = new StatisticalSum();
        beta.addSum(5);
        geneStats.put(GeneType.BETA, beta);
        dataModel.setGeneAttributes(geneStats);

        DataModel dataModelToMerge = new DataModel();
        Map<GeneType, StatisticalSum> geneStatsToMerge = new HashMap<>();
        dataModelToMerge.setGeneAttributes(geneStatsToMerge);
        Map<DriftType, StatisticalSum> driftStatsToMerge = new HashMap<>();
        driftStatsToMerge.put(DriftType.BH, new StatisticalSum());

        dataModelToMerge.setDriftStats(driftStatsToMerge);
        dataModelToMerge.increaseGeneration();
        dataModelToMerge.increaseDeath();
        dataModelToMerge.setFloatersGenerated(1);
        dataModelToMerge.setNewBreederFloater(2);
        dataModelToMerge.setNewBreederHelper(3);


        //when
        DataModel result = dataModel.merge(dataModelToMerge);

        //then
        assertEquals(2, result.getGeneration());
        assertEquals(2, result.getDeaths());
        assertEquals(2, result.getFloatersGenerated());
        assertEquals(4, result.getNewBreederFloater());
        assertEquals(3, result.getNewBreederHelper());
        assertNotNull(dataModel.getGroupSize());
        assertNotNull(dataModel.getCumulativeHelp());
        assertNotNull(dataModel.getPhenotypeAttributes());
        assertNotNull(dataModel.getGeneAttributes());
        assertNotNull(dataModel.getAgeStats());
    }

}
