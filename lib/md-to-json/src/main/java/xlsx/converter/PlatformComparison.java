package xlsx.converter;

import java.util.List;

public class PlatformComparison {
	
	private String label;
	private List<ComparisonCriteria> criteria;
	
	public PlatformComparison() {
		// TODO Auto-generated constructor stub
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public List<ComparisonCriteria> getCriteria() {
		return criteria;
	}

	public void setCriteria(List<ComparisonCriteria> criteria) {
		this.criteria = criteria;
	}
	

}