package seekbar.ggh.com.soundsrecord.audio;

import android.media.MediaRecorder;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Android does not support pause and resume when recording amr audio,
 * so we implement it to provide pause and resume funciton.
 * <p>
 * Created by Water Zhang on 11/25/15.
 */
public class AMRAudioRecorder {

    private boolean singleFile = true;

    private MediaRecorder recorder;

    private ArrayList<String> files = new ArrayList<String>();

    private String fileDirectory;

    private String finalAudioPath;

    private boolean isRecording;

    public boolean isRecording() {
        return isRecording;
    }

    public String getAudioFilePath() {
        return finalAudioPath;
    }

    public AMRAudioRecorder(String audioFileDirectory) {
        this.fileDirectory = audioFileDirectory;

        if (!this.fileDirectory.endsWith("/")) {
            this.fileDirectory += "/";
        }

//        if (FileManager.isFolderExist(fileDirectory)) {
//            FileManager.deleteAllFile(fileDirectory, false);
//        } else {
//        }

        newRecorder();
    }

    public boolean start() {
        Log.d("record", "start");
        try {
            prepareRecorder();
            recorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (recorder != null) {
            recorder.start();
            isRecording = true;

            return true;
        }

        return false;
    }

    public boolean pause() {
        Log.d("record", "pause");
        if (recorder == null || !isRecording) {
            throw new IllegalStateException("[AMRAudioRecorder] recorder is not recording!");
        }

        recorder.stop();
        recorder.release();
        recorder = null;

        isRecording = false;

        return merge();
    }

    public boolean resume() {
        Log.d("record", "resume");
        if (isRecording) {
            throw new IllegalStateException("[AMRAudioRecorder] recorder is recording!");
        }

        singleFile = false;
        newRecorder();
        return start();
    }

    public boolean stop() {
        Log.d("record", "stop");
        if (!isRecording) {
            return merge();
        }

        if (recorder == null) {
            return false;
        }

        recorder.stop();
        recorder.release();
        recorder = null;
        isRecording = false;

        return merge();
    }

    public void clear() {
        if (recorder != null || isRecording) {
            recorder.stop();
            isRecording = false;
        }
        for (int i = 0, len = files.size(); i < len; i++) {
            File file = new File(this.files.get(i));
            file.delete();
        }
    }

    public void reset() {
        //删除所有文件
        if (files.size() > 0) {
            files.clear();
        }
        if (recorder != null) {
            recorder.reset();
        }
        isRecording = false;
        newRecorder();
    }

    public void destroy(){
        if (recorder!=null){
            recorder.reset();
            recorder.release();
            recorder = null;
        }

    }

    private boolean merge() {

        // If never paused, just return the file
        if (singleFile) {
            this.finalAudioPath = this.files.get(0);
            return true;
        }

        // Merge files
        String mergedFilePath = this.fileDirectory + new Date().getTime() + ".amr";
        try {
            FileOutputStream fos = new FileOutputStream(mergedFilePath);

            for (int i = 0, len = files.size(); i < len; i++) {
                File file = new File(this.files.get(i));
                FileInputStream fis = new FileInputStream(file);

                // Skip file header bytes,
                // amr file header's length is 6 bytes
                if (i > 0) {
                    for (int j = 0; j < 6; j++) {
                        fis.read();
                    }
                }

                byte[] buffer = new byte[512];
                int count = 0;
                while ((count = fis.read(buffer)) != -1) {
                    fos.write(buffer, 0, count);
                }

                fis.close();
                fos.flush();
                file.delete();
            }

            fos.flush();
            fos.close();

            this.finalAudioPath = mergedFilePath;
            files.clear();
            files.add(finalAudioPath);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    private void newRecorder() {
        recorder = new MediaRecorder();
    }

    private void prepareRecorder() {
        File directory = new File(this.fileDirectory);
        if (!directory.exists() || !directory.isDirectory()) {
            directory.mkdirs();
        }

        String filePath = directory.getAbsolutePath() + "/" + new Date().getTime() + ".amr";
        this.files.add(filePath);

        recorder.setOutputFile(filePath);
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
    }

    public double getDBForMic() {
        if (recorder != null) {
            double ratio = (double) recorder.getMaxAmplitude();
            double db = 0;// 分贝
            if (ratio > 1)
                db = 20 * Math.log10(ratio);
            Log.d("record", "分贝值：" + db);

            return db;
        }
        return 0;
    }
}
