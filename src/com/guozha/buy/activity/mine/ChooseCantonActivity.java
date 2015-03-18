package com.guozha.buy.activity.mine;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.guozha.buy.R;
import com.guozha.buy.activity.global.BaseActivity;

/**
 * 选择行政区
 * @author PeggyTong
 *
 */
public class ChooseCantonActivity extends BaseActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_choose_canton);
		customActionBarStyle("行政区");
		
		List<String> cantons = new ArrayList<String>();
		for(int i = 0; i < 10; i++){
			cantons.add("西湖区" + i);
		}
		
		ListView cantonList = (ListView) findViewById(R.id.canton_item_list);
		cantonList.setAdapter(new ArrayAdapter<String>(this, R.layout.list_canton_item_cell, cantons));
		cantonList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ChooseCantonActivity.this.finish();
			}
		});
	}

}
