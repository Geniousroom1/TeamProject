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
                    R.drawable.android1, R.drawable.android2, R.drawable.android3,
                    R.drawable.android4, R.drawable.android5, R.drawable.android6,
                    R.drawable.android7, R.drawable.android8,
                    R.drawable.android1, R.drawable.android2, R.drawable.android3,
                    R.drawable.android4, R.drawable.android5, R.drawable.android6,
                    R.drawable.android7, R.drawable.android8,
                    R.drawable.android1, R.drawable.android2, R.drawable.android3,
                    R.drawable.android4, R.drawable.android5, R.drawable.android6,
                    R.drawable.android7, R.drawable.android8,
                    R.drawable.android1, R.drawable.android2, R.drawable.android3,
                    R.drawable.android4, R.drawable.android5, R.drawable.android6,
                    R.drawable.android7, R.drawable.android8,
                    R.drawable.android1, R.drawable.android2, R.drawable.android3,
                    R.drawable.android4, R.drawable.android5, R.drawable.android6,
                    R.drawable.android7, R.drawable.android8,
                    R.drawable.android1, R.drawable.android2, R.drawable.android3,
                    R.drawable.android4, R.drawable.android5, R.drawable.android6,
                    R.drawable.android7, R.drawable.android8,
                    R.drawable.android1, R.drawable.android2, R.drawable.android3,
                    R.drawable.android4, R.drawable.android5, R.drawable.android6,
                    R.drawable.android7, R.drawable.android8,
                    R.drawable.android1, R.drawable.android2, R.drawable.android3,
                    R.drawable.android4, R.drawable.android5, R.drawable.android6,
                    R.drawable.android7, R.drawable.android8,
                    R.drawable.android1, R.drawable.android2, R.drawable.android3,
                    R.drawable.android4, R.drawable.android5, R.drawable.android6,
                    R.drawable.android7, R.drawable.android8
            },
            {// 1번째 탭에서 필요한 이미지
                    R.drawable.p1, R.drawable.p2, R.drawable.p3,
                    R.drawable.p4, R.drawable.p5, R.drawable.p6,
                    R.drawable.p7, R.drawable.p8, R.drawable.p9
            },
            {}, {}, {}, {}, {}, {}, {}
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
                iv.setLayoutParams(new ViewGroup.LayoutParams(110, 110));
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
