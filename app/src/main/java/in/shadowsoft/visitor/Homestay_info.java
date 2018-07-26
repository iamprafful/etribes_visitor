package in.shadowsoft.visitor;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

public class Homestay_info extends AppCompatActivity {
    ImageView img;
    TextView hs_title,hs_description;
    String id,name="",lat,lng,contact_name,contact,address,image_loc,description="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homestay_info);
        hs_title=findViewById(R.id.hs_title);
        hs_description=findViewById(R.id.hs_des);
        id=getIntent().getStringExtra("id");
        name=getIntent().getStringExtra("name");
        lat=getIntent().getStringExtra("lat");
        lng=getIntent().getStringExtra("lng");
        contact_name=getIntent().getStringExtra("contact_name");
        contact=getIntent().getStringExtra("contact");
        address=getIntent().getStringExtra("address");
        image_loc=getIntent().getStringExtra("image_loc");
        description=getIntent().getStringExtra("description");
        Log.e("name",name);
        Log.e("Description",description);
        this.setTitle("More about "+name);
        hs_title.setText(String.valueOf(name));
        hs_description.setText(String.valueOf(description));
        img=findViewById(R.id.img);
        new DownloadImageTask((ImageView) findViewById(R.id.img))
                .execute("http://ciphers.shadowsoft.in/tribal/"+image_loc);


    }

    public void call(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+contact));
        startActivity(intent);
    }

    public void direction(View view) {
        String packageName = "com.google.android.apps.maps";
        String query = "google.navigation:q="+lat+","+lng;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(query));
        intent.setPackage(packageName);
        startActivity(intent);
    }

    public void map(View view) {
        String geoUri = "http://maps.google.com/maps?q=loc:" +lat + "," + lng+ " (" + name + ")";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
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
