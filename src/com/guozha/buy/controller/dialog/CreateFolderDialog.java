package com.guozha.buy.controller.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.guozha.buy.R;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.model.BaseModel;
import com.guozha.buy.model.CollectionModel;
import com.guozha.buy.model.result.CollectionModelResult;
import com.guozha.buy.util.ToastUtil;

/**
 * 创建收藏菜谱文件夹
 * @author PeggyTong
 *
 */
public class CreateFolderDialog extends Activity implements OnClickListener{
	
	private EditText newFolderName;
	private CollectionModel mCollectionModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_create_newfolder);
		setResult(0);
		initView();
		mCollectionModel = new CollectionModel(new MyCollectionModelResult());
	}
	
	private void initView(){
		findViewById(R.id.cancel_button).setOnClickListener(this);
		findViewById(R.id.agree_button).setOnClickListener(this);
		newFolderName = (EditText) findViewById(R.id.create_newfolder_name);
		
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.agree_button:
			String folderName = newFolderName.getText().toString();
			if(folderName.isEmpty()){
				ToastUtil.showToast(CreateFolderDialog.this, "请输入分组名称");
				return;
			}else if(folderName.length() > 10){
				ToastUtil.showToast(CreateFolderDialog.this, "组名称不能超过10个字");
				return;
			}
			requestAddNewFolder(folderName);
			break;
		case R.id.cancel_button:
			CreateFolderDialog.this.finish();
			break;
		}
	}
	
	/**
	 * 请求添加新的分组
	 * @param folderName
	 */
	private void requestAddNewFolder(String folderName){
		int userId = ConfigManager.getInstance().getUserId();
		String token = ConfigManager.getInstance().getUserToken();
		//TODO 去登录
		if(token == null) return;
		mCollectionModel.requestAddCollectDir(this, userId, token, folderName);
	}
	
	class MyCollectionModelResult extends CollectionModelResult{
		@Override
		public void requestAddCollectDirResult(String returnCode, String msg) {
			if(BaseModel.REQUEST_SUCCESS.equals(returnCode)){
				CreateFolderDialog.this.finish();
			}else{
				ToastUtil.showToast(CreateFolderDialog.this, msg);
			}
		}
	}
}
