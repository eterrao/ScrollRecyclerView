package com.welove.welove520.albumrecyclverviewheader.album;

import android.content.Context;

/**
 * Created by LightHuangfu
 * User: Seraph
 * Date: 12-9-12
 * Time: 上午11:22
 */
public class DensityUtil {
    private static final int DEFAULT_WIDTH_PIXELS = 480; // 480 * 800 的默认宽


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 根据手机的分辨率 按比例缩放设计图中的view的大小
     *
     * @param pxValue 480*800 设计图中对应的长度,单位:像素
     * @return 按比例缩放后得到的值, 单位:像素
     */
    public static int scaleSize(Context context, float pxValue) {
        float currentWidthPix = context.getResources().getDisplayMetrics().widthPixels;
        return (int) (pxValue * DEFAULT_WIDTH_PIXELS / currentWidthPix);
    }
}
