package edu.android.teamproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
    private ArrayList<StickerImageView> arrayList;
    private int b = 0;

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
        arrayList = new ArrayList<StickerImageView>();
    } // end onCreate


    // 이모티콘 콜백리스너 메소드
    @Override
    public void onTabItemClicked(int tab, int posotion) {

        int imgnum = EmoticonFragment.IMAGE_EMOTICONS[tab][posotion];
        StickerImageView iv_sticker;
        arrayList.add(iv_sticker = new StickerImageView(MainActivity.this));
        if(1<arrayList.size()){
            for (int i = 0 ; i < arrayList.size() ; i++) {
                arrayList.get(i).setControlItemsHidden(true);
            }
            arrayList.get(arrayList.size()-1).setControlItemsHidden(false);
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

       frameLayout.setVisibility(View.VISIBLE);
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
            for (int i = 0; i < arrayList.size(); i++) {
                StickerImageView siv = arrayList.get(i);
                siv.setControlItemsHidden(true);
            }
    }

    public void nobhidden(View view) {
        for (int i = 0; i < arrayList.size(); i++) {
            StickerImageView siv = arrayList.get(i);
            siv.setControlItemsHidden(false);
        }
    }

}
