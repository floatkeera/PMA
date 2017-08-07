package com.teephopk.pma;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {


    Preference notificationPref;
    Preference soundPref;



    public SettingsFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        notificationPref = (SwitchPreference) findPreference("NOTIFICATIONS");
        soundPref = (SwitchPreference) findPreference("SOUND");



        /*
        notificationPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {


                boolean isChecked = (Boolean) o;
                if (isChecked) {
                    FirebaseMessaging.getInstance().subscribeToTopic("MAIN_NO_SOUND");
                } else {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("MAIN_NOSOUND");
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("MAIN_SOUND");
                }

                return isChecked;

            }
        });

        soundPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                boolean isChecked = (Boolean) o;
                if (isChecked) {
                    FirebaseMessaging.getInstance().subscribeToTopic("MAIN_SOUND");
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("MAIN_NOSOUND");
                } else {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("MAIN_SOUND");
                    FirebaseMessaging.getInstance().subscribeToTopic("MAIN_NOSOUND");
                }

                return isChecked;

            }
        });
        */
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        ((MenuActivity)getActivity()).navigationView.setCheckedItem(R.id.nav_settings);
        ((MenuActivity)getActivity()).changeTitle(getString(R.string.action_settings));

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

        if(s.equals("NOTIFICATIONS")){

            boolean isChecked = Boolean.valueOf(sharedPreferences.toString());
            if (isChecked) {
                FirebaseMessaging.getInstance().subscribeToTopic("MAIN_NO_SOUND");
            } else {
                FirebaseMessaging.getInstance().unsubscribeFromTopic("MAIN_NOSOUND");
                FirebaseMessaging.getInstance().unsubscribeFromTopic("MAIN_SOUND");
            }

        }

        if(s.equals("SOUND")){

            boolean isChecked = Boolean.valueOf(sharedPreferences.toString());
            if (isChecked) {
                FirebaseMessaging.getInstance().subscribeToTopic("MAIN_SOUND");
                FirebaseMessaging.getInstance().unsubscribeFromTopic("MAIN_NOSOUND");
            } else {
                FirebaseMessaging.getInstance().unsubscribeFromTopic("MAIN_SOUND");
                FirebaseMessaging.getInstance().subscribeToTopic("MAIN_NOSOUND");
            }

        }


    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }
}
