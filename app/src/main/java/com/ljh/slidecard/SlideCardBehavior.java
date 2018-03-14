package com.ljh.slidecard;

import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.view.View;

/**
 * Created by lijionghui on 2018/3/14.
 */
public class SlideCardBehavior extends CoordinatorLayout.Behavior<SlideCardLayout> {


    private int mInitOffset;

    @Override
    public boolean onMeasureChild(CoordinatorLayout parent, SlideCardLayout child,
                                  int parentWidthMeasureSpec, int widthUsed,
                                  int parentHeightMeasureSpec, int heightUsed) {
        //当前控件的高度等于父容器减去上面和下面的child头部的高度
        //offset上部和下部几个头部的高度
        int offet = getChildMeasureOffset(parent, child);
        int height = View.MeasureSpec.getSize(parentHeightMeasureSpec) - offet;
        //每一个卡片的高度是改变的
        child.measure(parentWidthMeasureSpec, View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY));
        return true;
    }

    private int getChildMeasureOffset(CoordinatorLayout parent, SlideCardLayout child) {
        int offset = 0;
        for (int i = 0; i < parent.getChildCount(); i++) {
            View view = parent.getChildAt(i);
            if (view != child && view instanceof SlideCardLayout) {
                offset += ((SlideCardLayout) view).getHeaderHeight();
            }
        }
        return offset;
    }


    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, SlideCardLayout child, int layoutDirection) {
        //摆放parent里所有子控件
        parent.onLayoutChild(child, layoutDirection);
        //给里面的child进行偏移
        //拿到上面的child,获取每一个child的头部相加
        SlideCardLayout pervious = getPreviouChild(parent, child);
        if (pervious != null) {
            int offset = pervious.getTop() + pervious.getHeaderHeight();
            child.offsetTopAndBottom(offset);
        }
        mInitOffset = child.getTop();
        return true;
    }

    private SlideCardLayout getPreviouChild(CoordinatorLayout parent, SlideCardLayout child) {
        int cardIndex = parent.indexOfChild(child);
        for (int i = cardIndex - 1; i >= 0; i--) {
            View view = parent.getChildAt(i);
            if (view instanceof SlideCardLayout)
                return (SlideCardLayout) view;
        }
        return null;
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                       @NonNull SlideCardLayout child, @NonNull View directTargetChild,
                                       @NonNull View target, int axes, int type) {
        if (coordinatorLayout.indexOfChild(child)==0)
            return false;
        boolean isVertical = (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
        return isVertical && child == directTargetChild;
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                               @NonNull SlideCardLayout child, @NonNull View target,
                               int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        int shift = scroll(child, dyUnconsumed, mInitOffset, mInitOffset + child.getHeight() - child.getHeaderHeight());
        //2.控制上边和下边child的移动
        shiftSlideings(shift, coordinatorLayout, child);
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull SlideCardLayout child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {

        consumed[1] = scroll(child, dy, mInitOffset, mInitOffset + child.getHeight() - child.getHeaderHeight());
        //2.控制上边和下边child的移动
        shiftSlideings(consumed[1], coordinatorLayout, child);
    }

    private void shiftSlideings(int shift, CoordinatorLayout parent, SlideCardLayout child) {
        if (shift == 0)
            return;
        if (shift > 0) {//往上推
            SlideCardLayout current = child;
            SlideCardLayout card = getPreviouChild(parent, current);
            while (card != null) {
                int offset = getHeaderOverlap(card, current);
                if (offset > 0)
                    card.offsetTopAndBottom(-offset);
                current = card;
                card = getPreviouChild(parent, current);
            }

        } else {//往下推 推动下面所有卡片 找到下面所有卡片
            int index = parent.indexOfChild(child);
            SlideCardLayout current = child;
            SlideCardLayout card = getNextChild(parent, current);
            while (card != null) {
                int offset = getHeaderOverlap(current, card);
                if (offset > 0)
                    card.offsetTopAndBottom(offset);
                current = card;
                card = getNextChild(parent, current);
            }

        }


    }

    private int getHeaderOverlap(SlideCardLayout above, SlideCardLayout below) {
        return above.getTop() + above.getHeaderHeight() - below.getTop();
    }

    private SlideCardLayout getNextChild(CoordinatorLayout parent, SlideCardLayout child) {
        int cardIndex = parent.indexOfChild(child);
        for (int i = cardIndex + 1; i < parent.getChildCount(); i++) {
            View view = parent.getChildAt(i);
            if (view instanceof SlideCardLayout)
                return (SlideCardLayout) view;
        }
        return null;
    }

    private int scroll(SlideCardLayout child, int dy, int minoffset, int maxoffset) {
        //1.控制自己的移动
        int mInitOffset = child.getTop();
        //dy:[min,max]
        int offset = clamp(mInitOffset - dy, minoffset, maxoffset) - mInitOffset;
        child.offsetTopAndBottom(offset);
        return -offset;//是往上还是下滑动（上正下负）
    }

    private int clamp(int i, int minoffset, int maxoffset) {
        if (i > maxoffset)
            return maxoffset;
        else if (i < minoffset)
            return minoffset;
        else
            return i;
    }
}
