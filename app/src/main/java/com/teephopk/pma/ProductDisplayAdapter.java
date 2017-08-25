package com.teephopk.pma;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.Rating;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.fasterxml.jackson.databind.deser.Deserializers;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by floatkeera on 8/7/17.
 */

public class ProductDisplayAdapter extends RecyclerView.Adapter<ProductDisplayAdapter.BaseViewHolder> {


    ArrayList<Product> data;
    Context context;
    ItemClickListener mClickListener;
    boolean highlights;

    public ProductDisplayAdapter(Context context, ArrayList<Product> input, boolean highlights) {

        this.context = context;
        this.data = input;
        this.highlights = highlights;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(highlights){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_grid_highlights, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_grid, parent, false);
        }
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holderBase, int position) {


            holderBase.bindData(position);


    }

    // total number of cells
    @Override
    public int getItemCount() {
        if (data != null)
            return data.size();
        else
            return 0;
    }


    public abstract class BaseViewHolder extends  RecyclerView.ViewHolder{

        public BaseViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void bindData(int position);

    }


    public class MyViewHolder extends BaseViewHolder implements View.OnClickListener {

        ImageView productImageView;
        CustomTitleTextView productNameView;
        CustomTextView productPriceView;
        RatingBar ratingBar;
        CustomTextView discountPriceView;



        public MyViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            productImageView = (ImageView) itemView.findViewById(R.id.imgProduct);
            productNameView = (CustomTitleTextView) itemView.findViewById(R.id.txtProductName);
            productPriceView = (CustomTextView) itemView.findViewById(R.id.txtProductPrice);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
            discountPriceView = (CustomTextView) itemView.findViewById(R.id.discount);



        }

        @Override
        public void bindData(int position) {

            Product obj = data.get(position);

            String name = obj.name;
            String photoURL = obj.photoURL;
            String price = NumberFormat.getCurrencyInstance(new Locale("th", "TH")).format(obj.price);
            Double discount = obj.discount;


            GlideApp.with(context)
                    .load(photoURL)
                    .fitCenter()
                    .transition(withCrossFade())
                    .into(productImageView);

            productNameView.setText(name);
            productPriceView.setText(price);
            ratingBar.setRating(obj.rating);
            if(discount != 0){
                productPriceView.setPaintFlags(productPriceView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                discountPriceView.setVisibility(View.VISIBLE);
                discountPriceView.setText( NumberFormat.getCurrencyInstance(new Locale("th", "TH")).format(obj.price - (obj.price*(discount/100))) + " (-" + String.valueOf((int)Math.round(discount))+"%)");
            } else{
                productPriceView.setPaintFlags(productPriceView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                discountPriceView.setVisibility(View.INVISIBLE);
            }


        }


        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public Product getItem(int id) {
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

