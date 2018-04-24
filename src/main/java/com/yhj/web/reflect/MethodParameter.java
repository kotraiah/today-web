package com.yhj.web.reflect;

public final class MethodParameter {

	/**	是否不能为空*/
	private boolean required = true;
	
	/**	参数名*/
	private String parameterName;

	/**	参数类型*/
	private Class<?> parameterClass;


	public MethodParameter(String parameterName, Class<?> parameterClass, boolean required) {
		this.parameterName = parameterName;
		this.parameterClass = parameterClass;
		this.required = required;
	}
	
	
	public MethodParameter(String parameterName, Class<?> parameterClass) {
		this.parameterName = parameterName;
		this.parameterClass = parameterClass;
	}


	public MethodParameter() {

	}
	
	
	public String getParameterName() {
		return parameterName;
	}

	public Class<?> getParameterClass() {
		return parameterClass;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	public void setParameterClass(Class<?> parameterClass) {
		this.parameterClass = parameterClass;
	}

	public final boolean isRequired() {
		return required;
	}


	public final void setRequired(boolean required) {
		this.required = required;
	}


	@Override
	public String toString() {
		return "{\n\t\"required\":\"" + required + "\",\n\t\"parameterName\":\"" + parameterName
				+ "\",\n\t\"parameterClass\":\"" + parameterClass + "\"\n}";
	}

	
}
