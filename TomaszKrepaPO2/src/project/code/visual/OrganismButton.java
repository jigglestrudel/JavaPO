package project.code.visual;

import project.code.base.Coordinate;
import project.code.base.Organism;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class OrganismButton extends JButton {
    private Organism organism;
    public Coordinate position;

    public Organism getOrganism() {
        return organism;
    }

    public void setOrganism(Organism organism) {
        this.organism = organism;
    }

    public void updateButtonIcon(HashMap<String, Icon> textureMap) {
        if (organism != null) {
            this.setIcon(textureMap.get(organism.getClass().getSimpleName()));
        }
        else {
            this.setIcon(new ImageIcon());
        }
    }
}
