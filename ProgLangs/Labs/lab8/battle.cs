using System;
using System.Reflection;
using System.Linq;
using System.Collections.Generic;

namespace Homm.Core;

// Контекст исполнения: данные, доступные внутри навыка
public class BattleContext
{
    public int DamageDealt { get; set; }
    public UnitStats Attacker { get; set; }
    public UnitStats Defender { get; set; }
}

public class UnitStats { public int Hp { get; set; } }

// Этап выполнения (Execution Stage)
public enum TriggerType { OnAttack, OnDefense, PostBattle }

/// <summary>
/// Атрибут-маркер, превращающий обычный метод в исполняемую единицу бизнес-логики.
/// </summary>
[AttributeUsage(AttributeTargets.Method, Inherited = false, AllowMultiple = false)]
public class CombatSkillAttribute : Attribute
{
    /// <summary>Уникальный идентификатор навыка в системе.</summary>
    public string Name { get; }

    /// <summary>Точка внедрения в пайплайн обработки.</summary>
    public TriggerType Trigger { get; }

    /// <summary>Приоритет выполнения (выше значение -> раньше выполнение).</summary>
    public int Priority { get; }

    public CombatSkillAttribute(string name, TriggerType trigger, int priority = 1)
    {
        Name = name;
        Trigger = trigger;
        Priority = priority;
    }
}

// Этот атрибут нужен для упрощения поиска классов, реализующих логику игры
[AttributeUsage(AttributeTargets.Class, Inherited = false, AllowMultiple = false)]
public class GameAttribute : Attribute { }