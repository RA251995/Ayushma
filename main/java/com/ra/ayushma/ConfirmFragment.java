package com.ra.ayushma;


import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ConfirmFragment extends Fragment {
    int position;
    String date_time;
    Doctor dr;

    public ConfirmFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            position=bundle.getInt("Position", -1);
            date_time=bundle.getString("DateTime","");
            dr = ((MyApplication) getActivity().getApplication()).doctorList.get(position);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_confirm, container, false);
        CircleImageView ivConfirmPic = (CircleImageView) v.findViewById(R.id.ivConfirmPic);
        TextView tvConfirmName = (TextView) v.findViewById(R.id.tvConfirmName);
        TextView tvConfirmSpec = (TextView) v.findViewById(R.id.tvConfirmSpec);
        TextView tvConfirmLoc = (TextView) v.findViewById(R.id.tvConfirmLoc);
        TextView tvConfirmFee = (TextView) v.findViewById(R.id.tvConfirmFee);
        RatingBar rbConfirm = (RatingBar) v.findViewById(R.id.rbConfirm);

        Drawable drawable = getResources().getDrawable(getResources().getIdentifier("pp", "drawable", getActivity().getPackageName()));
        Picasso.with(getActivity()).load("http://ayushma.in/app/images/"+dr.id+".jpg").placeholder(drawable) //this is optional the image to display while the url image is downloading
                .error(drawable).into(ivConfirmPic);
        tvConfirmName.setText(dr.name);
        tvConfirmSpec.setText(dr.spec);
        tvConfirmLoc.setText(dr.loc);
        tvConfirmFee.setText("Rs."+dr.fee);
        rbConfirm.setRating(Float.parseFloat(dr.rating));

        TextView tvConfirm = (TextView)v.findViewById(R.id.tvConfirm);
        tvConfirm.setText(Html.fromHtml("Please Pay <b>Rs."+dr.fee+"</b> towards<br/>the consultation fee of <br/><b>"+dr.name+"</b>"));

        Button bPayOffline=(Button)v.findViewById(R.id.bPayOffline);
        bPayOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BookTask().execute();
            }
        });
        return v;
    }

    private class BookTask extends AsyncTask<Void, Void, Boolean> {
        boolean result = false;
        int book_id;
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please Wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... arg0) {
            try {
                HttpHandler sh = new HttpHandler();

                String user_id = ((MyApplication) getActivity().getApplication()).userid;

                String url = "http://ayushma.in/app/book.php?user_id=" + user_id + "&doctor_id=" + dr.id + "&date_time=" + date_time + "&doctor_loc=" + dr.loc;
                String encodedUrl = url.replaceAll(" ", "%20");;

                // Making a request to url and getting response
                String responseStr = sh.makeServiceCall(encodedUrl);

                //Log.e("TAG", "Response from url: " + responseStr);
                book_id=Integer.parseInt(responseStr.trim());
                if (book_id>0) {
                    result = true;
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            return result;
        }


        @Override
        protected void onPostExecute(Boolean res) {
            pDialog.cancel();
            if (res == true) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }

                BookFinishFragment bookFinishFragment = new BookFinishFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("BookId", book_id);
                bundle.putInt("Position", position);
                bundle.putString("DateTime",date_time);
                bookFinishFragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame, bookFinishFragment);
                fragmentTransaction.commit();
            } else {
                Toast.makeText(getActivity(),"Please Try Again Later",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
