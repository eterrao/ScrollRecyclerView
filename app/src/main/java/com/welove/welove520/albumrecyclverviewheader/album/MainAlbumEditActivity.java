package com.welove.welove520.albumrecyclverviewheader.album;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
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
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
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

public class MainAlbumEditActivity extends Activity implements MainAlbumEditContract.MainAlbumEditView {

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
    CompatRecyclerView rvPhotoList;
    @BindView(R.id.iv_album_edit_preview)
    PinchImageView ivAlbumEditPreview;
    @BindView(R.id.iv_full_screen_preview)
    ImageView ivFullScreenPreview;
    @BindView(R.id.layout_photo_preview)
    RelativeLayout rlAlbumHeader;

    private MainAlbumEditPresenterImpl mPresenter;

    private boolean isScaled = true;
    private boolean isTop;
    private GridLayoutManager gridLayoutManager;

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
        gridLayoutManager = new GridLayoutManager(this, 4);
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
        final int slop = ViewConfiguration.get(this).getScaledTouchSlop();
        rvPhotoList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                WeloveLog.debug(" getY = " + event.getY() + " , slop = " + slop);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_SCROLL:

                        break;
                }
                return false;
            }
        });
//        rvPhotoList.setMeasureHeight();
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

        ivFullScreenPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivAlbumEditPreview.reset();
            }
        });
        ivAlbumEditPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTop) {
                    ObjectAnimator downAnim = ObjectAnimator.ofFloat(rlAlbumHeader, "translationY", -rlAlbumHeader.getHeight() + rlAlbumHeader.getTop(), 0);
                    ObjectAnimator downAnim2 = ObjectAnimator.ofFloat(rvPhotoList, "translationY", -rlAlbumHeader.getHeight() + rlAlbumHeader.getTop(), 0);
                    downAnim.setDuration(400);
                    downAnim2.setDuration(400);
                    downAnim.setInterpolator(new DecelerateInterpolator());
                    downAnim2.setInterpolator(new DecelerateInterpolator());
                    AnimatorSet animatorSet = new AnimatorSet();
                    animatorSet.playTogether(downAnim, downAnim2);
                    animatorSet.start();
                    isTop = false;
                }
            }
        });
    }


    @Override
    public void setRVAdapter(final MainAlbumRVAdapter mainAlbumRVAdapter) {
        rvPhotoList.setAdapter(mainAlbumRVAdapter);
        rvPhotoList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int state) {
                super.onScrollStateChanged(recyclerView, state);
                WeloveLog.debug("scroll state ==> " + state);
                if (state == RecyclerView.SCROLL_STATE_IDLE) {
                    RecyclerView.LayoutManager layoutManager = rvPhotoList.getLayoutManager();
                    int firstVisibleItemPosition;
                    int lastVisibleItemPosition;
                    if (layoutManager instanceof GridLayoutManager) {
                        firstVisibleItemPosition = ((GridLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();
                        lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
                    } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                        int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                        ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
                        firstVisibleItemPosition = findMin(into);
                        lastVisibleItemPosition = findMax(into);
                    } else {
                        firstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();
                        lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
                    }
                    WeloveLog.debug(TAG, " onScrollStateChanged: firstVisibleItemPosition ==> " + firstVisibleItemPosition
                            + " ,lastVisibleItemPosition ==> " + lastVisibleItemPosition);
                    if (firstVisibleItemPosition == 0 && Math.abs(rvPhotoList.getTranslationY()) > 0) {
                        WeloveLog.debug("up up up rlAlbumHeader top = " + rlAlbumHeader.getTop() + " ,bottom = " + rlAlbumHeader.getBottom()
                                + " , height = " + rlAlbumHeader.getHeight() + " , measure height = " + rlAlbumHeader.getMeasuredHeight());
                        WeloveLog.debug("up up up rvPhotoList top = " + rvPhotoList.getTop() + " ,bottom = " + rvPhotoList.getBottom()
                                + " , height = " + rvPhotoList.getHeight() + " , measure height = " + rvPhotoList.getMeasuredHeight());
                        WeloveLog.debug("up up up  rvPhotoList x = " + rvPhotoList.getX() + " ,y = " + rvPhotoList.getY()
                                + " , getTranslationY() = " + rvPhotoList.getTranslationY() + " , measure getScrollY = " + rvPhotoList.getScrollY());
                        ObjectAnimator downAnim = ObjectAnimator.ofFloat(rlAlbumHeader, "translationY", -rlAlbumHeader.getHeight() + rlAlbumHeader.getTop(), 0);
                        ObjectAnimator downAnim2 = ObjectAnimator.ofFloat(rvPhotoList, "translationY", -rlAlbumHeader.getHeight() + rlAlbumHeader.getTop(), 0);
                        downAnim.setDuration(400);
                        downAnim2.setDuration(400);
                        downAnim.setInterpolator(new DecelerateInterpolator());
                        downAnim2.setInterpolator(new DecelerateInterpolator());
                        AnimatorSet animatorSet = new AnimatorSet();
                        animatorSet.playTogether(downAnim, downAnim2);
                        animatorSet.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                isTop = false;
                            }
                        });
                        animatorSet.start();
                    } else if (lastVisibleItemPosition >= rvPhotoList.getAdapter().getItemCount() - 1 && Math.abs(rvPhotoList.getTranslationY()) <= 0) {
                        WeloveLog.debug("down down down rlAlbumHeader top = " + rlAlbumHeader.getTop() + " ,bottom = " + rlAlbumHeader.getBottom()
                                + " , height = " + rlAlbumHeader.getHeight() + " , measure height = " + rlAlbumHeader.getMeasuredHeight());
                        WeloveLog.debug("down down down rvPhotoList top = " + rvPhotoList.getTop() + " ,bottom = " + rvPhotoList.getBottom()
                                + " , height = " + rvPhotoList.getHeight() + " , measure height = " + rvPhotoList.getMeasuredHeight());
                        WeloveLog.debug("down down down rvPhotoList x = " + rvPhotoList.getX() + " ,y = " + rvPhotoList.getY()
                                + " , getTranslationY() = " + rvPhotoList.getTranslationY() + " , measure getScrollY = " + rvPhotoList.getScrollY());
                        rvPhotoList.setMeasureHeight(DensityUtil.getScreenHeight(getApplicationContext()) - (rlAlbumHeader.getTop() * 2));
                        rvPhotoList.requestLayout();
                        ObjectAnimator upAnim = ObjectAnimator.ofFloat(rlAlbumHeader, "translationY", 0, -rlAlbumHeader.getHeight() + rlAlbumHeader.getTop());
                        ObjectAnimator upAnim2 = ObjectAnimator.ofFloat(rvPhotoList, "translationY", 0, -rlAlbumHeader.getHeight() + rlAlbumHeader.getTop());
//                        ObjectAnimator scaleAnim = ObjectAnimator.ofFloat(rvPhotoList, "scaleY", )
                        upAnim.setInterpolator(new DecelerateInterpolator());
                        upAnim2.setInterpolator(new DecelerateInterpolator());
                        upAnim.setDuration(400);
                        upAnim2.setDuration(400);
                        AnimatorSet animatorSet = new AnimatorSet();
                        animatorSet.playTogether(upAnim, upAnim2);
                        animatorSet.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
//                                WeloveLog.debug("onAnimationEnd down down down rlAlbumHeader top = " + rlAlbumHeader.getTop() + " ,bottom = " + rlAlbumHeader.getBottom()
//                                        + " , width = " + rlAlbumHeader.getWidth() + " , height = " + rlAlbumHeader.getHeight() + " , measure height = " + rlAlbumHeader.getMeasuredHeight());
//                                WeloveLog.debug("onAnimationEnd down down down rlAlbumHeader x = " + rlAlbumHeader.getX() + " ,y = " + rlAlbumHeader.getY()
//                                        + " , getTranslationY() = " + rlAlbumHeader.getTranslationY() + " , measure getScrollY = " + rlAlbumHeader.getScrollY());
//
//                                WeloveLog.debug("onAnimationEnd down down down rvPhotoList top = " + rvPhotoList.getTop() + " ,bottom = " + rvPhotoList.getBottom()
//                                        + " , height = " + rvPhotoList.getHeight() + " , measure height = " + rvPhotoList.getMeasuredHeight());
//                                WeloveLog.debug("onAnimationEnd down down down rvPhotoList x = " + rvPhotoList.getX() + " ,y = " + rvPhotoList.getY()
//                                        + " , getTranslationY() = " + rvPhotoList.getTranslationY() + " , measure getScrollY = " + rvPhotoList.getScrollY());
                                isTop = true;
//                                WeloveLog.debug("onAnimationEnd down down down rvPhotoList top = " + rvPhotoList.getTop() + " ,bottom = " + rvPhotoList.getBottom()
//                                        + " , height = " + rvPhotoList.getHeight() + " , measure height = " + rvPhotoList.getMeasuredHeight());
//                                WeloveLog.debug("onAnimationEnd down down down rvPhotoList x = " + rvPhotoList.getX() + " ,y = " + rvPhotoList.getY()
//                                        + " , getTranslationY() = " + rvPhotoList.getTranslationY() + " , measure getScrollY = " + rvPhotoList.getScrollY());
                            }
                        });
                        animatorSet.start();
                    }
                } else if (state == RecyclerView.SCROLL_STATE_DRAGGING) {
                    if (Math.abs(rvPhotoList.getTranslationY()) > 0) {

                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                WeloveLog.debug("on scrolled ==> dx = " + dx + " , dy = " + dy);
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
        } else {
            setBackground(photoPath);
            if (isTop) {
                ObjectAnimator downAnim = ObjectAnimator.ofFloat(rlAlbumHeader, "translationY", -rlAlbumHeader.getHeight() + rlAlbumHeader.getTop(), 0);
                ObjectAnimator downAnim2 = ObjectAnimator.ofFloat(rvPhotoList, "translationY", -rlAlbumHeader.getHeight() + rlAlbumHeader.getTop(), 0);
                downAnim.setDuration(400);
                downAnim2.setDuration(400);
                downAnim.setInterpolator(new DecelerateInterpolator());
                downAnim2.setInterpolator(new DecelerateInterpolator());
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(downAnim, downAnim2);
                animatorSet.start();
                isTop = false;
            }
        }
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
            if (ivAlbumEditPreview != null) {
                Glide.with(this).load(Uri.parse("file://" + path)).dontAnimate().into(ivAlbumEditPreview);
            }
        }
    }
}
