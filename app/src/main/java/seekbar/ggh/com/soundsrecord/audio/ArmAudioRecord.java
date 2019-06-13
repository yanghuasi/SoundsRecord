package seekbar.ggh.com.soundsrecord.audio;

public interface ArmAudioRecord {
    void initRecord(String uri);

    void startRecord();

    void stopRecord();

    void resumeRecord();

    void pauseRecord();

    void resetRecord();

    boolean isRecord();

    String getAudioFilePath();

    double getDBForMic();
}
