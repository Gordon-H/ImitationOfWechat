package com.example.lbf.imatationofwechat.chooseImage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imatationofwechat.util.ActivityUtil;

public class ChooseImageActivity extends AppCompatActivity {
    //    选择图片的模式，有单选和多选
    public static final String KEY_SELECT_MODE = "selectMode";
    public static final String KEY_ACTION = "action";
    public static final String KEY_POSITION = "position";
    public static final String KEY_CURRENT_FOLDER = "currentFolder";
    public static final String KEY_NAME_LIST = "nameList";
    public static final String KEY_SELECTED_LIST = "selectedList";
    //    单选模式
    public static final int MODE_SINGLE = 1;
    //    多选模式
    public static final int MODE_MULTIPLE = 2;
    //    intent附带的内容
    public static final String KEY_CONTENT = "Content";
    //    intent附带内容的类型，有文字和图片
    public static final String KEY_TYPE = "Type";

    public static final int CODE_TAKE_PHOTO = 1;
    public static final int CODE_VIEW_DETAIL = 2;
    public static int selectMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_image);
        initView();
        ChooseImageFragment fragment =
                (ChooseImageFragment) getSupportFragmentManager().findFragmentById(R.id.flContent);
        if (fragment == null) {
            // Create the fragment
            fragment = new ChooseImageFragment();
            ActivityUtil.addFragmentToActivity(
                    getSupportFragmentManager(), fragment, R.id.flContent);
        }

        ChooseImagePresenter presenter = new ChooseImagePresenter(this,getSupportLoaderManager(),fragment);
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        toolbar.setTitle("图片和视频");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        selectMode = intent.getIntExtra(KEY_SELECT_MODE,MODE_SINGLE);
    }

}
