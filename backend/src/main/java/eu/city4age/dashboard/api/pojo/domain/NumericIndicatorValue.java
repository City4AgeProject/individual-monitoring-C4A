package eu.city4age.dashboard.api.pojo.domain;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name="numeric_indicator_value")
public class NumericIndicatorValue extends AbstractBaseEntity {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2422155784673783252L;

	@ManyToOne
    @JoinColumn(name="data_source_type")
	private DataSourceType cdDataSourceType;
	
    @ManyToOne
    @JoinColumn(name="nui_type_id")
	private DetectionVariable cdDetectionVariable;
	
    @Transient
    @ManyToOne
    @JoinColumn(name="time_interval_id")
	private TimeInterval timeInterval;
   
    @ManyToOne
    @JoinColumn(name="user_in_role_id")
	private UserInRole userInRole;
    
    @Column(name="nui_value")
	private BigDecimal nuiValue;
	
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
	@OneToMany(mappedBy="numericIndicatorValue",fetch=FetchType.LAZY)
	private Set<InterActivityBehaviourVariation> interActivityBehaviourVariations = new HashSet<InterActivityBehaviourVariation>(0);

	public NumericIndicatorValue() {
	}

	public NumericIndicatorValue(DataSourceType cdDataSourceType, DetectionVariable cdDetectionVariable,
			TimeInterval timeInterval, BigDecimal nuiValue) {
		this.cdDataSourceType = cdDataSourceType;
		this.cdDetectionVariable = cdDetectionVariable;
		this.timeInterval = timeInterval;
		this.nuiValue = nuiValue;
	}

	public NumericIndicatorValue(DataSourceType cdDataSourceType, DetectionVariable cdDetectionVariable,
			TimeInterval timeInterval, UserInRole userInRole,
			BigDecimal nuiValue, Set<InterActivityBehaviourVariation> interActivityBehaviourVariations) {
		this.cdDataSourceType = cdDataSourceType;
		this.cdDetectionVariable = cdDetectionVariable;
		this.timeInterval = timeInterval;
		this.userInRole = userInRole;
		this.nuiValue = nuiValue;
		this.interActivityBehaviourVariations = interActivityBehaviourVariations;
	}

	public DataSourceType getCdDataSourceType() {
		return this.cdDataSourceType;
	}

	public void setCdDataSourceType(DataSourceType cdDataSourceType) {
		this.cdDataSourceType = cdDataSourceType;
	}

	public DetectionVariable getCdDetectionVariable() {
		return this.cdDetectionVariable;
	}

	public void setCdDetectionVariable(DetectionVariable cdDetectionVariable) {
		this.cdDetectionVariable = cdDetectionVariable;
	}

	public TimeInterval getTimeInterval() {
		return this.timeInterval;
	}

	public void setTimeInterval(TimeInterval timeInterval) {
		this.timeInterval = timeInterval;
	}

	public UserInRole getUserInRole() {
		return this.userInRole;
	}

	public void setUserInRole(UserInRole userInRole) {
		this.userInRole = userInRole;
	}

	public BigDecimal getNuiValue() {
		return this.nuiValue;
	}

	public void setNuiValue(BigDecimal nuiValue) {
		this.nuiValue = nuiValue;
	}

	public Set<InterActivityBehaviourVariation> getInterActivityBehaviourVariations() {
		return this.interActivityBehaviourVariations;
	}

	public void setInterActivityBehaviourVariations(Set<InterActivityBehaviourVariation> interActivityBehaviourVariations) {
		this.interActivityBehaviourVariations = interActivityBehaviourVariations;
	}
	
}
