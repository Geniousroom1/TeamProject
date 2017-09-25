package edu.android.teamproject;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
            R.id.button1, R.id.button2, R.id.button3, R.id.button4,
            R.id.button5, R.id.button6, R.id.button7, R.id.button8,
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
        switch (btnId) {
            case R.id.button1:




        }

    }


}
