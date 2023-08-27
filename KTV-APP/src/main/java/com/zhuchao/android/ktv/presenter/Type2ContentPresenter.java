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
import com.zhuchao.android.ktv.utils.FontDisplayUtil;


public class Type2ContentPresenter extends Presenter {
    private Context mContext;

    private static final String TAG = "TypeZeroContentPresenter";

    @Override
    public Presenter.ViewHolder onCreateViewHolder(ViewGroup parent) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_type_0_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {
        if (item instanceof Content.DataBean.WidgetsBean) {
            ViewHolder vh = (ViewHolder) viewHolder;
            Glide.with(mContext)
                    .load(((Content.DataBean.WidgetsBean) item).getUrl())
                    .apply(new RequestOptions()
                            .centerCrop()
                            .override(FontDisplayUtil.dip2px(mContext, 420),
                                    FontDisplayUtil.dip2px(mContext, 207))
                            .placeholder(R.drawable.bg_shape_default))
                    .into(vh.mIvTypeOnePoster);
        }
    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {

    }

    public static class ViewHolder extends Presenter.ViewHolder {

        private final ImageView mIvTypeOnePoster;

        public ViewHolder(View view) {
            super(view);
            mIvTypeOnePoster = view.findViewById(R.id.iv_type_one_poster);
        }
    }
}
