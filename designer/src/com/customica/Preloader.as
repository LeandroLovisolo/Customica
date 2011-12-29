package com.customica {
	
	import flash.display.DisplayObject;
	import flash.display.Sprite;
	import flash.events.ProgressEvent;
	import flash.events.TimerEvent;
	import flash.geom.Matrix;
	import flash.geom.Rectangle;
	import flash.text.TextField;
	import flash.text.TextFormat;
	import flash.text.TextFormatAlign;
	import flash.utils.Timer;
	
	import mx.core.BitmapAsset;
	import mx.events.FlexEvent;
	import mx.preloaders.IPreloaderDisplay;
	
	public class Preloader extends Sprite implements IPreloaderDisplay {
		
		[Embed(source="../../../images/loading.gif")]
		private var loadingImageClass:Class;
		
		[Bindable]
		public var backgroundAlpha: Number;
		
		[Bindable]
		public var backgroundColor: uint;
		
		[Bindable]
		public var backgroundImage: Object;
		
		[Bindable]
		public var backgroundSize: String;
		
		[Bindable]
		public var stageHeight: Number;
		
		[Bindable]
		public var stageWidth: Number;
		private var percentageLabel:TextField;
		
		
		public function Preloader() {
			super();
		}

		public function initialize():void {
			var loadingImage:BitmapAsset = new loadingImageClass() as BitmapAsset;
			loadingImage.smoothing = true;
			loadingImage.x = (stageWidth / 2) - (loadingImage.width / 2);
			loadingImage.y = 100;
			addChild(DisplayObject(loadingImage));
			
			percentageLabel = new TextField();
			percentageLabel.text = "0%";
			percentageLabel.width = 50;
			percentageLabel.x = (stageWidth / 2) - 25;
			percentageLabel.y = 150;
			
			var textFormat:TextFormat = new TextFormat();
			textFormat.align = TextFormatAlign.CENTER;
			textFormat.font = "Arial, Helvetica, sans-serif";
			textFormat.bold = true;
			textFormat.color = 0x666666;
			percentageLabel.defaultTextFormat = textFormat;
			
			addChild(percentageLabel);
			
			var preloader:Preloader = this;
			var timer:Timer = new Timer(100);
			timer.addEventListener(TimerEvent.TIMER, function():void {
				// Get the matrix of the object  
				var matrix:Matrix = loadingImage.transform.matrix; 
				
				// Get the rect of the object (to know the dimension) 
				var rect:Rectangle = loadingImage.getBounds(preloader); 
				
				// Translating the desired reference point (in this case, center)
				matrix.translate(- (rect.left + (rect.width/2)), - (rect.top + (rect.height/2))); 
				
				// Rotation (note: the parameter is in radian)
				matrix.rotate((30/180)*Math.PI);
				
				// Translating the object back to the original position.
				matrix.translate(rect.left + (rect.width/2), rect.top + (rect.height/2));
				
				loadingImage.transform.matrix = matrix;
			});
			timer.start();
		}
		
		public function set preloader(preloaderSprite:Sprite):void {
			preloaderSprite.addEventListener(ProgressEvent.PROGRESS, onProgress);
			preloaderSprite.addEventListener(FlexEvent.INIT_COMPLETE, onInitComplete);
		}
		
		private function onProgress(event:ProgressEvent):void {
			percentageLabel.text = int((event.bytesLoaded * 100 / event.bytesTotal)) + "%";
		}
		
		private function onInitComplete(event:FlexEvent):void {
			dispatchEvent(new Event(Event.COMPLETE));
		}
		
	}
	
}