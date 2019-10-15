package cn.zhengshang;

import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import com.android.deskclock.AlarmClockFragment;

import cn.zhengshang.wordalarm.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);


        AlarmClockFragment fragment = new AlarmClockFragment();
        getFragmentManager()
                .beginTransaction()
                .add(R.id.container, fragment)
                .commit();
    }
}
