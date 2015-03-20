package com.guozha.buy.activity.mpage;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.guozha.buy.R;
import com.guozha.buy.activity.global.BaseActivity;
import com.guozha.buy.adapter.SeasonItemListAdapter;
import com.guozha.buy.entry.mpage.season.Season;
import com.guozha.buy.entry.mpage.season.SeasonAdviceItem;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.util.LogUtil;
import com.guozha.buy.util.ToastUtil;
import com.guozha.buy.view.AutoViewFlipper;
import com.umeng.analytics.MobclickAgent;

/**
 * 时令界面
 * @author PeggyTong
 *
 */
public class SeasonActivity extends BaseActivity{
	
	private static final String PAGE_NAME = "SeasonPage";
	
	private static final int HAND_REQUEST_SEASON_COMPLETED = 0x0001; //请求时令信息完成
	private static final int HAND_UPDATE_SEASON_PIC = 0X0002;  //更新时令图片
	
	private ListView mSeasonItemList;
	
	private List<Season> mSeasonsList;
	private AutoViewFlipper mAutoViewFilpper;
	private List<Bitmap> seasonBitmaps;
	
	private List<SeasonAdviceItem> mAdviceItem;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HAND_REQUEST_SEASON_COMPLETED:
				requestSeasonImage();
				updateSeasonAdviceList(1);
				break;
			case HAND_UPDATE_SEASON_PIC:
				updateSeasonImage();
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_season);
		customActionBarStyle("时令养生");
		initView();
		//获取数据
		initData();
	}

	/**
	 * 初始化View
	 */
	private void initView(){
		mSeasonItemList = (ListView) findViewById(R.id.season_vegetable_list);
		mSeasonItemList.setAdapter(new SeasonItemListAdapter(SeasonActivity.this, mAdviceItem));
		
		mAutoViewFilpper = 
				(AutoViewFlipper) findViewById(R.id.season_auto_flipper_view);
		List<ImageView> imageViews = mAutoViewFilpper.getImageViews();
		for(int i = 0; i < imageViews.size(); i++){
			imageViews.get(i).setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View view, MotionEvent event) {
					int index = (Integer) view.getTag();
					updateSeasonAdviceList(index);
					return false;
				}
			});
		}
	}
	
	private void initData(){
		HttpManager.getInstance(this).volleyRequestByPost(
				HttpManager.URL + "season/list", new Listener<String>() {
			@Override
			public void onResponse(String response) {
				Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();  
				mSeasonsList = gson.fromJson(response, new TypeToken<List<Season>>() { }.getType());
				handler.sendEmptyMessage(HAND_REQUEST_SEASON_COMPLETED);
			}
		});
	}

	/**
	 * 更新顶部时令图片
	 */
	private void requestSeasonImage() {
		seasonBitmaps = new ArrayList<Bitmap>();
		if(mSeasonsList != null){
			for(int i = 0; i < mSeasonsList.size(); i++){
				Season season = mSeasonsList.get(i);
				if(season == null) continue;
				String imageUrl = season.getSeasonPicUrl();
				if(imageUrl == null) continue;
				LogUtil.e("imageUrl = " + imageUrl);
				HttpManager.getInstance(this).volleyImageRequest(
					HttpManager.URL + imageUrl.substring(1, imageUrl.length()), new Listener<Bitmap>() {
						@Override
						public void onResponse(Bitmap response) {
							seasonBitmaps.add(response);
							handler.sendEmptyMessage(HAND_UPDATE_SEASON_PIC);
						}
				});
			}
		}
	}
	
	/**
	 * 更新上面的图片
	 */
	private void updateSeasonImage(){
		mAutoViewFilpper.setImages(seasonBitmaps);
	}
	
	/**
	 * 更改列表数据
	 * @param index
	 */
	private void updateSeasonAdviceList(int index){
		if(mSeasonsList == null) return;
		Season season = mSeasonsList.get(index);
		if(season == null) return;
		mAdviceItem = season.getGoodsList();
		mSeasonItemList.setAdapter(new SeasonItemListAdapter(SeasonActivity.this, mAdviceItem));
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		//友盟界面统计
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart(PAGE_NAME);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		//友盟界面统计
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd(PAGE_NAME);
	}
}
