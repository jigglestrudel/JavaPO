package project.code.visual;

import project.code.animals.Human;
import project.code.base.Organism;

import javax.swing.*;

public class OrganismLabel {
    public JLabel orgInfo;
    public JLabel orgImg;

    private Organism organism;

    public OrganismLabel()
    {
        orgInfo = new JLabel();
        orgImg = new JLabel();
        organism = null;
    }

    public void setOrganism(Organism organism) {
        this.organism = organism;
    }

    public Organism getOrganism() {
        return organism;
    }

    public void updateLabels(Icon icon)
    {
        updateLabels();

        orgImg.setIcon(icon);
    }

    public void updateLabels()
    {
        if (organism != null && !organism.isAlive())
        {
            organism = null;
        }

        if (organism == null) {
            orgInfo.setText("No organism selected");
            orgImg.setIcon(new ImageIcon());
        }
        else
        {
            orgInfo.setText("<html>" + organism + "<br/>" +
                    "Age: " + organism.getAge() + "<br/>" +
                    "Strength: " + organism.getStrength() + "<br/>" +
                    "Initiative: " + organism.getInitiative() + "</html>");
            if (organism instanceof Human)
            {
                orgInfo.setText("<html>" + organism + "<br/>" +
                        "Age: " + organism.getAge() + "<br/>" +
                        "Strength: " + organism.getStrength() + "<br/>" +
                        "Initiative: " + organism.getInitiative() + "<br/>" +
                        "Special power :" + ((Human) organism).isSpecialPowerActivated() + "<br/>" +
                        "Turns left :" + ((Human) organism).getSpecialPowerTurnsLeft() + "</html>");
            }
        }
    }


}

