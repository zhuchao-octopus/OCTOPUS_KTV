package com.zhuchao.android.ktv.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.leanback.widget.Presenter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zhuchao.android.fbase.MMLog;
import com.zhuchao.android.ktv.R;
import com.zhuchao.android.ktv.bean.Content;
import com.zhuchao.android.ktv.utils.FontDisplayUtil;


public class Type660ContentPresenter extends Presenter {
    private Context mContext;

    private static final String TAG = "TypeFourContentPresenter";

    @Override
    public Presenter.ViewHolder onCreateViewHolder(ViewGroup parent) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_type_660_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {
        if (item instanceof Content.DataBean.WidgetsBean) {
            Type660ContentPresenter.ViewHolder vh = (Type660ContentPresenter.ViewHolder) viewHolder;
            MMLog.d(TAG, "onBindViewHolder: " + ((Content.DataBean.WidgetsBean) item).getUrl());
            Glide.with(mContext)
                    .load(((Content.DataBean.WidgetsBean) item).getUrl())
                    .apply(new RequestOptions()
                            .override(FontDisplayUtil.dip2px(mContext, 124),
                                    FontDisplayUtil.dip2px(mContext, 207))
                            .placeholder(R.drawable.bg_shape_default))
                    .into(vh.mIvPoster);
            String desc = ((Content.DataBean.WidgetsBean) item).getDesc();
            if (!TextUtils.isEmpty(desc)) {
                vh.mTvDesc.setText(desc);
            }
        }
    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {

    }

    public static class ViewHolder extends Presenter.ViewHolder {
        private final TextView mTvDesc;
        private final ImageView mIvPoster;
        public ViewHolder(View view) {
            super(view);
            mTvDesc = view.findViewById(R.id.tv_desc);
            mIvPoster = view.findViewById(R.id.iv_poster);
        }
    }
}

