package attacks;

import ru.ifmo.se.pokemon.*;

public class Psychic extends SpecialMove {
    public Psychic() {
        super(Type.PSYCHIC, 90, 100);
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
        if (isMinusSpDef) return "uses Psychic and decrease SpDamage";
        return "uses Psychic";
    }
}