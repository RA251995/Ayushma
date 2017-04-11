package com.ra.ayushma;


import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class DrDetailsFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    int position;
    Doctor dr;
    TextView tvDate;
    int hour,day,month,year;
    String loc;
    Button bTime10,bTime12,bTime2,bTime4;

    public DrDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            position=bundle.getInt("Position", -1);
            dr = ((MyApplication) getActivity().getApplication()).doctorList.get(position);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_dr_details, container, false);
        CircleImageView ivDetailsPic = (CircleImageView) v.findViewById(R.id.ivDetailsPic);
        TextView tvDetailsName = (TextView) v.findViewById(R.id.tvDetailsName);
        TextView tvDetailsSpec = (TextView) v.findViewById(R.id.tvDetailsSpec);
        TextView tvDetailsLoc = (TextView) v.findViewById(R.id.tvDetailsLoc);
        TextView tvDetailsFee = (TextView) v.findViewById(R.id.tvDetailsFee);
        RatingBar rbDetails = (RatingBar) v.findViewById(R.id.rbDetails);

        Drawable drawable = getResources().getDrawable(getResources().getIdentifier("pp", "drawable", getActivity().getPackageName()));
        Picasso.with(getActivity()).load("http://ayushma.in/app/images/"+dr.id+".jpg").placeholder(drawable) //this is optional the image to display while the url image is downloading
                .error(drawable).into(ivDetailsPic);
        tvDetailsName.setText(dr.name);
        tvDetailsSpec.setText(dr.spec);
        tvDetailsLoc.setText(dr.loc);
        tvDetailsFee.setText("Rs."+dr.fee);
        rbDetails.setRating(Float.parseFloat(dr.rating));

        LinearLayout llDate = (LinearLayout)v.findViewById(R.id.llDate);
        llDate.setOnClickListener(this);
        tvDate=(TextView)v.findViewById(R.id.tvDate);
        Calendar now = Calendar.getInstance();
        day=now.get(Calendar.DAY_OF_MONTH);
        month=now.get(Calendar.MONTH);
        year=now.get(Calendar.YEAR);
        tvDate.setText(now.get(Calendar.DAY_OF_MONTH)+" "+getMonthString(now.get(Calendar.MONTH))+" "+now.get(Calendar.YEAR));

        TextView tvSelectLoc = (TextView) v.findViewById(R.id.tvSelectLoc);
        this.loc=dr.loc;
        tvSelectLoc.setText(loc);

        bTime10 = (Button)v.findViewById(R.id.bTime10);
        bTime12 = (Button)v.findViewById(R.id.bTime12);
        bTime2 = (Button)v.findViewById(R.id.bTime2);
        bTime4 = (Button)v.findViewById(R.id.bTime4);
        bTime10.setOnClickListener(this);
        bTime12.setOnClickListener(this);
        bTime2.setOnClickListener(this);
        bTime4.setOnClickListener(this);
        hour=0;

        Button bBook = (Button)v.findViewById(R.id.bBook);
        bBook.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.llDate:
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        DrDetailsFragment.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setAccentColor(Color.parseColor("#1E8A5E"));
                dpd.setMinDate(now);
                dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
                break;
            case R.id.bTime10:
                hour=10;
                bTime10.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_white_pressed));
                bTime12.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_white_normal));
                bTime2.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_white_normal));
                bTime4.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_white_normal));
                break;
            case R.id.bTime12:
                hour=12;
                bTime10.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_white_normal));
                bTime12.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_white_pressed));
                bTime2.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_white_normal));
                bTime4.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_white_normal));
                break;
            case R.id.bTime2:
                hour=2;
                bTime10.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_white_normal));
                bTime12.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_white_normal));
                bTime2.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_white_pressed));
                bTime4.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_white_normal));
                break;
            case R.id.bTime4:
                hour=4;
                bTime10.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_white_normal));
                bTime12.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_white_normal));
                bTime2.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_white_normal));
                bTime4.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_white_pressed));
                break;
            case R.id.bBook:
                if(hour!=0){
                    ConfirmFragment confirmFragment = new ConfirmFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("Position", position);
                    bundle.putString("DateTime", year+"-"+padZero(month)+"-"+padZero(day)+"%20"+padZero(hour)+":00:00");
                    //bundle.putString("DrPic", mValues.get(position).drPic);
                    confirmFragment.setArguments(bundle);
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager()
                            .beginTransaction();
                    fragmentTransaction.replace(R.id.frame, confirmFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }else{
                    Toast.makeText(getActivity(),"Select Time",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private String padZero(int data) {
        if(data<10){
            return "0"+data;
        }
        return String.valueOf(data);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        this.day=dayOfMonth;
        this.month=monthOfYear;
        this.year=year;
        tvDate.setText(dayOfMonth+" "+getMonthString(monthOfYear)+" "+year);
    }

    private String getMonthString(int monthOfYear) {
        switch(monthOfYear){
            case 0:
                return "January";
            case 1:
                return "February";
            case 2:
                return "March";
            case 3:
                return "April";
            case 4:
                return "May";
            case 5:
                return "June";
            case 6:
                return "July";
            case 7:
                return "August";
            case 8:
                return "September";
            case 9:
                return "October";
            case 10:
                return "November";
            case 11:
                return "December";
        }
        return "January";
    }
}
