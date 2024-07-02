package dev.adamag.tripmasterfront.Activity.Adapter;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dev.adamag.tripmasterfront.R;
import dev.adamag.tripmasterfront.model.BoundaryObject;

public class PackagesAdapter extends RecyclerView.Adapter<PackagesAdapter.PackageViewHolder> {

    private List<BoundaryObject> packages;

    public PackagesAdapter(List<BoundaryObject> packages) {
        this.packages = packages;
    }

    @NonNull
    @Override
    public PackageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_package, parent, false);
        return new PackageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PackageViewHolder holder, int position) {
        BoundaryObject vacationPackage = packages.get(position);

        String packageName = "Package Name: " + (String) vacationPackage.getObjectDetails().get("packageName");
        String destination = "Destination: " + (String) vacationPackage.getObjectDetails().get("destination");
        String hotelName = "Hotel: " + (String) vacationPackage.getObjectDetails().get("hotelName");
        String stars = "Stars: " + String.valueOf(vacationPackage.getObjectDetails().get("stars")) + "☆";
        Boolean isConnectionFlight = (Boolean) vacationPackage.getObjectDetails().get("isConnectionFlight");
        String connectionFlight = "Connection Flight: " + (isConnectionFlight != null && isConnectionFlight ? "Yes" : "No");
        String price = "Price: " + String.valueOf(vacationPackage.getObjectDetails().get("price")) + "$";
        String email = "Contact: " + vacationPackage.getCreatedBy().getUserId().getEmail();

        holder.packageNameTextView.setText(packageName);
        holder.destinationTextView.setText(destination);
        holder.hotelNameTextView.setText(hotelName);
        holder.starsTextView.setText(stars);
        holder.isConnectionFlightTextView.setText(connectionFlight);
        holder.priceTextView.setText(price);
        holder.emailTextView.setText(email);


    }

    @Override
    public int getItemCount() {
        return packages.size();
    }

    static class PackageViewHolder extends RecyclerView.ViewHolder {

        TextView packageNameTextView, destinationTextView, hotelNameTextView, starsTextView, isConnectionFlightTextView, priceTextView, emailTextView;

        public PackageViewHolder(@NonNull View itemView) {
            super(itemView);
            packageNameTextView = itemView.findViewById(R.id.package_name);
            destinationTextView = itemView.findViewById(R.id.destination);
            hotelNameTextView = itemView.findViewById(R.id.hotel_name);
            starsTextView = itemView.findViewById(R.id.stars);
            isConnectionFlightTextView = itemView.findViewById(R.id.is_connection_flight);
            priceTextView = itemView.findViewById(R.id.price);
            emailTextView = itemView.findViewById(R.id.email);
        }
    }
}
