package attacks;

import ru.ifmo.se.pokemon.*;

public class FireBlast extends SpecialMove {
    public FireBlast() {
        super(Type.FIRE, 110, 85);
    }

    private boolean isBurned;
    @Override
    protected void applyOppEffects(Pokemon p) {
        if (Math.random() <= 0.1) {
            isBurned = true;
            Effect.burn(p);
        }
    }

    @Override
    protected String describe() {
        if (isBurned) return "uses Fire Blast and burn";
        return "uses Fire Blast";
    }
}