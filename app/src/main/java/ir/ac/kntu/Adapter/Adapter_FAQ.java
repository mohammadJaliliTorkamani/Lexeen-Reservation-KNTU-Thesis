package ir.ac.kntu.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ir.ac.kntu.Entity.FAQ;
import ir.ac.kntu.R;
import ir.ac.kntu.Technical.Other.Other.ContextHelper;
import ir.ac.kntu.Technical.Other.Other.Helper;

public class Adapter_FAQ extends RecyclerView.Adapter<Adapter_FAQ.ViewHolder> {
    private List<FAQ> list;
    private int lastPosition = -1;

    public Adapter_FAQ(List<FAQ> list) {
        this.list = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(ContextHelper.retrieveContext()).inflate(R.layout.item_faq, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TextView question = holder.itemView.findViewById(R.id.faq_item_question);
        TextView answer = holder.itemView.findViewById(R.id.faq_item_answer);
        CircleImageView circleImageView = holder.itemView.findViewById(R.id.faq_item_circle);
        circleImageView.setBackgroundColor(Color.parseColor(Helper.getInstance().getMainAppColor()));
        question.setText(list.get(position).getQuestion());
        answer.setText(list.get(position).getAnswer());
        setAnimation(holder.itemView, position);
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation scale_animation = AnimationUtils.loadAnimation(ContextHelper.retrieveContext(), R.anim.scale_both);
            viewToAnimate.startAnimation(scale_animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View itemView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }
}
