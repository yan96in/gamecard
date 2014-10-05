package com.sp.platform.vo;

/**
 * User: yangl
 * Date: 13-5-30 上午3:03
 */
public class CardVo {
    private int cardId;
    private String cardName;
    private int priceId;
    private String priceName;
    private String province;
    private int count;
    private int count4;
    private int count5;
    private int count6;
    private int count7;

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public int getPriceId() {
        return priceId;
    }

    public void setPriceId(int priceId) {
        this.priceId = priceId;
    }

    public String getPriceName() {
        return priceName;
    }

    public void setPriceName(String priceName) {
        this.priceName = priceName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount4() {
        return count4;
    }

    public void setCount4(int count4) {
        this.count4 = count4;
    }

    public int getCount5() {
        return count5;
    }

    public void setCount5(int count5) {
        this.count5 = count5;
    }

    public int getCount6() {
        return count6;
    }

    public void setCount6(int count6) {
        this.count6 = count6;
    }

    public int getCount7() {
        return count7;
    }

    public void setCount7(int count7) {
        this.count7 = count7;
    }

    @Override
    public String toString() {
        return "CardVo{" +
                "cardId=" + cardId +
                ", cardName='" + cardName + '\'' +
                ", priceId=" + priceId +
                ", priceName='" + priceName + '\'' +
                ", province='" + province + '\'' +
                ", count=" + count +
                ", count5=" + count5 +
                ", count6=" + count6 +
                ", count7=" + count7 +
                ", count4=" + count4 +
                '}';
    }
}
