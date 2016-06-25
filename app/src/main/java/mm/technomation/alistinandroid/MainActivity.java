package mm.technomation.alistinandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import mm.technomation.alistin.Alistin;

public class MainActivity extends AppCompatActivity {

    Alistin alistin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alistin = new Alistin(this);

    }

    public void play(View v) {
        String s = ((EditText) findViewById(R.id.etParagraph)).getText().toString() + "";
        alistin.speak(s);
    }

    public void stop(View v) {
        alistin.stop();
    }

    public void clear(View v) {
        ((EditText) findViewById(R.id.etParagraph)).setText(null);
    }
}
