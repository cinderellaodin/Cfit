package com.odin.cfit;


import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

/*import android.support.v4.app.Fragment;*/


public class diet extends Fragment {

TextView tvdiet;
     ImageButton bt_toggle_text;
     Button bt_hide_text;
     View lyt_expand_text;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Diet");


        //intitialized
        tvdiet = (TextView) view.findViewById(R.id.dietfrag_txt);
        tvdiet.setText("Click the Fab Button to calculate your required calorie to lose weight.");

        bt_toggle_text = (ImageButton) view.findViewById(R.id.bt_toggle_text);
        bt_hide_text = (Button) view.findViewById(R.id.bt_hide_text);
        lyt_expand_text = (View) view.findViewById(R.id.lyt_expand_text);
        lyt_expand_text.setVisibility(View.GONE);

        initComponent();
        
    }

    public void initComponent(){
        // text section


        bt_toggle_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionText(bt_toggle_text);
            }
        });

        bt_hide_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionText(bt_toggle_text);
            }
        });
    }
    private void toggleSectionText(View view) {
        boolean show = toggleArrow(view);
        if (show) {
            ViewAnimation.expand(lyt_expand_text, new ViewAnimation.AnimListener() {
                @Override
                public void onFinish() {
                   // Tools.nestedScrollTo(nested_scroll_view, lyt_expand_text);
                }
            });
        } else {
            ViewAnimation.collapse(lyt_expand_text);
        }
    }

    public boolean toggleArrow(View view) {
        if (view.getRotation() == 0) {
            view.animate().setDuration(200).rotation(180);
            return true;
        } else {
            view.animate().setDuration(200).rotation(0);
            return false;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_diet, container, false);
    }

}
