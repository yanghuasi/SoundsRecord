package seekbar.ggh.com.soundsrecord.audio_list;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class DataEntity implements Parcelable {
    private String mName; // file name
    private String mFilePath; //file path
    private int mId; //id in database
    private double mLength; // length of recording in seconds
    private long mTime; // date/time of the recording


    protected DataEntity(Parcel in) {
        mName = in.readString();
        mFilePath = in.readString();
        mId = in.readInt();
        mLength = in.readDouble();
        mTime = in.readLong();
    }

    public static final Creator<DataEntity> CREATOR = new Creator<DataEntity>() {
        @Override
        public DataEntity createFromParcel(Parcel in) {
            return new DataEntity(in);
        }

        @Override
        public DataEntity[] newArray(int size) {
            return new DataEntity[size];
        }
    };

    public DataEntity() {

    }

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeString(mFilePath);
        dest.writeInt(mId);
        dest.writeDouble(mLength);
        dest.writeLong(mTime);
    }
}