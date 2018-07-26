package in.shadowsoft.visitor;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class homestay extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {
    private GoogleMap mMap;
    android.support.v7.widget.CardView info;
    Button know_more;
    String title;
    String temp;
    LatLng position;
    TextView hs_title, hs_add;
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homestay);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        know_more = findViewById(R.id.know_more);
        info = findViewById(R.id.card_info);
        info.setVisibility(View.INVISIBLE);
        hs_title = findViewById(R.id.hs_title);
        hs_add = findViewById(R.id.hs_add);
        setSupportActionBar(toolbar);


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

        if (id == R.id.nav_home) {
            Intent intent = new Intent(this, home.class);
            startActivity(intent);
            this.overridePendingTransition(0, 0);
        } else if (id == R.id.nav_events) {
            Intent intent = new Intent(this, Events.class);
            startActivity(intent);
            this.overridePendingTransition(0, 0);
        } else if (id == R.id.nav_buy) {
            Intent intent = new Intent(this, buy.class);
            startActivity(intent);
            this.overridePendingTransition(0, 0);
        } else if (id == R.id.nav_Homestays) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_Destination) {
            Intent intent = new Intent(this, Destination.class);
            startActivity(intent);
            this.overridePendingTransition(0, 0);
        } else if (id == R.id.nav_Guide) {
            Intent intent = new Intent(this, tour.class);
            startActivity(intent);
            this.overridePendingTransition(0, 0);
        } else if (id==R.id.nav_outlet)
        {
            Intent intent= new Intent(this,food_outlet.class);
            startActivity(intent);
            this.overridePendingTransition(0,0);
        }
        else if (id == R.id.nav_exit) {
            finishAffinity();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onMapReady(GoogleMap googleMap) {


        String[] id = getIntent().getStringArrayExtra("id");
        String[] name = getIntent().getStringArrayExtra("name");
        String[] lat = getIntent().getStringArrayExtra("lat");
        String[] lng = getIntent().getStringArrayExtra("lng");
        String[] contact_name = getIntent().getStringArrayExtra("contact_name");
        String[] contact = getIntent().getStringArrayExtra("contact");
        final String[] address = getIntent().getStringArrayExtra("address");
        String[] image_loc = getIntent().getStringArrayExtra("image_loc");
        String[] description = getIntent().getStringArrayExtra("description");
        int length_of_array = getIntent().getIntExtra("array_length", 0);
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                info.setVisibility(View.INVISIBLE);
            }
        });
        Drawable myDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.homesttay_marker, null);
        Bitmap markerBitmap = ((BitmapDrawable)myDrawable).getBitmap();

        for (int i=0; i<length_of_array; i++)
        {
            LatLng loc = new LatLng(Double.parseDouble(lat[i]),Double.parseDouble(lng[i]));
            Marker marker= mMap.addMarker(new MarkerOptions().position(loc).title(name[i]).snippet(contact_name[i]).icon(BitmapDescriptorFactory.fromBitmap(markerBitmap)));
            marker.setTag(loc);

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    position = (LatLng) marker.getTag();
                    title=marker.getTitle();
                    info.setVisibility(View.VISIBLE);
                    hs_title.setText(""+title);
                    index=searchDetail(position.longitude);
                    hs_add.setText(""+address[index]);
                    return false;
                }
            });
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc,12.0f));
        }

    }
    public int searchDetail(Double lngVal){
        String[] lng=getIntent().getStringArrayExtra("lng");
        int pos = 0;
        int length_of_array=getIntent().getIntExtra("array_length",0);
        for (int i=0; i<length_of_array;i++)
        {
            if(lngVal==Double.parseDouble(lng[i])){
                pos=i;
                break;
            }
        }
        return pos;
    }

    public void knowMore(View view) {
        String[] id=getIntent().getStringArrayExtra("id");
        String[] name=getIntent().getStringArrayExtra("name");
        String[] lat=getIntent().getStringArrayExtra("lat");
        String[] lng=getIntent().getStringArrayExtra("lng");
        String[] contact_name=getIntent().getStringArrayExtra("contact_name");
        String[] contact=getIntent().getStringArrayExtra("contact");
        final String[] address=getIntent().getStringArrayExtra("address");
        String[] image_loc=getIntent().getStringArrayExtra("image_loc");
        String[] description=getIntent().getStringArrayExtra("description");
        Intent launc_hs_info = new Intent(this,Homestay_info.class);
        launc_hs_info.putExtra("id",id[index]);
        launc_hs_info.putExtra("name",name[index]);
        launc_hs_info.putExtra("lat",lat[index]);
        launc_hs_info.putExtra("lng",lng[index]);
        launc_hs_info.putExtra("contact_name",contact_name[index]);
        launc_hs_info.putExtra("contact",contact[index]);
        launc_hs_info.putExtra("address",address[index]);
        launc_hs_info.putExtra("image_loc",image_loc[index]);
        launc_hs_info.putExtra("description",description[index]);
        this.startActivity(launc_hs_info);

    }

    public void direction(View view) {

//      String geoUri = "http://maps.google.com/maps?q=loc:" + position.latitude + "," + position.longitude + " (" + title + ")";
        String packageName = "com.google.android.apps.maps";
        String query = "google.navigation:q="+position.latitude+","+position.longitude;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(query));
        intent.setPackage(packageName);
        startActivity(intent);
    }





}

