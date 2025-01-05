package attacks;

import ru.ifmo.se.pokemon.*;

public class EnergyBall extends SpecialMove {
    public EnergyBall() {
        super(Type.GRASS, 90, 100);
    }

    private boolean isMinusSpDef;
    @Override
    protected void applyOppEffects(Pokemon p) {
        if (Math.random() <= 0.1) {
            p.setMod(Stat.SPECIAL_DEFENSE, -1);
            isMinusSpDef = true;
        }
    }

    @Override
    protected String describe() {
        if (isMinusSpDef) return "uses Energy Ball and decrease SpDamage";
        return "uses Energy Ball";
    }
}