package edu.android.teamproject;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity
        implements EmoticonFragment.EmoticonListener, FilterFragment.FilterListener,
        View.OnTouchListener { // 인터페이스 구현

    private FrameLayout frameLayout;
    private FragmentManager fm;
    private Fragment fragment;
    private FrameLayout inflatedLayout;
    private LayoutInflater inflater;
    private static final String TAG = "Touch";
    private static final float MIN_ZOOM = 1f,MAX_ZOOM = 1f;

    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;
    private PointF start = new PointF();
    private PointF mid = new PointF();
    private float oldDist = 1f;
    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();
    private ImageView cameraIMG;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frameLayout = (FrameLayout) findViewById(R.id.container);
        frameLayout.setVisibility(View.GONE);

        inflatedLayout = (FrameLayout) findViewById(R.id.dummydata);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        cameraIMG = (ImageView) findViewById(R.id.cameraView);
        cameraIMG.setImageBitmap(SecondActivity.bit);

  /*      // EmoticonFragment를 FrameLayout에 끼워넣기
        fm = getSupportFragmentManager();
        // EmoticonFragment를 찾음
        fragment = fm.findFragmentById(R.id.container);
        if (fragment == null) {
            // Fragment에 attach시킬 프래그먼트 클래스 인스턴스 생성
            fragment = new EmoticonFragment();
        }


        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.container, fragment);
        transaction.commit();*/



    } // end onCreate


    // 이모티콘 콜백리스너 메소드
    @Override
    public void onTabItemClicked(int tab, int posotion) {
        int imgnum = EmoticonFragment.IMAGE_EMOTICONS[tab][posotion];
        View view  = inflater.inflate(R.layout.image_dummy, null);
        ImageView data = view.findViewById(R.id.data);
        data.setImageResource(imgnum);
        data.setOnTouchListener(this);
        inflatedLayout.addView(view);
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
    }


    // 필터 콜백리스너 메소드
    @Override
    public void onButtonClicked(int id) {
        FragmentManager fm = getSupportFragmentManager();
        FilterFragment fragment = (FilterFragment) fm.findFragmentById(R.id.container);
        fragment.getArguments();
    }


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
    }


    public void FragmentRepalce(Fragment f) {
        fm = getSupportFragmentManager();
        fragment = fm.findFragmentById(R.id.container);

        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.container, f).commit();

    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        ImageView view = (ImageView) v;
        view.setScaleType(ImageView.ScaleType.MATRIX);
        float scale;
//        start = new PointF();
//        mid = new PointF();
//        oldDist = 1f;
//        matrix = new Matrix();
//        savedMatrix = new Matrix();
        dumpEvent(event);
        // Handle touch events here...

        switch (event.getAction() & MotionEvent.ACTION_MASK)
        {
            case MotionEvent.ACTION_DOWN:   // first finger down only
                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                Log.d(TAG, "mode=DRAG"); // write to LogCat
                mode = DRAG;
                break;

            case MotionEvent.ACTION_UP: // first finger lifted

            case MotionEvent.ACTION_POINTER_UP: // second finger lifted
                mode = NONE;
                Log.d(TAG, "mode=NONE");
                break;
            case MotionEvent.ACTION_POINTER_DOWN: // first and second finger down
                oldDist = spacing(event);
                Log.d(TAG, "oldDist=" + oldDist);
                if (oldDist > 5f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                    Log.d(TAG, "mode=ZOOM");
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG)
                {
                    matrix.set(savedMatrix);
                    matrix.postTranslate(event.getX() - start.x, event.getY() - start.y); // create the transformation in the matrix  of points
                }
                else if (mode == ZOOM)
                {
                    // pinch zooming
                    float newDist = spacing(event);
                    Log.d(TAG, "newDist=" + newDist);
                    if (newDist > 5f)
                    {
                        matrix.set(savedMatrix);
                        scale = newDist / oldDist; // setting the scaling of the
                        // matrix...if scale > 1 means
                        // zoom in...if scale < 1 means
                        // zoom out
                        matrix.postScale(scale, scale, mid.x, mid.y);
                    }
                }
                break;
        }

        view.setImageMatrix(matrix); // display the transformation on screen

        return true; // indicate event was handled
    }

    /*
     * --------------------------------------------------------------------------
     * Method: spacing Parameters: MotionEvent Returns: float Description:
     * checks the spacing between the two fingers on touch
     * ----------------------------------------------------
     */

    private float spacing(MotionEvent event)
    {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float)Math.sqrt(x * x + y * y);
    }

    /*
     * --------------------------------------------------------------------------
     * Method: midPoint Parameters: PointF object, MotionEvent Returns: void
     * Description: calculates the midpoint between the two fingers
     * ------------------------------------------------------------
     */

    private void midPoint(PointF point, MotionEvent event)
    {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    /** Show an event in the LogCat view, for debugging */
    private void dumpEvent(MotionEvent event)
    {
        String names[] = { "DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE","POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?" };
        StringBuilder sb = new StringBuilder();
        int action = event.getAction();
        int actionCode = action & MotionEvent.ACTION_MASK;
        sb.append("event ACTION_").append(names[actionCode]);

        if (actionCode == MotionEvent.ACTION_POINTER_DOWN || actionCode == MotionEvent.ACTION_POINTER_UP)
        {
            sb.append("(pid ").append(action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
            sb.append(")");
        }

        sb.append("[");
        for (int i = 0; i < event.getPointerCount(); i++)
        {
            sb.append("#").append(i);
            sb.append("(pid ").append(event.getPointerId(i));
            sb.append(")=").append((int) event.getX(i));
            sb.append(",").append((int) event.getY(i));
            if (i + 1 < event.getPointerCount())
                sb.append(";");
        }

        sb.append("]");
        Log.d("Touch Events ---------", sb.toString());
    }




}
