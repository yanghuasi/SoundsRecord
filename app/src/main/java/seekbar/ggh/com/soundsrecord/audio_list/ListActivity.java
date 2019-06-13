package seekbar.ggh.com.soundsrecord.audio_list;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import seekbar.ggh.com.soundsrecord.R;
import seekbar.ggh.com.soundsrecord.audio.AMRAudioRecorder;
import seekbar.ggh.com.soundsrecord.audio.FileManager;
import seekbar.ggh.com.soundsrecord.audio_sigle.FileUtils;


public class ListActivity extends Activity {
    private RecyclerView rv;
    private ListAdapter adapter;
    private List<DataEntity> sounds;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        rv = (RecyclerView)findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));

        String uri = Environment.getExternalStorageDirectory() + "/SoundRecorder";
//        AMRAudioRecorder amrAudioRecorder=new AMRAudioRecorder(uri);
//        String path = Environment.getExternalStorageDirectory() + "/SoundRecorder"+amrAudioRecorder.getAudioFilePath();
//        adapter = new ListAdapter(getFilesSizeModifyTime(uri));
        sounds = new ArrayList<>();
        List<String> filesPath = FileManager.getFilePathFromFolder(uri);
        for (String path:filesPath){
            DataEntity entity = new DataEntity();
            entity.setFilePath(path);//设置路径
           //你要设置adapter需要的元素才有的跑啊要不然就全注释掉
            entity.setName(FileManager.extractFileName(path));
//            entity.setTime(System.currentTimeMillis());
//            entity.setLength( FileManager.getFileSize(path));
            sounds.add(entity);
        }

        adapter = new ListAdapter(sounds);
        rv.setAdapter(adapter);


    }
//    public static List<DataEntity> getFilesSizeModifyTime(String folderPath){
//        List<DataEntity> returnList = new ArrayList();
//        List<String> filePathList = seekbar.ggh.com.soundsrecord.audio.FileManager.getFilePathFromFolder(folderPath);
//        for(int i=0;i<filePathList.size();i++){
//            List<String> tempList = new ArrayList();
//            String filePath = (String)filePathList.get(i);
//            String modifyTime = seekbar.ggh.com.soundsrecord.audio.FileManager.fileModifyTime(filePath);
//            Double fileSize = seekbar.ggh.com.soundsrecord.audio.FileManager.getFileSize(filePath);
//            tempList.add(filePath);
//            tempList.add(modifyTime);
//            tempList.add(fileSize);
//            returnList.add((DataEntity) tempList);
//        }
//        return returnList;
//    }

    public  List<DataEntity>getAllRecord(String folderPath){
        List<DataEntity> list = new ArrayList<>();
        if (!TextUtils.isEmpty(folderPath)) {
            if (FileUtils.isFileExists(folderPath)) {
                File file = FileUtils.getFileByPath(folderPath);
                String[] files = file.list();
                if (files!=null){
                    for (String fs : files) {
                        DataEntity bean = new DataEntity();
                        String filePath = folderPath + "/" + fs;
                        boolean isFile = FileUtils.isFile(filePath);
                        bean.setFilePath(filePath);
                        bean.setName(FileUtils.getFileName(filePath));
                        bean.setLength((int) FileUtils.getFileLength(filePath));
                        bean.setTime(FileUtils.getFileLastModified(filePath));
                        if (isFile) {
                            if (checkIsSoundFile(bean.getName())) {
                                list.add(bean);
                            }
                        } else {
                            list.add(bean);
                        }

                    }
                }

            }
        }
        return list;
    }
    /**
     * 检查扩展名，得到音频格式的文件
     *
     * @param fName 文件名
     * @return
     */
    @SuppressLint("DefaultLocale")
    private boolean checkIsSoundFile(String fName) {
        boolean isSoundFile = false;
        // 获取扩展名
        String FileEnd = fName.substring(fName.lastIndexOf(".") + 1,
                fName.length()).toLowerCase();
        if (FileEnd.equals("mp3") || FileEnd.equals("mp4")
                || FileEnd.equals("amr") || FileEnd.equals("wav")) {
            //|| FileEnd.equals("gif")
            isSoundFile = true;
        } else {
            isSoundFile = false;
        }
        return isSoundFile;
    }
}
