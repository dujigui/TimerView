package com.pheynix.forthewatch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MyTimer.OnTimeChangeListener {

    MyTimer timer;
    Button btn_start,btn_stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timer = (MyTimer) findViewById(R.id.timer);
        timer.setOnTimeChangeListener(this);
        btn_start = (Button) findViewById(R.id.btn_start);
        btn_stop = (Button) findViewById(R.id.btn_stop);
        btn_start.setOnClickListener(this);
        btn_stop.setOnClickListener(this);

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
}
