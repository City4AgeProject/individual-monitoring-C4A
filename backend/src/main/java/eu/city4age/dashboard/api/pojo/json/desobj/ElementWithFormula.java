package eu.city4age.dashboard.api.pojo.json.desobj;

import java.math.BigDecimal;

public class ElementWithFormula extends Element {

		private String formula;
		
		public ElementWithFormula(String formula, String name, Integer level, BigDecimal weight) {
			super(name,level,weight);
			this.formula = formula;
			}

		public String getFormula() {
			return formula;
		}
		public void setFormula(String formula) {
			this.formula = formula;
		}


	}