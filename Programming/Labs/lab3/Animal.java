import java.util.Objects;


abstract class Animal implements IslandInhabitant {
    private String name;
    private String location;
    private AnimalType type;

    public Animal(String name, String location, AnimalType type) {
        this.name = name;
        this.location = location;
        this.type = type;
    }

    @Override
    public String getName() { return name; }
    @Override
    public String getLocation() { return location; }
    @Override
    public abstract void interact(IslandInhabitant other);

    public AnimalType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        return Objects.equals(name, animal.name) && Objects.equals(location, animal.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, location);
    }

    @Override
    public String toString() {
        return "Animal{" +
                "name='" + name + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}