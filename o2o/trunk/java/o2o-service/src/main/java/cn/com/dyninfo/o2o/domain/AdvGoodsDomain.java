package cn.com.dyninfo.o2o.domain;

import cn.com.dyninfo.o2o.model.AdvGoodsModel;

/**
 * Created by Administrator on 2016/7/19.
 */
public class AdvGoodsDomain extends AdvGoodsModel {
    private GoodsDomain goodsDomain;
    private AreaInfoDomain areaInfoDomain;
    private AAdvInfoDomain aAdvInfoDomain;

    public GoodsDomain getGoodsDomain() {
        return goodsDomain;
    }

    public void setGoodsDomain(GoodsDomain goodsDomain) {
        this.goodsDomain = goodsDomain;
    }

    public AreaInfoDomain getAreaInfoDomain() {
        return areaInfoDomain;
    }

    public void setAreaInfoDomain(AreaInfoDomain areaInfoDomain) {
        this.areaInfoDomain = areaInfoDomain;
    }

    public AAdvInfoDomain getaAdvInfoDomain() {
        return aAdvInfoDomain;
    }

    public void setaAdvInfoDomain(AAdvInfoDomain aAdvInfoDomain) {
        this.aAdvInfoDomain = aAdvInfoDomain;
    }
}