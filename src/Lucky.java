package capitaly.game;

public class Lucky extends Field {
    private final int luckyAmount;

    public Lucky(int luckyAmount) {
        this.luckyAmount = luckyAmount;
    }

    @Override
    public void landOn(Player p) {
        p.receive(luckyAmount);
    }
}
