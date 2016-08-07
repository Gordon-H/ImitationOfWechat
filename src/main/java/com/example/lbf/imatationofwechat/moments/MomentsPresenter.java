package com.example.lbf.imatationofwechat.moments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imatationofwechat.adapter.EmojiPagerAdapter;
import com.example.lbf.imatationofwechat.beans.CommentBean;
import com.example.lbf.imatationofwechat.beans.ContactBean;
import com.example.lbf.imatationofwechat.beans.MomentBean;
import com.example.lbf.imatationofwechat.common.CommonAdapter;
import com.example.lbf.imatationofwechat.common.CommonViewHolder;
import com.example.lbf.imatationofwechat.data.source.ChatsRepository;
import com.example.lbf.imatationofwechat.data.source.MomentsLoader;
import com.example.lbf.imatationofwechat.util.CommonUtil;
import com.example.lbf.imatationofwechat.util.DataUtil;
import com.example.lbf.imatationofwechat.views.CommentsTextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lbf on 2016/7/29.
 */
public class MomentsPresenter implements MomentsContract.Presenter, LoaderManager.LoaderCallbacks<List<MomentBean>> {
    private static final int LOAD_MOMENTS = 1;
    private Context mContext;
    private MomentsContract.View mView;
    private ChatsRepository mRepository;
    private LoaderManager mLoaderManager;

    private int inputMethodHeight;
    private int targetContactId;

    public MomentsPresenter(Context context, LoaderManager loaderManager,  MomentsContract.View view, ChatsRepository repository) {
        mContext = context;
        mLoaderManager = CommonUtil.checkNotNull(loaderManager);
        mView = CommonUtil.checkNotNull(view,"view cannot be null!");
        mRepository = CommonUtil.checkNotNull(repository);
        mView.setPresenter(this);
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(DataUtil.PREF_NAME, Context.MODE_PRIVATE);
        inputMethodHeight = sharedPreferences.getInt(DataUtil.PREF_KEY_INPUT_METHOD_HEIGHT, 0);
    }

    @Override
    public void start() {
        mView.setPanelHeight(inputMethodHeight);
        Bundle bundle = new Bundle();
        bundle.putInt("page",0);
        mLoaderManager.initLoader(LOAD_MOMENTS,bundle,this);
    }

    @Override
    public void loadData(int page) {
        Bundle bundle = new Bundle();
        bundle.putInt("page",page);
        mLoaderManager.restartLoader(LOAD_MOMENTS,bundle,this);
    }

    @Override
    public void addComment( CommentsTextView view) {
        String text = mView.getTextInputViewContent();
        if(TextUtils.isEmpty(text)){
            Toast.makeText(mContext,"评论内容不能为空！",Toast.LENGTH_SHORT).show();
            return;
        }
        ContactBean contactFrom = mRepository.getContactInfo(0);
        ContactBean contactTo = mRepository.getContactInfo(targetContactId);
        CommentBean commentBean = new CommentBean(text,contactFrom,contactTo);
        view.addComment(commentBean);
        mView.clearTextInputViewContent();
        mView.hideBottomBar();
        mView.hideEmojiPanel();
        mView.hideKeyboard();
    }

    @Override
    public void startComment(int targetContactId) {
        this.targetContactId = targetContactId;
        mView.showBottomBar();
        mView.showKeyboard();
    }

    @Override
    public void clickContact(int id) {
        CommonUtil.showNoImplementText(mContext);
    }

    @Override
    public void setEmojiPanelData() {
        List<RecyclerView> views = new ArrayList<>(5);
        for (int i = 0; i < 5; i++) {
            RecyclerView gridView = new RecyclerView(mContext);
            List<String> expressionList = new ArrayList<>();
            for (int j = i * 20; j < (i + 1) * 20; j++) {
                expressionList.add("smiley_" + j);
            }
            expressionList.add("emoji_delete");
            gridView.setLayoutManager(new GridLayoutManager(mContext, 7));
            gridView.setAdapter(new CommonAdapter<String>(mContext, expressionList, R.layout.chat_aty_emoji_grid_item) {
                @Override
                protected void convert(CommonViewHolder holder, final String s) {
                    Field field = null;
                    try {
                        field = R.drawable.class.getDeclaredField(s);
                        final int resourceId = Integer.parseInt(field.get(null).toString());
                        holder.setImageRes(R.id.iv_expression, resourceId)
                                .setOnClickLisener(R.id.iv_expression, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        addEmojiExpression(resourceId,s);
                                    }
                                });
                        ViewGroup root = (ViewGroup) holder.get(R.id.fl_chat_root);
                        ViewGroup.LayoutParams params = root.getLayoutParams();
                        params.height = inputMethodHeight/3;
                        root.setLayoutParams(params);
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            });
            views.add(gridView);
        }
        mView.setPanelAdapter(new EmojiPagerAdapter(views));
    }

    @Override
    public void addEmojiExpression(int resourceId, String s) {
        if(resourceId == R.drawable.emoji_delete){
            if(!mView.isTextInputViewEmpty()){
                deleteEmojiExpression();
            }
            return;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        int size = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,24,mContext.getResources().getDisplayMetrics());
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), resourceId,options);
        bitmap = Bitmap.createScaledBitmap(bitmap,size,size,true);
        //  根据Bitmap对象创建ImageSpan对象
        ImageSpan imageSpan = new ImageSpan(mContext, bitmap);
        //  创建一个SpannableString对象，以便插入用ImageSpan对象封装的图像
        SpannableString spannableString = new SpannableString("["+s+"]");
        //  用ImageSpan对象替换string
        spannableString.setSpan(imageSpan, 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mView.appendString(spannableString);
    }

    @Override
    public void deleteEmojiExpression() {
        String text = mView.getTextInputViewContent();
        int index2 = mView.getTextInputViewIndex();
        if(text.isEmpty()||index2 == 0){
            return;
        }
        int index1 = text.substring(0,index2-1).lastIndexOf("[");
        if(text.charAt(index2-1)!=']'||index1==-1){
            mView.deleteString(index2-1,index2);
        }else{
            mView.deleteString(index1,index2);
        }
    }

    @Override
    public void showOrHideEmojiPanel(boolean showPanel) {
        if (showPanel) {
            setInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
            mView.hideKeyboard();
            mView.showEmojiPanel();
        } else {
            mView.hideEmojiPanelDelayed();

        }
    }

    @Override
    public void setInputMode(int mode) {
        WindowManager.LayoutParams layoutParams = ((Activity)mContext).getWindow().getAttributes();
        if(layoutParams.softInputMode!=mode){
            layoutParams.softInputMode = mode;
            ((Activity)mContext).getWindow().setAttributes(layoutParams);
        }
    }

    @Override
    public Loader<List<MomentBean>> onCreateLoader(int id, Bundle args) {
        mView.showLoading();
        MomentsLoader loader = new MomentsLoader(mContext,mRepository);
        int page = args.getInt("page");
        loader.setPage(page);
        return loader;
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<List<MomentBean>> loader, List<MomentBean> data) {
        if(((MomentsLoader) loader).getPage() == 0){
            mView.setMomentList(data);
        }else{
            mView.addMomentList(data);
        }
        mView.hideLoading();
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<List<MomentBean>> loader) {

    }
}
