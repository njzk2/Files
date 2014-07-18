package com.njzk2.files;

import java.io.File;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class FileAdapter extends ArrayAdapter<File> {

	public FileAdapter(Context context, File[] objects) {
		super(context, 0, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView view = (TextView) (convertView != null ? convertView : LayoutInflater.from(getContext()).inflate(
				android.R.layout.simple_list_item_activated_1, null));

		File item = getItem(position);
		view.setCompoundDrawablesWithIntrinsicBounds(getIcon(item), 0, 0, 0);
		view.setCompoundDrawablePadding(view.getPaddingLeft());
		view.setText(item.getName() + (item.isDirectory() ? "/" : ""));
		return view;
	}

	private static int getIcon(File file) {
		if (file.isDirectory()) {
			return R.drawable.ic_action_collection;
		}
		String mime = getMime(file);
		if (mime.contains("image")) {
			return R.drawable.ic_action_picture;
		}
		if (mime.contains("video")) {
			return R.drawable.ic_action_video;
		}
		if (mime.contains("audio") || mime.contains("ogg")) {
			return R.drawable.ic_action_volume_on;
		}
		System.out.println(mime);

		return 0;
	}

	public static String getMime(File file) {
		String ext = MimeTypeMap.getFileExtensionFromUrl(file.getName());
		if (!TextUtils.isEmpty(ext)) {
			String mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext);
			if (mime != null) {
				return mime;
			}
		}
		return "";
	}
}
