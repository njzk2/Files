package com.njzk2.files;

import java.io.File;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.ListView;

public class DirListFragment extends ListFragment {

	protected static final String TAG = DirListFragment.class.getSimpleName();

	public static Fragment newInstance(String path) {
		Fragment frag = new DirListFragment();
		Bundle args = new Bundle();
		args.putString("path", path);
		frag.setArguments(args);
		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		File root = new File(getArguments().getString("path"));
		setListAdapter(new FileAdapter(getActivity(), root.listFiles()));
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		setEmptyText(getResources().getString(R.string.empty));
		getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		getListView().setMultiChoiceModeListener(new MultiChoiceModeListener() {
			
			@Override
			public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
				return false;
			}
			
			@Override
			public void onDestroyActionMode(ActionMode mode) {
			}
			
			@Override
			public boolean onCreateActionMode(ActionMode mode, Menu menu) {
				MenuInflater inflater = getActivity().getMenuInflater();
	            //inflater.inflate(R.menu.list_select_menu, menu);
				// TODO strings.xml
	            mode.setTitle(R.string.select_items);
				return true;
			}
			
			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				Log.d(TAG, "clicked");
				// TODO Delete
				mode.finish();
				return true;
			}
			
			@Override
			public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
			}
		});
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		File selectedFile = (File) l.getItemAtPosition(position);
		if (selectedFile.isDirectory()) {
			getFragmentManager().beginTransaction().replace(getId(), newInstance(selectedFile.getAbsolutePath()))
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(null)
					.setBreadCrumbTitle(selectedFile.getName()).commit();
		} else {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			String mime = FileAdapter.getMime(selectedFile);
			if (!TextUtils.isEmpty(mime)) {
				intent.setDataAndType(Uri.fromFile(selectedFile), FileAdapter.getMime(selectedFile));
			} else {
				intent.setData(Uri.fromFile(selectedFile));
			}
			try {
				getActivity().startActivity(intent);
			} catch (Exception e) {
				e.printStackTrace();
				// TODO appologize to the user
			}
		}
	}

}
