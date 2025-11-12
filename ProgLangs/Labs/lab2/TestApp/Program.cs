using System;



abstract class Character
{
    public string Name { get; }
    public int Health { get; set; }
    public int AttackPower { get; }
    public Character(string name, int health, int attackPower)
    {
        Name = name;
        Health = health;
        AttackPower = attackPower;
    }

    public abstract void Attack(Character target);

    public void TakeDamage(int damage)
    {
        Health -= damage;
        if (Health < 0) Health = 0;
        Console.WriteLine($"{Name} получил {damage} урона. Осталось здоровья: {Health}");
    }

    public bool IsAlive => Health > 0;
}

class Mage : Character
{
    public int Mana { get; set; }

    public Mage(string name) : base(name, 60, 15)
    {
        Mana = 50;
    }

    public override void Attack(Character target)
    {
        if (Mana >= 10)
        {
            Console.WriteLine($"{Name} колдует заклинание против {target.Name}!");
            target.TakeDamage(AttackPower + 10);
            Mana -= 10;
        }
        else
        {
            Console.WriteLine($"{Name} атакует посохом {target.Name}!");
            target.TakeDamage(AttackPower);
        }
    }
}

class Knight : Character
{
    public Knight(string name) : base(name, 100, 12) { }

    public override void Attack(Character target)
    {
        Console.WriteLine($"{Name} наносит удар мечом по {target.Name}!");
        target.TakeDamage(AttackPower);
    }
}

class Archer : Character
{
    public Archer(string name) : base(name, 80, 10) { }

    public override void Attack(Character target)
    {
        Console.WriteLine($"{Name} стреляет из лука в {target.Name}!");
        target.TakeDamage(AttackPower + 5);
    }
}

class Program
{
    static void Main(string[] args)
    {
        Character mage = new Mage("Гендальф");
        Character knight = new Knight("Артур");
        Character archer = new Archer("Робин");

        Character[] fighters = { mage, knight, archer };
        int round = 1;

        while (fighters[0].IsAlive && fighters[1].IsAlive && fighters[2].IsAlive)
        {
            Console.WriteLine($"\n--- Раунд {round} ---");
            mage.Attack(knight);
            if (!knight.IsAlive) break;

            knight.Attack(archer);
            if (!archer.IsAlive) break;

            archer.Attack(mage);
            if (!mage.IsAlive) break;

            round++;
        }

        Console.WriteLine("\nБой окончен!");
        for (int i = 0; i < fighters.Length; i++)
        {
            Console.WriteLine($"{fighters[i].Name}: {(fighters[i].IsAlive ? "жив" : "побежден")}");
        }
    }
}