package com.sp.platform.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mopdzz
 * Date: 14-8-18
 * Time: 下午10:33
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "gc_price")
public class Price extends BaseEntity{
    private Integer price;
    private String description;
    private List<Paytype> paytypes;
    public static final Price NULL = new Price("");

    public Price(String description) {
        this.description = description;
    }

    public Price() {
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Transient
    public List<Paytype> getPaytypes() {
        return paytypes;
    }

    public void setPaytypes(List<Paytype> paytypes) {
        this.paytypes = paytypes;
    }

    @Override
    public String toString() {
        return "Price{" +
                "price=" + price +
                ", description='" + description + '\'' +
                ", paytypes=" + paytypes +
                '}';
    }
}
