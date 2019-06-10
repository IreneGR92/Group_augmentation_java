package org.groupaugmentation.model.fishtype;

import lombok.extern.slf4j.Slf4j;
import org.groupaugmentation.model.types.GeneType;

import java.util.Map;

@Slf4j
public class Breeder extends Individual {

    public Breeder(Map<GeneType, Double> genes) {
        super(genes);
    }


}
