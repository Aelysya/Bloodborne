package bloodborne.items;

public enum StatScaling {
    S(1.0),
    A(0.9),
    B(0.75),
    C(0.5),
    D(0.4),
    E(0.25),
    NONE(0.0);

    private final double RATIO;

    StatScaling(double ratio){
        RATIO = ratio;
    }

    double getRATIO(){
        return RATIO;
    }
}
