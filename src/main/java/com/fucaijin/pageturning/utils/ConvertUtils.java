package com.fucaijin.pageturning.utils;

import android.content.Context;

/**
 * 转换工具类，例如px和sp、dp之间的转换等
 * Created by fucaijin on 2018/5/16.
 */

public class ConvertUtils {

    /**
     * dp转换成px
     */
    public static int dp2px(Context context, float dpValue){
        float scale=context.getResources().getDisplayMetrics().density;
        return (int)(dpValue*scale+0.5f);
    }

    /**
     * px转换成dp
     */
    public int px2dp(Context context,float pxValue){
        float scale=context.getResources().getDisplayMetrics().density;
        return (int)(pxValue/scale+0.5f);
    }

    /**
     * sp转换成px
     */
    public static int sp2px(Context context, float spValue){
        float fontScale=context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue*fontScale+0.5f);
    }

    /**
     * px转换成sp
     */
    public int px2sp(Context context,float pxValue){
        float fontScale=context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue/fontScale+0.5f);
    }
}
