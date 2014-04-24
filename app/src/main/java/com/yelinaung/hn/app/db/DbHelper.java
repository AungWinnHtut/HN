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
import android.database.sqlite.SQLiteDatabase;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.yelinaung.hn.app.model.Story;
import java.sql.SQLException;

/**
 * Created by Ye Lin Aung on 14/01/30.
 */
public class DbHelper extends OrmLiteSqliteOpenHelper {

  private static final String DATABASE_NAME = "hn.db";
  private static final int DATABASE_VERSION = 1;
  private Dao<Story, Integer> mStoryDao = null;

  public DbHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
    try {
      TableUtils.createTable(connectionSource, Story.class);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i,
      int i2) {
    try {
      TableUtils.dropTable(connectionSource, Story.class, true);
      onCreate(sqLiteDatabase, connectionSource);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public Dao<Story, Integer> getStoryDao() throws SQLException {
    if (mStoryDao == null) {
      mStoryDao = getDao(Story.class);
    }
    return mStoryDao;
  }

  @Override public void close() {
    super.close();
    mStoryDao = null;
  }
}
