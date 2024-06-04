package dev.adamag.tripmasterfront.util;


import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JsonSerializer {

    public static String objectToJson(Object obj, String superAppId, String superAppEmail) {
        try {
            Map<String, Object> objectDetails = new HashMap<>();
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                objectDetails.put(field.getName(), field.get(obj));
            }

            Map<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("objectId", new HashMap<String, Object>() {{
                put("superapp", "YourSuperApp");
                put("id", superAppId);  // This ID should be fetched or generated as needed.
            }});
            jsonMap.put("type", obj.getClass().getSimpleName());
            jsonMap.put("alias", "AliasBasedOnObject");  // Customize based on object
            jsonMap.put("location", new HashMap<String, Object>() {{
                put("lat", 0.0);
                put("lng", 0.0);
            }});
            jsonMap.put("active", "Active");
            jsonMap.put("creationTimestamp", new Date().toString());
            jsonMap.put("createdBy", new HashMap<String, Object>() {{
                put("userId", new HashMap<String, Object>() {{
                    put("superapp", "YourSuperApp");
                    put("email", superAppEmail);
                }});
            }});
            jsonMap.put("objectDetails", objectDetails);

            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(jsonMap);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
