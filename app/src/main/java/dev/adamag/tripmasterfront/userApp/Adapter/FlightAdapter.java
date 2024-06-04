package dev.adamag.tripmasterfront.userApp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dev.adamag.tripmasterfront.R;
import dev.adamag.tripmasterfront.model.Flight;
import dev.adamag.tripmasterfront.userApp.Activity.DisplayFlightsActivity;

public class FlightAdapter extends RecyclerView.Adapter<FlightAdapter.FlightViewHolder> {
    private List<Flight> flightList;
    private Context context;

    public FlightAdapter(List<Flight> flightList, Context context) {
        this.flightList = flightList;
        this.context = context;
    }

    @NonNull
    @Override
    public FlightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_flight, parent, false);
        return new FlightViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlightViewHolder holder, int position) {
        Flight flight = flightList.get(position);

        holder.textViewAirline.setText("Airline: " + flight.getObjectDetails().get("airline").toString());
        holder.textViewDepartureAirport.setText("From: " + flight.getObjectDetails().get("origin").toString());
        holder.textViewArrivalAirport.setText("To: " + flight.getObjectDetails().get("destination").toString());
        holder.textViewDepartureTime.setText("Departure Time: " + flight.getObjectDetails().get("departureTime").toString());
        holder.textViewArrivalTime.setText("Arrival Time: " + flight.getObjectDetails().get("arrivalTime").toString());
        holder.textViewAdults.setText("Adults: " + flight.getObjectDetails().get("adults").toString());
        holder.textViewChildren.setText("Children: " + flight.getObjectDetails().get("children").toString());

        String url = flight.getObjectDetails().get("url").toString();
        holder.buttonBookFlight.setOnClickListener(v -> {
            ((DisplayFlightsActivity) context).setReturningFromBooking(true,
                    flight.getObjectDetails().get("airline").toString(),
                    flight.getObjectDetails().get("departureTime").toString(),
                    flight.getObjectDetails().get("arrivalTime").toString(),
                    flight.getObjectDetails().get("origin").toString(),
                    flight.getObjectDetails().get("destination").toString(),
                    Integer.parseInt(flight.getObjectDetails().get("adults").toString()),
                    Integer.parseInt(flight.getObjectDetails().get("children").toString()));
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(browserIntent);
        });
    }

    @Override
    public int getItemCount() {
        return flightList.size();
    }

    public static class FlightViewHolder extends RecyclerView.ViewHolder {
        TextView textViewAirline, textViewDepartureAirport, textViewArrivalAirport, textViewDepartureTime, textViewArrivalTime, textViewAdults, textViewChildren;
        Button buttonBookFlight;

        public FlightViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAirline = itemView.findViewById(R.id.textViewAirline);
            textViewDepartureAirport = itemView.findViewById(R.id.textViewDepartureAirport);
            textViewArrivalAirport = itemView.findViewById(R.id.textViewArrivalAirport);
            textViewDepartureTime = itemView.findViewById(R.id.textViewDepartureTime);
            textViewArrivalTime = itemView.findViewById(R.id.textViewArrivalTime);
            textViewAdults = itemView.findViewById(R.id.textViewAdults);
            textViewChildren = itemView.findViewById(R.id.textViewChildren);
            buttonBookFlight = itemView.findViewById(R.id.buttonBookFlight);
        }
    }
}
