package com.jakomulski.calculator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    public final static String EXTRA_HISTORY = "EXTRA_HISTORY";

    private Operation operation = Operation.NOTHING;
    private TextView currentNumberView = null;
    private TextView operationsView = null;
    private BigDecimal currentNumber = null;
    private BigDecimal result = BigDecimal.ZERO;
    private StringBuilder operationsHistory = new StringBuilder();
    private ArrayList<String> history = new ArrayList<>();
    private boolean isFraction = false;

    private final Map<Integer, Runnable> buttonActions = new HashMap<>();
    {
        buttonActions.put(R.id.button_add, () -> setCurrentOperation(Operation.ADD));
        buttonActions.put(R.id.button_divison, () -> setCurrentOperation(Operation.DIVIDE));
        buttonActions.put(R.id.button_multiply, () -> setCurrentOperation(Operation.MULTIPLY));
        buttonActions.put(R.id.button_subtract, () -> setCurrentOperation(Operation.SUBTRACT));
        buttonActions.put(R.id.button_ce, this::clearCurrentNumber);
        buttonActions.put(R.id.button_c, this::clearAll);
        buttonActions.put(R.id.button_history, this::openHistory);
        buttonActions.put(R.id.button_solve, this::maintainOperation);
        buttonActions.put(R.id.button_zero, () -> setCurrentNumber(BigDecimal.ZERO));
        buttonActions.put(R.id.button_one, () -> setCurrentNumber(BigDecimal.ONE));
        buttonActions.put(R.id.button_two, () -> setCurrentNumber(BigDecimal.valueOf(2)));
        buttonActions.put(R.id.button_three, () -> setCurrentNumber(BigDecimal.valueOf(3)));
        buttonActions.put(R.id.button_four, () -> setCurrentNumber(BigDecimal.valueOf(4)));
        buttonActions.put(R.id.button_five, () -> setCurrentNumber(BigDecimal.valueOf(5)));
        buttonActions.put(R.id.button_six, () -> setCurrentNumber(BigDecimal.valueOf(6)));
        buttonActions.put(R.id.button_seven, () -> setCurrentNumber(BigDecimal.valueOf(7)));
        buttonActions.put(R.id.button_eight, () -> setCurrentNumber(BigDecimal.valueOf(8)));
        buttonActions.put(R.id.button_nine, () -> setCurrentNumber(BigDecimal.valueOf(9)));
        buttonActions.put(R.id.button_coma, () -> {isFraction = true;});
    }

    private void openHistory() {
        clearAll();

        Intent intent = new Intent(this, HistoryActivity.class);
        intent.putStringArrayListExtra(EXTRA_HISTORY, history);
        startActivity(intent);
    }

    private void clearAll() {
        if(operationsHistory.length()>0) {
            history.add(operationsHistory.toString());
        }
        this.operationsHistory.setLength(0);
        this.result = BigDecimal.ZERO;
        this.operation = Operation.NOTHING;
        clearCurrentNumber();
        refreshOperationsHistoryView();
    }

    private void clearCurrentNumber() {
        currentNumber = null;
        currentNumberView.setText(R.string.null_value);
    }

    private void setCurrentNumber(BigDecimal currentNumber) {
        if (this.currentNumber == null) {
            this.currentNumber = currentNumber;
        }
        else if(isFraction) {
            String[] integerDecimalSplit = this.currentNumber.toString().split("\\.");
            int decimalLength = 0;
            if(integerDecimalSplit.length > 1) {
                decimalLength += integerDecimalSplit[1].length();
            }
            for (int i = 0; i <= decimalLength; ++i) {
                currentNumber = currentNumber.divide(BigDecimal.TEN);
            }
            this.currentNumber = this.currentNumber.add(currentNumber);
        }
        else {
            this.currentNumber = this.currentNumber.multiply(BigDecimal.TEN);
            this.currentNumber = this.currentNumber.add(currentNumber);
        }
        currentNumberView.setText(this.currentNumber.toString());
    }

    private void setCurrentOperation(Operation operation) {
        maintainOperation();
        this.operation = operation;
        refreshOperationsHistoryView();
    }

    private void maintainOperation() {
        if (currentNumber == null) {
            return;
        }
        appendOperationsHistory();
        if (result.equals(BigDecimal.ZERO)) {
            result = currentNumber;
        } else {
            result = operation.compute(result, currentNumber);
        }
        this.currentNumberView.setText(result.toString());
        this.currentNumber = null;
        isFraction = false;
    }

    private void appendOperationsHistory() {
        if (! result.equals(BigDecimal.ZERO)) {
            operationsHistory.append(operation.toString());
        }
        operationsHistory.append(currentNumber.toString());
        refreshOperationsHistoryView();
    }

    private void refreshOperationsHistoryView() {
        operationsView.setText(operationsHistory.toString() + operation.toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        currentNumberView = (TextView) findViewById(R.id.currentNumber);
        operationsView = (TextView) findViewById(R.id.operations);

        NumberPicker scalePicker = (NumberPicker) findViewById(R.id.scaleNumberPicker);
        scalePicker.setMinValue(0);
        scalePicker.setMaxValue(100);
        scalePicker.setWrapSelectorWheel(false);
        scalePicker.setValue(Operation.getScale());
        scalePicker.setOnValueChangedListener((p,o,n)->Operation.setScale(n));

        for (int key : buttonActions.keySet()) {
            findViewById(key).setOnClickListener((e) -> buttonActions.get(key).run());
        }
    }
}
