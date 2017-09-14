package edu.android.teamproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class ResultActivity extends AppCompatActivity {


    // 멤버 변수 선언
    private ImageView mainImage,changeImage;
    private ImageButton btn1, btn2, btn3;
    private boolean turn;
    private boolean change1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        mainImage = (ImageView) findViewById(R.id.mainImage);
        changeImage = (ImageView)findViewById(R.id.changeImage);
        btn1 = (ImageButton) findViewById(R.id.imageButton);
        btn2 = (ImageButton) findViewById(R.id.imageButton2);
        btn3 = (ImageButton) findViewById(R.id.imageButton3);
        ConstraintLayout rlBottomSheet = (ConstraintLayout) findViewById(R.id.rl_bottom_sheet);
        btn2.setVisibility(View.INVISIBLE);
        btn3.setVisibility(View.INVISIBLE);
        turn = true;
        changeImage.setVisibility(View.INVISIBLE);
        change1 = true;

        mainImage.setImageBitmap(SecondActivity.bit);
//        imageView.setBackground(new ShapeDrawable(new OvalShape())); //뒤에 검은 배경화면
        mainImage.setClickable(true);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (turn==false) {
                    btn2.setVisibility(View.INVISIBLE);
                    btn3.setVisibility(View.INVISIBLE);
                    turn = true;
                } else {
                    btn2.setVisibility(View.VISIBLE);
                    btn3.setVisibility(View.VISIBLE);
                    turn = false;
                }
            }
        });//end setOnClickListener (First btn)

        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(rlBottomSheet);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                //계속 실행됨

            }
        });

    }//end onCreate


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void firsttest() {
        //TODO : nothing to say
    }

    public void testOnClick(View view) {
        if (change1==false) {
            changeImage.setVisibility(View.INVISIBLE);
            change1 = true;
        } else {
            changeImage.setVisibility(View.VISIBLE);
            change1 = false;
        }
    }
}//end class ResultActivity












