package ir.ac.kntu.Technical.Other.Other;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import ir.ac.kntu.Entity.Address;

public class AddressTypeConverter {
    @TypeConverter
    public static Address stringToMeasurements(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<Address>() {
        }.getType();
        Address measurements = gson.fromJson(json, type);
        return measurements;
    }

    @TypeConverter
    public static String measurementsToString(Address address) {
        Gson gson = new Gson();
        Type type = new TypeToken<Address>() {
        }.getType();
        String json = gson.toJson(address, type);
        return json;
    }
}
