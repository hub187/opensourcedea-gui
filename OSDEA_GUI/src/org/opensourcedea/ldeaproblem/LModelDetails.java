package org.opensourcedea.ldeaproblem;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

import org.opensourcedea.dea.ModelType;

public class LModelDetails implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1687794316329891948L;
	private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
	private ModelType modelType;
	private double rtsUB = 1;
	private String modelName;
	private double rtsLB = 0;
	
	
	public double getRtsLB() {
		return rtsLB;
	}

	@SuppressWarnings("boxing")
	public void setRtsLB(double newRtsLowerBound) {
		double oldrtsLB = this.rtsLB;
		this.rtsLB = newRtsLowerBound;
		changeSupport.firePropertyChange("rtsLB", oldrtsLB, newRtsLowerBound);
//		this.rtsLB = newrtsLB;
	}
	
	public double getRtsUB() {
		return rtsUB;
	}
	@SuppressWarnings("boxing")
	public void setRtsUB(double newRtsUpperBound) {
		
		double oldrtsUB = this.rtsUB;
		this.rtsUB = newRtsUpperBound;
		changeSupport.firePropertyChange("rtsUB", oldrtsUB, newRtsUpperBound);

//		this.rtsUB = newRtsUpperBound;

	}
	
	public ModelType getModelType() {
		return modelType;
	}
	public void setModelType(ModelType newModelType) {
		ModelType oldModelType = this.modelType;
		this.modelType = newModelType;
		changeSupport.firePropertyChange("modelType", oldModelType, newModelType);
	}
	

	
	public String getModelName() {
		return modelName;
	}
	
	public void setModelName(String newModelName) {
		String oldModelName = this.modelName;
		this.modelName = newModelName;
		changeSupport.firePropertyChange("modelName", oldModelName, newModelName);
	}


	
	
	public void addPropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		changeSupport.addPropertyChangeListener(propertyName, listener);
	}
	
	public void removePropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		changeSupport.removePropertyChangeListener(propertyName, listener);
	}
	
	
	
}
