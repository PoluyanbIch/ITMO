package pokemons;

import attacks.*;
import ru.ifmo.se.pokemon.*;

public class Mawile extends Pokemon {
    public Mawile(String name, int level) {
        super(name, level);
        setStats(50, 85, 85, 55, 55, 50);
        setType(Type.STEEL, Type.FAIRY);
        setMove(new ShadowBall(), new Rest(), new FireBlast(), new Facade());
    }
}