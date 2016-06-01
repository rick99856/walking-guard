package com.compscitutorials.basigarcia.navigationdrawervideotutorial.AppList;

/**
 * Created by Ze on 2016/5/3.
 */

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.compscitutorials.basigarcia.navigationdrawervideotutorial.DataBase.PkgDAO;
import com.compscitutorials.basigarcia.navigationdrawervideotutorial.R;
import com.compscitutorials.basigarcia.navigationdrawervideotutorial.Step.TempPkg;

import java.util.Arrays;
import java.util.List;

//自定义适配器类，提供给listView的自定义view
public class BrowseApplicationInfoAdapter extends BaseAdapter {

    private List<AppInfo> mlistAppInfo = null;
    PkgDAO pkgDAO;
    LayoutInflater infater = null;

    public BrowseApplicationInfoAdapter(Context context, List<AppInfo> apps) {
        infater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mlistAppInfo = apps ;
        pkgDAO = new PkgDAO(context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        System.out.println("size" + mlistAppInfo.size());
        return mlistAppInfo.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mlistAppInfo.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertview, ViewGroup arg2) {
        System.out.println("getView at " + position);
        View view = null;
        ViewHolder holder = null;
        if (convertview == null || convertview.getTag() == null) {
            view = infater.inflate(R.layout.browse_app_item, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        else{
            view = convertview ;
            holder = (ViewHolder) convertview.getTag() ;
        }
        String pkg, app;
        AppInfo appInfo = (AppInfo) getItem(position);
        pkg = appInfo.getPkgName();
        app = appInfo.getAppLabel();
        holder.appIcon.setImageDrawable(appInfo.getAppIcon());
        holder.tvPkgName.setText(app);
//        Log.e("AdapterTAG", "Search " + pkg);
        Boolean stats = pkgDAO.getStats(pkg);
        if(stats)
            holder.tvPkgName.setChecked(true);
        else
            holder.tvPkgName.setChecked(false);
        return view;
    }

    class ViewHolder {
        ImageView appIcon;
        CheckedTextView tvPkgName;

        public ViewHolder(View view) {
            this.appIcon = (ImageView) view.findViewById(R.id.imgApp);
            this.tvPkgName = (CheckedTextView) view.findViewById(R.id.txvPkgName);
        }
    }
}