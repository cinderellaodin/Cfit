package com.odin.cfit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.odin.cfit.R;
import com.odin.cfit.model.News;

import java.util.List;

public class FitNewsAdapter extends RecyclerView.Adapter<FitNewsAdapter.ViewHolder> {
    private Context mContext;
    private List<News> mNews;
    private FitNewsAdapter.OnItemClickListener mListener;


    public FitNewsAdapter(FragmentActivity activity, List<News> mNews) {
        this.mContext = activity;
        this.mNews = mNews;
    }
   /* public FitNewsAdapter(Context context, List<News> mNews, int item_news_card) {
        this.mContext = context;
        this.mNews = mNews;

    }*/

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_news_card,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        News newsCurrent = mNews.get(position);

        holder.title.setText(newsCurrent.title);
        holder.subtitle.setText(newsCurrent.subtitle);
        holder.date.setText(newsCurrent.date);

        Glide.with(mContext)
                .load(newsCurrent.getImg())
                .centerCrop()
                .into(holder.image);

      /*  holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener == null) return;
                mListener.onItemClick(view, view.get(position), position);
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return mNews.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView image;
        public TextView title;
        public TextView subtitle;
        public TextView date;
        public View lyt_parent;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            subtitle = itemView.findViewById(R.id.subtitle);
            date = itemView.findViewById(R.id.date);
            lyt_parent = itemView.findViewById(R.id.lyt_parent);

           // itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            //passing clicks and position
            if (mListener != null){
                //check if listener is connected
                int position = getAdapterPosition();

                if (position != RecyclerView.NO_POSITION){
                    //send clickback to activity
                    mListener.onItemClick(position);
                }
            }
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
