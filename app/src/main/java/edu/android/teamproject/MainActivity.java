package edu.android.teamproject;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
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
    private ConstraintLayout constraintLayout;
    public static FrameLayout inflatedLayout;
    public ImageView cameraIMG;
    private ArrayList<StickerImageView> imgList;
    public static ArrayList<StickerTextView> textList;
    private FloatingActionButton floatingActionButton, floatingBtnEmoticon,
                            floatingBtnFilter, floatingBtnText, floatingBtnCloseLayout;
    private Animation mShowButton;
    private Animation mHideButton;
    private Animation mShowResButton;
    private Animation mHideResButton;


    private String mCurrentPhotoPath;
    public static final int REQ_CODE_CAMERA = 1000; // 카메라 키값
    public static final int PICK_FROM_GALLERY = 1; // 갤러리 키값
    public static final int PERMISTION_GALLERY = 2;
    public static final int PERMISTION_CAMERA = 3;
    public static final int PERMISTION_WRITE = 4;

    private Uri imageUri;
    public Bitmap bit = null; // 그림을 저장할 공간.

    private String filePath;
    private String folderName = "TeamProject";// 폴더명
    private String fileName = "Image"; // 파일명

    private ImageView galleryimgBtn, cameraimgBtn, backgroundimg, menubarOpener, menubarCloser, saveimgBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        constraintLayout = (ConstraintLayout) findViewById(R.id.mainxml);
        galleryimgBtn = (ImageView) findViewById(R.id.galleryimgBtn);
        cameraimgBtn = (ImageView) findViewById(R.id.cameraimgBtn);
        backgroundimg = (ImageView) findViewById(R.id.backgroundImg);
        menubarCloser = (ImageView) findViewById(R.id.meneBarCloser);
        menubarOpener = (ImageView) findViewById(R.id.menuBarOpener);
        saveimgBtn = (ImageView) findViewById(R.id.saveimgBtn);
        galleryimgBtn.setVisibility(View.INVISIBLE);
        cameraimgBtn.setVisibility(View.INVISIBLE);
        backgroundimg.setVisibility(View.INVISIBLE);
        saveimgBtn.setVisibility(View.INVISIBLE);
        menubarCloser.setVisibility(View.INVISIBLE);
        frameLayout = (FrameLayout) findViewById(R.id.container);
        frameLayout.setVisibility(View.GONE);
        mainLayout = (FrameLayout) findViewById(R.id.frameLayout);
        inflatedLayout = (FrameLayout) findViewById(R.id.dummydata);
        cameraIMG = (ImageView) findViewById(R.id.cameraView);
        imgList = new ArrayList<StickerImageView>();
        textList = new ArrayList<StickerTextView>();
        // 플로팅 버튼 찾음
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        floatingBtnEmoticon = (FloatingActionButton) findViewById(R.id.floatingBtnEmoticon);
        floatingBtnFilter = (FloatingActionButton) findViewById(R.id.floatingBtnFilter);
        floatingBtnText = (FloatingActionButton) findViewById(R.id.floatingBtnText);
        floatingBtnCloseLayout = (FloatingActionButton) findViewById(R.id.floatingBtnCloseLayout);
        floatingActionButton.setVisibility(View.VISIBLE);
        floatingBtnEmoticon.setVisibility(View.GONE);
        floatingBtnFilter.setVisibility(View.GONE);
        floatingBtnText.setVisibility(View.GONE);
        floatingBtnCloseLayout.setVisibility(View.GONE);
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
                        floatingBtnFilter.getVisibility() == View.GONE &&
                        floatingBtnText.getVisibility() == View.GONE) {
                    floatingBtnShow();
                    // 세 개의 플로팅 버튼이 VISIBLE 상태이면, 버튼을 GONE 상태로 변경
                } else if (floatingBtnEmoticon.getVisibility() == View.VISIBLE &&
                        floatingBtnFilter.getVisibility() == View.VISIBLE &&
                        floatingBtnText.getVisibility() == View.VISIBLE) {
                    floatingBtnGone();
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

        // 플로팅 버튼 GONE상태로 변경
        floatingBtnGone();
        floatingBtnCloseLayout.setVisibility(View.VISIBLE);
        floatingBtnCloseLayout.setClickable(true);


    }//end showEmoticon

    // 뒤로가기 버튼 클릭 시
    @Override
    public void onBackPressed() {
        if (frameLayout.getVisibility() == View.VISIBLE) {
            frameLayout.setVisibility(View.GONE);
            floatingBtnCloseLayout.setVisibility(View.GONE);
            floatingBtnCloseLayout.setClickable(false);
        } else if(floatingBtnEmoticon.getVisibility() == View.VISIBLE &&
                floatingBtnFilter.getVisibility() == View.VISIBLE &&
                floatingBtnText.getVisibility() == View.VISIBLE) {
            floatingBtnGone();
        } else if( galleryimgBtn.getVisibility() == View.VISIBLE &&
            cameraimgBtn.getVisibility() == View.VISIBLE &&
            backgroundimg.getVisibility() == View.VISIBLE &&
            menubarCloser.getVisibility() == View.VISIBLE &&
            saveimgBtn.getVisibility() == View.VISIBLE &&
            menubarOpener.getVisibility() == View.INVISIBLE) {
            menuClose(backgroundimg);
        } else {
            clearAnimations();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("종료");
            builder.setMessage("종료하시겠습니까?");
            builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            AlertDialog dlg = builder.create();
            dlg.show();

        }

    }

    // 필터 콜백리스너 메소드
    @Override
    public void onButtonClicked(int id) {
        FragmentManager fm = getSupportFragmentManager();
        FilterFragment fragment = (FilterFragment) fm.findFragmentById(R.id.container);
        fragment.getArguments();
        // 이미지 불러오지 않았을 때 필터버튼 클릭 시 예외를 잡아주기 위해서
        try {
            applyFilter(id);
        } catch(Exception e) {

        }

    }//end onButtonClicked

    private void applyFilter(int id) {
            ImageFilters imageFilters = new ImageFilters();
            switch(id) {
                case R.id.filter_black:
                    cameraIMG.setImageBitmap(imageFilters.applyBlackFilter(bit));
                    break;
                case R.id.filter_sunset:
                    cameraIMG.setImageBitmap(imageFilters.applyBoostEffect(bit, 1, 40));
                    break;
                case R.id.filter_sea:
                    cameraIMG.setImageBitmap(imageFilters.applyBoostEffect(bit, 3, 67));
                    break;
                case R.id.filter_bright:
                    cameraIMG.setImageBitmap(imageFilters.applyBrightnessEffect(bit, 80));
                    break;
                case R.id.filter_neon:
                    cameraIMG.setImageBitmap(imageFilters.applyColorFilterEffect(bit, 255, 0, 0));
                    break;
                case R.id.filter_neon2:
                    cameraIMG.setImageBitmap(imageFilters.applyColorFilterEffect(bit, 0, 255, 0));
                    break;
                case R.id.filter_neon3:
                    cameraIMG.setImageBitmap(imageFilters.applyColorFilterEffect(bit, 0, 0, 255));
                    break;
                case R.id.filter_paint:
                    cameraIMG.setImageBitmap(imageFilters.applyDecreaseColorDepthEffect(bit, 64));
                    break;
                case R.id.filter_paint2:
                    cameraIMG.setImageBitmap(imageFilters.applyDecreaseColorDepthEffect(bit, 32));
                    break;
                case R.id.filter_sketch:
                    cameraIMG.setImageBitmap(imageFilters.applyContrastEffect(bit, 70));
                    break;
                case R.id.filter_sketch2:
                    cameraIMG.setImageBitmap(imageFilters.applyEmbossEffect(bit));
                    break;
                case R.id.filter_sketch3:
                    cameraIMG.setImageBitmap(imageFilters.applyEngraveEffect(bit));
                    break;
                case R.id.filter_mosaic:
                    cameraIMG.setImageBitmap(imageFilters.applyFleaEffect(bit));
                    break;
                case R.id.filter_blur:
                    cameraIMG.setImageBitmap(imageFilters.applyGaussianBlurEffect(bit));
                    break;
                case R.id.filter_more_bright:
                    cameraIMG.setImageBitmap(imageFilters.applyGammaEffect(bit, 1.8, 1.8, 1.8));
                    break;
                case R.id.filter_black2:
                    cameraIMG.setImageBitmap(imageFilters.applyGreyscaleEffect(bit));
                    break;
                case R.id.filter_border:
                    cameraIMG.setImageBitmap(imageFilters.applyMeanRemovalEffect(bit));
                    break;
                case R.id.filter_romo:
                    cameraIMG.setImageBitmap(imageFilters.applyRoundCornerEffect(bit, 45));
                    break;
                case R.id.filter_white:
                    cameraIMG.setImageBitmap(imageFilters.applySaturationFilter(bit, 1));
                    break;
                case R.id.filter_black3:
                    cameraIMG.setImageBitmap(imageFilters.applySepiaToningEffect(bit, 10, 1.5, 0.6, 0.12));
                    break;
                case R.id.filter_black4:
                    cameraIMG.setImageBitmap(imageFilters.applySepiaToningEffect(bit, 10, 0.88, 2.45, 1.43));
                    break;
                case R.id.filter_black5:
                    cameraIMG.setImageBitmap(imageFilters.applySepiaToningEffect(bit, 10, 1.2, 0.87, 2.1));
                    break;
                case R.id.filter_natural:
                    cameraIMG.setImageBitmap(imageFilters.applySmoothEffect(bit, 100));
                    break;
                case R.id.filter_blue:
                    cameraIMG.setImageBitmap(imageFilters.applyShadingFilter(bit, Color.CYAN));
                    break;
                case R.id.filter_yellow:
                    cameraIMG.setImageBitmap(imageFilters.applyShadingFilter(bit, Color.YELLOW));
                    break;
                case R.id.filter_green:
                    cameraIMG.setImageBitmap(imageFilters.applyShadingFilter(bit, Color.GREEN));
                    break;
                case R.id.filter_rainbow:
                    cameraIMG.setImageBitmap(imageFilters.applyTintEffect(bit, 100));
                    break;

            } // end switch()
        } // end applyFilter()



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

        // 플로팅 버튼 GONE상태로 변경
        floatingBtnGone();
        floatingBtnCloseLayout.setVisibility(View.VISIBLE);
        floatingBtnCloseLayout.setClickable(true);
    }//end showFilter


    public void FragmentRepalce(Fragment f) {
        fm = getSupportFragmentManager();
        fragment = fm.findFragmentById(R.id.container);
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.container, f).commit();
    }//end FragmentRepalce


    public void screenCapture(View view) {
        clearAnimations();
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
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
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
        TextsActivity dialog = new TextsActivity(this);
        dialog.show();
        floatingBtnGone();
    }//end textBtnClick


    // 이미지뷰 공간에 버튼 클릭 시 테두리 사라지는 기능
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

    public void clearAnimations() {
        floatingBtnEmoticon.clearAnimation();
        floatingBtnFilter.clearAnimation();
        floatingBtnText.clearAnimation();
        floatingActionButton.clearAnimation();

    }

    // 플로팅 버튼을 사라지게 하는 메소드
    public void floatingBtnGone() {
        // 나머지 플로팅 버튼 GONE으로 변경
        floatingBtnEmoticon.setVisibility(View.GONE);
        floatingBtnEmoticon.setClickable(false);
        floatingBtnFilter.setVisibility(View.GONE);
        floatingBtnFilter.setClickable(false);
        floatingBtnText.setVisibility(View.GONE);
        floatingBtnText.setClickable(false);

        floatingBtnEmoticon.startAnimation(mHideResButton);
        floatingBtnFilter.startAnimation(mHideResButton);
        floatingBtnText.startAnimation(mHideResButton);
        floatingActionButton.startAnimation(mHideButton);


    }

    // 플로팅 버튼을 보여주는 메소드
    public void floatingBtnShow() {
        floatingBtnEmoticon.setVisibility(View.VISIBLE);
        floatingBtnEmoticon.setClickable(true);
        floatingBtnFilter.setVisibility(View.VISIBLE);
        floatingBtnFilter.setClickable(true);
        floatingBtnText.setVisibility(View.VISIBLE);
        floatingBtnText.setClickable(true);


        floatingBtnEmoticon.startAnimation(mShowResButton);
        floatingBtnFilter.startAnimation(mShowResButton);
        floatingBtnText.startAnimation(mShowResButton);
        floatingActionButton.startAnimation(mShowButton);
    }

    public void closeLayout(View view) {
        if (frameLayout != null) {
            frameLayout.setVisibility(View.GONE);
        }
        floatingBtnCloseLayout.setVisibility(View.GONE);
    }

