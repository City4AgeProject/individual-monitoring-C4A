/**
 * Copyright (c) 2014, 2016, Oracle and/or its affiliates.
 * The Universal Permissive License (UPL), Version 1.0
 */
"use strict";
/*
 Copyright 2013 jQuery Foundation and other contributors
 Released under the MIT license.
 http://jquery.org/license
*/
define(["ojs/ojcore","jquery","ojs/ojeditablevalue","ojs/ojbutton"],function(a,g){(function(){a.Na("oj.ojInputNumber",g.oj.editableValue,{version:"1.0.0",defaultElement:"\x3cinput\x3e",widgetEventPrefix:"oj",options:{converter:a.Da.Tl(a.vm.CONVERTER_TYPE_NUMBER).createConverter(),max:null,min:null,placeholder:void 0,rawValue:void 0,readOnly:!1,step:1,value:null},getNodeBySubId:function(a){var c=this._superApply(arguments),e;c||(e=a.subId,"oj-inputnumber-up"===e&&(c=this.widget().find(".oj-inputnumber-up")[0]),
"oj-inputnumber-down"===e&&(c=this.widget().find(".oj-inputnumber-down")[0]),"oj-inputnumber-input"===e&&(c=this.widget().find(".oj-inputnumber-input")[0]));return c||null},getSubIdByNode:function(a){var c=null;null!=a&&(a===this.widget().find(".oj-inputnumber-up")[0]?c={subId:"oj-inputnumber-up"}:a===this.widget().find(".oj-inputnumber-down")[0]?c={subId:"oj-inputnumber-down"}:a===this.widget().find(".oj-inputnumber-input")[0]&&(c={subId:"oj-inputnumber-input"}));return c||this._superApply(arguments)},
refresh:function(){this._super();this.$a()},stepDown:function(a){this.Eca(a,!1)},stepUp:function(a){this.Eca(a,!0)},widget:function(){return this.rm},Wf:function(b,c){var e=this.options,f=this;this._superApply(arguments);a.Me.mp([{ha:"disabled",Pd:!0},{ha:"placeholder"},{ha:"value"},{ha:"readonly",option:"readOnly",Pd:!0},{ha:"required",qh:!0,Pd:!0},{ha:"title"},{ha:"min"},{ha:"max"},{ha:"step"}],c,this,function(a){for(var b=["value","step","min","max"],c=0;c<b.length;c++){var d=b[c],g=d in a?a[d]:
e[d];null!=g&&(a[d]="step"===d?f.ID(g):f.Mo(d,g))}});if(void 0===e.value)throw Error("ojInputNumber has no value");if(null!=e.min&&null!=e.max&&e.max<e.min)throw Error("ojInputNumber's max must not be less than min");},_ComponentCreate:function(){this._super();this.xC();this.u9={};this.$a();this._on(this.gq);this._focusable(this.rm)},J0:function(a,c,e){this._superApply(arguments);switch(a){case "min":case "max":this.wj(a,this.options[a])}},_setOption:function(a,c,e){var f;f="value"===a||"max"===a||
"min"===a?this.Mo(a,c):"step"===a?this.ID(c):c;this._super(a,f,e);if("max"===a||"min"===a)this.d5(),this.wx();"disabled"===a&&this.element.prop("disabled",!!c);"readOnly"===a&&(this.element.prop("readonly",!!c),this.tK("readOnly",this.options.readOnly),this.Aaa("readOnly",this.options.readOnly))},_destroy:function(){var b=this._super();this.lX.ojButtonset("destroy");this.lX.remove();this.lX=this.BX=this.a_=null;a.A.unwrap(this.element,this.rm);clearTimeout(this.Le);return b},wj:function(a,c,e){this._superApply(arguments);
var f="value"===a||"max"===a||"min"===a,g=f||"disabled"===a,k;g&&(k=this.OI());f&&this.sK(k);"converter"===a&&this.uaa(k);g&&this.jL(k)},Cc:function(){return this.options.converter?this._superApply(arguments):g.oj.ojInputNumber.prototype.options.converter},BB:function(){var a=this._superApply(arguments);null==this.options.min&&null==this.options.max||this.d5();return g.extend(this.u9,a)},_GetDefaultStyleClass:function(){return"oj-inputnumber"},gq:{input:function(a){this.Tp(this.element.val(),a)},
keydown:function(a){a.keyCode===g.ui.keyCode.ENTER?(this.L3(a),a.preventDefault()):this.Sj()&&this.Fv(a)&&a.preventDefault()},keyup:function(a){this.bw(a)},blur:function(a){this.L3(a)},"touchstart .oj-inputnumber-button":function(a){this.Sj();this.mz(null,g(a.currentTarget).hasClass("oj-inputnumber-up")?1:-1,a)},"touchend .oj-inputnumber-button":function(a){c=Date.now();this.bw(a)},"touchcancel .oj-inputnumber-button":function(a){c=Date.now();this.bw(a)},"mousedown .oj-inputnumber-button":function(a){this.Dv(a)&&
(this.Sj(),this.mz(null,g(a.currentTarget).hasClass("oj-inputnumber-up")?1:-1,a))},"mouseup .oj-inputnumber-button":function(a){this.Dv(a)&&this.bw(a)},"mouseenter .oj-inputnumber-button":function(a){g(a.currentTarget).hasClass("oj-active")&&this.Dv(a)&&(this.Sj(),this.mz(null,g(a.currentTarget).hasClass("oj-inputnumber-up")?1:-1,a))},"mouseleave .oj-inputnumber-button":function(a){this.Dv(a)&&this.bw(a)}},Ka:{rna:"tooltipDecrement",sna:"tooltipIncrement"},Qr:{readOnly:"oj-read-only"},$a:function(){var a=
this.F(this.Ka.sna),c=this.F(this.Ka.rna),e=this.OI();this.a_.ojButton({label:a});this.BX.ojButton({label:c});this.sK(e);this.jL(e);"boolean"===typeof this.options.readOnly&&this.element.prop("readonly",this.options.readOnly);this.tK("readOnly",this.options.readOnly);this.Aaa("readOnly",this.options.readOnly)},zqa:function(){var a=this.rm.find(".oj-inputnumber-up"),c=this.rm.find(".oj-inputnumber-down"),e=a[0].parentNode;this.a_=a.ojButton({display:"icons",icons:{start:"oj-component-icon oj-inputnumber-up-icon"}});
this.BX=c.ojButton({display:"icons",icons:{start:"oj-component-icon oj-inputnumber-down-icon"}});this.lX=g(e).ojButtonset({focusManagement:"none"})},xC:function(){var a=this.element;this.rm=a.addClass("oj-inputnumber-input").wrap("\x3cspan class\x3d'oj-inputnumber-wrapper'\x3e\x3c/span\x3e").parent().append("\x3cdiv class\x3d'oj-buttonset-width-auto'\x3e\x3cbutton type\x3d'button' class\x3d'oj-inputnumber-button oj-inputnumber-down'\x3e\x3c/button\x3e\x3cbutton type\x3d'button' class\x3d'oj-inputnumber-button oj-inputnumber-up'\x3e\x3c/button\x3e\x3c/div\x3e");
this.oe?(this.rm=g(this.oe).append(this.rm),this.rm.addClass("oj-inputnumber oj-component")):this.rm=this.rm.wrap("\x3cdiv class\x3d'oj-inputnumber oj-component'\x3e\x3c/div\x3e").parent();this.saveType=a.prop("type");a.attr("type","text");this.rm.find(".oj-inputnumber-button").attr("tabIndex","-1");this.zqa()},Fv:function(a){var c=g.ui.keyCode;switch(a.keyCode){case c.UP:return this.mz(null,1,a),!0;case c.DOWN:return this.mz(null,-1,a),!0}return!1},qLa:function(){return"\x3cspan class\x3d'oj-inputnumber-wrapper'\x3e\x3c/span\x3e"},
GKa:function(){return"\x3cdiv class\x3d'oj-buttonset-width-auto'\x3e\x3cbutton type\x3d'button' class\x3d'oj-inputnumber-button oj-inputnumber-down'\x3e\x3c/button\x3e\x3cbutton type\x3d'button' class\x3d'oj-inputnumber-button oj-inputnumber-up'\x3e\x3c/button\x3e\x3c/div\x3e"},Sj:function(){return this.Gia=!0},mz:function(a,c,e){a=a||500;clearTimeout(this.Le);this.Le=this._delay(function(){this.mz(40,c,e)},a);this.kW(c*this.options.step,e)},kW:function(a,c){var e=this.OI(),f=this.options,g=f.min,
k=f.max,f=f.step,l=this.Cza(g,f),e=this.t3(e,a,g,k,f,l);this.Au()&&(this.element.val(e),this.sK(e),this.jL(e),this.Tp(e,c));this.Dc(e,c,this.QB.Bka)},Cza:function(a,c){var e=this.$$(c);null!=a&&(e=Math.max(e,this.$$(a)));return e},$$:function(a){a=a.toString();var c=a.indexOf(".");return-1===c?0:a.length-c-1},t3:function(b,c,e,f,g,k){var l,m;if(0<k)return this.loa(b,c,e,f,g,k);l=null!=e?e:0;try{b=parseFloat(b.toFixed(k))}catch(r){r instanceof TypeError&&(a.r.warn("inputNumber's value after conversion is not a number. \n\r\n                      The converter must convert the value to a Number. coercing using +"),
b=+b)}m=b-l;var t=Math.round(m/g)*g,t=parseFloat(t.toFixed(k));t!==m?(m=0>c?Math.ceil(m/g)*g:Math.floor(m/g)*g,b=l+m+c):b+=c;b=parseFloat(b.toFixed(k));return null!=e&&b<e?e:null!=f&&b>f?(e=Math.floor((f-l)/g)*g+l,e=parseFloat(e.toFixed(k))):b},loa:function(b,c,e,f,g,k){a.o.assert(0<k);k=Math.pow(10,k);return this.t3(b*k,c*k,null!=e?e*k:e,null!=f?f*k:f,null!=g?g*k:g,0)/k},bw:function(){this.Gia&&(clearTimeout(this.Le),this.Gia=!1)},Dv:function(){return a.A.Ke()?500<Date.now()-c:!0},jL:function(a){var c=
this.options,e=c.max,f=c.min,g=this.BX,k=this.a_,l,m;this.rm&&(g||k)&&(l=g.ojButton("option","disabled"),m=k.ojButton("option","disabled"),c.disabled||void 0===a?(l||g.ojButton("disable"),m||k.ojButton("disable")):null!=e&&a>=e?(l&&g.ojButton("enable"),m||k.ojButton("disable")):(null!=f&&a<=f?l||g.ojButton("disable"):l&&g.ojButton("enable"),m&&k.ojButton("enable")))},OI:function(){var a,c;try{c=this.Li()||0,a=this.kK(c)}catch(e){a=void 0}return a},L3:function(a){var c=this.element.val(),e;this.bw();
e=this.OI();this.sK(e);this.jL(e);this.Dc(c,a)},d5:function(){var b=this.options,c=b.min,e=b.max,f=(b=b.translations)?b.numberRange||{}:{},g,k,l,m,r,t,s,b=f.hint||{},n=f.messageDetail||{},f=f.messageSummary||{};null!==b&&(g=b.min||null,k=b.max||null,l=b.inRange||null);null!==n&&(m=n.rangeOverflow||null,r=n.rangeUnderflow||null);null!==f&&(t=f.rangeOverflow||null,s=f.rangeUnderflow||null);c={min:null!=c?c:void 0,max:null!=e?e:void 0,hint:{min:g||null,max:k||null,inRange:l||null},messageDetail:{rangeOverflow:m||
null,rangeUnderflow:r||null},messageSummary:{rangeOverflow:t||null,rangeUnderflow:s||null},converter:this.Cc()};this.u9[a.Pe.VALIDATOR_TYPE_NUMBERRANGE]=a.Da.$w(a.Pe.VALIDATOR_TYPE_NUMBERRANGE).createValidator(c)},Mo:function(a,c){var e;e=null!==c?+c:c;if(isNaN(e))throw Error("ojInputNumber's "+a+" option is not a number");return e},ID:function(a){if(null===a)return 1;a=this.Mo("step",a);if(0>=a)throw Error("Invalid step for ojInputNumber; step must be \x3e 0");if(null===a||0>=a)a=1;return a},tK:function(a,
c){-1!=Object.keys(this.Qr).indexOf(a)&&this.widget().toggleClass(this.Qr[a],!!c)},Aaa:function(a,c){c?this.element.removeAttr("role"):this.element.attr("role","spinbutton")},sK:function(a){this.element.attr({"aria-valuemin":this.options.min,"aria-valuemax":this.options.max,"aria-valuenow":a});this.uaa(a)},uaa:function(a){var c=this.element,e=c.val();this.yB("value",""+a,e)||c.attr({"aria-valuetext":e})},Eca:function(a,c){this.Sj();c?this.kW((a||1)*this.options.step):this.kW((a||1)*-this.options.step);
this.bw()}});var c})();a.Components.Ua("ojInputNumber","editableValue",{properties:{converter:{type:"Object"},max:{type:"number"},min:{type:"number"},placeholder:{type:"string"},rawValue:{type:"string",writeback:!0,readOnly:!0},readOnly:{type:"boolean"},step:{type:"number"},value:{type:"number",writeback:!0}},methods:{destroy:{},refresh:{},stepDown:{},stepUp:{},widget:{}},extension:{_hasWrapper:!0,_innerElement:"input",_widgetName:"ojInputNumber"}});a.Components.register("oj-input-number",a.Components.getMetadata("ojInputNumber"))});