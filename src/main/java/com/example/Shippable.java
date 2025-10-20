package com.example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public interface Shippable {


    BigDecimal calculateShippingCost();
    double weight();

}
