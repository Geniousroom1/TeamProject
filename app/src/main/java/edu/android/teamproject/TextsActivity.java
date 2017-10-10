package edu.android.teamproject;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;

public class TextsActivity extends AppCompatActivity {
    //TODO : 해당 액티비티를 새로운창에 띄우는게아닌 그림을 보며 텍스트를 수정할수있도록 생성
    public static String textActivity_Text;
    public static boolean textCancel;
    private EditText text;
    public static StickerTextView tv_sticker;
    public static Typeface font1;
    public static int color1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_texts);
        text = (EditText) findViewById(R.id.editText);
        color1 = Color.WHITE;
        font1 = Typeface.createFromAsset(getAssets(),"손글씨펜.ttf");
        text.setTypeface(font1);
        text.setTextColor(color1);
    }

    public void btn_cancel(View view) {
        textCancel = true;
        Toast.makeText(this, "취소", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void btn_enter(View view) {
        textCancel = false;
        textActivity_Text = text.getText().toString();
        MainActivity.textList.add(tv_sticker = new StickerTextView(TextsActivity.this));
        tv_sticker.setText(TextsActivity.textActivity_Text);
        MainActivity.inflatedLayout.addView(tv_sticker);
        finish();
    }

    public void font(View view) {

        final CharSequence font[] = new CharSequence[]{"고딕","손글씨 펜","CulDeSac","블럭","첫소리 강조"};

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setItems(font, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i)

                {

                    case 0:

                        font1 = Typeface.createFromAsset(getAssets(),"나눔고딕.ttf");
                        text.setTypeface(font1);


                        break;

                    case 1:

                        // 로그아웃
                        font1 = Typeface.createFromAsset(getAssets(),"손글씨펜.ttf");
                        text.setTypeface(font1);

                        break;
                    case 2:

                        font1 = Typeface.createFromAsset(getAssets(),"CulDeSac.ttf");
                        text.setTypeface(font1);
                        break;
                    case 3:

                        font1 = Typeface.createFromAsset(getAssets(),"Blox2.ttf");
                        text.setTypeface(font1);

                        break;
                    case 4:

                        font1 = Typeface.createFromAsset(getAssets(),"첫소리.ttf");
                        text.setTypeface(font1);
                        break;
                }
                dialogInterface.dismiss();
            }

        });
        builder.show();




    }

    public void colorbtn(View view) {
        final CharSequence font[] = new CharSequence[]{"빨강","파랑","초록","노랑","회색","검정","흰색"};

        final AlertDialog.Builder builder1 = new AlertDialog.Builder(this);

        builder1.setItems(font, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i)

                {

                    case 0:
                        color1 = Color.RED;
                        text.setTextColor(color1);

                        break;

                    case 1:
                        color1 = Color.BLUE;
                        text.setTextColor(color1);


                        break;
                    case 2:
                        color1 = Color.GREEN;
                        text.setTextColor(color1);

                        break;
                    case 3:
                        color1 = Color.YELLOW;
                        text.setTextColor(color1);

                        break;
                    case 4:

                        color1 = Color.DKGRAY;
                        text.setTextColor(color1);
                        break;
                    case 5:
                        color1 = Color.BLACK;
                        text.setTextColor(color1);
                        break;
                    case 6:
                        color1 = Color.WHITE;
                        text.setTextColor(color1);
                        break;
                }
                dialogInterface.dismiss();
            }

        });
        builder1.show();

    }
}
