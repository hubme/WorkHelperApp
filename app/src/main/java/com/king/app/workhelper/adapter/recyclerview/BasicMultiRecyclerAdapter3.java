package com.king.app.workhelper.adapter.recyclerview;

import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.king.app.workhelper.adapter.recyclerview.delegate.CategoryDelegate;
import com.king.app.workhelper.adapter.recyclerview.delegate.ContentDelegate;
import com.king.app.workhelper.adapter.recyclerview.delegate.UnknownDelegate;
import com.king.app.workhelper.model.entity.Advertisement;
import com.king.app.workhelper.model.entity.Cat;
import com.king.app.workhelper.model.entity.DisplayableItem;
import com.king.app.workhelper.model.entity.Dog;
import com.king.app.workhelper.model.entity.Gecko;
import com.king.app.workhelper.model.entity.Snake;
import com.king.app.workhelper.model.entity.StringEntity;
import com.king.applib.ui.recyclerview.multiitemdelegate.AdapterDelegatesManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author VanceKing
 * @since 2017/6/29.
 */

public class BasicMultiRecyclerAdapter3 extends RecyclerView.Adapter {

    private final List<StringEntity> mItems;
    private final AdapterDelegatesManager<List<StringEntity>> mDelegatesManager;

    public BasicMultiRecyclerAdapter3(List<StringEntity> items) {
        mItems = items;

        mDelegatesManager = new AdapterDelegatesManager<>();
        mDelegatesManager.addDelegate(new CategoryDelegate())
                .addDelegate(new ContentDelegate())
                .addDelegate(new UnknownDelegate());
    }


    @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return mDelegatesManager.onCreateViewHolder(parent, viewType);
    }

    @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        mDelegatesManager.onBindViewHolder(mItems, position, holder);
    }

    @Override public int getItemCount() {
        return mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mDelegatesManager.getItemViewType(mItems, position);
    }

    public static List<StringEntity> fakeMultiTypeData() {
        List<StringEntity> data = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            if (i % 5 == 0) {
                data.add(new StringEntity("category" + i, StringEntity.ItemType.CATEGORY));
            } else {
                data.add(new StringEntity("item " + i, StringEntity.ItemType.CONTENT));
            }
        }
        for (int i = 0; i < 6; i++) {
            data.add(new StringEntity("", StringEntity.ItemType.UNKNOWN));
        }

        Collections.shuffle(data);
        return data;
    }

    private List<DisplayableItem> getAnimals() {
        List<DisplayableItem> animals = new ArrayList<>();

        animals.add(new Cat("American Curl"));
        animals.add(new Cat("Baliness"));
        animals.add(new Cat("Bengal"));
        animals.add(new Cat("Corat"));
        animals.add(new Cat("Manx"));
        animals.add(new Cat("Nebelung"));
        animals.add(new Dog("Aidi"));
        animals.add(new Dog("Chinook"));
        animals.add(new Dog("Appenzeller"));
        animals.add(new Dog("Collie"));
        animals.add(new Snake("Mub Adder", "Adder"));
        animals.add(new Snake("Texas Blind Snake", "Blind snake"));
        animals.add(new Snake("Tree Boa", "Boa"));
        animals.add(new Gecko("Fat-tailed", "Hemitheconyx"));
        animals.add(new Gecko("Stenodactylus", "Dune Gecko"));
        animals.add(new Gecko("Leopard Gecko", "Eublepharis"));
        animals.add(new Gecko("Madagascar Gecko", "Phelsuma"));
        animals.add(new Advertisement());
        animals.add(new Advertisement());
        animals.add(new Advertisement());
        animals.add(new Advertisement());
        animals.add(new Advertisement());

        //随机打乱
        Collections.shuffle(animals);
        return animals;
    }
}
