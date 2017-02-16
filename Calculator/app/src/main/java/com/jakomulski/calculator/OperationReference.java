package com.jakomulski.calculator;

import java.math.BigDecimal;

public interface OperationReference {
    BigDecimal compute(BigDecimal a, BigDecimal b);
}
