/*
 * Copyright 2014 Ye Lin Aung
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yelinaung.hn.app.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
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
import com.yelinaung.hn.app.db.StoryDao;
import com.yelinaung.hn.app.model.Story;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class MainActivity extends Activity {

  @InjectView(R.id.list_view) ListView mNewsListView;
  @InjectView(R.id.swipe_view) SwipeRefreshLayout mSwipeView;

  private ArrayList<Story> stories = new ArrayList<Story>();
  private StoryAdapter storyAdapter;
  private StoryDao storyDao;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.inject(this);

    ActionBar actionBar = getActionBar();
    assert actionBar != null;
    actionBar.setTitle("Hacker News");

    storyDao = new StoryDao(MainActivity.this);

    mSwipeView.setColorScheme(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
        android.R.color.holo_red_light, android.R.color.holo_orange_light);
    mSwipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override public void onRefresh() {
        loadNews();
      }
    });

    mNewsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent browserIntent =
            new Intent(Intent.ACTION_VIEW, Uri.parse(stories.get(position).link));
        startActivity(browserIntent);
      }
    });
  }

  private void loadNews() {
    mSwipeView.setRefreshing(true);
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
                storyDao.deleteAllStories();

                for (int i = 0; i < storiesJsonArray.size(); i++) {
                  Story s = gson.fromJson((storiesJsonArray.get(i)).getAsJsonObject(), Story.class);
                  stories.add(s);
                  storyDao.create(s);
                }

                storyAdapter.notifyDataSetChanged();
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
  }

  @Override protected void onResume() {
    super.onResume();
    stories = (ArrayList<Story>) storyDao.getAll();
    storyAdapter = new StoryAdapter(MainActivity.this, stories);
    mNewsListView.setAdapter(storyAdapter);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    ButterKnife.reset(this);
  }

  @Override protected void onStart() {
    super.onStart();
  }
}
