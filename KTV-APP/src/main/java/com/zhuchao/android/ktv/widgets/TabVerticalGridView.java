package com.zhuchao.android.ktv.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;
import androidx.leanback.widget.VerticalGridView;

import com.zhuchao.android.fbase.MMLog;
import com.zhuchao.android.ktv.R;

public class TabVerticalGridView extends VerticalGridView {
    private static final String TAG = "TabVerticalGridView";
    private Group mRegularTitleGroup;
    private View mActivityTitleView;
    private Animation mShakeY;
    private boolean isPressUp = false;
    private boolean isPressDown = false;

    public TabVerticalGridView(Context context) {
        this(context, null);
    }

    public TabVerticalGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabVerticalGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setActivityTitleView(View tabView) {
        this.mActivityTitleView = tabView;
    }

    public void setRegularTitleGroup(Group mGroup) {
        this.mRegularTitleGroup = mGroup;
    }

    public boolean isPressUp() {
        return isPressUp;
    }

    public boolean isPressDown() {
        return isPressDown;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            isPressDown = false;
            isPressUp = false;
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    isPressDown = true;
                    break;
                case KeyEvent.KEYCODE_DPAD_UP:
                    isPressUp = true;
                    if (getSelectedPosition() == 0) {
                        mActivityTitleView.requestFocus();
                        return true;
                    }
                    break;
                case KeyEvent.KEYCODE_BACK:
                    backToTop();
                    return true;
                default:
                    break;
            }
        }
        return super.dispatchKeyEvent(event) || executeKeyEvent(event);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        return super.onTouchEvent(e);
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
    }

    public boolean executeKeyEvent(@NonNull KeyEvent event) {
        boolean handled = false;
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    handled = arrowScroll(FOCUS_DOWN);
                    break;
                case KeyEvent.KEYCODE_DPAD_UP:
                    break;
            }
        }
        return handled;
    }

    public boolean arrowScroll(int direction) {
        MMLog.d(TAG, "arrowScroll direction: " + direction);

        View currentFocused = findFocus();
        if (currentFocused == this) {
            currentFocused = null;
        } else if (currentFocused != null) {
            boolean isChild = false;
            for (ViewParent parent = currentFocused.getParent(); parent instanceof ViewGroup;
                 parent = parent.getParent()) {
                if (parent == this) {
                    isChild = true;
                    break;
                }
            }
            if (!isChild) {
                // This would cause the focus search down below to fail in fun ways.
                final StringBuilder sb = new StringBuilder();
                sb.append(currentFocused.getClass().getSimpleName());
                for (ViewParent parent = currentFocused.getParent(); parent instanceof ViewGroup;
                     parent = parent.getParent()) {
                    sb.append(" => ").append(parent.getClass().getSimpleName());
                }
                currentFocused = null;
            }
        }

        boolean handled = false;

        View nextFocused = FocusFinder.getInstance().findNextFocus(this, currentFocused, direction);
        if (nextFocused == null || nextFocused == currentFocused) {
            if (direction == FOCUS_DOWN) {
                if (currentFocused != null && getScrollState() == SCROLL_STATE_IDLE) {
                    if (mShakeY == null) {
                        mShakeY = AnimationUtils.loadAnimation(getContext(), R.anim.host_shake_y);
                    }
                    currentFocused.clearAnimation();
                    currentFocused.startAnimation(mShakeY);

                }
                handled = true;
            }
        }
        return handled;
    }

    public void backToTop() {
        if (mActivityTitleView != null) {
            if (mRegularTitleGroup != null && mRegularTitleGroup.getVisibility() != View.VISIBLE) {
                mRegularTitleGroup.setVisibility(View.VISIBLE);
            }
            mActivityTitleView.requestFocus();
        }
        scrollToPosition(0);
    }
}
