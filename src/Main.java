package capitaly.game;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {
        String capitalyFile = "capitaly.txt";
        String diceRollsFile = "diceRolls.txt";

        try {
            Capitaly capitaly = new Capitaly();

            capitaly.readCapitaly(capitalyFile);
            capitaly.readDiceRolls(diceRollsFile);
            capitaly.play();

            Player winner = capitaly.getWinner();

            if (winner == null) {
                System.out.println("The game has no winner.");
                return;
            }

            System.out.println("Winner: " + winner.getName());
            System.out.println("Balance: " + winner.getBalance());
            System.out.println(
                    "Owned properties: "
                    + winner.getProperties().length);
        } catch (FileNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (InvalidDataException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
