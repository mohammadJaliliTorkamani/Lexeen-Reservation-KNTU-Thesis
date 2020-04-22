package ir.ac.kntu.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.ac.kntu.Entity.License;
import ir.ac.kntu.R;
import ir.ac.kntu.Technical.CustomView.TextViewPlus;
import ir.ac.kntu.Technical.Other.Other.ContextHelper;

public class Adapter_OpenSource extends RecyclerView.Adapter {
    private List<License> list;

    public Adapter_OpenSource(List<License> licenses) {
        this.list = licenses;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(ContextHelper.retrieveContext()).inflate(R.layout.item_license, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TextViewPlus title = holder.itemView.findViewById(R.id.license_item_title);
        TextViewPlus content = holder.itemView.findViewById(R.id.license_item_content);

        title.setText(list.get(position).getTitle().substring(list.get(position).getTitle().lastIndexOf("_") + 1));
        content.setText(list.get(position).getContent().trim());
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
