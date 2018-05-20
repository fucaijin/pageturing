package com.fucaijin.pageturning.activity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.fucaijin.pageturning.R;
import com.fucaijin.pageturning.adpter.PagerFragmentAdapter;
import com.fucaijin.pageturning.fragment.PageFragment;
import com.fucaijin.pageturning.utils.ConvertUtils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
//    TODO 有空添加ViewPager切换效果的选项，选择不同的页面切换效果，例如：淡入淡出等
    private ImageView ivMoreSettingButton;
    private LinearLayout llMoreSettingRoot;
    private boolean isMoreSettingOpened = false;
    private EditText etContent;
    private EditText etSettingTime;
    private EditText etSettingTextSize;
    private String mContentStr;
    private int timeInt = 0;
    private int textSizeInt = 0;
    private LinearLayout llSettingPageRoot;
    private ImageView viPageTextColor;
    private ImageView viPageBackGroundColor;
    private EditText etSettingTextColor;
    private EditText etSettingPagerBackGroundColor;
    private SharedPreferences config;
    private String lastInputContent;
    private int lastInputTime;
    private String lastInputTextColor;
    private String lastInputBackgroundColor;
    private boolean isPlayOverFinishApp;
    private int lastInputTextSize;
    private CheckBox cbPlayOverFinishApp;
    private String mTimeStr;
    private String mTextSizeStr;
    private String mInputTextColorStr;
    private String mInputBackGroundColorStr;
    private ViewPager mViewPager;
    private FrameLayout mMainActivityRootLayout;
    private RadioGroup rgPlayMode;
    private RadioButton rbModeManual;
    private RadioButton rbModeAuto;
    private boolean isAutoPlay;
    private static boolean isPlaying; //记录当前是否正在播放

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        设置为充满全屏模式
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        getConfig();
        initUI();
    }

    /**
     * 获得存储在手机中的配置信息
     */
    private void getConfig() {
        config = getSharedPreferences("config", MODE_PRIVATE);
        lastInputContent = config.getString("content", "");
        lastInputTime = config.getInt("time", 15);
        lastInputTextSize = config.getInt("text_size", 10);
        lastInputTextColor = config.getString("text_color", "333333");
        lastInputBackgroundColor = config.getString("background_color", "DDDDDD");
        isPlayOverFinishApp = config.getBoolean("play_over_finish_app", false);
        isAutoPlay = config.getBoolean("isAutoPlay", true);
    }

    private void initUI() {
        mMainActivityRootLayout = (FrameLayout) findViewById(R.id.fl_main_activity_root);
        llSettingPageRoot = (LinearLayout) findViewById(R.id.ll_setting_page_root);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        rgPlayMode = (RadioGroup) findViewById(R.id.rg_play_mode);
        rgPlayMode.setOnCheckedChangeListener(this);
        rbModeAuto =  (RadioButton) findViewById(R.id.rb_mode_auto);
        rbModeManual =  (RadioButton) findViewById(R.id.rb_mode_manual);

        etContent = (EditText) findViewById(R.id.et_content);                                           //正文输入框
        etSettingTime = (EditText) findViewById(R.id.et_setting_time);                                  //时间输入框
        etSettingTextSize = (EditText) findViewById(R.id.et_setting_text_size);                         //字体大小输入框

        viPageTextColor  = (ImageView) findViewById(R.id.iv_page_text_color);                           //字体颜色预览块
        viPageBackGroundColor  = (ImageView) findViewById(R.id.iv_page_background_color);              //背景颜色预览块
        etSettingTextColor = (EditText) findViewById(R.id.et_setting_text_color);                       //字体颜色输入框 xml布局文件中已经限制其输入位数
        etSettingPagerBackGroundColor = (EditText) findViewById(R.id.et_setting_pager_background_color);//背景颜色输入框 xml布局文件中已经限制其输入位数
        Button btSurePageTextSize = (Button) findViewById(R.id.bt_sure_page_text_size);            //重置默认设置按钮
        Button btSurePageTextColor = (Button) findViewById(R.id.bt_sure_page_text_color);               //确认文字颜色按钮
        Button btSurePageBackGroundColor = (Button) findViewById(R.id.bt_sure_page_background_color);      //确认背景颜色按钮
        Button btResetDefaultSetting = (Button) findViewById(R.id.bt_reset_default_setting);            //重置默认设置按钮
        cbPlayOverFinishApp = (CheckBox) findViewById(R.id.cb_play_over_finish_app);

        btSurePageTextSize.setOnClickListener(this);
        btSurePageTextColor.setOnClickListener(this);
        btSurePageBackGroundColor.setOnClickListener(this);
        btResetDefaultSetting.setOnClickListener(this);

        ivMoreSettingButton = (ImageView) findViewById(R.id.iv_more_setting_button);
        llMoreSettingRoot = (LinearLayout) findViewById(R.id.ll_more_setting_root);
        Button btStartPlay = (Button) findViewById(R.id.bt_star_play);

        ivMoreSettingButton.setOnClickListener(this);
        btStartPlay.setOnClickListener(this);

        recoveryData();
        recoveryUI();
    }

    /**
     * 根据配置信息恢复界面UI
     */
    private void recoveryUI() {
        String inputTextColor = etSettingTextColor.getText().toString().trim();
        viPageTextColor.setColorFilter(Color.parseColor("#FF" + inputTextColor));
        etContent.setTextColor(Color.parseColor("#FF" + inputTextColor));

        int inputTextSize = Integer.parseInt(etSettingTextSize.getText().toString().trim());
        etContent.setTextSize(ConvertUtils.sp2px(this,inputTextSize));

        String inputBackGroundColor = etSettingPagerBackGroundColor.getText().toString().trim();
        viPageBackGroundColor.setColorFilter(Color.parseColor("#FF"+ inputBackGroundColor));
        mMainActivityRootLayout.setBackgroundColor(Color.parseColor("#FF" + inputBackGroundColor));
    }

    /**
     * 将配置文件保存的信息显示回界面上
     */
    private void recoveryData() {
        etContent.setText(lastInputContent);
        etSettingTime.setText(String.valueOf(lastInputTime));
        etSettingTextSize.setText(String.valueOf(lastInputTextSize));
        etSettingTextColor.setText(lastInputTextColor);
        etSettingPagerBackGroundColor.setText(lastInputBackgroundColor);
        cbPlayOverFinishApp.setChecked(isPlayOverFinishApp);
        viPageTextColor.setColorFilter(Color.parseColor("#FF" + lastInputTextColor));
        viPageBackGroundColor.setColorFilter(Color.parseColor("#FF" + lastInputBackgroundColor));

        if(isAutoPlay){
            rbModeAuto.setChecked(true);
            rbModeManual.setChecked(false);
        }else {
            rbModeAuto.setChecked(false);
            rbModeManual.setChecked(true);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_more_setting_button:
//                如果当前没打开更多设置，则更换图标并展开；如果打开了，则反之
                openMoreSetting();
                break;
            case R.id.bt_star_play:
                getDatas();
                startPlay();
                break;
            case R.id.bt_sure_page_text_size:
                int inputTextSize = Integer.parseInt(etSettingTextSize.getText().toString().trim());
                etContent.setTextSize(ConvertUtils.sp2px(this,inputTextSize));
                break;
            case R.id.bt_sure_page_text_color:
                String inputTextColor = etSettingTextColor.getText().toString().trim();
                viPageTextColor.setColorFilter(Color.parseColor("#FF" + inputTextColor));
                etContent.setTextColor(Color.parseColor("#FF" + inputTextColor));
                break;
            case R.id.bt_sure_page_background_color:
                String inputBackGroundColor = etSettingPagerBackGroundColor.getText().toString().trim();
                viPageBackGroundColor.setColorFilter(Color.parseColor("#FF"+ inputBackGroundColor));
                mMainActivityRootLayout.setBackgroundColor(Color.parseColor("#FF" + inputBackGroundColor));
                break;
            case R.id.bt_reset_default_setting:
                resetSetting();
                break;

        }
    }

    /**
     * 重置所有设置的方法
     */
    private void resetSetting() {
        etContent.setText("");
        etSettingTime.setText(R.string.default_play_time);
        etSettingTextSize.setText(R.string.default_play_text_size);
        etSettingTextColor.setText(R.string.default_play_text_color);
        etSettingPagerBackGroundColor.setText(R.string.default_play_back_ground_color);
        cbPlayOverFinishApp.setChecked(false);
        viPageTextColor.setColorFilter(Color.parseColor(getString(R.string.default_play_text_color_complete)));
        viPageBackGroundColor.setColorFilter(Color.parseColor(getString(R.string.default_play_back_ground_color_complete)));
        rbModeAuto.setChecked(true);
        rbModeManual.setChecked(false);
        recoveryUI();
    }

    /**
     * 获取所有输入框的信息
     */
    private void getDatas() {
        mContentStr = etContent.getText().toString().trim();
        mTimeStr = etSettingTime.getText().toString().trim();
        mTextSizeStr = etSettingTextSize.getText().toString().trim();
        mInputTextColorStr = etSettingTextColor.getText().toString().trim();
        mInputBackGroundColorStr = etSettingPagerBackGroundColor.getText().toString().trim();
        isPlayOverFinishApp = cbPlayOverFinishApp.isChecked();

        if(rbModeAuto.isChecked()){
            isAutoPlay = true;
        }else if(rbModeManual.isChecked()){
            isAutoPlay = false;
        }
    }

    /**
     * 对信息的正确性进行相应的判断，如果没有错误就正常播放
     */
    private void startPlay() {
        final boolean checked = isPlayOverFinishApp;

        viPageBackGroundColor.setColorFilter(Color.parseColor("#FF"+ mInputBackGroundColorStr));
        mMainActivityRootLayout.setBackgroundColor(Color.parseColor("#FF" + mInputBackGroundColorStr));

//        如果当前所有输入框都不为空，两个颜色都是6位数，且时间和字体不为0
        if(!mContentStr.isEmpty() && !mTimeStr.isEmpty() &&
                !mTextSizeStr.isEmpty() &&
                mInputTextColorStr.length() == 6 &&
                mInputBackGroundColorStr.length() == 6 ){

            timeInt = Integer.parseInt(mTimeStr);
            textSizeInt = Integer.parseInt(mTextSizeStr);

            if(timeInt !=0 && textSizeInt !=0){
//                将设置保存至配置文件
                saveConfigToXml(checked);

                llSettingPageRoot.setVisibility(View.GONE);
                final ViewPager viewPager = mViewPager;
                viewPager.setVisibility(View.VISIBLE);

                String[] split = mContentStr.split("\n");//正文数据都在这里，通过分割换行，获取一个数组
                final int pageSize = split.length;//有多少个换行就是多少行
                final ArrayList contentLengthArr = new ArrayList();//用来记录每一行字的字数
                ArrayList<PageFragment> FragmentList = new ArrayList<>();
                final float contentAllLength = mContentStr.length() + 0.0f;
                for (String text : split) {
                    PageFragment pageFragment = PageFragment.newInstance(text, textSizeInt, mInputTextColorStr, mInputBackGroundColorStr);
                    FragmentList.add(pageFragment);
                    contentLengthArr.add(text.length());
                }
//                ☆ 注意 ：FragmentPagerAdapter必须要继承FragmentStatePagerAdapter才可实现实时修改删除Fragment
                PagerFragmentAdapter mPagerFragmentAdapter = new PagerFragmentAdapter(getSupportFragmentManager(), FragmentList);
                viewPager.setAdapter(mPagerFragmentAdapter);
//                在子线程中根据输入行数和时间来进行延时执行更新任务

                if(isAutoPlay){
//                    自动播放
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            MainActivity.isPlaying = true;
//                        循环定时设置页面位置
                            for (int i = 0;i < pageSize;i++){
                                final int finalI = i;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        viewPager.setCurrentItem(finalI);
                                    }
                                });
                                float charTimes = timeInt / contentAllLength;//根据总事件/总字数，算出每一个字需要多少时间
                                int currentContentLength = (int) contentLengthArr.get(i); //根据当前页面的字数，来乘以时间，算出当前页面需要停留的时间
                                float cuttentPageSleepTime = charTimes * currentContentLength;
                                try {
                                    Thread.sleep((long) (1000 * cuttentPageSleepTime));
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
//                        翻页执行完后，判断是否要关闭APP还是返回设置界面
                            if(checked){
                                finish();
                            }else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        llSettingPageRoot.setVisibility(View.VISIBLE);
                                        viewPager.setVisibility(View.GONE);
                                        MainActivity.isPlaying = false;
                                    }
                                });
                            }
                        }
                    }).start();
                }else {
                    isPlaying = true;
//                    手动播放
                    llSettingPageRoot.setVisibility(View.GONE);
                    mViewPager.setVisibility(View.VISIBLE);
                }
            }else {
                Toast.makeText(this,"时间或字体大小不能为0",Toast.LENGTH_SHORT).show();
            }
        }else if(mContentStr.isEmpty()){
            Toast.makeText(this,"请输入正文",Toast.LENGTH_SHORT).show();
        }else if(mTimeStr.isEmpty()){
            Toast.makeText(this,"请输入时间",Toast.LENGTH_SHORT).show();
        }else if (mTextSizeStr.isEmpty()){
            Toast.makeText(this,"请设置文字大小",Toast.LENGTH_SHORT).show();
        }else if(mInputTextColorStr.length() < 6 ){
            Toast.makeText(this,"字体颜色字数不够6位",Toast.LENGTH_SHORT).show();
        }else if(mInputBackGroundColorStr.length() < 6){
            Toast.makeText(this,"背景颜色字数不够6位",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 保存当前设置信息到xml文件中
     * @param checked 是否播放完就关闭App
     */
    private void saveConfigToXml(boolean checked) {
        SharedPreferences.Editor edit = config.edit();
        edit.putString("content", mContentStr);
        edit.putInt("time",timeInt);
        edit.putInt("text_size",textSizeInt);
        edit.putString("text_color", mInputTextColorStr);
        edit.putString("background_color", mInputBackGroundColorStr);
        edit.putBoolean("play_over_finish_app",checked);
        edit.putBoolean("isAutoPlay",isAutoPlay);
        edit.apply();
    }

    /**
     * 如果当前没打开更多设置，则更换图标并展开；如果打开了，则反之
     */
    private void openMoreSetting() {
        if(!isMoreSettingOpened){
            ivMoreSettingButton.setImageResource(R.drawable.positive_triangle_pressed);
            llMoreSettingRoot.setVisibility(View.VISIBLE);
            isMoreSettingOpened = true;
        }else {
            ivMoreSettingButton.setImageResource(R.drawable.inverted_triangle_normal);
            llMoreSettingRoot.setVisibility(View.GONE);
            isMoreSettingOpened = false;
        }
    }

    /**
     * 播放模式的单选按钮的点击事件监听
     */
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (i == rbModeAuto.getId()){
            isAutoPlay = true;
        }else if(i == rbModeManual.getId()){
            isAutoPlay = false;
        }
    }

    /**
     * 返回按钮事件的监听
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(isPlaying){
                llSettingPageRoot.setVisibility(View.VISIBLE);
                mViewPager.setVisibility(View.GONE);
                isPlaying = false;
            }else {
                return super.onKeyDown(keyCode, event);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
