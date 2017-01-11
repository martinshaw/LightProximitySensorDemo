package com.adaptivemedia.lightproximitysensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private SensorManager mSensorManager;
    private Sensor mLum, mProx;
    public static TextView lightOut = null;
    public static TextView proxOut = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mLum = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mProx = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return LightSensorFragment.newInstance(position + 1);
                case 1:
                    return ProximitySensorFragment.newInstance(position + 1);
                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Light";
                case 1:
                    return "Proximity";
            }
            return null;
        }
    }

    public static class LightSensorFragment extends Fragment {

        public static LightSensorFragment newInstance(int sectionNumber) {
            LightSensorFragment fragment = new LightSensorFragment();
            return fragment;
        }

        public LightSensorFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_light_sensor, container, false);
            lightOut = (TextView) rootView.findViewById(R.id.lightLabel);

            return rootView;
        }
    }

    public static class ProximitySensorFragment extends Fragment {

        public static ProximitySensorFragment newInstance(int sectionNumber) {
            ProximitySensorFragment fragment = new ProximitySensorFragment();
            return fragment;
        }

        public ProximitySensorFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_proximity_sensor, container, false);
            proxOut = (TextView) rootView.findViewById(R.id.proxLabel);

            return rootView;
        }
    }



    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {

        if(lightOut != null && proxOut != null) {
            if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
                    lightOut.setText(Integer.toString((int) Math.round(event.values[0])));
            }
            if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
                if((int) Math.round(event.values[0]) > 50){
                    proxOut.setText("FAR");
                }else if((int) Math.round(event.values[0]) < 50 && (int) Math.round(event.values[0]) > 1){
                    proxOut.setText("NEAR");
                }else{
                    proxOut.setText("CLOSE");
                }
            }
        }

    }


    @Override
    protected void onResume() {
        // Register a listener for the sensor.
        super.onResume();
        mSensorManager.registerListener(this, mLum, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mProx, SensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

}
