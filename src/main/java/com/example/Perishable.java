package com.example;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public interface Perishable {
    List<Perishable> expiredList = new ArrayList<>();


    LocalDate expirationDate();
    default void isExpired(){
        if(this.expirationDate().isBefore(LocalDate.now())){
            expiredList.add(this);
        }
}
}
