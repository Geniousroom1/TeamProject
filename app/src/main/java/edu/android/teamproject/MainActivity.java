package edu.android.teamproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements EmoticonFragment.EmoticonListener, FilterFragment.FilterListener
{   private static final int WRITE_PERMISTION = 19; // 인터페이스 구현
    private FrameLayout frameLayout;
    private FrameLayout mainLayout;
    private FragmentManager fm;
    private Fragment fragment;
    public static FrameLayout inflatedLayout;
    private ImageView cameraIMG;
    private ArrayList<StickerImageView> imgList;
    public static ArrayList<StickerTextView> textList;
    private FloatingActionButton floatingActionButton, floatingBtnEmoticon, floatingBtnFilter, floatingBtnCapture;
    private Animation mShowButton;
    private Animation mHideButton;
    private Animation mShowResButton;
    private Animation mHideResButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frameLayout = (FrameLayout) findViewById(R.id.container);
        frameLayout.setVisibility(View.GONE);
        mainLayout = (FrameLayout) findViewById(R.id.frameLayout);
        inflatedLayout = (FrameLayout) findViewById(R.id.dummydata);
        cameraIMG = (ImageView) findViewById(R.id.cameraView);
        cameraIMG.setImageBitmap(SecondActivity.bit);
        imgList = new ArrayList<StickerImageView>();
        textList = new ArrayList<StickerTextView>();

        // 플로팅 버튼 찾음
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        floatingBtnEmoticon = (FloatingActionButton) findViewById(R.id.floatingBtnEmoticon);
        floatingBtnFilter = (FloatingActionButton) findViewById(R.id.floatingBtnFilter);
        floatingBtnCapture = (FloatingActionButton) findViewById(R.id.floatingBtnCapture);
        floatingActionButton.setVisibility(View.VISIBLE);
        floatingBtnEmoticon.setVisibility(View.GONE);
        floatingBtnFilter.setVisibility(View.GONE);
        floatingBtnCapture.setVisibility(View.GONE);
        // 버튼에 넣을 애니메이션 찾음
        mShowButton = AnimationUtils.loadAnimation(this, R.anim.show_button);
        mHideButton = AnimationUtils.loadAnimation(this, R.anim.hide_button);
        mShowResButton = AnimationUtils.loadAnimation(this, R.anim.show_res_button);
        mHideResButton = AnimationUtils.loadAnimation(this, R.anim.hide_res_button);

        // +플로팅 버튼 눌렀을 때
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 세 개의 플로팅 버튼이 GONE 상태이면, 버튼을 보여줌
                if (floatingBtnEmoticon.getVisibility() == View.GONE &&
                        floatingBtnFilter.getVisibility() == View.GONE) {

                    floatingBtnEmoticon.setVisibility(View.VISIBLE);
                    floatingBtnEmoticon.setClickable(true);
                    floatingBtnFilter.setVisibility(View.VISIBLE);
                    floatingBtnFilter.setClickable(true);
                    floatingBtnCapture.setVisibility(View.VISIBLE);
                    floatingBtnCapture.setClickable(true);

                    floatingBtnEmoticon.startAnimation(mShowResButton);
                    floatingBtnFilter.startAnimation(mShowResButton);
                    floatingBtnCapture.startAnimation(mShowResButton);
                    floatingActionButton.startAnimation(mShowButton);

                    // 세 개의 플로팅 버튼이 VISIBLE 상태이면, 버튼을 GONE 상태로 변경
                } else if (floatingBtnEmoticon.getVisibility() == View.VISIBLE &&
                        floatingBtnFilter.getVisibility() == View.VISIBLE) {

                    floatingBtnEmoticon.setVisibility(View.GONE);
                    floatingBtnFilter.setVisibility(View.GONE);
                    floatingBtnCapture.setVisibility(View.GONE);

                    floatingBtnEmoticon.startAnimation(mHideResButton);
                    floatingBtnFilter.startAnimation(mHideResButton);
                    floatingBtnCapture.startAnimation(mHideResButton);
                    floatingActionButton.startAnimation(mHideButton);
                }


            }
        });
    } // end onCreate


    // 이모티콘 콜백리스너 메소드
    @Override
    public void onTabItemClicked(int tab, int posotion) {

        int imgnum = EmoticonFragment.IMAGE_EMOTICONS[tab][posotion];
        StickerImageView iv_sticker;
        imgList.add(iv_sticker = new StickerImageView(MainActivity.this));
        if(1< imgList.size()){
            for (int i = 0; i < imgList.size() ; i++) {
                imgList.get(i).setControlItemsHidden(true);
            }
            imgList.get(imgList.size()-1).setControlItemsHidden(false);
        }
        iv_sticker.setImageDrawable(getResources().getDrawable(imgnum));
        inflatedLayout.addView(iv_sticker);

    }


    // 이벤트핸들러
    public void showEmoticon(View view) {
        // EmoticonFragment를 FrameLayout에 끼워넣기
        fm = getSupportFragmentManager();
        // EmoticonFragment를 찾음
        fragment = fm.findFragmentById(R.id.container);
        if (fragment == null) {
            // Fragment에 attach시킬 프래그먼트 클래스 인스턴스 생성
            fragment = new EmoticonFragment();
        } else {
            fragment = new EmoticonFragment();
            FragmentRepalce(fragment);
        }
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.container, fragment);
        transaction.commit();

        // 끼워 넣을 프래그먼트 레이아웃을 VISIBLE함
        if (frameLayout.getVisibility() == View.GONE) {
            frameLayout.setVisibility(View.VISIBLE);
        }

        // 나머지 플로팅 버튼 GONE
        floatingBtnEmoticon.setVisibility(View.GONE);
        floatingBtnEmoticon.setClickable(false);
        floatingBtnFilter.setVisibility(View.GONE);
        floatingBtnFilter.setClickable(false);
        floatingBtnCapture.setVisibility(View.GONE);
        floatingBtnCapture.setClickable(false);

        floatingBtnEmoticon.startAnimation(mHideResButton);
        floatingBtnFilter.startAnimation(mHideResButton);
        floatingBtnCapture.startAnimation(mHideResButton);
        floatingActionButton.startAnimation(mHideButton);
    }//end showEmoticon


    // 필터 콜백리스너 메소드
    @Override
    public void onButtonClicked(int id) {
        FragmentManager fm = getSupportFragmentManager();
        FilterFragment fragment = (FilterFragment) fm.findFragmentById(R.id.container);
        fragment.getArguments();
    }//end onButtonClickedf


    // 이벤트핸들러
    public void showFilter(View view) {
        // EmoticonFragment를 FrameLayout에 끼워넣기
        fm = getSupportFragmentManager();
        // EmoticonFragment를 찾음
        fragment = fm.findFragmentById(R.id.container);
        if (fragment == null) {
            // Fragment에 attach시킬 프래그먼트 클래스 인스턴스 생성
            fragment = new FilterFragment();
        } else {
            fragment = new FilterFragment();
            FragmentRepalce(fragment);
        }
      //  frameLayout.removeAllViews(); // 프레임 레이아웃의 뷰를 모두 지움
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.container, fragment);
        transaction.commit();
        frameLayout.setVisibility(View.VISIBLE);

        // 끼워 넣을 프래그먼트 레이아웃을 GONE함
        if (frameLayout.getVisibility() == View.GONE) {
            frameLayout.setVisibility(View.VISIBLE);
        }

        // 나머지 플로팅 버튼 GONE으로 변경
        floatingBtnEmoticon.setVisibility(View.GONE);
        floatingBtnEmoticon.setClickable(false);
        floatingBtnFilter.setVisibility(View.GONE);
        floatingBtnFilter.setClickable(false);
        floatingBtnCapture.setVisibility(View.GONE);
        floatingBtnCapture.setClickable(false);

        floatingBtnEmoticon.startAnimation(mHideResButton);
        floatingBtnFilter.startAnimation(mHideResButton);
        floatingBtnCapture.startAnimation(mHideResButton);
        floatingActionButton.startAnimation(mHideButton);
    }//end showFilter


    public void FragmentRepalce(Fragment f) {
        fm = getSupportFragmentManager();
        fragment = fm.findFragmentById(R.id.container);
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.container, f).commit();
    }//end FragmentRepalce


    public void screenCapture(View view) {
        mainLayout.buildDrawingCache();
        Bitmap capView = mainLayout.getDrawingCache();
        FileOutputStream fos = null;
        String filename = "scr_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        int checkWrite = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(checkWrite == PackageManager.PERMISSION_GRANTED){
            try {
                File file = new File(Environment.getExternalStorageDirectory().toString()+"/TeamProject");
                file.mkdir();
                fos = new FileOutputStream(Environment.getExternalStorageDirectory().toString()+"/TeamProject/"+filename+".jpeg");
                capView.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                Toast.makeText(getApplicationContext(), "Captured!", Toast.LENGTH_LONG).show();
                DMediaScanner scanner = new DMediaScanner(this);
                scanner.startScan(Environment.getExternalStorageDirectory().toString()+"/TeamProject/"+filename+".jpeg");
                finish();
            } catch (Exception e) {
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
    }//end screenCapture

    public void textBtnClick(View view) {
        Intent textIntent = new Intent(this, TextsActivity.class);
        startActivity(textIntent);
    }//end textBtnClick

    public void hidden(View view) {
            for (int i = 0; i < imgList.size(); i++) {
                StickerImageView siv = imgList.get(i);
                siv.setControlItemsHidden(true);
            }

            for (int i = 0; i < textList.size(); i++) {
                StickerTextView stv = textList.get(i);
                stv.setControlItemsHidden(true);
            }

    }

    public void nobhidden(View view) {
            for (int i = 0; i < imgList.size(); i++) {
                StickerImageView siv = imgList.get(i);
                siv.setControlItemsHidden(false);
            }

            for (int i = 0; i < textList.size(); i++) {
                StickerTextView stv = textList.get(i);
                stv.setControlItemsHidden(false);
            }
    }



    //TODO: 1. 필터 적용 기능 추가
    //TODO: 2. 온터치시 안보이는게아닌 클릭시 안보이게 하는 기능으로 변환
    //TODO: 3. 카메라 가로/세로 를 구분하는 기능 필요.
    //TODO: 4. 텍스트입력시 Text Font 및 color 를 변경할수 있게 하는 기능 추가
    //TODO: 5. 현재 activity_main.xml 에 있는 '텍스트 입력' 버튼을 플로팅 버튼형식으로 변경
    //TODO: 6. 기능 구현후 UI 보완


}
