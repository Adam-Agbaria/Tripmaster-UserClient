package dev.adamag.tripmasterfront.network;


import java.util.List;

import dev.adamag.tripmasterfront.model.BoundaryObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ObjectServiceImpl {

    private ObjectService service;

    public ObjectServiceImpl() {
        service = RetrofitClient.getTripMasterClient().create(ObjectService.class);
    }

    // Method to create an object
    public void createObject(BoundaryObject boundaryObject, Callback<BoundaryObject> callback) {
        Call<BoundaryObject> call = service.createObject(boundaryObject);
        call.enqueue(new Callback<BoundaryObject>() {
            @Override
            public void onResponse(Call<BoundaryObject> call, Response<BoundaryObject> response) {
                callback.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<BoundaryObject> call, Throwable t) {
                callback.onFailure(call, t);
            }
        });
    }

    // Method to retrieve an object by ID
    public void getObjectById(String id, Callback<BoundaryObject> callback) {
        Call<BoundaryObject> call = service.getObjectById(id);
        call.enqueue(new Callback<BoundaryObject>() {
            @Override
            public void onResponse(Call<BoundaryObject> call, Response<BoundaryObject> response) {
                callback.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<BoundaryObject> call, Throwable t) {
                callback.onFailure(call, t);
            }
        });
    }

    // Method to update an object
    public void updateObject(String id, BoundaryObject boundaryObject, Callback<Void> callback) {
        Call<Void> call = service.updateObject(id, boundaryObject);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                callback.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFailure(call, t);
            }
        });
    }

    // Method to retrieve all objects
    public void getAllObjects(Callback<List<BoundaryObject>> callback) {
        Call<List<BoundaryObject>> call = service.getAllObjects();
        call.enqueue(new Callback<List<BoundaryObject>>() {
            @Override
            public void onResponse(Call<List<BoundaryObject>> call, Response<List<BoundaryObject>> response) {
                callback.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<List<BoundaryObject>> call, Throwable t) {
                callback.onFailure(call, t);
            }
        });
    }


}
