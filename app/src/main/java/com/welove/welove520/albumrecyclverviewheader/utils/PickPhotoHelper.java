package com.welove.welove520.albumrecyclverviewheader.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by wanbo on 2016/12/31.
 */

public class PickPhotoHelper {

    static android.os.Handler r = new android.os.Handler(Looper.getMainLooper());
    public HashMap<String, List<String>> mGroupMap = new LinkedHashMap<>();
    private Context mContext;

    public PickPhotoHelper(Context context) {
        this.mContext = context;
    }

    public void getImages() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = mContext.getContentResolver();

                //jpeg & png
                Cursor mCursor = mContentResolver.query(mImageUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED);

                //jpeg & png & gif
//                Cursor mCursor = mContentResolver.query(mImageUri, null,
//                        MediaStore.Images.Media.MIME_TYPE + "=? or "
//                                + MediaStore.Images.Media.MIME_TYPE + "=? or "
//                                + MediaStore.Images.Media.MIME_TYPE + "=?",
//                        new String[]{"image/jpeg", "image/png", "image/gif"}, MediaStore.Images.Media.DATE_MODIFIED);

                if (mCursor == null) {
                    return;
                }
                List<String> dirNames = new ArrayList<>();
                while (mCursor.moveToNext()) {
                    // get image path
                    String path = mCursor.getString(mCursor
                            .getColumnIndex(MediaStore.Images.Media.DATA));

                    File file = new File(path);
                    if (!file.exists()) {
                        continue;
                    }

                    // get image parent name
                    String parentName = new File(path).getParentFile().getName();
                    Log.d(PickConfig.TAG, parentName + ":" + path);
                    // save all Photo
                    if (!mGroupMap.containsKey(PickConfig.ALL_PHOTOS)) {
                        dirNames.add(PickConfig.ALL_PHOTOS);
                        List<String> chileList = new ArrayList<>();
                        chileList.add(path);
                        mGroupMap.put(PickConfig.ALL_PHOTOS, chileList);
                    } else {
                        mGroupMap.get(PickConfig.ALL_PHOTOS).add(0, path);
                    }
                    // save by parent name
                    if (!mGroupMap.containsKey(parentName)) {
                        dirNames.add(parentName);
                        List<String> chileList = new ArrayList<>();
                        chileList.add(path);
                        mGroupMap.put(parentName, chileList);
                    } else {
                        mGroupMap.get(parentName).add(0, path);
                    }
                }
                mCursor.close();
                GroupImage groupImage = new GroupImage();
                groupImage.groupMap = mGroupMap;
                DirImage dirImage = new DirImage();
                dirImage.dirName = dirNames;
                PickPreferences.getInstance(mContext).saveImageList(groupImage);
                PickPreferences.getInstance(mContext).saveDirNames(dirImage);
                r.post(new Runnable() {
                    @Override
                    public void run() {
                        RxBus.getInstance().send(new ImageLoadOkEvent());
                    }
                });
            }
        }).start();
    }

    public void getImagesAsync() {
        Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver mContentResolver = mContext.getContentResolver();

        //jpeg & png
        Cursor mCursor = mContentResolver.query(mImageUri, null,
                MediaStore.Images.Media.MIME_TYPE + "=? or "
                        + MediaStore.Images.Media.MIME_TYPE + "=?",
                new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED);

        //jpeg & png & gif
//                Cursor mCursor = mContentResolver.query(mImageUri, null,
//                        MediaStore.Images.Media.MIME_TYPE + "=? or "
//                                + MediaStore.Images.Media.MIME_TYPE + "=? or "
//                                + MediaStore.Images.Media.MIME_TYPE + "=?",
//                        new String[]{"image/jpeg", "image/png", "image/gif"}, MediaStore.Images.Media.DATE_MODIFIED);

        if (mCursor == null) {
            return;
        }
        List<String> dirNames = new ArrayList<>();
        while (mCursor.moveToNext()) {
            // get image path
            String path = mCursor.getString(mCursor
                    .getColumnIndex(MediaStore.Images.Media.DATA));

            File file = new File(path);
            if (!file.exists()) {
                continue;
            }

            // get image parent name
            String parentName = new File(path).getParentFile().getName();
            Log.d(PickConfig.TAG, parentName + ":" + path);
            // save all Photo
            if (!mGroupMap.containsKey(PickConfig.ALL_PHOTOS)) {
                dirNames.add(PickConfig.ALL_PHOTOS);
                List<String> chileList = new ArrayList<>();
                chileList.add(path);
                mGroupMap.put(PickConfig.ALL_PHOTOS, chileList);
            } else {
                mGroupMap.get(PickConfig.ALL_PHOTOS).add(0, path);
            }
            // save by parent name
            if (!mGroupMap.containsKey(parentName)) {
                dirNames.add(parentName);
                List<String> chileList = new ArrayList<>();
                chileList.add(path);
                mGroupMap.put(parentName, chileList);
            } else {
                mGroupMap.get(parentName).add(0, path);
            }
        }
        mCursor.close();
        GroupImage groupImage = new GroupImage();
        groupImage.groupMap = mGroupMap;
        DirImage dirImage = new DirImage();
        dirImage.dirName = dirNames;
        PickPreferences.getInstance(mContext).saveImageList(groupImage);
        PickPreferences.getInstance(mContext).saveDirNames(dirImage);
        r.post(new Runnable() {
            @Override
            public void run() {
                RxBus.getInstance().send(new ImageLoadOkEvent());
            }
        });
    }
}
