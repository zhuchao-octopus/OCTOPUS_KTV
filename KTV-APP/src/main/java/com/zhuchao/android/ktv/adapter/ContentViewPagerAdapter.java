package com.zhuchao.android.ktv.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;

import com.zhuchao.android.fbase.MMLog;
import com.zhuchao.android.ktv.bean.Title;
import com.zhuchao.android.ktv.fragment.ContentFragment;

import java.util.List;


public class ContentViewPagerAdapter extends PagerFragmentStateAdapter {
    private static final String TAG = "ContentViewPagerAdapter";

    private List<Title.DataBean> dataBeans;

    public void setData(List<Title.DataBean> dataBeans) {
        this.dataBeans = dataBeans;
        Fragment fragment = null;
        //Fragment fragment = ContentFragment.newInstance(position, dataBeans.get(position).getTabCode());
        //    registeredFragments.put(position, fragment);
        for (int id = 0; id < dataBeans.size(); id++) {
            if (!this.containsItem(id)) {
                fragment = ContentFragment.newInstance(id, dataBeans.get(id).getTabCode());
                registeredFragments.put(id, fragment);
            }
        }
        MMLog.log(TAG, "setData getCount()=" + this.getItemCount() + " getItemChildrenCount()=" + getItemChildrenCount());
    }

    public List<Title.DataBean> getData() {
        return dataBeans;
    }

    public ContentViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public ContentViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        //MMLog.log(TAG, "createFragment() position=" + position);
        return registeredFragments.get(position);
    }

    public int getItemChildrenCount() {
        return dataBeans == null ? 0 : dataBeans.size();
    }
}
