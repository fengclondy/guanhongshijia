package com.wckj.gfsj.Bean;


import com.wckj.gfsj.Bean.Base.PageRequest;

/**
 * 根据商品分类查询商品列表请求类
 * request URL: https://serverurl/app/goods/listByCategory
 * request method: post
 */
public class CategoryGoodsListRequest extends PageRequest {

    private int categoryId;
    private int brandId;

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return "CategoryGoodsListRequest{" +
                "categoryId=" + categoryId +
                "brandId=" + brandId +
                '}';
    }
}
