package cn.zhengshang.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.zhengshang.wordalarm.R;

public class Settings extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.settings);

        findViewById(R.id.title_settings).setVisibility(View.INVISIBLE);
        TextView title = (TextView) findViewById(R.id.title_name);
        title.setText("����");


        findViewById(R.id.title_back).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                doSaveSettings();
                finish();
            }

        });

        ListView listVie = (ListView) findViewById(R.id.list);

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < 10; i++) {
            Map<String, Object> user = new HashMap<String, Object>();
            user.put("img", R.drawable.set_img);
            user.put("name", "ѡ��" + i + ")");

            list.add(user);
        }

        SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.detailset, new String[]{"img", "name"}, new int[]{R.id.set_img, R.id.set_name});

        listVie.setAdapter(adapter);

    }

    private void doSaveSettings() {
        // TODO Auto-generated method stub

    }

}
