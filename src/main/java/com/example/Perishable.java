package com.example;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public interface Perishable {


    LocalDate expirationDate();
    default void isExpired(){
        if(this.expirationDate().isBefore(LocalDate.now())){
            FoodProduct.expiredList.add(this);
        }
}
}
