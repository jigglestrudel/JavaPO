package project.code.plants;

import project.code.base.Coordinate;
import project.code.base.Organism;

public class Guarana extends Plant{

    public Guarana(Coordinate cord){
        super(cord);
        setStrength(0);
    }
    @Override
    public void spreadSeeds(Coordinate cord) {
        Guarana sprout = new Guarana(cord);
        //getWorldReference().addEvent(sprout.toString() + " has sprouted");
        getWorldReference().addOrganism(sprout);
    }

    @Override
    public void collision(Organism colliding) {
        colliding.setStrength(colliding.getStrength() + 3);
        super.collision(colliding);
    }
}
