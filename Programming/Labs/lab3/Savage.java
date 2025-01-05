class Savage implements IslandInhabitant {
    private String name;
    private String location;
    private int numberOfBoats;

    public Savage(String name, String location, int numberOfBoats) {
        this.name = name;
        this.location = location;
        this.numberOfBoats = numberOfBoats;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getLocation() {
        return location;
    }


    @Override
    public void interact(IslandInhabitant other) {
        if (other instanceof Person) {
            System.out.println("Дикари атакуют человека!");
        } else {
            System.out.println("Дикари не реагируют на это.");
        }
    }

    @Override
    public String toString() {
        return "Savage{" +
                "name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", numberOfBoats=" + numberOfBoats +
                '}';
    }
}


class SavageAttackException extends Exception {
    public SavageAttackException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}