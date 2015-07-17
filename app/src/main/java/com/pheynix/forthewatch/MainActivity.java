package com.pheynix.forthewatch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MyTimer.OnTimeChangeListener, MyTimer.OnSecondChangListener,MyTimer.OnMinChangListener,MyTimer.OnHourChangListener {

    MyTimer timer;
    Button btn_start,btn_stop,btn_reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timer = (MyTimer) findViewById(R.id.timer);
        timer.setOnTimeChangeListener(this);
        timer.setSecondChangListener(this);
        timer.setMinChangListener(this);
        timer.setHourChangListener(this);
        timer.setModel(Model.Timer);
        timer.setStartTime(1,30,30);
        btn_start = (Button) findViewById(R.id.btn_start);
        btn_stop = (Button) findViewById(R.id.btn_stop);
        btn_reset = (Button) findViewById(R.id.btn_reset);
        btn_start.setOnClickListener(this);
        btn_stop.setOnClickListener(this);
        btn_reset.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_start:
                timer.start();
                break;
            case R.id.btn_stop:
                timer.stop();
                break;
            case R.id.btn_reset:
                timer.reset();
                break;
        }
    }

    @Override
    public void onTimerStart(long timeStart) {
        Log.e("pheynix","onTimerStart "+timeStart);
    }

    @Override
    public void onTimeChange(long timeStart, long timeRemain) {
        Log.e("pheynix","onTimeChange timeStart "+timeStart);
        Log.e("pheynix","onTimeChange timeRemain "+timeRemain);
    }

    @Override
    public void onTimeStop(long timeStart, long timeRemain) {
        Log.e("pheynix","onTimeStop timeRemain "+timeStart);
        Log.e("pheynix","onTimeStop timeRemain "+timeRemain);
    }

    @Override
    public void onSecondChange(int second) {
        Log.e("swifty","second change to "+second);
    }

    @Override
    public void onHourChange(int hour) {
        Log.e("swifty","hour change to "+hour);
    }

    @Override
    public void onMinChange(int minute) {
        Log.e("swifty", "minute change to "+minute);
    }

}
