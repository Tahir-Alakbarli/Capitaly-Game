package capitaly.game;

public class Service extends Field {
    private final int serviceCost;

    public Service(int serviceCost) {
        this.serviceCost = serviceCost;
    }

    @Override
    public void landOn(Player p) {
        p.pay(serviceCost);
    }
}