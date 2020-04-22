package ir.ac.kntu.DataBase;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import ir.ac.kntu.Entity.Bill;
import ir.ac.kntu.Entity.Order;
import ir.ac.kntu.Entity.Restaurant;
import ir.ac.kntu.Interface.Room.Operable_Bill;
import ir.ac.kntu.Interface.Room.Operable_Order;
import ir.ac.kntu.Interface.Room.Operable_Restaurant;

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
    public abstract Operable_Bill billInterface();

    public abstract Operable_Restaurant restaurantInterface();

    public abstract Operable_Order orderInterface();

}