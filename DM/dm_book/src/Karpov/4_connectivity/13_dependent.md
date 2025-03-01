# Билет 13

## Зависимые и независимые разделяющие множества.

### **Определение**

+ Пусть G — k-связный граф. Через $R_k$(G) обозначим множество всех k-вершинных разделяющих множеств G. Назовем
  различные множества S,T ∈ $R_k$ (G) независимыми, если S не разделяет T и T не разделяет S. В противном случае мы
  будем называть эти множества зависимыми.
+ Через _**Comp(H)**_ обозначим множество всех компонент связности графа H

### **Лемма 6**

Пусть G — k-связный граф, S,T ∈ $R_k$(G) и компонента A ∈ _Comp(G − S)_ таковы, что T ∩ A = ∅. Тогда T не разделяет A ∪
S.

#### **Доказательство**

+ Граф G(A) связен.
+ [По Лемме 5](#лемма-5) любая вершина x ∈ S \ T смежна хотя бы с одной из вершин A.
+ Следовательно, граф G(A ∪ (S \ T)) связен, откуда следует,

### **Лемма 7**

Пусть G — k-связный граф, S,T ∈ $R_k$(G) таковы, что множество S не разделяет множество T. Тогда множество T не
разделяет множество S (то есть, эти множества независимы).

#### **Доказательство**

+ Так как S не разделяет T, множество T может пересекать внутренность не более, чем одной из компонент _Comp(G − S)_.
+ Тогда существует такая компонента A ∈ _Comp(G − S)_, что A ∩ T = ∅.
+ [По Лемме 6](#лемма-6) T не разделяет S.