package project.code.plants;

import project.code.base.Coordinate;
import project.code.base.Organism;

import java.util.Random;

public class Hogweed extends Plant{

    public Hogweed(Coordinate cord){
        super(cord);
        setStrength(10);
    }
    @Override
    public void spreadSeeds(Coordinate cord) {
        Hogweed sprout = new Hogweed(cord);
        //getWorldReference().addEvent(sprout.toString() + " has sprouted");
        getWorldReference().addOrganism(sprout);
    }

    @Override
    public void action() {
        Organism org = getWorldReference().findOrganismAroundPosition(getPosition(), true);
        while (org != null)
        {
            getWorldReference().addEvent(org.toString() + " has accidentally touched " + this.toString());
            org.die();
            org = getWorldReference().findOrganismAroundPosition(getPosition(), true);
        }

        Random rand = new Random();
        if (rand.nextInt(20) == 1)
            super.action();
    }

    @Override
    public void collision(Organism colliding) {
        super.collision(colliding);
        colliding.die();
    }
}
