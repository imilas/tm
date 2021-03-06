package bro.tm;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import events.DatabaseUpdate;

public class SetupFragment extends Fragment{

    ViewGroup rootView;
    public static SetupFragment newInstance(){
        SetupFragment thisFragment = new SetupFragment();
        return thisFragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        SharedPreferences prefs = this.getActivity().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_setup_layout, container, false);

        setSetupView();
        return rootView;
    }

    public void setSetupView(){
        //set the maxes and increments
        SharedPreferences  prefs = this.getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        TextView v = (TextView) rootView.findViewById(R.id.squat_max);
        v.setText(prefs.getString("squat_max","300"));
        v = (TextView) rootView.findViewById(R.id.deadlift_max);
        v.setText(prefs.getString("deadlift_max","400"));
        v = (TextView) rootView.findViewById(R.id.bench_max);
        v.setText(prefs.getString("bench_max","225"));
        v = (TextView) rootView.findViewById(R.id.bench_increment);
        v.setText(prefs.getString("bench_increment","10"));
        v = (TextView) rootView.findViewById(R.id.squat_increment);
        v.setText(prefs.getString("squat_increment","5"));
        v = (TextView) rootView.findViewById(R.id.deadlift_increment);
        v.setText(prefs.getString("deadlift_increment","5"));
        v = (TextView) rootView.findViewById(R.id.ohp_max);
        v.setText(prefs.getString("ohp_max","200"));
        v = (TextView) rootView.findViewById(R.id.ohp_increment);
        v.setText(prefs.getString("ohp_increment","5"));
        v = (TextView) rootView.findViewById(R.id.clean_max);
        v.setText(prefs.getString("clean_max","180"));
        v = (TextView) rootView.findViewById(R.id.clean_increment);
        v.setText(prefs.getString("clean_increment","5"));
        //units
        String unit = prefs.getString("unit","lbs");
        //flavors
        v = (TextView) rootView.findViewById(R.id.flavor_text);
        v.setText(prefs.getString("plan_type","Png TM"));

        //set units
        if(unit.equals("lbs")){
            RadioButton lbRadio = (RadioButton) rootView.findViewById(R.id.radio_lbs);
            lbRadio.setChecked(true);
        }else{
            RadioButton kgradio= (RadioButton) rootView.findViewById(R.id.radio_kgs);
            kgradio.setChecked(true);
        }

    }

    //TODO
    //wth is this
    // This method will be called when a MessageEvent is posted (in the UI thread for Toast)
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void DatabaseUpdate(DatabaseUpdate event) {
        //Toast.makeText(this.getActivity(), event.message + "setup fragment", Toast.LENGTH_SHORT).show();
        setSetupView();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

}
