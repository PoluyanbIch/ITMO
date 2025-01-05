package pokemons;

import attacks.*;
import ru.ifmo.se.pokemon.*;

public class Floette extends Pokemon {
    public Floette(String name, int level) {
        super(name, level);
        setStats(54, 45, 47, 75, 98, 52);
        setType(Type.FAIRY);
        setMove(new EnergyBall(), new Confide(), new Tackle());
    }
}