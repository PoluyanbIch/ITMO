subroutine BISECT(a, b, eps, f, x0, k)
    interface
        function f(x)
            real(16) :: f
            real(16), intent(in) :: x
        end function f
    end interface

    real(16) :: x0, eps, a, b, an, bn, r, y
    integer :: k

    k = 0
    an = a
    bn = b
    r = f(a)

    do
        x0 = 0.5_16 * (an + bn)
        y = f(x0)

        if ((y == 0.0_16) .or. ((bn - an) <= (2.0_16 * eps))) exit

        k = k + 1
        if (sign(1.0_16, r) * sign(1.0_16, y) < 0.0_16) then
            bn = x0
        else
            an = x0
            r = y
        end if
    end do

end subroutine BISECT

program main
    interface
        function f(x)
            real(16) :: f
            real(16), intent(in) :: x
        end function f
    end interface

    real(16) :: a, b, eps, x
    integer :: k

    print *, "Введите начальную левую границу интервала (a):"
    read *, a
    print *, "Введите начальную правую границу интервала (b):"
    read *, b
    print *, "Введите точность вычислений (eps):"
    read *, eps

    call BISECT(a, b, eps, f, x, k)

    print *, "Корень уравнения найден: "
    print *, "x =", x
    print *, "Число итераций:", k

end program main

function f(x)
    implicit none
    real(16) :: f
    real(16), intent(in) :: x

    f = x**3 - 3.0_16 * x - 2.0_16 * exp(-x)
    return
end function f