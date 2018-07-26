package in.shadowsoft.visitor;

import android.*;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Destination extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ListView lv_Dest;
    int count;
    String[] dTitle, destination_id;
    String[] sDes;
    String[] dAdd;
    String[] img_loc;
    String[] fDes,lat,lng, review_status;
    String id="0";
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        lv_Dest=findViewById(R.id.destinations);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        db = openOrCreateDatabase("ciphers_visitor",MODE_PRIVATE,null);
        db.execSQL("CREATE TABLE IF NOT EXISTS visitor(id VARCHAR,full_name VARCHAR, email VARCHAR, password VARCHAR, status VARCHAR);");
        Cursor resultSet = db.rawQuery("Select id from visitor where status='1'",null);
        try {
            resultSet.moveToFirst();
            id = resultSet.getString(0);
        }
        catch (Exception e)
        {
            //no result
        }
        getJSON("http://ciphers.shadowsoft.in/tribal/get_destination.php&id="+id);

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

        getMenuInflater().inflate(R.menu.destination, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
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
//            home obj=new home();
//            obj.getJSON("http://ciphers.shadowsoft.in/tribal/homestay_map.php", 2);
        }
        else if (id==R.id.nav_Destination)
        {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
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

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


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


            convertView = getLayoutInflater().inflate(R.layout.destination_list, null);
            TextView title_tv = convertView.findViewById(R.id.dest_title);
            TextView tv_sDes = convertView.findViewById(R.id.dest_sDes);
            TextView tv_dAdd = convertView.findViewById(R.id.dest_add);
            ImageView dest_img = convertView.findViewById(R.id.dest_img);
            title_tv.setText(dTitle[position]);
            tv_sDes.setText(sDes[position]);
            tv_dAdd.setText(dAdd[position]);
            new Destination.DownloadImageTask((ImageView) dest_img)
                    .execute("http://ciphers.shadowsoft.in/tribal/"+img_loc[position]);

            return convertView;
        }



    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;


        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);


        }
    }
        private void loadIntoListView(String json) throws JSONException {

            //creating a json array from the json string
            if(json.equals("0")){
                Toast.makeText(Destination.this,"No internet Connection",Toast.LENGTH_LONG).show();
                finish();
            }
            else {

                JSONArray jsonArray = new JSONArray(json);

                count=jsonArray.length();
                review_status=new String[count];
                destination_id=new String[count];
                dTitle=new String[count];
                sDes=new  String[count];
                dAdd=new String[count];
                img_loc=new String[count];
                fDes=new String[count];
                lat=new String[count];
                lng=new String[count];

                //creating a string array for listview
                //String[] heroes = new String[jsonArray.length()];

                //looping through all the elements in json array
                for (int i = 0; i < jsonArray.length(); i++) {

                    //getting json object from the json array
                    JSONObject obj = jsonArray.getJSONObject(i);

                    //getting the name from the json object and putting it inside string array
                    dTitle[i] = obj.getString("name");
                    sDes[i] = obj.getString("Short_description");
                    dAdd[i] = obj.getString("address");
                    img_loc[i]=obj.getString("image_loc");
                    fDes[i]=obj.getString("description");
                    lat[i]=obj.getString("lat");
                    lng[i]=obj.getString("lng");
                    destination_id[i]=obj.getString("id");
                    review_status[i]=obj.getString("review_status");

                }

                //the array adapter to load data into list
                Destination.customAdapter adpt = new Destination.customAdapter();
                lv_Dest.setAdapter(adpt);
                lv_Dest.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Intent launch_more_info=new Intent(Destination.this,destination_more.class);
                        launch_more_info.putExtra("id",destination_id[position]);
                        launch_more_info.putExtra("dTitle",dTitle[position]);
                        launch_more_info.putExtra("sDes",sDes[position]);
                        launch_more_info.putExtra("dAdd",dAdd[position]);
                        launch_more_info.putExtra("img_loc",img_loc[position]);
                        launch_more_info.putExtra("fDes",fDes[position]);
                        launch_more_info.putExtra("lat",lat[position]);
                        launch_more_info.putExtra("lng",lng[position]);
                        launch_more_info.putExtra("review_status",review_status[position]);
                        startActivity(launch_more_info);
                        Destination.this.overridePendingTransition(0,0);

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
            final ProgressDialog progressDialog = new ProgressDialog(Destination.this, R.style.MyAlertDialogStyle);


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
