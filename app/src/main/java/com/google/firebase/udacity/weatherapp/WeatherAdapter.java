//package com.google.firebase.udacity.weatherapp;
//
//import android.support.annotation.NonNull;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import java.util.zip.Inflater;
//
//public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {
//
//    WeatherAdapter(){
//        //Default constructor
//    }
//
//    @NonNull
//    @Override
//    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
//        View view = layoutInflater.inflate(R.layout.item_view,viewGroup,false);
//        WeatherViewHolder weatherViewHolder = new WeatherViewHolder(view);
//        return weatherViewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull WeatherViewHolder weatherViewHolder, int i) {
//        weatherViewHolder.textView.setText("On Bind View Holder" + i);
//    }
//
//    @Override
//    public int getItemCount() {
//        return 40;
//    }
//
//    class WeatherViewHolder extends RecyclerView.ViewHolder {
//        TextView textView;
//        public WeatherViewHolder(@NonNull View itemView) {
//            super(itemView);
//            textView = itemView.findViewById(R.id.text_view);
//        }
//    }
//}
