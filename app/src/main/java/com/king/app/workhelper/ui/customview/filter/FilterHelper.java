package com.king.app.workhelper.ui.customview.filter;

import android.text.TextUtils;

import com.king.applib.util.JsonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FilterHelper {

    //保存筛选条件的信息
    private final Map<String, FilterGroupModel> mFilterGroups = new HashMap<>();
    //保存选中的筛选条件
    private final FilterConditionModel mCheckedCondition = new FilterConditionModel();

    //每个属性（过滤条件）只显示一条，需要过滤。使用 Set 保存。
    private final Set<FilterGroupModel.FilterItem> mBrandsSet = new LinkedHashSet<>();
    private final Set<FilterGroupModel.FilterItem> mModelsSet = new LinkedHashSet<>();

    private static final String ddd = "[{\"brand\":\"Apple\",\"model\":\"iPhone 13\"},{\"brand\":\"HUAWEI\",\"model\":\"P50\"},{\"brand\":\"HUAWEI\",\"model\":\"Mate 40\"},{\"brand\":\"HUAWEI\",\"model\":\"Mate 30\"},{\"brand\":\"Apple\",\"model\":\"iPhone 10\"},{\"brand\":\"HUAWEI\",\"model\":\"nova 8\"},{\"brand\":\"Apple\",\"model\":\"iPhone 8\"},{\"brand\":\"HUAWEI\",\"model\":\"10 e\"},{\"brand\":\"Apple\",\"model\":\"iPhone 7\"},{\"brand\":\"Apple\",\"model\":\"iPhone 6\"},{\"brand\":\"Apple\",\"model\":\"iPhone 5\"},{\"brand\":\"Apple\",\"model\":\"iPhone 7 Pro\"},{\"brand\":\"HUAWEI\",\"model\":\"Mate 40 Lite\"},{\"brand\":\"Apple\",\"model\":\"iPhone 7\"},{\"brand\":\"HUAWEI\",\"model\":\"Nova 8\"},{\"brand\":\"Apple\",\"model\":\"iPhone 11 pro\"},{\"brand\":\"HUAWEI\",\"model\":\"10e Pro\"},{\"brand\":\"Apple\",\"model\":\"iPhone 11 PRO MAX\"},{\"brand\":\"Apple\",\"model\":\"iPhone X\"}]";

    public FilterConditionModel getFilterConditionModel() {
        return mCheckedCondition;
    }

    public Map<String, FilterGroupModel> getFilterGroups() {
        return mFilterGroups;
    }

    public FilterHelper() {
        mFilterGroups.put(FilterView.BRAND, new FilterGroupModel(FilterView.BRAND, false, new ArrayList<>()));
        mFilterGroups.put(FilterView.MODEL, new FilterGroupModel(FilterView.MODEL, false, new ArrayList<>()));
    }

    public static List<FilterConditionModel> fakeData() {
        return JsonUtil.decodeToList(ddd, FilterConditionModel.class);
    }

    public List<FilterGroupModel> transformConditions(List<FilterConditionModel> conditions) {
        clearSet();
        for (FilterConditionModel condition : conditions) {
            if (isFilterMatched(condition)) {
                addCondition(condition);
            }
        }

        List<FilterGroupModel.FilterItem> brands = mFilterGroups.get(FilterView.BRAND).getConditions();
        brands.clear();
        brands.addAll(mBrandsSet);

        List<FilterGroupModel.FilterItem> models = mFilterGroups.get(FilterView.MODEL).getConditions();
        models.clear();
        models.addAll(mModelsSet);

        return new ArrayList<>(mFilterGroups.values());
    }

    private void clearSet() {
        mBrandsSet.clear();
        mModelsSet.clear();
    }

    //比较所有条件不为 null 的条件。如果有一条不匹配则不添加此筛选项，所有条件都匹配才添加。
    @SuppressWarnings("All")
    private boolean isFilterMatched(FilterConditionModel condition) {
        if (mCheckedCondition.getBrand() != null) {
            if (!mCheckedCondition.getBrand().equals(condition.getBrand())) {
                return false;
            }
        }
        if (mCheckedCondition.getModel() != null) {
            if (!mCheckedCondition.getModel().equals(condition.getModel())) {
                return false;
            }
        }
        return true;
    }

    private void addCondition(FilterConditionModel condition) {
        //商品的某一个属性可能为 null，需判断
        String brand = condition.getBrand();
        if (!TextUtils.isEmpty(brand)) {
            mBrandsSet.add(new FilterGroupModel.FilterItem(brand, brand.equals(mCheckedCondition.getBrand())));
        }
        String model = condition.getModel();
        if (!TextUtils.isEmpty(model)) {
            mModelsSet.add(new FilterGroupModel.FilterItem(model, model.equals(mCheckedCondition.getModel())));
        }
    }
}
