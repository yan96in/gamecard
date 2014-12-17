package com.sp.platform.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mopdzz
 * Date: 14-8-18
 * Time: 下午10:18
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "gc_card")
public class Card extends BaseEntity {
    private String name;
    private String description;
    private List<Price> prices;
    public static final Card NULL = new Card("");

    public Card(String name) {
        this.name = name;
    }

    public Card() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Transient
    public List<Price> getPrices() {
        return prices;
    }

    public void setPrices(List<Price> prices) {
        this.prices = prices;
    }

    @Override
    public String toString() {
        return "Card{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", prices=" + prices +
                '}';
    }
}
