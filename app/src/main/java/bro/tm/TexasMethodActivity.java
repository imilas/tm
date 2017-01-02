package bro.tm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TexasMethodActivity extends FragmentActivity {

    private static final int NUM_PAGES = 2;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0){
                return new SetupFragment().newInstance();
            }else{
                Fragment fragment = new TexasMethodFragment().newInstance(position);
                return fragment;
            }
        }
        @Override
        public int getCount() {
            return NUM_PAGES;
        }

    }

    public void updateMaxes(View v){
        //get the maxes from the view and put them in the ShareadPreferences
        ViewGroup setupLayout = (ViewGroup) this.findViewById(R.id.setup_layout);

        SharedPreferences prefs = this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        //get new weights
        TextView tv = (TextView) setupLayout.findViewById(R.id.bench_max);
        String benchMax =  tv.getText().toString();
        tv = (TextView) setupLayout.findViewById(R.id.squat_max);
        String squatMax =  tv.getText().toString();
        tv = (TextView) setupLayout.findViewById(R.id.deadlift_max);
        String deadLiftMax =  tv.getText().toString();
        //get new increments
        tv = (TextView) setupLayout.findViewById(R.id.deadlift_increment);
        String deadliftIncrement =  tv.getText().toString();
        tv = (TextView) setupLayout.findViewById(R.id.squat_increment);
        String squatIncrement =  tv.getText().toString();
        tv = (TextView) setupLayout.findViewById(R.id.bench_increment);
        String benchIncrement =  tv.getText().toString();

        editor.putString("bench_max",benchMax);
        editor.putString("squat_max",squatMax);
        editor.putString("deadlift_max",deadLiftMax);
        editor.putString("deadlift_increment",deadliftIncrement);
        editor.putString("squat_increment",squatIncrement);
        editor.putString("bench_increment",benchIncrement);
        editor.commit();

        //update the plan
        rebuild();

    }
    public void finishWeek(View v){
        Intent intent = new Intent(this,FinishWeekActivity.class);
        startActivity(intent);

    }

    @Override
    public void onResume(){
        super.onResume();
        rebuild();

    }

    public void rebuild(){
        List<Fragment> fragies =getSupportFragmentManager().getFragments();
        if(fragies!=null){
            for (Fragment fragment : fragies) {
                try {
                    android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.detach(fragment).attach(fragment).commit();

                } catch (NullPointerException e) {

                }
            }

        }
    }
}
