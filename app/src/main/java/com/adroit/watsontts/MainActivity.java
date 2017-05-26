package com.adroit.watsontts;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ibm.watson.developer_cloud.android.library.audio.StreamPlayer;
import com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voice;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    EditText editText;
    Button button;
    StreamPlayer streamPlayer;
    String text_to_speech;

    private TextToSpeech initTextToSpeechService() {
        TextToSpeech service = new TextToSpeech();
        String username = "8eadb0ea-68e9-4529-84b3-12ef9187273d";
        String password = "8MckDxU5WquK";
        service.setUsernameAndPassword(username, password);
        return service;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.editText);
        button = (Button) findViewById(R.id.button);
        textView = (TextView) findViewById(R.id.teextView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("the text to speech : " + editText.getText());
                textView.setText("TTS: " + editText.getText());

                WatsonTask task = new WatsonTask();
                task.execute();
            }
        });
    }

    private class WatsonTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            text_to_speech = editText.getText().toString();
        }

        @Override
        protected String doInBackground(String... textToSpeak) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textView.setText("running the watson thread");
                }
            });
            TextToSpeech textToSpeech = initTextToSpeechService();
            streamPlayer = new StreamPlayer();
            streamPlayer.playStream(textToSpeech.synthesize(text_to_speech, Voice.EN_LISA).execute());
            return "text to speech done";
        }

        @Override
        protected void onPostExecute(String result) {
            textView.setText("TTS Status : " + result);
        }
    }
}
