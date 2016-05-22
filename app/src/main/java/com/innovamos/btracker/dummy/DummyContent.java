package com.innovamos.btracker.dummy;

import com.innovamos.btracker.dto.ProductDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static List<ProductDTO> ITEMS = new ArrayList<ProductDTO>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, ProductDTO> ITEM_MAP = new HashMap<String, ProductDTO>();

    static {
        // Add 3 sample items.
//        addItem(new DummyItem("1", "Item 1"));
//        addItem(new DummyItem("2", "Item 2"));
//        addItem(new DummyItem("3", "Item 3"));
    }

    private static void addItem(ProductDTO item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.getId(), item);
    }

}
