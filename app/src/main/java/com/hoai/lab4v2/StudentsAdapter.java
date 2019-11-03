package com.hoai.lab4v2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StudentsAdapter extends RecyclerView.Adapter<StudentsAdapter.NewsViewHolder> implements Filterable {

    Context mContext;
    List<StudentsItem> mData = new ArrayList<>();
    List<StudentsItem> mDataFiltered;
    ItemClickListener itemClickListener;


    public StudentsAdapter(Context mContext, List<StudentsItem> mData) {
        this.mContext = mContext;
        this.mData = mData;
        this.mDataFiltered = mData;
    }
    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View layout;
        layout = LayoutInflater.from(mContext).inflate(R.layout.layout_item,viewGroup,false);
        return new NewsViewHolder(layout);
    }


    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder newsViewHolder, final int position) {
        StudentsItem studentsItem= mData.get(position);
        final StudentsItem newsItem = mData.get(position);
        newsViewHolder.container.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.item_animation_from_right));
        newsViewHolder.tv_title.setText(mDataFiltered.get(position).getTitle());
        newsViewHolder.tv_content.setText(mDataFiltered.get(position).getContent());
        newsViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.OnItemClick(position,newsItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataFiltered.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                String Key = constraint.toString();
                if (Key.isEmpty()) {

                    mDataFiltered = mData ;

                }
                else {
                    List<StudentsItem> lstFiltered = new ArrayList<>();
                    for (StudentsItem row : mData) {

                        if (row.getTitle().toLowerCase().contains(Key.toLowerCase())){
                            lstFiltered.add(row);
                        }

                    }

                    mDataFiltered = lstFiltered;

                }


                FilterResults filterResults = new FilterResults();
                filterResults.values= mDataFiltered;
                return filterResults;

            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {


                mDataFiltered = (List<StudentsItem>) results.values;
                notifyDataSetChanged();

            }
        };




    }

    public void removeItem(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }
    public List<StudentsItem> getData() {
        return mData;
    }

    public void restoreItem(StudentsItem item, int position) {
        mData.add(position, item);
        notifyItemInserted(position);
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title,tv_content,tv_date;
        RelativeLayout container;
        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_content = itemView.findViewById(R.id.tv_description);
        }
    }

    public void onMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mData, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mData, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
    public void UpdateItem(int position , StudentsItem studentsItem){
        mData.remove(position);
        mData.add(studentsItem);
        notifyItemChanged(position);
        notifyDataSetChanged();
    }
}
