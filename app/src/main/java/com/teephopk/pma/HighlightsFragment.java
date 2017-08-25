package com.teephopk.pma;


import android.app.Fragment;
import android.os.Bundle;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HighlightsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HighlightsFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    HighlightsAdapter myAdapter;
    RecyclerView recyclerView;
    ArrayList<RecyclerViewDataModel> rvdmList;
    ArrayList<Product> hotProductsList;
    ArrayList<Product> topProductsList;
    ArrayList<CategoryDisplay> categoryDisplays;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public HighlightsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HighlightsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HighlightsFragment newInstance(String param1, String param2) {
        HighlightsFragment fragment = new HighlightsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_promotion, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

        super.onViewCreated(view, savedInstanceState);
        ((MenuActivity)getActivity()).changeTitle(getString(R.string.highlightstitle));

        ((MenuActivity)getActivity()).navigationView.setCheckedItem(R.id.nav_promotions);


        DatabaseReference mSlideImageRef = mRootRef.child("promotionsList");

        final ArrayList<TextSliderView> promotionImageModels = new ArrayList<TextSliderView>();



        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        rvdmList = new ArrayList<RecyclerViewDataModel>(2);

        mSlideImageRef.addValueEventListener(new ValueEventListener() {
                @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    promotionImageModels.clear();

                for(DataSnapshot ds : dataSnapshot.getChildren()){


                    PromotionImageModel model = ds.getValue(PromotionImageModel.class);


                    TextSliderView a = new TextSliderView(getActivity());
                    a.description(model.description);
                    a.image(model.photoURL);


                    a.setScaleType(BaseSliderView.ScaleType.FitCenterCrop);

                    promotionImageModels.add(a);
                }

                rvdmList.set(0, new RecyclerViewDataModel(RecyclerViewDataModel.SLIDER, promotionImageModels));
                myAdapter.notifyDataSetChanged();



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference mProductsRef = mRootRef.child("productList");
        hotProductsList = new ArrayList<Product>();

        mProductsRef.orderByChild("discount").limitToFirst(20).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                hotProductsList.clear();

                ArrayList<Product> logic = new ArrayList<Product>();

                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    logic.add(ds.getValue(Product.class));

                }

                for(int i = logic.size() - 1; i >= 0; i--){
                    hotProductsList.add(logic.get(i));
                }

                rvdmList.set(2, new RecyclerViewDataModel(RecyclerViewDataModel.PRODUCT, hotProductsList));
                myAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        topProductsList = new ArrayList<Product>();

        mProductsRef.orderByChild("rating").limitToLast(10).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                topProductsList.clear();

                ArrayList<Product> logic = new ArrayList<Product>();

                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    logic.add(ds.getValue(Product.class));

                }

                for(int i = logic.size() - 1; i >= 0; i--){
                    topProductsList.add(logic.get(i));
                }

                rvdmList.set(4, new RecyclerViewDataModel(RecyclerViewDataModel.PRODUCT, topProductsList));
                myAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final DatabaseReference categoryDisplayList = mRootRef.child("categoryDisplayList");


        categoryDisplays = new ArrayList<CategoryDisplay>();

        ValueEventListener categoryListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                categoryDisplays.clear();

                ArrayList<CategoryDisplay> logic = new ArrayList<CategoryDisplay>();

                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    logic.add(ds.getValue(CategoryDisplay.class));

                }

                for(int i = logic.size() - 1; i >= 0; i--){
                    categoryDisplays.add(logic.get(i));
                }

                rvdmList.set(6, new RecyclerViewDataModel(RecyclerViewDataModel.CATEGORY, categoryDisplays));
                myAdapter.notifyDataSetChanged();
                categoryDisplayList.removeEventListener(this);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        categoryDisplayList.orderByChild("viewcount").limitToLast(10).addValueEventListener(categoryListener);











        rvdmList.add(0, new RecyclerViewDataModel(RecyclerViewDataModel.PROGRESSBAR, null));
        rvdmList.add(1, new RecyclerViewDataModel(RecyclerViewDataModel.TITLE, getString(R.string.hotsale)));
        rvdmList.add(2, new RecyclerViewDataModel(RecyclerViewDataModel.PROGRESSBAR, null));
        rvdmList.add(3, new RecyclerViewDataModel(RecyclerViewDataModel.TITLE, getString(R.string.highestrated)));
        rvdmList.add(4, new RecyclerViewDataModel(RecyclerViewDataModel.PROGRESSBAR, null));
        rvdmList.add(5, new RecyclerViewDataModel(RecyclerViewDataModel.TITLE, getString(R.string.topcategories)));
        rvdmList.add(6, new RecyclerViewDataModel(RecyclerViewDataModel.PROGRESSBAR, null));
        rvdmList.add(7, new RecyclerViewDataModel(RecyclerViewDataModel.EMPTY, null));

        myAdapter = new HighlightsAdapter(getActivity(), rvdmList);





        recyclerView.setAdapter(myAdapter);
        RecyclerView.LayoutManager recyclerLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(recyclerLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());



    }

}
