public static void Main()
{
    var engine = new SkillEngine();

    // Регистрация текущей сборки (или загрузка внешней DLL)
    engine.RegisterAssembly(Assembly.GetExecutingAssembly());

    // Симуляция контекста боя
    var context = new BattleContext
    {
        DamageDealt = 100,
        Attacker = new UnitStats { Hp = 50 },
        Defender = new UnitStats { Hp = 100 }
    };

    Console.WriteLine("--- Starting Attack Phase ---");

    // Ядро само находит нужные методы и вызывает их в правильном порядке
    // (сначала CriticalStrike, потом Vampirism)
    engine.ExecutePipeline(TriggerType.OnAttack, context);

    Console.WriteLine($"Attacker Final HP: {context.Attacker.Hp}"); // 50 + 50 = 100
}