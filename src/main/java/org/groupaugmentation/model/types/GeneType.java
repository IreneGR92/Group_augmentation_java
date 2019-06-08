package org.groupaugmentation.model.types;

import java.util.stream.Stream;

public enum GeneType {
    ALPHA, ALPHA_AGE, ALPHA_AGE2, BETA, BETA_AGE, DRIFT;

    public static Stream<GeneType> stream() {
        return Stream.of(GeneType.values());
    }
}
