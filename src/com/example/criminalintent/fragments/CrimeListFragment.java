package com.example.criminalintent.fragments;

import java.util.ArrayList;

import android.R.id;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.criminalintent.Crime;
import com.example.criminalintent.CrimeLab;
import com.example.criminalintent.R;
import com.example.criminalintent.activities.CrimePagerActivity;
import com.example.criminalintent.adapters.AdapterFactory;
import com.example.criminalintent.listeners.ListenerFactory;

public class CrimeListFragment extends ListFragment {

	private ArrayList<Crime> crimes;
	private Boolean subtitleVisible;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		getActivity().setTitle(R.string.crimes_title);
		CrimeLab crimeLab = CrimeLab.getInstance(getActivity());
		crimes = crimeLab.getCrimes();

		ArrayAdapter<Crime> arrayAdapter = AdapterFactory.getCrimeListAdapter(getActivity(), crimes);
		setListAdapter(arrayAdapter);

		setRetainInstance(true);
		subtitleVisible = false;
	}

	@TargetApi(11)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = super.onCreateView(inflater, container, savedInstanceState);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			if (subtitleVisible) {
				getActivity().getActionBar().setSubtitle(R.string.show_subtitle);
			}
		}
		ListView listView = (ListView) view.findViewById(id.list);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
			listView.setMultiChoiceModeListener(ListenerFactory.getCrimesMultiChoiceModeListener(this));
		} else {
			registerForContextMenu(listView);
		}

		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_crime_list, menu);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		getActivity().getMenuInflater().inflate(R.menu.crime_list_item_context, menu);
	}

	@Override
	public void onResume() {
		super.onResume();
		@SuppressWarnings("unchecked")
		ArrayAdapter<Crime> arrayAdapter = (ArrayAdapter<Crime>) getListAdapter();
		arrayAdapter.notifyDataSetChanged();
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		Boolean isContextItemSelected = false;
		AdapterContextMenuInfo adapterContextMenuInfo = (AdapterContextMenuInfo) item.getMenuInfo();
		@SuppressWarnings("unchecked")
		ArrayAdapter<Crime> crimeListAdapter = (ArrayAdapter<Crime>) getListAdapter();
		int position = adapterContextMenuInfo.position;
		Crime crime = crimeListAdapter.getItem(position);

		switch (item.getItemId()) {
		case R.id.menu_item_delete_crime:
			CrimeLab.getInstance(getActivity()).deleteCrime(crime);
			crimeListAdapter.notifyDataSetChanged();
			isContextItemSelected = true;
			break;
		default:
			isContextItemSelected = super.onContextItemSelected(item);
			break;
		}

		return isContextItemSelected;
	}

	@TargetApi(11)
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean optionsItemsSelected;

		switch (item.getItemId()) {
		case R.id.menu_item_new_crime:
			Crime crime = new Crime();
			CrimeLab.getInstance(getActivity()).addCrime(crime);
			Intent intent = new Intent(getActivity(), CrimePagerActivity.class);
			intent.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
			startActivityForResult(intent, 0);
			optionsItemsSelected = true;
			break;
		case R.id.menu_item_show_subtitle:
			if (getActivity().getActionBar().getSubtitle() == null) {
				getActivity().getActionBar().setSubtitle(R.string.show_subtitle);
				subtitleVisible = true;
				item.setTitle(R.string.hide_subtitle);
			} else {
				getActivity().getActionBar().setSubtitle(null);
				subtitleVisible = false;
				item.setTitle(R.string.show_subtitle);
			}
			optionsItemsSelected = true;
		default:
			optionsItemsSelected = super.onOptionsItemSelected(item);
			break;
		}

		return optionsItemsSelected;
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		@SuppressWarnings("unchecked")
		Crime crime = ((ArrayAdapter<Crime>) getListAdapter()).getItem(position);
		Intent intent = new Intent(getActivity(), CrimePagerActivity.class);
		intent.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
		startActivity(intent);
	}
}
