package project.code.base;

import java.io.Serializable;

public abstract class Organism implements Serializable {
    private int strength, initiative;
    private int age;
    private boolean alive;
    private Coordinate position;

    private transient World worldReference;

    public Organism()
    {
        strength = 0;
        initiative = 0;
        age = 0;
        alive = true;
        position = new Coordinate(0, 0);
        worldReference = null;
    }
    public void setAge(int age) {
        this.age = age;
    }

    public boolean isAlive() {
        return alive;
    }

    public int getInitiative() {
        return initiative;
    }

    public void setInitiative(int initiative) {
        this.initiative = initiative;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getAge() {
        return age;
    }

    public void ageUp()
    {
        age++;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void die() {
        this.alive = false;
    }

    public Coordinate getPosition() {
        return position;
    }

    public void setPosition(Coordinate position) {
        this.position = position;
    }

    public abstract void action();
    public abstract void collision(Organism colliding);

    public World getWorldReference() {
        return worldReference;
    }

    public void setWorldReference(World worldReference) {
        this.worldReference = worldReference;
    }

    public int compareImportance(Organism org2)
    {
        if (this.getInitiative() == org2.getInitiative()) {
            return org2.getAge() - this.getAge() ;
        } else {
            return org2.getInitiative() - this.getInitiative();
        }
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " at " + this.getPosition().toString();
    }
}
