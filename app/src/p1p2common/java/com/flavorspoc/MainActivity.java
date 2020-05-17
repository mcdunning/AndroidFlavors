package java.com.flavorspoc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.flavorspoc.BuildConfig;
import com.flavorspoc.R;

public class MainActivity extends AppCompatActivity {
    TextView flavor1SpecificDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        flavor1SpecificDialog = findViewById(R.id.platform1SpecificDialog);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if ("platform1".equals(BuildConfig.FLAVOR)) {
            flavor1SpecificDialog.setVisibility(View.VISIBLE);
        } else {
            flavor1SpecificDialog.setVisibility(View.GONE);
        }
    }
}