//

    // startActivityForResult 할때의 기능 override
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode== REQ_CODE_CAMERA && resultCode == RESULT_OK) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            try {
                //         bit = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
                bit = BitmapFactory.decodeFile(mCurrentPhotoPath);
                Log.i("edu.android", "mCurrentPhotoPath :: " + mCurrentPhotoPath);
                // 이미지를 상황에 맞게 회전시킴
                ExifInterface exif = new ExifInterface(mCurrentPhotoPath);
                int exifOrientation = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                int exifDegree = exifOrientationToDegrees(exifOrientation);
                Log.i("edu.android", "exifDegree" + exifDegree);
                bit = rotate(bit, exifDegree);
                cameraIMG.setImageBitmap(bit);

                constraintLayout.setBackgroundColor(Color.BLACK);

                galleryimgBtn.setVisibility(View.INVISIBLE);
                cameraimgBtn.setVisibility(View.INVISIBLE);
                backgroundimg.setVisibility(View.INVISIBLE);
                menubarCloser.setVisibility(View.INVISIBLE);
                saveimgBtn.setVisibility(View.INVISIBLE);
                menubarOpener.setVisibility(View.VISIBLE);
//                Intent nextI = new Intent(this, MainActivity.class);
//                startActivity(nextI);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "취소", Toast.LENGTH_SHORT).show();
            }


        }else{

        }//end Camera

        if (requestCode== PICK_FROM_GALLERY && resultCode == RESULT_OK ) {
            Uri uri = data.getData();
            if(uri!=null) {
                try {
                    bit = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                    cameraIMG.setImageBitmap(bit);

                    constraintLayout.setBackgroundColor(Color.BLACK);

                    galleryimgBtn.setVisibility(View.INVISIBLE);
                    cameraimgBtn.setVisibility(View.INVISIBLE);
                    backgroundimg.setVisibility(View.INVISIBLE);
                    menubarCloser.setVisibility(View.INVISIBLE);
                    saveimgBtn.setVisibility(View.INVISIBLE);
                    menubarOpener.setVisibility(View.VISIBLE);
//                    Intent nextI = new Intent(this, MainActivity.class);
//                    startActivity(nextI);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "에러 발생", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "취소", Toast.LENGTH_SHORT).show();
            }
        }else{

        }//end Gallery

    }//end onActivityResult


    public static int exifOrientationToDegrees(int exifOrientation)
    {
        if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_90)
        {
            return 90;
        }
        else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_180)
        {
            return 180;
        }
        else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_270)
        {
            return 270;
        }
        return 0;
    }

    public static Bitmap rotate(Bitmap bitmap, int degrees)
    {
        if(degrees != 0 && bitmap != null)
        {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) bitmap.getWidth() / 2,
                    (float) bitmap.getHeight() / 2);

            try
            {
                Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0,
                        bitmap.getWidth(), bitmap.getHeight(), m, true);
                if(bitmap != converted)
                {
                    bitmap.recycle();
                    bitmap = converted;
                }
            }
            catch(OutOfMemoryError ex)
            {
                // 메모리가 부족하여 회전을 시키지 못할 경우 그냥 원본을 반환합니다.
            }
        }
        return bitmap;
    }



    // 카메라로 찍어서 값을 넘겨줌
    public void startCamera(View view) {
        clearAnimations();
        int check = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int check2 = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if(check == PackageManager.PERMISSION_GRANTED&& check2== PackageManager.PERMISSION_GRANTED){
            try {
                File photoFile = null;
                photoFile = createImageFile();
                imageUri = FileProvider.getUriForFile(this,"edu.android.teamproject",photoFile);
                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, REQ_CODE_CAMERA);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }else {
            String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(this, permissions, PERMISTION_CAMERA);
            ActivityCompat.requestPermissions(this, permissions, PERMISTION_WRITE);
        }

    }//end startCamera

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "SCR_" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }//end createImageFile

    public void startGallery(View view) {
        clearAnimations();
        int check = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (check == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT); // 갤러리 인텐트 생성
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_FROM_GALLERY); //갤러리 인텐트 실행
        } else {
            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(this, permissions, PERMISTION_GALLERY);
        }
    }

    public void menuClose(View view) {
        galleryimgBtn.setVisibility(View.INVISIBLE);
        cameraimgBtn.setVisibility(View.INVISIBLE);
        backgroundimg.setVisibility(View.INVISIBLE);
        menubarCloser.setVisibility(View.INVISIBLE);
        saveimgBtn.setVisibility(View.INVISIBLE);
        menubarOpener.setVisibility(View.VISIBLE);
    }

    public void menuOpen(View view) {
        galleryimgBtn.setVisibility(View.VISIBLE);
        cameraimgBtn.setVisibility(View.VISIBLE);
        backgroundimg.setVisibility(View.VISIBLE);
        menubarCloser.setVisibility(View.VISIBLE);
        saveimgBtn.setVisibility(View.VISIBLE);
        menubarOpener.setVisibility(View.INVISIBLE);
    }
}
