package eu.city4age.dashboard.api.pojo.json.desobj;

import java.math.BigDecimal;

public class Element {

		private String name;
		private Integer level;
		private BigDecimal weight;

		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Integer getLevel() {
			return level;
		}
		public void setLevel(Integer level) {
			this.level = level;
		}
		public BigDecimal getWeight() {
			return weight;
		}
		public void setWeight(BigDecimal weight) {
			this.weight = weight;
		}


	}