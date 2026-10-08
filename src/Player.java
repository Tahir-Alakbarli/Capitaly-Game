package capitaly.game;

import java.util.ArrayList;

public abstract class Player {
    private final String name;
    private int playerBalance = 10000;
    private int playerPosition;
    private final ArrayList<Property> playerProperties;
    private boolean eliminated;

    public Player(String name) {
        this.name = name;
        playerProperties = new ArrayList<>();
    }

    public void move(int steps, int boardSize) {
        playerPosition = (playerPosition + steps) % boardSize;
    }

    public abstract boolean mustBuy(int cost);

    public void buyProperty(Property p) {
        playerProperties.add(p);
        p.setOwner(this);
    }

    public void buildHouse(Property p) {
        p.buildHouse();
    }

    public boolean pay(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("The payment cannot be negative.");
        }

        if (playerBalance < amount) {
            lose();
            return false;
        }

        playerBalance -= amount;
        return true;
    }

    public void receive(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("The received amount cannot be negative.");
        }

        playerBalance += amount;
    }

    public void lose() {
        if (eliminated) {
            return;
        }

        eliminated = true;
        playerBalance = 0;

        for (Property property : playerProperties) {
            property.deleteOwner();
        }

        playerProperties.clear();
    }

    public int getBalance() {
        return playerBalance;
    }

    public Property[] getProperties() {
        Property[] properties = new Property[playerProperties.size()];

        for (int i = 0; i < playerProperties.size(); i++) {
            properties[i] = playerProperties.get(i);
        }

        return properties;
    }

    public String getName() {
        return name;
    }

    public int getPosition() {
        return playerPosition;
    }

    public boolean isEliminated() {
        return eliminated;
    }
}