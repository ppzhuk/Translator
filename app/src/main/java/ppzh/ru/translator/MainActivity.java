package ppzh.ru.translator;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static final String TEXT = "ppzh.ru.translator.text";
    private EditText toTranslate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button goButton = (Button) findViewById(R.id.go_button);
        toTranslate = (EditText) findViewById(R.id.text_to_translate);

        toTranslate.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    openTranslation(toTranslate, v);
                    return true;
                }
                return false;
            }
        });

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTranslation(toTranslate, v);
            }
        });
    }

    private void openTranslation(EditText et, View v) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);

        String text = et.getText().toString().trim();
        if (text.length() == 0) {
            Snackbar.make(v, R.string.empty_text_msg, Snackbar.LENGTH_LONG).show();
        } else {
            Intent i = new Intent(v.getContext(), TranslateActivity.class);
            i.putExtra(TEXT, text);
            startActivity(i);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (toTranslate != null)
            toTranslate.setText("");
    }
}
