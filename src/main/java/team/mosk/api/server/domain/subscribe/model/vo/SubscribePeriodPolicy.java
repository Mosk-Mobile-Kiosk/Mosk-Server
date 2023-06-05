package team.mosk.api.server.domain.subscribe.model.vo;

public enum SubscribePeriodPolicy {
    S(1L),
    R(3L),
    VIP(6L);

    private final Long period;

    private SubscribePeriodPolicy(Long period) {
        this.period = period;
    }

    public Long getPeriod() {
        return period;
    }

}
