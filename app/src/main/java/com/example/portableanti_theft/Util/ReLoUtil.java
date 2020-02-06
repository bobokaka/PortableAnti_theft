package com.example.portableanti_theft.Util;

import android.text.TextUtils;
import android.util.Log;

import java.security.MessageDigest;

/**
 * @ProjectName: PortableAnti_theft
 * @Package: com.example.portableanti_theft.Util
 * @ClassName: ReLoUtil
 * @Description: 登录、注册的共用工具方法
 * @Author: ED_Peng
 * @CreateDate: 2019-3-28 0028 上午 04:08
 * @UpdateUser: 更新者
 * @UpdateDate: 2019-3-28 0028 上午 04:08
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 **/
public class ReLoUtil {
    private String rephonenumber;
    private String repassword;
    private static final String TAG = "ReLoUtil";
    public  ReLoUtil(){

    }
    public ReLoUtil(String rephonenumber, String repassword) {
        this.rephonenumber = rephonenumber;
        this.repassword = repassword;
    }

    public String getRephonenumber() {
        return rephonenumber;
    }

    public void setRephonenumber(String rephonenumber) {
        this.rephonenumber = rephonenumber;
    }

    public String getRepassword() {
        return repassword;
    }

    public void setRepassword(String repassword) {
        this.repassword = repassword;
    }

    /**
     * @param mobileNums
     * @return
     */
    public static boolean isMobileNO(String mobileNums) {
        /**
         * 判断字符串是否符合手机号码格式
         * 移动号段: 134,135,136,137,138,139,147,150,151,152,157,158,159,170,178,182,183,184,187,188
         * 联通号段: 130,131,132,145,155,156,170,171,175,176,185,186
         * 电信号段: 133,149,153,170,173,177,180,181,189
         * @param str
         * @return 待检测的字符串
         */
        // "[1]"代表下一位为数字可以是几，"[0-9]"代表可以为0-9中的一个
        // "[5,7,9]"表示可以是5,7,9中的任意一位,[^4]表示除4以外的任何一个
        // \\d{9}"代表后面是可以是0～9的数字，有9位。
        String telRegex = "^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$";
        if (TextUtils.isEmpty(mobileNums))
            return false;
        else
            return mobileNums.matches(telRegex);
    }

    /***
     * MD5加码 生成32位md5码
     */
    public static String string2MD5(String inStr) {
        Log.d(TAG, "string2MD5: -------------------------");
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();

    }

    /**
     * 加密解密算法 执行一次加密，两次解密
     */
    public static String convertMD5(String inStr) {
        Log.d(TAG, "convertMD5: -------------------------");
        char[] a = inStr.toCharArray();
        for (int i = 0; i < a.length; i++) {
            a[i] = (char) (a[i] ^ 't');
        }
        String s = new String(a);
        return s;

    }

}
