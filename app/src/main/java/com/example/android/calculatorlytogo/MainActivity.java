package com.example.android.calculatorlytogo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private String expression = "";
    private double leftPart = 0;
    private String leftPartStr = "";
    private boolean leftPoint = false;
    private boolean rightPoint = false;
    private Operation currentOperation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.num0).setOnClickListener(this);
        findViewById(R.id.num1).setOnClickListener(this);
        findViewById(R.id.num2).setOnClickListener(this);
        findViewById(R.id.num3).setOnClickListener(this);
        findViewById(R.id.num4).setOnClickListener(this);
        findViewById(R.id.num5).setOnClickListener(this);
        findViewById(R.id.num6).setOnClickListener(this);
        findViewById(R.id.num7).setOnClickListener(this);
        findViewById(R.id.num8).setOnClickListener(this);
        findViewById(R.id.num9).setOnClickListener(this);
//        findViewById(R.id.AC).setOnClickListener(this);
//        findViewById(R.id.arrow).setOnClickListener(this);
        findViewById(R.id.percent).setOnClickListener(this);
        findViewById(R.id.minus).setOnClickListener(this);
        findViewById(R.id.plus).setOnClickListener(this);
        findViewById(R.id.mul).setOnClickListener(this);
        findViewById(R.id.div).setOnClickListener(this);
        findViewById(R.id.dot).setOnClickListener(this);
        findViewById(R.id.equ).setOnClickListener(this);

    }

    public void clean(View view) {
        expression = "";
        currentOperation = null;
        leftPoint = false;
        rightPoint = false;
        displayExpression();
    }

    public void arrowClicked(View view) {
        removeLastSymbol();
        displayExpression();
    }

    private void removeLastSymbol() {
        if (expression != null && expression.length() > 0) {
            String operations = Operation.getAllOperations();
            String lastSym = endWith();
            if (lastSym != null && operations.contains(lastSym)) {
                currentOperation = null;
            } else if (lastSym != null && lastSym.equals(".")) {
                if (currentOperation == null) leftPoint = false;
                else rightPoint = false;
            }
            expression = expression.substring(0, expression.length() - 1);
        }
    }

    private void displayExpression() {
        String out = "null";
        if (currentOperation != null) out = currentOperation.name();
        Toast.makeText(getApplicationContext(),
                out, Toast.LENGTH_SHORT).show();
        out += leftPart;
        TextView tv2 = (TextView) findViewById(R.id.op_tv);
        tv2.setText(out);
        TextView tv = (TextView) findViewById(R.id.text_view);
        tv.setText(expression);
    }


    private void displayErrorMessage() {
        Toast.makeText(getApplicationContext(), "Wrong expression!", Toast.LENGTH_SHORT).show();
    }

    private String endWith() {
        if (expression.length() == 0) return null;
        else return expression.substring(expression.length() - 1, expression.length());
    }

    public void tryExecute() {
        try {
            String lastSym = endWith();
            if (lastSym == null || currentOperation == null) return;
            String operations = Operation.getAllOperations();
            if (operations.contains(lastSym)) {
                expression = expression.substring(0, expression.length() - 1);
            } else {
                String operation = currentOperation.getSymbol();
                if (operation != null) {
                    String rightPart = expression.replace(leftPartStr + operation, "");
                    double result = currentOperation.doOperation(leftPart, Double.parseDouble(rightPart));
                    if (result == (int) result) {
                        expression = String.valueOf((int) result);
                        leftPoint = false;
                    } else {
                        expression = String.valueOf(result);
                        leftPoint = true;
                    }
                    rightPoint = false;
                    displayExpression();
                }
            }
        } catch (ArithmeticException e) {
            expression = "";
            currentOperation = null;
            leftPoint = false;
            rightPoint = false;
            displayErrorMessage();
        }
    }

    private boolean someOperationClicked(Operation operation) {
        boolean addThisSymbol = true;
        if (expression.length() == 0) addThisSymbol = false;
        else {
            if (currentOperation != null) tryExecute();
            currentOperation = operation;
            leftPartStr = expression;
            leftPart = Double.parseDouble(expression);
        }
        return addThisSymbol;
    }

    @Override
    public void onClick(View view) {
        String tag = view.getTag().toString();
        boolean addThisSymbol = true;
        switch (tag) {
            case "+":
                addThisSymbol = someOperationClicked(Operation.ADD);
                break;
            case "-":
                addThisSymbol = someOperationClicked(Operation.SUB);
                break;
            case "*":
                addThisSymbol = someOperationClicked(Operation.MUL);
                break;
            case "/":
                addThisSymbol = someOperationClicked(Operation.DIV);
                break;
            case "%":
                addThisSymbol = someOperationClicked(Operation.REM);
                break;
            case "=":
                addThisSymbol = false;
                tryExecute();
                currentOperation = null;
                break;
            case ".":
                if (currentOperation == null && !leftPoint) {
                    leftPoint = true;
                }
                else if (currentOperation != null && !rightPoint) {
                    rightPoint = true;
                }
                else addThisSymbol = false;
                break;
        }
        if (addThisSymbol) {
            expression += tag;
        }
        displayExpression();
    }
}