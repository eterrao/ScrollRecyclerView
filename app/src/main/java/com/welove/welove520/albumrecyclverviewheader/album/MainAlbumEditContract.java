package com.welove.welove520.albumrecyclverviewheader.album;

import android.view.View;

import com.welove.welove520.albumrecyclverviewheader.base.BasePresenter;
import com.welove.welove520.albumrecyclverviewheader.base.BaseView;


/**
 * Created by Raomengyang on 17-9-6.
 * Email    : ericrao@welove-inc.com
 * Desc     :
 * Version  : 1.0
 */

public interface MainAlbumEditContract {

    interface MainAlbumEditView extends BaseView<Presenter> {

        void setRVAdapter(MainAlbumRVAdapter mainAlbumRVAdapter);

        void onItemClicked(View view, int type, String photoPath);
    }

    interface Presenter extends BasePresenter {

        void initPickHelper(View headerView);

        void initRVAdapter(View headerView);

        void setRVHeaderView(View view);

        MainAlbumRVAdapter getRVAdapter();
    }
}
