package com.wolfer.view;

import android.app.Activity;
import android.app.Service;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wolfer.lalarm.R;
import com.wolfer.service.MyTime;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class AlarmActivity extends Activity implements OnClickListener {
    private MediaPlayer mediaPlayer;
    private Button btClose, btOnemin;
    private TextView alarm_time, tvDisplayWord;
    private EditText etInputWord;
    private MyTime myTime = new MyTime();
    private boolean flag;// flag control thread 'set time' close
    private String controlTime = null;
    private String word = null, mean = null;// wordΪ��ʾ�ĵ��ʣ�meanΪ��ʾ����˼��

    private Handler handler = new Handler() {// 10:10:20
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 1:
                    alarm_time.setText((String) msg.obj);
                    if (msg.obj.equals(controlTime)) {
                        mediaPlayer.seekTo(0);
                        mediaPlayer.start();
                        controlTime = "";
                    }
                    break;
            }
        }

    };
    private Vibrator mVibrator01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.alarm_layout);

        flag = true;
        alarm_time = (TextView) findViewById(R.id.alarm_time);
        btClose = (Button) findViewById(R.id.alarm_close);
        btOnemin = (Button) findViewById(R.id.alarm_onnmin);
        tvDisplayWord = (TextView) findViewById(R.id.alarm_display_word);
        etInputWord = (EditText) findViewById(R.id.alarm_input_word);

        alarm_time.setText(myTime.getWholeTime());

        btClose.setOnClickListener(this);
        btOnemin.setOnClickListener(this);

        etInputWord.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Toast.makeText(AlarmActivity.this, "afterTextChanged",
                // Toast.LENGTH_SHORT).show();
                if (word.equals(etInputWord.getText().toString().trim())) {
                    btClose.setEnabled(true);
                    btClose.setBackgroundResource(R.drawable.selector);
                }
            }
        });

        btClose.setBackgroundResource(R.drawable.button_press);
        btClose.setEnabled(false); // Ĭ��Ϊ���ɵ��״̬

        getTime();
        mediaPlayer = MediaPlayer.create(AlarmActivity.this, R.raw.defaultsound);
        mediaPlayer.setLooping(true);

        mediaPlayer.start();
        // ��ʼ��
        mVibrator01 = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);
        mVibrator01.vibrate(new long[]{100, 400, 100, 400}, -1);

        try {
            // ��ȡ�����ļ�
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.words), "gb2312"));

            String line = null;// ���������ϣ���Ȼ��֪��Ϊʲô

            double limit = Math.random() * 3665;
            int count = 0;

            while ((line = bufferedReader.readLine()) != null && (count < limit)) {

                if (line.trim().contains(" ")) {
                    String str[] = line.split(" ");
                    word = str[0];
                    mean = "";
                    for (int j = 1; j < str.length; j++) {
                        mean += str[j];
                    }
                }
                count++;
            }

            tvDisplayWord.setText(word + "\n" + mean);
            bufferedReader.close();

        } catch (Exception e) {
            e.printStackTrace();

            Toast.makeText(AlarmActivity.this, "buzhidao nali cuole ", Toast.LENGTH_SHORT).show();
        }
    }

    private void getTime() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (flag) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message message = new Message();
                    message.what = 1;
                    message.obj = myTime.getWholeTime();
                    handler.sendMessage(message);
                }

            }
        }).start();

    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.alarm_close:

                finish();
                break;
            case R.id.alarm_onnmin:
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();

                    if (myTime.getMinutes() >= 55 && myTime.getHour() == 23)
                        controlTime = "00:0" + (myTime.getMinutes() - 55) + ":" + myTime.getSecond();
                    else if (myTime.getMinutes() >= 55)
                        controlTime = (myTime.getHour() + 1) + ":0" + (myTime.getMinutes() - 55) + ":" + myTime.getSecond();
                    else
                        controlTime = myTime.getHour() + ":" + (myTime.getMinutes() + 5) + ":" + myTime.getSecond();
                }
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null)
            mediaPlayer.release();
        flag = false;
    }

}
