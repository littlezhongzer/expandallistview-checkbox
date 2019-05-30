package com.example.quectel.stress;


import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Switch;

import static android.content.ContentValues.TAG;


public class MainActivity extends Activity implements View.OnClickListener{

    ArrayList<Group> groups;
    ExpandableListView listView;
    EListAdapter adapter;

    SparseArray<Boolean> mySparseArray = new SparseArray();

    Button btn_start,btn_all_check,btn_all_clear;
    static boolean flag_btn_start=false;
    static boolean flag_btn_allcheck_child=false;
    static boolean flag_btn_allcheck_group=false;
    static boolean flag_btn_allclear=false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitView();
    }

    /** 解悉 JSON 字串 */
    private void getJSONObject() {
        String jsonStr = "{'CommunityUsersResult':[{'CommunityUsersList':[{'fullname':'SMS','userid':11,'username':'a1'}"
                + ",{'fullname':'b222','userid':12,'username':'b2'}],'id':1,'title':'Setting测试'},{'CommunityUsersList':[{'fullname':"
                + "'c333','userid':13,'username':'c3'},{'fullname':'d444','userid':14,'username':'d4'},{'fullname':'e555','userid':"
                + "15,'username':'e5'}],'id':2,'title':'Wifi测试'}]}";

        try {
            JSONObject CommunityUsersResultObj = new JSONObject(jsonStr);
            JSONArray groupList = CommunityUsersResultObj.getJSONArray("CommunityUsersResult");

            for (int i = 0; i < groupList.length(); i++) {
                JSONObject groupObj = (JSONObject) groupList.get(i);
                Group group = new Group(groupObj.getString("id"), groupObj.getString("title"));
                JSONArray childrenList = groupObj.getJSONArray("CommunityUsersList");

                for (int j = 0; j < childrenList.length(); j++) {
                    JSONObject childObj = (JSONObject) childrenList.get(j);
                    Child child = new Child(childObj.getString("userid"), childObj.getString("fullname"),
                            childObj.getString("username"));
                    group.addChildrenItem(child);
                }

                groups.add(group);
            }
        } catch (JSONException e) {
            Log.d("allenj", e.toString());
        }
    }

    public void InitView(){
        btn_start=findViewById(R.id.btn_click_start);
        btn_all_check=findViewById(R.id.btn_click_allcheck);
        btn_all_clear=findViewById(R.id.btn_click_allclear);
        btn_start.setOnClickListener(this);
        btn_all_check.setOnClickListener(this);
        btn_all_clear.setOnClickListener(this);

        groups = new ArrayList<Group>();
        getJSONObject();
        listView = (ExpandableListView) findViewById(R.id.expend_list);
        adapter = new EListAdapter(this, groups);
        listView.setAdapter(adapter);
        listView.setOnChildClickListener(adapter);
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_click_start:
                flag_btn_start=true;
                Log.d(TAG, "Main Click start flag:" +flag_btn_start);
                break;
            case R.id.btn_click_allcheck:
                if(flag_btn_allcheck_group==true) {
                    Message message_allcheck = new Message();
                    //handler.removeMessages(message_allcheck.what);
                    message_allcheck.what = 0x11;
                    handler.sendMessageDelayed(message_allcheck, 100);
                    btn_all_check.setText("重置");
                    flag_btn_allcheck_group=false;
                    //flag_btn_allclear=true;
                    break;
                }
                if(flag_btn_allcheck_group==false){Message message_allclear = new Message();
                    message_allclear.what = 0x12;
                    handler.sendMessage(message_allclear);
                    btn_all_check.setText("全选");
                    flag_btn_allcheck_group=true;
                    //flag_btn_allclear=false;
                    Log.d(TAG, "Main Click all clear:" + flag_btn_allcheck_group);
                    break;
                }
                Log.d(TAG, "Main Click all check flag:"+ flag_btn_allcheck_group);

            case R.id.btn_click_allclear:
             break;
        }
    }

    @SuppressLint("HandlerLeak")
    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x11) {
                for(int i=0;i<groups.size();i++) {
                    Log.d(TAG, "handleMessage: "+groups.size());
                    groups.get(i).setChecked(true);
                    mySparseArray.put(i,groups.get(i).getChecked());
                    Log.d(TAG, "onClick: start click sparsearray "+ mySparseArray.get(0));
                    Log.d(TAG, "onClick: start click sparsearray "+ mySparseArray.get(1));
                    Log.d(TAG, "onClick: start click sparsearray "+ mySparseArray.get(2));
                    Log.d(TAG, "onClick: start click sparsearray "+ mySparseArray.get(3));
                    Log.d(TAG, "onClick: start click sparsearray "+ mySparseArray.get(4));
                    Log.d(TAG, "onClick: start click sparsearray "+ mySparseArray.get(5));
                    adapter.notifyDataSetChanged();
                }
                // removeMessages(newMsg.what);
            }else if (msg.what==0x12) {
                for(int ii=0;ii<groups.size();ii++) {
                    groups.get(ii).setChecked(false);
                    mySparseArray.put(ii,groups.get(ii).getChecked());
                    Log.d(TAG, "onClick: start click sparsearray "+ mySparseArray.get(0));
                    Log.d(TAG, "onClick: start click sparsearray "+ mySparseArray.get(1));
                    Log.d(TAG, "onClick: start click sparsearray "+ mySparseArray.get(2));
                    Log.d(TAG, "onClick: start click sparsearray "+ mySparseArray.get(3));
                    Log.d(TAG, "onClick: start click sparsearray "+ mySparseArray.get(4));
                    Log.d(TAG, "onClick: start click sparsearray "+ mySparseArray.get(5));
                    adapter.notifyDataSetChanged();
                }
            }
        }
    };

    public static boolean getBtn_start_Flag(){
        return flag_btn_start;
    }
    public static boolean getBtn_allcheck_child_Flag(){
        return flag_btn_allcheck_child;
    }
    public static boolean getBtn_allcheck_group_Flag(){
        return flag_btn_allcheck_group;
    }
    public static boolean getBtn_allclear_Flag(){
        return flag_btn_allclear;
    }

}
