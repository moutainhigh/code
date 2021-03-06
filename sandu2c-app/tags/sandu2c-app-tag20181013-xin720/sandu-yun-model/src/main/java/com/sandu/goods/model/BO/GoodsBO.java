package com.sandu.goods.model.BO;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class GoodsBO implements Serializable
{
    private Integer id;

    private String pic;

    private String goodsName;

    private String prices;
    /**
     * 是否预售新品
     */
    private Integer isPresell;
    /**
     * 是否特卖商品
     */
    private Integer isSpecialOffer;
}
