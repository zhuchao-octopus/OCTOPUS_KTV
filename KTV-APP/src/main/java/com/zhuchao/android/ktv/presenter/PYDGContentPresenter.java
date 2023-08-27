package com.zhuchao.android.ktv.presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.leanback.widget.Presenter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zhuchao.android.ktv.R;
import com.zhuchao.android.ktv.bean.Content;
import com.zhuchao.android.ktv.bean.TypeSeven;
import com.zhuchao.android.ktv.keyboard.SkbContainer;
import com.zhuchao.android.ktv.utils.FontDisplayUtil;

public class PYDGContentPresenter extends Presenter {
    private Context mContext;
    private static final String TAG = "PYDGContentPresenter";

    @Override
    public Presenter.ViewHolder onCreateViewHolder(ViewGroup parent) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.pingying_diange_layout, parent, false);
        return new Type1ContentPresenter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {
        ///if (item instanceof Content.DataBean.WidgetsBean) {
            ///ViewHolder viewHolder1 = (ViewHolder) viewHolder;
        ///}
    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {

    }

    public static class ViewHolder extends Presenter.ViewHolder {
        private final SkbContainer skbContainer;

        public ViewHolder(View view) {
            super(view);
            skbContainer = view.findViewById(R.id.skbContainer);
        }
    }
}
