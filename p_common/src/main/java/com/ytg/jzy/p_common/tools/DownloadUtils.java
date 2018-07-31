package com.ytg.jzy.p_common.tools;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.io.FileNotFoundException;

@SuppressLint("NewApi") public class DownloadUtils {
	//下载器
    private DownloadManager downloadManager;
    //上下文
    private Context mContext;
    //下载的ID
    private long downloadId;
    public  DownloadUtils(Context context){
        this.mContext = context;
    }

    //下载apk
    public void downloadAPK(String url, String name) {

        //创建下载任务
        Request request = new Request(Uri.parse(url));
        //移动网络情况下是否允许漫游
        request.setAllowedOverRoaming(false);

        //在通知栏中显示，默认就是显示的
        request.setNotificationVisibility(Request.VISIBILITY_VISIBLE);
        request.setTitle("name");
        request.setDescription("Downloading");
        request.setVisibleInDownloadsUi(true);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        //设置下载的路径
        request.setDestinationInExternalPublicDir(mContext.getPackageName()+"/file" , name);

       //获取DownloadManager
        downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        //将下载请求加入下载队列，加入下载队列后会给该任务返回一个long型的id，通过该id可以取消任务，重启任务、获取下载的文件等等
        downloadId = downloadManager.enqueue(request);
        IntentFilter a = new IntentFilter(DownloadManager.ACTION_VIEW_DOWNLOADS);
        a.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        a.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED);
        //注册广播接收者，监听下载状态
        mContext.registerReceiver(receiver,
                a);
    }

    //广播监听下载的各个状态
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkStatus();
        }
    };


    //检查下载状态
    private void checkStatus() {
        Query query = new Query();
        //通过下载的id查找
        query.setFilterById(downloadId);
        Cursor c = downloadManager.query(query);

        if (c.moveToFirst()) {

            Log.i("URIsizetotal", c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))+"");
            Log.i("URIsize", c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))+"");
            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch (status) {
                 //下载暂停
                case DownloadManager.STATUS_PAUSED:
                    Log.i("URIsize--","STATUS_PAUSED");
                    break;
                //下载延迟
                case DownloadManager.STATUS_PENDING:
                    Log.i("URIsize--","STATUS_PENDING");
                    break;
                //正在下载
                case DownloadManager.STATUS_RUNNING:
//                	Log.i("URIsizetotal", c.getString(c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)));
//                	Log.i("URIsize", c.getString(c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)));
                    break;
                //下载完成
                case DownloadManager.STATUS_SUCCESSFUL:
                    //下载完成安装APK
//                    installAPK();
                	Toast.makeText(mContext, "下载完成安装", Toast.LENGTH_LONG).show();
                    try {
                        downloadManager.openDownloadedFile(downloadId);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
                //下载失败
                case DownloadManager.STATUS_FAILED:
                    Toast.makeText(mContext, "下载失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
        c.close();
    }

    //下载到本地后执行安装
    private void installAPK() {
        //获取下载文件的Uri
        Uri downloadFileUri = downloadManager.getUriForDownloadedFile(downloadId);
        if (downloadFileUri != null) {
            Intent intent= new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
            mContext.unregisterReceiver(receiver);
        }
    }
}
