
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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.yelinaung.hn.app.R;
import com.yelinaung.hn.app.model.Story;
import java.util.ArrayList;

/**
 * Created by Ye Lin Aung on 14/04/21.
 */
public class StoryAdapter extends ArrayAdapter<Story> {

  Context mContext;
  ArrayList<Story> stories;

  public StoryAdapter(Context context, ArrayList<Story> objects) {
    super(context, R.layout.row_story, objects);
    this.mContext = context;
    this.stories = objects;
  }

  @Override public View getView(int position, View convertView, ViewGroup parent) {
    final ViewHolder holder;
    LayoutInflater inflater = LayoutInflater.from(mContext);
    if (convertView != null) {
      holder = (ViewHolder) convertView.getTag();
    } else {
      convertView = inflater.inflate(R.layout.row_story, parent, false);
      holder = new ViewHolder(convertView);
      assert convertView != null;
      convertView.setTag(holder);
    }

    holder.points.setText("" + stories.get(position).points);
    holder.mTitle.setText(stories.get(position).title);
    holder.num_comments.setText(stories.get(position).num_comments + " Comments");

    return convertView;
  }

  static class ViewHolder {
    @InjectView(R.id.points) TextView points;
    @InjectView(R.id.title) TextView mTitle;
    @InjectView(R.id.num_comments) TextView num_comments;

    public ViewHolder(View view) {
      ButterKnife.inject(this, view);
    }
  }
}
