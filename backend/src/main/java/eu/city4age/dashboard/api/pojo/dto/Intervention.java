package eu.city4age.dashboard.api.pojo.dto;

public class Intervention {

  private Long careReceipientID;
  private Integer interventions;
  
  public Intervention(){}
  
  public Long getCareReceipientID() {
    return careReceipientID;
  }
  public void setCareReceipientID(Long careReceipientID) {
    this.careReceipientID = careReceipientID;
  }
  public Integer getInterventions() {
    return interventions;
  }
  public void setInterventions(Integer interventions) {
    this.interventions = interventions;
  }
  
  
}
