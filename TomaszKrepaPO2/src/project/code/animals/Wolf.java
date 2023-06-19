package project.code.animals;

import project.code.base.Coordinate;
import project.code.base.Organism;

public class Wolf extends Animal{

    public Wolf(Coordinate position)
    {
        super();
        this.setInitiative(5);
        this.setStrength(9);
        this.setPosition(position);
    }
    @Override
    public boolean breed(Organism potentialPartner) {
        if(potentialPartner instanceof Wolf)
        {
            goBack();

            if (this.canBreed && ((Wolf) potentialPartner).canBreed)
            {
                Coordinate birthPlace = this.findBirthplace((Animal)potentialPartner);
                if (birthPlace != null) {
                    Wolf baby = new Wolf(birthPlace);
                    this.getWorldReference().addOrganism(baby);
                }
            }

            return true;
        }
        else return false;
    }
}
