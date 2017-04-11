package com.ra.ayushma;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements View.OnClickListener, TextWatcher {
    EditText etDrLoc, etDrSpec;
    TextView tvError;
    String searchLoc, searchSpec;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        etDrLoc = (EditText)v.findViewById(R.id.etDrLoc);
        etDrSpec = (EditText)v.findViewById(R.id.etDrSpec);
        tvError = (TextView)v.findViewById(R.id.tvError);
        etDrSpec.addTextChangedListener(this);
        etDrLoc.addTextChangedListener(this);
        Button bSearch = (Button)v.findViewById(R.id.bSearch);
        bSearch.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bSearch:
                searchLoc = etDrLoc.getText().toString().trim();
                searchSpec = etDrSpec.getText().toString().trim();
                new JSONParse().execute();
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        tvError.setText("");
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }


    private class JSONParse extends AsyncTask<Void, Void, Boolean> {
        boolean result = false;
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ((MyApplication) getActivity().getApplication()).doctorList = new ArrayList<>();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading ...");
            pDialog.setIndeterminate(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall("http://ayushma.in/app/search.php?loc="+searchLoc+"&spec="+searchSpec);

            //Log.e("TAG", "Response from url: " + jsonStr);

            if (jsonStr != null) try {
                // Getting JSON Array node
                JSONArray jAry = new JSONArray(jsonStr);

                // looping through All Messagess
                for (int i = 0; i < jAry.length(); i++) {
                    JSONObject jObj = jAry.getJSONObject(i);
                    String id = jObj.getString("id");
                    String name = jObj.getString("name");
                    String spec = jObj.getString("spec");
                    String loc = jObj.getString("loc");
                    String rating = jObj.getString("rating");
                    String fee = jObj.getString("fee");

                    Doctor dr = new Doctor(id,name,spec,loc,rating,fee);
                    ((MyApplication) getActivity().getApplication()).doctorList.add(dr);
                }
                result = true;
            } catch (final JSONException e) {
                Log.e("Ayushma", "Json parsing error: " + e.getMessage());
            }
            else {
                Log.e("Ayushma", "Couldn't get json from server.");
            }
            return result;
        }


        @Override
        protected void onPostExecute(Boolean res) {
            pDialog.cancel();
            if (res == true) {
                DrListFragment drListFragment = new DrListFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame, drListFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            } else {
                tvError.setText("No Doctors Found");
            }
        }
    }
}
