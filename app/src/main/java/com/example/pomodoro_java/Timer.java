package com.example.pomodoro_java;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pomodoro_java.databinding.ActivityTimerBinding;

import java.util.Random;

public class Timer extends AppCompatActivity {

    ActivityTimerBinding binding;

    //Timer
    CountDownTimer restTimer;
    CountDownTimer studyTimer;
    CountDownTimer breakTimer;

    private boolean isStudy = true;
    private boolean isStop = false;

    static int mRound = 1;

    ViewGroup parent;

    CheckBox task;

    String[] Quotes = {"Fall seven times and stand up eight", "It's not that you are so smart, it's just that you stay with problems longer"
    , "Failure is only the opportunity to begin again, this time more intelligently", "Ask yourself this question: 'Will this matter a year from now?'"
    , "Success is the sum of small efforts, repeated day in and day out.", "It does not matter how slowly you go so long as you do not stop."};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTimerBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.timerActivity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        parent = findViewById(R.id.timerActivity);

        //getting variables from parameter
        Intent intent = getIntent();
        int StudyTime = intent.getIntExtra(Parameters.Extra_Stime, 25) * 60 * 1000;
        int BreakTime = intent.getIntExtra(Parameters.Extra_Btime, 5) * 60 * 1000;
        int RoundCount = intent.getIntExtra(Parameters.Extra_Rcount, 4);
        String Task = intent.getStringExtra(Parameters.Extra_Task);

        task = findViewById(R.id.task_check);
        //set quotes
        Random rand = new Random();
        int i = rand.nextInt(Quotes.length);
        binding.motivationTv.setText(Quotes[i]);

        //Set rounds Text
        binding.roundTv.setText(mRound + "/" + RoundCount);

        //check task if empty
        assert Task != null;
        if(Task.isEmpty()){
            parent.removeViewInLayout(task);
        }
        else{
            task.setText(Task);
        }

        //Start timer
        setResetTimer(RoundCount, StudyTime, BreakTime);
        //reset button
        binding.stopIv.setOnClickListener(v -> resetOrStart(RoundCount, StudyTime, BreakTime));

        task.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            if(isChecked){
                task.setPaintFlags(task.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
        }));
    }

    private void setResetTimer(int rCount, int tStudy, int breakT){

        int time;

        binding.getreadyTv.setText("Get Ready");
        binding.progressbar.setProgress(0);
        if(mRound==1){
            binding.progressbar.setMax(10);
            time = 10500;
        }
        else{
            binding.progressbar.setMax(1);
            time = 100;
        }

        restTimer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long p0) {
                binding.progressbar.setProgress((int) (p0/1000));
                binding.timerTv.setText(String.valueOf(p0/1000));
            }

            @Override
            public void onFinish() {
                if (isStudy){
                    setStudyView(rCount, tStudy, breakT);
                }
                else {
                    setBreakView(rCount, tStudy, breakT);
                }
            }
        }.start();
    }

    private void setStudyTimer(int rCount, int tStudy, int breakT){

        studyTimer = new CountDownTimer((long) tStudy + 500, 1000) {
            @Override
            public void onTick(long p0) {
                binding.progressbar.setProgress((int) (p0/1000));
                binding.timerTv.setText(createTimeLabel((int) p0/1000));
            }

            @Override
            public void onFinish() {
                if (mRound < rCount){
                    isStudy = false;
                    mRound++;
                    setResetTimer(rCount, tStudy, breakT);
                }
                else {
                    clearAttribute(rCount);
                    binding.getreadyTv.setText("You have finished your rounds 👏");
                }
            }
        }.start();
    }

    private void setBreakTimer(int rCount, int tStudy, int breakT){
        breakTimer = new CountDownTimer((long) breakT + 500, 1000) {
            @Override
            public void onTick(long p0) {
                binding.progressbar.setProgress((int) (p0/1000));
                binding.timerTv.setText(createTimeLabel((int) p0/1000));
            }

            @Override
            public void onFinish() {
                isStudy = true;
                setResetTimer(rCount, tStudy, breakT);
            }
        }.start();
    }
    private void setStudyView(int rCount, int studyT, int tBreak){

        binding.roundTv.setText(mRound + "/" + rCount);
        binding.getreadyTv.setText("Study Time");
        binding.progressbar.setMax(studyT/1000);

        if(studyTimer != null){
            studyTimer = null;
        }
        setStudyTimer(rCount, studyT, tBreak);
    }

    private void setBreakView(int rCount, int tStudy, int breakT){
        binding.getreadyTv.setText("Break Time");
        binding.progressbar.setMax(breakT/1000);

        if(breakTimer != null){
            breakTimer = null;
        }
        setBreakTimer(rCount, tStudy, breakT);
    }

    private void clearAttribute(int rCount){
        binding.getreadyTv.setText("Press play button to restart");
        binding.stopIv.setImageResource(R.drawable.play);
        binding.progressbar.setProgress(0);
        binding.timerTv.setText("0");
        mRound = 1;
        binding.roundTv.setText(mRound + "/" + rCount);
        if (restTimer != null) {
            restTimer.cancel();
        }
        if (studyTimer != null) {
            studyTimer.cancel();
        }
        if (breakTimer != null) {
            breakTimer.cancel();
        }
        isStop = true;
    }

    private String createTimeLabel(int time){
        String timeLable = "";
        int minutes = time / 60;
        int seconds = time % 60;


        if (minutes < 10) {
            timeLable += "0";
        }
        timeLable += minutes + ":";

        if(seconds<10){
            timeLable += "0";
        }
        timeLable += seconds;

        return timeLable;
    }

    private void resetOrStart(int rCount, int tStudy, int breakT){
        if(isStop){
            binding.stopIv.setImageResource(R.drawable.stop);
            setResetTimer(rCount, tStudy, breakT);
            isStop = false;
        }
        else clearAttribute(rCount);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (restTimer != null) {
            restTimer.cancel();
        }
        if (studyTimer != null) {
            studyTimer.cancel();
        }
        if (breakTimer != null) {
            breakTimer.cancel();
        }
    }
}