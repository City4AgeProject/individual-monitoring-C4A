/*
 * City4Age Project
 * Horizon 2020  * 
 */
package eu.city4age.dashboard.api.ws.jet;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.log4j.Logger;

/**
 *
 * @author mnou2
 */
@Path(OJCodeBook.PATH)
public class OJCodeBook {

    public static final String PATH = "OJCodeBook";

    private static Logger logger = Logger.getLogger(OJCodeBook.class);

    /**
     * Sample response : <br/>
     * [ {
     * "riskStatus" : "W", "riskStatusDesc" : "Warning", "imagePath" :
     * "images/warning_risk.png" }, { "riskStatus" : "A", "riskStatusDesc" :
     * "Alert", "imagePath" : "images/alert_risk.png" } ]
     *
     * @return
     * @throws JsonProcessingException
     */
    @GET
    @Path("selectAllRisks")
    @Produces(MediaType.APPLICATION_JSON)
    public Response selectAllRiskStauses() throws JsonProcessingException {

        List<OJCodeBook.CdRiskStatus> list = new ArrayList<OJCodeBook.CdRiskStatus>();
        //TODO : use query to get all risk statuses or some other service ...
        list.add(new OJCodeBook.CdRiskStatus("W", "Warning", "images/risk_warning.png"));
        list.add(new OJCodeBook.CdRiskStatus("A", "Alert", "images/risk_alert.png"));
        return Response.ok(ObjectMapperProvider.produceMapper().writeValueAsString(list)).build();
    }

    @GET
    @Path("selectDataValidities")
    @Produces(MediaType.APPLICATION_JSON)
    public Response selectAllDataValidities() throws JsonProcessingException {

        List<OJCodeBook.CdDataValidity> list = new ArrayList<OJCodeBook.CdDataValidity>();
        //TODO : use query to get all risk statuses or some other service ...
        list.add(new OJCodeBook.CdDataValidity("QUESTIONABLE_DATA", "Questionable data", "images/questionable_data.png"));
        list.add(new OJCodeBook.CdDataValidity("FAULTY_DATA", "Faulty data", "images/faulty_data.png"));
        list.add(new OJCodeBook.CdDataValidity("VALID_DATA", "Valid data", ""));
        return Response.ok(ObjectMapperProvider.produceMapper().writeValueAsString(list)).build();
    }
    
    //TODO  : theese classes should be in domain and mapped by codebook tables
    public class CdRiskStatus {

        private String riskStatus;
        private String riskStatusDesc;
        private String imagePath;

        public CdRiskStatus(String rs, String rsd, String pth) {
            this.imagePath = pth;
            this.riskStatusDesc = rsd;
            this.riskStatus = rs;
        }

        /**
         * @return the riskStatus
         */
        public String getRiskStatus() {
            return riskStatus;
        }

        /**
         * @param riskStatus the riskStatus to set
         */
        public void setRiskStatus(String riskStatus) {
            this.riskStatus = riskStatus;
        }

        /**
         * @return the riskStatusDesc
         */
        public String getRiskStatusDesc() {
            return riskStatusDesc;
        }

        /**
         * @param riskStatusDesc the riskStatusDesc to set
         */
        public void setRiskStatusDesc(String riskStatusDesc) {
            this.riskStatusDesc = riskStatusDesc;
        }

        /**
         * @return the imagePath
         */
        public String getImagePath() {
            return imagePath;
        }

        /**
         * @param imagePath the imagePath to set
         */
        public void setImagePath(String imagePath) {
            this.imagePath = imagePath;
        }

    }

    public class CdDataValidity {
        
        private String dataValidity;
        private String dataValidityDesc;
        private String imagePath;
        
        public CdDataValidity(String dataValidity, String dataValidityDesc, String imagePath) {
            this.dataValidity = dataValidity;
            this.dataValidityDesc = dataValidityDesc;
            this.imagePath = imagePath;
        }

        /**
         * @return the dataValidity
         */
        public String getDataValidity() {
            return dataValidity;
        }

        /**
         * @param dataValidity the dataValidity to set
         */
        public void setDataValidity(String dataValidity) {
            this.dataValidity = dataValidity;
        }

        /**
         * @return the dataValidityDesc
         */
        public String getDataValidityDesc() {
            return dataValidityDesc;
        }

        /**
         * @param dataValidityDesc the dataValidityDesc to set
         */
        public void setDataValidityDesc(String dataValidityDesc) {
            this.dataValidityDesc = dataValidityDesc;
        }

        /**
         * @return the imagePath
         */
        public String getImagePath() {
            return imagePath;
        }

        /**
         * @param imagePath the imagePath to set
         */
        public void setImagePath(String imagePath) {
            this.imagePath = imagePath;
        }
    }
    
}
