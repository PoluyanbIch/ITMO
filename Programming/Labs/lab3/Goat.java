import java.util.Objects;

class Goat extends Animal {
    private String name;
    private String location;

    public Goat(String name, String location) {
        super(name, location, AnimalType.GOAT);
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public void interact(IslandInhabitant other) {
        System.out.println("Коза пасется.");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Goat goat = (Goat) o;
        return Objects.equals(name, goat.name) && Objects.equals(location, goat.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, location);
    }

    @Override
    public String toString() {
        return "Goat{" +
                "name='" + name + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}