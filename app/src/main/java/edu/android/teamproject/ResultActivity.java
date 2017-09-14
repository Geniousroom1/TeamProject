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
    private ConstraintLayout rlBottomSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        //private 변수로 설정한 키들을 activity_result.xml에서 알맞는 키를 찾아줌
        rlBottomSheet = (ConstraintLayout) findViewById(R.id.rl_bottom_sheet);
        mainImage = (ImageView) findViewById(R.id.mainImage);
        changeImage = (ImageView)findViewById(R.id.changeImage);
        btn1 = (ImageButton) findViewById(R.id.imageButton);
        btn2 = (ImageButton) findViewById(R.id.imageButton2);
        btn3 = (ImageButton) findViewById(R.id.imageButton3);
        // end search

        //버튼 2,3번과 이모티콘이미지(test용) 을 invisible (보이지않는) 상태로 설정
        btn2.setVisibility(View.INVISIBLE);
        btn3.setVisibility(View.INVISIBLE);
        changeImage.setVisibility(View.INVISIBLE);
        //end set.invisible

        // MainImage 에 secondActivity에서 저장한 사진을 set
        mainImage.setImageBitmap(SecondActivity.bit);
        mainImage.setClickable(true);
        //end setMainImage

        // toggle 용으로 쓸 boolean 값들을 true 로 설정
        turn = true;
        change1 = true;
        // end toggle boolean

//        imageView.setBackground(new ShapeDrawable(new OvalShape())); //뒤에 검은 배경화면 추가

        // btn1 (처음 보이는 가장 큰 버튼) onclick 메소드
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (turn==false) {
                    //버튼이 보이지않는 상태로 변경 toggle값을 ,false로 설정
                    btn2.setVisibility(View.INVISIBLE);
                    btn3.setVisibility(View.INVISIBLE);
                    turn = true;
                } else {
                    //버튼이 보이는 상태로 변경 toggle값을 ,false로 설정
                    btn2.setVisibility(View.VISIBLE);
                    btn3.setVisibility(View.VISIBLE);
                    turn = false;
                }
            }
        });//end setOnClickListener (First btn)

        // bottomSheet (아래에 띄우는 바) 의 기능 설정
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(rlBottomSheet);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                //띄워진 상태 (토글)/ 들어간 상태 : 상태가 변할때 한번 실행됨
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                //계속 실행됨 slide 상태에서는.
            }
        });//end BottomSheetBehavior

    }//end onCreate


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }//end onActivityResult

    //테스트용 이모티콘 추가 기능 (태기형 이미지 버튼 2번)
    public void testOnClick(View view) {
        if (change1==false) {
            changeImage.setVisibility(View.INVISIBLE);
            change1 = true;
        } else {
            changeImage.setVisibility(View.VISIBLE);
            change1 = false;
        }
    }//end testOnClick
}//end class ResultActivity












