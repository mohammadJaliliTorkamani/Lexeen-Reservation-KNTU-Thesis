package ir.ac.kntu.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.ac.kntu.R;
import ir.ac.kntu.Technical.CustomView.TextViewPlus;
import ir.ac.kntu.Technical.Other.CustomRunnable.Runnable_SingleArg;
import ir.ac.kntu.Technical.Other.Other.ContextHelper;
import ir.ac.kntu.Technical.Other.Other.Helper;

public class Adapter_Roof extends RecyclerView.Adapter {
    private static int clicked_pos;
    private List<Integer> list;
    private Runnable_SingleArg<Integer> onClick;
    private RecyclerView.Adapter tableAdapter;


    public Adapter_Roof(List<Integer> roofs, RecyclerView.Adapter adapter, int default_index, Runnable_SingleArg<Integer> onClick) {
        this.list = roofs;
        this.tableAdapter = adapter;
        clicked_pos = default_index;
        this.onClick = onClick;
    }

    /**
     * creates new viewHolder UI object with XML demonstrating rows layout
     *
     * @param parent   to specify the parent of the current position
     * @param viewType to specify ViewType of the current position
     * @return ViewHolder row Object
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(ContextHelper.retrieveContext()).inflate(R.layout.item_roof, parent, false));
    }

    /**
     * initialize view object values + managers  from the list
     *
     * @param holder   to access item UI object
     * @param position position of the current row
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TextViewPlus text = holder.itemView.findViewById(R.id.roof_item_text);
        text.setTextColor(Color.parseColor(Helper.getInstance().getMainAppColor()));
        text.setText(String.valueOf(list.get(position)));
        if (position == clicked_pos) {
            text.setBackgroundResource(R.drawable.dr_selected_roof);
            Helper.getInstance().changeShapeColorToMainAppColor(text);
        } else {
            text.setBackgroundResource(R.drawable.dr_unselected_roof);
            Helper.getInstance().changeStrokeColorToMainAppColor(text);
        }
        text.setTextColor(Color.parseColor(position == clicked_pos ? "#FFFFFF" : Helper.getInstance().getMainAppColor()));
        text.setOnClickListener(v -> {
            if (clicked_pos != position) {
                clicked_pos = position;
                onClick.run(list.get(position));
                notifyDataSetChanged();
            }
        });
    }

    /**
     * counts items available in list
     *
     * @return size
     */
    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    /**
     * returnd clicked position
     *
     * @return
     */
    public int getClickedPosition() {
        return clicked_pos;
    }

    /**
     * define ViewHolder class to store each row object
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        private View itemView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }
}
