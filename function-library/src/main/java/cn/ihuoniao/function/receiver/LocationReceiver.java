package cn.ihuoniao.function.receiver;

import android.app.Activity;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.mistyrain.http.HttpUtility;
import com.mistyrain.http.callback.StringCallback;
import com.mistyrain.http.config.HttpMethod;

import java.util.HashMap;
import java.util.Map;

import cn.ihuoniao.function.command.base.Receiver;
import cn.ihuoniao.function.constant.API;
import cn.ihuoniao.function.util.Logger;
import cn.ihuoniao.function.util.SPUtils;

/**
 * Created by sdk-app-shy on 2017/6/9.
 */

public class LocationReceiver extends Receiver {

    private String passport = "";
    private double oldLat = 0;
    private double oldLon = 0;
    private LocationClient mLocationClient = null;
    private BDLocationListener myListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            try {
                //纬度
                double lat = location.getLatitude();
                //经度
                double lon = location.getLongitude();
                LatLng oldLatLng = new LatLng(oldLat, oldLon);
                LatLng latLng = new LatLng(lat, lon);
                double distance = DistanceUtil.getDistance(oldLatLng, latLng);
                String url = API.HTTP + API.JINDIAN_IP + API.JINDIAN_LOCATION;
                Logger.i("lat : " + lat + "  lon : " + lon + " distance : " + distance);
                //距离大于10米上报服务器
                if (distance > 10) {
                    Map<String, String> params = new HashMap<>();
                    params.put("uid", passport);
                    params.put("lng", String.valueOf(lon));
                    params.put("lat", String.valueOf(lat));
                    params.put("service", "waimai");
                    params.put("action", "updateCourierLocation");
                    HttpUtility.getInstance().execute(HttpMethod.POST, url, params, new StringCallback() {
                        @Override
                        public void onResponse(String s) {
                            Logger.i("location request : " + s);
                        }
                    });
                }
                oldLat = lat;
                oldLon = lon;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    };

    private static LocationReceiver receiver = null;

    private LocationReceiver() {
    }

    public static synchronized LocationReceiver getDefault() {
        if (receiver == null) {
            receiver = new LocationReceiver();
        }
        return receiver;
    }

    public void init(Activity activity, int second) {
        mLocationClient = new LocationClient(activity.getApplicationContext());
        mLocationClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setScanSpan(second);
        option.setOpenGps(true);
        option.setLocationNotify(true);
        mLocationClient.setLocOption(option);
    }

    public void uploadLocation() {
        String passport = SPUtils.getString("locationPassport");
        if (null != passport && passport.trim().length() > 0) {
            this.passport = passport;
            mLocationClient.start();
        }
    }

    public void stopLocation() {
        mLocationClient.stop();
    }
}
