package ir.ac.kntu.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;
import java.util.List;

import ir.ac.kntu.Adapter.Adapter_FAQ;
import ir.ac.kntu.Entity.FAQ;
import ir.ac.kntu.Interface.Retrofit.General_Server_API;
import ir.ac.kntu.R;
import ir.ac.kntu.Server.Connector;
import ir.ac.kntu.Technical.Other.Other.ContextHelper;
import ir.ac.kntu.Technical.Other.Other.Helper_Log;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_FAQ extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    ImageView back;
    List<FAQ> list = new LinkedList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_faq, container, false);
        findViews(view);
        initializeViewContents(view);
        initializeServerSupplied(view);
        manageListeners(view);
        return view;
    }

    private void manageListeners(View view) {
        back.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });
    }

    private void findViews(View view) {
        recyclerView = view.findViewById(R.id.faq_recycler);
        back = view.findViewById(R.id.faq_back);
    }

    private void initializeViewContents(View view) {
        recyclerView.setLayoutManager(new LinearLayoutManager(ContextHelper.retrieveContext(), RecyclerView.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        adapter = new Adapter_FAQ(list);
        recyclerView.setAdapter(adapter);
    }

    private void initializeServerSupplied(View view) {

        Connector.createService(view, General_Server_API.class, object -> object.getFAQs().enqueue(new Callback<List<FAQ>>() {
            @Override
            public void onResponse(Call<List<FAQ>> call, Response<List<FAQ>> response) {
                if (response.body() != null) {
                    list.clear();
                    list.addAll(getShownList(response.body()));
                    adapter.notifyDataSetChanged();
                } else {
                    Helper_Log.errorLog(Fragment_FAQ.class);
                }
            }

            @Override
            public void onFailure(Call<List<FAQ>> call, Throwable t) {
                Helper_Log.errorLog(t, Fragment_FAQ.class);
            }
        }));
    }

    private List<FAQ> getShownList(List<FAQ> list) {
        List<FAQ> tempList = new LinkedList<>();
        for (FAQ item : list)
            tempList.add(item);
        return tempList;
    }
}
