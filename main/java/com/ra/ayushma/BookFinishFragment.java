package com.ra.ayushma;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class BookFinishFragment extends Fragment {
    int position;
    String date_time;
    String dtStr;
    int book_id;
    Doctor dr;

    public BookFinishFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            book_id=bundle.getInt("BookId",0);
            position=bundle.getInt("Position", -1);
            date_time=bundle.getString("DateTime","");
            String dtAry[]=date_time.split("-|%20|:");
            dtStr=dtAry[2]+" "+getMonthString(Integer.parseInt(dtAry[1]))+" "+dtAry[0]+" : "+Integer.parseInt(dtAry[3])+" "+getAMPM(Integer.parseInt(dtAry[3]));
            dr = ((MyApplication) getActivity().getApplication()).doctorList.get(position);
        }
    }

    private String getAMPM(int hr) {
        if(hr < 12){
            return "PM";
        }else{
            return "AM";
        }
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_book_finish, container, false);
        CircleImageView ivFinishPic = (CircleImageView) v.findViewById(R.id.ivFinishPic);
        TextView tvFinishName = (TextView) v.findViewById(R.id.tvFinishName);
        TextView tvFinishDate = (TextView) v.findViewById(R.id.tvFinishDate);
        TextView tvFinishLoc = (TextView) v.findViewById(R.id.tvFinishLoc);
        TextView tvBookRef = (TextView) v.findViewById(R.id.tvBookRef);

        Drawable drawable = getResources().getDrawable(getResources().getIdentifier("pp", "drawable", getActivity().getPackageName()));
        Picasso.with(getActivity()).load("http://ayushma.in/app/images/"+dr.id+".jpg").placeholder(drawable) //this is optional the image to display while the url image is downloading
                .error(drawable).into(ivFinishPic);
        tvFinishName.setText(dr.name);
        tvFinishDate.setText(dtStr);
        tvFinishLoc.setText(dr.loc);
        tvBookRef.setText(String.format("%04d", book_id));
        return v;
    }

}
