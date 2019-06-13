package seekbar.ggh.com.soundsrecord.audio_sigle;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import java.io.File;

import seekbar.ggh.com.soundsrecord.R;
import seekbar.ggh.com.soundsrecord.audio.AMRAudioRecorder;
import seekbar.ggh.com.soundsrecord.audio.FileUtils;

public class SigleButtonActivity extends Activity {

    private FloatingActionButton mRecordButton = null;
    private AMRAudioRecorder mediaRecorder;
    private Chronometer mChronometer = null;
    private int mRecordPromptCount = 0;
    private boolean mStartRecording = true;
    private TextView mRecordingPrompt;
    long timeWhenPaused = 0; //stores time when user clicks pause button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_sigle);


        mRecordButton = (FloatingActionButton) findViewById(R.id.btnRecord);
        mRecordingPrompt = (TextView) findViewById(R.id.recording_status_text);
        mChronometer = (Chronometer) findViewById(R.id.chronometer);

        //录音要保存的位置（文件夹）
        String uri = Environment.getExternalStorageDirectory() + "/SoundRecorder";
        mediaRecorder = new AMRAudioRecorder(uri);//全局变量

        mRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRecord(mStartRecording);
                mStartRecording = !mStartRecording;

            }
        });


    }

    //TODO: recording pause
    private void onRecord(boolean start) {

        if (start) {
            // start recording
            mRecordButton.setImageResource(R.drawable.ic_media_stop);
            //mPauseButton.setVisibility(View.VISIBLE);
            Toast.makeText(SigleButtonActivity.this,R.string.toast_recording_start,Toast.LENGTH_SHORT).show();
//            File folder = new File(Environment.getExternalStorageDirectory() + "/SoundRecorder");
//            if (!folder.exists()) {
//                //folder /SoundRecorder doesn't exist, create the folder
//                folder.mkdir();
//            }

            //start Chronometer
            mChronometer.setBase(SystemClock.elapsedRealtime());
            mChronometer.start();
            mChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                @Override
                public void onChronometerTick(Chronometer chronometer) {
                    if (mRecordPromptCount == 0) {
                        mRecordingPrompt.setText(getString(R.string.record_in_progress) + ".");
                    } else if (mRecordPromptCount == 1) {
                        mRecordingPrompt.setText(getString(R.string.record_in_progress) + "..");
                    } else if (mRecordPromptCount == 2) {
                        mRecordingPrompt.setText(getString(R.string.record_in_progress) + "...");
                        mRecordPromptCount = -1;
                    }

                    mRecordPromptCount++;
                }
            });

            //start RecordingService
            mediaRecorder.start();


            mRecordingPrompt.setText(getString(R.string.record_in_progress) + ".");
            mRecordPromptCount++;

        } else {
            //stop recording
            mRecordButton.setImageResource(R.drawable.ic_mic_white_36dp);
            //mPauseButton.setVisibility(View.GONE);
            mChronometer.stop();
            mChronometer.setBase(SystemClock.elapsedRealtime());
            timeWhenPaused = 0;
            mRecordingPrompt.setText(getString(R.string.record_prompt));

            mediaRecorder.stop();
            renameFileDialog();

        }
    }
    public void renameFileDialog() {
        // File rename dialog
        AlertDialog.Builder renameFileBuilder = new AlertDialog.Builder(SigleButtonActivity.this);

        LayoutInflater inflater = LayoutInflater.from(SigleButtonActivity.this);
        View view = inflater.inflate(R.layout.dialog_rename_file, null);

        final EditText input = view.findViewById(R.id.new_name);

        renameFileBuilder.setTitle(SigleButtonActivity.this.getString(R.string.dialog_title_rename));
        renameFileBuilder.setCancelable(true);
        renameFileBuilder.setPositiveButton(SigleButtonActivity.this.getString(R.string.dialog_action_ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            String newName = input.getText().toString().trim() + ".amr";
                            String path = mediaRecorder.getAudioFilePath();
                            String oldName = FileUtils.extractFileName(path);
//                            FileUtils.reNamePath(oldName,newName);
                            seekbar.ggh.com.soundsrecord.audio_sigle.FileUtils.rename(mediaRecorder.getAudioFilePath(), newName);
//                            FileUtils.getResourceStream(path);


                        } catch (Exception e) {
                            Log.e("0", "exception", e);
                        }

                        dialog.cancel();
                    }
                });
        renameFileBuilder.setNegativeButton(SigleButtonActivity.this.getString(R.string.dialog_action_cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        renameFileBuilder.setView(view);
        AlertDialog alert = renameFileBuilder.create();
        alert.show();
    }
}


