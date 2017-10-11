package edu.android.teamproject;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class FilterFragment extends Fragment implements View.OnClickListener {



    interface FilterListener {
        void onButtonClicked(int id);
    }

    private FilterListener listener;


    private int[] buttonIds = {
            R.id.filter_sunset, R.id.filter_forest, R.id.filter_sea, R.id.filter_bright,
            R.id.filter_more_bright, R.id.filter_neon, R.id.filter_neon2, R.id.filter_neon3,
            R.id.filter_paint, R.id.filter_paint2, R.id.filter_black, R.id.filter_black2,
            R.id.filter_black3, R.id.filter_black4, R.id.filter_black5, R.id.filter_sketch,
            R.id.filter_sketch2, R.id.filter_sketch3, R.id.filter_mosaic, R.id.filter_blur,
            R.id.filter_border, R.id.filter_romo, R.id.filter_white, R.id.filter_natural,
            R.id.filter_blue, R.id.filter_yellow, R.id.filter_green, R.id.filter_rainbow,
    };


    public FilterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FilterListener) {
            listener = (FilterListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filter, container, false);

    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();

        for(int i = 0; i < buttonIds.length; i++) {
            Button btn = view.findViewById(buttonIds[i]);
            btn.setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View view) {
        int btnId = view.getId();
        // TODO : 필터 버튼 클릭 시마다 할 일
       listener.onButtonClicked(btnId);

    }


}
