package com.ra.ayushma;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DrListFragment.OnListFragmentInteractionListener, BookViewFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.setOnClickListener(new View.OnClickListener() {
         //   @Override
         //   public void onClick(View view) {
         //       Snackbar.make(view, "DEVELOPMENT VERSION", Snackbar.LENGTH_LONG)
         //               .setAction("Action", null).show();
         //   }
        //});

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SearchFragment searchFragment = new SearchFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, searchFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentManager fm = getSupportFragmentManager();
        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }

        if (id == R.id.nav_search) {
            SearchFragment searchFragment = new SearchFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame, searchFragment);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_booking){
            new ViewBookJSONParse().execute();
        } else if (id == R.id.nav_alerts) {

        } else if (id == R.id.nav_about) {
            AboutFragment aboutFragment = new AboutFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame, aboutFragment);
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onListFragmentInteraction(Doctor item) {

    }

    @Override
    public void onListFragmentInteraction(Booking item) {

    }

    private class ViewBookJSONParse extends AsyncTask<Void, Void, Boolean> {
        boolean result = false;
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ((MyApplication) getApplication()).bookingList = new ArrayList<>();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading ...");
            pDialog.setIndeterminate(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall("http://ayushma.in/app/viewbook.php?user_id="+((MyApplication) getApplication()).userid);

            //Log.e("TAG", "Response from url: " + jsonStr);

            if (jsonStr != null) try {
                // Getting JSON Array node
                JSONArray jAry = new JSONArray(jsonStr);

                // looping through All Messagess
                for (int i = 0; i < jAry.length(); i++) {
                    JSONObject jObj = jAry.getJSONObject(i);
                    String book_id = jObj.getString("id");
                    String doctor_id = jObj.getString("doctor_id");
                    String date_time = jObj.getString("date_time");
                    String doctor_loc = jObj.getString("doctor_loc");
                    String doctor_name = jObj.getString("name");

                    Booking booking = new Booking(book_id,doctor_id,date_time,doctor_loc,doctor_name);
                    ((MyApplication) getApplication()).bookingList.add(booking);
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
                BookViewFragment bookViewFragment = new BookViewFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame, bookViewFragment);
                fragmentTransaction.commit();
            } else {
                Toast.makeText(MainActivity.this,"No Bookings",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
