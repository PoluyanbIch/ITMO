package com.github.poluyanbIch.weblab3;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class AreaChecker {
    public static boolean hit(BigDecimal x, BigDecimal y, BigDecimal r) {
        BigDecimal zero = BigDecimal.ZERO;
        boolean isHit = false;

        if (x.compareTo(zero) >= 0 && y.compareTo(zero) >= 0) {
            if (x.compareTo(r) <= 0 && y.compareTo(r) <= 0) {
                isHit = true;
            }
        }

        if (x.compareTo(zero) <= 0 && y.compareTo(zero) <= 0) {
            BigDecimal border = x.negate().subtract(r);
            if (y.compareTo(border) >= 0) {
                isHit = true;
            }

        }

        if (x.compareTo(zero) >= 0 && y.compareTo(zero) <= 0) {
            BigDecimal radius = r;
            BigDecimal sum = x.multiply(x).add(y.multiply(y));
            if (sum.compareTo(radius.multiply(radius)) <= 0) {
                isHit = true;
            }
        }

        return isHit;
    }

}

