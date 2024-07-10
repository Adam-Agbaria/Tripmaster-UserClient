package dev.adamag.tripmasterfront.model;

import java.util.HashMap;
import java.util.Map;

public class CheckListItem {

    private String name;
    private int amount;
    private boolean checked;

    public CheckListItem(String name, int amount, boolean checked) {
        this.name = name;
        this.amount = amount;
        this.checked = checked;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public static CheckListItem fromJson(Map<String, Object> json) {
        String name = (String) json.get("name");
        int amount = ((Number) json.get("amount")).intValue();
        boolean checked = (boolean) json.get("checked");
        return new CheckListItem(name, amount, checked);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("amount", amount);
        map.put("checked", checked);
        return map;
    }
}
