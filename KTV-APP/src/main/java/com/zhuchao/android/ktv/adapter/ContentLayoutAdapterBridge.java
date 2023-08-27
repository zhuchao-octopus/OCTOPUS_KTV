package com.zhuchao.android.ktv.adapter;

import android.content.Context;

import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRow;

import com.zhuchao.android.ktv.bean.Content;
import com.zhuchao.android.ktv.bean.TypeSeven;
import com.zhuchao.android.ktv.presenter.PYDGContentPresenter;
import com.zhuchao.android.ktv.presenter.Type1ContentPresenter;
import com.zhuchao.android.ktv.presenter.Type2ContentPresenter;
import com.zhuchao.android.ktv.presenter.Type3ContentPresenter;
import com.zhuchao.android.ktv.presenter.Type4ContentPresenter;
import com.zhuchao.android.ktv.presenter.Type5ContentPresenter;
import com.zhuchao.android.ktv.presenter.Type6207ContentPresenter;
import com.zhuchao.android.ktv.presenter.Type660ContentPresenter;
import com.zhuchao.android.ktv.utils.Constants;

import java.util.List;

public class ContentLayoutAdapterBridge {

    public static Object getLayoutAdapter(Content.DataBean dataBean) {
        List<Content.DataBean.WidgetsBean> widgets = null;
        Object Adapter = null;
        switch (dataBean.getContentCode()) {
            case Constants.TYPE_ONE:
                Adapter = new ArrayObjectAdapter(new Type1ContentPresenter());
                widgets = dataBean.getWidgets();
                if (widgets == null) return null;
                if (widgets.size() > 1) widgets = widgets.subList(0, 1);
                ((ArrayObjectAdapter) Adapter).addAll(0, widgets);
                break;
            case Constants.TYPE_TWO:
                Adapter = new ArrayObjectAdapter(new Type2ContentPresenter());
                widgets = dataBean.getWidgets();
                if (widgets == null) return null;
                if (widgets.size() > 2) widgets = widgets.subList(0, 2);
                ((ArrayObjectAdapter) Adapter).addAll(0, widgets);
                break;
            case Constants.TYPE_THREE:
                Adapter = new ArrayObjectAdapter(new Type3ContentPresenter());
                widgets = dataBean.getWidgets();
                if (widgets == null) return null;
                if (widgets.size() > 3) widgets = widgets.subList(0, 3);
                ((ArrayObjectAdapter) Adapter).addAll(0, widgets);
                break;
            case Constants.TYPE_FOUR:
                Adapter = new ArrayObjectAdapter(new Type4ContentPresenter());
                widgets = dataBean.getWidgets();
                if (widgets == null) return null;
                if (widgets.size() > 4) widgets = widgets.subList(0, 4);
                ((ArrayObjectAdapter) Adapter).addAll(0, widgets);
                break;
            case Constants.TYPE_FIVE:
                Adapter = new ArrayObjectAdapter(new Type5ContentPresenter());
                widgets = dataBean.getWidgets();
                if (widgets == null) return null;
                if (widgets.size() > 5) widgets = widgets.subList(0, 5);
                ((ArrayObjectAdapter) Adapter).addAll(0, widgets);
                break;
            case Constants.TYPE_SIX_PIC:
                Adapter = new ArrayObjectAdapter(new Type6207ContentPresenter());
                widgets = dataBean.getWidgets();
                if (widgets == null) return null;
                if (widgets.size() > 6) widgets = widgets.subList(0, 6);
                ((ArrayObjectAdapter) Adapter).addAll(0, widgets);
                break;
            case Constants.TYPE_SIX_BTN:
                Adapter = new ArrayObjectAdapter(new Type660ContentPresenter());
                widgets = dataBean.getWidgets();
                if (widgets == null) return null;
                if (widgets.size() > 6) widgets = widgets.subList(0, 6);
                ((ArrayObjectAdapter) Adapter).addAll(0, widgets);
                break;
            case Constants.TYPE_SEVEN:
                TypeSeven typeSeven = new TypeSeven();
                widgets = dataBean.getWidgets();
                if (widgets == null) return null;
                if (widgets.size() > 5) widgets = widgets.subList(0, 5);

                widgets.get(0).setBigPic(true);
                typeSeven.setWidgetsBeanList(widgets);
                Adapter = (Object) typeSeven;
                break;
            case Constants.TYPE_ZERO:
            case Constants.TYPE_EIGHT:
            case Constants.TYPE_NINE:
            case Constants.TYPE_TEN:
            case Constants.TYPE_ELEVEN:
                break;
            case Constants.TYPE_PYDG:
                Adapter = new ArrayObjectAdapter(new PYDGContentPresenter());
                widgets = dataBean.getWidgets();
                ///if (widgets == null) return null;
                ///if (widgets.size() > 6) widgets = widgets.subList(0, 6);
                ///((ArrayObjectAdapter) Adapter).addAll(0, widgets);
                break;
        }
        return Adapter;
    }


}
