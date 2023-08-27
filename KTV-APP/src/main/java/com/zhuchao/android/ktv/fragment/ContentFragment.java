package com.zhuchao.android.ktv.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ItemBridgeAdapter;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.OnChildViewHolderSelectedListener;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.zhuchao.android.fbase.MMLog;
import com.zhuchao.android.ktv.R;
import com.zhuchao.android.ktv.activity.MainActivity;
import com.zhuchao.android.ktv.adapter.ContentDataAdapterBridge;
import com.zhuchao.android.ktv.adapter.ContentLayoutAdapterBridge;
import com.zhuchao.android.ktv.base.BaseLazyLoadFragment;
import com.zhuchao.android.ktv.bean.Content;
import com.zhuchao.android.ktv.bean.Footer;
import com.zhuchao.android.ktv.content.ContentPresenterSelector;
import com.zhuchao.android.ktv.utils.Constants;
import com.zhuchao.android.ktv.utils.FontDisplayUtil;
import com.zhuchao.android.ktv.utils.LocalJsonResolutionUtil;
import com.zhuchao.android.ktv.widgets.TabVerticalGridView;

import java.util.List;


public class ContentFragment extends BaseLazyLoadFragment {
    private static final String TAG = "ContentFragment";
    private static final String BUNDLE_KEY_POSITION = "bundleKeyPosition";
    private static final String BUNDLE_KEY_TAB_CODE = "bundleKeyTabCode";
    private static final String MSG_BUNDLE_KEY_ADD_ITEM = "msgBundleKeyItem";
    private static final int MSG_ADD_ITEM = 100;
    private static final int MSG_REMOVE_LOADING = 101;
    private TabVerticalGridView mVerticalGridView;
    private MainActivity mActivity;
    private View mRootView;
    private View mFirstView;
    private Handler mHandler;
    private ProgressBar mPbLoading;
    private ArrayObjectAdapter mAdapter;
    //private int mCurrentTabPosition;
    private String mCurrentTabCode;
    private ContentFragment.OnFragmentInteractionListener onFragmentInteractionListener;

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public static ContentFragment newInstance(int position, String tabCode) {
        MMLog.d(TAG, "newInstance: " + position + " tabCode:" + tabCode);
        ContentFragment fragment = new ContentFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_KEY_POSITION, position);
        bundle.putString(BUNDLE_KEY_TAB_CODE, tabCode);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ContentFragment.OnFragmentInteractionListener) {
            onFragmentInteractionListener = (ContentFragment.OnFragmentInteractionListener) context;
        }
        ///else {
        ///throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        ///}
        mActivity = (MainActivity) context;
        mHandler = new MyHandler(mActivity.getMainLooper());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle == null) {
            MMLog.d(TAG, "onCreate: bundle=null");
            return;
        }
        int mCurrentTabPosition = getArguments().getInt(BUNDLE_KEY_POSITION);
        mCurrentTabCode = getArguments().getString(BUNDLE_KEY_TAB_CODE);
        MMLog.d(TAG, "onCreate() position:" + mCurrentTabPosition + " tabCode: " + mCurrentTabCode);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            //MMLog.d(TAG, "onCreateView()");
            mRootView = inflater.inflate(R.layout.fragment_content, container, false);
            initView();
            initListener();
        }
        return mRootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //MMLog.d(TAG, "onDetach()");
        onFragmentInteractionListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fetchDataThread.interrupt();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        if (mVerticalGridView != null) {
            mVerticalGridView.removeOnScrollListener(onScrollListener);
            mVerticalGridView.removeOnChildViewHolderSelectedListener(onSelectedListener);
        }
    }

    private void initView() {
        mPbLoading = mRootView.findViewById(R.id.pb_loading);
        mVerticalGridView = mRootView.findViewById(R.id.hg_content);
        mVerticalGridView.setActivityTitleView(mActivity.getHorizontalGridView());
        mVerticalGridView.setRegularTitleGroup(mActivity.getRegularTitleGroup());
        mVerticalGridView.setVerticalSpacing(FontDisplayUtil.dip2px(mActivity, 24));
        ContentPresenterSelector presenterSelector = new ContentPresenterSelector();
        mAdapter = new ArrayObjectAdapter(presenterSelector);
        ItemBridgeAdapter itemBridgeAdapter = new ItemBridgeAdapter(mAdapter);
        mVerticalGridView.setAdapter(itemBridgeAdapter);
    }

    private void initListener() {
        mVerticalGridView.addOnScrollListener(onScrollListener);
        mVerticalGridView.addOnChildViewHolderSelectedListener(onSelectedListener);
    }

    @Override
    public void fetchData() {
        mPbLoading.setVisibility(View.VISIBLE);
        mVerticalGridView.setVisibility(View.INVISIBLE);
        MMLog.log(TAG, "start fetchData() " + mCurrentTabCode);
        fetchDataThread.start();
    }

    private final Thread fetchDataThread = new Thread(new Runnable() {
        @Override
        public void run() {
            String json = null;
            if (mCurrentTabCode == null) {
                mHandler.sendEmptyMessage(MSG_REMOVE_LOADING);
                return;
            }

            FragmentActivity activity = getActivity();
            if (activity != null) {
                json = ContentDataAdapterBridge.getDataForTabCode(activity, mCurrentTabCode);
            }

            if (json != null) {
                Content content = LocalJsonResolutionUtil.JsonToObject(json, Content.class);
                final Message msg = Message.obtain();
                msg.what = MSG_ADD_ITEM;
                Bundle b = new Bundle();
                b.putParcelable(MSG_BUNDLE_KEY_ADD_ITEM, content);
                msg.setData(b);
                //延迟1秒模拟加载数据过程
                mHandler.sendMessageDelayed(msg, 1000);
            }
        }
    });

    @SuppressLint("HandlerLeak")
    private class MyHandler extends Handler {
        public MyHandler(@NonNull Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_ADD_ITEM:
                    Content content = msg.getData().getParcelable(MSG_BUNDLE_KEY_ADD_ITEM);
                    if (content == null) {
                        break;
                    }
                    List<Content.DataBean> dataBeans = content.getData();
                    for (int i = 0; i < dataBeans.size(); i++) {
                        Content.DataBean dataBean = dataBeans.get(i);
                        addItem(dataBean);
                    }
                    addFooter();
                    mPbLoading.setVisibility(View.GONE);
                    mVerticalGridView.setVisibility(View.VISIBLE);
                    break;
                case MSG_REMOVE_LOADING:
                    mPbLoading.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }
    }

    private void addItem(Content.DataBean dataBean) {
        List<Content.DataBean.WidgetsBean> Widgets = null;
        Object object = ContentLayoutAdapterBridge.getLayoutAdapter(dataBean);
        HeaderItem headerItem = null;
        if (dataBean.getShowTitle())
            headerItem = new HeaderItem(dataBean.getTitle());

        switch (dataBean.getContentCode()) {
            case Constants.TYPE_ONE:
            case Constants.TYPE_TWO:
            case Constants.TYPE_THREE:
            case Constants.TYPE_FOUR:
            case Constants.TYPE_FIVE:
            case Constants.TYPE_SIX_PIC:
            case Constants.TYPE_SIX_BTN:
                if (headerItem != null)
                    addWithTryCatch(new ListRow(headerItem, (ArrayObjectAdapter) object));
                else
                    addWithTryCatch(new ListRow((ArrayObjectAdapter) object));
                break;
            case Constants.TYPE_SEVEN:
            case Constants.TYPE_PYDG:
                addWithTryCatch(object);
                break;
            case Constants.TYPE_ZERO:
            case Constants.TYPE_EIGHT:
            case Constants.TYPE_NINE:
            case Constants.TYPE_TEN:
            case Constants.TYPE_ELEVEN:
                break;
        }
    }

    private void addFooter() {
        addWithTryCatch(new Footer());
    }

    private void addWithTryCatch(Object item) {
        try {
            if (!mVerticalGridView.isComputingLayout()) {
                mAdapter.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void scrollToTop() {
        if (mVerticalGridView != null) {
            mVerticalGridView.scrollToPosition(0);
            if (mActivity.getRegularTitleGroup() != null && mActivity.getRegularTitleGroup().getVisibility() != View.VISIBLE) {
                mActivity.getRegularTitleGroup().setVisibility(View.VISIBLE);
            }
        }
    }

    public static boolean isVisibleLocal(View target) {
        Rect rect = new Rect();
        target.getLocalVisibleRect(rect);
        MMLog.log(TAG, "rect:" + rect.top);
        return rect.top == 0;
    }

    private void handleTitleVisible(boolean isShow) {
        if (isShow) {
            if (mActivity.getRegularTitleGroup().getVisibility() != View.VISIBLE) {
                mActivity.getRegularTitleGroup().setVisibility(View.VISIBLE);
            }
        } else {
            if (mActivity.getRegularTitleGroup().getVisibility() != View.GONE) {
                mActivity.getRegularTitleGroup().setVisibility(View.GONE);
            }
        }
    }

    private final RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            //MMLog.log(TAG, "onScrollStateChanged newState=" + newState);
            switch (newState) {
                //当屏幕滚动且用户使用的触碰或手指还在屏幕上，停止加载图片
                case RecyclerView.SCROLL_STATE_DRAGGING:
                    //由于用户的操作，屏幕产生惯性滑动，停止加载图片
                case RecyclerView.SCROLL_STATE_SETTLING:
                    Glide.with(mActivity).pauseRequests();
                    break;
                case RecyclerView.SCROLL_STATE_IDLE:
                    Glide.with(mActivity).resumeRequests();
            }
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            ///MMLog.log(TAG,"recyclerView dx="+dx+" dy="+dy);
            ///mFirstView = mVerticalGridView.getChildAt(0);
            ///MMLog.log(TAG,"mVerticalGridView.getChildCount()="+ Objects.requireNonNull(mVerticalGridView.getAdapter()).getItemCount());
        }
    };

    private final OnChildViewHolderSelectedListener onSelectedListener = new OnChildViewHolderSelectedListener() {
        @Override
        public void onChildViewHolderSelected(RecyclerView parent, RecyclerView.ViewHolder child, int position, int subposition) {
            super.onChildViewHolderSelected(parent, child, position, subposition);
            ///MMLog.d(TAG, "mVerticalGridView.onChildViewHolderSelected: " + position );
            ///MMLog.d(TAG, "mVerticalGridView.onChildViewHolderSelected position: " + position + " isPressUp:" + mVerticalGridView.isPressUp() + " isPressDown:" + mVerticalGridView.isPressDown());
        }
    };


}
