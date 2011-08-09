var LoginControls = {
	
	initialize: function() {
		FB.Event.subscribe("auth.login", LoginControls.update);
		FB.Event.subscribe("auth.logout", LoginControls.update);
		LoginControls.update();
	},
	
	update: function() {
		console.log("LoginControls.update");
		if(FB.getSession() == null) {
			$("div.loggedInControls").css("display", "none");
			$("div.loggedOutControls").css("display", "block");
		} else {
			$("div.loggedInControls").css("display", "block");
			$("div.loggedOutControls").css("display", "none");
		}
	},
	
	logout: function() {
		FB.logout();
	}
	
};


var Modal = {
		
	current: null,
	
	show: function(id) {
		var modal = $.modal($("#" + id));
		if(modal !== false) Modal.current = modal; 
		$("#simplemodal-overlay").click(Modal.hide);
	},
	
	hide: function() {
		Modal.current.close();
	}
	
};

var SizePicker = {
		
	sizeSelected: false,
		
	initialize: function() {
		SizePicker.addSizes();
		$("ul.gender.tabs a").click(SizePicker.onGenderTabClick);
	},
	
	addSizes: function() {
		$("#sizeField option").each(function() {
			var option = this;
			$("div.sizePicker").append(
					$("<div class='size'></div>")
						.text($(option).text())
						.click(function() {
							SizePicker.onSizeClick(this, $(option).val());
						}));
		});
		$("div.sizePicker")
			.addClass("male")
			.append("<br class='clear'/>")
	},
	
	onSizeClick: function(div, value) {
		$("div.sizePicker div.size").removeClass("selected");
		$(div).addClass("selected");
		$("#sizeField").val(value);
		SizePicker.sizeSelected = true;
	},
	
	onGenderTabClick: function() {
		if($(this).attr("href") == "#male") {
			$("div.sizePicker").removeClass("female");
			$("div.sizePicker").addClass("male");
			$("#genderField").val("MALE");
		}
		if($(this).attr("href") == "#female") {
			$("div.sizePicker").removeClass("male");
			$("div.sizePicker").addClass("female");
			$("#genderField").val("FEMALE");
		}
	}
};

var BuyButton = {
		
	initialize: function() {
		$("#form").submit(function() {
			if(!SizePicker.sizeSelected) {
				$("div.chooseSize").css("display", "block");
				return false;
			} else {
				$("div.chooseSize").css("display", "none");
			}
		});
	}

};