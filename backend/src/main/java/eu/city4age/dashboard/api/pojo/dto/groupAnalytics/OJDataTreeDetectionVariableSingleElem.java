package eu.city4age.dashboard.api.pojo.dto.groupAnalytics;

import java.util.List;

public class OJDataTreeDetectionVariableSingleElem {
	
	private OJDataTreeDetectionVariableAttribute attr;
	
	private List<OJDataTreeDetectionVariableSingleElem> children;

	/**
	 * 
	 */
	public OJDataTreeDetectionVariableSingleElem() {
	}

	/**
	 * @param attr
	 * @param children
	 */
	public OJDataTreeDetectionVariableSingleElem(OJDataTreeDetectionVariableAttribute attr,
			List<OJDataTreeDetectionVariableSingleElem> children) {
		this.attr = attr;
		this.children = children;
	}

	/**
	 * @return the attr
	 */
	public OJDataTreeDetectionVariableAttribute getAttr() {
		return attr;
	}

	/**
	 * @param attr the attr to set
	 */
	public void setAttr(OJDataTreeDetectionVariableAttribute attr) {
		this.attr = attr;
	}

	/**
	 * @return the children
	 */
	public List<OJDataTreeDetectionVariableSingleElem> getChildren() {
		return children;
	}

	/**
	 * @param children the children to set
	 */
	public void setChildren(List<OJDataTreeDetectionVariableSingleElem> children) {
		this.children = children;
	}

}
