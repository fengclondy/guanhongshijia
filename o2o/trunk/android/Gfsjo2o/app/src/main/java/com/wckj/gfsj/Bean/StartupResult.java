package com.wckj.gfsj.Bean;


import com.wckj.gfsj.Bean.Base.BaseResult;
import com.wckj.gfsj.Bean.entity.GoodsSummary;

import java.util.List;

/**
 * 启动返回类
 */
public class StartupResult extends BaseResult {

    //登录成功返回token，为空表示未登录
    private String token;

    //启动页图片，需要缓存
    private String mainPic;

    //首页商品列表
    private List<GoodsSummary> goodsList;

    public List<GoodsSummary> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<GoodsSummary> goodsList) {
        this.goodsList = goodsList;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMainPic() {
        return mainPic;
    }

    public void setMainPic(String mainPic) {
        this.mainPic = mainPic;
    }

    @Override
    public String toString() {
        return "StartupResult{" +
                "token='" + token + '\'' +
                ", mainPic='" + mainPic + '\'' +
                ", goodsList=" + goodsList +
                '}';
    }
}
