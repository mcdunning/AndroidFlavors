package com.flavorspoc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.flavorspoc.widget.Numberpad;
import com.flavorspoc.widget.NumberpadModel;

public class MainActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getCanonicalName();

    View mainView;
    TextView speed;
    TextView weight;
    Button submit;

    View.OnClickListener parameterClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final TextView textView = (TextView) v;
            final NumberpadModel numberpadModel;
            switch (v.getId()) {
                case R.id.text_view_1_value:
                    numberpadModel = new NumberpadModel(Float.parseFloat(textView.getText().toString()), 0.0f, 25.0f, 2);
                    break;
                case R.id.text_view_2_value:
                    numberpadModel = new NumberpadModel(Float.parseFloat(textView.getText().toString()), 0.0f, 250.0f, 0);
                    break;
                default:
                    numberpadModel = null;
                    Log.e(TAG, "The selected view is not supported");
            }

            if (numberpadModel != null) {
                Numberpad numPad = new Numberpad(mainView.getContext());
                numPad.init(mainView,
                        numberpadModel,
                        cl -> {
                            if (R.id.text_view_2_value == textView.getId()) {
                                textView.setText(Integer.toString((int) numberpadModel.getValue()));
                            } else {
                                textView.setText(Float.toString(numberpadModel.getValue()));
                            }
                            numPad.hide();
                        }
                );

                numPad.show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mainView = findViewById(R.id.main_view);
        speed = findViewById(R.id.text_view_1_value);
        weight = findViewById(R.id.text_view_2_value);
        submit = findViewById(R.id.submit);
    }

    @Override
    protected void onResume() {
        super.onResume();

        speed.setText("0.0");
        speed.setOnClickListener(parameterClickListener);

        weight.setText("0");
        weight.setOnClickListener(parameterClickListener);

        submit.setOnClickListener(v -> {
            Toast toast = Toast.makeText(MainActivity.this.getApplicationContext(),
                    getString(R.string.submit_toast_message,
                            getString(R.string.flavor_name_title),
                            getString(R.string.speed_lc),
                            speed.getText().toString(),
                            getString(R.string.weight_lc),
                            weight.getText().toString()),
                    Toast.LENGTH_LONG);
            toast.show();
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        speed.setOnClickListener(null);
        weight.setOnClickListener(null);
        submit.setOnClickListener(null);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mainView = null;
        speed = null;
        weight = null;
        submit = null;

        parameterClickListener = null;
    }
}
