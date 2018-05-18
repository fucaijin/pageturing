package com.fucaijin.pageturning.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.fucaijin.pageturning.R;
import com.fucaijin.pageturning.activity.MainActivity;
import com.fucaijin.pageturning.utils.ConvertUtils;

/**
 * 这是要放入PagerView的Fragment页面
 * Created by fucaijin on 2018/5/17.
 */

public class PageFragment extends Fragment {

    private final String contentText;
    private Context mContext;
    int textSizeInt;
    String inputTextColor, inputBackGroundColor;
    public PageFragment(MainActivity mainActivity, String s, int textSizeInt, String inputTextColor, String inputBackGroundColor) {
        super();
        contentText = s;
        mContext = mainActivity;
        this.textSizeInt =textSizeInt;
        this.inputTextColor = inputTextColor;
        this.inputBackGroundColor = inputBackGroundColor;
        this.textSizeInt = textSizeInt;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FrameLayout rootLayout = (FrameLayout) inflater.inflate(R.layout.page_fragment_layout, container, false);
        TextView textView = rootLayout.findViewById(R.id.page_fragment_content_tv);
        textView.setText(contentText);
        textView.setTextSize(ConvertUtils.sp2px(mContext,textSizeInt));
        textView.setTextColor(Color.parseColor("#FF" + inputTextColor));

        return rootLayout;
    }

}
