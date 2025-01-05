import java.util.ArrayList;
import java.util.Objects;

class Person implements IslandInhabitant {
    private String name;
    private String location;
    private ArrayList<Goat> goats;

    public Person(String name, String location) {
        this.name = name;
        this.location = location;
        goats = new ArrayList<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getLocation() {
        return location;
    }

    public ArrayList<Goat> getGoats() {
        return goats;
    }

    @Override
    public void interact(IslandInhabitant other) {
        if (other instanceof Savage) {
            System.out.println("Человек убегает от дикарей!");
        } else if (other instanceof Goat) {
            System.out.println("Человек заботится о козах");
        } else {
            System.out.println("Человек не реагирует.");
        }
    }


    public void addGoat(Goat goat) {
        goats.add(goat);
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", location='" + location + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(name, person.name) && Objects.equals(location, person.location) && Objects.equals(goats, person.goats);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, location, goats);
    }
}