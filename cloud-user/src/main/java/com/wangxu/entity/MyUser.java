package com.wangxu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("my_user")
public class MyUser {
  //@TableId(value = "uid",type = IdType.NONE)
  @TableId(value = "uid",type = IdType.UUID)
  private String uid;
  private String mobile;
  private String passwd;
  private String name;
  private String sex;
  private String birthday;
  private String updatedTime;


  public String getUid() {
    return uid;
  }

  public void setUid(String uid) {
    this.uid = uid;
  }


  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }


  public String getPasswd() {
    return passwd;
  }

  public void setPasswd(String passwd) {
    this.passwd = passwd;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  public String getSex() {
    return sex;
  }

  public void setSex(String sex) {
    this.sex = sex;
  }


  public String getBirthday() {
    return birthday;
  }

  public void setBirthday(String birthday) {
    this.birthday = birthday;
  }


  public String getUpdatedTime() {
    return updatedTime;
  }

  public void setUpdatedTime(String updatedTime) {
    this.updatedTime = updatedTime;
  }

}
