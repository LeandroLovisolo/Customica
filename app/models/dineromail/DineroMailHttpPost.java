package models.dineromail;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DineroMailHttpPost {
	
	public static class Parameter {
		
		private String name;
		private String value;
		
		public Parameter(String name) {
			this(name, null);
		}
		
		public Parameter(String name, String value) {
			this.name = name;
			this.value = value;
		}

		public String getName() {
			return name;
		}

		public String getValue() {
			return value;
		}
		
		public Parameter newWithValue(String value) {
			return new Parameter(name, value);
		}
		
	}

	private String url;
	private List<Parameter> parameters = new ArrayList<Parameter>();
	
	public String getUrl() {
		return url;
	}
	
	public DineroMailHttpPost setUrl(String url) {
		this.url = url;
		return this;
	}
	
	public DineroMailHttpPost addParameter(Parameter parameter) {
		parameters.add(parameter);
		return this;
	}

	public List<Parameter> getParameters() {
		return Collections.unmodifiableList(parameters);
	}
	
}