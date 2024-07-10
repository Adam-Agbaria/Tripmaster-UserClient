package dev.adamag.tripmasterfront.Activity.VacationPackageActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import dev.adamag.tripmasterfront.Activity.Adapter.PackagesAdapter;
import dev.adamag.tripmasterfront.Activity.FlightActivity.MenuBarActivity;
import dev.adamag.tripmasterfront.R;
import dev.adamag.tripmasterfront.model.BoundaryObject;
import dev.adamag.tripmasterfront.model.User;
import dev.adamag.tripmasterfront.network.ObjectService;
import dev.adamag.tripmasterfront.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DisplayAllVacationPackagesActivity extends MenuBarActivity {

    private RecyclerView recyclerView;
    private PackagesAdapter packagesAdapter;
    private TextView noPackagesTextView;
    private String userIdBoundaryJson;
    private User.UserIdBoundary userIdBoundary;
    private List<BoundaryObject> allPackages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_all_vacation_packages);
        setupBottomNavigationBar();

        recyclerView = findViewById(R.id.recycler_view);
        noPackagesTextView = findViewById(R.id.no_packages_text);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        userIdBoundaryJson = intent.getStringExtra("userIdBoundary");
        if (userIdBoundaryJson != null) {
            userIdBoundary = new Gson().fromJson(userIdBoundaryJson, User.UserIdBoundary.class);
        } else {
            Toast.makeText(this, "User information is missing", Toast.LENGTH_SHORT).show();
            return;
        }

        fetchVacationPackages();

        Button filterButton = findViewById(R.id.filtering_button);
        filterButton.setOnClickListener(v -> showFilterDialog());
    }

    private void fetchVacationPackages() {
        ObjectService objectService = RetrofitClient.getTripMasterClient().create(ObjectService.class);

        Call<List<BoundaryObject>> call = objectService.searchObjectsByType(
                "VacationPackage",
                userIdBoundary.getSuperapp(),
                userIdBoundary.getEmail(),
                100,
                0
        );

        call.enqueue(new Callback<List<BoundaryObject>>() {
            @Override
            public void onResponse(Call<List<BoundaryObject>> call, Response<List<BoundaryObject>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allPackages = response.body();
                    updateRecyclerView(allPackages);
                } else {
                    Toast.makeText(DisplayAllVacationPackagesActivity.this, "Failed to fetch packages", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<BoundaryObject>> call, Throwable t) {
                Toast.makeText(DisplayAllVacationPackagesActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_filter, null);
        builder.setView(dialogView);

        AutoCompleteTextView destinationFilter = dialogView.findViewById(R.id.destination_filter);
        EditText minStarsFilter = dialogView.findViewById(R.id.min_stars_filter);
        EditText maxStarsFilter = dialogView.findViewById(R.id.max_stars_filter);
        RadioButton connectionYes = dialogView.findViewById(R.id.connection_yes);
        RadioButton connectionNo = dialogView.findViewById(R.id.connection_no);
        EditText minPriceFilter = dialogView.findViewById(R.id.min_price_filter);
        EditText maxPriceFilter = dialogView.findViewById(R.id.max_price_filter);
        Button startDateFilterButton = dialogView.findViewById(R.id.start_date_filter_button);
        Button endDateFilterButton = dialogView.findViewById(R.id.end_date_filter_button);
        Button applyFiltersButton = dialogView.findViewById(R.id.apply_filters_button);
        Button resetFiltersButton = dialogView.findViewById(R.id.reset_filters_button);

        // Initialize with default values
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.destinations_array, android.R.layout.simple_dropdown_item_1line);
        destinationFilter.setAdapter(adapter);
        minStarsFilter.setText("1");
        maxStarsFilter.setText("5");
        minPriceFilter.setText("0");
        maxPriceFilter.setText("20000");

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        String currentDate = dateFormat.format(calendar.getTime());
        calendar.add(Calendar.YEAR, 1);
        String nextYearDate = dateFormat.format(calendar.getTime());
        startDateFilterButton.setText(currentDate);
        endDateFilterButton.setText(nextYearDate);

        setupDatePicker(startDateFilterButton);
        setupDatePicker(endDateFilterButton);

        AlertDialog dialog = builder.create();

        applyFiltersButton.setOnClickListener(v -> {
            String destination = destinationFilter.getText().toString();
            String minStars = minStarsFilter.getText().toString();
            String maxStars = maxStarsFilter.getText().toString();
            boolean isConnection = connectionYes.isChecked();
            boolean noConnection = connectionNo.isChecked();
            String minPrice = minPriceFilter.getText().toString();
            String maxPrice = maxPriceFilter.getText().toString();
            String startDate = startDateFilterButton.getText().toString();
            String endDate = endDateFilterButton.getText().toString();

            List<BoundaryObject> filteredPackages = new ArrayList<>(allPackages);

            if (!destination.isEmpty()) {
                filteredPackages = filteredPackages.stream()
                        .filter(pkg -> destination.equalsIgnoreCase((String) pkg.getObjectDetails().get("destination")))
                        .collect(Collectors.toList());
            }

            if (!minStars.isEmpty() && !maxStars.isEmpty()) {
                int minStarsValue = Integer.parseInt(minStars);
                int maxStarsValue = Integer.parseInt(maxStars);
                filteredPackages = filteredPackages.stream()
                        .filter(pkg -> {
                            int stars = ((Number) pkg.getObjectDetails().get("stars")).intValue();
                            return stars >= minStarsValue && stars <= maxStarsValue;
                        })
                        .collect(Collectors.toList());
            }

            if (isConnection) {
                filteredPackages = filteredPackages.stream()
                        .filter(pkg -> (Boolean) pkg.getObjectDetails().get("isConnectionFlight"))
                        .collect(Collectors.toList());
            } else if (noConnection) {
                filteredPackages = filteredPackages.stream()
                        .filter(pkg -> !(Boolean) pkg.getObjectDetails().get("isConnectionFlight"))
                        .collect(Collectors.toList());
            }

            if (!minPrice.isEmpty() && !maxPrice.isEmpty()) {
                double minPriceValue = Double.parseDouble(minPrice);
                double maxPriceValue = Double.parseDouble(maxPrice);
                filteredPackages = filteredPackages.stream()
                        .filter(pkg -> {
                            double price = ((Number) pkg.getObjectDetails().get("price")).doubleValue();
                            return price >= minPriceValue && price <= maxPriceValue;
                        })
                        .collect(Collectors.toList());
            }

            if (!startDate.isEmpty() && !endDate.isEmpty()) {
                SimpleDateFormat dateFormatFilter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                try {
                    Date minDate = dateFormatFilter.parse(startDate);
                    Date maxDate = dateFormatFilter.parse(endDate);
                    filteredPackages = filteredPackages.stream()
                            .filter(pkg -> {
                                try {
                                    Date packageStartDate = dateFormatFilter.parse((String) pkg.getObjectDetails().get("startDate"));
                                    Date packageEndDate = dateFormatFilter.parse((String) pkg.getObjectDetails().get("endDate"));
                                    return (packageStartDate.after(minDate) || packageStartDate.equals(minDate)) &&
                                            (packageEndDate.before(maxDate) || packageEndDate.equals(maxDate));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                    return false;
                                }
                            })
                            .collect(Collectors.toList());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            updateRecyclerView(filteredPackages);
            dialog.dismiss();
        });

        resetFiltersButton.setOnClickListener(v -> {
            destinationFilter.setText("");
            minStarsFilter.setText("1");
            maxStarsFilter.setText("5");
            connectionYes.setChecked(false);
            connectionNo.setChecked(false);
            minPriceFilter.setText("0");
            maxPriceFilter.setText("20000");
            startDateFilterButton.setText(currentDate);
            endDateFilterButton.setText(nextYearDate);
        });

        dialog.show();
    }

    private void setupDatePicker(Button dateButton) {
        dateButton.setOnClickListener(v -> showDatePickerDialog(dateButton));
    }

    private void showDatePickerDialog(Button dateButton) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth, monthOfYear + 1, year1);
                    dateButton.setText(selectedDate);
                }, year, month, day);

        datePickerDialog.show();
    }

    private void updateRecyclerView(List<BoundaryObject> packages) {
        if (packages.isEmpty()) {
            noPackagesTextView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            noPackagesTextView.setVisibility(View.GONE);
            packagesAdapter = new PackagesAdapter(packages);
            recyclerView.setAdapter(packagesAdapter);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}
