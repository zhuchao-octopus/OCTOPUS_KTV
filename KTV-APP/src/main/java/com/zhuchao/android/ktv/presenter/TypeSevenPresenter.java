package com.zhuchao.android.ktv.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.BaseGridView;
import androidx.leanback.widget.HorizontalGridView;
import androidx.leanback.widget.Presenter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zhuchao.android.ktv.R;
import com.zhuchao.android.ktv.bean.Content;
import com.zhuchao.android.ktv.bean.TypeSeven;
import com.zhuchao.android.ktv.utils.FontDisplayUtil;
import com.zhuchao.android.ktv.widgets.focus.MyItemBridgeAdapter;

import java.util.List;


public class TypeSevenPresenter extends Presenter {

    private Context mContext;


    @Override
    public Presenter.ViewHolder onCreateViewHolder(ViewGroup parent) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.type_seven_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {
        if (item instanceof TypeSeven) {
            ViewHolder vh = (ViewHolder) viewHolder;
            vh.updatePresenter(((TypeSeven) item).getWidgetsBeanList());
        }

    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {

    }

    public static class ViewHolder extends Presenter.ViewHolder {

        private final HorizontalGridView mHorizontalGridView;
        private GridLayoutManager mGridLayoutManager;

        @SuppressLint("RestrictedApi")
        public ViewHolder(View view) {
            super(view);
            mHorizontalGridView = view.findViewById(R.id.hg_seven);
            mHorizontalGridView.setNumRows(2);
            mGridLayoutManager = new GridLayoutManager(view.getContext(), 2, GridLayoutManager.HORIZONTAL, false);
            mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return position == 0 ? 2 : 1;
                }
            });
            mHorizontalGridView.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(@NonNull Rect outRect,
                                           @NonNull View view,
                                           @NonNull RecyclerView parent,
                                           @NonNull RecyclerView.State state) {
                    super.getItemOffsets(outRect, view, parent, state);
                    RecyclerView.Adapter adapter = parent.getAdapter();
                    if (adapter == null) {
                        return;
                    }
                    int childCount = adapter.getItemCount();
                    int childAdapterPosition = parent.getChildAdapterPosition(view);
                    if (childAdapterPosition == 0) {
                        outRect.set(0,
                                0,
                                FontDisplayUtil.dip2px(view.getContext(), 24),
                                0);
                    } else if (childAdapterPosition == childCount - 1) {
                        outRect.set(0,
                                FontDisplayUtil.dip2px(view.getContext(), 12),
                                0,
                                0);
                    } else if (childAdapterPosition == childCount - 2) {
                        outRect.set(0,
                                0, 0,
                                FontDisplayUtil.dip2px(view.getContext(), 12));
                    } else {
                        if (childAdapterPosition % 2 == 0) {
                            outRect.set(0,
                                    FontDisplayUtil.dip2px(view.getContext(), 12),
                                    FontDisplayUtil.dip2px(view.getContext(), 24),
                                    0);
                        } else {
                            outRect.set(0,
                                    0,
                                    FontDisplayUtil.dip2px(view.getContext(), 24),
                                    FontDisplayUtil.dip2px(view.getContext(), 12));
                        }
                    }
                }
            });
            mHorizontalGridView.setFocusScrollStrategy(BaseGridView.FOCUS_SCROLL_ITEM);
            mHorizontalGridView.setLayoutManager(mGridLayoutManager);

        }

        private void updatePresenter(List<Content.DataBean.WidgetsBean> widgetsBeanList) {
            ArrayObjectAdapter mArrayObjectAdapter = new ArrayObjectAdapter(new Type7ContentPresenter());
            MyItemBridgeAdapter myItemBridgeAdapter = new MyItemBridgeAdapter(mArrayObjectAdapter) {
                @Override
                public OnItemViewClickedListener getOnItemViewClickedListener() {
                    return new OnItemViewClickedListener() {
                        @Override
                        public void onItemClicked(View focusView,
                                                  Presenter.ViewHolder itemViewHolder,
                                                  Object item) {

                        }
                    };
                }
            };
            mHorizontalGridView.setAdapter(myItemBridgeAdapter);
            mArrayObjectAdapter.addAll(0, widgetsBeanList);
        }
    }
}
