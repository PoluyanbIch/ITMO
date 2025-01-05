package attacks;

import ru.ifmo.se.pokemon.*;

public class Facade extends PhysicalMove {
    public Facade() {
        super(Type.NORMAL, 70, 100);
    }

    private boolean isDoubleDmg;
    @Override
    protected void applyOppDamage(Pokemon p, double damage) {
        Status condition = p.getCondition();
        if (condition.equals(Status.POISON) || condition.equals(Status.BURN) || condition.equals(Status.PARALYZE)) {
            isDoubleDmg = true;
            p.setMod(Stat.HP, -2 * (int)damage);
        }
        else p.setMod(Stat.HP, (int)damage);
    }

    @Override
    protected String describe() {
        if (isDoubleDmg) return "uses Facade and damage x2";
        return "uses Facade";
    }
}