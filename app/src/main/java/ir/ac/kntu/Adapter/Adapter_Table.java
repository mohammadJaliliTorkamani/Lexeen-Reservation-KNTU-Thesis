package ir.ac.kntu.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ir.ac.kntu.DataBase.Database;
import ir.ac.kntu.Entity.Bill;
import ir.ac.kntu.Entity.ChairSet;
import ir.ac.kntu.Entity.Desk;
import ir.ac.kntu.Entity.FullDesk;
import ir.ac.kntu.Entity.G_Desk;
import ir.ac.kntu.Entity.GraphicChairSet;
import ir.ac.kntu.Entity.TableInfo;
import ir.ac.kntu.Interface.Retrofit.Table_Server_API;
import ir.ac.kntu.R;
import ir.ac.kntu.Server.Connector;
import ir.ac.kntu.Technical.Other.CustomRunnable.Runnable_SingleArg;
import ir.ac.kntu.Technical.Other.Other.Constants;
import ir.ac.kntu.Technical.Other.Other.ContextHelper;
import ir.ac.kntu.Technical.Other.Other.Helper;
import ir.ac.kntu.Technical.Other.Other.Helper_Log;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Adapter_Table extends RecyclerView.Adapter<Adapter_Table.ViewHolder> {
    private List<FullDesk> fullDesks = new LinkedList<>();
    private TableInfo tableInfo;

    public Adapter_Table(TableInfo tableInfo) {
        this.tableInfo = tableInfo;
    }


    public void clearFullDesks() {
        fullDesks.clear();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(ContextHelper.retrieveContext()).inflate(R.layout.item_table_container_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int row) {
        LinearLayout linearLayout = holder.itemView.findViewById(R.id.table_container_row_item_linear_layout);
        linearLayout.removeAllViews();
        linearLayout.setVisibility(View.INVISIBLE);
        for (int column = 0; column < tableInfo.getMaxCol(); column++) {
            ConstraintLayout constraintLayout = new ConstraintLayout(ContextHelper.retrieveContext());
            ConstraintSet constraintSet = new ConstraintSet();
            constraintLayout.setId(View.generateViewId());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
            layoutParams.gravity = RelativeLayout.LAYOUT_DIRECTION_RTL;
            layoutParams.weight = 1;
            constraintLayout.setLayoutParams(layoutParams);
            linearLayout.addView(constraintLayout);
            Desk desk = Desk.getDeskWithRowCol(tableInfo, row, column);
            G_Desk g_desk = createCorresponding_G_Desk(desk);
            if (desk != null && g_desk != null) {
                fullDesks.add(new FullDesk(desk, g_desk));
                constraintLayout.addView(g_desk);
                constraintSet.clone(constraintLayout);
                getChairsOf(holder.itemView, desk.getId(), chairSet -> {
                    GraphicChairSet graphicChairSet = ChairSet.populateGraphics(chairSet, tableInfo.getMaxCol());
                    if (g_desk.getType().getValue() == G_Desk.DeskType.CORNER.getValue() && g_desk.getCornerType().getValue() == G_Desk.CornerType.START.getValue()) {
                        constraintLayout.addView(graphicChairSet.getGTopChair());
                        constraintLayout.addView(graphicChairSet.getGStartChair());
                        constraintLayout.addView(graphicChairSet.getGBottomChair());
                        graphicChairSet.getGBottomChair().startAnimation(AnimationUtils.loadAnimation(ContextHelper.retrieveContext(), R.anim.scale_horizontal));
                        graphicChairSet.getGStartChair().startAnimation(AnimationUtils.loadAnimation(ContextHelper.retrieveContext(), R.anim.scale_vertical));
                        graphicChairSet.getGTopChair().startAnimation(AnimationUtils.loadAnimation(ContextHelper.retrieveContext(), R.anim.scale_horizontal));

                        ConstraintLayout.LayoutParams layoutParams1 = new ConstraintLayout.LayoutParams(graphicChairSet.getGTopChair().getLayoutParams());
                        layoutParams1.bottomMargin = 17;
                        graphicChairSet.getGTopChair().setLayoutParams(layoutParams1);

                        ConstraintLayout.LayoutParams layoutParams2 = new ConstraintLayout.LayoutParams(graphicChairSet.getGBottomChair().getLayoutParams());
                        layoutParams2.topMargin = 17;
                        graphicChairSet.getGBottomChair().setLayoutParams(layoutParams2);

                        ConstraintLayout.LayoutParams layoutParams3 = new ConstraintLayout.LayoutParams(graphicChairSet.getGStartChair().getLayoutParams());
                        layoutParams3.leftMargin = 17;
                        graphicChairSet.getGStartChair().setLayoutParams(layoutParams3);

                        constraintSet.clone(constraintLayout);

                        constraintSet.connect(g_desk.getId(), ConstraintSet.TOP, constraintLayout.getId(), ConstraintSet.TOP);
                        constraintSet.connect(g_desk.getId(), ConstraintSet.BOTTOM, constraintLayout.getId(), ConstraintSet.BOTTOM);
                        constraintSet.connect(g_desk.getId(), ConstraintSet.START, constraintLayout.getId(), ConstraintSet.START);

                        constraintSet.connect(graphicChairSet.getGTopChair().getId(), ConstraintSet.START, g_desk.getId(), ConstraintSet.START);
                        constraintSet.connect(graphicChairSet.getGTopChair().getId(), ConstraintSet.END, g_desk.getId(), ConstraintSet.END);
                        constraintSet.connect(graphicChairSet.getGTopChair().getId(), ConstraintSet.BOTTOM, g_desk.getId(), ConstraintSet.TOP);

                        constraintSet.connect(graphicChairSet.getGStartChair().getId(), ConstraintSet.TOP, g_desk.getId(), ConstraintSet.TOP);
                        constraintSet.connect(graphicChairSet.getGStartChair().getId(), ConstraintSet.BOTTOM, g_desk.getId(), ConstraintSet.BOTTOM);
                        constraintSet.connect(graphicChairSet.getGStartChair().getId(), ConstraintSet.START, g_desk.getId(), ConstraintSet.END);

                        constraintSet.connect(graphicChairSet.getGBottomChair().getId(), ConstraintSet.START, g_desk.getId(), ConstraintSet.START);
                        constraintSet.connect(graphicChairSet.getGBottomChair().getId(), ConstraintSet.END, g_desk.getId(), ConstraintSet.END);
                        constraintSet.connect(graphicChairSet.getGBottomChair().getId(), ConstraintSet.TOP, g_desk.getId(), ConstraintSet.BOTTOM);

                    } else if (g_desk.getType().getValue() == G_Desk.DeskType.CORNER.getValue() && g_desk.getCornerType().getValue() == G_Desk.CornerType.END.getValue()) {
                        constraintLayout.addView(graphicChairSet.getGTopChair());
                        constraintLayout.addView(graphicChairSet.getGEndChair());
                        constraintLayout.addView(graphicChairSet.getGBottomChair());
                        graphicChairSet.getGBottomChair().startAnimation(AnimationUtils.loadAnimation(ContextHelper.retrieveContext(), R.anim.scale_vertical));
                        graphicChairSet.getGEndChair().startAnimation(AnimationUtils.loadAnimation(ContextHelper.retrieveContext(), R.anim.scale_horizontal));
                        graphicChairSet.getGTopChair().startAnimation(AnimationUtils.loadAnimation(ContextHelper.retrieveContext(), R.anim.scale_horizontal));

                        ConstraintLayout.LayoutParams layoutParams1 = new ConstraintLayout.LayoutParams(graphicChairSet.getGTopChair().getLayoutParams());
                        layoutParams1.bottomMargin = 17;
                        graphicChairSet.getGTopChair().setLayoutParams(layoutParams1);

                        ConstraintLayout.LayoutParams layoutParams2 = new ConstraintLayout.LayoutParams(graphicChairSet.getGBottomChair().getLayoutParams());
                        layoutParams2.topMargin = 17;
                        graphicChairSet.getGBottomChair().setLayoutParams(layoutParams2);

                        ConstraintLayout.LayoutParams layoutParams3 = new ConstraintLayout.LayoutParams(graphicChairSet.getGEndChair().getLayoutParams());
                        layoutParams3.rightMargin = 17;
                        graphicChairSet.getGEndChair().setLayoutParams(layoutParams3);

                        constraintSet.clone(constraintLayout);

                        constraintSet.connect(g_desk.getId(), ConstraintSet.TOP, constraintLayout.getId(), ConstraintSet.TOP);
                        constraintSet.connect(g_desk.getId(), ConstraintSet.BOTTOM, constraintLayout.getId(), ConstraintSet.BOTTOM);
                        constraintSet.connect(g_desk.getId(), ConstraintSet.END, constraintLayout.getId(), ConstraintSet.END);

                        constraintSet.connect(graphicChairSet.getGTopChair().getId(), ConstraintSet.START, g_desk.getId(), ConstraintSet.START);
                        constraintSet.connect(graphicChairSet.getGTopChair().getId(), ConstraintSet.END, g_desk.getId(), ConstraintSet.END);
                        constraintSet.connect(graphicChairSet.getGTopChair().getId(), ConstraintSet.BOTTOM, g_desk.getId(), ConstraintSet.TOP);

                        constraintSet.connect(graphicChairSet.getGEndChair().getId(), ConstraintSet.TOP, g_desk.getId(), ConstraintSet.TOP);
                        constraintSet.connect(graphicChairSet.getGEndChair().getId(), ConstraintSet.BOTTOM, g_desk.getId(), ConstraintSet.BOTTOM);
                        constraintSet.connect(graphicChairSet.getGEndChair().getId(), ConstraintSet.END, g_desk.getId(), ConstraintSet.START);

                        constraintSet.connect(graphicChairSet.getGBottomChair().getId(), ConstraintSet.START, g_desk.getId(), ConstraintSet.START);
                        constraintSet.connect(graphicChairSet.getGBottomChair().getId(), ConstraintSet.END, g_desk.getId(), ConstraintSet.END);
                        constraintSet.connect(graphicChairSet.getGBottomChair().getId(), ConstraintSet.TOP, g_desk.getId(), ConstraintSet.BOTTOM);
                    } else if (g_desk.getType().getValue() == G_Desk.DeskType.CORNER.getValue() && g_desk.getCornerType().getValue() == G_Desk.CornerType.TOP.getValue()) {
                        constraintLayout.addView(graphicChairSet.getGTopChair());
                        constraintLayout.addView(graphicChairSet.getGEndChair());
                        constraintLayout.addView(graphicChairSet.getGStartChair());
                        graphicChairSet.getGEndChair().startAnimation(AnimationUtils.loadAnimation(ContextHelper.retrieveContext(), R.anim.scale_vertical));
                        graphicChairSet.getGStartChair().startAnimation(AnimationUtils.loadAnimation(ContextHelper.retrieveContext(), R.anim.scale_vertical));
                        graphicChairSet.getGTopChair().startAnimation(AnimationUtils.loadAnimation(ContextHelper.retrieveContext(), R.anim.scale_horizontal));


                        ConstraintLayout.LayoutParams layoutParams1 = new ConstraintLayout.LayoutParams(graphicChairSet.getGTopChair().getLayoutParams());
                        layoutParams1.bottomMargin = 17;
                        graphicChairSet.getGTopChair().setLayoutParams(layoutParams1);

                        ConstraintLayout.LayoutParams layoutParams2 = new ConstraintLayout.LayoutParams(graphicChairSet.getGStartChair().getLayoutParams());
                        layoutParams2.leftMargin = 17;
                        graphicChairSet.getGStartChair().setLayoutParams(layoutParams2);

                        ConstraintLayout.LayoutParams layoutParams3 = new ConstraintLayout.LayoutParams(graphicChairSet.getGEndChair().getLayoutParams());
                        layoutParams3.rightMargin = 17;
                        graphicChairSet.getGEndChair().setLayoutParams(layoutParams3);

                        constraintSet.clone(constraintLayout);

                        constraintSet.connect(g_desk.getId(), ConstraintSet.START, constraintLayout.getId(), ConstraintSet.START);
                        constraintSet.connect(g_desk.getId(), ConstraintSet.END, constraintLayout.getId(), ConstraintSet.END);
                        constraintSet.connect(g_desk.getId(), ConstraintSet.BOTTOM, constraintLayout.getId(), ConstraintSet.BOTTOM);

                        constraintSet.connect(graphicChairSet.getGTopChair().getId(), ConstraintSet.START, g_desk.getId(), ConstraintSet.START);
                        constraintSet.connect(graphicChairSet.getGTopChair().getId(), ConstraintSet.END, g_desk.getId(), ConstraintSet.END);
                        constraintSet.connect(graphicChairSet.getGTopChair().getId(), ConstraintSet.BOTTOM, g_desk.getId(), ConstraintSet.TOP);

                        constraintSet.connect(graphicChairSet.getGEndChair().getId(), ConstraintSet.TOP, g_desk.getId(), ConstraintSet.TOP);
                        constraintSet.connect(graphicChairSet.getGEndChair().getId(), ConstraintSet.BOTTOM, g_desk.getId(), ConstraintSet.BOTTOM);
                        constraintSet.connect(graphicChairSet.getGEndChair().getId(), ConstraintSet.END, g_desk.getId(), ConstraintSet.START);

                        constraintSet.connect(graphicChairSet.getGStartChair().getId(), ConstraintSet.TOP, g_desk.getId(), ConstraintSet.TOP);
                        constraintSet.connect(graphicChairSet.getGStartChair().getId(), ConstraintSet.START, g_desk.getId(), ConstraintSet.END);
                        constraintSet.connect(graphicChairSet.getGStartChair().getId(), ConstraintSet.BOTTOM, g_desk.getId(), ConstraintSet.BOTTOM);
                    } else if (g_desk.getType().getValue() == G_Desk.DeskType.CORNER.getValue() && g_desk.getCornerType().getValue() == G_Desk.CornerType.BOTTOM.getValue()) {
                        constraintLayout.addView(graphicChairSet.getGBottomChair());
                        constraintLayout.addView(graphicChairSet.getGEndChair());
                        constraintLayout.addView(graphicChairSet.getGStartChair());
                        graphicChairSet.getGEndChair().startAnimation(AnimationUtils.loadAnimation(ContextHelper.retrieveContext(), R.anim.scale_vertical));
                        graphicChairSet.getGStartChair().startAnimation(AnimationUtils.loadAnimation(ContextHelper.retrieveContext(), R.anim.scale_vertical));
                        graphicChairSet.getGBottomChair().startAnimation(AnimationUtils.loadAnimation(ContextHelper.retrieveContext(), R.anim.scale_horizontal));

                        ConstraintLayout.LayoutParams layoutParams1 = new ConstraintLayout.LayoutParams(graphicChairSet.getGBottomChair().getLayoutParams());
                        layoutParams1.topMargin = 17;
                        graphicChairSet.getGBottomChair().setLayoutParams(layoutParams1);

                        ConstraintLayout.LayoutParams layoutParams2 = new ConstraintLayout.LayoutParams(graphicChairSet.getGStartChair().getLayoutParams());
                        layoutParams2.leftMargin = 17;
                        graphicChairSet.getGStartChair().setLayoutParams(layoutParams2);

                        ConstraintLayout.LayoutParams layoutParams3 = new ConstraintLayout.LayoutParams(graphicChairSet.getGEndChair().getLayoutParams());
                        layoutParams3.rightMargin = 17;
                        graphicChairSet.getGEndChair().setLayoutParams(layoutParams3);

                        constraintSet.clone(constraintLayout);

                        constraintSet.connect(g_desk.getId(), ConstraintSet.START, constraintLayout.getId(), ConstraintSet.START);
                        constraintSet.connect(g_desk.getId(), ConstraintSet.END, constraintLayout.getId(), ConstraintSet.END);
                        constraintSet.connect(g_desk.getId(), ConstraintSet.TOP, constraintLayout.getId(), ConstraintSet.TOP);

                        constraintSet.connect(graphicChairSet.getGBottomChair().getId(), ConstraintSet.START, g_desk.getId(), ConstraintSet.START);
                        constraintSet.connect(graphicChairSet.getGBottomChair().getId(), ConstraintSet.END, g_desk.getId(), ConstraintSet.END);
                        constraintSet.connect(graphicChairSet.getGBottomChair().getId(), ConstraintSet.TOP, g_desk.getId(), ConstraintSet.BOTTOM);

                        constraintSet.connect(graphicChairSet.getGEndChair().getId(), ConstraintSet.TOP, g_desk.getId(), ConstraintSet.TOP);
                        constraintSet.connect(graphicChairSet.getGEndChair().getId(), ConstraintSet.BOTTOM, g_desk.getId(), ConstraintSet.BOTTOM);
                        constraintSet.connect(graphicChairSet.getGEndChair().getId(), ConstraintSet.END, g_desk.getId(), ConstraintSet.START);

                        constraintSet.connect(graphicChairSet.getGStartChair().getId(), ConstraintSet.TOP, g_desk.getId(), ConstraintSet.TOP);
                        constraintSet.connect(graphicChairSet.getGStartChair().getId(), ConstraintSet.START, g_desk.getId(), ConstraintSet.END);
                        constraintSet.connect(graphicChairSet.getGStartChair().getId(), ConstraintSet.BOTTOM, g_desk.getId(), ConstraintSet.BOTTOM);

                    } else if (g_desk.getType().getValue() == G_Desk.DeskType.FULL.getValue()) {
                        constraintLayout.addView(graphicChairSet.getGBottomChair());
                        constraintLayout.addView(graphicChairSet.getGTopChair());
                        constraintLayout.addView(graphicChairSet.getGEndChair());
                        constraintLayout.addView(graphicChairSet.getGStartChair());
                        graphicChairSet.getGEndChair().startAnimation(AnimationUtils.loadAnimation(ContextHelper.retrieveContext(), R.anim.scale_vertical));
                        graphicChairSet.getGStartChair().startAnimation(AnimationUtils.loadAnimation(ContextHelper.retrieveContext(), R.anim.scale_vertical));
                        graphicChairSet.getGTopChair().startAnimation(AnimationUtils.loadAnimation(ContextHelper.retrieveContext(), R.anim.scale_horizontal));
                        graphicChairSet.getGBottomChair().startAnimation(AnimationUtils.loadAnimation(ContextHelper.retrieveContext(), R.anim.scale_horizontal));


                        ConstraintLayout.LayoutParams layoutParams1 = new ConstraintLayout.LayoutParams(graphicChairSet.getGTopChair().getLayoutParams());
                        layoutParams1.bottomMargin = 17;
                        graphicChairSet.getGTopChair().setLayoutParams(layoutParams1);

                        ConstraintLayout.LayoutParams layoutParams2 = new ConstraintLayout.LayoutParams(graphicChairSet.getGStartChair().getLayoutParams());
                        layoutParams2.leftMargin = 17;
                        graphicChairSet.getGStartChair().setLayoutParams(layoutParams2);

                        ConstraintLayout.LayoutParams layoutParams3 = new ConstraintLayout.LayoutParams(graphicChairSet.getGEndChair().getLayoutParams());
                        layoutParams3.rightMargin = 17;
                        graphicChairSet.getGEndChair().setLayoutParams(layoutParams3);

                        ConstraintLayout.LayoutParams layoutParams4 = new ConstraintLayout.LayoutParams(graphicChairSet.getGBottomChair().getLayoutParams());
                        layoutParams4.topMargin = 17;
                        graphicChairSet.getGBottomChair().setLayoutParams(layoutParams4);

                        constraintSet.clone(constraintLayout);

                        constraintSet.connect(g_desk.getId(), ConstraintSet.START, constraintLayout.getId(), ConstraintSet.START);
                        constraintSet.connect(g_desk.getId(), ConstraintSet.END, constraintLayout.getId(), ConstraintSet.END);
                        constraintSet.connect(g_desk.getId(), ConstraintSet.TOP, constraintLayout.getId(), ConstraintSet.TOP);
                        constraintSet.connect(g_desk.getId(), ConstraintSet.BOTTOM, constraintLayout.getId(), ConstraintSet.BOTTOM);

                        constraintSet.connect(graphicChairSet.getGBottomChair().getId(), ConstraintSet.START, g_desk.getId(), ConstraintSet.START);
                        constraintSet.connect(graphicChairSet.getGBottomChair().getId(), ConstraintSet.END, g_desk.getId(), ConstraintSet.END);
                        constraintSet.connect(graphicChairSet.getGBottomChair().getId(), ConstraintSet.TOP, g_desk.getId(), ConstraintSet.BOTTOM);

                        constraintSet.connect(graphicChairSet.getGEndChair().getId(), ConstraintSet.TOP, g_desk.getId(), ConstraintSet.TOP);
                        constraintSet.connect(graphicChairSet.getGEndChair().getId(), ConstraintSet.BOTTOM, g_desk.getId(), ConstraintSet.BOTTOM);
                        constraintSet.connect(graphicChairSet.getGEndChair().getId(), ConstraintSet.END, g_desk.getId(), ConstraintSet.START);

                        constraintSet.connect(graphicChairSet.getGStartChair().getId(), ConstraintSet.TOP, g_desk.getId(), ConstraintSet.TOP);
                        constraintSet.connect(graphicChairSet.getGStartChair().getId(), ConstraintSet.START, g_desk.getId(), ConstraintSet.END);
                        constraintSet.connect(graphicChairSet.getGStartChair().getId(), ConstraintSet.BOTTOM, g_desk.getId(), ConstraintSet.BOTTOM);

                        constraintSet.connect(graphicChairSet.getGTopChair().getId(), ConstraintSet.START, g_desk.getId(), ConstraintSet.START);
                        constraintSet.connect(graphicChairSet.getGTopChair().getId(), ConstraintSet.END, g_desk.getId(), ConstraintSet.END);
                        constraintSet.connect(graphicChairSet.getGTopChair().getId(), ConstraintSet.BOTTOM, g_desk.getId(), ConstraintSet.TOP);

                    } else if (g_desk.getType().getValue() == G_Desk.DeskType.MIDDLE.getValue() && g_desk.getMiddleType().getValue() == G_Desk.MiddleType.HORIZONTAL.getValue()) {
                        constraintLayout.addView(graphicChairSet.getGBottomChair());
                        constraintLayout.addView(graphicChairSet.getGTopChair());
                        graphicChairSet.getGTopChair().startAnimation(AnimationUtils.loadAnimation(ContextHelper.retrieveContext(), R.anim.scale_horizontal));
                        graphicChairSet.getGBottomChair().startAnimation(AnimationUtils.loadAnimation(ContextHelper.retrieveContext(), R.anim.scale_horizontal));


                        ConstraintLayout.LayoutParams layoutParams1 = new ConstraintLayout.LayoutParams(graphicChairSet.getGTopChair().getLayoutParams());
                        layoutParams1.bottomMargin = 17;
                        graphicChairSet.getGTopChair().setLayoutParams(layoutParams1);

                        ConstraintLayout.LayoutParams layoutParams2 = new ConstraintLayout.LayoutParams(graphicChairSet.getGBottomChair().getLayoutParams());
                        layoutParams2.topMargin = 17;
                        graphicChairSet.getGBottomChair().setLayoutParams(layoutParams2);


                        constraintSet.clone(constraintLayout);

                        constraintSet.connect(g_desk.getId(), ConstraintSet.START, constraintLayout.getId(), ConstraintSet.START);
                        constraintSet.connect(g_desk.getId(), ConstraintSet.END, constraintLayout.getId(), ConstraintSet.END);
                        constraintSet.connect(g_desk.getId(), ConstraintSet.TOP, constraintLayout.getId(), ConstraintSet.TOP);
                        constraintSet.connect(g_desk.getId(), ConstraintSet.BOTTOM, constraintLayout.getId(), ConstraintSet.BOTTOM);

                        constraintSet.connect(graphicChairSet.getGBottomChair().getId(), ConstraintSet.START, g_desk.getId(), ConstraintSet.START);
                        constraintSet.connect(graphicChairSet.getGBottomChair().getId(), ConstraintSet.END, g_desk.getId(), ConstraintSet.END);
                        constraintSet.connect(graphicChairSet.getGBottomChair().getId(), ConstraintSet.BOTTOM, g_desk.getId(), ConstraintSet.TOP);

                        constraintSet.connect(graphicChairSet.getGTopChair().getId(), ConstraintSet.START, g_desk.getId(), ConstraintSet.START);
                        constraintSet.connect(graphicChairSet.getGTopChair().getId(), ConstraintSet.END, g_desk.getId(), ConstraintSet.END);
                        constraintSet.connect(graphicChairSet.getGTopChair().getId(), ConstraintSet.TOP, g_desk.getId(), ConstraintSet.BOTTOM);


                    } else if (g_desk.getType().getValue() == G_Desk.DeskType.MIDDLE.getValue() && g_desk.getMiddleType().getValue() == G_Desk.MiddleType.VERTICAL.getValue()) {
                        constraintLayout.addView(graphicChairSet.getGStartChair());
                        constraintLayout.addView(graphicChairSet.getGEndChair());
                        graphicChairSet.getGEndChair().startAnimation(AnimationUtils.loadAnimation(ContextHelper.retrieveContext(), R.anim.scale_vertical));
                        graphicChairSet.getGStartChair().startAnimation(AnimationUtils.loadAnimation(ContextHelper.retrieveContext(), R.anim.scale_vertical));

                        ConstraintLayout.LayoutParams layoutParams1 = new ConstraintLayout.LayoutParams(graphicChairSet.getGStartChair().getLayoutParams());
                        layoutParams1.leftMargin = 17;
                        graphicChairSet.getGStartChair().setLayoutParams(layoutParams1);

                        ConstraintLayout.LayoutParams layoutParams2 = new ConstraintLayout.LayoutParams(graphicChairSet.getGEndChair().getLayoutParams());
                        layoutParams2.rightMargin = 17;
                        graphicChairSet.getGEndChair().setLayoutParams(layoutParams2);

                        constraintSet.clone(constraintLayout);

                        constraintSet.connect(g_desk.getId(), ConstraintSet.START, constraintLayout.getId(), ConstraintSet.START);
                        constraintSet.connect(g_desk.getId(), ConstraintSet.END, constraintLayout.getId(), ConstraintSet.END);
                        constraintSet.connect(g_desk.getId(), ConstraintSet.TOP, constraintLayout.getId(), ConstraintSet.TOP);
                        constraintSet.connect(g_desk.getId(), ConstraintSet.BOTTOM, constraintLayout.getId(), ConstraintSet.BOTTOM);

                        constraintSet.connect(graphicChairSet.getGStartChair().getId(), ConstraintSet.TOP, g_desk.getId(), ConstraintSet.TOP);
                        constraintSet.connect(graphicChairSet.getGStartChair().getId(), ConstraintSet.BOTTOM, g_desk.getId(), ConstraintSet.BOTTOM);
                        constraintSet.connect(graphicChairSet.getGStartChair().getId(), ConstraintSet.END, g_desk.getId(), ConstraintSet.START);

                        constraintSet.connect(graphicChairSet.getGEndChair().getId(), ConstraintSet.TOP, g_desk.getId(), ConstraintSet.TOP);
                        constraintSet.connect(graphicChairSet.getGEndChair().getId(), ConstraintSet.BOTTOM, g_desk.getId(), ConstraintSet.BOTTOM);
                        constraintSet.connect(graphicChairSet.getGEndChair().getId(), ConstraintSet.START, g_desk.getId(), ConstraintSet.END);
                    }
                    g_desk.startAnimation(AnimationUtils.loadAnimation(ContextHelper.retrieveContext(), R.anim.scale_vertical));
                    constraintSet.applyTo(constraintLayout);
                    linearLayout.setVisibility(View.VISIBLE);
                });
            }
            if (row == tableInfo.getMaxRow() - 1 && column == tableInfo.getMaxCol() - 1) {  //means : if was finishing drawing
                List<List<FullDesk>> connectedComponents = createConnectedComponents(fullDesks);
                for (List<FullDesk> component : connectedComponents) {
                    for (FullDesk fullDesk : component) {
                        fullDesk.getG_desk().setOnClickListener(fullDesk.getDesk().isReserved() ? null : v -> {
                            if (!Database.getInstance(ContextHelper.retrieveContext(), Constants._MAIN_DATABASE).billInterface().getWithLexinTableID(fullDesk.getDesk().getLexinTableID(), Helper.getInstance().getSelectedRestaurantDecryptedQRCode()).isEmpty()) {
                                Database.getInstance(ContextHelper.retrieveContext(), Constants._MAIN_DATABASE).billInterface().deleteWithLexinTableID(fullDesk.getDesk().getLexinTableID(), Helper.getInstance().getSelectedRestaurantDecryptedQRCode());
                                for (FullDesk item : component)
                                    item.getG_desk().setReserveMode(G_Desk.Mode.NOT_RESERVED);
                            } else {
                                Database.getInstance(ContextHelper.retrieveContext(), Constants._MAIN_DATABASE).billInterface().insert(new Bill(fullDesk.getDesk().getLexinTableID()));
                                for (FullDesk item : component)
                                    item.getG_desk().setReserveMode(G_Desk.Mode.TO_RESERVE);
                            }
                        });

                    }
                }
            }
        }
    }

    private List<List<FullDesk>> createConnectedComponents(List<FullDesk> fullDesks) {
        List<List<FullDesk>> components = new LinkedList<>();
        HashMap<Integer, List<FullDesk>> map = new HashMap<>();
        for (FullDesk fullDesk : fullDesks) {
            if (!map.containsKey(fullDesk.getDesk().getLexinTableID())) {
                map.put(fullDesk.getDesk().getLexinTableID(), new LinkedList<>());
            }
            map.get(fullDesk.getDesk().getLexinTableID()).add(fullDesk);
        }

        for (Map.Entry<Integer, List<FullDesk>> entry : map.entrySet())
            components.add(entry.getValue());

        return components;
    }

    @Override
    public int getItemCount() {
        return tableInfo.getMaxRow();
    }

    private G_Desk createCorresponding_G_Desk(Desk desk) {
        if (desk == null)
            return null;
        G_Desk g_desk = new G_Desk(ContextHelper.retrieveContext());
        g_desk.setId(View.generateViewId());
        CoordinatorLayout.LayoutParams layoutParams = new CoordinatorLayout.LayoutParams(G_Desk.getWidthOf(desk, tableInfo.getMaxCol()), G_Desk.getHeightOf(desk, tableInfo.getMaxCol()));
        g_desk.setLayoutParams(layoutParams);
        g_desk.setType(desk.getDeskTypeDirection());
        if (g_desk.getType().getValue() == G_Desk.DeskType.MIDDLE.getValue()) {
            g_desk.setMiddleType(desk.getMiddleTypeMode());
        } else if (g_desk.getType().getValue() == G_Desk.DeskType.CORNER.getValue()) {
            g_desk.setCornerType(desk.getCornerType());
        }
        g_desk.setReserveMode(desk.isReserved() ? G_Desk.Mode.RESERVED : G_Desk.Mode.NOT_RESERVED);
        return g_desk;
    }

    private void getChairsOf(View view, int deskID, Runnable_SingleArg<ChairSet> runnable) {
        Connector.createService(view, Table_Server_API.class, object -> object.getChairs(deskID).enqueue(new Callback<ChairSet>() {
            @Override
            public void onResponse(Call<ChairSet> call, Response<ChairSet> response) {
                if (response.body() != null) {
                    runnable.run(response.body());
                } else
                    Helper_Log.errorLog(Adapter_Table.class);
            }

            @Override
            public void onFailure(Call<ChairSet> call, Throwable t) {
                Helper_Log.errorLog(t, Adapter_Table.class);
            }
        }));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private View itemView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }
}