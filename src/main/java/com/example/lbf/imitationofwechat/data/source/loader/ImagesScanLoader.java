package com.example.lbf.imitationofwechat.data.source.loader;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.lbf.imitationofwechat.beans.ImageFolderBean;
import com.example.lbf.imitationofwechat.data.source.ChatsRepository;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by lbf on 2016/7/30.
 */
public class ImagesScanLoader extends BaseLoader<String> {

    private List<ImageFolderBean> imageFolderList;
    private Set<String> pathSet;
    int maxSize = 0;

    public ImagesScanLoader(Context context, List<ImageFolderBean> imageFolderList) {
        super(context);
        this.imageFolderList = imageFolderList;
        pathSet = new HashSet<>();
    }

    public ImagesScanLoader(Context context, ChatsRepository repository , List<ImageFolderBean> imageFolderList) {
        super(context,repository);
        this.imageFolderList = imageFolderList;
        pathSet = new HashSet<>();
    }

    @Override
    public String loadInBackground() {
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = new String[]{MediaStore.Images.Media.DATA};
        String selection = MediaStore.Images.Media.MIME_TYPE +"=? or "
                + MediaStore.Images.Media.MIME_TYPE +"=?";
        String[] selectionArgs = new String[]{"image/jpeg","image/png"};
        String result = "";
        Cursor cursor = mContext.getContentResolver().query(uri,projection,selection,selectionArgs,
                MediaStore.Images.Media.DATE_MODIFIED);
        if(cursor.moveToFirst()){
            do{
                String path = cursor.getString(0);
                File file = new File(path);
                File parentFile = file.getParentFile();
                if(pathSet.contains(parentFile.getAbsolutePath())){
                    continue;
                }else{
                    pathSet.add(parentFile.getAbsolutePath());
                    if(parentFile.list() == null){
                        continue;
                    }
                    int imageCount = parentFile.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String filename) {
                            if(filename.endsWith(".jpg")||filename.endsWith(".png")){
                                return true;
                            }
                            return false;
                        }
                    }).length;
                    ImageFolderBean bean = new ImageFolderBean(
                            parentFile.getAbsolutePath(),file.getName(),imageCount);
                    imageFolderList.add(bean);
                    if(imageCount>maxSize){
                        maxSize = imageCount;
                        result = parentFile.getAbsolutePath();
                    }
                }
            }while(cursor.moveToNext());
        }
        return result;
    }

}
