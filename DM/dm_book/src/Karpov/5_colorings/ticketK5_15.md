# Билет 15. Списочные раскраски вершин и k-редуцируемые графы.

## Списочные раскраски 

### Определение
- Каждой вершине графа $v \in V(G)$ ставится в
  соответствие список L(v), после чего рассматривается
  правильная раскраска вершин с дополнительным
  ограничением: каждая вершина v должна быть
  покрашена в цвет из списка L(v).
- Минимальное такое $k \in N$, что для любых списков из k
  цветов существует правильная раскраска вершин графа
  G, обозначается через ch(G) (и носит название списочное
  хроматическое число, по-английски — choice number).
- Во всех случаях, когда речь идет о списках цветов, мы
  будем обозначать список большой буквой (как
  правило, L), а его размер — соответствующей строчной:
  так, ℓ(v) = |L(v)|.
- Очевидно, $ch(G) \ge \chi(G)$. Существуют графы, для
  которых ch(G) > $\chi(G)$.
- Аналогично списочным раскраскам вершин можно
  определить списочные раскраски рёбер и списочный
  хроматический индекс ch′(G).
- Очевидно, $ch^′(G) \ge \chi^′(G)$. В отличие от ситуации со
  списочными раскрасками вершин, не известно ни одного
  графа, для которого $ch^′(G) > \chi^′(G)$.
- Более того, выдвинута гипотеза (List Color Conjecture) о
  том, что $ch^′(G) = \chi^′(G)$ для любого графа G. В 1995
  году Гэльвин доказал эту гипотезу для двудольных
  графов.

## K-редуцируемые графы

### Определение

Пусть $k \in N$. Граф G называется k-редуцируемым, если его
вершины можно занумеровать $v_1,\dotsc, v_n$ так, что каждая
вершина смежна менее чем с k вершинами с большим
номером.

### Лемма 7

Пусть G — k-редуцируемый граф. Тогда $\chi(G) \ge ch(G) \ge k$.

>#### Доказательство {collapsible="true"}
> - Пусть $v_1,\dotsc, v_n$ — нумерация вершин графа из определения,
    причем каждой вершине $v_i$ соответствует список $L(v_i)$ длины
    $ℓ(v_i) \ge k$.
> - Покрасим вершины в порядке, обратном нумерации.
> - При покраске вершины $v_i$ количество запретов на цвет не
    превосходит количество ее соседей среди вершин с большим
    номером, а таких не более k − 1.
> - Значит, мы можем покрасить вершину $v_i$ в цвет из ее
    списка.
> - Докажем критерий k-редуцируемости графа.


### Лемма 8

Граф G является k-редуцируемым, если и только если для
любого его подграфа H выполняется $\delta(H) \le k − 1$.

>#### Доказательство {collapsible="true"}
> $\Rightarrow$  
> - Пронумеруем вершины графа G как в определении.
> - Предположим противное, пусть подграф H таков, что $\delta(H) \ge k$
> - Пусть $v_i \in V(H)$ — вершина с наименьшим номером.
> - Тогда $v_i$ смежна не менее чем с $d_H(v_i) \ge \delta(H) \ge k$
    вершинами с большим номером, противоречие.  
> $\Leftarrow$  
> - Пусть $v_1$ — вершина графа G наименьшей степени. Тогда
    она смежна не более чем с $d_G(v_1) = \delta(G) \le k − 1$ вершинами.
> - Предположим, что вершины $v_1,\dotsc, v_{i−1}$ уже построены.
> - Положим $G_i$ = G − {$v_1, \dotsc , v_{i−1}$}. Тогда граф $G_i$ имеет
    вершину степени не более $\delta(G_i) \le k − 1$ — именно она и будет
    вершиной $v_i$.


