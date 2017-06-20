package eu.city4age.dashboard.api.pojo.domain;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.FilterJoinTable;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import eu.city4age.dashboard.api.pojo.json.view.View;

@Entity
@Table(name = "assessment")
@FilterDefs(value = { @FilterDef(name = "riskStatus", parameters = @ParamDef(name = "riskStatus", type = "char")),
		@FilterDef(name = "dataValidity", parameters = @ParamDef(name = "dataValidity", type = "char")),
		@FilterDef(name = "roleId", parameters = @ParamDef(name = "roleId", type = "long")) })
@Filters(value = { @Filter(name = "riskStatus", condition = "risk_status in (:riskStatus)"),
		@Filter(name = "dataValidity", condition = "data_validity_status in (:dataValidity)") })
public class Assessment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8849726716647395001L;

	static protected Logger logger = LogManager.getLogger(Assessment.class);

	@JsonView(View.AssessmentView.class)
	@Id
	@Basic(optional = false)
	@SequenceGenerator(name = "aa_seq", sequenceName = "assessment_id_seq", allocationSize = 1)
	@GeneratedValue(generator = "aa_seq", strategy = GenerationType.SEQUENCE)
	@Column(name = "id", insertable = true, updatable = true, unique = true, nullable = false)
	protected Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@JsonView(View.AssessmentView.class)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "author_id")
	private UserInRole userInRole;

	@JsonView(View.AssessmentView.class)
	@Column(name = "assessment_comment")
	private String assessmentComment;

	@JsonView(View.AssessmentView.class)
	@Column(name = "risk_status")
	private Character riskStatus;

	@JsonView(View.AssessmentView.class)
	@Column(name = "data_validity_status")
	private Character dataValidity;

	@ManyToOne(targetEntity = GeriatricFactorValue.class, cascade = CascadeType.ALL)
	@JoinTable(name = "assessed_gef_value_set", joinColumns = @JoinColumn(name = "assessment_id"), inverseJoinColumns = @JoinColumn(name = "gef_value_id"))
	private GeriatricFactorValue geriatricFactorValue;

	/*
	 * @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE },
	 * mappedBy = "assessments", targetEntity = GeriatricFactorValue.class)
	 * private Set<GeriatricFactorValue> geriatricFactorValue = new
	 * HashSet<GeriatricFactorValue>();
	 */
	@FilterJoinTable(name="roleId", condition="role_id = :roleId")
	@ManyToMany(targetEntity = Role.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "assessment_audience_role", joinColumns = @JoinColumn(name = "assessment_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<Role>(0);

	@JsonIgnore
	private Date created;

	@JsonIgnore
	private Date updated;

	public Assessment() {
	}

	public Assessment(AssessmentBuilder builder) {
		this.userInRole = builder.userInRole;
		this.assessmentComment = builder.assessmentComment;
		this.riskStatus = builder.riskStatus;
		this.dataValidity = builder.dataValidity;
		this.created = builder.created;
	}

	public Assessment(UserInRole userInRole, String assessmentComment, Character riskStatus, Character dataValidity,
			GeriatricFactorValue geriatricFactorValue, Date created, Date updated, Set<Role> roles) {
		this.userInRole = userInRole;
		this.assessmentComment = assessmentComment;
		this.riskStatus = riskStatus;
		this.dataValidity = dataValidity;
		this.created = created;
		this.updated = updated;
		this.geriatricFactorValue = geriatricFactorValue;
		this.roles = roles;
	}

	public UserInRole getUserInRole() {
		return this.userInRole;
	}

	public void setUserInRole(UserInRole userInRole) {
		this.userInRole = userInRole;
	}

	public String getAssessmentComment() {
		return this.assessmentComment;
	}

	public void setAssessmentComment(String assessmentComment) {
		this.assessmentComment = assessmentComment;
	}

	public Character getRiskStatus() {
		return this.riskStatus;
	}

	public void setRiskStatus(Character riskStatus) {
		this.riskStatus = riskStatus;
	}

	@JsonView(View.AssessmentView.class)
	public String getRiskStatusDesc() {
		if (this.riskStatus != null)
			switch (this.riskStatus) {
			case 'A':
				return ("Alert");
			case 'W':
				return ("Warning");
			default:
				return ("Comment");

			}
		return null;
	}

	public Character getDataValidity() {
		return this.dataValidity;
	}

	public void setDataValidity(Character dataValidity) {
		this.dataValidity = dataValidity;
	}

	@JsonView(View.AssessmentView.class)
	public String getDataValidityDesc() {
		if (this.dataValidity != null)
			switch (this.dataValidity) {
			case 'F':
				return ("Faulty data");
			case 'Q':
				return ("Questionable data");
			default:
				return ("Valid data");
			}
		return null;
	}

	public GeriatricFactorValue getGeriatricFactorValue() {
		return geriatricFactorValue;
	}

	public void setGeriatricFactorValue(GeriatricFactorValue geriatricFactorValue) {
		this.geriatricFactorValue = geriatricFactorValue;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Date getCreated() {
		return created;
	}

	@JsonView(View.AssessmentView.class)
	public String getDateAndTime() {
		if (this.created != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			return sdf.format(created);
		}
		return null;
	}

	public Date getUpdated() {
		return updated;
	}

	public String getUserInSystemDisplayName() {
		return getUserInRole().getUserInSystem().getDisplayName();
	}

	@JsonIgnore
	public Long getRoleId() {
		return getRoles().iterator().next().getId();
	}

	public static class AssessmentBuilder {
		private UserInRole userInRole = new UserInRole();
		private String assessmentComment = "";
		private Character riskStatus = null;
		private Character dataValidity = null;
		private Date created = new Date();

		public AssessmentBuilder userInRole(UserInRole userInRole) {
			this.userInRole = userInRole;
			return this;
		}

		public AssessmentBuilder assessmentComment(String assessmentComment) {
			this.assessmentComment = assessmentComment;
			return this;
		}

		public AssessmentBuilder riskStatus(Character riskStatus) {
			this.riskStatus = riskStatus;
			return this;
		}

		public AssessmentBuilder dataValidity(Character dataValidity) {
			this.dataValidity = dataValidity;
			return this;
		}

		public AssessmentBuilder created(Date created) {
			this.created = created;
			return this;
		}

		public Assessment build() {
			return new Assessment(this);
		}

	}

	public int hashCode() {
		return id.intValue();
	}

	public boolean equals(Object obj) {
		boolean flag = false;
		Assessment aa = (Assessment) obj;
		if (aa.id == id)
			flag = true;
		return flag;
	}

}
