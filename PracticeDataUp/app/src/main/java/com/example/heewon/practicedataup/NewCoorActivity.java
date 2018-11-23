package com.example.heewon.practicedataup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewCoorActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.example.heewon.coorlistsql.REPLY";

    private EditText editCoorView;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editCoorView = findViewById(R.id.edit_coor);

        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(editCoorView.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    String word = editCoorView.getText().toString();
                    replyIntent.putExtra(EXTRA_REPLY, word);
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });
    }
}
