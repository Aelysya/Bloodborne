package bloodborne.entities;

import java.util.Map;

public class Boss extends Enemy {

    private final int BASE_DAMAGE;
    private final double DODGE_RATE;
    private final double FIRE_WEAKNESS;
    private final double BOLT_WEAKNESS;
    private final double ARCANE_WEAKNESS;
    private final int SECOND_PHASE_THRESHOLD;
    private final int THIRD_PHASE_THRESHOLD;

    public Boss(String id, String description, Map<String, String> attributes,
                int baseDamage, double dodgeRate, double fireWeakness, double boltWeakness,
                double arcaneWeakness, int secondPhaseThreshold, int thirdPhaseThreshold) {
        super(id, description, attributes);
        BASE_DAMAGE = baseDamage;
        DODGE_RATE = dodgeRate;
        FIRE_WEAKNESS = fireWeakness;
        BOLT_WEAKNESS = boltWeakness;
        ARCANE_WEAKNESS = arcaneWeakness;
        SECOND_PHASE_THRESHOLD = secondPhaseThreshold;
        THIRD_PHASE_THRESHOLD = thirdPhaseThreshold;
    }

    public int getBASE_DAMAGE() {
        return BASE_DAMAGE;
    }

    public double getDODGE_RATE() {
        return DODGE_RATE;
    }

    public double getFIRE_WEAKNESS() {
        return FIRE_WEAKNESS;
    }

    public double getBOLT_WEAKNESS() {
        return BOLT_WEAKNESS;
    }

    public double getARCANE_WEAKNESS() {
        return ARCANE_WEAKNESS;
    }

    public int getSECOND_PHASE_THRESHOLD() {
        return SECOND_PHASE_THRESHOLD;
    }

    public int getTHIRD_PHASE_THRESHOLD() {
        return THIRD_PHASE_THRESHOLD;
    }

    public Boolean isInSecondPhase(){
        return healthPoints <= SECOND_PHASE_THRESHOLD;
    }

    public Boolean isInThirdPhase(){
        return healthPoints <= THIRD_PHASE_THRESHOLD;
    }
}
