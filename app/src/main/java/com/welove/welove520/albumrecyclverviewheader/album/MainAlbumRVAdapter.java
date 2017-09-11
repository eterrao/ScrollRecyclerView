package com.welove.welove520.albumrecyclverviewheader.album;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.welove.welove520.albumrecyclverviewheader.R;
import com.welove.welove520.albumrecyclverviewheader.utils.DirImage;
import com.welove.welove520.albumrecyclverviewheader.utils.GroupImage;
import com.welove.welove520.albumrecyclverviewheader.utils.PickConfig;
import com.welove.welove520.albumrecyclverviewheader.utils.PickPreferences;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Raomengyang on 17-9-6.
 * Email    : ericrao@welove-inc.com
 * Desc     :
 * Version  : 1.0
 */

public class MainAlbumRVAdapter extends RecyclerView.Adapter<MainAlbumRVAdapter.ViewHolder> implements MainAlbumEditActivity.Callback {

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_HEADER = 1;
    public static final int TYPE_CAMERA = 2;
    private static final String TAG = MainAlbumRVAdapter.class.getSimpleName();

    private final Context mContext;
    private final DirImage dirImage;
    private final GroupImage groupImage;

    private OnAlbumItemClickListener itemClickListener;

    private List<String> photoList;
    private View mHeaderView;

    private int slectedPosition;
    private RecyclerView mRecyclerView;

    public MainAlbumRVAdapter(Context context) {
        mContext = context;
        this.dirImage = PickPreferences.getInstance(context).getDirImage();
        this.groupImage = PickPreferences.getInstance(context).getListImage();
        if (groupImage != null && groupImage.getGroupMap() != null && groupImage.getGroupMap().size() > 0) {
            photoList = groupImage.getGroupMap().get(PickConfig.ALL_PHOTOS);
        }
    }

    public void setHeaderView(View headerView) {
        this.mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public boolean isHeader(int position) {
        return position < 1 && mHeaderView != null;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_album, null);
        if (mHeaderView != null && viewType == TYPE_HEADER) {
            return new ViewHolder(mHeaderView);
        }
        return new ViewHolder(view);
    }

    // TODO: 2017/9/6 封装 Glide 加载 URI 这种类型
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        setImageView(holder, position);
        setClickEvents(holder, position);
    }

    private void setClickEvents(final ViewHolder holder, final int position) {
        if (holder.ivPhotoPreview != null) {
            holder.ivPhotoPreview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WeloveLog.debug(TAG, "点击事件 Position ==> " + position);
                    int clickType = getItemViewType(position);
                    String photoPath = null;
                    if (clickType == TYPE_NORMAL && photoList != null && photoList.size() > 0) {
                        slectedPosition = position;
                        int currentPosition = position - 2;
                        if (currentPosition >= 0) {
                            photoPath = photoList.get(currentPosition);
                        }
                    }
                    itemClickListener.onClick(v, clickType, photoPath);
                    notifyDataSetChanged();
                }
            });
        }
    }

    /**
     * todo 规范化Glide
     *
     * @param holder
     * @param position
     */
    private void setImageView(ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER) {
            if (mHeaderView instanceof ImageView) {
                Glide.with(mContext).load(R.drawable.anni_bg_large_2).placeholder(R.drawable.anni_bg_large_2).dontAnimate().into((ImageView) mHeaderView);
            }
        } else if (getItemViewType(position) == TYPE_NORMAL) {
            if (slectedPosition >= 1 && position == slectedPosition) {
                holder.ivPhotoPreviewMask.setVisibility(View.VISIBLE);
            } else {
                holder.ivPhotoPreviewMask.setVisibility(View.GONE);
            }
            if (photoList != null && photoList.size() > 0) {
                Glide.with(mContext).load(Uri.parse("file://" + photoList.get(position - 1))).placeholder(R.drawable.ab_timeline_album_holder).dontAnimate().override(200, 200).into(holder.ivPhotoPreview);
            }
        } else {
            if (slectedPosition >= 2 && position == slectedPosition) {
                holder.ivPhotoPreviewMask.setVisibility(View.VISIBLE);
            } else {
                holder.ivPhotoPreviewMask.setVisibility(View.GONE);
            }
            Glide.with(mContext).load(R.drawable.bg_album_edit_camera_holder).placeholder(R.drawable.bg_album_edit_camera_holder).dontAnimate().override(200, 200).into(holder.ivPhotoPreview);
        }
    }

    @Override
    public int getItemCount() {
        return photoList != null ? photoList.size() : 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeaderView == null) {
            if (position == 0) {
                return TYPE_CAMERA;
            } else return TYPE_NORMAL;
        } else {
            if (position == 0) {
                return TYPE_HEADER;
            } else if (position == 1) {
                return TYPE_CAMERA;
            } else return TYPE_NORMAL;
        }
    }

    public OnAlbumItemClickListener getItemClickListener() {
        return itemClickListener;
    }

    public void setOnItemClickListener(OnAlbumItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    @Override
    public View findTargetView(float x, float y) {
        return mRecyclerView.findChildViewUnder(x, y);
    }

    public interface OnAlbumItemClickListener {
        void onClick(View view, int type, String photoPath);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_photo_preview)
        ImageView ivPhotoPreview;
        @BindView(R.id.iv_photo_preview_mask)
        ImageView ivPhotoPreviewMask;

        ViewHolder(View view) {
            super(view);
            if (view == mHeaderView) {
                return;
            }
            ButterKnife.bind(this, view);
        }
    }

    static class PhotoModel implements Serializable {

        private static final long serialVersionUID = -8357536996928570667L;

        private String path;
        private String tag;
        private boolean isChecked;

        public PhotoModel(String path, String tag, boolean isChecked) {
            this.path = path;
            this.tag = tag;
            this.isChecked = isChecked;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }
    }
}
