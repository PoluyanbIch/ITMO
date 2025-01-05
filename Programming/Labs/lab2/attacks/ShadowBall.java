package attacks;

import ru.ifmo.se.pokemon.*;

public class ShadowBall extends SpecialMove{
    public ShadowBall() {
        super(Type.GHOST, 80, 100);
    }

    private boolean isMinusSpDef;
    @Override
    protected void applyOppEffects(Pokemon p) {
        if (Math.random() <= 0.2) {
            isMinusSpDef = true;
            p.setMod(Stat.SPECIAL_DEFENSE, -1);
        }
    }

    @Override
    protected String describe() {
        if (isMinusSpDef) return "uses Shadow Ball and decrease SpDef";
        return "uses Shadow Ball";
    }
}