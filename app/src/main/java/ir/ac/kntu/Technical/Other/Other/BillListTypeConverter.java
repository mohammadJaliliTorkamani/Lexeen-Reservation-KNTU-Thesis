package ir.ac.kntu.Technical.Other.Other;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import ir.ac.kntu.Entity.Bill;

public class BillListTypeConverter {
    @TypeConverter
    public static List<Bill> stringToMeasurements(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Bill>>() {
        }.getType();
        List<Bill> measurements = gson.fromJson(json, type);
        return measurements;
    }

    @TypeConverter
    public static String measurementsToString(List<Bill> list) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Bill>>() {
        }.getType();
        String json = gson.toJson(list, type);
        return json;
    }
}
