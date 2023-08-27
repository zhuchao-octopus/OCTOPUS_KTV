package com.zhuchao.android.ktv.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.FragmentManager;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.FocusHighlight;
import androidx.leanback.widget.FocusHighlightHelper;
import androidx.leanback.widget.HorizontalGridView;
import androidx.leanback.widget.ItemBridgeAdapter;
import androidx.leanback.widget.OnChildViewHolderSelectedListener;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.zhuchao.android.fbase.FileUtils;
import com.zhuchao.android.fbase.MMLog;
import com.zhuchao.android.ktv.R;
import com.zhuchao.android.ktv.adapter.ContentDataAdapterBridge;
import com.zhuchao.android.ktv.adapter.ContentViewPagerAdapter;
import com.zhuchao.android.ktv.base.BaseActivity;
import com.zhuchao.android.ktv.bean.Title;
import com.zhuchao.android.ktv.presenter.TitlePresenter;
import com.zhuchao.android.ktv.utils.Constants;
import com.zhuchao.android.ktv.utils.FontDisplayUtil;
import com.zhuchao.android.ktv.utils.LocalJsonResolutionUtil;
import com.zhuchao.android.ktv.widgets.ScaleTextView;

import java.lang.ref.WeakReference;
import java.util.List;

public class MainActivity extends BaseActivity implements ViewTreeObserver.OnGlobalFocusChangeListener, View.OnKeyListener, View.OnClickListener {
    private static final String TAG = "MainActivity";
    private static final int MSG_NOTIFY_TITLE = 100;
    private ViewPager2 mViewPager2;
    private ContentViewPagerAdapter mContentViewPagerAdapter;
    private Group mRegularTitleGroup;//all title,包括了mActivityTitleGridView
    private HorizontalGridView mActivityTitleGridView;
    private ArrayObjectAdapter mActivityTitleArrayAdapter;
    private int mCurrentTitlePageIndex = 0;
    private TextView mOldTitle;
    private ConstraintLayout mClSearch;
    private ConstraintLayout mClHistory;
    private ConstraintLayout mClLogin;
    private ConstraintLayout mClOpenVip;
    private ScaleTextView mTvAd;
    private ImageView mIvNetwork;

    public ArrayObjectAdapter getArrayObjectAdapter() {
        return mActivityTitleArrayAdapter;
    }

    public HorizontalGridView getHorizontalGridView() {
        return mActivityTitleGridView;
    }

    public Group getRegularTitleGroup() {
        return mRegularTitleGroup;
    }

