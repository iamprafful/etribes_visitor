package in.shadowsoft.visitor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText pwd;
    CheckBox show;
    Button login;
    EditText email;
    SQLiteDatabase db;
    String final_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pwd=findViewById(R.id.pwd);
        show=findViewById(R.id.showCheck);
        login=findViewById(R.id.login);
        email=findViewById(R.id.email);
        try {
            db = openOrCreateDatabase("ciphers_visitor",MODE_PRIVATE,null);
            db.execSQL("CREATE TABLE IF NOT EXISTS visitor(id VARCHAR,full_name VARCHAR, email VARCHAR, password VARCHAR, status VARCHAR);");
            Cursor resultSet = db.rawQuery("Select id from visitor where status='1'",null);
            resultSet.moveToFirst();
            String id = resultSet.getString(0);
            Intent already_login = new Intent(this, home.class);
            already_login.putExtra("id", id);
            startActivity(already_login);
            finish();
        }
        catch (Exception e)
        {
            //Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }

    }

    public void showPwd(View view)
    {
        pwd.setSelection(pwd.getText().length());

        if (show.isChecked())
        {
            pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
            Toast viible=Toast.makeText(this, "Password is Visible",Toast.LENGTH_LONG);
            viible.show();

        }
        else{
            pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            Toast hide=Toast.makeText(this, "Password is Hidden",Toast.LENGTH_LONG);
            hide.show();

        }
        pwd.setSelection(pwd.getText().length());
        pwd.requestFocus();
    }

    public void Validate(View view) {
        String password=pwd.getText().toString();
        String em=email.getText().toString();
        if (password.isEmpty() && em.isEmpty())
        {
            pwd.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            pwd.setHintTextColor(ColorStateList.valueOf(Color.RED));
            email.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            email.setHintTextColor(ColorStateList.valueOf(Color.RED));
            Toast t=Toast.makeText(this, "Please Enter Email & Password",Toast.LENGTH_SHORT);
            t.show();
            email.requestFocus();
        }
        else if (em.isEmpty())
        {
            email.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            email.setHintTextColor(ColorStateList.valueOf(Color.RED));
            Toast t1=Toast.makeText(this, "Please Enter Email",Toast.LENGTH_SHORT);
            t1.show();
            email.requestFocus();
        }
        else if (password.isEmpty())
        {
            pwd.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            pwd.setHintTextColor(ColorStateList.valueOf(Color.RED));
            Toast t=Toast.makeText(this, "Please Enter Password",Toast.LENGTH_SHORT);
            t.show();
            pwd.requestFocus();
        }
        else{
            final_url = "http://ciphers.shadowsoft.in/tribal/visitor_auth.php?email=" + em + "&password=" + password;
            try {
                Log.e("sql", final_url);
                getJSON(final_url);
            } catch (Exception ex) {
                Toast e = Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG);
                e.show();
            }

        }

    }
    public void getServerMsg(String json) throws JSONException {
        if (json.equals("0"))
        {
            Toast.makeText(MainActivity.this,"No Internet Connection",Toast.LENGTH_LONG).show();
        }
        else{

            //creating a json array from the json string
            JSONArray jsonArray = new JSONArray(json);

            //looping through all the elements in json array
            String[] id = new String[1];
            String[] name = new String[1];
            String[] phone = new String[1];
            String[] password = new String[1];

            for (int i = 0; i < 1; i++) {

                //getting json object from the json array
                JSONObject obj = jsonArray.getJSONObject(i);

                //getting the name from the json object and putting it inside string array
                id[i] = obj.getString("id");
                name[i] = obj.getString("Full_name");
                phone[i] = obj.getString("email");
                password[i] = obj.getString("password");

            }
            if (id[0].equals("404")) {
                Toast.makeText(MainActivity.this, "Invalid Phone or password", Toast.LENGTH_LONG).show();

            } else {
                db.execSQL("CREATE TABLE IF NOT EXISTS visitor(id VARCHAR,full_name VARCHAR, email VARCHAR, password VARCHAR, status VARCHAR);");
                Cursor resultSet = db.rawQuery("Select count(*) from visitor where id='"+id[0]+"'",null);
                resultSet.moveToFirst();
                int count = resultSet.getInt(0);
                if (count==0)
                {
                    db.execSQL("INSERT INTO visitor VALUES('"+id[0]+"','"+name[0]+"','"+phone[0]+"','"+password[0]+"','1');");
                }
                else
                {
                    db.execSQL("Update visitor set status='1' where id='"+id[0]+"'");
                }
                finish();

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
            final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this, R.style.MyAlertDialogStyle);

            //this method will be called before execution
            //you can display a progress bar or something
            //so that user can understand that he should wait
            //as network operation may take some time
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Authenticating");
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
                    return "0";
                }

            }
        }

        //creating asynctask object and executing it
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

    public void signup(View view) {
        Intent intent = new Intent(MainActivity.this, SignUP.class);
        startActivity(intent);
        finish();

    }
}
