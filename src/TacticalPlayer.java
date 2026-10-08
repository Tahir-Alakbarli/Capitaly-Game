package capitaly.game;

public class TacticalPlayer extends Player {
    private int buyingCount;

    public TacticalPlayer(String name) {
        super(name);
    }

    @Override
    public boolean mustBuy(int cost) {
        if (getBalance() < cost) {
            return false;
        }

        buyingCount++;
        return buyingCount % 2 == 1;
    }
}
