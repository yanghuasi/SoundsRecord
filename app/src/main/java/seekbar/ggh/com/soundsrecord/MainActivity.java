package seekbar.ggh.com.soundsrecord;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import seekbar.ggh.com.soundsrecord.audio_double.DoubleButtonActivity;
import seekbar.ggh.com.soundsrecord.audio_list.ListActivity;
import seekbar.ggh.com.soundsrecord.audio_sigle.SigleButtonActivity;

/**
 * 两个按钮的，一个开始录音，一个停止录音
 */
public class MainActivity extends Activity {
    private Button sigle;
    private Button two;
    private Button list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        sigle = (Button) findViewById(R.id.sigle);
        two = (Button) findViewById(R.id.two);
        list = (Button) findViewById(R.id.list);


        sigle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SigleButtonActivity.class));
                }

        });
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, DoubleButtonActivity.class));
            }
        });
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ListActivity.class));
            }
        });
    }


}
