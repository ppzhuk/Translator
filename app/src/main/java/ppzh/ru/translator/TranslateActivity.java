package ppzh.ru.translator;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class TranslateActivity extends AppCompatActivity {
    private final String url =
        "https://translate.yandex.net/api/v1.5/tr/translate" +
        "?key=trnsl.1.1.20160306T080530Z.55125315a1bb2d4e.576ead730c7369204404407980a74cbec04f1f6c" +
        "&text=";
    private String textToTranslate;
    private final String fromTo = "&lang=en-ru";

    private TextView translated;
    private Button goButton;
    private EditText toTranslate;
    private ProgressBar downloadProgress;
    private GridView grid;

    private ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);

        goButton = (Button) findViewById(R.id.go_button);
        toTranslate = (EditText) findViewById(R.id.text_to_translate);
        translated = (TextView) findViewById(R.id.translated);
        downloadProgress = (ProgressBar) findViewById(R.id.progressBar);
        grid = (GridView) findViewById(R.id.gridView);
        imageAdapter = new ImageAdapter(this);
        // TODO
        // disable clickability on gridView
        grid.setAdapter(imageAdapter);

        textToTranslate = getIntent().getStringExtra(MainActivity.TEXT);
        toTranslate.setText(textToTranslate);
        getTrsnslation();

        goButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                translate(v);
            }
        });

        toTranslate.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    // TODO: prevent hiding keyboard if editText is empty
                    translate(v);
                    return true;
                }
                return false;
            }
        });
    }

    private void translate(View v) {
        String text = toTranslate.getText().toString().trim();
        if (text.length() == 0) {
            Snackbar.make(v, R.string.empty_text_msg, Snackbar.LENGTH_LONG).show();
            toTranslate.setText("");
            return;
        }
        if (!text.equals(textToTranslate)) {
            textToTranslate = text;
            getTrsnslation();
        }

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
    }

    private void getTrsnslation() {
        downloadTranslation();
        downloadPictures();
    }

    private void downloadTranslation() {
        String query = formTranslationQuery();
        new AsyncTask<String, Integer, String>() {
            @Override
            protected void onPreExecute() {
                downloadProgress.setProgress(0);
                downloadProgress.setVisibility(ProgressBar.VISIBLE);
            }

            @Override
            protected String doInBackground(String... params) {
                publishProgress(1);
                StringBuilder sb = new StringBuilder();
                try {
                    URL url = new URL(params[0]);
                    XmlPullParser xpp = XmlPullParserFactory.newInstance().newPullParser();
                    xpp.setInput(url.openStream(), null);
                    publishProgress(2);
                    int eventType = xpp.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.TEXT)
                            sb.append(xpp.getText());
                        eventType = xpp.next();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    return getResources().getString(R.string.incorrect_query_error);
                } catch (IOException | XmlPullParserException e) {
                    e.printStackTrace();
                    return getResources().getString(R.string.translation_error);
                }
                publishProgress(3);
                return sb.toString();
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                downloadProgress.setProgress(values[0]);
            }

            @Override
            protected void onPostExecute(String s) {
                translated.setText(s);
            }
        }.execute(query);

    }

    private String formTranslationQuery() {
        return url+textToTranslate+fromTo;
    }

    // TODO
    // add forming query for image downloading
    private String formImagesQuery() {
        return translated.getText().toString();
    }

    private void downloadPictures() {
        String query = formImagesQuery();
        new AsyncTask<String, Integer, String[]>() {
            @Override
            protected String[] doInBackground(String... params) {
                publishProgress(4);
                // TODO
                // execute request and get images' urls
                String url =
                        "https://compscicenter.ru/media/cache/04/af/04af00c38359378a149700f9f9786f27.jpg";
                String[] imageUrls = new String[20];
                Arrays.fill(imageUrls, url);
                publishProgress(5);
                return imageUrls;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                downloadProgress.setProgress(values[0]);
            }

            @Override
            protected void onPostExecute(String[] imageUrls) {
                // TODO
                // Image downloading executes in imageAdapter, so
                // progress bar isn't update with it. Don't like it.
                imageAdapter.setContent(imageUrls);
                imageAdapter.notifyDataSetChanged();
                downloadProgress.setVisibility(ProgressBar.INVISIBLE);
            }

        }.execute(query);
    }
}
