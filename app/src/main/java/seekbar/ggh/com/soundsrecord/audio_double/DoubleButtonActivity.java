package seekbar.ggh.com.soundsrecord.audio_double;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

import seekbar.ggh.com.soundsrecord.audio_list.ListActivity;
import seekbar.ggh.com.soundsrecord.R;
import seekbar.ggh.com.soundsrecord.audio.AMRAudioRecorder;

public class DoubleButtonActivity extends Activity {
    private Button recording;
    private Button stop;
    private Button list;
    private AMRAudioRecorder mediaRecorder;
    private Chronometer chronometer= null;;
    private int mRecordPromptCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording_audio);


        recording = (Button) findViewById(R.id.recording);
        stop = (Button) findViewById(R.id.stop);
        chronometer= (Chronometer) findViewById(R.id.chronometer);
        list = (Button) findViewById(R.id.list);
        //录音要保存的位置（文件夹）
        String uri = Environment.getExternalStorageDirectory() + "/SoundRecorder";
        mediaRecorder = new AMRAudioRecorder(uri);//全局变量

        recording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaRecorder != null) {

                    mediaRecorder.start();

                    chronometer.setBase(SystemClock.elapsedRealtime());
                    chronometer.start();
                    chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                        @Override
                        public void onChronometerTick(Chronometer chronometer) {
//                            if (mRecordPromptCount == 0) {
//                                recording_text.setText(getString(R.string.function_recording) + ".");
//                                recording_text.getResources().getColor(R.color.app_pic_check);
//                            } else if (mRecordPromptCount == 1) {
//                                recording_text.setText(getString(R.string.function_recording) + "..");
//                                recording_text.getResources().getColor(R.color.app_pic_check);
//                            } else if (mRecordPromptCount == 2) {
//                                recording_text.setText(getString(R.string.function_recording) + "...");
//                                recording_text.getResources().getColor(R.color.app_pic_check);
//                                mRecordPromptCount = -1;
//                            }

                            mRecordPromptCount++;
                        }
                    });
                    Toast.makeText(DoubleButtonActivity.this,"开始录音" +mediaRecorder.getAudioFilePath(), Toast.LENGTH_LONG).show();
                }
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaRecorder != null) {
                    mediaRecorder.stop();
                    chronometer.stop();
                    chronometer.setBase(SystemClock.elapsedRealtime());
                }
                Toast.makeText(DoubleButtonActivity.this,"停止录音"+mediaRecorder.getAudioFilePath() , Toast.LENGTH_LONG).show();
            }
        });
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DoubleButtonActivity.this, ListActivity.class));
            }
        });
    }


}

