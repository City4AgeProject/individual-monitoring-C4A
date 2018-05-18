/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.city4age.dashboard.api.pojo.ws;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import eu.city4age.dashboard.api.pojo.dto.oj.ItemValueType;

/**
 *
 * @author EMantziou
 */
public class C4AServiceGetGereatricFactorsResponse {

	private String groupName;
	private List<Long> idList = new ArrayList<Long>();
	private List<ItemValueType> ItemList = new ArrayList<ItemValueType>();
	private Set<String> dateList = new TreeSet<String>();

	public C4AServiceGetGereatricFactorsResponse(String groupName, List<Long> idList, List<ItemValueType> itemList,
			Set<String> dateList) {
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

	public List<ItemValueType> getItemList() {
		return ItemList;
	}

	public void setItemList(List<ItemValueType> ItemList) {
		this.ItemList = ItemList;
	}

	public Set<String> getDateList() {
		return dateList;
	}

	public void setDateList(Set<String> dateList) {
		this.dateList = dateList;
	}

}// end class
