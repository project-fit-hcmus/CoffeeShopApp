package com.example.myjavaapp.View.Interfaces;

import com.example.myjavaapp.Model.entity.BeverageAndCartDetail;

public interface CartItemClickListener {
    void onCartItemClick(Integer position, String id, String beverage, Integer number, BeverageAndCartDetail item);
}
