package project.code.animals;

import project.code.base.Coordinate;
import project.code.base.Organism;

import java.util.Random;

public class Tortoise extends Animal{

    public Tortoise(Coordinate position)
    {
        super();
        this.setInitiative(1);
        this.setStrength(2);
        this.setPosition(position);
    }
    @Override
    public boolean breed(Organism potentialPartner) {
        if(potentialPartner instanceof Tortoise)
        {
            goBack();

            if (this.canBreed && ((Tortoise) potentialPartner).canBreed)
            {
                Coordinate birthPlace = this.findBirthplace((Animal)potentialPartner);
                if (birthPlace != null) {
                    Tortoise baby = new Tortoise(birthPlace);
                    getWorldReference().addOrganism(baby);
                }
            }

            return true;
        }
        else return false;
    }

    @Override
    public boolean doesMove() {
        Random rand = new Random();
        return rand.nextBoolean();
    }

    @Override
    public boolean didBounceOffAttack(Organism attacker) {

        if (attacker.getStrength() < 5)
        {
            getWorldReference().addEvent(this.toString() + " uses its shield");
            return true;
        }
        return false;
    }
}
