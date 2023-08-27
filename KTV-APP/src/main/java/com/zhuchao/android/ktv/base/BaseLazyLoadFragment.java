package com.zhuchao.android.ktv.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.zhuchao.android.fbase.MMLog;


public abstract class BaseLazyLoadFragment extends Fragment {
    private static final String TAG = "BaseLazyLoadFragment";
    protected boolean isViewInitiated;
    protected boolean isVisibleToUser;
    protected boolean isDataInitiated;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //MMLog.log(TAG, "onAttach()");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //MMLog.log(TAG, "onCreateView()");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isViewInitiated = true;
        //MMLog.log(TAG, "onViewCreated()");
        prepareFetchData(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        //MMLog.log(TAG, "onStart()");
    }

    @Override
    public void onResume() {
        super.onResume();
        isVisibleToUser = true;
        //MMLog.log(TAG, "onResume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        //MMLog.log(TAG, "onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        //MMLog.log(TAG, "onStop()");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isVisibleToUser = false;
        //MMLog.log(TAG, "onDestroyView()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //MMLog.log(TAG, "onDestroy()");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //MMLog.log(TAG, "onDetach()");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        MMLog.log(TAG, "onLowMemory()!!!!!!!!!!!!!!!!!");
    }

    private void prepareFetchData(boolean forceUpdate) {
        MMLog.log(TAG, "prepareFetchData() isVisibleToUser=" + isVisibleToUser + " isViewInitiated=" + isViewInitiated + " isDataInitiated=" + isDataInitiated);
        if (isViewInitiated && (!isDataInitiated || forceUpdate)) {
            fetchData();
            isDataInitiated = true;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        MMLog.log(TAG, "onHiddenChanged()hidden=" + hidden);
    }

    public abstract void fetchData();
}

