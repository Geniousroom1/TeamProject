package edu.android.teamproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class StartActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }//end onCreate

    // goSecondActivity : SecondActivity class 를 intent 로 생성하여 실행하는 메소드
    public void goSecondActivity(View view) {
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent); // SecondActivity 를 실행
        finish(); // StartActivity 를 종료.
    }//end goSecondActivity
} // end class StartActivity
