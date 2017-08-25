package com.teephopk.pma;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by floatkeera on 8/22/17.
 */

public class HighlightsAdapter extends RecyclerView.Adapter<HighlightsAdapter.BaseViewHolder> {

    Context context;
    ArrayList<RecyclerViewDataModel> input;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

    public HighlightsAdapter(Context context, ArrayList<RecyclerViewDataModel> input) {
        this.context = context;
        this.input = input;
    }


    @Override
    public HighlightsAdapter.BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case RecyclerViewDataModel.SLIDER:
                return new SliderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.promotion_1, parent, false));
            case RecyclerViewDataModel.TITLE:
                return new TitleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.promotion_title, parent, false));
            case RecyclerViewDataModel.PROGRESSBAR:
                return new ProgressViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.promotion_progress, parent, false));
            case RecyclerViewDataModel.EMPTY:
                return new EmptyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.promotion_empty, parent, false));
            case RecyclerViewDataModel.PRODUCT:
                return new ProductRecyclerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.promotion_innerrecycler, parent, false));
            case RecyclerViewDataModel.CATEGORY:
                return new CategoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.promotion_innerrecycler, parent, false));
            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(HighlightsAdapter.BaseViewHolder holder, int position) {

        holder.bindData(position);

    }

    @Override
    public int getItemCount() {
        if (input != null)
            return input.size();
        else
            return 0;

    }

    @Override
    public int getItemViewType(int position) {
        if(input.get(position)!= null) {
            return input.get(position).vType;
        } else{
            return 0;
        }
    }

    public class SliderViewHolder extends BaseViewHolder {

        SliderLayout sliderLayout;
        ArrayList<TextSliderView> slideList;


        public SliderViewHolder(View itemView) {
            super(itemView);

            sliderLayout = (SliderLayout) itemView.findViewById(R.id.slider);
            sliderLayout.setCustomIndicator((PagerIndicator) itemView.findViewById(R.id.custom_indicator));
            sliderLayout.setCustomAnimation(new DescriptionAnimation());


        }

        @Override
        public void bindData(int position) {



            sliderLayout.removeAllSliders();

            slideList = (ArrayList<TextSliderView>) input.get(position).data;

            for(TextSliderView tsv : slideList){

                sliderLayout.addSlider(tsv);
            }

        }
    }

    public class TitleViewHolder extends BaseViewHolder{

        CustomTitleTextView titleTextView;

        public TitleViewHolder(View itemView) {
            super(itemView);

            titleTextView = (CustomTitleTextView) itemView.findViewById(R.id.promotionTitle);


        }

        @Override
        public void bindData(int position) {

            String title = (String) input.get(position).data;
            titleTextView.setText(title);

        }
    }

    public class ProgressViewHolder extends BaseViewHolder{

        ProgressBar progressBar;

        public ProgressViewHolder(View itemView) {
            super(itemView);

            progressBar = (ProgressBar) itemView.findViewById(R.id.progress);
            ThreeBounce cubeGrid = new ThreeBounce();
            cubeGrid.setColor(context.getResources().getColor(R.color.colorAccent));
            progressBar.setIndeterminateDrawable(cubeGrid);
        }

        @Override
        public void bindData(int position) {


        }
    }

    public class EmptyViewHolder extends BaseViewHolder{

        public EmptyViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bindData(int position) {

        }
    }

    public class ProductRecyclerViewHolder extends BaseViewHolder implements ProductDisplayAdapter.ItemClickListener{


        ProductDisplayAdapter  myAdapter;
        RecyclerView recyclerView;


        public ProductRecyclerViewHolder(View itemView) {
            super(itemView);

            recyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerView);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setHasFixedSize(true);
            recyclerView.setNestedScrollingEnabled(false);

        }

        @Override
        public void bindData(int position) {

            ArrayList<Product> productArrayList = (ArrayList<Product>) input.get(position).data;


            myAdapter = new ProductDisplayAdapter(context, productArrayList, true);
            recyclerView.setAdapter(myAdapter);
            myAdapter.notifyDataSetChanged();
            myAdapter.setClickListener(this);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(layoutManager);


        }

        @Override
        public void onItemClick(View view, int position) {
            Product thisproduct = myAdapter.getItem(position);
            Intent a = new Intent(context, ProductActivity.class);
            a.putExtra("CHOSEN_PRODUCT", thisproduct);
            context.startActivity(a);
            ((MenuActivity) context).animateIntent();

        }
    }

    public class CategoryViewHolder extends ProductRecyclerViewHolder implements CategoryDisplayAdapter.ItemClickListener {

        CategoryDisplayAdapter myAdapter;

        public CategoryViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bindData(int position) {
            ArrayList<CategoryDisplay> categoryDisplays = (ArrayList<CategoryDisplay>) input.get(position).data;

            myAdapter = new CategoryDisplayAdapter(context, categoryDisplays, true);
            recyclerView.setAdapter(myAdapter);
            myAdapter.notifyDataSetChanged();
            myAdapter.setClickListener(this);
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 2, LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(layoutManager);



        }

        @Override
        public void onItemClick(View view, int position) {


            final CategoryDisplay thiscat = myAdapter.getItem(position);

            DatabaseReference categoryDisplayList = mRootRef.child("categoryDisplayList");



            categoryDisplayList.child(thiscat.UID).child("viewcount").setValue(thiscat.viewcount + 1);

            Intent a = new Intent(context, SubCatalogActivity.class);


            if (Locale.getDefault().getLanguage().equals("th")) {
                a.putExtra("NAME", thiscat.name_th);
            } else {
                a.putExtra("NAME", thiscat.name_en);
            }

            a.putExtra("UID", thiscat.UID);
            a.putExtra("IMAGE", thiscat.background);

            context.startActivity(a);
            ((MenuActivity) context).animateIntent();


        }
    }



    public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

        public BaseViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void bindData(int position);

    }


}
