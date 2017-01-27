/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.city4age.dashboard.api.pojo.dto;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author EMantziou
 */
public class C4AServiceGetGereatricFactorsResponse {

	private String groupName;
	private List<Long> idList = new ArrayList<Long>();
	private List<Float> ItemList = new ArrayList<Float>();
	private List<String> dateList = new ArrayList<String>();

	public C4AServiceGetGereatricFactorsResponse(String groupName, List<Long> idList, List<Float> itemList,
			List<String> dateList) {
		this.groupName = groupName;
		this.idList = idList;
		this.ItemList = itemList;
		this.dateList = dateList;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public List<Long> getIdList() {
		return idList;
	}

	public void setIdList(List<Long> idList) {
		this.idList = idList;
	}

	public List<Float> getItemList() {
		return ItemList;
	}

	public void setItemList(List<Float> ItemList) {
		this.ItemList = ItemList;
	}

	public List<String> getDateList() {
		return dateList;
	}

	public void setDateList(List<String> dateList) {
		this.dateList = dateList;
	}

}// end class
