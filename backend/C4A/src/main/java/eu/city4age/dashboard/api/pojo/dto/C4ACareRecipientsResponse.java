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
public class C4ACareRecipientsResponse {

	private String message;

	private int responseCode;

	private List<C4ACareRecipientListResponse> ItemList;

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

	public List<C4ACareRecipientListResponse> getItemList() {
		return ItemList;
	}

	public void setItemList(List<C4ACareRecipientListResponse> ItemList) {
		this.ItemList = ItemList;
	}

}// end clas
