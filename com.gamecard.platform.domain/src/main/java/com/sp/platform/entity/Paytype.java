package com.sp.platform.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created with IntelliJ IDEA.
 * User: mopdzz
 * Date: 14-8-18
 * Time: 下午10:31
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "gc_paytype")
public class Paytype extends BaseEntity{
    private String name;
    private String img;
    private String description;
    private String op;
    private Integer oi;
    private int sort;

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public Integer getOi() {
        return oi;
    }

    public void setOi(Integer oi) {
        this.oi = oi;
    }

    @Override
    public String toString() {
        return "Paytype{" +
                "name='" + name + '\'' +
                ", img='" + img + '\'' +
                '}';
    }
}
