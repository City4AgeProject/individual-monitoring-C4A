package eu.city4age.dashboard.api.pojo.domain;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
import javax.persistence.Table;

import org.apache.log4j.Logger;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.FilterJoinTable;
import org.hibernate.annotations.FilterJoinTables;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import eu.city4age.dashboard.api.pojo.json.view.View;

@Entity
@Table(name = "assessment")
@FilterDefs(value = { @FilterDef(name = "filterByAll", parameters = {
		@ParamDef(name = "riskStatusWarning", type = "boolean"), @ParamDef(name = "riskStatusAlert", type = "boolean"),
		@ParamDef(name = "dataValidityQuestionable", type = "boolean"),
		@ParamDef(name = "dataValidityFaulty", type = "boolean"),
		@ParamDef(name = "assessmentComment", type = "boolean"), @ParamDef(name = "orderByDateAsc", type = "boolean"),
		@ParamDef(name = "orderByDateDesc", type = "boolean"),
		@ParamDef(name = "orderByAuthorNameAsc", type = "boolean"),
		@ParamDef(name = "orderByAuthorNameDesc", type = "boolean"),
		@ParamDef(name = "orderByAuthorRoleAsc", type = "boolean"),
		@ParamDef(name = "orderByAuthorRoleDesc", type = "boolean") }),
		@FilterDef(name = "filterByUser", parameters = { @ParamDef(name = "userInRoleId", type = "long") }) })
@Filters({ @Filter(name = "riskStatusWarning", condition = "riskStatus = 'W'"),
		@Filter(name = "riskStatusAlert", condition = "riskStatus = 'A'"),
		@Filter(name = "dataValidityQuestionable", condition = "dataValidity = 'Q'"),
		@Filter(name = "dataValidityFaulty", condition = "dataValidity = 'F'"),
		@Filter(name = "assessmentComment", condition = "assessmentComment != NULL"),
		@Filter(name = "orderByDateAsc", condition = "created ASC"),
		@Filter(name = "orderByDateDesc", condition = "created DESC") })
public class Assessment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8849726716647395001L;

	static protected Logger logger = Logger.getLogger(Assessment.class);

	@JsonView(View.AssessmentView.class)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
	@FilterJoinTables({ @FilterJoinTable(name = "authorRoleId", condition = "id = :userInRoleId"),
			@FilterJoinTable(name = "orderByAuthorNameAsc", condition = "userInSystem.id ASC"),
			@FilterJoinTable(name = "orderByAuthorNameDesc", condition = "userInSystem.id DESC"),
			@FilterJoinTable(name = "orderByAuthorRoleAsc", condition = "roleId ASC"),
			@FilterJoinTable(name = "orderByAuthorRoleDesc", condition = "roleId DESC") })
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

	@JsonView(View.AssessmentView.class)
	public String getRiskStatusImage() {
		if (this.riskStatus != null)
			switch (this.riskStatus) {
			case 'A':
				return ("images/risk_alert.png");
			case 'W':
				return ("images/risk_warning.png");
			default:
				return ("images/comment.png");
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

	@JsonView(View.AssessmentView.class)
	public String getDataValidityImage() {
		if (this.dataValidity != null)
			switch (dataValidity) {
			case 'F':
				return ("images/faulty_data.png");
			case 'Q':
				return ("images/questionable_data.png");
			default:
				return ("images/valid_data.png");
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
	public String getDateAndTime() throws ParseException {
		if (this.created != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm");
			return sdf.format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(created.toString()));
		}
		return null;
	}

	public Date getUpdated() {
		return updated;
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

}
