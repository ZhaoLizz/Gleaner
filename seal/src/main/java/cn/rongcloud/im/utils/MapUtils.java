package cn.rongcloud.im.utils;

import android.content.Context;

import com.amap.api.maps2d.LocationSource;
import com.amap.api.netlocation.AMapNetworkLocationClient;
import com.orhanobut.logger.Logger;

import io.rong.imkit.plugin.location.AMapLocationActivity;

public class MapUtils {
    private AMapNetworkLocationClient mLocationClient = null;
    private LocationSource.OnLocationChangedListener mOnLocationChangedListener;

    public void setOnLocationChangedListener(LocationSource.OnLocationChangedListener onLocationChangedListener) {
        mOnLocationChangedListener = onLocationChangedListener;
    }

    public MapUtils(Context context) {
        mLocationClient = new AMapNetworkLocationClient(context);
        mLocationClient.getNetworkLocation();
        Logger.d(mLocationClient.getNetworkLocation());
    }
}
