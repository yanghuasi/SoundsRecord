package seekbar.ggh.com.soundsrecord.audio_list;

import android.os.Parcel;
import android.os.Parcelable;

public class DataEntity {
    private String mName; // file name
    private String mFilePath; //file path
    private int mId; //id in database
    private double mLength; // length of recording in seconds
    private long mTime; // date/time of the recording



    public String getFilePath() {
        return mFilePath;
    }

    public void setFilePath(String filePath) {
        mFilePath = filePath;
    }

    public double getLength() {
        return mLength;
    }

    public void setLength(double length) {
        mLength = length;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }




}