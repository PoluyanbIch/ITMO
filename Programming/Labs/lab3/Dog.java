import java.util.Objects;

class Dog extends Animal {
    public Dog(String name, String location) {
        super(name, location, AnimalType.DOG);
    }

    @Override
    public void interact(IslandInhabitant other) {
        if (other instanceof Person) {
            System.out.println("Собака ластится к человеку.");
        } else {
            System.out.println("Собака лает.");
        }
    }

      @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode());
    }
}