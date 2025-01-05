package pokemons;

import attacks.*;
import ru.ifmo.se.pokemon.*;

public class Leafeon extends Pokemon {
    public Leafeon(String name, int level) {
        super(name, level);
        setStats(65, 110, 130, 60, 65, 95);
        setType(Type.GRASS);
        setMove(new SandAttack(), new Confide(), new BabyDollEyes(), new XScissor());
    }
}