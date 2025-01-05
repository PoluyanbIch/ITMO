# Билет 15

## Стягивание ребра в трёхсвязном графе без потери трёхсвязности.

### **Теорема 9**

_(W. T. Tutte.)_ Пусть G — трёхсвязный граф с v(G) ≥ 5. Тогда существует такое ребро e ∈ E(G), что граф G · e
трёхсвязен.

<a id="утверждение-2"></a>

**Утверждение** Для любого ребра ab ∈ E(G) существует такое множество $T_{ab}$ ∈ $R_3$(G), что $T_{ab}$ ∋ a, b.

#### **Доказательство** (от противного)

+ Пусть ab ∈ E(G). Тогда граф G · ab нетрёхсвязен, а значит, имеет двухвершинное разделяющее множество S.
+ Пусть ab ∈ E(G). Тогда граф G · ab нетрёхсвязен, а значит, имеет двухвершинное разделяющее множество S.
+ Пусть w = a · b. Предположим, что w $\notin$ S.
+ Так как ab ∈ E(G), вершины a, b лежат в одной компоненте Comp(G − S).
+ Пусть U ∈ Comp(G · ab − S) — другая компонента. Тогда в G · ab нет пути из U в w.
+ Значит, в G − S нет пути из U в {a, b} — противоречие с трёхсвязностью G.
+ Остается случай, когда w ∈ S. Пусть S = {w, x}.
+ Тогда $T_{a,b}$ = {a, b, x} нам подходит: граф G − {a, b, x} = G · ab − w несвязен.
+ Рассмотрим минимальную компоненту связности H, отделяемую в графе G множеством, содержащим две вершины одного ребра.
+ Пусть ab ∈ E(G), $T_{ab}$ = {a, b, c}, H ∈ Comp(G − $T_{ab}$).
+ Существует вершина d ∈ H, смежная с c. Рассмотрим множество $T_{cd}$ ∈ $R_3$(G).
+ Так как $T_{ab}$ \ $T_{cd}$ ⊂ {a, b}, а эти две вершины смежны, $T_{cd}$ не разделяет $T_{ab}$.
+ Тогда $T_{ab}$ и $T_{cd}$ независимы по Лемме 7.
+ По Лемме 8 существует такая компонента H' ∈ _Comp(G − $T_{cd}$ )_, что H′ ⊊ H.
+ Противоречие с минимальностью H.