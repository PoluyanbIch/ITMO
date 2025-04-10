# Билет 4

## Число элементов декартова произведения двух и нескольких множеств. Количество подмножеств данного множества.

> • Пусть X — конечное множество. Количество его элементов будем
> обозначать через |X|.

### Лемма (принцип произведения)

> Если |X| = m и |Y| = n, то |X ×Y| =mn.

> ### Доказательство.
>
> Каждый из m элементов множества X
> входит ровно в n пар с элементами множества Y.

> ### Следствие
>
> Если $|X_i|$ = $m_i$, где i $\in$ [1..k], то $|X_1 \times ... \times X_k|$ = $m_1 ·...· m_k$.

> ### Доказательство.
>
> Доказательство. Индукция по k. 
> База (k = 2): см. предыдущую лемму. 
> Переход (k → k +1): 
> $|X_1 ×... ×X_k ×X_{k+1}|$ = $|(X_1 ×...×X_k)×X_{k+1}|$ = $|X_1 ×...×X_k|·|X_{k+1}|$ = $(m_1 ·...·m_k)·m_{k+1} = m_1 ·...·m_k ·m_{k+1}.$

### Количество подмножеств данного множества:

#### Теорема: Если |X| = m, то |P(X)| = $2^m$.

> ### Доказательство.
>
> Доказательство. Пусть X = ${x_1,...,x_m}.$ 
> • Для каждого из i ∈ [1..m] есть два варианта: $x_i$ можно либо включить в подмножество, либо не включать. 
> • Итого, есть $2^m$ способов выбрать подмножество.

> ### Замечание
>
> • Фактически, мы построили следующую биекцию между множествами P(X) и $\{0,1\}^m$