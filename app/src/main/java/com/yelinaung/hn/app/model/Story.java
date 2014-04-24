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

package com.yelinaung.hn.app.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Ye Lin Aung on 14/04/21.
 */

@DatabaseTable(tableName = "Story")
public class Story {
  @DatabaseField(generatedId = true) public int id;
  @DatabaseField public int story_id;
  @DatabaseField public String comments_link;
  @DatabaseField public String domain;
  @DatabaseField public boolean is_self;
  @DatabaseField public String link;
  @DatabaseField public int num_comments;
  @DatabaseField public int points;
  @DatabaseField public String published_time;
  @DatabaseField public int rank;
  @DatabaseField public String submitter;
  @DatabaseField public String submitter_profile;
  @DatabaseField public String title;

  public Story() {
  }

  public Story(int story_id, String comments_link, String domain, boolean is_self, String link,
      int num_comments, int points, String published_time, int rank, String submitter,
      String submitter_profile, String title) {
    this.story_id = story_id;
    this.comments_link = comments_link;
    this.domain = domain;
    this.is_self = is_self;
    this.link = link;
    this.num_comments = num_comments;
    this.points = points;
    this.published_time = published_time;
    this.rank = rank;
    this.submitter = submitter;
    this.submitter_profile = submitter_profile;
    this.title = title;
  }
}
