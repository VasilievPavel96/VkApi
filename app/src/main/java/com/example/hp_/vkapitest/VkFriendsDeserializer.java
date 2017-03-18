package com.example.hp_.vkapitest;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HP- on 10.03.2017.
 */

public class VkFriendsDeserializer implements JsonDeserializer<List<User>> {
    @Override
    public List<User> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonObject response = jsonObject.getAsJsonObject("response");
        JsonArray items = response.getAsJsonArray("items");
        ArrayList<User> users = new ArrayList<>();
        for (int i = 0; i < items.size(); ++i) {
            JsonObject jsonUser = (JsonObject) items.get(i);
            User user = new User();
            user.setId(jsonUser.getAsJsonPrimitive("id").getAsLong());
            user.setFirstName(jsonUser.getAsJsonPrimitive("first_name").getAsString());
            user.setLastName(jsonUser.getAsJsonPrimitive("last_name").getAsString());
            user.setPhotoUrl(jsonUser.getAsJsonPrimitive("photo_50").getAsString());
            users.add(user);
        }
        return users;
    }
}
