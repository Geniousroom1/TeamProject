package edu.android.teamproject;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class EmoticonFragment extends Fragment {
    // 인터페이스 - 콜백 리스너
    interface EmoticonListener {
        void onTabItemClicked(int tab, int pos);
    }

    // 인터페이스 멤버변수
        private EmoticonListener listener;


    // 멤버변수 선언
    private static final String ARGS_POSITION = "args_position";
    private int position;
    private GridView gridView;


    // 이모티콘 종류별 배열로 묶어서 상수 정의
    public static final int[][] IMAGE_EMOTICONS = {
            {// 0번째 탭에서 필요한 이미지
                    R.drawable.a1, R.drawable.a2, R.drawable.a3,R.drawable.a4, R.drawable.a5,
                    R.drawable.a6, R.drawable.a7, R.drawable.a8,R.drawable.a9, R.drawable.a10,
                    R.drawable.a11, R.drawable.a12,R.drawable.a13, R.drawable.a14, R.drawable.a15,
                    R.drawable.a16, R.drawable.a17, R.drawable.a18, R.drawable.a19, R.drawable.a20,
                    R.drawable.a21, R.drawable.a22, R.drawable.a23, R.drawable.a24, R.drawable.a25,
                    R.drawable.a26, R.drawable.a27, R.drawable.a28, R.drawable.a29, R.drawable.a30,
                    R.drawable.a31, R.drawable.a32, R.drawable.a33, R.drawable.a34, R.drawable.a35,
                    R.drawable.a36, R.drawable.a37, R.drawable.a38, R.drawable.a39, R.drawable.a40,
                    R.drawable.a41, R.drawable.a42, R.drawable.a43, R.drawable.a44, R.drawable.a45,
                    R.drawable.a46, R.drawable.a47, R.drawable.a48, R.drawable.a49, R.drawable.a50,
                    R.drawable.a51, R.drawable.a52, R.drawable.a53, R.drawable.a54, R.drawable.a55,
                    R.drawable.a56,
            },
            {// 1번째 탭에서 필요한 이미지
                    R.drawable.a1, R.drawable.c1111, R.drawable.p3,
                    R.drawable.p4, R.drawable.p5, R.drawable.p6,
                    R.drawable.p7, R.drawable.p8, R.drawable.p9
            },
            {},
            {},
            {},
            {},
            {},
            {},
            {}
    };

    public EmoticonFragment() {
        // Required empty public constructor
    }

    /*// 팩토리 메소드()
    public static EmoticonFragment newInstance(int position) {
        EmoticonFragment fragment = new EmoticonFragment();

        Bundle args = new Bundle();
        args.putInt(ARGS_POSITION, position);
        fragment.setArguments(args);

        return fragment;
    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // MainActivity로 넘겨줌
        if (context instanceof EmoticonListener) {
            listener = (EmoticonListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            position = args.getInt(ARGS_POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        // 레이아웃을 만들어줌
        return inflater.inflate(R.layout.fragment_emoticon, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();

        // 탭 레이아웃에 id부여하고 찾기
        TabLayout tabs = view.findViewById(R.id.tabs);
        final GridView gridView = view.findViewById(R.id.grid_emoticons);
        // 탭을 클릭했을 때 이벤트핸들러
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // TODO: EmoticonFragment를 끼워넣음
                int position = tab.getPosition();
                gridView.setAdapter(new EmoticonAdapter(getContext(), position));

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        //  gridView.setAdapter(new EmoticonAdapter(getContext(), 0));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      //   @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View convertView,
                                    int position, // GridView의 아이템 번호
                                    long id) {
//              Toast.makeText(getContext(), "얍" + position  , Toast.LENGTH_SHORT).show();
                listener.onTabItemClicked(EmoticonFragment.this.position, position);


            }

        });
        /*      ClipData.Item item = new ClipData.Item((CharSequence) convertView.getResources());
                ClipData clipData = new ClipData(convertView.getResources().getString(position), null, item);
                View.DragShadowBuilder dsb = new View.DragShadowBuilder(convertView);
                convertView.startDragAndDrop(clipData, dsb, this, position);*/

    } // end onStart


    // Adapter 내부클래스
    // -> getView를 이용해서 GridView에 이모티콘(아이템)을 하나씩 그려주기
    class EmoticonAdapter extends BaseAdapter {

        // 이모티콘이 보여질 멤버변수
        private Context context;
        private int tabPosition;

        public EmoticonAdapter(Context context, int tabPosition) {
            this.context = context;
            this.tabPosition = tabPosition;
            position = tabPosition;
        }

        @Override
        public int getCount() {
            int length = IMAGE_EMOTICONS[tabPosition].length;
            Log.i("edu.android", "tabPos: " + tabPosition + ", length: " + length);
            return length;
        }

        @Override
        public Object getItem(int i) {

            return null;
        }

        @Override
        public long getItemId(int i) {

            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView iv = null;
            if (convertView == null) {
                // convertView에 암것도 없으면 넣어주고,
                iv = new ImageView(context);
                iv.setLayoutParams(new ViewGroup.LayoutParams(90, 90));
                iv.setPadding(5, 10, 5, 10);

            } else {
                // 내용이 있으면 화면에 그대로 보여주기
                iv = (ImageView) convertView;
            }

            // 그리드뷰에 그려질 이미지뷰에 이모티콘 넣기
            iv.setImageResource(IMAGE_EMOTICONS[tabPosition][position]);


            return iv;


        }


    }


}
