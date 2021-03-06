package com.albertlardizabal.packoverflow.helpers;

import com.albertlardizabal.packoverflow.models.PackingList;
import com.albertlardizabal.packoverflow.models.PackingListItem;

import java.util.ArrayList;

/**
 * Created by albertlardizabal on 3/8/17.
 */

public final class Utils {

	// Dummy data
	public static ArrayList<PackingList> stageData() {

		ArrayList<PackingList> packingLists = new ArrayList<>();

		PackingListItem packingListItem = new PackingListItem();
		packingListItem.setTitle("Shirt");
		packingListItem.setSubtitle("Polo");
		packingListItem.setQuantity("1");

		PackingListItem packingListItem2 = new PackingListItem();
		packingListItem2.setTitle("Pants");
		packingListItem2.setSubtitle("Formal");
		packingListItem2.setQuantity("2");

		PackingListItem packingListItem3 = new PackingListItem();
		packingListItem3.setTitle("Shoes");
		packingListItem3.setSubtitle("Sneakers");
		packingListItem3.setQuantity("3");

		PackingList packingList = new PackingList();
		packingList.setActive(false);
		packingList.setTitle("Overnight");
		ArrayList<PackingListItem> plist = new ArrayList<>();
		plist.add(packingListItem);
		plist.add(packingListItem2);
		plist.add(packingListItem3);

		packingList.setItems(plist);

		PackingList packingList2 = new PackingList();
		packingList2.setActive(false);
		packingList2.setTitle("Arizona");
		ArrayList<PackingListItem> plist2 = new ArrayList<>();
		plist2.add(packingListItem2);
		plist2.add(packingListItem3);
		plist2.add(packingListItem2);

		packingList2.setItems(plist2);

		PackingList packingList3 = new PackingList();
		packingList3.setActive(true);
		packingList3.setTitle("Beach");
		ArrayList<PackingListItem> plist3 = new ArrayList<>();
		plist3.add(packingListItem);
		plist3.add(packingListItem2);
		plist3.add(packingListItem3);

		packingList3.setItems(plist3);

		packingLists.add(packingList);
		packingLists.add(packingList2);
		packingLists.add(packingList3);

		return packingLists;
	}
}
