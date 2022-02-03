package bloodborne.items;

public enum StatScaling {
    S(1.0),
    A(0.8),
    B(0.6),
    C(0.4),
    D(0.25),
    E(0.1),
    NONE(0.0);

    private final double RATIO;

    StatScaling(double ratio) {
        RATIO = ratio;
    }

    double getRATIO() {
        return RATIO;
    }
}
