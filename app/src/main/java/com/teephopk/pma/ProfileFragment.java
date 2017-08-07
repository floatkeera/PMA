package com.teephopk.pma;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.request.RequestOptions;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    final static int IMAGE_CHANGED = 999;
    final static int NAME_CHANGED = 888;
    final static int EDIT_PROFILE = 777;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String[] profile1 = new String[] {"Change password", "Update my profile", "My payment methods", "My addresses", "Logout"};
    ListView lv;
    ImageView imageView;
    Uri imageUri;

    StorageReference storageRef;
    StorageReference imageRef;
    FirebaseUser mUser;

    View listheader;

    boolean imageExists;

    CustomTitleTextView mNameView;

    FirebaseAuth mAuth;

    ViewGroup mainContainer;




    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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

        View view =  inflater.inflate(R.layout.fragment_profile, container, false);
        mainContainer = container;



        // Inflate the layout for this fragment
       return view;

    }

    @Override
    public void onPause(){


        super.onPause();
    }

    @Override
    public void onResume(){

        super.onResume();

        ((MenuActivity)getActivity()).navigationView.setCheckedItem(R.id.nav_profile);

        /*if(imageRef != null) {
            imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {

                    imageUri = uri;

                    GlideApp.with(ProfileFragment.this)
                            .load(imageUri)
                            .apply(new RequestOptions().R.drawable.progress_animationCrop())
                            .placeholder(R.drawable.progress_animation)
                            .into(imageView);


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                }
            });
        }*/
    }





    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        if(Locale.getDefault().getLanguage().equals("th")){
            profile1 = new String[] {"เปลี่ยนรหัสผ่าน", "อัปเดตโปรไฟล์", "วิธีชำระเงินของฉัน", "ที่อยู่ของฉัน", "ออกจากระบบ"};
        }

        ((MenuActivity)getActivity()).navigationView.setCheckedItem(R.id.nav_profile);


        mUser = FirebaseAuth.getInstance().getCurrentUser();


        lv = (ListView) view.findViewById(R.id.listProfile1);


        listheader = getActivity().getLayoutInflater().inflate(R.layout.listviewheader, null, false);
        lv.addHeaderView(listheader, null, false);

        imageView = (ImageView) listheader.findViewById(R.id.profilepic);
        mNameView = (CustomTitleTextView)listheader.findViewById(R.id.txtName);


        mAuth = FirebaseAuth.getInstance();


        
        mNameView.setText(mUser.getDisplayName());

        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.simplerow, R.id.rowTextView, profile1);
        lv.setAdapter(listAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(i==1){
                    Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
                    startActivity(intent);
                    ((MenuActivity)getActivity()).animateIntent();
                }

                if(i==2){

                    Intent intent = new Intent(getActivity(), EditProfileActivity.class);

                    if(imageExists){
                        intent.putExtra("IMAGEURI", imageUri.toString());


                    }




                    //Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle();

                    startActivityForResult(intent, EDIT_PROFILE);
                    ((MenuActivity)getActivity()).animateIntent();


                }

                if(i == 5) {






                    AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
                    ab.setMessage("Are you sure you want to log out?").setPositiveButton("Yes, Exit", dialogClickListener)
                            .setNegativeButton("No, stay", dialogClickListener).show();
                }

            }
        });



        ((MenuActivity)getActivity()).changeTitle(getString(R.string.profiletitle));


        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        imageRef = storageRef.child("users");

        String UID = mUser.getUid();
        imageRef = imageRef.child(UID+"/dp.png");


        if(imageUri == null) {
            attemptGetPic();
        } else{
            GlideApp.with(ProfileFragment.this)
                    .load(imageUri)
                    .apply(new RequestOptions().circleCrop())
                    .placeholder(R.drawable.progress_animation)
                    .into(imageView);
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == IMAGE_CHANGED && requestCode == EDIT_PROFILE) {

            attemptGetPic();
            ((MenuActivity)getActivity()).updateHeaderPicture();

        }

        if(resultCode == NAME_CHANGED && requestCode == EDIT_PROFILE) {



            mUser.reload().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    mUser = FirebaseAuth.getInstance().getCurrentUser();
                    mNameView.setText(mUser.getDisplayName());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Log.d("FBFAILED", "Firebase failed reload");
                }
            });

            ((MenuActivity)getActivity()).updateHeaderName();


        }



    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:


                    FirebaseAuth.getInstance().signOut();
                    LoginManager.getInstance().logOut();


                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    dialog.dismiss();
                    break;
            }
        }
    };



    private void attemptGetPic(){
        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                imageUri = uri;

                GlideApp.with(ProfileFragment.this)
                        .load(imageUri)
                        .apply(new RequestOptions().circleCrop())
                        .placeholder(R.drawable.progress_animation)
                        .into(imageView);

                imageExists = true;


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                imageExists = false;
            }
        });
    }




}
