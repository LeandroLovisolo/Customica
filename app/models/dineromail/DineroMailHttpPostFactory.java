package models.dineromail;

import models.Order;
import models.TShirt;
import models.TShirt.Gender;
import models.dineromail.DineroMailHttpPost.Parameter;


public class DineroMailHttpPostFactory {

	private static final String URL = "https://checkout.dineromail.com/CheckOut";
	
	private static final Parameter TOOL_PARAMETER = new Parameter("tool", "button");
	private static final Parameter MERCHANT_PARAMETER = new Parameter("merchant", "780321");
	private static final Parameter COUNTRY_ID_PARAMETER = new Parameter("country_id", "1");
	private static final Parameter SELLER_NAME_PARAMETER = new Parameter("seller_name", "Customica.com");
	private static final Parameter LANGUAGE_PARAMETER = new Parameter("language", "es");
	private static final Parameter TRANSACTION_ID_PARAMETER = new Parameter("transaction_id");
	private static final Parameter CURRENCY_PARAMETER = new Parameter("currency", "ars");
	private static final Parameter OK_URL_PARAMETER = new Parameter("ok_url");
	private static final Parameter ERROR_URL_PARAMETER = new Parameter("error_url");
	private static final Parameter PENDING_URL_PARAMETER = new Parameter("pending_url");
	private static final Parameter CHANGE_QUANTITY_PARAMETER = new Parameter("change_quantity", "0");
	private static final Parameter ITEM_NAME_1_PARAMETER = new Parameter("item_name_1", "Remera (incluye envío)");
	private static final Parameter ITEM_CODE_1_PARAMETER = new Parameter("item_code_1");
	private static final Parameter ITEM_QUANTITY_1_PARAMETER = new Parameter("item_quantity_1", "1");
	private static final Parameter ITEM_AMMOUNT_1_PARAMETER = new Parameter("item_ammount_1", TShirt.PRICE.toString() + "00");
	private static final Parameter ITEM_CURRENCY_1_PARAMETER = new Parameter("item_currency_1", "ars");
	private static final Parameter ITEM_NAME_2_PARAMETER = new Parameter("item_name_2", "Envío");
	private static final Parameter ITEM_CODE_2_PARAMETER = new Parameter("item_code_2", "0");
	private static final Parameter ITEM_QUANTITY_2_PARAMETER = new Parameter("item_quantity_2", "1");
	private static final Parameter ITEM_AMMOUNT_2_PARAMETER = new Parameter("item_ammount_2", TShirt.SHIPPING_COST.toString() + "00");
	private static final Parameter ITEM_CURRENCY_2_PARAMETER = new Parameter("item_currency_2", "ars");	
	private static final Parameter HEADER_IMAGE_PARAMETER = new Parameter("header_image");
	private static final Parameter HEADER_WIDTH_PARAMETER = new Parameter("header_width", "1");
	private static final Parameter EXPANDED_STEP_PM_PARAMETER = new Parameter("expanded_step_PM", "1");
	private static final Parameter STEP_COLOR_PARAMETER = new Parameter("step_color", "F6F6F6");
	private static final Parameter HOVER_STEP_COLOR_PARAMETER = new Parameter("hover_step_color", "E8FFD3");
	private static final Parameter LINKS_COLOR_PARAMETER = new Parameter("links_color", "669900");
	private static final Parameter FONT_COLOR_PARAMETER = new Parameter("font_color", "666666");
	private static final Parameter BORDER_COLOR_PARAMETER = new Parameter("border_color", "999999");
	private static final Parameter BUTTON_COLOR_PARAMETER = new Parameter("button_color", "7DBE0B");
	
	public static DineroMailHttpPost create(
			Order order,
			String logoUrl,
			String successUrl,
			String failureUrl) {
		return new DineroMailHttpPost()
				.setUrl(URL)
				.addParameter(TOOL_PARAMETER)
				.addParameter(MERCHANT_PARAMETER)
				.addParameter(COUNTRY_ID_PARAMETER)
				.addParameter(SELLER_NAME_PARAMETER)
				.addParameter(LANGUAGE_PARAMETER)
				.addParameter(TRANSACTION_ID_PARAMETER.newWithValue(order.id.toString()))
				.addParameter(CURRENCY_PARAMETER)
				.addParameter(OK_URL_PARAMETER.newWithValue(successUrl))
				.addParameter(ERROR_URL_PARAMETER.newWithValue(failureUrl))
				.addParameter(PENDING_URL_PARAMETER.newWithValue(failureUrl))
				.addParameter(CHANGE_QUANTITY_PARAMETER)
				.addParameter(ITEM_NAME_1_PARAMETER.newWithValue(
						"Remera de " + (order.gender == Gender.MALE ? "varón" : "mujer") + " talle " + order.size))
				.addParameter(ITEM_CODE_1_PARAMETER.newWithValue(order.tShirt.id.toString()))
				.addParameter(ITEM_QUANTITY_1_PARAMETER)
				.addParameter(ITEM_AMMOUNT_1_PARAMETER)
				.addParameter(ITEM_CURRENCY_1_PARAMETER)
				.addParameter(ITEM_NAME_2_PARAMETER)
				.addParameter(ITEM_CODE_2_PARAMETER)
				.addParameter(ITEM_QUANTITY_2_PARAMETER)
				.addParameter(ITEM_AMMOUNT_2_PARAMETER)
				.addParameter(ITEM_CURRENCY_2_PARAMETER)				
				.addParameter(HEADER_IMAGE_PARAMETER.newWithValue(logoUrl))
				.addParameter(HEADER_WIDTH_PARAMETER)
				.addParameter(EXPANDED_STEP_PM_PARAMETER)
				.addParameter(STEP_COLOR_PARAMETER)
				.addParameter(HOVER_STEP_COLOR_PARAMETER)
				.addParameter(LINKS_COLOR_PARAMETER)
				.addParameter(FONT_COLOR_PARAMETER)
				.addParameter(BORDER_COLOR_PARAMETER)
				.addParameter(BUTTON_COLOR_PARAMETER);
	}
	
}