/* tmp */

// WARNING: IF YOU CHANGE THIS PASS DateCalendarTest.txt

openxava.getScript(openxava.contextPath + "/xava/editors/flatpickr/" + openxava.language + ".js");

openxava.addEditorInitFunction(function() {
	$('.flatpickr-calendar').remove();
	$('.xava_date').flatpickr({
	    allowInput: true,
	    clickOpens: false,  
	    wrap: true,
	    locale: openxava.language, 
	    onChange: function(selectedDates, dateStr, instance) {
        	if (!$(instance.input).data("datePopupJustClosed") || dateStr === $(instance.input).attr('value')) {
        		$(instance.input).data("changedCancelled", true);
        	}
        	$(instance.input).attr('value', dateStr);
        	$(instance.input).removeData("datePopupJustClosed");
    	},
    	onClose: function(selectedDates, dateStr, instance) {
	    	$(instance.input).data("datePopupJustClosed", true);
    	},    	 
	});
});
