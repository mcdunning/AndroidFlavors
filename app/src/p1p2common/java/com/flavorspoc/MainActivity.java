package java.com.flavorspoc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.flavorspoc.BuildConfig;
import com.flavorspoc.R;
import com.flavorspoc.widget.Numberpad;
import com.flavorspoc.widget.NumberpadModel;

public class MainActivity extends AppCompatActivity {
    View mainView;
    TextView flavor1SpecificDialog;
    TextView speed;
    Button submit;

    View.OnClickListener speedClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final NumberpadModel numberpadModel = new NumberpadModel(0.0f, 25.0f, 2);
            Numberpad numPad = new Numberpad(mainView, numberpadModel, cl -> speed.setText(Float.toString(numberpadModel.getValue())));
            numPad.show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainView = findViewById(R.id.main_view);
        flavor1SpecificDialog = findViewById(R.id.platform1SpecificDialog);
        speed = findViewById(R.id.speed);
        submit = findViewById(R.id.submit);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if ("platform1".equals(BuildConfig.FLAVOR)) {
            flavor1SpecificDialog.setVisibility(View.VISIBLE);
        } else {
            flavor1SpecificDialog.setVisibility(View.GONE);
        }
        speed.setText("0.0");
        speed.setOnClickListener(speedClickListener);
        submit.setOnClickListener(v -> {
            Toast toast = Toast.makeText(MainActivity.this.getApplicationContext(), getString(R.string.submit_toast_message, getString(R.string.flavor_name_title), speed.getText().toString()), Toast.LENGTH_LONG);
            toast.show();
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        speed.setOnClickListener(null);
        submit.setOnClickListener(null);
    }
}
