package bro.tm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

public class TexasMethodActivity extends AppCompatActivity {

    private static final int NUM_PAGES =4;
    public String logsTag="";
    public String setupTag="";
    public String planTag="";
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    SharedPreferences prefs = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent intent = new Intent(this,startup_activity.class);
        startActivity(intent);

        SharedPreferences prefs = this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);
        this.prefs = this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabsies);
        tabLayout.setupWithViewPager(mPager);

    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
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
        String benchMax =  checkEmpty(tv.getText().toString());
        tv = (TextView) setupLayout.findViewById(R.id.squat_max);
        String squatMax =  checkEmpty(tv.getText().toString());
        tv = (TextView) setupLayout.findViewById(R.id.deadlift_max);
        String deadLiftMax =  checkEmpty(tv.getText().toString());
        //get new increments
        tv = (TextView) setupLayout.findViewById(R.id.deadlift_increment);
        String deadliftIncrement = checkEmpty(tv.getText().toString());
        tv = (TextView) setupLayout.findViewById(R.id.squat_increment);
        String squatIncrement =  checkEmpty(tv.getText().toString());;
        tv = (TextView) setupLayout.findViewById(R.id.bench_increment);
        String benchIncrement =  checkEmpty(tv.getText().toString());
        tv = (TextView) setupLayout.findViewById(R.id.ohp_max);
        String ohpMax =  checkEmpty(tv.getText().toString());;
        tv = (TextView) setupLayout.findViewById(R.id.ohp_increment);
        String ohpIncrement =  checkEmpty(tv.getText().toString());
        tv = (TextView) setupLayout.findViewById(R.id.clean_max);
        String cleanMax =  checkEmpty(tv.getText().toString());
        tv = (TextView) setupLayout.findViewById(R.id.clean_increment);
        String cleanIncrement =  checkEmpty(tv.getText().toString());

        //sanitize the inputs otherwise dont change them

        editor.putString("bench_max",benchMax);
        editor.putString("squat_max",squatMax);
        editor.putString("deadlift_max",deadLiftMax);
        editor.putString("deadlift_increment",deadliftIncrement);
        editor.putString("squat_increment",squatIncrement);
        editor.putString("bench_increment",benchIncrement);
        editor.putString("ohp_max",ohpMax);
        editor.putString("ohp_increment",ohpIncrement);
        editor.putString("clean_max",cleanMax);
        editor.putString("clean_increment",cleanIncrement);

        editor.commit();
        //update the PlanVanilla
       rebuild();


    }

    private String checkEmpty(String boxVal){
        try {
            java.lang.Double.parseDouble(boxVal);
        } catch(Exception e) {
            boxVal = "0";
        }

        if(boxVal.isEmpty() || boxVal.length() == 0 || boxVal.equals("") || boxVal == null)
        {
            //EditText is empty
            boxVal = "0";
        }
        return boxVal;
    }

    public void saveUnit(View view){
        //save whatever unit the user has selected
        //get the maxes from the view and put them in the ShareadPreferences
        ViewGroup setupLayout = (ViewGroup) this.findViewById(R.id.setup_layout);
        SharedPreferences prefs = this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_lbs:
                if (checked)
                    editor.putString("unit", "lbs");
                    editor.commit();
                    break;
            case R.id.radio_kgs:
                if (checked)
                    editor.putString("unit", "kgs");
                    editor.commit();
                    break;
        }
        rebuild();
    }

    private class ScreenSlidePagerAdapter extends FragmentPagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0){
                PlanFragment fraggy = new PlanFragment().newInstance();
                fraggy.setWeekNumber(position);
                return  fraggy;
            }if (position == 1){
                PlanFragment fraggy = new PlanFragment().newInstance();
                fraggy.setWeekNumber(position);
                return  fraggy;
            }else if(position==2){
                return new SetupFragment().newInstance();
            }else if(position==3){
                return new LogsFragment().newInstance();
            }else{
                return null;
            }

        }
        @Override
        public CharSequence getPageTitle(int position) {

            if (position == 0) {
                return "Plan A";
            }
            if(position==1){
                return "Plan B";
            }
            if(position == 2) {
                return "Setup";
            }
            if(position==3){
                return "Logs";
            }
            return "Unknown Position";
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

    }

    public void finishWeek(View v){
        Intent intent = new Intent(this,FinishWeekActivity.class);
        startActivity(intent);
    }

    public void timerActivity(View v){
        Intent intent = new Intent(this,TimerActivity.class);
        startActivity(intent);
    }

    public void planSelector(View v){
        Intent intent = new Intent(this, PlanSelectActivity.class);
        startActivity(intent);
    }

    @Override
    public void onResume(){
        super.onResume();
        rebuild();
    }

    public void rebuild(){
        mPagerAdapter.notifyDataSetChanged();

    }
}

