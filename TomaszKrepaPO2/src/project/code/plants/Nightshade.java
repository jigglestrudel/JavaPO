package project.code.plants;

import project.code.base.Coordinate;
import project.code.base.Organism;

public class Nightshade extends Plant{

    public Nightshade(Coordinate cord){
        super(cord);
        setStrength(99);
    }
    @Override
    public void spreadSeeds(Coordinate cord) {
        Nightshade sprout = new Nightshade(cord);
        //getWorldReference().addEvent(sprout.toString() + " has sprouted");
        getWorldReference().addOrganism(sprout);
    }

    @Override
    public void collision(Organism colliding) {
        super.collision(colliding);
        colliding.die();
    }
}
