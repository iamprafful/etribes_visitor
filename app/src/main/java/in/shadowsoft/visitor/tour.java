package in.shadowsoft.visitor;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.AdapterView;
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

public class tour extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ListView tour_pkg_list;
    int count;
    String[] tour_summary, guide_name, guide_contact, tour_cost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tour_pkg_list=findViewById(R.id.tour_pkg_list);
        getJSON("http://ciphers.shadowsoft.in/tribal/get_guides.php");


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
        getMenuInflater().inflate(R.menu.tour, menu);
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
            Intent intent= new Intent(this,homestay.class);
            startActivity(intent);
            this.overridePendingTransition(0,0);
        }
        else if (id==R.id.nav_Destination)
        {
            Intent intent= new Intent(this,Destination.class);
            startActivity(intent);
            this.overridePendingTransition(0,0);
        }
        else if (id==R.id.nav_Guide)
        {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
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
    class customAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return count;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            convertView = getLayoutInflater().inflate(R.layout.guide_list, null);
            TextView tour_pkg_name = convertView.findViewById(R.id.tour_pkg_name);
            TextView tour_guide_name = convertView.findViewById(R.id.guide_name);
            TextView tour_price = convertView.findViewById(R.id.tour_price);
            tour_pkg_name.setText(tour_summary[position]);
            tour_guide_name.setText("by "+guide_name[position]+" (+91 "+guide_contact[position]+")");
            tour_price.setText("Rs. "+tour_cost[position]);
            return convertView;
        }



    }
    private void loadIntoListView(String json) throws JSONException {

        //creating a json array from the json string
        if(json.equals("0")){
            Toast.makeText(tour.this,"No internet Connection",Toast.LENGTH_LONG).show();
            finish();
        }
        else {

            JSONArray jsonArray = new JSONArray(json);
            count=jsonArray.length();
            guide_name=new String[count];
            tour_summary=new  String[count];
            tour_cost=new String[count];
            guide_contact=new String[count];

            //creating a string array for listview
            //String[] heroes = new String[jsonArray.length()];

            //looping through all the elements in json array
            for (int i = 0; i < jsonArray.length(); i++) {

                //getting json object from the json array
                JSONObject obj = jsonArray.getJSONObject(i);

                //getting the name from the json object and putting it inside string array
                tour_summary[i] = obj.getString("tour_summary");
                tour_cost[i] = obj.getString("cost");
                guide_name[i] = obj.getString("guide_name");
                guide_contact[i]=obj.getString("guide_phone");

            }

            //the array adapter to load data into list
            tour.customAdapter adpt = new tour.customAdapter();
            tour_pkg_list.setAdapter(adpt);
            tour_pkg_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        final int position, long id) {

                    AlertDialog.Builder call_confirm=new AlertDialog.Builder(tour.this, R.style.defaultDailog);
                    call_confirm.setIcon(R.drawable.ic_call_custom_48dp);
                    call_confirm.setTitle("Calling?");
                    call_confirm.setMessage("Are you sure you want to call "+guide_name[position]);
                    call_confirm.setPositiveButton("Call", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:"+guide_contact[position]));
                            startActivity(intent);
                        }
                    });
                    call_confirm.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    call_confirm.show();


                }

            });
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
            final ProgressDialog progressDialog = new ProgressDialog(tour.this, R.style.MyAlertDialogStyle);


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
