package attacks;

import ru.ifmo.se.pokemon.*;

public class BabyDollEyes extends StatusMove{
    public BabyDollEyes() {
        super(Type.FAIRY, 0, 100);
        priority++;
    }

    @Override
    protected void applyOppEffects(Pokemon p) {
        p.setMod(Stat.ATTACK, -1);
    }

    @Override
    protected String describe() {
        return "uses Baby-Doll Eyes";
    }
}