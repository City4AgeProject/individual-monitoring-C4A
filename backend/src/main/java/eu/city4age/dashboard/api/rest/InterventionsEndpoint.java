package eu.city4age.dashboard.api.rest;

import java.util.Date;
import java.util.List;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import eu.city4age.dashboard.api.jpa.CareProfileRepository;
import eu.city4age.dashboard.api.jpa.UserInRoleRepository;
import eu.city4age.dashboard.api.pojo.domain.CareProfile;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.dto.Intervention;

@Component
@Transactional(value="transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED, readOnly = false)
@Path(InterventionsEndpoint.PATH)
public class InterventionsEndpoint {

  public static final String PATH = "inteventions";

  protected static Logger logger = LogManager.getLogger(InterventionsEndpoint.class);
  
  @Autowired
  private CareProfileRepository careProfileRepo;
  
  @Autowired
  private UserInRoleRepository userInRoleRepository;
  
  @PUT
  @Path("updateInterventions")
  @Produces({MediaType.APPLICATION_JSON, "application/javascript"})
  public ResponseEntity<Boolean> updateInterventions(@RequestBody List<Intervention> interventions){
   
    ResponseEntity<Boolean> response;
    
    try{
      
      for(int i= 0; i < interventions.size(); i++){
        
        Intervention intervention = interventions.get(i);
        
        if(intervention.getCareReceipientID() !=null && intervention.getCareReceipientID() > 0){
          
          CareProfile profile = careProfileRepo.findOne(intervention.getCareReceipientID());
          
          if(profile != null){
            
            profile.setInterventions(((intervention.getInterventions() > 0) ? intervention.getInterventions() : 0));
            
          }else{
            
            profile = new CareProfile();
            
            UserInRole userInRole = userInRoleRepository.findOne(intervention.getCareReceipientID());
            UserInRole system = userInRoleRepository.findOne((long)2);

            profile.setUserInRoleId(userInRole.getId());
            profile.setUserInRole(userInRole);
            profile.setAttentionStatus('A');
            profile.setIndividualSummary("N/A");
            profile.setUserInRoleByCreatedBy(system);
            profile.setCreated(new Date());
            profile.setInterventions((intervention.getInterventions() > 0) ? intervention.getInterventions() : 0);
              
          }
          
          careProfileRepo.saveAndFlush(profile);
          
        }
        
      }//end for
      
      response = new ResponseEntity<>(true, HttpStatus.OK); 
      
    }catch(Exception e){
      
      response = new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
      logger.error("Error updating interventions!", e);
    }
    
    return response;
    
  }
  
}
