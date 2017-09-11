package com.welove.welove520.albumrecyclverviewheader.album;

import android.view.View;


import com.welove.welove520.albumrecyclverviewheader.utils.PickConfig;
import com.welove.welove520.albumrecyclverviewheader.utils.PickPhotoHelper;
import com.welove.welove520.albumrecyclverviewheader.utils.PickPreferences;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Raomengyang on 17-9-6.
 * Email    : ericrao@welove-inc.com
 * Desc     :
 * Version  : 1.0
 */

public class MainAlbumEditPresenterImpl implements MainAlbumEditContract.Presenter {

    private MainAlbumEditContract.MainAlbumEditView albumEditView;
    private MainAlbumRVAdapter mainAlbumRVAdapter;
    private List<AlbumModel> photoList;
    private PickPreferences pickPreferences;

    public MainAlbumEditPresenterImpl(MainAlbumEditContract.MainAlbumEditView view) {
        this.albumEditView = view;
        albumEditView.setPresenter(this);
    }

    public static MainAlbumEditPresenterImpl newInstance(MainAlbumEditContract.MainAlbumEditView view) {
        return new MainAlbumEditPresenterImpl(view);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }

    /**
     * #initPickHelper() 必须放在 #initRVAdapter() 前，先对PickHelper进行初始化
     *
     * @param headerView
     */
    @Override
    public void initPickHelper(final View headerView) {
        pickPreferences = PickPreferences.getInstance(headerView.getContext());
        PickPhotoHelper helper = new PickPhotoHelper(headerView.getContext());
        pickPreferences.saveCurrentDirName(PickConfig.ALL_PHOTOS);
        Observable.just(helper)
                .map(new Func1<PickPhotoHelper, Object>() {
                    @Override
                    public Object call(PickPhotoHelper pickPhotoHelper) {
                        pickPhotoHelper.getImagesAsync();
                        return null;
                    }
                }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        initRVAdapter(headerView);
                    }
                });
    }

    @Override
    public void initRVAdapter(View headerView) {
        mainAlbumRVAdapter = new MainAlbumRVAdapter(headerView.getContext());
        mainAlbumRVAdapter.setHasStableIds(true);
        mainAlbumRVAdapter.setOnItemClickListener(new MainAlbumRVAdapter.OnAlbumItemClickListener() {
            @Override
            public void onClick(View view, int type, String photoPath) {
                albumEditView.onItemClicked(view, type, photoPath);
            }
        });
        albumEditView.setRVAdapter(mainAlbumRVAdapter);
    }

    @Override
    public void setRVHeaderView(View view) {
    }

    @Override
    public MainAlbumRVAdapter getRVAdapter() {
        return mainAlbumRVAdapter;
    }

}
