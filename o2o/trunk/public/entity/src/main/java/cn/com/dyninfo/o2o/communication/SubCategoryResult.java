package cn.com.dyninfo.o2o.communication;

import cn.com.dyninfo.o2o.communication.common.BaseResult;
import cn.com.dyninfo.o2o.communication.common.PageResult;
import cn.com.dyninfo.o2o.entity.Category;
import cn.com.dyninfo.o2o.entity.CategoryTwo;

import java.util.List;

/**
 * Created by Administrator on 2016/7/25.
 */
public class SubCategoryResult extends PageResult {

    private List<CategoryTwo> categoryList;

    public List<CategoryTwo> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<CategoryTwo> categoryList) {
        this.categoryList = categoryList;
    }

    @Override
    public String toString() {
        return "SubCategoryResult{" +
                "categoryList=" + categoryList +
                '}';
    }
}
