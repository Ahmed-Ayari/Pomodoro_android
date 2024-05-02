package com.example.pomodoro_java;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pomodoro_java.databinding.ActivityMainBinding;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    TextInputEditText name_input;
    Button confirm_btn;

    public static final String Extra_Name = "name";
    public static final String NAME_PREF = "name_pref";
    public static final String ACTIVITY_PREF = "activity_pref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        name_input = findViewById(R.id.name_input);

        SharedPreferences namePref = getSharedPreferences(NAME_PREF, Context.MODE_PRIVATE);
        String savedName = namePref.getString(Extra_Name, "");
        name_input.setText(savedName);

        confirm_btn = findViewById(R.id.confirm_btn);

        confirm_btn.setOnClickListener(v -> {
            String userName = Objects.requireNonNull(name_input.getText()).toString();
            SharedPreferences.Editor nameEditor = namePref.edit();
            nameEditor.putString(Extra_Name, userName);
            nameEditor.commit();

            Intent intent = new Intent(MainActivity.this, Parameters.class);
            intent.putExtra(Extra_Name, userName);
            startActivity(intent);
        });

        SharedPreferences activityPref = getSharedPreferences(ACTIVITY_PREF, Context.MODE_PRIVATE);
        boolean activityExecuted = activityPref.getBoolean("activity_executed", false);
        if (activityExecuted) {
            Intent intent = new Intent(MainActivity.this, Parameters.class);
            startActivity(intent);
            finish();
        } else {
            SharedPreferences.Editor activityEditor = activityPref.edit();
            activityEditor.putBoolean("activity_executed", true);
            activityEditor.commit();
        }
    }

}