package capitaly.game;

public class Property extends Field {
    private Player owner;
    private boolean propertyHouse;
    private final int buyingPrice = 1000;
    private final int buildHousePrice = 4000;
    private final int penalty = 500;
    private final int housePenalty = 2000;

    public Property() {
    }

    @Override
    public void landOn(Player p) {
        if (!isOwned()) {
            if (p.mustBuy(buyingPrice) && p.pay(buyingPrice)) {
                p.buyProperty(this);
            }
            return;
        }

        if (owner == p) {
            if (!propertyHouse
                    && p.mustBuy(buildHousePrice)
                    && p.pay(buildHousePrice)) {
                p.buildHouse(this);
            }
            return;
        }

        int amount = penalty;

        if (propertyHouse) {
            amount = housePenalty;
        }

        if (p.pay(amount)) {
            owner.receive(amount);
        }
    }

    public boolean isOwned() {
        return owner != null;
    }

    public void deleteOwner() {
        owner = null;
        propertyHouse = false;
    }

    void setOwner(Player owner) {
        this.owner = owner;
    }

    void buildHouse() {
        propertyHouse = true;
    }
}
