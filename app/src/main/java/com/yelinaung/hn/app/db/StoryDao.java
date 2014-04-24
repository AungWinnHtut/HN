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

package com.yelinaung.hn.app.db;

import android.content.Context;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.yelinaung.hn.app.model.Story;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Ye Lin Aung on 14/01/30.
 */
public class StoryDao {
  private Dao<Story, Integer> mStoryDao;
  private ConnectionSource source;

  public StoryDao(Context ctx) {
    try {
      DbMgr dbManager = new DbMgr();
      DbHelper dbHelper = dbManager.getHelper(ctx);
      mStoryDao = dbHelper.getStoryDao();
      source = dbHelper.getConnectionSource();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public int create(Story Story) {
    try {
      return mStoryDao.create(Story);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return 0;
  }

  public List<Story> getAll() {
    try {
      return mStoryDao.queryForAll();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public void deleteAllStories() throws SQLException {
    TableUtils.clearTable(source, Story.class);
  }
}
