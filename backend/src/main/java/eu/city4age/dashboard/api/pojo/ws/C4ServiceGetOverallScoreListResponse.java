/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.city4age.dashboard.api.pojo.ws;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import eu.city4age.dashboard.api.pojo.domain.FrailtyStatusTimeline;
import eu.city4age.dashboard.api.pojo.domain.GeriatricFactorValue;
import eu.city4age.dashboard.api.pojo.dto.oj.DataIdValue;
import eu.city4age.dashboard.api.pojo.dto.oj.ItemValueType;

/**
 *
 * @author EMantziou
 */
public class C4ServiceGetOverallScoreListResponse {

	private final List<C4AServiceGetGereatricFactorsResponse> items = new ArrayList<C4AServiceGetGereatricFactorsResponse>(); // 1
	private final List<Long> idList = new ArrayList<Long>();
	private final List<ItemValueType> ItemList = new ArrayList<ItemValueType>();
	private final Set<String> dateList = new TreeSet<String>();
	private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");

	private String groupName;
	private Long gefTypeId;

	private String parentGroupName;

	public C4ServiceGetOverallScoreListResponse(List<GeriatricFactorValue> gereatricfactparamsList,
			String parentGroupName, List<FrailtyStatusTimeline> fs) {

		List<DataIdValue> months = new ArrayList<DataIdValue>();

		for (GeriatricFactorValue factors : gereatricfactparamsList) {

			idList.add(factors.getId());
			String date = sdf.format(factors.getTimeInterval().getIntervalStart());
			this.gefTypeId = factors.getGefTypeId() !=null ? factors.getGefTypeId().getId() : null;

			dateList.add(date);
			months.add(new DataIdValue(factors.getTimeInterval().getId(), date));
			groupName = factors.getGefTypeId().getDetectionVariableName();

		}

		this.parentGroupName = parentGroupName;

		items.add(new C4AServiceGetGereatricFactorsResponse(groupName, idList, ItemList, dateList));

	}

	public C4ServiceGetOverallScoreListResponse() {

	}

	public C4ServiceGetOverallScoreListResponse(List<ItemValueType> itemList, List<Long> idList,
			Set<String> dateList, String groupName, String parentGroupName, Long gefTypeId) {
		this.groupName = groupName;
		this.parentGroupName = parentGroupName;
		this.gefTypeId = gefTypeId;
		items.add(new C4AServiceGetGereatricFactorsResponse(groupName, idList, itemList, dateList));
	}

	public List<C4AServiceGetGereatricFactorsResponse> getItems() {
		return items;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getParentGroupName() {
		return parentGroupName;
	}

	public void setParentGroupName(String parentGroupName) {
		this.parentGroupName = parentGroupName;
	}

	/**
	 * @return the gefTypeId
	 */
	public Long getGefTypeId() {
		return gefTypeId;
	}

	/**
	 * @param gefTypeId
	 *            the gefTypeId to set
	 */
	public void setGefTypeId(Long gefTypeId) {
		this.gefTypeId = gefTypeId;
	}

}// end class
