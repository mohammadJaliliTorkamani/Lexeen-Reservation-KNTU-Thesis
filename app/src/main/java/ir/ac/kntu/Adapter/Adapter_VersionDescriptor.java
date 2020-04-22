package ir.ac.kntu.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.ac.kntu.Entity.VersionFeature;
import ir.ac.kntu.R;
import ir.ac.kntu.Technical.CustomView.TextViewPlus;
import ir.ac.kntu.Technical.Other.Other.Constants;
import ir.ac.kntu.Technical.Other.Other.ContextHelper;
import ir.ac.kntu.Technical.Other.Other.Helper;

public class Adapter_VersionDescriptor extends RecyclerView.Adapter {

    private List<VersionFeature> list;

    public Adapter_VersionDescriptor(List<VersionFeature> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(ContextHelper.retrieveContext()).inflate(R.layout.item_version_feature, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TextViewPlus textViewPlus = holder.itemView.findViewById(R.id.item_version_feature_text);
        textViewPlus.setText("*   " + list.get(position).getText());
        textViewPlus.setOnClickListener(v -> Helper.getInstance().toast(String.valueOf(position + 1), Constants.ToastMode.SUCCESS));
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private View itemView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }
}
