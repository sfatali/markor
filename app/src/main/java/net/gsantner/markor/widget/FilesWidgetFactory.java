/*
 * Copyright (c) 2014 Jeff Martin
 * Copyright (c) 2015 Pedro Lafuente
 * Copyright (c) 2017 Gregor Santner and Markor contributors
 *
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */
package net.gsantner.markor.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import net.gsantner.markor.R;
import net.gsantner.markor.model.Constants;

import java.io.File;
import java.io.FileFilter;

public class FilesWidgetFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context _context;
    private File[] _widgetFilesList = new File[0];
    private int _appWidgetId;
    private File _dir;

    public FilesWidgetFactory(Context context, Intent intent) {
        _context = context;
        _appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        _dir = new File(intent.getStringExtra(Constants.EXTRA_FOLDERPATH));
    }

    @Override
    public void onCreate() {
        onDataSetChanged();
    }

    @Override
    public void onDataSetChanged() {
        updateFiles();
    }

    private void updateFiles() {
        this._widgetFilesList = _dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return !pathname.isDirectory();
            }
        });
    }

    @Override
    public void onDestroy() {
        _widgetFilesList = new File[0];
    }

    @Override
    public int getCount() {
        return _widgetFilesList.length;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        File file = _widgetFilesList[position];
        Intent fillInIntent = new Intent().putExtra(Constants.NOTE_KEY, file);
        RemoteViews rowView = new RemoteViews(_context.getPackageName(), R.layout.widget_file_item);
        rowView.setTextViewText(R.id.widget_note_title, Constants.MD_EXTENSION.matcher(file.getName()).replaceAll(""));
        rowView.setOnClickFillInIntent(R.id.widget_note_title, fillInIntent);
        return rowView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
