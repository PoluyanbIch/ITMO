import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        new ByteArrayOutputStream

        Person person = new Person("Человек", "Нора");
        person.addGoat(new Goat("Коза1", "Загон"));
        person.addGoat(new Goat("Коза2", "Загон"));

        Savage savage1 = new Savage("Дикарь1", "Пляж", 200);
        ArrayList<Savage> savages = new ArrayList<>();
        savages.add(savage1);

        Dog dog = new Dog("Бобик", "Нора");


        try {
            if (Math.random() < 0.3) {
                throw new SavageAttackException("Дикари атакуют!");
            }

            person.interact(savage1);
            if (!person.getGoats().isEmpty()) {
                person.interact(person.getGoats().get(0));
            } else {
                System.out.println("У человека нет коз!");
            }
            person.interact(dog);

            Shot shot1 = new Shot(true, "Дикарь", 50.0);
            Shot shot2 = new Shot(false, "Олень", 100.0);

            System.out.println(shot1);
            System.out.println(shot2);

        } catch (SavageAttackException | NullPointerException | IndexOutOfBoundsException e) {
            System.err.println(e.getMessage());
        }
    }
}