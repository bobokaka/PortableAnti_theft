package com.example.portableanti_theft.domain;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.LitePalSupport;

/**
 * @ProjectName: PortableAnti_theft
 * @Package: com.example.portableanti_theft.domain
 * @ClassName: LoginBean
 * @Description: java类作用描述写这里
 * @Author: ED_Peng
 * @CreateDate: 2019-3-25 0025 下午 06:46
 * @UpdateUser: 更新者
 * @UpdateDate: 2019-3-25 0025 下午 06:46
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 **/
public class LoginBean extends LitePalSupport implements Parcelable {
    private String userid;

    private String userphonenumber;

    private String username;

    private String userpassword;

    private String userqq;

    private String userweixin;
    private String userphotopath;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userid);
        dest.writeString(userphonenumber);
        dest.writeString(username);
        dest.writeString(userpassword);
        dest.writeString(userqq);
        dest.writeString(userweixin);
        dest.writeString(userphotopath);

    }


    public static final Parcelable.Creator<LoginBean> CREATOR =
            new Parcelable.Creator<LoginBean>() {

                @Override
                public LoginBean createFromParcel(Parcel parcel) {
                    LoginBean loginBean = new LoginBean();
                    loginBean.userid = parcel.readString();
                    loginBean.userphonenumber = parcel.readString();
                    loginBean.username = parcel.readString();
                    loginBean.userpassword = parcel.readString();
                    loginBean.userqq = parcel.readString();
                    loginBean.userweixin = parcel.readString();
                    loginBean.userphotopath = parcel.readString();
                    return loginBean;
                }

                @Override
                public LoginBean[] newArray(int size) {
                    return new LoginBean[size];
                }
            };

    @Override
    public String toString() {
        return "LoginBean{" +
                "userid='" + userid + '\'' +
                ", userphonenumber='" + userphonenumber + '\'' +
                ", username='" + username + '\'' +
                ", userpassword='" + userpassword + '\'' +
                ", userqq='" + userqq + '\'' +
                ", userweixin='" + userweixin + '\'' +
                ", userphotopath='" + userphotopath + '\'' +
                '}';
    }

    public String getUserphotopath() {
        return userphotopath;
    }

    public void setUserphotopath(String userphotopath) {
        this.userphotopath = userphotopath;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUserphonenumber() {
        return userphonenumber;
    }

    public void setUserphonenumber(String userphonenumber) {
        this.userphonenumber = userphonenumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserpassword() {
        return userpassword;
    }

    public void setUserpassword(String userpassword) {
        this.userpassword = userpassword;
    }

    public String getUserqq() {
        return userqq;
    }

    public void setUserqq(String userqq) {
        this.userqq = userqq;
    }

    public String getUserweixin() {
        return userweixin;
    }

    public void setUserweixin(String userweixin) {
        this.userweixin = userweixin;
    }
}
