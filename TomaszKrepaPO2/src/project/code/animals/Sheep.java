package project.code.animals;

import project.code.base.Coordinate;
import project.code.base.Organism;

public class Sheep extends Animal{

    public Sheep(Coordinate position)
    {
        super();
        this.setInitiative(4);
        this.setStrength(4);
        this.setPosition(position);
    }
    @Override
    public boolean breed(Organism potentialPartner) {
        if(potentialPartner instanceof Sheep)
        {
            goBack();

            if (this.canBreed && ((Sheep) potentialPartner).canBreed)
            {
                Coordinate birthPlace = this.findBirthplace((Animal)potentialPartner);
                if (birthPlace != null) {
                    Sheep baby = new Sheep(birthPlace);
                    this.getWorldReference().addOrganism(baby);
                }
            }

            return true;
        }
        else return false;
    }
}
