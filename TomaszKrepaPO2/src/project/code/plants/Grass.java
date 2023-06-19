package project.code.plants;

import project.code.base.Coordinate;

public class Grass extends Plant{

    public Grass(Coordinate cord){
        super(cord);
        setStrength(0);
    }
    @Override
    public void spreadSeeds(Coordinate cord) {
        Grass sprout = new Grass(cord);
        //getWorldReference().addEvent(sprout.toString() + " has sprouted");
        getWorldReference().addOrganism(sprout);
    }
}
