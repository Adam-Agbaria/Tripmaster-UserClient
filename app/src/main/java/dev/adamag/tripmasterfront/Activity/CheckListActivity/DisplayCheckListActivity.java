package dev.adamag.tripmasterfront.Activity.CheckListActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import dev.adamag.tripmasterfront.Activity.Adapter.CheckListAdapter;
import dev.adamag.tripmasterfront.Activity.FlightActivity.MenuBarActivity;
import dev.adamag.tripmasterfront.R;
import dev.adamag.tripmasterfront.model.BoundaryCommand;
import dev.adamag.tripmasterfront.model.BoundaryObject;
import dev.adamag.tripmasterfront.model.CheckList;
import dev.adamag.tripmasterfront.model.CheckListItem;
import dev.adamag.tripmasterfront.model.User;
import dev.adamag.tripmasterfront.network.CommandServiceImpl;
import dev.adamag.tripmasterfront.network.ObjectService;
import dev.adamag.tripmasterfront.network.ObjectServiceImpl;
import dev.adamag.tripmasterfront.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DisplayCheckListActivity extends MenuBarActivity {

    private RecyclerView recyclerViewCheckList;
    private CheckListAdapter checkListAdapter;
    private List<CheckListItem> checkListItems = new ArrayList<>();
    private ImageView addCheckListItem;
    private String userIdBoundaryJson;
    private User.UserIdBoundary userIdBoundary;
    private String checklistId = null; // Store checklist ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_checklist);
        setupBottomNavigationBar();

        recyclerViewCheckList = findViewById(R.id.recyclerViewChecklist);
        addCheckListItem = findViewById(R.id.addChecklistItem);

        recyclerViewCheckList.setLayoutManager(new LinearLayoutManager(this));
        checkListAdapter = new CheckListAdapter(checkListItems, this);
        recyclerViewCheckList.setAdapter(checkListAdapter);

        Intent intent = getIntent();
        userIdBoundaryJson = intent.getStringExtra("userIdBoundary");

        fetchCheckList();

        addCheckListItem.setOnClickListener(v -> showAddItemDialog());
    }

    private void fetchCheckList() {
        if (userIdBoundaryJson == null) {
            Toast.makeText(this, "User information is missing", Toast.LENGTH_SHORT).show();
            return;
        }

        userIdBoundary = new Gson().fromJson(userIdBoundaryJson, User.UserIdBoundary.class);
        BoundaryCommand.TargetObject.ObjectId objectId = new BoundaryCommand.TargetObject.ObjectId("tripMaster", "4893f3dd-ceec-4d9e-992a-fae8f08137eb");
        BoundaryCommand.TargetObject targetObject = new BoundaryCommand.TargetObject(objectId);

        BoundaryCommand.InvokedBy.UserId userId = new BoundaryCommand.InvokedBy.UserId("tripMaster", userIdBoundary.getEmail());
        BoundaryCommand.InvokedBy invokedBy = new BoundaryCommand.InvokedBy(userId);

        Map<String, Object> commandAttributes = new HashMap<>();
        commandAttributes.put("email", userIdBoundary.getEmail());
        commandAttributes.put("type", "CheckList");
        commandAttributes.put("page", 0);
        commandAttributes.put("size", 1);

        String timestamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault()).format(new Date());

        BoundaryCommand command = new BoundaryCommand(null, "findObjectsByCreatorEmailAndType", targetObject, timestamp, invokedBy, commandAttributes);
        CommandServiceImpl commandService = new CommandServiceImpl();

        commandService.createCommand("userApp", command, new Callback<BoundaryCommand[]>() {
            @Override
            public void onResponse(Call<BoundaryCommand[]> call, Response<BoundaryCommand[]> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BoundaryCommand[] results = response.body();
                    List<Map<String, Object>> resultList = (List<Map<String, Object>>) results[0].getCommandAttributes().get("results");
                    if (resultList != null && !resultList.isEmpty()) {
                        BoundaryObject checkListObj = new Gson().fromJson(new Gson().toJson(resultList.get(0)), BoundaryObject.class);
                        checklistId = checkListObj.getObjectId().getId(); // Store checklist ID
                        CheckList checkList = CheckList.fromJson(checkListObj.getObjectDetails());
                        checkListItems.addAll(checkList.getItems());
                        checkListAdapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(DisplayCheckListActivity.this, "Failed to fetch checklist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BoundaryCommand[]> call, Throwable t) {
                Toast.makeText(DisplayCheckListActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAddItemDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_add_checklist_item, null);

        TextInputEditText itemNameInput = view.findViewById(R.id.editTextItemName);
        TextInputEditText itemAmountInput = view.findViewById(R.id.editTextAmount);

        builder.setView(view)
                .setPositiveButton("Add", (dialog, which) -> {
                    String itemName = itemNameInput.getText().toString();
                    String itemAmountStr = itemAmountInput.getText().toString();
                    int itemAmount = itemAmountStr.isEmpty() ? 1 : Integer.parseInt(itemAmountStr);

                    CheckListItem newItem = new CheckListItem(itemName, itemAmount, false);
                    checkListItems.add(newItem);
                    checkListAdapter.notifyDataSetChanged();

                    saveCheckList();
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    public void saveCheckList() {
        CheckList checkList = new CheckList(checkListItems);
        BoundaryObject boundaryObject = checkList.toBoundaryObject();

        // Ensure the createdBy field is set correctly
        boundaryObject.getCreatedBy().getUserId().setEmail(userIdBoundary.getEmail());
        boundaryObject.getCreatedBy().getUserId().setSuperapp(userIdBoundary.getSuperapp());

        if (checklistId == null) {
            // Send command to check if checklist exists
            CommandServiceImpl commandService = new CommandServiceImpl();
            commandService.sendFindObjectsByCreatorEmailAndTypeCommand(
                    userIdBoundary.getEmail(),
                    "tripMaster",
                    userIdBoundary.getEmail(),
                    "CheckList",
                    0,
                    1,
                    new Callback<List<BoundaryObject>>() {
                        @Override
                        public void onResponse(Call<List<BoundaryObject>> call, Response<List<BoundaryObject>> response) {
                            if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                                // Checklist exists, update it
                                BoundaryObject existingCheckList = response.body().get(0);
                                checklistId = existingCheckList.getObjectId().getId();
                                updateCheckList(boundaryObject);
                            } else {
                                // Create new checklist
                                createNewCheckList(boundaryObject);
                            }
                        }

                        @Override
                        public void onFailure(Call<List<BoundaryObject>> call, Throwable t) {
                            Log.e("CheckList", "Failed to find checklist: " + t.getMessage());
                            Toast.makeText(DisplayCheckListActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                        }
                    }
            );
        } else {
            // Update existing checklist
            updateCheckList(boundaryObject);
        }
    }

    private void createNewCheckList(BoundaryObject boundaryObject) {
        ObjectServiceImpl serviceUtil = new ObjectServiceImpl();
        serviceUtil.createObject(boundaryObject, new Callback<BoundaryObject>() {
            @Override
            public void onResponse(Call<BoundaryObject> call, Response<BoundaryObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    checklistId = response.body().getObjectId().getId(); // Store the new checklist ID
                    Log.d("CheckList", "Checklist created successfully");
                } else {
                    Log.e("CheckList", "Failed to create checklist: " + response.toString());
                    Toast.makeText(DisplayCheckListActivity.this, "Failed to create checklist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BoundaryObject> call, Throwable t) {
                Log.e("CheckList", "Create checklist error: " + t.getMessage());
                Toast.makeText(DisplayCheckListActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateCheckList(BoundaryObject boundaryObject) {
        boundaryObject.getObjectId().setId(checklistId); // Ensure we use the existing checklist ID
        ObjectServiceImpl serviceUtil = new ObjectServiceImpl();
        serviceUtil.updateObject(
                "tripMaster",
                checklistId,
                boundaryObject,
                userIdBoundary.getSuperapp(),
                userIdBoundary.getEmail(),
                new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Log.d("CheckList", "Checklist updated successfully");
                        } else {
                            Log.e("CheckList", "Failed to update checklist: " + response.toString());
                            Toast.makeText(DisplayCheckListActivity.this, "Failed to update checklist", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("CheckList", "Update checklist error: " + t.getMessage());
                        Toast.makeText(DisplayCheckListActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
}
