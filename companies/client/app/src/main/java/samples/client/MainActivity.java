package samples.client;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String BASE_URL = "";
    private static final String SERVICE_URL = "http://10.0.2.2:8080/";
    private ListView mKeys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mKeys = (ListView) findViewById(R.id.keys);
        mKeys.setOnItemClickListener((adapterView, view, i, l) -> {
            Uri uri = Uri.parse(BASE_URL + ((TextView) view).getText().toString());
            MainActivity.this.startActivity(new Intent(Intent.ACTION_VIEW, uri));
        });
        new HttpRequestTask().execute();
    }

    private class HttpRequestTask extends AsyncTask<Void, Void, Integer[]> {
        @Override
        protected Integer[] doInBackground(Void... params) {
            try {
                return new RestTemplate().getForObject(SERVICE_URL + "/companies", Integer[].class);
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer[] keys) {
            final List<String> items = new ArrayList<>();
            for (Integer key : keys) {
                items.add(key.toString());
            }
            mKeys.setAdapter(new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, items));
        }
    }
}
