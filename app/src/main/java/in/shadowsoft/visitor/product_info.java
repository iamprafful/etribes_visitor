package in.shadowsoft.visitor;

import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

public class product_info extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView tv_cost, tv_des, tv_pName, tv_sName, tv_sPhone, tv_sAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.setTitle(getIntent().getStringExtra("pName"));
        tv_cost=findViewById(R.id.tv_productPrice);
        tv_cost.setText("Rs. "+getIntent().getStringExtra("pCost"));
        tv_pName=findViewById(R.id.tv_productName);
        tv_pName.setText(getIntent().getStringExtra("pName"));
        tv_des=findViewById(R.id.tv_fDes);
        tv_des.setText(getIntent().getStringExtra("pDes"));
        tv_sName=findViewById(R.id.seller_name);
        tv_sName.setText("Name: "+getIntent().getStringExtra("oName"));
        tv_sAdd=findViewById(R.id.shop_add);
        tv_sAdd.setText("Shop Address: "+getIntent().getStringExtra("sAdd"));
        tv_sPhone=findViewById(R.id.seller_phone);
        tv_sPhone.setText("Phone: "+getIntent().getStringExtra("oPhone"));
        new product_info.DownloadImageTask((ImageView) findViewById(R.id.img_product))
                .execute("http://ciphers.shadowsoft.in/tribal/"+getIntent().getStringExtra("img_loc"));




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
        getMenuInflater().inflate(R.menu.product_info, menu);
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
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
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
        else if (id==R.id.nav_exit)
        {
            finishAffinity();
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void call_seller(MenuItem item) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+getIntent().getStringExtra("oPhone")));
        startActivity(intent);
    }

    public void launch_call_seller(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+getIntent().getStringExtra("oPhone")));
        startActivity(intent);
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
}
