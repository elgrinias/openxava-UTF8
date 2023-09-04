package org.openxava.actions;

import java.rmi.*;
import java.util.*;

import javax.ejb.*;
import javax.validation.*;
import javax.validation.metadata.*;

import org.openxava.model.*;
import org.openxava.util.*;
import org.openxava.validators.ValidationException;
import org.openxava.view.*;

/**
 * To save a collection element. <p>
 * 
 * The case of collections of entities with @AsEmbedded (or with as-aggregate="true")
 * is treated by {@link AddElementsToCollectionAction}. <p>
 * 
 * @author Javier Paniza
 * @author Jeromy Altuna 
 */

public class SaveElementInCollectionAction extends CollectionElementViewBaseAction {
	
	private boolean containerSaved = false;
	
	public void execute() throws Exception {
		System.out.println("In here++++++++++++++++++++++++");
		Map containerKey = saveIfNotExists(getCollectionElementView().getParent());
		if (XavaPreferences.getInstance().isMapFacadeAutoCommit()) {
			getView().setKeyEditable(false); // To mark as saved
		}
		saveCollectionElement(containerKey);
		commit(); // If we change this, we should run all test suite using READ COMMITED (with hsqldb 2 for example)
		getView().setKeyEditable(false); // To mark as saved
		getCollectionElementView().setCollectionEditingRow(-1);
		getCollectionElementView().clear();		
		resetDescriptionsCache();
		if (containerSaved) getView().getRoot().refresh(); 
		else getView().recalculateProperties();
		closeDialog(); 		
	}
	
	
	protected Map getValuesToSave() throws Exception {
		System.out.println("Role:"+getCollectionElementView().getMetaModel().getMetaReferences());
		return getCollectionElementView().getValues();		
	}
		
	/**
	 * Saves the collection or aggregate.
	 * @param containerKey 
	 * @throws Exception
	 * @since 4.7 
	 */
	protected void saveCollectionElement(Map containerKey) throws Exception {
		if (getCollectionElementView().isEditable()) {
			// Aggregate or entity reference used as aggregate
			boolean isEntity = isEntityReferencesCollection();
			Map values = getValuesToSave();		
			System.out.println("if"+getCollectionElementView().getMetaModel().getMembersNames().toString());
			
			try {
				MapFacade.setValues(getCollectionElementView().getModelName(), getCollectionElementView().getKeyValues(), values);
				addMessage(isEntity?"entity_modified":"aggregate_modified", getCollectionElementView().getModelName());
			}
			catch (ObjectNotFoundException ex) {
					System.out.println(containerKey.toString());
				create(values, isEntity, containerKey);
			}		
		}
		else {
			System.out.println("Else");
			// Entity reference used in the standard way
			validateMaximum(1); 
			associateEntity(getCollectionElementView().getKeyValues());
			addMessage("entity_associated" , getCollectionElementView().getModelName(), getCollectionElementView().getParent().getModelName());
		}
	}
	
	protected void create(Map values, boolean isEntity, Map containerKey) throws CreateException { 
		validateMaximum(1);
		String modelName = isEntity?getCollectionElementView().getParent().getModelName() + "." + getCollectionElementView().getModelName():getCollectionElementView().getModelName();
		System.out.println(isEntity+" "+modelName+" "+values.values().toString()+" "+getMetaCollection().getName());
		MapFacade.createAggregate(modelName, containerKey, getMetaCollection().getName(), values);
		addMessage(isEntity?"entity_created_and_associated":"aggregate_created", getCollectionElementView().getModelName(), getCollectionElementView().getParent().getModelName());
	}

	protected void associateEntity(Map keyValues) throws ValidationException, XavaException, ObjectNotFoundException, FinderException, RemoteException {		
		MapFacade.addCollectionElement(
				getCollectionElementView().getParent().getMetaModel().getName(),
				getCollectionElementView().getParent().getKeyValues(),
				getCollectionElementView().getMemberName(),  
				keyValues);		
	}

	/**
	 * @return The saved object 
	 */
	protected Map saveIfNotExists(View view) throws Exception {
		
		if (getView() == view) {
			if (view.isKeyEditable()) {
			
				Map key = MapFacade.createNotValidatingCollections(getModelName(), view.getValues());
				addMessage("entity_created", getModelName());
				view.addValues(key);
				containerSaved=true;				
				return key;								
			}			
			else {
				System.out.println("222222222222222222222222");
				return view.getKeyValues();									
			}
		}			
		else {
			
			if (isKeyIncomplete(view)) { 
				Map parentKey = saveIfNotExists(view.getParent());
				Map key = Collections.EMPTY_MAP; 
				if (isEntityReferencesCollection(view)) {
					Map values = view.getValues();
					String containerReference = view.getMetaCollection().getMetaReference().getRole();
					values.put(containerReference, parentKey);
					key = MapFacade.createNotValidatingCollections(view.getModelName(), values);
					addMessage("entity_created", view.getModelName()); 
				}
				else if (view.isRepresentsCollection()) { 
					key = MapFacade.createAggregateReturningKey( 
						view.getModelName(),
						parentKey, view.getMemberName(),
						view.getValues()
					);
					addMessage("aggregate_created", view.getModelName());
				} 
				else if (view.isRepresentsAggregate() && !view.getMetaModel().getAllMetaPropertiesKey().isEmpty()) { // A reference to an entity @AsEmbedded
					view.getRoot().refresh();
					key = view.getKeyValues();
				}
				view.addValues(key);
				return key;										
			}
			else {			
				return view.getKeyValues();
			}
		}
	}
	
	private boolean isKeyIncomplete(View view) { 
		Map keyValues = view.getKeyValuesWithValue(); 
		return keyValues.isEmpty() || 
			keyValues.size() < view.getMetaModel().getKeyPropertiesNames().size() + view.getMetaModel().getKeyReferencesNames().size();		
	}
	
	/**
	 * 
	 * @since 5.9
	 */
	protected void addValidationMessage(Exception ex) {  
		if (ex instanceof ValidationException) {		
			addErrors(((ValidationException)ex).getErrors());
		}  
		else if(ex instanceof ConstraintViolationException){
			addConstraintViolationErrors((ConstraintViolationException) ex);					
		} 
		else if (ex instanceof javax.validation.ValidationException) {
			addError(ex.getMessage());
		}
	}

	private void addConstraintViolationErrors(ConstraintViolationException ex) {
		Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
		for (ConstraintViolation<?> violation : violations) {
			String message = violation.getMessage();
			if (message.startsWith("{") && message.endsWith("}")) {			
				message = message.substring(1, message.length() - 1); 							
			}				
			ConstraintDescriptor<?> descriptor = violation.getConstraintDescriptor(); 
			java.lang.annotation.Annotation annotation = descriptor.getAnnotation();
			if(annotation instanceof javax.validation.constraints.AssertTrue) {							
				Object bean = violation.getRootBean();				
				addError(message, bean);					
			}
		}
	}


	public String getNextAction() throws Exception { 		
		return getErrors().contains()?null:getCollectionElementView().getNewCollectionElementAction();
	}
		
}
