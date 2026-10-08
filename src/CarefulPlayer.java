package capitaly.game;

public class CarefulPlayer extends Player {
    public CarefulPlayer(String name) {
        super(name);
    }

    @Override
    public boolean mustBuy(int cost) {
        return cost <= getBalance() / 2;
    }
}
