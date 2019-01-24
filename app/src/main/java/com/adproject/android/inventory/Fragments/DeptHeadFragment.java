package com.adproject.android.inventory.Fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.adproject.android.inventory.Adapter.UseraAdpter;
import com.adproject.android.inventory.Entity.User;
import com.adproject.android.inventory.R;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DeptHeadFragment extends Fragment {
    Spinner spinner;
    User u;
    String userid;
    String selcetid;
    int Year1 = 1990, Month1=1,Day1=1;
    int Year2 = 2200, Month2 =1 ,Day2=1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_depthead, container, false);
        userid = (String)getArguments().getSerializable("userid");
        GetEmployee(userid);
        GetHead(userid);
        return(v);
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle("Dept Head");
        spinner = (Spinner) getActivity().findViewById(R.id.spinner2);
        if(spinner!=null) {
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    u = (User) parent.getItemAtPosition(position);
                    selcetid = u.get("UserID");
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }

            });
        };
        Button btnStart = getActivity().findViewById(R.id.buttonSetStartTime);
        Button btnEnd = getActivity().findViewById(R.id.buttonSetEndTime);
        Button save = getActivity().findViewById(R.id.buttonDeptHeadSave);

        final TextView startdate = getActivity().findViewById(R.id.editStart);


        final TextView enddate = getActivity().findViewById(R.id.editEnd);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Year1 =1990;Month1 =01;Day1=01;
                showDatePickerDialog(getActivity(),startdate,0,Year2,Month2,Day2);

            }
        });
        btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Year2 =2200;Month2 =01;Day2=01;
                showDatePickerDialog(getActivity(),enddate,1,Year1,Month1,Day1);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(u!=null){
                    Save(selcetid);
                }
                else {
                    Toast.makeText(getActivity().getApplicationContext(), "wait...",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }




    void showDatePickerDialog(Activity activity, final TextView textViewMM, final int status, int year, int month, int day) {
        DatePickerDialog datePickerDialog =  new DatePickerDialog(activity ,new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {
                textViewMM.setText((monthOfYear+1)+"/"+dayOfMonth+"/"+year);
                if(status==0){
                    Year1=year;Month1=monthOfYear;Day1=dayOfMonth;
                }
                else if (status==1){
                    Year2=year;Month2=monthOfYear;Day2=dayOfMonth;
                }

            }
        }
                ,2019
                ,1
                ,1);
        Calendar calendarmin =Calendar.getInstance();
        calendarmin.set(Year1,Month1,Day1);
        Calendar calendarmax = Calendar.getInstance();
        calendarmax.set(Year2,Month2,Day2);

        datePickerDialog.getDatePicker().setMinDate(calendarmin.getTimeInMillis());
        datePickerDialog.getDatePicker().setMaxDate(calendarmax.getTimeInMillis());

        datePickerDialog.show();
    }

    void GetHead(String id){
        new AsyncTask<String,Void,User>() {
            @Override
            protected User doInBackground(String... voids) {
                List<User> users = User.ReadUser(voids[0]);
                User head = new User("","","","","","");
                for(User u : users){
                    if(u.get("UserType").equals("Manager")){
                        head=u;
                    }
                }
                return head;
            }

            @Override
            protected void onPostExecute(User user) {
               try{
                TextView textHead =getActivity().findViewById(R.id.textCurrentHead);
                textHead.setText(user.get("Name"));
               }catch (Exception e){
                   e.printStackTrace();
               }
            }
        }.execute(id);
    }


    void GetEmployee(String id){
        new AsyncTask<String,Void,List<User>>() {
            @Override
            protected List<User> doInBackground(String... voids) {
                List<User> users1 = User.ReadUser(voids[0]);
                List<User> users = new ArrayList<>();
                for(User u : users1){
                    if(!u.get("UserType").equals("Manager")){
                        users.add(u);
                    }
                }
                return users;
            }

            @Override
            protected void onPostExecute(List<User> users) {
                try {
                    UseraAdpter adapter = new UseraAdpter(getActivity(), users);
                    spinner.setAdapter(adapter);
                }
                catch (Exception e){

                    e.printStackTrace();
                }
            }
        }.execute(id);
    }

    void Save(String id){
        String url = "https://inventoryaandroid.azurewebsites.net/DepManager/saveNewRep?dropdown1="+id;
        new AsyncTask<String, Void,Boolean>(){
            @Override
            protected Boolean doInBackground(String... strings) {
                try {
                    URL u = new URL(strings[0]);
                    HttpURLConnection conn = (HttpURLConnection) u.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();
                    String response = conn.getResponseMessage();
                    conn.disconnect();
                    if(response.equals("OK")) {

                        return true;
                    }
                    else {
                        return false;
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();return false;
                } catch (IOException e) {
                    e.printStackTrace();return false;
                }

            }
            @Override
            protected void onPostExecute(Boolean aBoolean) {
                //super.onPostExecute(aBoolean);
                Toast.makeText(getActivity().getApplicationContext(), aBoolean.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        }.execute(url);
    }
}
