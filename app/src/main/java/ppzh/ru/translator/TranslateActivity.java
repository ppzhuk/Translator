package ppzh.ru.translator;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class TranslateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);

        Button goButton = (Button) findViewById(R.id.go_button);
        final EditText toTranslate = (EditText) findViewById(R.id.text_to_translate);

        toTranslate.setText(getIntent().getStringExtra(MainActivity.TEXT));
    }

    private void getTrsnslation() {

    }
}
