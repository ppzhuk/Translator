package ppzh.ru.translator;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);

        goButton = (Button) findViewById(R.id.go_button);
        toTranslate = (EditText) findViewById(R.id.text_to_translate);
        translated = (TextView) findViewById(R.id.translated);

        textToTranslate = getIntent().getStringExtra(MainActivity.TEXT);
        toTranslate.setText(textToTranslate);

        getTrsnslation();
    }


    private void getTrsnslation() {

        downloadTranslation();
        downloadPictures();


    }

    private void downloadTranslation() {
        String query = formQuery();
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                StringBuilder sb = new StringBuilder();
                try {
                    URL url = new URL(params[0]);
                    XmlPullParser xpp = XmlPullParserFactory.newInstance().newPullParser();
                    xpp.setInput(url.openStream(), null);
                    int eventType = xpp.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.TEXT)
                            sb.append(xpp.getText());
                        eventType = xpp.next();
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {


                } catch (XmlPullParserException e) {

                }

                return sb.toString();
            }

            @Override
            protected void onPostExecute(String s) {
                translated.setText(s);
            }
        }.execute(query);

    }

    private String formQuery() {
        return url+textToTranslate+fromTo;
    }

    private void downloadPictures() {

    }
}
