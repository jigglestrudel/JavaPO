package project.code.animals;

import project.code.base.Coordinate;
import project.code.base.Organism;

import java.util.Random;

public class Human extends Animal{

    private boolean specialPowerActivated;
    private int specialPowerTurnsLeft;
    private Coordinate destination;

    public Human(Coordinate coordinate)
    {
        super();
        setStrength(5);
        setInitiative(4);
        setSpeed(1);
        setPosition(coordinate);
        specialPowerActivated = false;
        specialPowerTurnsLeft = 0;
        destination = coordinate;
    }
    @Override
    public boolean breed(Organism potentialPartner) {
        return false;
    }


    @Override
    public void die() {
        getWorldReference().setHumanAlive(false);
        super.die();
    }

    @Override
    public void action() {
        setPosition(destination);
        specialPowerUpdate();
    }

    @Override
    public int getSpeed() {

        Random random = new Random();
        if (specialPowerActivated)
        {
            if (specialPowerTurnsLeft > 3)
                return 2;
            else if (random.nextBoolean())
                return 2;
        }
        return 1;
    }

    public Coordinate getDestination() {
        return destination;
    }

    public void setDestination(Coordinate destination) {
        this.destination = destination;
    }

    public int getSpecialPowerTurnsLeft() {
        return specialPowerTurnsLeft;
    }

    public void setSpecialPowerTurnsLeft(int specialPowerTurnsLeft) {
        this.specialPowerTurnsLeft = specialPowerTurnsLeft;
    }

    public boolean isSpecialPowerActivated() {
        return specialPowerActivated;
    }

    public void setSpecialPowerActivated(boolean specialPowerActivated) {
        this.specialPowerActivated = specialPowerActivated;
    }


    public void specialPowerUpdate(){
        if (specialPowerActivated) {
            specialPowerTurnsLeft--;
            if (specialPowerTurnsLeft == 0)
                specialPowerActivated = false;
        }
    }
}
