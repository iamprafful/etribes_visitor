package in.shadowsoft.visitor;

import android.*;
import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    int homestay_req = 2;
    int upcoming_event_req = 1;
    LocationManager locationManager;
    LocationListener locationListener;
    LottieAnimationView shimmer;

    String[] dates = new String[3];
    String[] months = new String[3];
    String[] titles = new String[3];
    String[] add = new String[3];
    ListView events;
    HorizontalScrollView sv_nav;
    Button more_btn;
    CardView upcoming_cv;
    TextView eve_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getJSON("http://ciphers.shadowsoft.in/tribal/upcoming_events.php", upcoming_event_req);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        events = findViewById(R.id.list_event);
        sv_nav = findViewById(R.id.upcoming_nav);
        more_btn = findViewById(R.id.more_btn);
        upcoming_cv = findViewById(R.id.card_view_Events);
        eve_title = findViewById(R.id.event_title);
        shimmer=findViewById(R.id.shimmer);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                //Toast.makeText(home.this,location.getLatitude()+","+location.getLongitude(),Toast.LENGTH_LONG).show();
               // finish();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                AlertDialog.Builder builder = new AlertDialog.Builder(home.this);
                builder.setTitle("Need your Location");
                builder.setMessage("Turn on GPS")
                        .setPositiveButton("Open Settings", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });

                AlertDialog dailog=builder.create();
                dailog.show();
                Button negative = dailog.getButton(DialogInterface.BUTTON_NEGATIVE);
                negative.setTextColor(Color.BLACK);
                Button positive = dailog.getButton(DialogInterface.BUTTON_POSITIVE);
                positive.setTextColor(Color.BLACK);

            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

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
        getMenuInflater().inflate(R.menu.home, menu);
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

        if (id==R.id.nav_home)
        {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
        else if (id==R.id.nav_events)
        {
            Intent intent= new Intent(this,Events.class);
            startActivity(intent);
            this.overridePendingTransition(0,0);
        }
        else if (id==R.id.nav_buy)
        {
            Intent intent= new Intent(this,buy.class);
            startActivity(intent);
            this.overridePendingTransition(0,0);
        }
        else if (id==R.id.nav_Homestays)
        {
            getJSON("http://ciphers.shadowsoft.in/tribal/homestay_map.php", homestay_req);
        }
        else if (id==R.id.nav_Destination)
        {
            Intent intent= new Intent(this,Destination.class);
            startActivity(intent);
            this.overridePendingTransition(0,0);
        }
        else if (id==R.id.nav_Guide)
        {
            Intent intent= new Intent(this,tour.class);
            startActivity(intent);
            this.overridePendingTransition(0,0);
        }else if (id==R.id.nav_outlet)
        {
            Intent intent= new Intent(this,food_outlet.class);
            startActivity(intent);
            this.overridePendingTransition(0,0);
        }
        else if (id==R.id.nav_exit)
        {
            finishAffinity();
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void launch_homestay(View view) {
        getJSON("http://ciphers.shadowsoft.in/tribal/homestay_map.php", homestay_req);
    }

    public void launch_destination(View view) {
        Intent intent= new Intent(this,Destination.class);
        startActivity(intent);
        this.overridePendingTransition(0,0);
    }

    public void launch_buy(View view) {
        Intent intent= new Intent(this,buy.class);
        startActivity(intent);
        this.overridePendingTransition(0,0);
    }
    public void launch_outlet(View view) {
        Intent intent= new Intent(this,food_outlet.class);
        startActivity(intent);
        this.overridePendingTransition(0,0);
    }

    public void brouse_tour_pkg(View view) {
        Intent intent= new Intent(this,tour.class);
        startActivity(intent);
        this.overridePendingTransition(0,0);
    }

    public void launch_events(View view) {
        Intent intent= new Intent(this,Events.class);
        startActivity(intent);
        this.overridePendingTransition(0,0);
    }

    class customAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return dates.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView=getLayoutInflater().inflate(R.layout.upcoming_event_list,null);
            TextView date_tv=convertView.findViewById(R.id.tv_date);
            TextView month_tv=convertView.findViewById(R.id.tv_month);
            TextView title_tv=convertView.findViewById(R.id.tv_title);
            TextView add_tv=convertView.findViewById(R.id.tv_address);
            date_tv.setText(dates[position]);
            month_tv.setText(months[position]);
            title_tv.setText(titles[position]);
            add_tv.setText(add[position]);
            return convertView;
        }
    }
    private void loadIntoListView(String json) throws JSONException {
        //creating a json array from the json string
        if(json.equals("0")){
           Toast.makeText(this,"No internet Connection",Toast.LENGTH_LONG).show();
           sv_nav.setVisibility(View.INVISIBLE);
            more_btn.setVisibility(View.INVISIBLE);
            ViewGroup.LayoutParams params = upcoming_cv.getLayoutParams();
            params.height = 165;
            upcoming_cv.setLayoutParams(params);
            eve_title.setTextColor(Color.RED);
            eve_title.setText("Unable to update upcoming events :(\nPlease Check your Internet Connection");
        }
        else {
            JSONArray jsonArray = new JSONArray(json);

            //creating a string array for listview
            //String[] heroes = new String[jsonArray.length()];

            //looping through all the elements in json array
            for (int i = 0; i < 3; i++) {

                //getting json object from the json array
                JSONObject obj = jsonArray.getJSONObject(i);

                //getting the name from the json object and putting it inside string array
                titles[i] = obj.getString("title");
                dates[i] = obj.getString("Date");
                months[i] = obj.getString("month");
                add[i] = obj.getString("address");

            }

            //the array adapter to load data into list
            customAdapter adpt = new customAdapter();
            events.setAdapter(adpt);
        }
    }

    private void gethomestay(String json) throws JSONException {
        //creating a json array from the json string
        if(json.equals("0")){
            Toast.makeText(this,"No internet Connection",Toast.LENGTH_LONG).show();
        }
        else {
            JSONArray jsonArray = new JSONArray(json);

            String[] id= new String[jsonArray.length()];
            String[] name= new String[jsonArray.length()];
            String[] lat= new String[jsonArray.length()];
            String[] lng= new String[jsonArray.length()];
            String[] contact_name= new String[jsonArray.length()];
            String[] contact= new String[jsonArray.length()];
            String[] address= new String[jsonArray.length()];
            String[] image_loc= new String[jsonArray.length()];
            String[] description= new String[jsonArray.length()];
            int length=jsonArray.length();


            //creating a string array for listview
            //String[] heroes = new String[jsonArray.length()];

            //looping through all the elements in json array
            for (int i = 0; i < jsonArray.length(); i++) {

                //getting json object from the json array
                JSONObject obj = jsonArray.getJSONObject(i);

                //getting the name from the json object and putting it inside string array
                id[i] = obj.getString("id");
                name[i] = obj.getString("name");
                lat[i] = obj.getString("lat");
                lng[i] = obj.getString("lng");
                contact_name[i] = obj.getString("contact_name");
                contact[i] = obj.getString("contact");
                address[i] = obj.getString("address");
                image_loc[i] = obj.getString("image_loc");
                description[i] = obj.getString("description");

            }

            Intent launch_homestay_map= new Intent(home.this,homestay.class);
            launch_homestay_map.putExtra("id",id);
            launch_homestay_map.putExtra("name",name);
            launch_homestay_map.putExtra("lat",lat);
            launch_homestay_map.putExtra("lng",lng);
            launch_homestay_map.putExtra("contact_name",contact_name);
            launch_homestay_map.putExtra("contact",contact);
            launch_homestay_map.putExtra("address",address);
            launch_homestay_map.putExtra("image_loc",image_loc);
            launch_homestay_map.putExtra("array_length",length);
            launch_homestay_map.putExtra("description",description);
            startActivity(launch_homestay_map);
            this.overridePendingTransition(0,0);

        }
    }
    public void getJSON(final String urlWebService, final int reqID) {
        /*
        * As fetching the json string is a network operation
        * And we cannot perform a network operation in main thread
        * so we need an AsyncTask
        * The constrains defined here are
        * Void -> We are not passing anything
        * Void -> Nothing at progress update as well
        * String -> After completion it should return a string and it will be the json string
        * */
        class GetJSON extends AsyncTask<Void, Void, String> {
            final ProgressDialog progressDialog = new ProgressDialog(home.this, R.style.MyAlertDialogStyle);


            //this method will be called before execution
            //you can display a progress bar or something
            //so that user can understand that he should wait
            //as network operation may take some time
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                switch (reqID)
                {
                    case 2:
                        progressDialog.setIndeterminate(true);
                        progressDialog.setMessage("Loading Please Wait...");
                        progressDialog.show();
                        progressDialog.setCancelable(false);
                        break;
                }
            }


            //this method will be called after execution
            //so here we are displaying a toast with the json string
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    switch(reqID){
                        case 1:
                            shimmer.setVisibility(View.INVISIBLE);
                            loadIntoListView(s);
                            break;
                        case 2:
                            progressDialog.hide();
                            gethomestay(s);
                            break;
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }

            //in this method we are fetching the json string
            @Override
            protected String doInBackground(Void... voids) {



                try {
                    //creating a URL
                    URL url = new URL(urlWebService);

                    //Opening the URL using HttpURLConnection
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

                    //StringBuilder object to read the string from the service
                    StringBuilder sb = new StringBuilder();

                    //We will use a buffered reader to read the string from service
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    //A simple string to read values from each line
                    String json;

                    //reading until we don't find null
                    while ((json = bufferedReader.readLine()) != null) {

                        //appending it to string builder
                        sb.append(json + "\n");
                    }

                    //finally returning the read string
                    return sb.toString().trim();
                } catch (Exception e) {
                    return "0";
                }

            }
        }

        //creating asynctask object and executing it
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }


}
