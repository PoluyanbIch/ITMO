# Билет 5

## Число отображений из одного множества в другое. Число инъекций. Число перестановок данного множества. Размещения и размещения с повторениями.

### Теорема

> Пусть |X| = k и |Y| = n. Тогда
>
> 1. число отображений из X в Y равно nk;
> 2. число инъекций из X в Y равно n(n−1)...(n−k +1).

### Доказательство.

> Пусть $X = {x_1,...,x_k}$.
>
> 1. Для каждого элемента xi ∈ X можно n способами выбрать его образ.
> 2. Образ $x_1$ можно выбрать n способами. После этого останется n −1
>    способ выбрать образ $x_2$, ..., n − k +1 способ выбрать образ $x_k$.

### Замечание

> При n ≥k имеем n(n−1)...(n−k +1) = $\frac{n!}{(n−k)!}$

### Конечные множества: перестановки и размещения:

Перестановкой на множестве X называется произвольная биекция σ: X → X.

### Следствие

Если |X| = n, то число перестановок на множестве X равно n!.

### Определение

> Число инъекций f : [1..k] → [1..n] называется числом размещений из n
> элементов по k и обозначается $A^k_n$.
>
> Число отображений f : [1..k] → [1..n] называется числом размещений с
> повторениями из n элементов по k и обозначается $\bar{A^k_n}$

1. $A^k_n$ = n(n−1)...(n−k +1) = $\frac{n!}{(n−k)!}$
2. $\bar{A^k_n}$ = $n^k$