package com.jakomulski.calculator;

import java.math.BigDecimal;

import static com.jakomulski.calculator.Constants.SCALE;

class Constants{
    public static int SCALE = 10;
}

public enum Operation {
    NOTHING("", (f,s)->f),
    ADD("+", (f,s)->f.add(s)),
    SUBTRACT("-", (f,s)->f.subtract(s)),
    MULTIPLY("*", (f,s)->f.multiply(s)),
    DIVIDE("/", (f,s)->f.divide(s, SCALE, BigDecimal.ROUND_HALF_UP));

    private String sign;
    private OperationReference operationReference;

    public static int getScale(){
        return Constants.SCALE;
    }

    public static void setScale(int scale){
        Constants.SCALE = scale;
    }

    Operation(String sign, OperationReference operationReference) {
        this.operationReference = operationReference;
        this.sign = sign;
    }

    public BigDecimal compute(BigDecimal a, BigDecimal b) {
        return operationReference.compute(a,b);
    }

    @Override
    public String toString() {
        return sign;
    }
}
