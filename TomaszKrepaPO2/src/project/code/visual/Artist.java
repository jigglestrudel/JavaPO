package project.code.visual;

import project.code.animals.*;
import project.code.base.Coordinate;
import project.code.base.Directions;
import project.code.base.World;
import project.code.base.Organism;
import project.code.plants.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Vector;


public class Artist extends JFrame implements KeyListener{
    private static final int tileSize = 30;
    private static final int worldSize = 25;
    private World virtualWorld;
    private OrganismButton[][] buttons;
    private OrganismLabel organismLabel;
    private OrganismLabel humanLabel;
    private JTextArea historyPanel;
    public HashMap<String, Icon> textures;
    private JComboBox<String> dropDownMenu;


    public Artist() {
        setTitle("Tomasz Krepa 193047");
        virtualWorld = new World(worldSize, worldSize);
        textures = new HashMap<String, Icon>();

        setSize(1550,800);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try
        {
            loadTextures();
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }

        initializeButtons();
        updateButtons();

        initializeLabels();
        organismLabel.updateLabels();
        humanLabel.setOrganism(virtualWorld.catchHuman());
        humanLabel.updateLabels();

        initializeHistoryPanel();
        //UpdateHistoryPanel();

        initializeDropDownMenu();

        addKeyListener(this);
        setFocusable(true);

        setVisible(true);
    }

