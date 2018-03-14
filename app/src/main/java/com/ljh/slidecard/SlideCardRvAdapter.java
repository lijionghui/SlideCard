package com.ljh.slidecard;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lijionghui on 2018/3/14.
 */

public class SlideCardRvAdapter extends RecyclerView.Adapter<SlideCardRvAdapter.ViewHolder> {

    List<String> mList = new ArrayList<>();
    private Context mContext;

    public SlideCardRvAdapter(Context mContext) {
        this.mContext = mContext;
        mList.add("1");
        mList.add("2");
        mList.add("3");
        mList.add("4");
        mList.add("5");
        mList.add("6");
        mList.add("7");
        mList.add("8");
        mList.add("9");
        mList.add("10");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                mContext).inflate(R.layout.item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv.setText(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv;

        public ViewHolder(View itemView) {
            super(itemView);
            tv =  itemView.findViewById(R.id.tv);

        }
    }
}
