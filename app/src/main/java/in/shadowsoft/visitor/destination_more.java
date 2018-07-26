package in.shadowsoft.visitor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class destination_more extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,OnMapReadyCallback {
    String dTitle;
    String sDes;
    String dAdd;
    String img_loc;
    String fDes,lat,lng, feedback_title_text, feedback_text, final_url, destination_id, user_id;
    private GoogleMap mMap;
    ImageView img;
    TextView d_title,s_des,d_add,tv_fDes;
    RatingBar ratingBar;
    float rating_count;
    SQLiteDatabase db;
    String id="0";
    EditText feedback, feedback_title;
    String[] Success = new String[1];
    String[] Msg=new String[1];
    String[] review_id=new String[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination_more);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        db = openOrCreateDatabase("ciphers_visitor",MODE_PRIVATE,null);
        db.execSQL("CREATE TABLE IF NOT EXISTS visitor(id VARCHAR,full_name VARCHAR, email VARCHAR, password VARCHAR, status VARCHAR);");
        d_title=findViewById(R.id.d_title);
        dTitle=getIntent().getStringExtra("dTitle");
        this.setTitle(dTitle);
        d_title.setText(dTitle);
        s_des=findViewById(R.id.s_des);
        sDes=getIntent().getStringExtra("sDes");
        s_des.setText(sDes);
        d_add=findViewById(R.id.d_add);
        dAdd=getIntent().getStringExtra("dAdd");
        d_add.setText(dAdd);
        img_loc=getIntent().getStringExtra("img_loc");
        tv_fDes=findViewById(R.id.tv_fDes);
        fDes=getIntent().getStringExtra("fDes");
        tv_fDes.setText(fDes);
        lat=getIntent().getStringExtra("lat");
        lng=getIntent().getStringExtra("lng");
        img=findViewById(R.id.img_dest);
        new destination_more.DownloadImageTask((ImageView) findViewById(R.id.img_dest))
                .execute("http://ciphers.shadowsoft.in/tribal/"+img_loc);
        ratingBar=findViewById(R.id.rating_bar);
         feedback_title=findViewById(R.id.et_title);
         feedback=findViewById(R.id.et_feedback);
        destination_id=getIntent().getStringExtra("id");


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        getMenuInflater().inflate(R.menu.destination_more, menu);
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



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng location = new LatLng(Double.parseDouble(lat),  Double.parseDouble(lng));
        mMap.addMarker(new MarkerOptions().position(location).title(dTitle));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
    }

    public void launch_map(View view) {String packageName = "com.google.android.apps.maps";
        String query = "google.navigation:q="+lat+","+lng;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(query));
        intent.setPackage(packageName);
        startActivity(intent);
    }
    public void checkLogin(){

        Cursor resultSet = db.rawQuery("Select id from visitor where status='1'",null);
        try {
            resultSet.moveToFirst();
            id = resultSet.getString(0);
        }
        catch (Exception e)
        {
            //no result
        }
    }

    public void submit_feedback(View view) {
        checkLogin();
        if(id.equals("0"))
        {
            Intent login = new Intent(destination_more.this, SignUP.class);
            startActivity(login);
        }
        else{
            rating_count=ratingBar.getRating();
            feedback_title_text=feedback_title.getText().toString();
            feedback_text=feedback.getText().toString();
            if(feedback_text.equals("")||feedback_title_text.equals("")){
                if(rating_count<0.5)
                {
                    Toast.makeText(this, "Minimum length of rating is 0.5", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(this, "Please fill all fields", Toast.LENGTH_LONG).show();
                }
            }
            else{
                final_url="http://ciphers.shadowsoft.in/tribal/add_review.php?title="+feedback_title_text.replace(" ","%20")+"&feedback="+feedback_text.replace(" ","%20")+"&ratings="+String.valueOf(rating_count)+"&service_id="+destination_id+"&service_code=1&user_id="+id;
                try {
                    Log.e("sql",final_url);
                    getJSON(final_url);
                }
                catch (Exception ex)
                {
                    Toast e=Toast.makeText(this,ex.getMessage(),Toast.LENGTH_LONG);
                    e.show();
                }
            }
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
    public void getServerMsg(String json) throws JSONException {
        if (json.equals("0"))
        {
            Toast.makeText(destination_more.this,"No Internet Connection",Toast.LENGTH_LONG).show();
        }
        else
        {
//            Name_text=et_name.getText().toString();
//            email_text=et_email.getText().toString();
//            pwd_text=et_pwd.getText().toString();

            //creating a json array from the json string
            JSONArray jsonArray = new JSONArray(json);

            //looping through all the elements in json array
            for (int i = 0; i < 1; i++) {

                //getting json object from the json array
                JSONObject obj = jsonArray.getJSONObject(i);

                //getting the name from the json object and putting it inside string array
                review_id[i]=obj.getString("id");
                Success[i] = obj.getString("Success");
                Msg[i]=obj.getString("Comment");
                Log.e("serverMsg",Msg[i]);
            }
            if(Success[0].equals("1"))
            {
//                db.execSQL("INSERT INTO visitor VALUES('"+id[0]+"','"+Name_text+"','"+email_text+"','"+pwd_text+"','1');");
////                Intent successful_login=new Intent(this,MainActivity.class);
////                successful_login.putExtra("id",id[0]);
////                this.startActivity(successful_login);
//                finish();
                Toast err=Toast.makeText(this,"Success",Toast.LENGTH_LONG);
                err.show();
            }
            else {
                Toast err=Toast.makeText(this,Msg[0],Toast.LENGTH_LONG);
                err.show();
            }
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
            final ProgressDialog progressDialog = new ProgressDialog(destination_more.this, R.style.MyAlertDialogStyle);

            //this method will be called before execution
            //you can display a progress bar or something
            //so that user can understand that he should wait
            //as network operation may take some time
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Adding Feedback");
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
                    getServerMsg(s);
                } catch (JSONException ex) {
                    ex.printStackTrace();
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
                    String error="0";
                    return error;
                }

            }
        }

        //creating asynctask object and executing it
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }
}
