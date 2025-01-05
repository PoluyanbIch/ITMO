package pokemons;

import attacks.*;
import ru.ifmo.se.pokemon.*;

public class Florges extends Pokemon {
    public Florges(String name, int level) {
        super(name, level);
        setStats(78, 65, 68, 112, 154, 75);
        setType(Type.FAIRY);
        setMove(new EnergyBall(), new Confide(), new Tackle(), new Psychic());
    }
}