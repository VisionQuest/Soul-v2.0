package bluepanther.jiljungjuk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Sign_Up extends AppCompatActivity {


    EditText name,password,sclass,section,mobile,reEnterPassword;
    String usn,pass,cls,sec,mob,rep;
    Button register;
    TextView login;
    String date;

    ProgressDialog progressDialog;

    private String Base_url = "https://soul-for-schools.firebaseio.com/";
    private Firebase fb_db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name = (EditText)findViewById(R.id.name);
        password = (EditText)findViewById(R.id.password);
        sclass = (EditText)findViewById(R.id.sclass);
        section = (EditText)findViewById(R.id.section);
        register = (Button)findViewById(R.id.register);
        mobile = (EditText) findViewById(R.id.mobile);
        reEnterPassword = (EditText)findViewById(R.id.reEnterPassword);
        login = (TextView) findViewById(R.id.login);

        Firebase.setAndroidContext(this);
        fb_db = new Firebase(Base_url);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sign_Up.this,Sign_In.class);
                startActivity(intent);
            }
        });
register.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        usn = name.getText().toString();
        pass = password.getText().toString();
        cls = sclass.getText().toString();
        sec = section.getText().toString().toUpperCase();
        mob = mobile.getText().toString();
        rep = reEnterPassword.getText().toString();

        System.out.println("noob");
        boolean a = validate();
        System.out.println("noob3");
        if(a)
            new MyTask().execute();
        else
            Toast.makeText(Sign_Up.this,"Registration Failed",Toast.LENGTH_SHORT).show();
    }
});
    }


    public boolean validate() {
        System.out.println("noob2");
        boolean valid = true;

        if (usn.isEmpty() || usn.length() < 3) {
            name.setError("at least 3 characters");
            valid = false;
        } else {
            name.setError(null);
        }

        if (cls.isEmpty()) {
            sclass.setError("Enter Valid Class");
            valid = false;
        } else {
            sclass.setError(null);
        }
        if (sec.isEmpty()) {
            section.setError("Enter Valid Section");
            valid = false;
        } else {
            section.setError(null);
        }

        if (mob.isEmpty() || mob.length()!=10) {
            mobile.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            mobile.setError(null);
        }

        if (pass.isEmpty() || pass.length() < 4 || pass.length() > 10) {
            password.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            password.setError(null);
        }

        if (rep.isEmpty() || rep.length() < 4 || rep.length() > 10 || !(rep.equals(pass))) {
            reEnterPassword.setError("Password Do not match");
            valid = false;
        } else {
           reEnterPassword.setError(null);
        }

        return valid;
    }


    private class MyTask extends AsyncTask<String, Integer, String> {

        // Runs in UI before background thread is called
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(Sign_Up.this, "Message", "Creating Account...");

        }

        // This is run in a background thread
        @Override
        protected String doInBackground(String... params) {
            // get the string from params, which is an array
            //  Accounts doc=new Accounts(date,fullname,image);

            Cred_Update cred_update = new Cred_Update();
            cred_update.setUsn(usn);
            cred_update.setPass(pass);
            cred_update.setCls(cls);
            cred_update.setSec(sec);
            cred_update.setIdate(date);
            cred_update.setAdate(date);
            cred_update.setVdate(date);
            cred_update.setFdate(date);
            cred_update.setTdate(date);

            fb_db.child("Accounts").child(usn).setValue(cred_update);
            fb_db.child("Classes").child(cls).child(sec).child("Members").child(usn).setValue(cred_update);



            return "SUCCESS";
        }



        // This runs in UI when background thread finishes
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(result.equals("SUCCESS")){
                System.out.println("SUCCESS");

                Toast.makeText(Sign_Up.this,"Account Created",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Sign_Up.this, Sign_In.class);
                System.out.println("HHS NOOB :"+usn);
                System.out.println("HHS NOOB :"+cls);
                System.out.println("HHS NOOB :"+sec);
                startActivity(intent);

            }
            else{
                Toast.makeText(Sign_Up.this,"Failed..",Toast.LENGTH_LONG).show();
            }

            progressDialog.dismiss();
            // Do things like hide the progress bar or change a TextView
        }
    }

}
