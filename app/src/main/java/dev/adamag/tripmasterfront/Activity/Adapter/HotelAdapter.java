package dev.adamag.tripmasterfront.Activity.Adapter;

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
import java.util.Map;

import dev.adamag.tripmasterfront.Activity.HotelActivity.DisplayHotelsActivity;
import dev.adamag.tripmasterfront.R;
import dev.adamag.tripmasterfront.model.Hotel;

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.HotelViewHolder> {

    private List<Hotel> hotelList;
    private Context context;

    public HotelAdapter(List<Hotel> hotelList, Context context) {
        this.hotelList = hotelList;
        this.context = context;
    }

    @NonNull
    @Override
    public HotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hotel, parent, false);
        return new HotelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelViewHolder holder, int position) {
        Hotel hotel = hotelList.get(position);
        Map<String, Object> details = hotel.getObjectDetails();

        holder.hotelName.setText(details.get("hotelName").toString());
        holder.city.setText("City: " + details.get("city").toString());
        holder.checkInDate.setText("Check-in: " + details.get("checkInDate").toString());
        holder.checkOutDate.setText("Check-out: " + details.get("checkOutDate").toString());
        holder.adults.setText("Adults: " + details.get("adults").toString());
        holder.children.setText("Children: " + details.get("children").toString());
        holder.rooms.setText("Rooms: " + details.get("rooms").toString());
        holder.price.setText("Price: " + details.get("price").toString());

        String url = details.get("url").toString();
        holder.bookButton.setOnClickListener(v -> {
            ((DisplayHotelsActivity) context).setReturningFromBooking(
                    true,
                    details.get("hotelName").toString(),
                    details.get("city").toString(),
                    details.get("checkInDate").toString(),
                    details.get("checkOutDate").toString(),
                    url,
                    details.get("price").toString(),
                    Integer.parseInt(details.get("adults").toString()),
                    Integer.parseInt(details.get("children").toString()),
                    Integer.parseInt(details.get("rooms").toString())
            );
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(browserIntent);
        });
    }

    @Override
    public int getItemCount() {
        return hotelList.size();
    }

    public static class HotelViewHolder extends RecyclerView.ViewHolder {
        TextView hotelName, city, checkInDate, checkOutDate, adults, children, rooms, price;
        Button bookButton;

        public HotelViewHolder(@NonNull View itemView) {
            super(itemView);
            hotelName = itemView.findViewById(R.id.hotelNameTextView);
            city = itemView.findViewById(R.id.hotelCityTextView);
            checkInDate = itemView.findViewById(R.id.hotelCheckInTextView);
            checkOutDate = itemView.findViewById(R.id.hotelCheckOutTextView);
            adults = itemView.findViewById(R.id.textViewAdults);
            children = itemView.findViewById(R.id.textViewChildren);
            rooms = itemView.findViewById(R.id.hotelRoomsTextView);
            price = itemView.findViewById(R.id.hotelPriceTextView);
            bookButton = itemView.findViewById(R.id.bookButton);
        }
    }
}