    private MyEventHandler mMyEventHandler;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMyEventHandler = new MyEventHandler(this);
        initView();
        initListener();
        loadAndInitUIData();
    }

    @Override
    protected void onDestroy() {
        mMyEventHandler.removeCallbacksAndMessages(null);
        mActivityTitleGridView.removeOnChildViewHolderSelectedListener(onActivityTitleChildViewHolderSelectedListener);
        getWindow().getDecorView().getViewTreeObserver().removeOnGlobalFocusChangeListener(this);
        super.onDestroy();
        initDataThread.interrupt();
    }

    private void initView() {
        mActivityTitleGridView = findViewById(R.id.hg_title);
        mViewPager2 = findViewById(R.id.vp_content);
        mRegularTitleGroup = findViewById(R.id.id_group);
        mIvNetwork = findViewById(R.id.iv_network);
        mClSearch = findViewById(R.id.cl_search);
        mClHistory = findViewById(R.id.cl_history);
        mClLogin = findViewById(R.id.cl_login);
        mClOpenVip = findViewById(R.id.cl_ad_msg);
        mTvAd = findViewById(R.id.tv_ad);

        mActivityTitleGridView.setHorizontalSpacing(FontDisplayUtil.dip2px(this, 10));
        mActivityTitleArrayAdapter = new ArrayObjectAdapter(new TitlePresenter());
        ItemBridgeAdapter itemBridgeAdapter = new ItemBridgeAdapter(mActivityTitleArrayAdapter);
        mActivityTitleGridView.setAdapter(itemBridgeAdapter);
        FocusHighlightHelper.setupBrowseItemFocusHighlight(itemBridgeAdapter, FocusHighlight.ZOOM_FACTOR_MEDIUM, false);

        mViewPager2.setOffscreenPageLimit(3);  ///设置缓存数量
        //mViewPager2.setUserInputEnabled(false); ///禁止滑动
        mViewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);  ///设置滑动方向
        //ContentViewPagerAdapter viewPagerAdapter = new ContentViewPagerAdapter(this);
        mContentViewPagerAdapter = new ContentViewPagerAdapter(getSupportFragmentManager(), getLifecycle());
        mViewPager2.setAdapter(mContentViewPagerAdapter);
        mViewPager2.registerOnPageChangeCallback(onContentViewPager2PageChangeCallback);
    }

    private void initListener() {
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalFocusChangeListener(this);
        mActivityTitleGridView.addOnChildViewHolderSelectedListener(onActivityTitleChildViewHolderSelectedListener);
        mClSearch.setOnClickListener(this);
        mClHistory.setOnClickListener(this);
        mClLogin.setOnClickListener(this);
        mClOpenVip.setOnClickListener(this);
        mTvAd.setOnClickListener(this);

        mClSearch.setOnKeyListener(this);
        mClHistory.setOnKeyListener(this);
        mClLogin.setOnKeyListener(this);
        mClOpenVip.setOnKeyListener(this);
        mTvAd.setOnKeyListener(this);
    }

    private void initActivityTitle(List<Title.DataBean> titleDataBeans) {
        if (mActivityTitleArrayAdapter == null) return;
        mActivityTitleArrayAdapter.addAll(0, titleDataBeans);
        //initViewPager(titleDataBeans);
        mContentViewPagerAdapter.setData(titleDataBeans);

        if (mActivityTitleGridView != null && titleDataBeans.size() > Constants.TAG_FEATURE_POSITION) {
            View positionView = mActivityTitleGridView.getChildAt(Constants.TAG_FEATURE_POSITION);
            if (positionView != null) {
                mOldTitle = positionView.findViewById(R.id.tv_main_title);
            }
            mActivityTitleGridView.setSelectedPositionSmooth(Constants.TAG_FEATURE_POSITION);
        }
    }

    private void setViewPager2PageIndex(int position) {
        MMLog.log(TAG, "setViewPager2PageIndex to " + position + " mViewPager2.getCurrentItem()=" + mViewPager2.getCurrentItem());
        if (mViewPager2 != null && position != mViewPager2.getCurrentItem()) {
            mViewPager2.setCurrentItem(position);
        }
    }

    private void loadAndInitUIData() {
        initDataThread.start();
    }

    private final Thread initDataThread = new Thread(new Runnable() {
        @Override
        public void run() {
            String titleJson = ContentDataAdapterBridge.getActivityTitle(MainActivity.this);
            Title title = LocalJsonResolutionUtil.JsonToObject(titleJson, Title.class);
            if (title == null) return;
            List<Title.DataBean> dataBeans = title.getData();
            if (dataBeans != null && dataBeans.size() > 0) {
                Message msg = Message.obtain();
                msg.what = MSG_NOTIFY_TITLE;
                msg.obj = dataBeans;
                mMyEventHandler.sendMessage(msg);
            }
        }
    });

    private static class MyEventHandler extends Handler {
        private final WeakReference<MainActivity> mActivity;

        public MyEventHandler(MainActivity activity) {
            super(activity.getMainLooper());
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity activity = mActivity.get();
            if (activity == null) return;
            switch (msg.what) {
                case MSG_NOTIFY_TITLE:
                    activity.initActivityTitle(FileUtils.toType(msg.obj));
                    break;
                case 2:
                default:
                    break;
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///回调事件
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
            switch (v.getId()) {
                case R.id.cl_search:
                case R.id.cl_history:
                case R.id.cl_login:
                case R.id.cl_ad_msg:
                case R.id.tv_ad:
                    if (mActivityTitleGridView != null) {
                        mActivityTitleGridView.requestFocus();
                    }
                    return true;
                default:
                    break;
            }
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_UP:
                case KeyEvent.KEYCODE_DPAD_DOWN:
                case KeyEvent.KEYCODE_BACK:
                case KeyEvent.KEYCODE_DPAD_LEFT:
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                default:
                    break;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cl_search:
                startActivity(new Intent(this, AppInstalledActivity.class));
                Toast.makeText(this, "已安装应用", Toast.LENGTH_SHORT).show();
                break;
            case R.id.cl_history:
                Toast.makeText(this, "历史", Toast.LENGTH_SHORT).show();
                break;
            case R.id.cl_login:
                Toast.makeText(this, "登录", Toast.LENGTH_SHORT).show();
                break;
            case R.id.cl_ad_msg:
                Toast.makeText(this, "开通VIP", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_ad:
                Toast.makeText(this, "新人礼包", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //标题先择监听器
    private final OnChildViewHolderSelectedListener onActivityTitleChildViewHolderSelectedListener = new OnChildViewHolderSelectedListener() {
        @Override
        public void onChildViewHolderSelected(RecyclerView parent, RecyclerView.ViewHolder child, int position, int subposition) {
            super.onChildViewHolderSelected(parent, child, position, subposition);
            //MMLog.d(TAG, "onActivityTitleChildViewHolderSelected: mCurrentTitlePageIndex=" + mCurrentTitlePageIndex + " position=" + position + " subposition=" + subposition + " child=" + child.toString());
            if (position != mCurrentTitlePageIndex) {
                TextView currentTitle = child.itemView.findViewById(R.id.tv_main_title);
                MMLog.d(TAG, "onActivityTitleChildViewHolderSelected position=" + position);

                if (mOldTitle != null) {
                    //MMLog.d(TAG, "onChildViewHolderSelected: mOldTitle=" + mOldTitle.toString());

                    mOldTitle.setTextColor(getResources().getColor(R.color.colorWhite, getResources().newTheme()));
                    Paint paint = mOldTitle.getPaint();
                    if (paint != null) {
                        paint.setFakeBoldText(false);
                        //viewpager切页标题不刷新，调用invalidate刷新
                        mOldTitle.invalidate();
                    }
                }
                currentTitle.setTextColor(getResources().getColor(R.color.colorBlue));
                Paint paint = currentTitle.getPaint();
                if (paint != null) {
                    paint.setFakeBoldText(true);
                    //viewpager切页标题不刷新，调用invalidate刷新
                    currentTitle.invalidate();
                }
                mOldTitle = currentTitle;
                mCurrentTitlePageIndex = position;
                if (mViewPager2.getCurrentItem() != position)
                    setViewPager2PageIndex(position);
            }
        }
    };

    private final ViewPager2.OnPageChangeCallback onContentViewPager2PageChangeCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }

        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            MMLog.d(TAG, "ViewPager2.OnPageChangeCallback.onPageSelected position: " + position);
            if (position != mActivityTitleGridView.getSelectedPosition()) {
                mActivityTitleGridView.setSelectedPosition(position);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            super.onPageScrollStateChanged(state);
        }
    };

    @Override
    public void onGlobalFocusChanged(View oldFocus, View newFocus) {
        //MMLog.d(TAG, "onGlobalFocusChanged newFocus: " + newFocus);
        MMLog.d(TAG, "onGlobalFocusChanged oldFocus: " + oldFocus + " -> newFocus" + newFocus);
        if (newFocus == null || oldFocus == null) {
            return;
        }
        if (newFocus.getId() == R.id.tv_main_title
                && oldFocus.getId() == R.id.tv_main_title) {
            ((TextView) newFocus).setTextColor(getResources().getColor(R.color.colorWhite));
            ((TextView) newFocus).getPaint().setFakeBoldText(true);
            ((TextView) oldFocus).setTextColor(getResources().getColor(R.color.colorWhite));
            ((TextView) oldFocus).getPaint().setFakeBoldText(false);
        } else if (newFocus.getId() == R.id.tv_main_title
                && oldFocus.getId() != R.id.tv_main_title) {
            ((TextView) newFocus).setTextColor(getResources().getColor(R.color.colorWhite));
            ((TextView) newFocus).getPaint().setFakeBoldText(true);
        } else if (newFocus.getId() != R.id.tv_main_title
                && oldFocus.getId() == R.id.tv_main_title) {
            ((TextView) oldFocus).setTextColor(getResources().getColor(R.color.colorBlue));
            ((TextView) oldFocus).getPaint().setFakeBoldText(true);
        }
    }
}


