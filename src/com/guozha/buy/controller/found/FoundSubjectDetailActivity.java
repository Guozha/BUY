package com.guozha.buy.controller.found;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.guozha.buy.R;
import com.guozha.buy.adapter.FoundSubjectDetailListAdapter;
import com.guozha.buy.controller.BaseActivity;
import com.guozha.buy.entry.found.SubjectDetail;
import com.guozha.buy.global.net.BitmapCache;
import com.guozha.buy.model.FoundModel;
import com.guozha.buy.model.result.FoundModelResult;
import com.guozha.buy.util.DimenUtil;

/**
 * 发现-主题 的详情
 * @author PeggyTong
 *
 */
public class FoundSubjectDetailActivity extends BaseActivity{
	
	private static final int HAND_SUBJECT_DETAIL_COMPLETED = 0x0001;
	
	private ImageView mHeadImage;   //头部图片
	private TextView mHeadTitle;	//头部标题
	private TextView mHeadDescript; //头部描述
	private ListView mDetailList;
	
	private int mSubjectId;
	private String mSubjectName;
	private String mSubjectDescript;
	private BitmapCache mBitmapCache = BitmapCache.getInstance();
	private List<SubjectDetail> mSubjectDetails = new ArrayList<SubjectDetail>();
	
	private FoundModel mFoundModel = new FoundModel(new MyFoundModelResult());
	
	private FoundSubjectDetailListAdapter mSubjectDetailAdapter;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HAND_SUBJECT_DETAIL_COMPLETED:
				mSubjectDetailAdapter.notifyDataSetChanged();
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_found_subject_detail);
		customActionBarStyle("主题详情");
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if(bundle != null){
			mSubjectId = bundle.getInt("subjectId");
			mSubjectName = bundle.getString("subjectName");
			mSubjectDescript = bundle.getString("subjectDescript");
		}
		initView();
		initData();
	}
	
	private void initView(){
		mDetailList = (ListView) findViewById(R.id.subject_detail_list);
		mDetailList.addHeaderView(getLayoutInflater()
				.inflate(R.layout.list_found_subject_header, null));
		mHeadImage = (ImageView) findViewById(R.id.subject_detail_head_image);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				DimenUtil.getScreenWidth(this), DimenUtil.getScreenWidth(this) / 2);
		mHeadImage.setLayoutParams(params);
		mHeadTitle = (TextView) findViewById(R.id.subject_detail_head_title);
		mHeadDescript = (TextView) findViewById(R.id.subject_detail_head_descript);
		
		
		mSubjectDetailAdapter = new FoundSubjectDetailListAdapter(
				this, mSubjectDetails, mBitmapCache);
		
		mHeadImage.setImageResource(R.drawable.temp_subject_item_imag);
		mHeadTitle.setText(mSubjectName);
		mHeadDescript.setText(mSubjectDescript);
		
		mDetailList.setAdapter(mSubjectDetailAdapter);
	}
	
	private void initData(){
		mSubjectDetails.clear();
		mFoundModel.requestFoundSubjectDetail(this, mSubjectId);
	}
	
	class MyFoundModelResult extends FoundModelResult{
		@Override
		public void requestFoundSubjectDetailResult(
				List<SubjectDetail> subjectDetails) {
			if(subjectDetails == null) return;
			mSubjectDetails.addAll(subjectDetails);
			mHandler.sendEmptyMessage(HAND_SUBJECT_DETAIL_COMPLETED);
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		mBitmapCache.fluchCache();
	}
}
