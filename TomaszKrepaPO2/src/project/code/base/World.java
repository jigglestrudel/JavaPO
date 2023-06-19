package project.code.base;

import project.code.animals.*;
import project.code.plants.*;

import java.io.*;
import java.util.*;

import static java.lang.Math.abs;

public class World implements Serializable {
    private boolean humanAlive;
    private int height, width, roundCount;
    private final Vector<Organism> organisms;
    private final Vector<Organism> organismsToAdd;
    private final Vector<String> history;

    public World(int w, int h) {
        width = w;
        height = h;
        organisms = new Vector<Organism>();
        history = new Vector<String>();
        organismsToAdd = new Vector<Organism>();
        shuffle();
    }

    public boolean isHumanAlive() {
        return humanAlive;
    }

    public void setHumanAlive(boolean humanAlive) {
        this.humanAlive = humanAlive;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getRoundCount() {
        return roundCount;
    }

    public void setRoundCount(int roundCount) {
        this.roundCount = roundCount;
    }

    public void addOrganism(Organism org) {
        org.setWorldReference(this);
        organismsToAdd.add(org);
    }

    public void directlyAddOrganism(Organism org) {
        org.setWorldReference(this);
        organisms.add(org);
    }

    public Organism findOrganismAtPosition(Coordinate position) {
        Vector<Organism> all = new Vector<Organism>();
        all.addAll(organisms);
        all.addAll(organismsToAdd);
        for (Organism org : all) {
            if (org.isAlive() && org.getPosition().equals(position))
                return org;
        }
        return null;
    }


    public boolean checkIfPlaceBlocked(Coordinate position) {
        Vector<Organism> all = new Vector<Organism>();
        all.addAll(organisms);
        all.addAll(organismsToAdd);
        for (Organism org : all) {
            if (org.isAlive() && org.getPosition().equals(position))
                return true;
        }
        return false;
    }

    public Organism findCollidingOrganism(Organism organism) {

        Vector<Organism> all = new Vector<Organism>();
        all.addAll(organisms);
        all.addAll(organismsToAdd);
        for (Organism org : all) {
            if (org != organism && org.getPosition().equals(organism.getPosition())) {
                if (org.isAlive())
                    return org;
            }
        }
        return null;
    }

    public Organism findOrganismAroundPosition(Coordinate position, boolean isAnimal) {
        Vector<Organism> all = new Vector<Organism>();
        all.addAll(organisms);
        all.addAll(organismsToAdd);
        for (Organism org : all) {
            if (org.isAlive() && org.getPosition().distanceTo(position) < 1.1 && !org.getPosition().equals(position) &&
                    (!isAnimal || org instanceof Animal))
                return org;
        }
        return null;
    }

    public void addEvent(String event) {
        this.history.add(event);
    }

    public boolean isWithinBounds(Coordinate pos) {
        return pos.x >= 0 && pos.x < this.width
                && pos.y >= 0 && pos.y < this.height;
    }

    public ArrayList<Coordinate> getValidMoves(Coordinate pos, int distance) {
        ArrayList<Coordinate> listOfMoves = new ArrayList<Coordinate>();
        for (int dx = -distance; dx <= distance; dx++) {
            for (int dy = -distance; dy <= distance; dy++) {
                Coordinate newPos = pos.sum(new Coordinate(dx, dy));
                if (isWithinBounds(newPos) && pos.distanceTo(newPos) < distance + 0.1 && pos.distanceTo(newPos) > distance - 0.9
                        && !pos.equals(newPos)) {
                    listOfMoves.add(newPos);
                }
            }
        }
        return listOfMoves;
    }

    public void manageOrganisms() {
        organismsToAdd.clear();
        organisms.sort(Organism::compareImportance);

        for (Organism org : organisms) {

            if (org.isAlive()) {
                org.action();
                Organism colliding = findCollidingOrganism(org);
                if (colliding != null) {
                    org.collision(colliding);
                }

            }
        }

        for (Organism org : organisms) {
            org.ageUp();
        }

        organisms.addAll(organismsToAdd);

        organisms.removeIf(org -> !org.isAlive());
    }

    public void nextRound() {
        roundCount++;
        history.clear();
        manageOrganisms();

    }

    public void shuffle() {

        organisms.clear();
        humanAlive = false;
        roundCount = 0;

        Random rand = new Random(System.currentTimeMillis());

        for (int i = 0; i < width * height / 20; i++) {
            Coordinate pos = new Coordinate(rand.nextInt(width), rand.nextInt(height));
            if (checkIfPlaceBlocked(pos)) continue;

            Organism orgToAdd = null;
            switch (AnimalTypes.values()[rand.nextInt(AnimalTypes.values().length)]) {

                case Antelope -> {
                    orgToAdd = new Antelope(pos);
                }
                case Fox -> {
                    orgToAdd = new Fox(pos);
                }
                case Sheep -> {
                    orgToAdd = (new Sheep(pos));
                }
                case Tortoise -> {
                    orgToAdd = (new Tortoise(pos));
                }
                case Wolf -> {
                    orgToAdd = (new Wolf(pos));
                }
            }
            directlyAddOrganism(orgToAdd);
        }
        for (int i = 0; i < width * height / 20; i++) {
            Coordinate pos = new Coordinate(rand.nextInt(width), rand.nextInt(height));
            if (checkIfPlaceBlocked(pos)) continue;

            switch (PlantTypes.values()[rand.nextInt(PlantTypes.values().length)]) {

                case Grass -> {
                    directlyAddOrganism(new Grass(pos));
                }
                case Dandelion -> {
                    directlyAddOrganism(new Dandelion(pos));
                }
                case Guarana -> {
                    directlyAddOrganism(new Guarana(pos));
                }
                case Nightshade -> {
                    directlyAddOrganism(new Nightshade(pos));
                }
                case Hogweed -> {
                    directlyAddOrganism(new Hogweed(pos));
                }
            }
        }

        createHuman();

    }

    public String printState() {
        StringBuilder str = new StringBuilder("Round " + roundCount + "\n");
        for (String e : history) {
            str.append(e).append("\n");
        }

        return str.toString();
    }

    public void createHuman() {
        if (!humanAlive) {
            Random random = new Random();
            humanAlive = true;
            Human player = new Human(new Coordinate(random.nextInt(width), random.nextInt(height)));
            if (findOrganismAtPosition(player.getPosition()) != null)
                findOrganismAtPosition(player.getPosition()).die();
            directlyAddOrganism(player);
        }
    }


    public void setHumanDirection(Directions direction) {
        if (humanAlive) {
            Human human = catchHuman();
            Coordinate coordinate = new Coordinate(0, 0);
            switch (direction) {

                case North -> {
                    coordinate = new Coordinate(0, -1);
                }
                case South -> {
                    coordinate = new Coordinate(0, 1);
                }
                case East -> {
                    coordinate = new Coordinate(1, 0);
                }
                case West -> {
                    coordinate = new Coordinate(-1, 0);
                }
            }

            Coordinate plannedDestination = human.getPosition().sum(coordinate.multiply(human.getSpeed()));
            if (isWithinBounds(plannedDestination)) {
                human.setDestination(plannedDestination);
            } else {
                human.setDestination(human.getPosition());
            }


        }
    }

    public void activateHumanPower() {
        if (humanAlive) {
            Human human = catchHuman();
            if (!human.isSpecialPowerActivated()) {
                human.setSpecialPowerActivated(true);
                human.setSpecialPowerTurnsLeft(5);
            }
        }
    }

    public Human catchHuman() {
        if (humanAlive) {
            for (Organism organism : organisms) {
                if (organism instanceof Human) {
                    return (Human) organism;
                }
            }
        }
        return null;
    }

    public void makeAllOrganismsYours(){
        for (Organism org:organisms)
        {
            org.setWorldReference(this);
        }
    }


}





