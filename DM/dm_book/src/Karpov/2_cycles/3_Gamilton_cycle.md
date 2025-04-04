# Гамильтонов путь и цикл

## Определение  
1) **Гамильтонов путь** в графе *G* - это простой путь, проходящий по каждой вершине графа.  
2) **Гамильтонов цикл** в графе *G* - это простой цикл, проходящий по каждой вершине графа.  
3) Граф называется **гамильтоновым**, если в нем есть гамильтонов цикл.  

## Критерий Оре  
1) Если для любых двух несмежных вершин $u, v \in V(G)$ выполняется $d_G(u) + d_G(v) \ge v(G) - 1$, то в графе G есть гамильтонов путь.  
2) Если $v(G) > 2$ и для любых двух несмежных вершин $u, v \in V(G)$ выполняется $d_G(u) + d_G(v) \ge v(G)$, то в графе *G* есть гамильтонов цикл

### Доказательство  
1) * Случай, когда в графе *G* ровно две вершины, очевиден. Далее пусть $v(G) > 2$.  
* Граф *G* связен. (Пусть *a* и *b* - две несмежные вершины графа, тогда $d_G(a) + d_g(b) \ge v(G) - 1$, откуда следует, что $N_G(a) \cap N_G(b) \ne \varnothing$, то есть, вершины *a* и *b* связаны)
* Пусть $a_1 ... a_n$ - наибольший простой путь графа *G*. Поскольку граф *G* связен и $v(G) > 2$, то $n \ge 3$.  
Предположим, что $n \le v(G) - 1$
* В графе есть цикл на *n* вершинах: при $a_1 a_n \in E(G)$ это очевидно, а при $a_1 a_n \notin E(G)$ мы имеем $d_G(a_1) + d_G(a_n) \ge v(G) - 1$ и цикл существует по лемме (Пусть $n > 2, a_1 ... a_n$ - максимальный путь в графе *G*, причем $d_G(a_1) + d_G(a_n) \ge n$. Тогда в графе есть цикл длины *n*.) в графе *G* есть цикл *Z* из *n* вершин.
* Так как граф связен, существует не вошедшая в этот цикл вершина, смежная хотя бы с одной из вершин цикла. Тогда и путь на $n + 1$ вершине, противоречие. Таким образом, в графе есть ГП.  
2) * Гамильтонов путь в графе уже есть по пункту 1. Пусть $n = v(G)$ и это путь $a_1 ... a_n$.
* Если вершины $a_1$ и $a_n$ смежны, то $a_1 ... a_n$ - ГЦ. Если $a_1 a_n \notin E(G)$, то $d_G(a_1) + d_G(a_n) \ge v(G) = n$ и ГЦ есть по лемме

## Критерий Дирака (следствие критерия Оре)  
1) Если $\delta(G) \ge \frac {v(G) - 1}{2}$, то в графе *G* есть ГП.  
2) Если $\delta(G) \ge \frac {v(G)}{2}$, то в графе *G* есть ГЦ.

