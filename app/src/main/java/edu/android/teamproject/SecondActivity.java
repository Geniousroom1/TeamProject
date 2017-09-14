package edu.android.teamproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {

    public static final int REQ_CODE_CAMERA = 1000; // 카메라 키값
    public static final int PICK_FROM_GALLERY = 1; // 갤러리 키값
    private static final int REQ_PERM_GALLERY = 10; // 갤러리 키값

    private Button btn_gallery; // 갤러리 (앨범) 을 실행하는 버튼

    public static Bitmap bit = null; // 그림을 저장할 공간.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallary);


        btn_gallery = (Button) findViewById(R.id.btn_gallery); // 갤러리 버튼을 찾음
        // 갤러리 버튼 클릭 메소드
        btn_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK); // 갤러리 인텐트 생성
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_GALLERY); //갤러리 인텐트 실행
            }//end onClick@
        });//end click Gallery

    }//end onCreate();


    // startActivityForResult 할때의 기능 override
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode== REQ_CODE_CAMERA && resultCode == RESULT_OK) {

            Bundle bundle =data.getExtras(); // data 인텐트를 저장하는 bundle 객체 생성

            if (bundle !=null) {
                bit = (Bitmap) bundle.get("data"); // 그림을 저장하는 bit 변수에 인텐트에서 받아온 번들값을 저장
                Intent nextI = new Intent(this, ResultActivity.class); //
                startActivity(nextI);
            } else  {
                Toast.makeText(this, "취소", Toast.LENGTH_SHORT).show();
            }
        }else{

        }//end Camera

        if (requestCode== PICK_FROM_GALLERY && resultCode == RESULT_OK) {
            Bundle bundle =data.getExtras();

            if (bundle != null) {
                bit = (Bitmap) bundle.get("data");
                Intent nextI = new Intent(this, ResultActivity.class);
                startActivity(nextI);
            } else {
                Toast.makeText(this, "취소", Toast.LENGTH_SHORT).show();
            }
        }else{

        }//end Gallery

    }//end onActivityResult

    // 카메라로 찍어서 값을 넘겨줌
    public void startCamera(View view) {
        int check = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if(check == PackageManager.PERMISSION_GRANTED){
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQ_CODE_CAMERA);
        }else {
            String[] permissions = {Manifest.permission.CAMERA};
            ActivityCompat.requestPermissions(this, permissions, REQ_PERM_GALLERY);
        }

    }//end startCamera

}//end class SecondActivity