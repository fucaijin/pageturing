package com.fucaijin.pageturning.fragment;

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
import com.fucaijin.pageturning.utils.ConvertUtils;

/**
 * 这是要放入PagerView的Fragment页面
 * Created by fucaijin on 2018/5/17.
 */

public class PageFragment extends Fragment {
    private String contentText;
    int textSizeInt;
    String inputTextColor, inputBackGroundColor;

//    之前使用此构造方法不能打包，会保存，先注释，如果无误可以删除
//    public PageFragment(MainActivity mainActivity, String s, int textSizeInt, String inputTextColor, String inputBackGroundColor) {
//        super();
//        contentText = s;
//        mContext = mainActivity;
//        this.textSizeInt = textSizeInt;
//        this.inputTextColor = inputTextColor;
//        this.inputBackGroundColor = inputBackGroundColor;
//        this.textSizeInt = textSizeInt;
//    }

    public static PageFragment newInstance(String text, int textSizeInt, String inputTextColor, String inputBackGroundColor) {
        PageFragment newFragment = new PageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("content", text);
        bundle.putInt("textSizeInt", textSizeInt);
        bundle.putString("inputTextColor", inputTextColor);
        bundle.putString("inputBackGroundColor", inputBackGroundColor);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    /**
     * 在此方法获取上级(Activity)传过来的参数
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            contentText = args.getString("content");
            inputTextColor = args.getString("inputTextColor");
            inputBackGroundColor = args.getString("inputBackGroundColor");
            textSizeInt = args.getInt("textSizeInt");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FrameLayout rootLayout = (FrameLayout) inflater.inflate(R.layout.page_fragment_layout, container, false);
        TextView textView = rootLayout.findViewById(R.id.page_fragment_content_tv);
        contentText = contentText.replaceAll("nnn", "\n");
        textView.setText(contentText);
        textView.setTextSize(ConvertUtils.sp2px(getContext(), textSizeInt));
        textView.setTextColor(Color.parseColor("#FF" + inputTextColor));
        return rootLayout;
    }

}
