package com.skh.enjoyriding.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skh.enjoyriding.R;
import com.skh.enjoyriding.model.Bike;
import com.skh.enjoyriding.model.Booking;
import com.skh.enjoyriding.model.Hours;
import com.skh.enjoyriding.persistence.DBHelper;

import java.util.ArrayList;
import java.util.List;

//Adapter used in the Account activity
public class BookingListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    class ViewHolder extends  RecyclerView.ViewHolder{

        public TextView txtBikeTitle, txtBookingHour;
        public ImageButton btnCancelBooking;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtBikeTitle = (TextView) itemView.findViewById(R.id.txtBikeTitle);
            txtBookingHour = (TextView) itemView.findViewById(R.id.txtBookingHour);
            btnCancelBooking = (ImageButton) itemView.findViewById(R.id.btnCancelBooking);
        }
    }

    private List<Booking> bookingList;
    private DBHelper dbh;

    public BookingListAdapter(List<Booking> bookingList, Context context) {
        super();
        this.bookingList = bookingList;

        //Instantiates the database helper
        dbh = new DBHelper(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_record_layout, parent, false);
        ViewHolder viewHolder1 = new BookingListAdapter.ViewHolder(v);

        viewHolder1.btnCancelBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Booking booking = bookingList.get(viewHolder1.getAdapterPosition());

                dbh.deleteBooking(booking.getBookingId());

                bookingList.remove(viewHolder1.getAdapterPosition());
                notifyItemRemoved(viewHolder1.getAdapterPosition());
                notifyItemRangeChanged(viewHolder1.getAdapterPosition(), bookingList.size());
            }
        });
        return viewHolder1;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Booking booking = bookingList.get(position);
        Bike bike = dbh.getBikeById(booking.getBikeId());
        Hours hour = dbh.getHourById(booking.getStartHour());

        String hourStr = hour.getHour() + ":00 to " + (hour.getHour() + 1) + ":00";

        ((ViewHolder) holder).txtBikeTitle.setText(bike.getTitle());
        ((ViewHolder) holder).txtBookingHour.setText(hourStr);
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }
}
