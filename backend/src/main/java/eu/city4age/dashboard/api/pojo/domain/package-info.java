/**
 * 
 */
/**
 * @author milos.holclajtner
 *
 */
@org.hibernate.annotations.TypeDefs({
	
	@org.hibernate.annotations.TypeDef(name = "DVTEnumUserType", typeClass = eu.city4age.dashboard.api.pojo.enu.DVTEnumUserType.class),
	
	@org.hibernate.annotations.TypeDef(name = "PilotEnumUserType", typeClass = eu.city4age.dashboard.api.pojo.enu.PilotEnumUserType.class)

})
package eu.city4age.dashboard.api.pojo.domain;