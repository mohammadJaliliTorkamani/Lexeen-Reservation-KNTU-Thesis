package ir.ac.kntu.Technical.Other.Other;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * used as retrofit request JSON converter for Complex data type (String List)
 */
public class StringListTypeConverter {
    @TypeConverter
    public static List<String> stringToMeasurements(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {
        }.getType();
        List<String> measurements = gson.fromJson(json, type);
        return measurements;
    }

    @TypeConverter
    public static String measurementsToString(List<String> list) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {
        }.getType();
        String json = gson.toJson(list, type);
        return json;
    }
}
