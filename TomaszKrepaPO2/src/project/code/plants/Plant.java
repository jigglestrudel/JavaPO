package project.code.plants;

import project.code.base.Coordinate;
import project.code.base.Organism;

import java.util.ArrayList;
import java.util.Random;

public abstract class Plant extends Organism {
    public Plant(Coordinate pos)
    {
        super();
        setPosition( pos);
        setInitiative(0);
    }
    public abstract void spreadSeeds(Coordinate cord);
    @Override
    public void action() {
        if (this.getAge() > 0)
        {
            ArrayList<Coordinate> possibleSeeds = new ArrayList<Coordinate>();
            possibleSeeds = getWorldReference().getValidMoves(this.getPosition(),1);
            possibleSeeds.removeIf(cord -> getWorldReference().checkIfPlaceBlocked(cord));


            Random rand = new Random();

            if (possibleSeeds.size() > 0  && rand.nextInt(5) == 1)
            {
                //getWorldReference().addEvent(this + " spreads");
                int choice = rand.nextInt(possibleSeeds.size());
                spreadSeeds(possibleSeeds.get(choice));
                possibleSeeds.remove(choice);
            }
            else
            {
                //getWorldReference().addEvent(this + " failed to spread");
            }


        }

    }

    @Override
    public void collision(Organism colliding) {
        getWorldReference().addEvent(this + " has been eaten by " + colliding);
        this.die();
    }
}
