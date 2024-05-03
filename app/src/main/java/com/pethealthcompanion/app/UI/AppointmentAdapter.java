package com.pethealthcompanion.app.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pethealthcompanion.app.R;
import com.pethealthcompanion.app.entities.Appointment;

import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {

    private List<Appointment> mAppointments;
    private final Context context;
    private final LayoutInflater mInflater;

    public AppointmentAdapter(Context context){
        mInflater= LayoutInflater.from(context);
        this.context=context;
    }

    public class AppointmentViewHolder extends RecyclerView.ViewHolder {

        private final TextView appointmentItemView;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);

            appointmentItemView=itemView.findViewById(R.id.textView2);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    final Appointment current=mAppointments.get(position);
                    Intent intent=new Intent(context, AppointmentDetails.class);
                    intent.putExtra("appointmentID", current.getAppointmentID());
                    intent.putExtra("petName", current.getPetName());
                    intent.putExtra("appointmentType", current.getAppointmentType());
                    intent.putExtra("veterinaryClinic", current.getVeterinaryClinic());
                    intent.putExtra("notes", current.getNotes());
                    intent.putExtra("appointmentDate", current.getAppointmentDate());
                    intent.putExtra("appointmentTime", current.getAppointmentTime());
                    context.startActivity(intent);

                }
            });
        }
    }

    @NonNull
    @Override
    public AppointmentAdapter.AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView=mInflater.inflate(R.layout.appointment_list_item,parent,false);
        return new AppointmentViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull AppointmentAdapter.AppointmentViewHolder holder, int position) {
        if(mAppointments!=null){
            Appointment current=mAppointments.get(position);
            String name = current.getPetName() + " - " + current.getAppointmentType() + " - " + current.getAppointmentDate();
            holder.appointmentItemView.setText(name);
        }
        else{
            holder.appointmentItemView.setText("No appointment name");
        }
    }

    @Override
    public int getItemCount() {
        if (mAppointments!=null) {
            return mAppointments.size();
        }
        else return 0;
    }
    public void setAppointments(List<Appointment> appointments){
        mAppointments=appointments;
        notifyDataSetChanged();
    }

}

