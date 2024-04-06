package com.jlu.takeout.service;

import com.jlu.takeout.dto.ShoppingCartDTO;
import com.jlu.takeout.entity.ShoppingCart;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ShoppingCartService {
    /**
     * 添加购物车
     * @param shoppingCartDTO
     */
    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);

    List<ShoppingCart> showShoppingCart();

    void clean();

    void deleteOne(ShoppingCartDTO shoppingCartDTO);
}
