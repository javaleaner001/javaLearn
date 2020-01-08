package com.fuxl.threadDemo;

public class Quote {
    public Shop shop;


    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public static Quote parse(String future){

        Shop shop = new Shop();
        shop.setName(future);
        Quote quote = new Quote();
        quote.setShop(shop);
        return  quote ;
    }
}
