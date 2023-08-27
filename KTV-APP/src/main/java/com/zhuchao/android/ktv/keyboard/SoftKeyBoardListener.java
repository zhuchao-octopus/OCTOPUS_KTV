package com.zhuchao.android.ktv.keyboard;

public interface SoftKeyBoardListener {
    public void onCommitText(SoftKey key);

    public void onDelete(SoftKey key);

    public void onBack(SoftKey key);
}
