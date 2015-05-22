package com.guozha.buy.ui.dialog;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;


/**
 * 通用删除对话框
 * @author PeggyTong
 *
 */
public class CustomDialog{

	   
    private android.app.AlertDialog.Builder builder;  
    private int layout;  
    private AlertDialog dialog;  
    private Window window;  
      
    public CustomDialog(Context context, int layout){  
        builder = new AlertDialog.Builder(context);  
        this.layout = layout;  
    }  
      
    public CustomDialog(Context context, int theme, int layout){  
        builder = new AlertDialog.Builder(context, theme);  
        this.layout = layout;  
    }  
      
    public Builder getBuilder(){  
        return builder;  
    }  
      
    /** 
     * 获取对话框的Window对象 
     * @return 
     */  
    public Window getWindow(){  
        dialog = builder.create();  
        dialog.show();  
        window = dialog.getWindow();  
        window.setContentView(layout);  
        return window;  
    }  
      
    /** 
     * 通过ID获取对应的View 
     * @param id 
     * @return 
     */  
    public View getViewById(int id){  
        if(window == null) getWindow();  
        return window.findViewById(id);  
    }  
      
    /** 
     * 设置需要添加关闭事件的按钮ID 
     * @param id 
     */  
    public void setDismissButtonId(int id){  
        View view = getViewById(id);  
        if(view == null) return;
        view.setOnClickListener(new OnClickListener() {  
            @Override  
            public void onClick(View v) {  
                dismiss();  
            }  
        });  
    }  
      
    /** 
     * 关闭对话框 
     */  
    public void dismiss(){  
        if(dialog != null){  
            dialog.dismiss();  
        }  
    }  
}
