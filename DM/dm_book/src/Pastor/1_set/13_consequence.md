# Билет 13

## Следствия об объединении и разности бесконечного множества и счётного множества. Примеры множеств мощности континуума.

### Следствие 1
 Если X бесконечно и Y не более чем счётно, то X ∪Y ∼ X.
 
> ###  Доказательство. Пусть B ⊂ X — счётное множество;
> • A=X \B иC =Y \X. \
> • Тогда X = A ∪ B и X ∪Y =A ∪ B ∪ C; \
> • при этом A ∩ B =A ∩ C =∅, \
> • множество C не более чем счётно и B ∪ C счётно. \
> • Рассмотрим биекцию f : B → B ∪C. \
> • Тогда отображение g: X → X ∪Y, задаваемое формулой \
> $g(x) = \begin{cases} f(x), x ∈ B \\ x, x ∈ A \end{cases}$ - Биекция 

### Следствие 2
Если X несчётно и Y не более чем счётно, то X \ Y ∼ X.
>### Доказательство. 
> Заметим, что X = (X \ Y) ∪ (X ∩ Y); \
> • множество X \ Y бесконечно и X ∩ Y — не более чем счётно.


### Примеры множеств мощности континуума
> ### Утверждение
> Пусть a,b ∈ R и a < b. Тогда множества \
> [a, b], [a, b), (a,b] и (a,b) имеют мощность континуума.

> ### Доказательство.
> • Функция f(x) = (b −a)x +a задает биекцию f : [0,1] → [a,b]. \
> • Множества [a,b), (a,b] и (a,b) равномощны [a,b], поскольку получаются удалением конечного числа элементов.

### Утверждение
 Множество R имеет мощность континуума.
>### Доказательство.
 Функция tg(x) задает биекцию из $(\frac{−\Pi}{2}, \frac{\Pi}{2})$на R