package com.chinatelecom.pimtest.net;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import com.chinatelecom.pimtest.log.Log;
import com.chinatelecom.pimtest.utils.StringUtils;

import org.apache.http.conn.util.InetAddressUtils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;



/**
 * @author lichao
 * @since 20/01/2015
 * desc:
 */
public class Connection {
    private static final Log logger = Log.build(Connection.class);

    private static final Uri PREFER_APN = Uri.parse("content://telephony/carriers/preferapn");


    private static final String CTWAP_USER = "ctwap@mycdma.cn";

    private static final String CTNET_USER = "ctnet@mycdma.cn";

    private String CTWAP = "ctwap";
    private String CTNET = "ctnet";

    private static final String KEY_USER = "user";

    private static final String LOG_TAG = Connection.class.getName();

    private static Connection instance;

    private Context context;

    /**
     * 连接类型
     */
    enum Type {
        NONE,
        WIFI,
        CTWAP,
        CTNET,
        CMWAP,
        CMNET,
        OTHER,
        GNET,
        GWAP,
        CTLTE
    }

    /**
     * @return 是否打开WIFI
     */
    public boolean isWIFI() {
        return getType() == Type.WIFI;
    }

    /**
     * @return 是否CTWAP
     */
    public boolean isCtwap() {
        return getType() == Type.CTWAP;
    }

    /**
     * @return 是否CMWAP
     */
    public boolean isCmwap() {
        return getType() == Type.CMWAP;
    }

    public String getTypeString() {
        Type type = getType();
        if (type == null) {
            return "";
        }
        return type.toString();
    }

    /**
     * @return 是否已经连接
     */
    public boolean isConnected() {
//        return getType() != Type.NONE;
        return checkConnection(context);
    }

    public static boolean isHTCApnDevice = false;

    public static void initialize(Context context) {
        if (instance == null) {
            instance = new Connection(context);
            //todo 暂时注销了 特定机型的设置
            /*isHTCApnDevice = StringUtils.equals(Device.getCurrent().getModel(), "HTC Z510d") ||
                    StringUtils.equals(Device.getCurrent().getModel(), "HTC X515d");*/
        }
    }

    private Connection(final Context context) {
        this.context = context;
    }

    public static Connection getInstance(Context context) {
        if (instance == null) {
            initialize(context);
        }
        return instance;
    }

