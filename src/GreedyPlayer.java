package capitaly.game;

public class GreedyPlayer extends Player {
    public GreedyPlayer(String name) {
        super(name);
    }

    @Override
    public boolean mustBuy(int cost) {
        return getBalance() >= cost;
    }
}
