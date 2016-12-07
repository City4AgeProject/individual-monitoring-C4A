/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.city4age.dashboard.api.ws.jet.dto;

import eu.city4age.dashboard.api.dto.DiagramDataDTO;
import eu.city4age.dashboard.api.model.GeriatricFactorValue;
import eu.city4age.dashboard.api.model.TimeInterval;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author mnou2
 */
public class DataSet implements Serializable {
    
    private String name;
    private List<String> groups;
    private List<Serie> series;

    private static List<String> MARKABLE_SERIES_NAMES = Arrays.asList(new String[] {"Alerts","Warnings","Comments"});
    private static Map<String, String> MARKABLE_COLORS = new HashMap<String, String>();
    
    static {
        MARKABLE_COLORS.put("Alerts", "#e83d17");
        MARKABLE_COLORS.put("Warnings", "#ffff66");
        MARKABLE_COLORS.put("Comments", "#ebebeb");
    }
    
    public DataSet(DiagramDataDTO dto) {
        name = "";
        List<GeriatricFactorValue> gefData = dto.getGefData();
        groups = new ArrayList<String>();
        series = new ArrayList<Serie>();
        for(String serieName : dto.getGefLabels()) {
            Serie newSerie = new Serie();
            newSerie.setName(serieName.trim());
            series.add(newSerie);
        }
        
        for(GeriatricFactorValue gefv : gefData) {
            Item newItem = new Item(gefv.getId(), gefv.getGefValue().floatValue());
            String currentSerieName = gefv.getCdDetectionVariable().getDetectionVariableName().trim();
            Serie foundSerie = findSerieByName(currentSerieName);
            if(foundSerie.getItems()==null)
                foundSerie.setItems(new ArrayList<Item>());
            foundSerie.getItems().add(newItem);
            if(MARKABLE_SERIES_NAMES.contains(currentSerieName)) {
                foundSerie.setColor(MARKABLE_COLORS.get(currentSerieName));
                foundSerie.setSource("images/comment.png");
                foundSerie.setLineType("none");
                foundSerie.setMarkerDisplayed("on");
                foundSerie.setMarkerSize(20);
            }
            String groupName = calculateGroupNameForTimeInterval(gefv.getTimeInterval());
            if(!groups.contains(groupName)) {
                groups.add(groupName);
            }
        }
    }
    
    private Serie findSerieByName(String serieName) {
        if(series!=null)
            for(Serie current : series) {
                if(current.getName().equals(serieName.trim())) {
                    return current;
                }
            }
        return null;
    }
    
    private String calculateGroupNameForTimeInterval(TimeInterval source) {
        if("MON".equals(source.getTypicalPeriod())) {
            return new SimpleDateFormat("MMM yyyy").format(source.getIntervalStart());
        }
        if("QTR".equals(source.getTypicalPeriod())) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(source.getIntervalStart());
            int quarter = (cal.get(Calendar.MONTH) / 3);
            String[] mQuarterKey = {"Qt1", "Qt2", "Qt3", "Qt4"};
            String strQuarter = mQuarterKey[quarter];
            return new SimpleDateFormat("yyyy").format(source.getIntervalStart()) + " " + strQuarter;
        }
        if("SEM".equals(source.getTypicalPeriod())) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(source.getIntervalStart());
            int quarter = (cal.get(Calendar.MONTH));
            String[] mQuarterKey = {"Sm1", "Sm2"};
            String strSem = mQuarterKey[quarter];
            return new SimpleDateFormat("yyyy").format(source.getIntervalStart()) + " " + strSem;
        }
        if("1YR".equals(source.getTypicalPeriod())) {
            return new SimpleDateFormat("yyyy").format(source.getIntervalStart());
        }
        if("2YR".equals(source.getTypicalPeriod())) {
            String beginYr = new SimpleDateFormat("yyyy").format(source.getIntervalStart());
            String endYr = new SimpleDateFormat("yyyy").format(source.getIntervalEnd());
            return beginYr + " " + endYr;
        }
        if("3YR".equals(source.getTypicalPeriod())) {
            String beginYr = new SimpleDateFormat("yyyy").format(source.getIntervalStart());
            String endYr = new SimpleDateFormat("yyyy").format(source.getIntervalEnd());
            return beginYr + " " + endYr;
        }
        if("5YR".equals(source.getTypicalPeriod())) {
            String beginYr = new SimpleDateFormat("yyyy").format(source.getIntervalStart());
            String endYr = new SimpleDateFormat("yyyy").format(source.getIntervalEnd());
            return beginYr + " " + endYr;
        }
        else return "";
    }
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the groups
     */
    public List<String> getGroups() {
        return groups;
    }

    /**
     * @param groups the groups to set
     */
    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    /**
     * @return the series
     */
    public List<Serie> getSeries() {
        return series;
    }

    /**
     * @param series the series to set
     */
    public void setSeries(List<Serie> series) {
        this.series = series;
    }
    
}
