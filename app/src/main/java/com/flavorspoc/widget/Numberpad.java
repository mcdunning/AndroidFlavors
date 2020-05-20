package com.flavorspoc.widget;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.flavorspoc.R;

public class Numberpad extends AppCompatActivity {
    private final String TAG = getClass().getCanonicalName();

    private View anchor;

    private NumberpadModel numberpadModel;
    private View.OnClickListener onConfirm;

    private TextView value;
    private ImageButton one;
    private ImageButton two;
    private ImageButton three;
    private ImageButton four;
    private ImageButton five;
    private ImageButton six;
    private ImageButton seven;
    private ImageButton eight;
    private ImageButton nine;
    private ImageButton zero;
    private ImageButton decimal_point;
    private ImageButton submit;
    private ImageButton backspace;

    public Numberpad(final View anchor, final NumberpadModel numberpadModel, final View.OnClickListener onConfirm) {
        this.anchor = anchor;
        this.numberpadModel = numberpadModel;
        this.onConfirm = onConfirm;
    }

    private View.OnClickListener numberpadButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.nine:
                    handleNumberPress(R.string.nine);
                    break;
                case R.id.eight:
                    handleNumberPress(R.string.eight);
                    break;
                case R.id.seven:
                    handleNumberPress(R.string.seven);
                    break;
                case R.id.six:
                    handleNumberPress(R.string.six);
                    break;
                case R.id.five:
                    handleNumberPress(R.string.five);
                    break;
                case R.id.four:
                    handleNumberPress(R.string.four);
                    break;
                case R.id.three:
                    handleNumberPress(R.string.three);
                    break;
                case R.id.two:
                    handleNumberPress(R.string.two);
                    break;
                case R.id.one:
                    handleNumberPress(R.string.one);
                    break;
                case R.id.zero:
                    handleNumberPress(R.string.zero);
                    break;
                case R.id.decimal_point:
                    handleNumberPress(R.string.decimal_point);
                    break;
                case R.id.submit:
                    numberpadModel.setValue(Float.parseFloat(value.getText().toString()));
                    onConfirm.onClick(v);
                case R.id.backspace:
                    CharSequence currentValue = value.getText();
                    if (currentValue.length() != 0) {
                        if (currentValue.length() == 1) {
                            value.setText("");
                        } else {
                            value.setText(currentValue.subSequence(0, currentValue.length() - 1));
                        }
                    }
                    break;
                default:
                    Log.d(TAG, "The view id," + v.getId() + ", is not supported by the keypad");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numerpad);

        value = findViewById(R.id.value);
        backspace = findViewById(R.id.backspace);
        nine = findViewById(R.id.nine);
        eight = findViewById(R.id.eight);
        seven = findViewById(R.id.seven);
        six = findViewById(R.id.six);
        five = findViewById(R.id.five);
        four = findViewById(R.id.four);
        three = findViewById(R.id.three);
        two = findViewById(R.id.two);
        one = findViewById(R.id.one);
        zero = findViewById(R.id.zero);
        decimal_point = findViewById(R.id.decimal_point);
        submit = findViewById(R.id.submit);
    }

    @Override
    protected void onResume() {
        super.onResume();

        backspace.setOnClickListener(numberpadButtonClickListener);
        nine.setOnClickListener(numberpadButtonClickListener);
        eight.setOnClickListener(numberpadButtonClickListener);
        seven.setOnClickListener(numberpadButtonClickListener);
        six.setOnClickListener(numberpadButtonClickListener);
        five.setOnClickListener(numberpadButtonClickListener);
        four.setOnClickListener(numberpadButtonClickListener);
        three.setOnClickListener(numberpadButtonClickListener);
        two.setOnClickListener(numberpadButtonClickListener);
        one.setOnClickListener(numberpadButtonClickListener);
        zero.setOnClickListener(numberpadButtonClickListener);
        decimal_point.setOnClickListener(numberpadButtonClickListener);
        submit.setOnClickListener(onConfirm);

    }

    @Override
    protected void onPause() {
        super.onPause();

        backspace.setOnClickListener(null);
        nine.setOnClickListener(null);
        eight.setOnClickListener(null);
        seven.setOnClickListener(null);
        six.setOnClickListener(null);
        five.setOnClickListener(null);
        four.setOnClickListener(null);
        three.setOnClickListener(null);
        two.setOnClickListener(null);
        one.setOnClickListener(null);
        zero.setOnClickListener(null);
        decimal_point.setOnClickListener(null);
        submit.setOnClickListener(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        anchor = null;
        numberpadModel = null;
        onConfirm = null;

        value = null;
        backspace = null;
        nine = null;
        eight = null;
        seven = null;
        six = null;
        five = null;
        four = null;
        three = null;
        two = null;
        one = null;
        zero = null;
        decimal_point = null;
        submit = null;
    }

    public void hide() {
        finish();
    }

    private void handleNumberPress(int numberpadKeyId) {
        boolean updateValue = false;
        StringBuilder newValue = new StringBuilder(value.getText().toString());
        String passedValue = getResources().getString(numberpadKeyId);
        if (R.string.decimal_point == numberpadKeyId) {
            if (!newValue.toString().contains(passedValue)) {
                newValue.append(passedValue);
                updateValue = true;
            }
        } else {
            int decimalPlaceIndex = newValue.toString().indexOf(getResources().getString(R.string.decimal_point));
            if (decimalPlaceIndex == -1 || (newValue.toString().length() - decimalPlaceIndex -1) < numberpadModel.getPrecision()) {
                newValue.append(passedValue);
                Float newFloatValue = Float.parseFloat(newValue.toString());
                updateValue = newFloatValue <= numberpadModel.getMax() && newFloatValue >= numberpadModel.getMin();
            }
        }

        if (updateValue) {
            value.setText(newValue.toString());
        }
    }

    public void show() {
        anchor.setAlpha(0);
        
    }
}
