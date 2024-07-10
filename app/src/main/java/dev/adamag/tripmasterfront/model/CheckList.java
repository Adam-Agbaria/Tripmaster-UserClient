package dev.adamag.tripmasterfront.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckList extends BoundaryObject {

    private List<CheckListItem> items;

    public CheckList(List<CheckListItem> items) {
        this.items = items;
    }

    public List<CheckListItem> getItems() {
        return items;
    }

    public void setItems(List<CheckListItem> items) {
        this.items = items;
    }

    public static CheckList fromJson(Map<String, Object> json) {
        List<Map<String, Object>> itemsJson = (List<Map<String, Object>>) json.get("items");
        List<CheckListItem> items = new ArrayList<>();
        for (Map<String, Object> itemJson : itemsJson) {
            items.add(CheckListItem.fromJson(itemJson));
        }
        return new CheckList(items);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> itemsJson = new ArrayList<>();
        for (CheckListItem item : items) {
            itemsJson.add(item.toMap());
        }
        map.put("items", itemsJson);
        return map;
    }

    public BoundaryObject toBoundaryObject() {
        Map<String, Object> details = toMap();
        return new BoundaryObject(
                getObjectId() != null ? getObjectId() : new ObjectIdBoundary("YourSuperApp", null),
                "CheckList",
                "User's Checklist",
                getLocation() != null ? getLocation() : new LocationBoundary(0.0, 0.0),
                true,
                getCreationTimestamp() != null ? getCreationTimestamp() : new Date().toString(),
                getCreatedBy() != null ? getCreatedBy() : new CreatedByBoundary(new CreatedByBoundary.UserIdBoundary("YourSuperApp", "user@example.com")),
                details
        );
    }
}
