package ir.ac.kntu.Entity;


import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;

import com.ahmadrosid.svgloader.SvgLoader;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.expand.ChildPosition;
import com.mindorks.placeholderview.annotations.expand.Collapse;
import com.mindorks.placeholderview.annotations.expand.Expand;
import com.mindorks.placeholderview.annotations.expand.Parent;
import com.mindorks.placeholderview.annotations.expand.ParentPosition;
import com.mindorks.placeholderview.annotations.expand.SingleTop;

import ir.ac.kntu.R;
import ir.ac.kntu.Technical.CustomView.TextViewPlus;
import ir.ac.kntu.Technical.Other.Other.ContextHelper;
import ir.ac.kntu.Technical.Other.Other.Encryption;
import ir.ac.kntu.Technical.Other.Other.Helper;
import ir.ac.kntu.Technical.Other.Other.Helper_Log;

@Parent
@SingleTop
@Layout(R.layout.item_menu_header)
public class MenuHeadingView {


    @View(R.id.item_header_left_side)
    android.view.View leftSide;

    @View(R.id.item_header_right_side)
    android.view.View rightSide;

    @View(R.id.item_header_top_side)
    android.view.View topSide;

    @View(R.id.item_header_bottom_side)
    android.view.View bottomSide;

    @View(R.id.item_header_name)
    TextViewPlus headingTxt;

    @View(R.id.item_header_item_arrow)
    ImageView toggleIcon;

    @View(R.id.item_header_image)
    ImageView imageIcon;

    @View(R.id.iteam_header_frame_card)
    CardView imageCard;

    @View(R.id.item_header_toggle_view)
    ConstraintLayout card;

    @ParentPosition
    int parentPosition;
    @ChildPosition
    int childPosition;

    private Category category;
    private Activity activity;


    public MenuHeadingView(Category category, FragmentActivity activity) {
        this.category = category;
        this.activity = activity;
    }

    /**
     * does design UI operations when item resolved to show
     */
    @Resolve
    public void onResolved() {
        try {
            SvgLoader.pluck().with(activity).load(Encryption.getInstance().decrypt(category.getLogos().get(0)), imageIcon);
        } catch (Exception e) {
            Helper_Log.errorLog(e, MenuHeadingView.class);
        }
        card.startAnimation(AnimationUtils.loadAnimation(ContextHelper.retrieveContext(), R.anim.scale_both));
        toggleIcon.setImageDrawable(ContextHelper.retrieveContext().getResources().getDrawable(R.drawable.ic_arrow));
        headingTxt.setText(Encryption.getInstance().decrypt(category.getName()));
        imageCard.setCardBackgroundColor(Color.parseColor(Encryption.getInstance().decrypt(category.getColor())));
        topSide.setVisibility(android.view.View.VISIBLE);
        bottomSide.setVisibility(android.view.View.VISIBLE);
        rightSide.setVisibility(android.view.View.VISIBLE);
        leftSide.setVisibility(android.view.View.VISIBLE);
    }

    /**
     * does design UI operations and initializations when expanded
     */
    @Expand
    public void onExpand() {

        RotateAnimation rotate = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(333);
        rotate.setFillAfter(true);
        rotate.setInterpolator(new LinearInterpolator());
        toggleIcon.startAnimation(rotate);


        bottomSide.setVisibility(android.view.View.GONE);


        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) card.getLayoutParams();
        layoutParams.bottomMargin = Helper.getInstance().dp2px(0);
        card.setLayoutParams(layoutParams);


        imageCard.setBackgroundResource(R.drawable.dr_rec_top_radius);
        ((GradientDrawable) imageCard.getBackground()).setColor(Color.parseColor(Encryption.getInstance().decrypt(category.getColor())));


        ViewGroup.MarginLayoutParams imageLayoutParams = (ViewGroup.MarginLayoutParams) imageCard.getLayoutParams();
        imageLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        imageLayoutParams.bottomMargin = Helper.getInstance().dp2px(0);
        imageCard.setLayoutParams(imageLayoutParams);

    }

    /**
     * does design UI operations when collapsed
     */
    @Collapse
    public void onCollapse() {

        RotateAnimation rotate = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(333);
        rotate.setFillAfter(true);
        rotate.setInterpolator(new LinearInterpolator());
        toggleIcon.startAnimation(rotate);


        bottomSide.setVisibility(android.view.View.VISIBLE);


        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) card.getLayoutParams();
        layoutParams.bottomMargin = Helper.getInstance().dp2px(16);
        card.setLayoutParams(layoutParams);

        imageCard.setBackgroundResource(R.drawable.dr_rec_category);

        ((GradientDrawable) imageCard.getBackground()).setColor(Color.parseColor(Encryption.getInstance().decrypt(category.getColor())));


        ConstraintLayout.LayoutParams layoutParams1 = (ConstraintLayout.LayoutParams) imageCard.getLayoutParams();
        layoutParams1.height = ContextHelper.retrieveContext().getResources().getDimensionPixelSize(R.dimen._45sdp);
        layoutParams1.width = ContextHelper.retrieveContext().getResources().getDimensionPixelSize(R.dimen._45sdp);
        layoutParams1.bottomMargin = ContextHelper.retrieveContext().getResources().getDimensionPixelSize(R.dimen._12sdp);
        imageCard.setLayoutParams(layoutParams1);
    }
}
