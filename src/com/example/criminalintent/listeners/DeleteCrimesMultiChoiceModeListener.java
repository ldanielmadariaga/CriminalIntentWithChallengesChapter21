package com.example.criminalintent.listeners;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.support.v4.app.ListFragment;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.ArrayAdapter;

import com.example.criminalintent.Crime;
import com.example.criminalintent.CrimeLab;
import com.example.criminalintent.R;

@TargetApi(11)
class DeleteCrimesMultiChoiceModeListener implements MultiChoiceModeListener {

	ListFragment listFragment;

	private DeleteCrimesMultiChoiceModeListener(ListFragment listFragment) {
		this.listFragment = listFragment;
	}

	public static DeleteCrimesMultiChoiceModeListener getInstance(ListFragment listFragment) {
		return new DeleteCrimesMultiChoiceModeListener(listFragment);
	}

	@Override
	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		Boolean isActionItemClicked = false;
		switch (item.getItemId()) {
		case R.id.menu_item_delete_crime:
			@SuppressWarnings("unchecked")
			ArrayAdapter<Crime> crimeListAdapter = (ArrayAdapter<Crime>) listFragment.getListAdapter();
			CrimeLab crimeLab = CrimeLab.getInstance(listFragment.getActivity());
			ArrayList<Crime> crimes = crimeLab.getCrimes();
			ArrayList<Crime> crimesToDelete = new ArrayList<Crime>();
			for (Crime crime : crimes) {
				if (isItemChecked(crimes, crime)) {
					crimesToDelete.add(crimeListAdapter.getItem(crimes.indexOf(crime)));
				}
			}

			for (Crime crime : crimesToDelete) {
				crimeLab.deleteCrime(crime);
			}

			mode.finish();
			crimeListAdapter.notifyDataSetChanged();
			isActionItemClicked = true;
			break;

		default:
			isActionItemClicked = false;
			break;
		}
		return isActionItemClicked;
	}

	private boolean isItemChecked(ArrayList<Crime> crimes, Crime crime) {
		return listFragment.getListView().isItemChecked(crimes.indexOf(crime));
	}

	@Override
	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		MenuInflater menuInflater = mode.getMenuInflater();
		menuInflater.inflate(R.menu.crime_list_item_context, menu);
		return true;
	}

	@Override
	public void onDestroyActionMode(ActionMode arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onPrepareActionMode(ActionMode arg0, Menu arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onItemCheckedStateChanged(ActionMode arg0, int arg1, long arg2, boolean arg3) {
		// TODO Auto-generated method stub

	}

}
