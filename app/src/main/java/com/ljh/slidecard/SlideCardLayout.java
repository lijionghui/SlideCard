package com.ljh.slidecard;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by lijionghui on 2018/3/13.
 */
@CoordinatorLayout.DefaultBehavior(SlideCardBehavior.class)
public class SlideCardLayout extends FrameLayout {
    private int mHeaderHeight;

    public SlideCardLayout(@NonNull Context context) {
        super(context);
    }

    public SlideCardLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);

    }

    public int getHeaderHeight() {
        return mHeaderHeight;
    }

    public SlideCardLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.widget_card,this);
        RecyclerView list = findViewById(R.id.list);
        TextView header = findViewById(R.id.header);
        SlideCardRvAdapter adapter = new SlideCardRvAdapter(context);
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(getContext()));

        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.SlideCardLayout);
        header.setBackgroundColor(a.getColor(R.styleable.SlideCardLayout_colorbackground,0));
        header.setText(a.getText(R.styleable.SlideCardLayout_text));
        a.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w!=oldw||h!=oldh){
            mHeaderHeight = findViewById(R.id.header).getMeasuredHeight();
        }
    }
}
