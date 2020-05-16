package com.example.dentistapplication.ui.pHome;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dentistapplication.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterDoctors extends RecyclerView.Adapter<AdapterDoctors.MyHolder> {

    Context context;
    List<ModelDoctors> doctorsList;

    public AdapterDoctors(Context context, List<ModelDoctors> doctorsList) {
        this.context = context;
        this.doctorsList = doctorsList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View view = LayoutInflater.from(context).inflate(R.layout.row_element, parent, false);
        return  new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        //pobieranie danych do wyświetlenia
        String doctorImage = doctorsList.get(i).getImageURL();
        final String doctorName = doctorsList.get(i).getName();
        String doctorSurname = doctorsList.get(i).getSurname();
        String doctorAddress = doctorsList.get(i).getAddress();

        //ustawianie danych lekarza
        myHolder.mName.setText(doctorName);
        myHolder.mSurname.setText(doctorSurname);
        myHolder.mAddress.setText(doctorAddress);
        try {
            Picasso.get().load(doctorImage)
                    .placeholder(R.drawable.ic_doctor)
                    .into(myHolder.mImage);
        }
        catch (Exception e){

        }

        //następstwo kliknięcia w danego lekarza
        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"kliknięto"+doctorName,Toast.LENGTH_LONG);
            }
        });
    }

    @Override
    public int getItemCount() {
        return doctorsList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        ImageView mImage;
        TextView mName, mSurname, mAddress;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            //inicjacja widoków
            mImage = itemView.findViewById(R.id.rImage);
            mName = itemView.findViewById(R.id.rName);
            mSurname = itemView.findViewById(R.id.rSurname);
            mAddress = itemView.findViewById(R.id.rAddress);
        }
    }
}
