package org.groupaugmentation.model.fishtype;

public class Floater extends Individual {

    public Floater(Helper helper) {
        super(helper.getGenes());
        this.expressHelp = false;
    }
}
