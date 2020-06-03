package com.example.dentistapplication.ui.pHome;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dentistapplication.R;

import java.util.List;

public class AdapterDates extends RecyclerView.Adapter<AdapterDates.MyHolder> {

    Context context;
    List<ModelDates> datesList;

    public AdapterDates(Context context, List<ModelDates> datesList) {
        this.context = context;
        this.datesList = datesList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //wyświetlenie layoutu
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dates_element, parent, false);
        return  new AdapterDates.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterDates.MyHolder myHolder, int i) {
        String free = datesList.get(i).getFree();

            //pobieranie danych do wyświetlenia na liście
            String day = datesList.get(i).getDate();
            String hour = datesList.get(i).getHour();
            Log.v("tak", "jestem tu");

            //ustawianie danych lekarza
            myHolder.mDay.setText(day);
            myHolder.mHour.setText(hour);

            //wyświetlenie profilu wybranego lekarza w momencie gdy zostanie kliknięty
            myHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, doctorProfile.class);
                    context.startActivity(intent);
                }
            });

    }

    @Override
    public int getItemCount() {
        return datesList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        TextView mDay, mHour;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            //inicjacja widoków
            mDay = itemView.findViewById(R.id.day);
            mHour = itemView.findViewById(R.id.hour);
        }
    }
}
