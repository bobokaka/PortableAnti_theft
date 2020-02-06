package com.example.portableanti_theft.domain;

/**
 * @ProjectName: PortableAnti_theft
 * @Package: com.example.portableanti_theft.domain
 * @ClassName: SBBean
 * @Description: java类作用描述写这里
 * @Author: ED_Peng
 * @CreateDate: 2019-4-8 0008 下午 07:57
 * @UpdateUser: 更新者
 * @UpdateDate: 2019-4-8 0008 下午 07:57
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 **/
public class SBBean {
    private  String equipmentid;
    private  String userid;
    private  String equipmentname;

    public String getEquipmentid() {
        return equipmentid;
    }

    public void setEquipmentid(String equipmentid) {
        this.equipmentid = equipmentid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getEquipmentname() {
        return equipmentname;
    }

    public void setEquipmentname(String equipmentname) {
        this.equipmentname = equipmentname;
    }

    @Override
    public String toString() {
        return "SBBean{" +
                "equipmentid='" + equipmentid + '\'' +
                ", userid='" + userid + '\'' +
                ", equipmentname='" + equipmentname + '\'' +
                '}';
    }
}
