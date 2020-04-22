package ir.ac.kntu.Technical.Other.Other;

import android.app.Application;
import android.content.Context;

import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;
import com.pushpole.sdk.PushPole;

import ir.map.sdk_map.Mapir;
import ir.map.servicesdk.MapirService;

public class ContextHelper extends Application {
    private static Context context;

    public static Context retrieveContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        PushPole.initialize(context, true);
        MapirService.init(this, "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImRjZWZhYjgxMGNjODAyZThmYWYzYzkxNTY3NTMzMzA0OGFkYjJkMzg3ODE3ODI1ZGJjYjk3NWUxZDE2M2VkOGY4ZGYzOTU1OTAwMWNmMTA2In0.eyJhdWQiOiIzODQ0IiwianRpIjoiZGNlZmFiODEwY2M4MDJlOGZhZjNjOTE1Njc1MzMzMDQ4YWRiMmQzODc4MTc4MjVkYmNiOTc1ZTFkMTYzZWQ4ZjhkZjM5NTU5MDAxY2YxMDYiLCJpYXQiOjE1NjQzMTA4NTksIm5iZiI6MTU2NDMxMDg1OSwiZXhwIjoxNTY2OTg5MjU4LCJzdWIiOiIiLCJzY29wZXMiOlsiYmFzaWMiXX0.i4qgrioZiIce6wlboI3jPySPZjhwtuBr594blUC6_r4msSVIA1qvu_uopfSqQ6W_DCLF6761D3zCpIzw5ymzmB9OdUoapoao1NdmMtHH1icifzW91EAUT-0FgqSTUdflflfsOBI6U6QYiO-E3NiIqXVv-ITXxYewWKK7heZwXzFcVwDV4eO21HDVtjakkaTujTVADuYMgh0bfNFJl_ng2pP0dJLwMlVGL5nShz5ulHsEygsVgdYb9e0CLdgE7QdQGZ_APCv_VX67hckseLxzsj4aWu5pa4-XeUd1nb7qNhIElCjqZPkR_FrU5Nvnu-dUGkoL7xpejBth9yG_Tn0zjw");
        Mapir.getInstance(this, "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImRjZWZhYjgxMGNjODAyZThmYWYzYzkxNTY3NTMzMzA0OGFkYjJkMzg3ODE3ODI1ZGJjYjk3NWUxZDE2M2VkOGY4ZGYzOTU1OTAwMWNmMTA2In0.eyJhdWQiOiIzODQ0IiwianRpIjoiZGNlZmFiODEwY2M4MDJlOGZhZjNjOTE1Njc1MzMzMDQ4YWRiMmQzODc4MTc4MjVkYmNiOTc1ZTFkMTYzZWQ4ZjhkZjM5NTU5MDAxY2YxMDYiLCJpYXQiOjE1NjQzMTA4NTksIm5iZiI6MTU2NDMxMDg1OSwiZXhwIjoxNTY2OTg5MjU4LCJzdWIiOiIiLCJzY29wZXMiOlsiYmFzaWMiXX0.i4qgrioZiIce6wlboI3jPySPZjhwtuBr594blUC6_r4msSVIA1qvu_uopfSqQ6W_DCLF6761D3zCpIzw5ymzmB9OdUoapoao1NdmMtHH1icifzW91EAUT-0FgqSTUdflflfsOBI6U6QYiO-E3NiIqXVv-ITXxYewWKK7heZwXzFcVwDV4eO21HDVtjakkaTujTVADuYMgh0bfNFJl_ng2pP0dJLwMlVGL5nShz5ulHsEygsVgdYb9e0CLdgE7QdQGZ_APCv_VX67hckseLxzsj4aWu5pa4-XeUd1nb7qNhIElCjqZPkR_FrU5Nvnu-dUGkoL7xpejBth9yG_Tn0zjw");
        AppCenter.start(this, "b81f5bc4-e4a9-40b5-ab0c-fe8773b30e6d", Analytics.class, Crashes.class);
    }
}
