# Билет 3

## Крайние блоки.

### **Определение**

1. Назовем блок B крайним, если он соответствует висячей вершине дерева блоков и точек сочленения.
2. Внутренность _Int(B)_ блока B — это множество всех его вершин, не являющихся точками сочленения в графе G.

+ Нетрудно понять, что блок недвусвязного графа G является крайним тогда и только тогда, когда он содержит ровно одну
  точку сочленения.
+ Внутренность некрайнего блока может быть пустой. Внутренность крайнего блока всегда непуста.
+ Если у связного графа G есть точки сочленения, то он имеет хотя бы два крайних блока.
+ Если B — блок графа G, а x ∈ _Int(B)_, то граф G − x связен.

### **Лемма 3**

Пусть B — крайний блок связного графа G, а G′ = G − _Int(B)_. Тогда граф G′ связен, а блоки G′ — это все блоки G, кроме
B.

#### **Доказательство**

+ Пусть a ∈ V(B) — точка сочленения, отрезающая крайний блок B от остального графа. Тогда _Int(B)_ — это одна из
  компонент связности графа G − a, откуда очевидно следует связность графа G′.
+ Все отличные от B блоки графа G являются подграфами G′, не имеют точек сочленения и являются максимальными подграфами
  G′ с таким свойством (они были максимальными даже в G). Следовательно, все они — блоки графа G′.
+ Пусть B′ — блок графа G′. Очевидно, v(G′) ≥ 2, поэтому B′содержит хотя бы одно ребро e, которое в графе G лежит в
  некотором блоке B∗ &ne; B. Теперь очевидно, что B∗ = B ′.