package com.wolfer.view;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.wolfer.lalarm.R;
import com.wolfer.service.MyTime;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final int SET_CODE = 1;
    public static final int WORD_CODE = 2;
    public long time = 0;

    private MyTime myTime = new MyTime();
    private int count = 0;
    private ListView listView;
    private ArrayList<String> list_display = new ArrayList<String>();
    private ArrayAdapter adapter;

    private AlarmManager alarmManager;
    private Intent intent;
    private PendingIntent pi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        String str = null;

        if (savedInstanceState != null) {
            for (int i = 0; i < savedInstanceState.size() - 1; i++) {
                str = savedInstanceState.getString("" + i);
                list_display.add(str);
            }
        }

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        intent = new Intent(MainActivity.this, AlarmActivity.class);

        findViewById(R.id.title_back).setVisibility(View.GONE);

        listView = (ListView) findViewById(R.id.main_list);

        adapter = new ArrayAdapter(MainActivity.this, R.layout.list_layout, R.id.list_time, list_display);
        listView.setAdapter(adapter);

        // add listView longpress listener.
        listView.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                final int position = arg2;
                new AlertDialog.Builder(MainActivity.this).setTitle("ɾ��?").setPositiveButton("��", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        list_display.remove(position);
                        pi = PendingIntent.getActivity(MainActivity.this, position + 1, intent, 0);
                        alarmManager.cancel(pi);
                        adapter.notifyDataSetChanged();
                    }
                }).setNegativeButton("��", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
                return false;
            }
        });


        Button buttonSettings = (Button) findViewById(R.id.title_settings);
        buttonSettings.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Settings.class);
                startActivityForResult(intent, SET_CODE);
            }
        });

        findViewById(R.id.alarmset).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new TimePickerDialog(MainActivity.this, new OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        int currentHour, currentMinute;

                        currentHour = myTime.getHour();
                        currentMinute = myTime.getMinutes();

                        if ((currentHour + currentMinute * 0.1) <= (hourOfDay + minute * 0.1))
                            time = (hourOfDay - currentHour) * 3600 + (minute - currentMinute) * 60;
                        else
                            time = (24 - currentHour + hourOfDay) * 3600 + (minute - currentMinute) * 60; // 4:30
                        count++;// ���onTimeSet������ִ��2�飬count����ִֻһ�δ���ת��

                        setDisplayTime(hourOfDay, minute, time);
                    }
                }, myTime.getHour(), myTime.getMinutes(), true).show();

                count = 0;
            }
        });

    }

    private void update(String str) {
        list_display.add(str);

    }

    private void setDisplayTime(int hourOfDay, int minute, long time) {
        if (count == 2) {
            if (minute <= 9) {
                list_display.add(hourOfDay + " : 0" + minute);
            } else {
                list_display.add(hourOfDay + " : " + minute);
            }

            pi = PendingIntent.getActivity(MainActivity.this, list_display.size(), intent, 0);
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000 * time, pi);
            adapter.notifyDataSetChanged();
            // startAlarm(time);
        }
    }

    private void startAlarm(long time) {

        // alarmManager = (AlarmManager)
        // getSystemService(Context.ALARM_SERVICE);

        // alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
        // SystemClock.elapsedRealtime() + 1000 * time, pi);

        Toast.makeText(MainActivity.this, "��ӳɹ�!", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
        if (list_display.size() > 0) {
            for (int i = 0; i < list_display.size(); i++) {
                outState.putString("" + i, list_display.get(i));
            }
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Bundle bundle = new Bundle();
        if (list_display.size() > 0) {
            for (int i = 0; i < list_display.size(); i++) {
                bundle.putString("" + i, list_display.get(i));
            }
        }
    }

}
