package xlsx.converter;

import java.util.Map;

public class ComparisonCriteria {
	
	private String label;
	private Map<String, ComparisonElement> criteria;
	
	public ComparisonCriteria() {
		// TODO Auto-generated constructor stub
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Map<String, ComparisonElement> getCriteria() {
		return criteria;
	}

	public void setCriteria(Map<String, ComparisonElement> criteria) {
		this.criteria = criteria;
	}
	
	

}
