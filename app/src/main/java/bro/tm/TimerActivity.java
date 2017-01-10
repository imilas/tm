package bro.tm;

/**
 * Created by root on 02/01/17.
 */

import android.app.Activity;
        import android.os.Bundle;
        import android.os.Handler;
        import android.view.View;
        import android.widget.Button;
        import android.widget.TextView;

public class TimerActivity extends Activity {

    TextView timerTextView;
    long startTime = 0;
    long stopTime = 0;

    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            timerTextView.setText(String.format("%d:%02d", minutes, seconds));

            timerHandler.postDelayed(this, 500);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        timerTextView = (TextView) findViewById(R.id.timer_text);
    }


    public void startStopClicked(View v) {
        Button b = (Button) v;
        if (b.getText().equals("stop")) {
            stopTime=System.currentTimeMillis()-startTime;
            timerHandler.removeCallbacks(timerRunnable);
            b.setText("start");
        } else {
            startTime = System.currentTimeMillis()-stopTime;
            timerHandler.postDelayed(timerRunnable, 0);
            b.setText("stop");
        }
    }

    public void resetClicked(View v) {

            startTime = System.currentTimeMillis();

    }
    @Override
    public void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
    }

}
