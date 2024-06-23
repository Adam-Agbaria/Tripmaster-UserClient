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
    public void getObjectById(String superapp, String id, String userSuperapp, String userEmail, Callback<BoundaryObject> callback) {
        Call<BoundaryObject> call = service.getObjectById(superapp, id, userSuperapp, userEmail);
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
    public void updateObject(String superapp, String id, BoundaryObject boundaryObject, String userSuperapp, String userEmail, Callback<Void> callback) {
        Call<Void> call = service.updateObject(superapp, id, boundaryObject, userSuperapp, userEmail);
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
    public void getAllObjects(String userSuperapp, String userEmail, int size, int page, Callback<List<BoundaryObject>> callback) {
        Call<List<BoundaryObject>> call = service.getAllObjects(userSuperapp, userEmail, size, page);
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

    // Method to search objects by type
    public void searchObjectsByType(String type, String userSuperapp, String userEmail, int size, int page, Callback<List<BoundaryObject>> callback) {
        Call<List<BoundaryObject>> call = service.searchObjectsByType(type, userSuperapp, userEmail, size, page);
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

    // Method to search objects by alias
    public void searchObjectsByAlias(String alias, String userSuperapp, String userEmail, int size, int page, Callback<List<BoundaryObject>> callback) {
        Call<List<BoundaryObject>> call = service.searchObjectsByAlias(alias, userSuperapp, userEmail, size, page);
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

    // Method to search objects by alias pattern
    public void searchObjectsByAliasPattern(String pattern, String userSuperapp, String userEmail, int size, int page, Callback<List<BoundaryObject>> callback) {
        Call<List<BoundaryObject>> call = service.searchObjectsByAliasPattern(pattern, userSuperapp, userEmail, size, page);
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

    // Method to search objects by location
    public void searchObjectsByLocation(double lat, double lng, double distance, String units, String userSuperapp, String userEmail, int size, int page, Callback<List<BoundaryObject>> callback) {
        Call<List<BoundaryObject>> call = service.searchObjectsByLocation(lat, lng, distance, units, userSuperapp, userEmail, size, page);
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
