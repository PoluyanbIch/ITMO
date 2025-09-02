import numpy as np
from scipy.integrate import quad

# Исходная функция
def f(x):
    return np.sin(x**3)

# Метод прямоугольников
def rectangle_method(a, b, n):
    h = (b - a) / n
    integral = 0
    for i in range(n):
        x = a + (i + 0.5) * h
        integral += f(x)
    return integral * h

# Метод трапеций
def trapezoid_method(a, b, n):
    h = (b - a) / n
    integral = (f(a) + f(b)) / 2
    for i in range(1, n):
        x = a + i * h
        integral += f(x)
    return integral * h

# Метод Симпсона
def simpson_method(a, b, n):
    if n % 2 != 0:
        n += 1  # Делаем n четным
    h = (b - a) / n
    integral = f(a) + f(b)
    for i in range(1, n):
        x = a + i * h
        if i % 2 == 0:
            integral += 2 * f(x)
        else:
            integral += 4 * f(x)
    return integral * h / 3

# Параметры интегрирования
a, b = 1, 2
epsilon = 1e-5

# Вычисление с автоматическим подбором n для достижения точности
def compute_integral(method, a, b, epsilon):
    n = 4
    prev_value = method(a, b, n)
    while True:
        n *= 2
        curr_value = method(a, b, n)
        if abs(curr_value - prev_value) < epsilon:
            break
        prev_value = curr_value
    return curr_value, n

# Вычисление интегралов
rect_value, rect_n = compute_integral(rectangle_method, a, b, epsilon)
trap_value, trap_n = compute_integral(trapezoid_method, a, b, epsilon)
simp_value, simp_n = compute_integral(simpson_method, a, b, epsilon)

# Вывод результатов
print(f"Метод прямоугольников: {rect_value:.5f}, шагов: {rect_n}")
print(f"Метод трапеций: {trap_value:.5f}, шагов: {trap_n}")
print(f"Метод Симпсона: {simp_value:.5f}, шагов: {simp_n}")