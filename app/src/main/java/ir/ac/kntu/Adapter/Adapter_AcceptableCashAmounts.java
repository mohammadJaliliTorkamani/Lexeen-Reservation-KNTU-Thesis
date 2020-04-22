package ir.ac.kntu.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.ac.kntu.R;
import ir.ac.kntu.Technical.CustomView.TextViewPlus;
import ir.ac.kntu.Technical.Other.CustomRunnable.Runnable_SingleArg;
import ir.ac.kntu.Technical.Other.Other.ContextHelper;
import ir.ac.kntu.Technical.Other.Other.Helper;

public class Adapter_AcceptableCashAmounts extends RecyclerView.Adapter {
    private static int clickedPos;
    private List<Double> list;
    private Runnable_SingleArg<Double> singleArgRunnable;

    public Adapter_AcceptableCashAmounts(List<Double> list, Runnable_SingleArg<Double> toClickResponse) {
        this.list = list;
        this.singleArgRunnable = toClickResponse;
        clickedPos = -1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(ContextHelper.retrieveContext()).inflate(R.layout.item_acceptable_cash, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        LinearLayout container = holder.itemView.findViewById(R.id.acceptable_cash_text_container);
        TextViewPlus text = holder.itemView.findViewById(R.id.acceptable_cash_text);
        text.setText(Helper.getOneDigitOrNon(list.get(position), false) + " " + Helper.getPurchaseUnit());
        text.setTextColor(position == clickedPos ? Color.WHITE : Color.BLACK);
        if (position == clickedPos) {
            holder.itemView.setBackgroundResource(R.drawable.dr_acceptable_cash_selected);
            Helper.changeShapeColorToMainAppColor(holder.itemView);
        } else
            holder.itemView.setBackgroundResource(R.drawable.dr_acceptable_cash_unselected);

        container.setOnClickListener(v -> {
            clickedPos = position;
            notifyDataSetChanged();
            singleArgRunnable.run(list.get(position));
        });
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
