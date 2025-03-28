# 1. Делимость. Свойства. Теорема о делениии с остатком
## Делимость
### Определение
>  Пусть $a, b \in \Z, b \not = 0$. Тогда *a* делится на *b* (обозначение: $a \, \vdots \, b$) или, что то же самое, *b* делит *a* (обозначение: $b \, | \, a$), если $a = bc$, где $c \in \Z$.  
Если $a \, \vdots \, b$, то *b* - делитель *a*.

## Свойства

### Свойство 1
> Если $a \, \vdots \, b$ и $b \, \vdots \, c$, то $a \, \vdots \, c$
### Доказательство
> Тогда $a = kb$ и $b = nc$, где $k, n \in \Z$, откуда следует *a = knc*

### Свойство 2
> Пусть $a, b \, \vdots \, d$, а $x, y \in \Z$. Тогда $ax + by \, \vdots \, d$
### Доказательство 
> Тогда *a = kd* и *b = nd*, где $k, n \in \Z$, откуда следует $ax + by = (kx + ny)d$.

### Свойство 3
> Пусть $a, d \in \N, a \, \vdots \, d$. Тогда $a \ge d$
### Доказательство 
> Тогда *a = kd*, где $k \in \N$, откуда следует $a = kd \ge d$

## Теорема о делении с остатком 

### Теорема 
> Пусть $a \in \Z, b \in \N$. Тогда существуют единственные такие $q, r \in \Z$, что $0 \le r \lt b$ и $a = bq + r$.  
$\bullet$ Число *r* называется остатком от деления *a* на *b*.
### Доказательство
> **Существование**. Пусть *q* - такое целое число, что $bq \le a \lt b(q+1)$, а $r = a - bq$. Тогда $0 \le r \lt b$ (вычтем из всех трех частей первого неравенства *bq*)  
**Единственность**. $\bullet$ Пусть $a = bq_1 + r_1 = bq_2 + r_2$, причем $0 \le r_1 \lt b$ и $0 \le r_2 \lt b$  
$\bullet$ НУО $r_1 > r_2$. Тогда $0 < r_1 - r_2 < b$.  
$\bullet$ С другой стороны, $r_1 - r_2 = b(q_2 - q_1) \gt b$. **Противоречие**