    private void initializeDropDownMenu() {
        Vector<String> list = new Vector<String>();
        for (AnimalTypes a : AnimalTypes.values()){
            list.add(a.name());
        }
        for (PlantTypes a : PlantTypes.values()){
            list.add(a.name());
        }
        dropDownMenu = new JComboBox<String>(list);
        dropDownMenu.setBounds(tileSize*virtualWorld.getWidth()+250, 10, 120, tileSize);
        add(dropDownMenu);
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {

        switch (keyEvent.getKeyCode()) {
            case KeyEvent.VK_UP -> virtualWorld.setHumanDirection(Directions.North);
            case KeyEvent.VK_DOWN -> virtualWorld.setHumanDirection(Directions.South);
            case KeyEvent.VK_LEFT -> virtualWorld.setHumanDirection(Directions.West);
            case KeyEvent.VK_RIGHT -> virtualWorld.setHumanDirection(Directions.East);
        }
        virtualWorld.nextRound();
        overallUpdate();

    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }


    private void UpdateHistoryPanel() {
        historyPanel.append(virtualWorld.printState());
    }

    private void initializeHistoryPanel() {
        historyPanel = new JTextArea();
        historyPanel.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(historyPanel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        scrollPane.setBorder(new TitledBorder("History"));

        scrollPane.setBounds(tileSize*virtualWorld.getWidth()+20, tileSize*7, 400, tileSize*5);
        add(scrollPane);
    }

    private void initializeLabels() {
        organismLabel = new OrganismLabel();
        organismLabel.orgInfo.setBounds(tileSize*virtualWorld.getWidth()+20, tileSize*3, 150, tileSize*4);
        organismLabel.orgImg.setBounds(tileSize*virtualWorld.getWidth()+20, tileSize*2, tileSize, tileSize);
        add(organismLabel.orgInfo);
        add(organismLabel.orgImg);

        humanLabel = new OrganismLabel();
        humanLabel.orgInfo.setBounds(tileSize*virtualWorld.getWidth()+20, tileSize*15, 150, tileSize*4);
        humanLabel.orgImg.setBounds(tileSize*virtualWorld.getWidth()+20, tileSize*12, tileSize, tileSize);
        add(humanLabel.orgInfo);
        add(humanLabel.orgImg);

        JLabel humanInfo = new JLabel();
        humanInfo.setText("Human");
        humanInfo.setBounds(tileSize*virtualWorld.getWidth()+20, tileSize*14, 150, tileSize);
        add(humanInfo);
    }

    private void initializeButtons() {
        //NEXT TURN BUTTON
        JButton nextTurnButton = new JButton("Next turn");
        nextTurnButton.setBounds(virtualWorld.getWidth()*tileSize+20, 10, 100, 20);
        nextTurnButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                virtualWorld.nextRound();
                overallUpdate();

            }
        });
        add(nextTurnButton);
        //Save BUTTON
        JButton saveButton = new JButton("Save");
        saveButton.setBounds(virtualWorld.getWidth()*tileSize+400, 10, 100, 20);
        saveButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                saveGame();

            }
        });
        add(saveButton);
        //Load BUTTON
        JButton loadButton = new JButton("Load ");
        loadButton.setBounds(virtualWorld.getWidth()*tileSize+510, 10, 100, 20);
        loadButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                virtualWorld = loadGame();
                virtualWorld.makeAllOrganismsYours();
                overallUpdate();
            }
        });
        add(loadButton);

        //NEW WORLD BUTTON
        JButton newWorldButton = new JButton("Reroll");
        newWorldButton.setBounds(virtualWorld.getWidth()*tileSize+130, 10, 100, 20);
        newWorldButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                virtualWorld.shuffle();

                organismLabel.setOrganism(null);
                overallUpdate();
                historyPanel.setText("");
            }
        });
        add(newWorldButton);

        //SPECIAL POWER BUTTON
        JButton specialPowerButton = new JButton("Activate Power");
        specialPowerButton.setBounds(virtualWorld.getWidth()*tileSize+20, tileSize*13 , 150, 20);
        specialPowerButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){

                virtualWorld.activateHumanPower();
                overallUpdate();
            }
        });
        add(specialPowerButton);

        //THE WORLD BUTTONS
        buttons = new OrganismButton[virtualWorld.getWidth()][virtualWorld.getHeight()];
        for (int x = 0; x < virtualWorld.getWidth(); x++)
            for (int y = 0; y < virtualWorld.getHeight(); y++)
            {
                OrganismButton b = new OrganismButton();
                b.setBounds(x*tileSize+10, y*tileSize+10, tileSize, tileSize);
                b.setBackground(Color.white);
                b.setBorder(new LineBorder(Color.BLACK));
                b.position = new Coordinate(x, y);
                buttons[x][y] = b;
                b.updateButtonIcon(textures);
                b.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        getFocused();
                        if (b.getOrganism() == null) {

                            int choice = dropDownMenu.getSelectedIndex();
                            Organism org = getOrganismToWorld(choice, b.position);
                            virtualWorld.directlyAddOrganism(org);
                            b.setOrganism(org);
                            b.updateButtonIcon(textures);
                        }
                        else
                        {
                            organismLabel.setOrganism(b.getOrganism());
                            organismLabel.updateLabels(b.getIcon());
                        }
                    }
                });
                add(buttons[x][y]);

            }
    }

    public void getFocused(){
        this.requestFocusInWindow();
    }
    public void updateButtons(){

        for (int x = 0; x < virtualWorld.getWidth(); x++)
            for (int y = 0; y < virtualWorld.getHeight(); y++)
            {
                OrganismButton b = buttons[x][y];
                Organism org = virtualWorld.findOrganismAtPosition(new Coordinate(x, y));
                if (b.getOrganism() != org)
                {
                    b.setOrganism(org);
                    b.updateButtonIcon(textures);
                }
            }
    }

    public void loadTextures() throws IOException {
        for (AnimalTypes animal : AnimalTypes.values()) {
            String name = animal.name();
            Icon icon = createImageIcon("/images/"+name.toLowerCase()+".png");
            if (icon == null)
            {
                throw new IOException("failed to load /images/"+name.toLowerCase()+".png");
            }
            textures.put(name, icon);
        }
        for (PlantTypes plant : PlantTypes.values())
        {
            String name = plant.name();
            Icon icon = createImageIcon("/images/"+name.toLowerCase()+".png");
            if (icon == null)
            {
                throw new IOException("failed to load /images/"+name.toLowerCase()+".png");
            }
            textures.put(name, icon);
        }
        Icon icon = createImageIcon("/images/human.png");
        if (icon == null)
        {
            throw new IOException("failed to load /images/human.png");
        }
        textures.put("Human", icon);
    }

    protected ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(new ImageIcon(imgURL).getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_SMOOTH));
        } else {
            return null;
        }
    }

    private Organism getOrganismToWorld(int choice, Coordinate coordinate)
    {
        Organism org = null;
        if (choice < AnimalTypes.values().length)
            switch (AnimalTypes.values()[choice]){

                case Antelope -> {
                    org = new Antelope(coordinate);
                }
                case Fox -> {
                    org = new Fox(coordinate);
                }
                case Sheep -> {
                    org = new Sheep(coordinate);
                }
                case Tortoise -> {
                    org = new Tortoise(coordinate);
                }
                case Wolf -> {
                    org = new Wolf(coordinate);
                }
            }
        else{
            switch (PlantTypes.values()[choice - AnimalTypes.values().length]){

                case Grass -> {
                    org = new Grass(coordinate);
                }
                case Dandelion -> {
                    org = new Dandelion(coordinate);
                }
                case Guarana -> {
                    org = new Guarana(coordinate);
                }
                case Nightshade -> {
                    org = new Nightshade(coordinate);
                }
                case Hogweed -> {
                    org = new Hogweed(coordinate);
                }
            }

        }
        return org;

    }

    private void overallUpdate(){
        getFocused();
        updateButtons();
        organismLabel.updateLabels();
        humanLabel.setOrganism(virtualWorld.catchHuman());
        humanLabel.updateLabels();
        UpdateHistoryPanel();
    }

    public void saveGame() {
        try (FileOutputStream fos = new FileOutputStream("save.sav");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(virtualWorld);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static World loadGame() {
        World gameState = null;

        try (FileInputStream fis = new FileInputStream("save.sav");
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            gameState = (World) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return gameState;
    }
}

