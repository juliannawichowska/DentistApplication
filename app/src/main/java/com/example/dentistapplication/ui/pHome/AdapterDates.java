package com.example.dentistapplication.ui.pHome;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dentistapplication.R;
import com.example.dentistapplication.ui.pCalendar.pCalendarFragment;

import java.util.List;

public class AdapterDates extends RecyclerView.Adapter<AdapterDates.MyHolder> {

    private Context mcontext;
    List<ModelDates> datesList;

    public AdapterDates(Context context, List<ModelDates> datesList) {
        this.mcontext = context;
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
    public void onBindViewHolder(@NonNull AdapterDates.MyHolder myHolder, final int i) {
            final String free = datesList.get(i).getFree();

            //pobieranie danych do wyświetlenia na liście
            final String day = datesList.get(i).getDate();
            final String hour = datesList.get(i).getHour();
            final String uidDoctor = datesList.get(i).getUid();
            final String keyVisit = datesList.get(i).getKey();
            Log.v("tak", "jestem tu");

            //ustawianie danych lekarza
            myHolder.mDay.setText(day);
            myHolder.mHour.setText(hour);

            if(free.equals("false")){
                myHolder.mFree.setText("Wizyta zajęta");
                
            }

            //wyświetlenie profilu wybranego lekarza w momencie gdy zostanie kliknięty
            myHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(free.equals("true")) {
                        Log.v("TAKK", "dotre");
                        Context mcontext = v.getContext();
                        Intent intent = new Intent(mcontext,
                                pVisit.class);
                        intent.putExtra("day", day);
                        intent.putExtra("hour", hour);
                        intent.putExtra("uidDoctor", uidDoctor);
                        intent.putExtra("keyVisit", keyVisit);
                        mcontext.startActivity(intent);
                    } else {
                        Context mcontext = v.getContext();
                        Toast.makeText(mcontext, "Ta wizyta jest zajęta, proszę wybrać inną", Toast.LENGTH_LONG).show();
                    }
                }
            });

    }

    @Override
    public int getItemCount() {
        return datesList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        TextView mDay, mHour, mFree;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            //inicjacja widoków
            mDay = itemView.findViewById(R.id.day);
            mHour = itemView.findViewById(R.id.hour);
            mFree = itemView.findViewById(R.id.is_free);
        }

    }
}
