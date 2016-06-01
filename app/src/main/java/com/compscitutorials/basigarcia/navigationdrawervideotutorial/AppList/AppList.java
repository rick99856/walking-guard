package com.compscitutorials.basigarcia.navigationdrawervideotutorial.AppList;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.compscitutorials.basigarcia.navigationdrawervideotutorial.DataBase.PkgDAO;
import com.compscitutorials.basigarcia.navigationdrawervideotutorial.MainActivity;
import com.compscitutorials.basigarcia.navigationdrawervideotutorial.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AppList extends Fragment {

    public static final int FILTER_THIRD_APP = 2; // 第三方应用程序

    public AppList() {
        // Required empty public constructor
    }

    private ListView listView = null;
    private PackageManager pm;
    private int filter = FILTER_THIRD_APP;
    private List<AppInfo> mLsitAppInfo;
    private BrowseApplicationInfoAdapter browseAppInfoAdapter = null;
    private View rootView;
    private PkgDAO pkgDAO;

    MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_applist, container, false);

        listView = (ListView) rootView.findViewById(R.id.applist_listView);

        pkgDAO = new PkgDAO(getActivity());
        mLsitAppInfo = queryFilterAppInfo(filter);

        browseAppInfoAdapter = new BrowseApplicationInfoAdapter(getActivity(), mLsitAppInfo);
        listView.setAdapter(browseAppInfoAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckedTextView item = (CheckedTextView) view.findViewById(R.id.txvPkgName);
                String appName = mLsitAppInfo.get(position).getAppLabel();
                String pkgName = mLsitAppInfo.get(position).getPkgName();
                Toast.makeText(getActivity(), ""+appName, Toast.LENGTH_SHORT).show();
                if (item.isChecked()==true){
                    item.setChecked(false);
                    pkgDAO.delete(pkgName);
                }
                else{
                    item.setChecked(true);
                    pkgDAO.add(pkgName);
                }
            }
        });


        // Inflate the layout for this fragment
        return rootView;
    }

    private List<AppInfo> queryFilterAppInfo(int filter) {
        pm = getActivity().getPackageManager();

        List<ApplicationInfo> listAppcations = pm
                .getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);

        Collections.sort(listAppcations,
                new ApplicationInfo.DisplayNameComparator(pm));

        List<AppInfo> appInfos = new ArrayList<AppInfo>();
        appInfos.clear();
        for (ApplicationInfo app : listAppcations){
            if ((app.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
                if (!app.packageName.equals("com.compscitutorials.basigarcia.navigationdrawervideotutorial"))
                    appInfos.add(getAppInfo(app));
            }
        }

        return appInfos;
    }
    String pkgName, appName;
    private AppInfo getAppInfo(ApplicationInfo app){
        AppInfo appInfo = new AppInfo();
        pkgName = app.packageName;
        appName = (String) app.loadLabel(pm);

        appInfo.setAppLabel(appName);
        appInfo.setAppIcon(app.loadIcon(pm));
        appInfo.setPkgName(pkgName);
        pkgDAO.addAll(pkgName, appName);

        return appInfo;
    }



}
