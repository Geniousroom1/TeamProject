package edu.android.teamproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class TextsActivity extends AppCompatActivity {
    public static String textActivity_Text;
    public static boolean textCancel;
    private EditText text;
    public static StickerTextView tv_sticker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_texts);
        text = (EditText) findViewById(R.id.editText);
    }

    public void btn_cancel(View view) {
        textCancel = true;
        Toast.makeText(this, "취소", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void btn_enter(View view) {
        textCancel = false;
        textActivity_Text = text.getText().toString();
        tv_sticker = new StickerTextView(TextsActivity.this);
        tv_sticker.setText(TextsActivity.textActivity_Text);
        MainActivity.inflatedLayout.addView(tv_sticker);
        finish();
    }
}
