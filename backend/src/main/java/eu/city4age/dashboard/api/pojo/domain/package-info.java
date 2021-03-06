/**
 * 
 */
/**
 * @author milos.holclajtner
 *
 */
@org.hibernate.annotations.TypeDefs({
	
	@org.hibernate.annotations.TypeDef(name = "DVTEnumUserType", typeClass = eu.city4age.dashboard.api.jpa.user.types.DVTEnumUserType.class),
	
	@org.hibernate.annotations.TypeDef(name = "PilotEnumUserType", typeClass = eu.city4age.dashboard.api.jpa.user.types.PilotEnumUserType.class),

	@org.hibernate.annotations.TypeDef(name = "AttentionEnumUserType", typeClass = eu.city4age.dashboard.api.jpa.user.types.AttentionEnumUserType.class),
	
	@org.hibernate.annotations.TypeDef (name = "JsonStringType", typeClass = eu.city4age.dashboard.api.jpa.user.types.JsonStringType.class)

})
package eu.city4age.dashboard.api.pojo.domain;