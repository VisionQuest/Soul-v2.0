package bluepanther.jiljungjuk;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import bluepanther.jiljungjuk.Notifications.Videonoti;
import bluepanther.jiljungjuk.Notifications.Videonoti2;
import bluepanther.jiljungjuk.Reports_Grid.CustomAdapter;

/**
 * Created by Hariharsudan on 11/3/2016.
 */

public class NotifyVideo extends Fragment {

    ListView mylistview,mylistview2;
    public CustomAdapter adapter,adapter2;
    public String member_names[],member_names2[];
    private String Base_url = "https://soul-for-schools.firebaseio.com/";
    private Firebase fb_db;
    String date;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.noti_view,container,false);
        Firebase.setAndroidContext(getActivity());
        fb_db = new Firebase(Base_url);

        mylistview=(ListView)v.findViewById(R.id.listView);
        mylistview2 = (ListView) v.findViewById(R.id.listView2);
        try {
            if(Videonoti.state) {

                member_names = Arrays.copyOf(Videonoti.members, Videonoti.members.length);
                adapter = new CustomAdapter(getActivity(), Videonoti.row);
                mylistview.setAdapter(adapter);
            }
            if(Videonoti2.state) {
                member_names2 = Arrays.copyOf(Videonoti2.members, Videonoti2.members.length);
                adapter2 = new CustomAdapter(getActivity(), Videonoti2.row);
                mylistview2.setAdapter(adapter2);
            }
        }
        catch(Exception e)
        {

        }
        //   mylistview.setOnItemClickListener(Timeline.this);
        date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());



        mylistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String res=member_names[position];

                System.out.println("Downloading"+res);

                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Classes").child(CurrentUser.sclass).child(CurrentUser.ssec).child("Videos").child(res);

                System.out.println("Storage refference : " + storageReference);


                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        System.out.println("NOOB");
                        Intent i=new Intent();
                        i.setAction(Intent.ACTION_VIEW);
                        i.setDataAndType(uri,"video/*");
                        startActivity(i);
//                                            Picasso.with(Reports.this).load(uri).fit().centerCrop().into(imgg);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                        System.out.println("sad" + exception);
                    }
                });
            }
        });


        mylistview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String res=member_names2[position];

                System.out.println("Downloading"+res);

                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Classes").child(CurrentUser.sclass).child(CurrentUser.ssec).child("Videos").child(res);

                System.out.println("Storage refference : " + storageReference);


                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        System.out.println("NOOB");
                        Intent i=new Intent();
                        i.setAction(Intent.ACTION_VIEW);
                        i.setDataAndType(uri,"video/*");
                        startActivity(i);
//                                            Picasso.with(Reports.this).load(uri).fit().centerCrop().into(imgg);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                        System.out.println("sad" + exception);
                    }
                });
            }
        });
        new MyTask().execute();

        return v;

    }
    private class MyTask extends AsyncTask<String, Integer, String> {

        // Runs in UI before background thread is called
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // progressDialog = ProgressDialog.show(Sign_Up.this, "Message", "Creating Account...");

        }

        // This is run in a background thread
        @Override
        protected String doInBackground(String... params) {
            // get the string from params, which is an array
            //  Accounts doc=new Accounts(date,fullname,image);
            Cred_Update cred_update = new Cred_Update();
            cred_update.setUsn(CurrentUser.user);
            cred_update.setPass(CurrentUser.pass);
            cred_update.setCls(CurrentUser.sclass);
            cred_update.setSec(CurrentUser.ssec);
            cred_update.setIdate(CurrentUser.sidate);
            cred_update.setFdate(CurrentUser.sfdate);
            cred_update.setAdate(CurrentUser.sadate);
            cred_update.setTdate(CurrentUser.stdate);
            cred_update.setVdate(date);
            CurrentUser.svdate=date;

            fb_db.child("Accounts").child(CurrentUser.user).setValue(cred_update);
            fb_db.child("Classes").child(CurrentUser.sclass).child(CurrentUser.ssec).child("Members").child(CurrentUser.user).setValue(cred_update);



            return "SUCCESS";
        }



        // This runs in UI when background thread finishes
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // Do things like hide the progress bar or change a TextView
        }
    }

}

