package in.shadowsoft.visitor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Events extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    String[] dates;
    String[] months;
    String[] titles;
    String[] add;
    ListView events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getJSON("http://ciphers.shadowsoft.in/tribal/upcoming_events.php");
        events=findViewById(R.id.events_lv);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
        getMenuInflater().inflate(R.menu.events, menu);
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
            Intent intent= new Intent(this,home.class);
            startActivity(intent);
            this.overridePendingTransition(0,0);
        }
        else if (id==R.id.nav_events)
        {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
        else if (id==R.id.nav_buy)
        {
            Intent intent= new Intent(this,buy.class);
            startActivity(intent);
            this.overridePendingTransition(0,0);
        }
        else if (id==R.id.nav_Homestays)
        {
//            home obj=new home();
//            obj.getJSON("http://ciphers.shadowsoft.in/tribal/homestay_map.php", 2);
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
        }
        else if (id==R.id.nav_outlet)
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
    class customAdapter extends BaseAdapter {

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
            finish();
        }
        else {
            JSONArray jsonArray = new JSONArray(json);

            dates=new String[jsonArray.length()];
            months=new String[jsonArray.length()];
            titles=new String[jsonArray.length()];
            add=new String[jsonArray.length()];

            //creating a string array for listview
            //String[] heroes = new String[jsonArray.length()];

            //looping through all the elements in json array
            for (int i = 0; i < jsonArray.length(); i++) {

                //getting json object from the json array
                JSONObject obj = jsonArray.getJSONObject(i);

                //getting the name from the json object and putting it inside string array
                titles[i] = obj.getString("title");
                dates[i] = obj.getString("Date");
                months[i] = obj.getString("month");
                add[i] = obj.getString("address");

            }

            //the array adapter to load data into list
            Events.customAdapter adpt = new Events.customAdapter();
            events.setAdapter(adpt);
        }
    }
    private void getJSON(final String urlWebService) {
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
            final ProgressDialog progressDialog = new ProgressDialog(Events.this, R.style.MyAlertDialogStyle);


            //this method will be called before execution
            //you can display a progress bar or something
            //so that user can understand that he should wait
            //as network operation may take some time
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Loading Please Wait...");
                progressDialog.show();
                progressDialog.setCancelable(false);
            }


            //this method will be called after execution
            //so here we are displaying a toast with the json string
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();
                try {
                            loadIntoListView(s);


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
