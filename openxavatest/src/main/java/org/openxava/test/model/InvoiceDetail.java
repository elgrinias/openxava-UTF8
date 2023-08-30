package org.openxava.test.model;

import java.math.*;

import javax.persistence.*;
import javax.persistence.Entity;

import org.hibernate.annotations.*;
import org.hibernate.annotations.Parameter;
import org.openxava.annotations.*;
import org.openxava.calculators.*;
import org.openxava.test.actions.*;
import org.openxava.test.validators.*;

import lombok.*;

/**
 * 
 * @author Javier Paniza
 */

@Entity @Data 
@Views({
	@View(members=
		"serviceType;" +
		"quantity, unitPrice, amount;" +
		"product;"  +
		"deliveryDate, soldBy;" +	
		"remarks"
	),
	@View(name="AllMembersInSections", members=
		"overview { " +
			"serviceType;" +
			"product;" +
			"deliveryDate, soldBy;" +	
			"remarks" + 				
		"}" +
		"amounts { " +
			"quantity, unitPrice; total [ amount ]" + // Group inside a section to test a case
		"}"
	)
})
@EntityValidator(value=InvoiceDetailValidator.class,
	properties= { 
		@PropertyValue(name="invoice"), 
		@PropertyValue(name="oid"), 
		@PropertyValue(name="product"),
		@PropertyValue(name="unitPrice"),
		@PropertyValue(name="amount")
	}
)
public class InvoiceDetail {
	
	@ManyToOne // Lazy fetching produces a fails on removing a detail from invoice
	/*
	 This mapping is the assumed one	
	  @JoinColumns({
		@JoinColumn(name="INVOICE_YEAR", referencedColumnName="YEAR"),
		@JoinColumn(name="INVOICE_NUMBER", referencedColumnName="NUMBER")
	})
	*/
	private Invoice invoice;
	
	// It's calculAted in the method calculateOID
	@Id @Hidden 
	private String oid;
	
	@org.hibernate.annotations.Type(type="org.openxava.types.Base1EnumType", 
		parameters={			
			@Parameter(name="enumType", value="org.openxava.test.model.InvoiceDetail$ServiceType")
		}
	)
	private ServiceType serviceType;
	public enum ServiceType { SPECIAL, URGENT }
	
	@Column(name="QTY", length=4) // The column name does not match the property name to test sumColum for this case 
	@Required 
	private int quantity;
	
	@Stereotype("MONEY") @Required
	private BigDecimal unitPrice;
	
	@ManyToOne(fetch=FetchType.LAZY, optional=false)
	private Product product;
	
	@Type(type="org.openxava.types.Date3Type") 
	@Columns(columns = { 
		@Column(name="YEARDELIVERY"), 
		@Column(name="MONTHDELIVERY"), 
		@Column(name="DAYDELIVERY") 
	})	
	@DefaultValueCalculator(CurrentDateCalculator.class)	
	private java.util.Date deliveryDate;
	
	@ManyToOne(fetch=FetchType.LAZY) 	
	@DescriptionsList
	private Seller soldBy;
	
	@Stereotype("MEMO")
	@OnChange(forViews="AllMembersInSections", value=OnChangeVoidAction.class) 
	private String remarks;
	
	@Stereotype("MONEY") @Depends("unitPrice, quantity")
	public BigDecimal getAmount() {
		return getUnitPrice().multiply(new BigDecimal(getQuantity()));
	}

	public boolean isFree() {
		return getAmount().compareTo(new BigDecimal("0")) <= 0;
	}
	
	@PrePersist
	private void calculateOID() {
		// Thus we can calculate an oid in a custom way
		
		// In EJB2 and Hibernate version we use a counter as third element (instead of
		// a currentTimeMillis), but in EJB3 version of OpenXava this is not supported,
		// because it's better to use the standard oid generation of JPA, and rarely
		// to receive a sequential counter from UI would be useful.
		
		// That is the technique of org.openxava.calculators.IAggregateOidCalculator
		// is deprecated in OX3 
		oid = invoice.getYear() + ":" + invoice.getNumber() + ":" + System.currentTimeMillis();
	}
	
	//@PostRemove // Literal translation of postremove-calculator, though it does not work fine with READ COMMITED 
	@PreRemove  // Works fine with READ COMMITED, though it's not a literal translation of postremove-calculator XML component counterpart 
	private void postRemove() {
		invoice.setComment(invoice.getComment() + "DETAIL DELETED");
	}
	
	public BigDecimal getUnitPrice() {
		return unitPrice==null?BigDecimal.ZERO:unitPrice;
	}
	
	public Seller getSoldBy() {
		// In this way because the column for 'soldBy' does not admit null
		try {
			if (soldBy != null) soldBy.toString(); // to force load
			return soldBy;
		}
		catch (EntityNotFoundException ex) {			
			return null;  
		}
	}
	
}
