package com.skh.enjoyriding.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skh.enjoyriding.R;
import com.skh.enjoyriding.model.Hours;

import java.util.List;

//Adapter used in the Schedule Bike activity
public class HoursListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    class ViewHolder extends  RecyclerView.ViewHolder{

        public TextView txtHourAvailable;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtHourAvailable = (TextView) itemView.findViewById(R.id.txtHourAvailable);
        }
    }

    private List<Hours> hoursList;
    private OnHourClickListener mListener;

    public HoursListAdapter(List<Hours> hoursList, Context context, OnHourClickListener listener) {
        super();
        this.hoursList = hoursList;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.hour_record_layout, parent, false);
        ViewHolder viewHolder1 = new ViewHolder(v);

        viewHolder1.txtHourAvailable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Hours hour = hoursList.get(viewHolder1.getAdapterPosition());
                mListener.OnHourClickListener(hour);
                notifyItemChanged(viewHolder1.getAdapterPosition());
            }
        });

        return viewHolder1;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Hours hour = hoursList.get(position);

        String hourStr = String.valueOf(hour.getHour()) + ":00 to " + String.valueOf(hour.getHour() + 1) + ":00";

        ((ViewHolder) holder).txtHourAvailable.setText(hourStr);
    }

    @Override
    public int getItemCount() {
        return hoursList.size();
    }

    public interface OnHourClickListener {
        void OnHourClickListener( Hours hour );
    }
}
