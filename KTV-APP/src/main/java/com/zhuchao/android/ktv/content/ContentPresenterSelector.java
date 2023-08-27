package com.zhuchao.android.ktv.content;

import androidx.leanback.widget.ListRow;

import com.zhuchao.android.ktv.base.BasePresenterSelector;
import com.zhuchao.android.ktv.bean.Footer;
import com.zhuchao.android.ktv.bean.TypeSeven;
import com.zhuchao.android.ktv.presenter.ImageRowHeaderPresenter;
import com.zhuchao.android.ktv.presenter.PYDGContentPresenter;
import com.zhuchao.android.ktv.presenter.Type5ContentPresenter;
import com.zhuchao.android.ktv.presenter.Type660ContentPresenter;
import com.zhuchao.android.ktv.presenter.TypeFooterPresenter;
import com.zhuchao.android.ktv.presenter.Type4ContentPresenter;
import com.zhuchao.android.ktv.presenter.Type1ContentPresenter;
import com.zhuchao.android.ktv.presenter.TypeSevenPresenter;
import com.zhuchao.android.ktv.presenter.Type6207ContentPresenter;
import com.zhuchao.android.ktv.presenter.Type3ContentPresenter;
import com.zhuchao.android.ktv.presenter.Type2ContentPresenter;
import com.zhuchao.android.ktv.presenter.row.ContentListRowPresenter;
import com.zhuchao.android.ktv.presenter.row.TypeFiveListRowPresenter;
import com.zhuchao.android.ktv.presenter.row.TypeZeroListRowPresenter;


public class ContentPresenterSelector extends BasePresenterSelector {
    public ContentPresenterSelector() {
        ContentListRowPresenter listRowPresenter = new ContentListRowPresenter();
        listRowPresenter.setShadowEnabled(false);
        listRowPresenter.setSelectEffectEnabled(false);
        listRowPresenter.setKeepChildForeground(false);

        TypeZeroListRowPresenter listRowPresenterOne = new TypeZeroListRowPresenter();
        listRowPresenterOne.setShadowEnabled(false);
        listRowPresenterOne.setSelectEffectEnabled(false);
        listRowPresenterOne.setKeepChildForeground(false);

        TypeFiveListRowPresenter listRowPresenterFive = new TypeFiveListRowPresenter();
        listRowPresenterFive.setShadowEnabled(false);
        listRowPresenterFive.setSelectEffectEnabled(false);
        listRowPresenterFive.setKeepChildForeground(false);
        listRowPresenterFive.setHeaderPresenter(new ImageRowHeaderPresenter());

        addClassPresenter(ListRow.class, listRowPresenterOne, Type1ContentPresenter.class);
        addClassPresenter(ListRow.class, listRowPresenterOne, Type2ContentPresenter.class);
        addClassPresenter(ListRow.class, listRowPresenterOne, Type3ContentPresenter.class);
        addClassPresenter(ListRow.class, listRowPresenterOne, Type4ContentPresenter.class);
        addClassPresenter(ListRow.class, listRowPresenterFive, Type5ContentPresenter.class);
        addClassPresenter(ListRow.class, listRowPresenterOne, Type660ContentPresenter.class);
        addClassPresenter(ListRow.class, listRowPresenterOne, Type6207ContentPresenter.class);
        addClassPresenter(Footer.class, new TypeFooterPresenter());

        addClassPresenter(TypeSeven.class, new TypeSevenPresenter());


        addClassPresenter(ListRow.class, listRowPresenterOne, PYDGContentPresenter.class);
    }

}
