package xlsx.converter;

public class ComparisonElement {
	
	private String label;
	private String criterium;
	private double elemValue;
	
	public ComparisonElement() {
		// TODO Auto-generated constructor stub
	}

	

	public String getLabel() {
		return label;
	}



	public void setLabel(String label) {
		this.label = label;
	}



	public String getCriterium() {
		return criterium;
	}

	public void setCriterium(String criterium) {
		this.criterium = criterium;
	}



	public double getElemValue() {
		return elemValue;
	}



	public void setElemValue(double elemValue) {
		this.elemValue = elemValue;
	}

	@Override
	public String toString() {
		return " -> " + this.label + " | " + this.criterium + " | " + this.elemValue;
	}

}
