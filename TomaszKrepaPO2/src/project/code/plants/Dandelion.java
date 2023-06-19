package project.code.plants;

import project.code.base.Coordinate;

public class Dandelion extends Plant{

    public Dandelion(Coordinate cord){
        super(cord);
        setStrength(0);
    }

    @Override
    public void action() {
        for (int i = 0; i < 3; i++)
            super.action();
    }

    @Override
    public void spreadSeeds(Coordinate cord) {
        Dandelion sprout = new Dandelion(cord);
        //getWorldReference().addEvent(sprout.toString() + " has sprouted");
        getWorldReference().addOrganism(sprout);
    }

}
