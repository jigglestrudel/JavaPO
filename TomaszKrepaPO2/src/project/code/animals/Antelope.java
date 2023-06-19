package project.code.animals;

import project.code.base.Coordinate;
import project.code.base.Organism;

import java.util.ArrayList;
import java.util.Random;

public class Antelope extends Animal{

    public Antelope(Coordinate position)
    {
        super();
        this.setSpeed(2);
        this.setInitiative(4);
        this.setStrength(4);
        this.setPosition(position);
    }
    @Override
    public boolean breed(Organism potentialPartner) {
        if(potentialPartner instanceof Antelope)
        {
            goBack();

            if (this.canBreed && ((Antelope) potentialPartner).canBreed)
            {
                Coordinate birthPlace = this.findBirthplace((Animal)potentialPartner);
                if (birthPlace != null) {
                    Antelope baby = new Antelope(birthPlace);
                    this.getWorldReference().addOrganism(baby);
                }
            }

            return true;
        }
        else return false;
    }

    @Override
    public boolean didRunAway() {
        Random rand = new Random();
        if (rand.nextBoolean())
        {
            ArrayList<Coordinate> potentialRunAway = getWorldReference().getValidMoves(getPosition(),1);
            potentialRunAway.removeIf(coordinate -> getWorldReference().findOrganismAtPosition(coordinate) != null);
            if (potentialRunAway.size() == 0)
                return false;

            setPosition(potentialRunAway.get(rand.nextInt(potentialRunAway.size())));
            getWorldReference().addEvent(this.toString() + " ran away from " + positionBeforeMove.toString());
            return true;
        }
        else return false;
    }
}
