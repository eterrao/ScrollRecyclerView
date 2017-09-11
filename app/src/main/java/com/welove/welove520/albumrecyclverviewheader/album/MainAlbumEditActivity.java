package com.welove.welove520.albumrecyclverviewheader.album;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.welove.welove520.albumrecyclverviewheader.R;
import com.welove.welove520.albumrecyclverviewheader.utils.PickConfig;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Raomengyang on 17-9-6.
 * Email    : ericrao@welove-inc.com
 * Desc     :
 * Version  : 1.0
 */

public class MainAlbumEditActivity extends Activity implements MainAlbumEditContract.MainAlbumEditView, GestureDetector.OnGestureListener {

    private static final int REQUEST_CODE_PICK_IMAGE = 0x001;
    private static final String TAG = MainAlbumEditActivity.class.getSimpleName();
    @BindView(R.id.iv_tool_bar_left_arrow)
    ImageView ivToolBarLeftArrow;
    @BindView(R.id.ll_tool_bar_album_change)
    LinearLayout llToolBarAlbumChange;
    @BindView(R.id.tv_toolbar_ok)
    TextView tvToolbarOk;
    @BindView(R.id.rl_tool_bar_album)
    RelativeLayout rlToolBarAlbum;
    @BindView(R.id.rv_photo_list)
    RecyclerView rvPhotoList;
    @BindView(R.id.iv_album_edit_preview)
    PinchImageView ivAlbumEditPreview;
    @BindView(R.id.iv_full_screen_preview)
    ImageView ivFullScreenPreview;

    private View mTargetView;
    private MainAlbumEditPresenterImpl mPresenter;
    private ViewHolder holder;
    private Callback mCallback;


    private GestureDetectorCompat gestureDetector;
    private boolean isScaled = true;

    @Override
    public void setPresenter(MainAlbumEditContract.Presenter presenter) {
        mPresenter = (MainAlbumEditPresenterImpl) presenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainAlbumEditPresenterImpl.newInstance(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.fragment_main_album);
        ButterKnife.bind(this);
        gestureDetector = new GestureDetectorCompat(this, this);
        initView();
    }


    private void initView() {
        initRecyclerView();
        initPhotoView();
        initClickEvents();
    }

    private void initPhotoView() {
    }

    private void initRecyclerView() {
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
//        View headerView = LayoutInflater.from(this).inflate(R.layout.layout_item_album_header, null);
//        holder = new ViewHolder(headerView);
//        headerView.setTag("flag");
//        photoView.setOnPhotoTapListener(mPhotoTapListener);
//        photoView.setOnLongClickListener(mLongClickListener);

        mPresenter.initPickHelper(ivAlbumEditPreview);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (mPresenter.getRVAdapter() != null) {
                    return mPresenter.getRVAdapter().isHeader(position) ? gridLayoutManager.getSpanCount() : 1;
                } else return gridLayoutManager.getSpanCount();
            }
        });
        rvPhotoList.setLayoutManager(gridLayoutManager);
    }

    private void initClickEvents() {
        ivToolBarLeftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 17-9-7 规范化
//                ((MainCoverActivity) getActivity()).setCurrentItem(1);
            }
        });

        llToolBarAlbumChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    @Override
    public void setRVAdapter(MainAlbumRVAdapter mainAlbumRVAdapter) {
        rvPhotoList.setAdapter(mainAlbumRVAdapter);
        mCallback = mainAlbumRVAdapter;
        rvPhotoList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int state) {
                super.onScrollStateChanged(recyclerView, state);
                if (state == RecyclerView.SCROLL_STATE_IDLE) {
                    RecyclerView.LayoutManager layoutManager = rvPhotoList.getLayoutManager();
                    int firstVisibleItemPosition;
                    int lastVisibleItemPosition;
                    if (layoutManager instanceof GridLayoutManager) {
                        firstVisibleItemPosition = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
                        lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                    } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                        int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                        ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
                        firstVisibleItemPosition = findMin(into);
                        lastVisibleItemPosition = findMax(into);
                    } else {
                        firstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                        lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                    }
                    WeloveLog.debug(TAG, "onScrollStateChanged: firstVisibleItemPosition==> " + firstVisibleItemPosition
                            + " ,lastVisibleItemPosition ==> " + lastVisibleItemPosition);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    private int findMin(int[] firstPositions) {
        int min = firstPositions[0];
        for (int value : firstPositions) {
            if (value < min) {
                min = value;
            }
        }
        return min;
    }

    @Override
    public void onItemClicked(View view, int type, String photoPath) {
        if (type == MainAlbumRVAdapter.TYPE_HEADER) {
        } else if (type == MainAlbumRVAdapter.TYPE_CAMERA) {
//            if (PermissionManager.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)) {
//                takePhoto();
//            } else
//                requestPermissions(new String[]{Manifest.permission.CAMERA}, PermissionUtil.CAMERA_PERMISSION_REQUEST_CODE);
        } else setBackground(photoPath);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                List<String> paths = (List<String>) data.getSerializableExtra(PickConfig.INTENT_IMG_LIST_SELECT);
                String path = (paths != null) ? paths.get(0) : null;
                setBackground(path);
            }
        }
    }

    private void setBackground(String path) {
        if (!TextUtils.isEmpty(path)) {
            WeloveLog.debug("photoPath is " + path);
            if (holder != null && holder.ivAlbumEditPreview != null) {
                Glide.with(this).load(Uri.parse("file://" + path)).dontAnimate().into(holder.ivAlbumEditPreview);
            }
        }
    }

    private void takePhoto() {
//        ScreenLockTemporarySettings.getInstance().setDisableScreenLock(true);
//        PickData pickData = new PickData();
//        pickData.setMode(PickConfig.MODE_TAKE_PHOTO);
//        pickData.setCrop(true);
//        pickData.setCropMode(CropImageView.CropMode.RATIO_9_16.getId());
//
//        Intent intent = new Intent();
//        intent.setClass(getActivity(), PickCameraPhotoActivity.class);
//        intent.putExtra(PickConfig.INTENT_PICK_DATA, pickData);
//        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
//        getActivity().overridePendingTransition(R.anim.activity_transition_in_from_bottom, R.anim.activity_transition_none);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    static class ViewHolder {
        @BindView(R.id.iv_album_edit_preview)
        PinchImageView ivAlbumEditPreview;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


    public interface Callback {
        View findTargetView(float x, float y);
    }

}
