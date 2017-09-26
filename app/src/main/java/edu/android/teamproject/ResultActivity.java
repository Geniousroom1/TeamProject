package edu.android.teamproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import uk.co.senab.photoview.PhotoViewAttacher;

// 핀치 줌(zoom)기능을 구현할 import


public class ResultActivity extends AppCompatActivity implements View.OnTouchListener{


    // 멤버 변수 선언
    public static int WRITE_PERMISTION = 9;
    private ImageView mainImage,changeImage;
    private ImageButton btn1, btn2, btn3;
    private boolean turn;
    private boolean change1;
    private ConstraintLayout rlBottomSheet;
    private PhotoViewAttacher attacher; // 핀치 줌 멤버변수
    private float oldXvalue;
    private float oldYvalue;
    private CoordinatorLayout mainScreen;
    private Bitmap mainimg;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);



        //private 변수로 설정한 키들을 activity_result.xml에서 알맞는 키를 찾아줌
        rlBottomSheet = (ConstraintLayout) findViewById(R.id.rl_bottom_sheet);
        mainImage = (ImageView) findViewById(R.id.mainImage);
        changeImage = (ImageView)findViewById(R.id.changeImage);
        btn1 = (ImageButton) findViewById(R.id.imageButton1);
        btn2 = (ImageButton) findViewById(R.id.imageButton2);
        btn3 = (ImageButton) findViewById(R.id.imageButton3);
        mainScreen = (CoordinatorLayout) findViewById(R.id.mainScreen);

        // 핀치 줌 멤버변수에 zoon in-out이 필요한 imageView를 넣어줌
        attacher = new PhotoViewAttacher(mainImage);
        // end search

        //버튼 2,3번과 이모티콘이미지(test용) 을 invisible (보이지않는) 상태로 설정
        btn2.setVisibility(View.INVISIBLE);
        btn3.setVisibility(View.INVISIBLE);
        changeImage.setVisibility(View.INVISIBLE);
        //end set.invisible

        // MainImage 에 secondActivity에서 저장한 사진을 set
        InputStream is = null;
        InputStreamReader reader = null;
        BufferedInputStream bis = null;
//        is = new FileInputStream();
//        reader (is)
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
            changeImage.setOnTouchListener(this);


        }
    }//end testOnClick


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        int width = ((ViewGroup) v.getParent()).getWidth() - v.getWidth();
        int height = ((ViewGroup) v.getParent()).getHeight() - v.getHeight();

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            oldXvalue = event.getX();
            oldYvalue = event.getY();
            //  Log.i("Tag1", "Action Down X" + event.getX() + "," + event.getY());
            Log.i("Tag1", "Action Down rX " + event.getRawX() + "," + event.getRawY());
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            v.setX(event.getRawX() - oldXvalue);
            v.setY(event.getRawY() - (oldYvalue + v.getHeight()));
            //  Log.i("Tag2", "Action Down " + me.getRawX() + "," + me.getRawY());
        } else if (event.getAction() == MotionEvent.ACTION_UP) {

            if (v.getX() > width && v.getY() > height) {
                v.setX(width);
                v.setY(height);
            } else if (v.getX() < 0 && v.getY() > height) {
                v.setX(0);
                v.setY(height);
            } else if (v.getX() > width && v.getY() < 0) {
                v.setX(width);
                v.setY(0);
            } else if (v.getX() < 0 && v.getY() < 0) {
                v.setX(0);
                v.setY(0);
            } else if (v.getX() < 0 || v.getX() > width) {
                if (v.getX() < 0) {
                    v.setX(0);
                    v.setY(event.getRawY() - oldYvalue - v.getHeight());
                } else {
                    v.setX(width);
                    v.setY(event.getRawY() - oldYvalue - v.getHeight());
                }
            } else if (v.getY() < 0 || v.getY() > height) {
                if (v.getY() < 0) {
                    v.setX(event.getRawX() - oldXvalue);
                    v.setY(0);
                } else {
                    v.setX(event.getRawX() - oldXvalue);
                    v.setY(height);
                }
            }
        }//end if-else
        return true;
    }//end onTouch

    public void screenShot(View view) {
        mainScreen.buildDrawingCache();
        Bitmap capView = mainScreen.getDrawingCache();


        FileOutputStream fos = null;
        String filename = "scr_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        int checkWrite = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(checkWrite == PackageManager.PERMISSION_GRANTED){
            try {
                fos = new FileOutputStream(Environment.getExternalStorageDirectory().toString()+"/TeamProject/"+filename+".jpeg");
                capView.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://"+"TeamProject/"+"*"+".jpeg")));
                Toast.makeText(getApplicationContext(), "Captured!", Toast.LENGTH_LONG).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else {
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(this, permissions, WRITE_PERMISTION);
        }

    }//end screenShot

    // 흑백필터를 비트맵에 적용하는 메소드
    public static Bitmap createContrast(Bitmap src) { //흑백
        int width = src.getWidth();
        int height = src.getHeight();
        // 비트맵을 만듭니다. setPixel이라는 함수를 통해서 각 픽셀에 해당 필터를 적용할 예정입니다.
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        int A, R, G, B; // A는 알파값(쉽게 말하면 투명도), 나머지는 RGB 아시지요?
        int pixel;
        double contrast = 50.0;

        // 픽셀 하나하나씩 돌려봅니다.
        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                // get pixel color
                pixel = src.getPixel(x, y);
                A = Color.alpha(pixel);
                // 아래 작업은 저도 퍼온 함수입니다. 255와 지금의 값과 contrast라는 명암비를 곱한 값을 가지고 0, 255(흑과백)
                // 을 결정하는 정도로 보면 됩니다.
                R = Color.red(pixel);
                R = (int)(((((R / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if(R < 0) { R = 0; }
                else if(R > 255) { R = 255; }

                G = Color.red(pixel);
                G = (int)(((((G / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if(G < 0) { G = 0; }
                else if(G > 255) { G = 255; }

                B = Color.red(pixel);
                B = (int)(((((B / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if(B < 0) { B = 0; }
                else if(B > 255) { B = 255; }

                // 새로운 픽셀을 적용합니다.
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }
        return bmOut;
    }


    // 버튼 클릭시 흑백 필터 적용!
    public void onBlackFilter(View view) {
        Bitmap bit = createContrast(SecondActivity.bit);
        mainImage.setImageBitmap(bit);
    }

}//end class ResultActivity












