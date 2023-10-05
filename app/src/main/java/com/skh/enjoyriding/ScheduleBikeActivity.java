package com.skh.enjoyriding;

import static com.skh.enjoyriding.utils.Constant.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.skh.enjoyriding.adapters.HoursListAdapter;
import com.skh.enjoyriding.model.Bike;
import com.skh.enjoyriding.model.Booking;
import com.skh.enjoyriding.model.Hours;
import com.skh.enjoyriding.persistence.DBHelper;
import com.skh.enjoyriding.utils.Constant;
import com.skh.enjoyriding.utils.CustomDialog;

import java.util.ArrayList;
import java.util.List;

public class ScheduleBikeActivity extends AppCompatActivity {

    ImageView ivBike;
    TextView tvName;
    TextView tvSnippet;
    TextView tvStartTime;
    TextView tvEndTime;
    RecyclerView recyclerView;
    Button btnReserve;

    DBHelper dbh;
    List<Hours> availableHours;
    HoursListAdapter adapter;
    Bike bike;
    Hours selectedHour;

    String startHourStr;
    String finishHourStr;

    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_bike);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences sharedPreferences = getSharedPreferences(Constant.USER_ID, Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt(Constant.USER_ID, 0);

        init();
    }

    private void init() {
        tvStartTime = findViewById(R.id.tvStartTime);
        tvEndTime = findViewById(R.id.tvEndTime);
        recyclerView = findViewById(R.id.recyclerView);
        btnReserve = (Button) findViewById(R.id.btnReserve);
        btnReserve.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Booking booking = new Booking(userId, bike.getBikeId(), selectedHour.getHourId());

                String status = dbh.insertOrUpdateBooking(booking);

                if (status == null) {
                    String message = "Bike booked successfully for today from " + startHourStr + " to " + finishHourStr;
                    CustomDialog cd = new CustomDialog(message, "Booking Successful", "OK", null);
                    cd.show(getSupportFragmentManager(), "Custom");
                    getAvailableHours();
                    tvStartTime.setText("-:--");
                    tvEndTime.setText("-:--");
                    btnReserve.setEnabled(false);
                }else{
                    CustomDialog cd = new CustomDialog(status, "Booking Error", "OK", null);
                    cd.show(getSupportFragmentManager(), "Custom");
                }
            }
        });
        btnReserve.setEnabled(false);

        //Instantiates the database helper
        dbh = new DBHelper(getBaseContext());

        bike = (Bike) getIntent().getSerializableExtra("BIKE");

        ivBike = findViewById(R.id.ivBike);
        tvName = findViewById(R.id.tvName);
        tvSnippet = findViewById(R.id.tvSnippet);

        ivBike.setImageResource(bike.getDrawableId());
        tvName.setText(bike.getTitle());
        tvSnippet.setText(bike.getSnippet());

        getAvailableHours();
    }

    private void getAvailableHours(){
        availableHours = new ArrayList<>();

        dbh = new DBHelper(getBaseContext());
        Cursor cursor1 = dbh.getAllBikesAvailableHours(bike.getBikeId());

        if(cursor1 == null){
            Toast.makeText(getBaseContext(), "No hour available.", Toast.LENGTH_LONG).show();
        }else{
            cursor1.moveToFirst();

            do{
                Hours hour = new Hours(
                        cursor1.getInt(0),
                        cursor1.getInt(1)
                );
                availableHours.add(hour);
            }while (cursor1.moveToNext());
        }
        cursor1.close();
        dbh.close();
        BindAdapter();
    }

    //The adapter will control how all the data collected from the DB will be shown in the screen
    private void BindAdapter(){
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager(layoutManager);
        // Invokes the constructor of List Adapter with the list of all employees
        adapter=new HoursListAdapter(availableHours, getBaseContext(), new HoursListAdapter.OnHourClickListener() {
            @Override
            public void OnHourClickListener(Hours hour) {
                startHourStr = hour.getHour() + ":00";
                finishHourStr = (hour.getHour()+1 == 24 ? 0 : hour.getHour()+1) + ":00";

                tvStartTime.setText(startHourStr);
                tvEndTime.setText(finishHourStr);
                selectedHour = hour;
                btnReserve.setEnabled(true);
            }
        });
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}