package xlsx.converter;

import java.util.List;

public class ComparisonCriteria {
	
	private String label;
	private List<ComparisonElement> criteriaElements;
	
	public ComparisonCriteria() {
		// TODO Auto-generated constructor stub
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public List<ComparisonElement> getCriteriaElements() {
		return criteriaElements;
	}

	public void setCriteriaElements(List<ComparisonElement> criteriaElements) {
		this.criteriaElements = criteriaElements;
	}
	
	
}
