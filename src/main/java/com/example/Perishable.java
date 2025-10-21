package com.example;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public interface Perishable {


    LocalDate expirationDate();
    default boolean isExpired(){
        return this.expirationDate().isBefore(LocalDate.now());
}
}
