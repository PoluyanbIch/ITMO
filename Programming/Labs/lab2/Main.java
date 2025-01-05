import ru.ifmo.se.pokemon.*;
import pokemons.*;


public class Main {
    public static void main(String[] args) {
        Battle b = new Battle();
        Mawile p1 = new Mawile("Mawile", 1);
        Eevee p2 = new Eevee("Eevee", 1);
        Leafeon p3 = new Leafeon("Leafon", 1);
        Flabebe p4 = new Flabebe("Flabebe", 1);
        Floette p5 = new Floette("Floette", 1);
        Florges p6 = new Florges("Florges", 1);
        
        b.addAlly(p1);
        b.addAlly(p2);
        b.addAlly(p3);
        b.addFoe(p4);
        b.addFoe(p5);
        b.addFoe(p6);

        b.go();
    }
}