    //判断是否有可用网络
    public static boolean checkConnection(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (mConnectivityManager == null) {
                return false;
            }
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isConnected() && mNetworkInfo.isAvailable()) {
                return true;
            }
        }

        return false;
    }

    /**
     * 判断当前连接是否在移动网络下
     * @param context
     * @return
     */
    public static boolean isMobileNow(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkINfo = cm.getActiveNetworkInfo();
        if (networkINfo != null && networkINfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            return true;
        }
        return false;
    }

    /**
     * 判断当前连接是否在wifi下
     * @param context
     * @return
     */
    public static boolean isWifiNow(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkINfo = cm.getActiveNetworkInfo();
        if (networkINfo != null && networkINfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }


    private Type getType() {
        if (context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager == null) {
                return Type.NONE;
            }
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            boolean connected = (networkInfo != null && networkInfo.getState() == NetworkInfo.State.CONNECTED
                    && networkInfo.isAvailable());
            if (connected) {
                if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    return Type.WIFI;
                } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {


                /*
                * HTC z510D 移动网络设置的VPN和系统设置的会冲突
                * 判断方法为判断IP地址为10.开头的为CTWAP
                */
                    if (isHTCApnDevice) {
                        InetAddress inetAddress = getLocalInetAddress();
                        if (inetAddress != null && inetAddress.getHostAddress() != null) {
                            logger.debug("current HTC ip Address:%s", inetAddress.getHostAddress().toString());
                            if (StringUtils.startsWith(inetAddress.getHostAddress().toString(), "10.")) {
                                return Type.CTWAP;
                            } else {
                                return Type.CTNET;
                            }
                        }
                    } else if (StringUtils.equalsIgnoreCase(networkInfo.getExtraInfo(), "ctwap") &&
                            (StringUtils.equals(networkInfo.getReason(), "dataEnabled") || StringUtils.equals(networkInfo.getReason(), "connected")
                                    //todo
                                /*|| Device.connectionCTWAP()*/)) {
                        return Type.CTWAP;
                    } else if (StringUtils.equalsIgnoreCase(networkInfo.getExtraInfo(), "ctnet") &&
                            (StringUtils.equals(networkInfo.getReason(), "dataEnabled") || StringUtils.equals(networkInfo.getReason(), "connected")
                                    //todo
                                /*|| Device.connectionCTWAP()*/)) {
                        return Type.CTNET;
                    } else if (StringUtils.equals(networkInfo.getExtraInfo(), "ctlte") || StringUtils.equals(networkInfo.getExtraInfo(), "connected")) {
                        return Type.CTLTE;
                    } else if (StringUtils.equals(networkInfo.getExtraInfo(), "cmwap") || StringUtils.equals(networkInfo.getExtraInfo(), "cmwap:GSM")) {
                        return Type.CMWAP;
                    } else if (StringUtils.equals(networkInfo.getExtraInfo(), "cmnet") || StringUtils.equals(networkInfo.getExtraInfo(), "cmnet:GSM")) {
                        return Type.CMNET;
                    } else if (StringUtils.equals(networkInfo.getExtraInfo(), "3gnet")) {
                        return Type.GNET;
                    } else if (StringUtils.equals(networkInfo.getExtraInfo(), "3gwap")) {
                        return Type.GWAP;
                    } else if ((networkInfo.getExtraInfo() != null && networkInfo.getExtraInfo().indexOf("GSM") == -1) ||
                            networkInfo.getExtraInfo() == null) { //2G,3G网络

                        return getApnTypeCT(context);
                    }
                }
            } else {
                // 注意一：
                // NetworkInfo 为空或者不可以用的时候正常情况应该是当前没有可用网络，
                // 但是有些电信机器，仍可以正常联网，
                // 所以当成net网络处理依然尝试连接网络。
                // （然后在socket中捕捉异常，进行二次判断与用户提示）。
                return Type.NONE;
            }
        }
        return Type.NONE;
    }

    /**
     * 获得当前的apn
     * 开发中，都会遇到手机网络类型判断，因为就目前的android平台手机来说：可能会存在4中状态
     * 1.无网络（这种状态可能是因为手机停机，网络没有开启，信号不好等原因）
     * 2.使用WIFI上网
     * 3.CMWAP（中国移动代理）
     * 4.CMNET上网
     * 这四种状态，如果没有网络，肯定是无法请求Internet了，如果是wap就需要为手机添加中国移动代理，关于为手机添加中国移动的代理，请到
     * 下面是网络判断的方法：
     *
     * @return Type
     */
    public Type getApnTypeCM(Context context) {
        Type netType = Type.NONE;
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            logger.debug("networkInfo.getExtraInfo() is %s", networkInfo.getExtraInfo());

            if (networkInfo.getExtraInfo().toLowerCase().equals("cmnet")) {
                netType = Type.CMNET;
            } else {
                netType = Type.CMWAP;
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = Type.WIFI;
        }
        return netType;
    }

    private Type getApnTypeCT(Context context) {
        Type apntype = Type.NONE;
        Cursor cursor;
        try {
            cursor = context.getContentResolver().query(PREFER_APN, null,
                    null, null, null);
            cursor.moveToFirst();
            String user = cursor.getString(cursor.getColumnIndex("apn"));
            logger.debug("chinatelecom network APN Type : %s", user);
            if (user.startsWith(CTNET)) {
                apntype = Type.CTNET;
            } else if (user.startsWith(CTWAP)) {
                apntype = Type.CTWAP;
            }

            if (cursor != null)
                cursor.close();

            return apntype;
        } catch (Exception e) {

            e.printStackTrace();
            return Type.NONE;
        }
    }

    /**
     * 获得当前的apn
     *
     * @return Type
     */
    //todo  我用getApnTypeCT(context)方法代替了此方法
    /*private Type getApnType() {
        ContentResolver resolver = context.getContentResolver();
        try {
            final Type[] type = new Type[1];
            CursorTemplate.one(resolver.query(PREFER_APN, null, null, null, null),
                    new Closure<Cursor>() {
                        @Override
                        public void execute(Cursor cursor) {
                            String user = CursorUtils.getString(cursor, KEY_USER);
                            if (StringUtils.equalsIgnoreCase(user, CTWAP_USER)) {
                                type[0] = Type.CTWAP;
                            } else if (StringUtils.equalsIgnoreCase(user, CTNET_USER)) {
                                type[0] = Type.CTNET;
                            } else {
                                type[0] = Type.NONE;
                            }
                        }
                    });
            return type[0];
        } catch (Exception e) {
            e.printStackTrace();
            return Type.NONE;
        }
    }*/
    private InetAddress getLocalInetAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address)) {
                        return inetAddress;
                    }
                }
            }
        } catch (SocketException ex) {
            logger.debug("%s", ex.toString());
        }
        return null;
    }

    /**
     * 获取手机ip地址
     *
     * @return
     */
    public static String getPhoneIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        // if (!inetAddress.isLoopbackAddress() && inetAddress
                        // instanceof Inet6Address) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception e) {
        }
        return "";
    }

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && InetAddressUtils.isIPv4Address(inetAddress.getHostAddress())) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    //判断WIFI网络是否可用
    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            if (mConnectivityManager == null) {
                return false;
            }

            NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }

        return false;
    }

    //判断MOBILE网络是否可用
    public static boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            if (mConnectivityManager == null) {
                return false;
            }

            NetworkInfo mMobileNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable();
            }
        }

        return false;
    }

    //获取当前网络连接的类型信息
    public static int getConnectedType(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            if (mConnectivityManager == null) {
                return -1;
            }

            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();

            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                return mNetworkInfo.getType();
            }
        }

        return -1;
    }
}
