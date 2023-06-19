package project.code.animals;

import project.code.base.Coordinate;
import project.code.base.Organism;

public class Fox extends Animal{

    public Fox(Coordinate position)
    {
        super();
        this.setInitiative(7);
        this.setStrength(3);
        this.setPosition(position);
    }
    @Override
    public boolean breed(Organism potentialPartner) {
        if(potentialPartner instanceof Fox)
        {
            goBack();

            if (this.canBreed && ((Fox) potentialPartner).canBreed)
            {
                Coordinate birthPlace = this.findBirthplace((Animal)potentialPartner);
                if (birthPlace != null) {
                    Fox baby = new Fox(birthPlace);
                    this.getWorldReference().addOrganism(baby);
                }
            }

            return true;
        }
        else return false;
    }

    @Override
    public boolean checkSafety(Coordinate cord) {
        Organism opponent = this.getWorldReference().findOrganismAtPosition(cord);
        if (opponent == null || opponent.getStrength() <= this.getStrength())
        {
            return true;
        }
        else
        {
            this.getWorldReference().addEvent(this.toString() + " senses danger at " + cord.toString());
            return false;
        }

    }
}
