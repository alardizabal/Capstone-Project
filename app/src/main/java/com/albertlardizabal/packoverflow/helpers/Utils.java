package com.albertlardizabal.packoverflow.helpers;

import com.albertlardizabal.packoverflow.models.PackingList;
import com.albertlardizabal.packoverflow.models.PackingListItem;

import java.util.ArrayList;

/**
 * Created by albertlardizabal on 3/8/17.
 */

public final class Utils {

	public static ArrayList<PackingList> stageData() {

		ArrayList<PackingList> packingLists = new ArrayList<>();

		PackingListItem packingListItem = new PackingListItem();
		packingListItem.setTitle("Shirt");
		packingListItem.setSubtitle("Apples");
		packingListItem.setQuantity("1");

		PackingListItem packingListItem2 = new PackingListItem();
		packingListItem2.setTitle("Pants");
		packingListItem2.setSubtitle("Bananas");
		packingListItem2.setQuantity("2");

		PackingListItem packingListItem3 = new PackingListItem();
		packingListItem3.setTitle("Shoes");
		packingListItem3.setSubtitle("Coconuts");
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
		packingList2.setActive(true);
		packingList2.setTitle("Arizona");
		ArrayList<PackingListItem> plist2 = new ArrayList<>();
		plist2.add(packingListItem2);
		plist2.add(packingListItem3);
		plist2.add(packingListItem2);
		plist2.add(packingListItem3);
		plist2.add(packingListItem2);
		plist2.add(packingListItem3);
		plist2.add(packingListItem);
		plist2.add(packingListItem3);
		plist2.add(packingListItem2);
		plist2.add(packingListItem3);
		plist2.add(packingListItem);
		plist2.add(packingListItem3);
		plist2.add(packingListItem2);
		plist2.add(packingListItem);
		plist2.add(packingListItem2);
		plist2.add(packingListItem3);
		plist2.add(packingListItem2);
		plist2.add(packingListItem3);
		plist2.add(packingListItem2);
		plist2.add(packingListItem3);
		plist2.add(packingListItem);
		plist2.add(packingListItem3);
		plist2.add(packingListItem2);
		plist2.add(packingListItem3);
		plist2.add(packingListItem);
		plist2.add(packingListItem3);
		plist2.add(packingListItem2);
		plist2.add(packingListItem);
		packingList2.setItems(plist2);

		PackingList packingList3 = new PackingList();
		packingList3.setActive(false);
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
