package com.sp.platform.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by yanglei on 15/6/28.
 */
@Entity
@Table(name = "black_code")
public class BlackCode {
    private Integer id;
    private String code;
    private Date ctime;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCtime() {
        return ctime;
    }

    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
