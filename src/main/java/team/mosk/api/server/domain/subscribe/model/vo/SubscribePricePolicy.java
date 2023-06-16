package team.mosk.api.server.domain.subscribe.model.vo;

public enum SubscribePricePolicy {

    S(50000L),
    R(135000L),
    VIP(255000L);

    private final Long price;

    private SubscribePricePolicy(Long price) {
        this.price = price;
    }

    public Long getPrice() {
        return price;
    }
}
