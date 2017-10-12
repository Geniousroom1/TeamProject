package edu.android.teamproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class StartActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        startActivity(new Intent(this, LoadingActivity.class));
    }//end onCreate
    public void goSecondActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent); // SecondActivity 를 실행
        finish(); // StartActivity 를 종료.
    }//end goSecondActivity
} // end class StartActivity
