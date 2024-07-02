package dev.adamag.tripmasterfront.Activity.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

import dev.adamag.tripmasterfront.R;
import dev.adamag.tripmasterfront.model.Flight;

public class FlightAdapterNoButton extends RecyclerView.Adapter<FlightAdapterNoButton.FlightViewHolder> {
    private List<Flight> flightList;
    private Context context;

    public FlightAdapterNoButton(List<Flight> flightList, Context context) {
        this.flightList = flightList;
        this.context = context;
    }

    @NonNull
    @Override
    public FlightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_flight_no_button, parent, false);
        return new FlightViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlightViewHolder holder, int position) {
        Flight flight = flightList.get(position);
        Map<String, Object> details = flight.getObjectDetails();

        holder.textViewAirline.setText(getValue(details, "airline"));
        holder.textViewPrice.setText("Price: " + getValue(details, "price"));
        holder.textViewOutboundDepartureTime.setText("Outbound Departure: " + getValue(details, "outboundDeparture"));
        holder.textViewOutboundArrivalTime.setText("Outbound Arrival: " + getValue(details, "outboundArrival"));
        holder.textViewReturnDepartureTime.setText("Return Departure: " + getValue(details, "returnDeparture"));
        holder.textViewReturnArrivalTime.setText("Return Arrival: " + getValue(details, "returnArrival"));
        holder.textViewAdults.setText("Adults: " + getValue(details, "adults"));
        holder.textViewChildren.setText("Children: " + getValue(details, "children"));
        holder.textViewDepartureDate.setText("Departure Date: " + getValue(details, "departureDate"));
        holder.textViewReturnDate.setText("Return Date: " + getValue(details, "returnDate"));
    }

    @Override
    public int getItemCount() {
        return flightList.size();
    }

    public static class FlightViewHolder extends RecyclerView.ViewHolder {
        TextView textViewAirline, textViewPrice, textViewOutboundDepartureTime, textViewOutboundArrivalTime,
                textViewReturnDepartureTime, textViewReturnArrivalTime, textViewAdults, textViewChildren,
                textViewDepartureDate, textViewReturnDate;

        public FlightViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAirline = itemView.findViewById(R.id.textViewAirline);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            textViewOutboundDepartureTime = itemView.findViewById(R.id.textViewOutboundDepartureTime);
            textViewOutboundArrivalTime = itemView.findViewById(R.id.textViewOutboundArrivalTime);
            textViewReturnDepartureTime = itemView.findViewById(R.id.textViewReturnDepartureTime);
            textViewReturnArrivalTime = itemView.findViewById(R.id.textViewReturnArrivalTime);
            textViewAdults = itemView.findViewById(R.id.textViewAdults);
            textViewChildren = itemView.findViewById(R.id.textViewChildren);
            textViewDepartureDate = itemView.findViewById(R.id.textViewDepartureDate);
            textViewReturnDate = itemView.findViewById(R.id.textViewReturnDate);
        }
    }

    private String getValue(Map<String, Object> details, String key) {
        Object value = details.get(key);
        return value != null ? value.toString() : "N/A";
    }
}
