package com.skh.enjoyriding;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.skh.enjoyriding.adapters.BookingListAdapter;
import com.skh.enjoyriding.adapters.HoursListAdapter;
import com.skh.enjoyriding.model.Bike;
import com.skh.enjoyriding.model.Booking;
import com.skh.enjoyriding.model.Hours;
import com.skh.enjoyriding.persistence.DBHelper;
import com.skh.enjoyriding.utils.Constant;

import java.util.ArrayList;
import java.util.List;

public class AccountFragment extends Fragment {

    View v;
    TextView txtUsersBookings;
    RecyclerView recyclerView;
    Button btnLogout;
    BookingListAdapter adapter;

    private DBHelper dbh;
    private List<Booking> bookingList;

    private int userId;
    private String username;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_account, container, false);

        //Instantiates the database helper
        dbh = new DBHelper(getActivity());

        txtUsersBookings = v.findViewById(R.id.txtUsersBookings);
        recyclerView = v.findViewById(R.id.recyclerViewBooking);
        btnLogout = v.findViewById(R.id.btnLogout);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constant.USER_ID, Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt(Constant.USER_ID, 0);
        username = sharedPreferences.getString(Constant.USER_NAME, "");

        txtUsersBookings.setText(username + "'s Bookings");

        getBookings();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        return v;
    }

    private void getBookings(){
        bookingList = new ArrayList<>();

        dbh = new DBHelper(getActivity());
        Cursor cursor1 = dbh.getBookingByUser(userId);

        if(!cursor1.moveToFirst()){
            Toast.makeText(getActivity(), "No bike record found.", Toast.LENGTH_LONG).show();
        }else{
            cursor1.moveToFirst();

            do{
                Booking booking = new Booking(
                        cursor1.getInt(0),
                        cursor1.getInt(1),
                        cursor1.getInt(2),
                        cursor1.getInt(3)
                );
                bookingList.add(booking);
            }while (cursor1.moveToNext());
        }
        cursor1.close();
        dbh.close();
        BindAdapter();
    }

    //The adapter will control how all the data collected from the DB will be shown in the screen
    private void BindAdapter(){
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        // Invokes the constructor of List Adapter with the list of all employees
        adapter = new BookingListAdapter(bookingList, getContext());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }
}