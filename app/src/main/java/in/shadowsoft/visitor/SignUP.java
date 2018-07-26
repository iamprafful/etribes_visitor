package in.shadowsoft.visitor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SignUP extends AppCompatActivity {

    SQLiteDatabase db;
    int state=0;
    EditText et_name, et_email, et_pwd, et_cpwd;
    String[] Success = new String[1];
    String[] Msg=new String[1];
    String[] id=new String[1];
    String confirm_password;
    String Name_text, email_text, pwd_text,final_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        et_name=findViewById(R.id.et_name);
        et_email=findViewById(R.id.et_email);
        et_pwd=findViewById(R.id.et_pwd);
        et_cpwd=findViewById(R.id.et_cpwd);
        db = openOrCreateDatabase("ciphers_visitor",MODE_PRIVATE,null);
        db.execSQL("CREATE TABLE IF NOT EXISTS visitor(id VARCHAR,full_name VARCHAR, email VARCHAR, password VARCHAR, status VARCHAR);");
        Cursor resultSet = db.rawQuery("Select id from visitor where status='1'",null);
        try {
            resultSet.moveToFirst();
            String id = resultSet.getString(0);
            Intent already_login = new Intent(this, home.class);
            already_login.putExtra("id", id);
            startActivity(already_login);
            finish();
        }
        catch (Exception e)
        {
            //no result
        }
    }

    public void login(View view) {
        Intent intent = new Intent(SignUP.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void validate_and_upload(View view) {
        Name_text=et_name.getText().toString();
        email_text=et_email.getText().toString();
        pwd_text=et_pwd.getText().toString();
        confirm_password=et_cpwd.getText().toString();


        if (Name_text.isEmpty()||email_text.isEmpty()||pwd_text.isEmpty()||confirm_password.isEmpty()){
            Toast validate_error=Toast.makeText(this,"Please fill all fields",Toast.LENGTH_LONG);
            validate_error.show();
        }
        else{
            if (!confirm_password.equals(pwd_text))
            {
                Toast password_error=Toast.makeText(this,"Password not matched",Toast.LENGTH_LONG);
                password_error.show();
            }
            else {

            final_url="http://ciphers.shadowsoft.in/tribal/register_visitor.php?name="+Name_text.replace(" ","%20")+"&email="+email_text+"&pwd="+pwd_text;
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
    public void getServerMsg(String json) throws JSONException {
        if (json.equals("0"))
        {
            Toast.makeText(SignUP.this,"No Internet Connection",Toast.LENGTH_LONG).show();
        }
        else
        {
            Name_text=et_name.getText().toString();
            email_text=et_email.getText().toString();
            pwd_text=et_pwd.getText().toString();

            //creating a json array from the json string
            JSONArray jsonArray = new JSONArray(json);

            //looping through all the elements in json array
            for (int i = 0; i < 1; i++) {

                //getting json object from the json array
                JSONObject obj = jsonArray.getJSONObject(i);

                //getting the name from the json object and putting it inside string array
                id[i]=obj.getString("id");
                Success[i] = obj.getString("Success");
                Msg[i]=obj.getString("Comment");
                Log.e("serverMsg",Msg[i]);
            }
            if(Success[0].equals("1"))
            {
                db.execSQL("INSERT INTO visitor VALUES('"+id[0]+"','"+Name_text+"','"+email_text+"','"+pwd_text+"','1');");
//                Intent successful_login=new Intent(this,MainActivity.class);
//                successful_login.putExtra("id",id[0]);
//                this.startActivity(successful_login);
                finish();
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
            final ProgressDialog progressDialog = new ProgressDialog(SignUP.this, R.style.MyAlertDialogStyle);

            //this method will be called before execution
            //you can display a progress bar or something
            //so that user can understand that he should wait
            //as network operation may take some time
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Creating your Account");
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
