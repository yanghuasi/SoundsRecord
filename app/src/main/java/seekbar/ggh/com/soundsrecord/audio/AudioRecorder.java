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
 * <p>
 * Created by ggh on 9/19/2018.
 */
public class AudioRecorder {
    private static AudioRecorder audioRecorder;
    private MediaRecorder mMediaRecorder;
    private String fileDirectory;
    private String finalAudioPath;
    private boolean isRecording;
    private ArrayList<String> files = new ArrayList<String>();

    public static AudioRecorder getAudioRecorder() {
        if (audioRecorder == null) {
            audioRecorder = new AudioRecorder();
        }
        return audioRecorder;
    }

    public AudioRecorder() {
        mMediaRecorder = new MediaRecorder();

    }


    public boolean isRecording() {
        return isRecording;
    }

    public String getAudioFilePath() {
        return finalAudioPath;
    }

    public void setFileDirectory(String fileDirectory) {
        this.fileDirectory = fileDirectory;
        if (!this.fileDirectory.endsWith("/")) {
            this.fileDirectory += "/";
        }
    }

    public void startRecord() {
        File directory = new File(this.fileDirectory);
        if (!directory.exists() || !directory.isDirectory()) {
            directory.mkdirs();
        }
        String filePath = directory.getAbsolutePath() + "/" + new Date().getTime() + ".amr";
        files.add(filePath);
        try {
            if (mMediaRecorder != null) {
                mMediaRecorder.reset();
                mMediaRecorder.setOutputFile(filePath);
                mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
                mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                mMediaRecorder.prepare();
                mMediaRecorder.start();
                isRecording = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void stop() {
        try {
            if (!isRecording) {
                merge();
            } else {
                if (mMediaRecorder != null)
                    mMediaRecorder.stop();
                isRecording = false;
                merge();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void reset() {
        //删除所有文件
        if (files.size() > 0) {
            files.clear();
            Log.d("audio", "已清除所有文件" + files.size());
        }
        if (mMediaRecorder != null) {
            mMediaRecorder.reset();
        }
    }

    public void destroy() {
        clear();
        if (mMediaRecorder != null) {
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
            audioRecorder = null;
        }

    }

    public void clear() {
        try {
            if (mMediaRecorder != null && isRecording) {
                mMediaRecorder.stop();
            }
            isRecording = false;
            for (int i = 0, len = files.size(); i < len; i++) {
                File file = new File(this.files.get(i));
                file.delete();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public double getDBForMic() {
        if (mMediaRecorder != null) {
            double ratio = (double) mMediaRecorder.getMaxAmplitude();
            double db = 0;// 分贝
            if (ratio > 1)
                db = 20 * Math.log10(ratio);
            Log.d("record", "分贝值：" + db);

            return db;
        }
        return 0;
    }

    /**
     * 合并续录文件
     *
     * @return
     */
    private boolean merge() {
        // If never paused, just return the file
        if (files != null && files.size() == 1) {
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
}
