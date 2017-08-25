package com.teephopk.pma;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by floatkeera on 8/7/17.
 */

public class CategoryDisplayAdapter extends RecyclerView.Adapter<CategoryDisplayAdapter.MyViewHolder> {


    ArrayList<CategoryDisplay> data;
    Context context;
    ItemClickListener mClickListener;
    boolean highlights;

    public CategoryDisplayAdapter(Context context, ArrayList<CategoryDisplay> input, boolean highlights) {

        this.context = context;
        this.data = input;
        this.highlights = highlights;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if(highlights){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_grid_highlights, parent, false);
        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_grid, parent, false);
        }

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        CategoryDisplay obj = data.get(position);
        String categoryNameEN = obj.name_en;
        String categoryNameTH = obj.name_th;
        String iconURL = obj.icon;
        String backgroundURL = obj.background;

        if(Locale.getDefault().getLanguage().equals("th")) {
            holder.categoryNameView.setText(categoryNameTH);
        } else{
            holder.categoryNameView.setText(categoryNameEN);
        }


        GlideApp.with(context)
                .load(iconURL)
                .fitCenter()
                .transition(withCrossFade())
                .into(holder.categoryImageView);



        GlideApp.with(context)
                .load(backgroundURL)
                .centerCrop()
                .placeholder(new ColorDrawable(context.getResources().getColor(R.color.md_grey_600)))
                .transition(withCrossFade())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(holder.background);




        holder.background.setColorFilter(Color.rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);

    }

    // total number of cells
    @Override
    public int getItemCount() {
        if (data != null)
            return data.size();
        else
            return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView categoryImageView;
        CustomTitleTextView categoryNameView;
        CustomTextView productPriceView;
        ImageView background;


        public MyViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            categoryImageView = (ImageView) itemView.findViewById(R.id.imgProduct);
            categoryNameView = (CustomTitleTextView) itemView.findViewById(R.id.txtProductName);
            productPriceView = (CustomTextView) itemView.findViewById(R.id.txtProductPrice);
            background = (ImageView) itemView.findViewById(R.id.backgroundimage);


        }


        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public CategoryDisplay getItem(int id) {
        return data.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}

