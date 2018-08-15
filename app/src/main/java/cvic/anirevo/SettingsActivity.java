package cvic.anirevo;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {

    public static final String TAG = "cvic.anirevo.SETTINGS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Switch switch13 = findViewById(R.id.settings_switch_13);
        Switch switch18 = findViewById(R.id.settings_switch_18);

        switch13.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                toggle13Button(compoundButton, b);
            }
        });

        switch18.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                toggle18Button(compoundButton, b);
            }
        });
    }

    private void toggle13Button(CompoundButton buttonView, boolean isChecked) {
        //Log.i(TAG, "13+ btn toggled");
        Switch switch18 = findViewById(R.id.settings_switch_18);
        if (isChecked) {
            //enable 18+ button
            switch18.setClickable(true);
            switch18.setTextColor(Color.BLACK);
        } else {
            findViewById(R.id.settings_switch_18).setClickable(false);
            if(switch18.isChecked()) {
                switch18.toggle();
            }
            switch18.setTextColor(Color.GRAY);
        }
    }

    private void toggle18Button(CompoundButton buttonView, boolean isChecked) {
        //Log.i(TAG, "18+ btn toggled");
    }

}
