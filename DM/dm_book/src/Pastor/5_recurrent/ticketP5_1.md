# Билет 40. Числа Каталана. Определение и простейшие интерпретации (скобочные последовательности, последовательности единиц и минус единиц, пути на клетчатой сетке). 

## Числа Каталана

### Определение {id = "O1"}

Числа Каталана — последовательность, задаваемая соотношениями

$c_0 = 1$ и $c_{n+1} = c_0c_n + c_1c_{n−1} + \dotsc + c_nc_0$ при $n \ge 0 $

## Правильные скобочные последовательности {id = "Title1"}

### Определение {id = "O2"}

Последовательность открывающих и закрывающих скобок называется правильной скобочной последовательностью, если она удовлетворяет следующим двум условиям:

- количества открывающих и закрывающих скобок равны
- для любого k среди первых k скобок открывающих не меньше,
  чем закрывающих

### Теорема 1 {id = "T1"}

Количество правильных скобочных последовательностей из 2n скобок
равно $c_n$.

>##### Доказательство {collapsible = "true"}
>Обозначим через $s_n$ количество правльных скобочных последовательностей из 2n скобок.
>- Нужно доказать, что $s_n = c_n$ при всех $n \ge 0$.
>- $s_0 = c_0 = 1$, поскольку есть ровно одна правильная скобочная последовательность длины 0 (пустая).
>- Докажем, что последовательность s_n задается тем же рекуррентным соотношением, что и $c_n$
>- Тогда индукцией по n получим, что $s_n = c_n$ при всех $n \ge 0$.
>- Рассмотрим правльную скобочную последовательность длины 2n + 2.
>  - Отметим в ней первую и m-ю скобки, где m = min {k | среди первых k поровну "(" и ")}.
>  - Очевидно, что, m = 2l + 2, где $l \in [0..n]$
>  - между первой и m-й скобками находится правильная скобочная последовательность длины 2l
>  - а после m-й скобки - правильная скобочная последовательность длины 2(n-l).
>- Обратно, любой упорядоченной паре правильных скобочных последовательностей длин 2l и 2(n-l) соответсвует правильная скобочная последовательность длины 2n + 2
>- Итого получаем $s_ls_{n-l}$ правильных скобочных последовательностей при данном l.
>- Просуммировав по всем l получим $s_{n+1} = s_0s_n + s_1s_{n-1} + \dotsc + s_ns_0$

#### Следствие 1 {id="S1"}

Количество последовательностей длины 2n, в которых n членов равны 1,
n членов равны −1 и все частичные суммы неотрицательны, равно $c_n$.

>##### Доказательство {collapsible="true"}
>- Открывающей скобке соответствует 1
>- Закрывающей скобке соответствует -1


#### Следствие 2 {id="S2"}

Количество путей из точки (0, 0) в точку (n, n) по линиям клетчатой сетки,
идущих вверх и вправо, и не опускающихся ниже прямой y = x, равно $c_n$.

> ##### Доказательство {collapsible="true"}
> Очев

