package capitaly.game;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Capitaly {
    private ArrayList<Field> fields;
    private ArrayList<Player> players;
    private ArrayList<Integer> diceRoll;

    public Capitaly() {
        fields = new ArrayList<>();
        players = new ArrayList<>();
        diceRoll = new ArrayList<>();
    }

    public void readCapitaly(String fileName)
            throws FileNotFoundException, InvalidDataException {

        Scanner scanner = new Scanner(new FileReader(fileName));
        ArrayList<Field> loadedFields = new ArrayList<>();
        ArrayList<Player> loadedPlayers = new ArrayList<>();

        int fieldCount = readNumber(scanner, "number of fields");

        for (int i = 0; i < fieldCount; i++) {
            String fieldType = readFile(scanner, "field type");

            if (fieldType.equalsIgnoreCase("PROPERTY")) {
                loadedFields.add(new Property());
            } else if (fieldType.equalsIgnoreCase("SERVICE")) {
                int serviceCost =
                        readNumber(scanner, "service cost");
                loadedFields.add(new Service(serviceCost));
            } else if (fieldType.equalsIgnoreCase("LUCKY")) {
                int luckyAmount =
                        readNumber(scanner, "lucky amount");
                loadedFields.add(new Lucky(luckyAmount));
            } else {
                scanner.close();
                throw new InvalidDataException(
                        "Unknown field type: " + fieldType);
            }
        }

        int playerCount =
                readNumber(scanner, "number of players");

        if (playerCount < 2) {
            scanner.close();
            throw new InvalidDataException(
                    "At least two players are required.");
        }

        for (int i = 0; i < playerCount; i++) {
            String name = readFile(scanner, "player name");
            String strategy = readFile(scanner, "player strategy");

            if (strategy.equalsIgnoreCase("GREEDY")) {
                loadedPlayers.add(new GreedyPlayer(name));
            } else if (strategy.equalsIgnoreCase("CAREFUL")) {
                loadedPlayers.add(new CarefulPlayer(name));
            } else if (strategy.equalsIgnoreCase("TACTICAL")) {
                loadedPlayers.add(new TacticalPlayer(name));
            } else {
                scanner.close();
                throw new InvalidDataException(
                        "Unknown player strategy: " + strategy);
            }
        }

        if (scanner.hasNext()) {
            String extraData = scanner.next();
            scanner.close();
            throw new InvalidDataException(
                    "Unexpected extra data: " + extraData);
        }

        scanner.close();
        fields = loadedFields;
        players = loadedPlayers;
    }

    public void readDiceRolls(String fileName)
            throws FileNotFoundException, InvalidDataException {

        Scanner scanner = new Scanner(new FileReader(fileName));
        ArrayList<Integer> loadedDiceRoll = new ArrayList<>();

        while (scanner.hasNext()) {
            if (!scanner.hasNextInt()) {
                String invalidRoll = scanner.next();
                scanner.close();
                throw new InvalidDataException(
                        "Invalid dice roll: " + invalidRoll);
            }

            int roll = scanner.nextInt();

            if (roll < 1 || roll > 6) {
                scanner.close();
                throw new InvalidDataException(
                        "A dice roll must be between 1 and 6: " + roll);
            }

            loadedDiceRoll.add(roll);
        }

        scanner.close();

        if (loadedDiceRoll.isEmpty()) {
            throw new InvalidDataException(
                    "The dice-roll file is empty.");
        }

        diceRoll = loadedDiceRoll;
    }

    public void play() {
        if (fields.isEmpty() || players.isEmpty()) {
            throw new IllegalStateException(
                    "The game configuration has not been loaded.");
        }

        if (diceRoll.isEmpty()) {
            throw new IllegalStateException(
                    "The dice rolls have not been loaded.");
        }

        int playerIndex = 0;

        for (int roll : diceRoll) {
            if (countActivePlayers() == 1) {
                return;
            }

            while (players.get(playerIndex).isEliminated()) {
                playerIndex =
                        (playerIndex + 1) % players.size();
            }

            movePlayer(players.get(playerIndex), roll);
            playerIndex =
                    (playerIndex + 1) % players.size();
        }

        if (countActivePlayers() != 1) {
            throw new IllegalStateException(
                    "The dice rolls ended before the game had a winner.");
        }
    }

    public void movePlayer(Player p, int roll) {
        if (roll < 1 || roll > 6) {
            throw new IllegalArgumentException(
                    "A dice roll must be between 1 and 6.");
        }

        p.move(roll, fields.size());
        fields.get(p.getPosition()).landOn(p);
    }

    public Player getWinner() {
        Player winner = null;

        for (Player player : players) {
            if (!player.isEliminated()) {
                if (winner != null) {
                    return null;
                }

                winner = player;
            }
        }

        return winner;
    }

    private int countActivePlayers() {
        int count = 0;

        for (Player player : players) {
            if (!player.isEliminated()) {
                count++;
            }
        }

        return count;
    }

    private String readFile(
            Scanner scanner,
            String description) throws InvalidDataException {

        if (!scanner.hasNext()) {
            throw new InvalidDataException(
                    "Missing " + description + ".");
        }

        return scanner.next();
    }

    private int readNumber(
            Scanner scanner,
            String description) throws InvalidDataException {

        String valueFromFile = readFile(scanner, description);
        int value;

        try {
            value = Integer.parseInt(valueFromFile);
        } catch (NumberFormatException e) {
            throw new InvalidDataException(
                    "Invalid " + description + ": " + valueFromFile);
        }

        if (value <= 0) {
            throw new InvalidDataException(
                    "The " + description + " must be positive.");
        }

        return value;
    }
}