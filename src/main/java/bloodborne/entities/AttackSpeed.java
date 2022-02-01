package bloodborne.entities;

public enum AttackSpeed {
    FAST(-0.1),
    MEDIUM(0.0),
    SLOW(0.1),
    VERY_SLOW(0.2);

    private final double DODGE_MODIFICATION;

    AttackSpeed(double dodgeModification) {
        DODGE_MODIFICATION = dodgeModification;
    }

    double getDODGE_MODIFICATION() {
        return DODGE_MODIFICATION;
    }
}
