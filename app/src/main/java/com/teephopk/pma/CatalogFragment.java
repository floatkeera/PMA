package com.teephopk.pma;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CatalogFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CatalogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CatalogFragment extends Fragment implements CategoryDisplayAdapter.ItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    SwipeRefreshLayout swipeLayout;
    CustomButton testBtn;
    RecyclerView recyclerView;
    CategoryDisplayAdapter myAdapter;
    ValueEventListener valueEventListener;

    ArrayList<CategoryDisplay> categoryDisplays;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();


    public CatalogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CatalogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CatalogFragment newInstance(String param1, String param2) {
        CatalogFragment fragment = new CatalogFragment();
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
        View view = inflater.inflate(R.layout.fragment_catalog, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        categoryDisplays = new ArrayList<CategoryDisplay>();


        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);


        ((MenuActivity) getActivity()).changeTitle(getString(R.string.catalogtitle));

        ((MenuActivity) getActivity()).navigationView.setCheckedItem(R.id.nav_products);

        DatabaseReference categoryDisplayList = mRootRef.child("categoryDisplayList");



        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                categoryDisplays.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    categoryDisplays.add(ds.getValue(CategoryDisplay.class));
                }

                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        categoryDisplayList.addValueEventListener(valueEventListener);


        String[] data = {"BOOKS", "ELECTRONICS", "CLOTHING", "GIFTS", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48"};
        myAdapter = new CategoryDisplayAdapter(getActivity(), categoryDisplays, false);
        myAdapter.setClickListener(this);
        recyclerView.setAdapter(myAdapter);
        RecyclerView.LayoutManager recyclerLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(recyclerLayoutManager);


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onItemClick(View view, int position) {


        final CategoryDisplay thiscat = myAdapter.getItem(position);

        DatabaseReference categoryDisplayList = mRootRef.child("categoryDisplayList");


        categoryDisplayList.removeEventListener(valueEventListener);


        categoryDisplayList.child(thiscat.UID).child("viewcount").setValue(thiscat.viewcount + 1);

        Intent a = new Intent(getActivity(), SubCatalogActivity.class);


        if (Locale.getDefault().getLanguage().equals("th")) {
            a.putExtra("NAME", thiscat.name_th);
        } else {
            a.putExtra("NAME", thiscat.name_en);
        }

        a.putExtra("UID", thiscat.UID);
        a.putExtra("IMAGE", thiscat.background);

        startActivity(a);
        ((MenuActivity) getActivity()).animateIntent();


    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
