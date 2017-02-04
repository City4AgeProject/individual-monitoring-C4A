/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.city4age.dashboard.api.pojo.dto;

import java.util.List;

/**
 *
 * @author EMantziou
 */
public class C4AGroupsResponse {

	private String message;

	private int responseCode;

	private String careRecipientName;

	private OJDiagramFrailtyStatus frailtyStatus;

	private List<C4ServiceGetOverallScoreListResponse> ItemList;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public String getCareRecipientName() {
		return careRecipientName;
	}

	public void setCareRecipientName(String careRecipientName) {
		this.careRecipientName = careRecipientName;
	}

	public OJDiagramFrailtyStatus getFrailtyStatus() {
		return frailtyStatus;
	}

	public void setFrailtyStatus(OJDiagramFrailtyStatus frailtyStatus) {
		this.frailtyStatus = frailtyStatus;
	}

	public List<C4ServiceGetOverallScoreListResponse> getItemList() {
		return ItemList;
	}

	public void setItemList(List<C4ServiceGetOverallScoreListResponse> ItemList) {
		this.ItemList = ItemList;
	}

}// end class
