package project.code.animals;

import project.code.base.Coordinate;
import project.code.base.Organism;
import project.code.plants.Plant;

import java.util.ArrayList;
import java.util.Random;

public abstract class Animal extends Organism{

    private int speed;
    protected boolean canBreed;

    protected Coordinate positionBeforeMove;


    public Animal()
    {
        super();
        speed = 1;
        canBreed = false;
        positionBeforeMove = getPosition();
    }
    public boolean isCanBreed() {
        return canBreed;
    }

    @Override
    public void ageUp() {
        canBreed = true;
        super.ageUp();
    }

    @Override
    public void die() {
        getWorldReference().addEvent(this + " has died");
        super.die();
    }

    public void goBack() {
        // MAYBE TODO CHECK IF POSITION CAN BE RETURNED TO

        this.setPosition(positionBeforeMove);
    }

    public boolean didRunAway() {
        return false;
    }
    public boolean didBounceOffAttack(Organism attacker) {
        return false;
    }

    public boolean checkSafety(Coordinate cord)
    {
        return true;
    }

    public boolean doesMove()
    {
        return true;
    }

    @Override
    public void action() {
        ArrayList<Coordinate> moves = getWorldReference().getValidMoves(this.getPosition(), this.speed);
        //System.out.println(moves);
        moves.removeIf(cord -> !checkSafety(cord));
        Random rand = new Random();

        if (moves.size() > 0 && doesMove())
        {
            setPosition(moves.get(rand.nextInt(moves.size())));
            //getWorldReference().addEvent(toString() + " moves from " + positionBeforeMove.toString());

        }
    }

    public abstract boolean breed(Organism potentialPartner);
    @Override
    public void collision(Organism colliding) {

        if (colliding instanceof Animal)
        {
            boolean didBreed = this.breed(colliding);
            if (didBreed)
            {
                this.canBreed = false;
                ((Animal) colliding).canBreed = false;
                getWorldReference().addEvent(this.toString() + " and " + colliding.toString() + " created new life");
            }
            else if (((Animal) colliding).didRunAway())
            {
                return;
            }
            else if (((Animal) colliding).didBounceOffAttack(this))
            {
                this.goBack();
            }
            else if (this.getStrength() >= colliding.getStrength())
            {
                getWorldReference().addEvent(this.toString() + " killed " + colliding.toString());
                colliding.die();

            }
            else
            {
                getWorldReference().addEvent(this.toString() + " has been killed by " + colliding.toString());
                this.die();
            }
        }
        if (colliding instanceof Plant)
        {
            colliding.collision(this);
        }


    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public Coordinate findBirthplace(Animal partner)
    {
        ArrayList<Coordinate> birthPlace = this.getWorldReference().getValidMoves(getPosition(), 1);
        ArrayList<Coordinate> partnerBirthPlace = partner.getWorldReference().getValidMoves(getPosition(), 1);
        birthPlace.addAll(partnerBirthPlace);
        birthPlace.removeIf(coordinate -> getWorldReference().checkIfPlaceBlocked(coordinate));


        if (birthPlace.size() > 0)
        {
            Random rand = new Random();
            return birthPlace.get(rand.nextInt(birthPlace.size()));
        }
        else return null;
    }

    @Override
    public void setPosition(Coordinate position) {
        positionBeforeMove = getPosition();
        super.setPosition(position);
    }
}
