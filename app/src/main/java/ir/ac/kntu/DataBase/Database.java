package ir.ac.kntu.DataBase;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import ir.ac.kntu.Entity.Bill;
import ir.ac.kntu.Entity.Order;
import ir.ac.kntu.Entity.Restaurant;
import ir.ac.kntu.Interface.Room.Bill_Database_API;
import ir.ac.kntu.Interface.Room.Order_Database_API;
import ir.ac.kntu.Interface.Room.Restaurant_Database_API;

@androidx.room.Database(entities = {Restaurant.class, Bill.class, Order.class},
        version = 1, exportSchema = false)

public abstract class Database extends RoomDatabase {

    private static Database INSTANCE;

    public static Database getInstance(Context context, String db_name) {

        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context,
                            Database.class, db_name)
                            // allow queries on the main thread. Don't do this on a real app! See PersistenceBasicSample for an example.
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    /*there is a prob here it seems its better to implement the above code in the main thread
     * so we need to implement it in the main thread
     * */
    public abstract Bill_Database_API billInterface();

    public abstract Restaurant_Database_API restaurantInterface();

    public abstract Order_Database_API orderInterface();

}