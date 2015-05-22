package com.guozha.buy.ui.dialog;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.android.volley.Response.Listener;
import com.guozha.buy.R;
import com.guozha.buy.global.ConfigManager;
import com.guozha.buy.global.net.HttpManager;
import com.guozha.buy.global.net.RequestParam;
import com.guozha.buy.util.ToastUtil;

/**
 * 创建收藏菜谱文件夹
 * @author PeggyTong
 *
 */
public class CreateFolderDialog extends Activity implements OnClickListener{
	
	private EditText newFolderName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_create_newfolder);
		setResult(0);
		initView();
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
		String token = ConfigManager.getInstance().getUserToken(CreateFolderDialog.this);
		if(token == null) return;
		RequestParam paramPath = new RequestParam("account/myfavo/insertDir")
		.setParams("userId", userId)
		.setParams("token", token)
		.setParams("dirName", folderName);
		HttpManager.getInstance(this).volleyJsonRequestByPost(
				HttpManager.URL + paramPath, new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					String returnCode = response.getString("returnCode");
					if("1".equals(returnCode)){
						CreateFolderDialog.this.finish();
					}else{
						ToastUtil.showToast(CreateFolderDialog.this, response.getString("msg"));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
