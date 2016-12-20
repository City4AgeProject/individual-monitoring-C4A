/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.city4age.dashboard.api.external;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import eu.city4age.dashboard.api.domain.DataIdValue;
import eu.city4age.dashboard.api.model.FrailtyStatusTimeline;
import eu.city4age.dashboard.api.model.GeriatricFactorValue;

/**
 *
 * @author EMantziou
 */
public class C4ServiceGetOverallScoreListResponse {

	private final FrailtyStatusDTO frailtyStatus;

    private final List<C4AServiceGetGereatricFactorsResponse> items = new ArrayList<C4AServiceGetGereatricFactorsResponse>();
    private final List<Long> idList = new ArrayList<Long>();
    private final List<Float> ItemList = new ArrayList<Float>();
    private final List<String> dateList = new ArrayList<String>();
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");

    private String groupName;

    private String parentGroupName;
    
    public C4ServiceGetOverallScoreListResponse(List<GeriatricFactorValue> gereatricfactparamsList, String parentGroupName, List<FrailtyStatusTimeline> fs) {

    	List<DataIdValue> months = new ArrayList<DataIdValue>();
    	
    	for (GeriatricFactorValue factors : gereatricfactparamsList) {
	        
	         ItemList.add(factors.getGefValue().floatValue());
	         idList.add(factors.getId());
	         String date = sdf.format(factors.getTimeInterval().getIntervalStart());
	
	         dateList.add(date);
	         months.add(new DataIdValue(factors.getTimeInterval().getId(), date));
	         groupName = factors.getGefTypeId().getDetectionVariableName();
	
	     }
    	
    	 this.frailtyStatus = transformToDto(fs, months);
    	
	     this.parentGroupName = parentGroupName;
	     
	     items.add(new C4AServiceGetGereatricFactorsResponse(groupName, idList, ItemList, dateList));

    }

	private FrailtyStatusDTO transformToDto(List<FrailtyStatusTimeline> fs, List<DataIdValue> months) {
		FrailtyStatusDTO dto = new FrailtyStatusDTO();
		dto.setMonths(months);
		
		Serie preFrail = new Serie("Pre-Frail", new ArrayList<eu.city4age.dashboard.api.external.DataIdValue>());
		Serie frail = new Serie("Frail", new ArrayList<eu.city4age.dashboard.api.external.DataIdValue>());
		Serie fit = new Serie("Fit", new ArrayList<eu.city4age.dashboard.api.external.DataIdValue>());

		int i = 0;
		for(FrailtyStatusTimeline frailty:fs) {
			
			if(frailty != null && frailty.getCdFrailtyStatus() != null) {
				switch(frailty.getCdFrailtyStatus().getFrailtyStatus()) {
	
					case "Pre-frail":
						preFrail.getItems().add(new eu.city4age.dashboard.api.external.DataIdValue(frailty.getTimeInterval().getId(), 0.1));
						frail.getItems().add(new eu.city4age.dashboard.api.external.DataIdValue(frailty.getTimeInterval().getId(), null));
						fit.getItems().add(new eu.city4age.dashboard.api.external.DataIdValue(frailty.getTimeInterval().getId(), null));
						break;
					case "Frail":
						frail.getItems().add(new eu.city4age.dashboard.api.external.DataIdValue(frailty.getTimeInterval().getId(), 0.1));
						preFrail.getItems().add(new eu.city4age.dashboard.api.external.DataIdValue(frailty.getTimeInterval().getId(), null));
						fit.getItems().add(new eu.city4age.dashboard.api.external.DataIdValue(frailty.getTimeInterval().getId(), null));
						break;
					case "Fit":
						fit.getItems().add(new eu.city4age.dashboard.api.external.DataIdValue(frailty.getTimeInterval().getId(), 0.1));
						preFrail.getItems().add(new eu.city4age.dashboard.api.external.DataIdValue(frailty.getTimeInterval().getId(), null));
						frail.getItems().add(new eu.city4age.dashboard.api.external.DataIdValue(frailty.getTimeInterval().getId(), null));
						break;
					default:
						preFrail.getItems().add(new eu.city4age.dashboard.api.external.DataIdValue(frailty.getTimeInterval().getId(), null));
						frail.getItems().add(new eu.city4age.dashboard.api.external.DataIdValue(frailty.getTimeInterval().getId(), null));
						fit.getItems().add(new eu.city4age.dashboard.api.external.DataIdValue(frailty.getTimeInterval().getId(), null));
						break;
		
				}	
			}
			i++;
		}

		dto.getSeries().add(preFrail);
		dto.getSeries().add(frail);
		dto.getSeries().add(fit);

		return dto;
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

	public FrailtyStatusDTO getFrailtyStatus() {
		return frailtyStatus;
	}


}//end class
