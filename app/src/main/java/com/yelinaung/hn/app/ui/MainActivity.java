package com.yelinaung.hn.app.ui;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ListView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.yelinaung.hn.app.R;
import com.yelinaung.hn.app.model.Story;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class MainActivity extends Activity {

  @InjectView(R.id.list_view) ListView mNewsListView;
  @InjectView(R.id.swipe_view) SwipeRefreshLayout mSwipeView;

  ArrayList<Story> stories = new ArrayList<Story>();
  StoryAdapter storyAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.inject(this);

    mSwipeView.setColorScheme(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
        android.R.color.holo_red_light, android.R.color.holo_orange_light);
    mSwipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override public void onRefresh() {
        new GetStories().execute();
      }
    });
  }

  public class GetStories extends AsyncTask<Void, Void, Void> {

    @Override protected void onPreExecute() {
      super.onPreExecute();
      mSwipeView.setRefreshing(true);
    }

    @Override protected Void doInBackground(Void... params) {
      Ion.with(MainActivity.this)
          .load("http://hnify.herokuapp.com/get/top")
          .asString()
          .setCallback(new FutureCallback<String>() {
            @Override public void onCompleted(Exception e, String result) {
              try {
                if (e != null) throw e;
                mSwipeView.setRefreshing(false);
                JsonParser p = new JsonParser();
                try {
                  InputStream in = new ByteArrayInputStream(result.getBytes("UTF-8"));
                  JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
                  JsonObject storiesObject = (JsonObject) p.parse(reader);
                  JsonArray storiesJsonArray = storiesObject.getAsJsonArray("stories");
                  Gson gson = new GsonBuilder().create();
                  stories.clear();
                  for (int i = 0; i < storiesJsonArray.size(); i++) {
                    Story s =
                        gson.fromJson(((JsonObject) (storiesJsonArray.get(i)).getAsJsonObject()),
                            Story.class);
                    stories.add(s);
                    storyAdapter = new StoryAdapter(MainActivity.this, stories);
                    storyAdapter.notifyDataSetChanged();
                    mNewsListView.setAdapter(storyAdapter);
                  }
                } catch (UnsupportedEncodingException e1) {
                  e1.printStackTrace();
                }
              } catch (Exception ex) {
                ex.printStackTrace();
                Toast.makeText(MainActivity.this, "Can't connect", Toast.LENGTH_SHORT).show();
                mSwipeView.setRefreshing(false);
              }
            }
          });
      return null;
    }

    @Override protected void onPostExecute(Void aVoid) {
      super.onPostExecute(aVoid);
    }
  }
}