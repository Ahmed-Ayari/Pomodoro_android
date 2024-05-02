package com.example.pomodoro_java;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class Parameters extends AppCompatActivity {

    TextView welcome;

    TextInputEditText studyTime;
    TextInputEditText breakTime;
    TextInputEditText roundCount;
    TextInputEditText taskIn;

    TextView start;

    //variables to send
    public static final String Extra_Stime = "study Time";
    public static final String Extra_Btime = "Break Time";
    public static final String Extra_Rcount = "Round Count";
    public static final String Extra_Task = "Task";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_parameters);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.parametersActivity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //integers
        AtomicInteger study_Time = new AtomicInteger(25);
        AtomicInteger break_time = new AtomicInteger(5);
        AtomicInteger round_count = new AtomicInteger(4);

        //User Name
        SharedPreferences namePref = getSharedPreferences(MainActivity.NAME_PREF, Context.MODE_PRIVATE);
        String userName = namePref.getString(MainActivity.Extra_Name, "");

        //find view by id
        welcome = findViewById(R.id.welcome_tv);
        studyTime = findViewById(R.id.study_time);
        breakTime = findViewById(R.id.break_time);
        roundCount = findViewById(R.id.round_count);
        taskIn = findViewById(R.id.task_input);
        start = findViewById(R.id.start_timer);


        //welcome message + name
        welcome.append(" " + userName);

        //on button click
        start.setOnClickListener(v -> {

            //convert to String
            String s_sTime = Objects.requireNonNull(studyTime.getText()).toString();
            String s_bTime = Objects.requireNonNull(breakTime.getText()).toString();
            String s_rCount = Objects.requireNonNull(roundCount.getText()).toString();

            //convert to int
            if(checkString(s_sTime)){
                study_Time.set(Integer.parseInt(s_sTime));
            }
            if(checkString(s_bTime)){
                break_time.set(Integer.parseInt(s_bTime));
            }
            if(checkString(s_rCount)){
                round_count.set(Integer.parseInt(s_rCount));
            }

            //variables to send
            String task = Objects.requireNonNull(taskIn.getText()).toString();
            Intent intent1 = new Intent(Parameters.this, Timer.class);
            intent1.putExtra(Extra_Stime, study_Time.get());
            intent1.putExtra(Extra_Btime, break_time.get());
            intent1.putExtra(Extra_Rcount, round_count.get());
            intent1.putExtra(Extra_Task, task);

            startActivity(intent1);
        });

    }

    boolean checkString(String s){

       boolean numeric = true;
       try {
           int n = Integer.parseInt(s);
       } catch (NumberFormatException e) {
           numeric = false;
       }
       return numeric;
    }

}