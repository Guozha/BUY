package com.guozha.buy.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.guozha.buy.R;
import com.guozha.buy.adapter.newfold.FoundSubjectListAdapter;

/**
 * 发现-专题
 * @author PeggyTong
 *
 */
public class FoundSubjectFragment extends BaseFragment{
	
	private static final String PAGE_NAME = "FoundSubjectPage";
	private ListView mSubjectListView;
	private FoundSubjectListAdapter mSubjectListAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_found_subject, container, false);
		initView(view); 
		return view;
	}
	
	private void initView(View view){
		mSubjectListView = (ListView) view.findViewById(R.id.found_subject_list);
		mSubjectListView.setItemsCanFocus(true);
		mSubjectListAdapter = new FoundSubjectListAdapter(getActivity());
		mSubjectListView.setAdapter(mSubjectListAdapter);
		
		mSubjectListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
			}
		});
	}

}
