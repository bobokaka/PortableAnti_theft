package com.example.portableanti_theft.domain;

/**
 * @ProjectName: PortableAnti_theft
 * @Package: com.example.portableanti_theft.Activity
 * @ClassName: EquipmentidBean
 * @Description: java类作用描述写这里
 * @Author: ED_Peng
 * @CreateDate: 2019-4-9 0009 上午 02:53
 * @UpdateUser: 更新者
 * @UpdateDate: 2019-4-9 0009 上午 02:53
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 **/
public class EquipmentidBean {
    private String gpsid;
    private String equipmentid;
    private String gpsiime;
    private String gpslatitude;
    private String gpslongitude;

    public String getGpsid() {
        return gpsid;
    }

    public void setGpsid(String gpsid) {
        this.gpsid = gpsid;
    }

    public String getEquipmentid() {
        return equipmentid;
    }

    public void setEquipmentid(String equipmentid) {
        this.equipmentid = equipmentid;
    }

    public String getGpsiime() {
        return gpsiime;
    }

    public void setGpsiime(String gpsiime) {
        this.gpsiime = gpsiime;
    }

    public String getGpslatitude() {
        return gpslatitude;
    }

    public void setGpslatitude(String gpslatitude) {
        this.gpslatitude = gpslatitude;
    }

    public String getGpslongitude() {
        return gpslongitude;
    }

    public void setGpslongitude(String gpslongitude) {
        this.gpslongitude = gpslongitude;
    }

    @Override
    public String toString() {
        return "EquipmentidBean{" +
                "gpsid='" + gpsid + '\'' +
                ", equipmentid='" + equipmentid + '\'' +
                ", gpsiime='" + gpsiime + '\'' +
                ", gpslatitude='" + gpslatitude + '\'' +
                ", gpslongitude='" + gpslongitude + '\'' +
                '}';
    }
}
