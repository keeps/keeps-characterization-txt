package pt.keep.validator.txt.result;

import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import pt.keep.validator.txt.utils.jaxb.MapAdapter;

@XmlRootElement(name = "textCharacterizationResult")
@XmlType(propOrder = { "validationInfo","features" })
public class Result {

	
	private ValidationInfo validationInfo;
	private Map<String,String> features;
	
	
	@XmlElement(required=false)
	@XmlJavaTypeAdapter(MapAdapter.class)
	public Map<String, String> getFeatures() {
		return features;
	}
	public void setFeatures(Map<String, String> features) {
		this.features = features;
	}
	

	@XmlElement(required=true)
	public ValidationInfo getValidationInfo() {
		return validationInfo;
	}

	public void setValidationInfo(ValidationInfo validationInfo) {
		this.validationInfo = validationInfo;
	}
	
	
	

}
