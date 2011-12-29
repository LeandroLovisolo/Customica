package com.customica.canvas
{
	import mx.binding.utils.BindingUtils;

	public class TShirtColor
	{
		
		[Bindable]
		public var name:String;
		
		[Bindable]
		public var icon:Class;
		
		public var xmlName:String;
		public var image:Class;
		
		public function TShirtColor(name:String, xmlName:String, image:Class, icon:Class) {
			this.name = name;
			this.xmlName = xmlName;
			this.image = image;
			this.icon = icon;
		}
		
	}
}