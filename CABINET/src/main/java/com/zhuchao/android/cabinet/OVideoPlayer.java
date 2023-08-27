package com.zhuchao.android.cabinet;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

public class OVideoPlayer extends StandardGSYVideoPlayer {
    public OVideoPlayer(Context context, Boolean fullFlag) {
        super(context, fullFlag);
        hideFullScreenButton();
    }

    public OVideoPlayer(Context context) {
        super(context);
        hideFullScreenButton();
    }

    public OVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        hideFullScreenButton();
    }

    private void hideFullScreenButton() {
        mTopContainer.findViewById(com.shuyu.gsyvideoplayer.R.id.back).setVisibility(View.GONE);
        mTopContainer.findViewById(com.shuyu.gsyvideoplayer.R.id.title).setVisibility(View.GONE);
        getFullscreenButton().setVisibility(View.GONE);
    }
}
