/**
 * Copyright (c) 2014, 2016, Oracle and/or its affiliates.
 * The Universal Permissive License (UPL), Version 1.0
 */
"use strict";
define(["ojs/ojcore","jquery","ojs/ojcomponentcore","ojs/ojdatasource-common"],function(a,g){a.Xg=function(a,b){this.nIa=a;this.ed=b};o_("EmptyNodeSet",a.Xg,a);a.Xg.prototype.getParent=function(){return this.nIa};a.b.g("EmptyNodeSet.prototype.getParent",{getParent:a.Xg.prototype.getParent});a.Xg.prototype.getStart=function(){return this.ed};a.b.g("EmptyNodeSet.prototype.getStart",{getStart:a.Xg.prototype.getStart});a.Xg.prototype.getCount=function(){return 0};a.b.g("EmptyNodeSet.prototype.getCount",
{getCount:a.Xg.prototype.getCount});a.Xg.prototype.getData=function(){return null};a.b.g("EmptyNodeSet.prototype.getData",{getData:a.Xg.prototype.getData});a.Xg.prototype.getMetadata=function(){return null};a.b.g("EmptyNodeSet.prototype.getMetadata",{getMetadata:a.Xg.prototype.getMetadata});a.Yg=function(a,b){this.oc=a;this.ed=b};o_("FlattenedNodeSet",a.Yg,a);a.Yg.prototype.getParent=function(){return this.oc.getParent()};a.b.g("FlattenedNodeSet.prototype.getParent",{getParent:a.Yg.prototype.getParent});
a.Yg.prototype.getStart=function(){return void 0!=this.ed?this.ed:this.oc.getStart()};a.b.g("FlattenedNodeSet.prototype.getStart",{getStart:a.Yg.prototype.getStart});a.Yg.prototype.getCount=function(){void 0===this.NM&&(this.NM=this.L6(this.oc,0),void 0!=this.ed&&(this.NM-=this.ed));return this.NM};a.b.g("FlattenedNodeSet.prototype.getCount",{getCount:a.Yg.prototype.getCount});a.Yg.prototype.L6=function(a,b){var d,e,f,g;d=a.getStart();e=a.getCount();b+=e;if(a.ni)for(f=0;f<e;f++)g=a.ni(f+d),null!=
g&&(b=this.L6(g,b));return b};a.Yg.prototype.getData=function(a){return this.DS(this.oc,a,{index:this.oc.getStart()},this.Nb)};a.b.g("FlattenedNodeSet.prototype.getData",{getData:a.Yg.prototype.getData});a.Yg.prototype.getMetadata=function(a){return this.DS(this.oc,a,{index:this.oc.getStart()},this.YS)};a.b.g("FlattenedNodeSet.prototype.getMetadata",{getMetadata:a.Yg.prototype.getMetadata});a.Yg.prototype.YS=function(a,b){return a.getMetadata(b)};a.Yg.prototype.Nb=function(a,b){return a.getData(b)};
a.Yg.prototype.DS=function(a,b,d,e){var f,g,k,l;f=a.getStart();g=a.getCount();for(k=0;k<g;k++){l=d.index;if(l===b)return e.call(this,a,k+f);d.index=l+1;if(a.ni&&(l=a.ni(k+f),null!=l&&(l=this.DS(l,b,d,e),null!=l)))return l}return null};a.qj=function(a,b,d){this.Ft=a;this.fZ=b;this.eZ=this.rsa(d)};o_("MergedNodeSet",a.qj,a);a.qj.prototype.rsa=function(a){var b,d,e;b=this.Ft.getStart();for(d=b+this.Ft.getCount();b<d;b++)if(e=this.Ft.getMetadata(b).key,a===e)return b;return d-1};a.qj.prototype.getParent=
function(){return this.Ft.getParent()};a.b.g("MergedNodeSet.prototype.getParent",{getParent:a.qj.prototype.getParent});a.qj.prototype.getStart=function(){return this.Ft.getStart()};a.b.g("MergedNodeSet.prototype.getStart",{getStart:a.qj.prototype.getStart});a.qj.prototype.getCount=function(){return this.Ft.getCount()+this.fZ.getCount()};a.b.g("MergedNodeSet.prototype.getCount",{getCount:a.qj.prototype.getCount});a.qj.prototype.getData=function(a){a=this.G7(a);return a.set.getData(a.index)};a.b.g("MergedNodeSet.prototype.getData",
{getData:a.qj.prototype.getData});a.qj.prototype.getMetadata=function(a){a=this.G7(a);return a.set.getMetadata(a.index)};a.b.g("MergedNodeSet.prototype.getMetadata",{getMetadata:a.qj.prototype.getMetadata});a.qj.prototype.G7=function(a){if(a<=this.eZ)return{set:this.Ft,index:a};var b=this.fZ.getCount();return a>this.eZ+b?{set:this.Ft,index:a-b}:{set:this.fZ,index:a-(this.eZ+1)}};a.dh=function(a,b,d,e){this.oc=a;this.bm=b;this.wA=d;this.cm=e};o_("NodeSetWrapper",a.dh,a);a.dh.prototype.getParent=function(){return this.oc.getParent()};
a.b.g("NodeSetWrapper.prototype.getParent",{getParent:a.dh.prototype.getParent});a.dh.prototype.getStart=function(){return null!=this.wA?this.wA.start:this.oc.getStart()};a.b.g("NodeSetWrapper.prototype.getStart",{getStart:a.dh.prototype.getStart});a.dh.prototype.getCount=function(){var a,b;a=this.oc.getStart();b=this.oc.getCount();null!=this.wA&&(this.wA.start>a?b=Math.min(0,b-(this.wA.start-a)):this.wA.start<a&&(b=0));return b};a.b.g("NodeSetWrapper.prototype.getCount",{getCount:a.dh.prototype.getCount});
a.dh.prototype.getData=function(a){return this.oc.getData(a)};a.b.g("NodeSetWrapper.prototype.getData",{getData:a.dh.prototype.getData});a.dh.prototype.getMetadata=function(a){var b;b=this.oc.getMetadata(a);b.index=a;b.parentKey=this.getParent();this.bm.call(null,b.key,b);return b};a.b.g("NodeSetWrapper.prototype.getMetadata",{getMetadata:a.dh.prototype.getMetadata});a.dh.prototype.ni=function(c){return null!=this.cm&&-1!=this.cm.indexOf(this.oc.getMetadata(c).key)||!this.oc.ni||(c=this.oc.ni(c),
null==c)?null:new a.dh(c,this.bm,null,this.cm)};a.b.g("NodeSetWrapper.prototype.getChildNodeSet",{ni:a.dh.prototype.ni});a.ua=function(c,b){this.uf=c;this.qa=b||{};a.ua.u.constructor.call(this)};o_("FlattenedTreeDataSource",a.ua,a);a.b.ta(a.ua,a.ym,"oj.FlattenedTreeDataSource");a.ua.prototype.Init=function(){var c;a.ua.u.Init.call(this);this.uf.on("change",this.Tva.bind(this));this.ZY=parseInt(this.qa.fetchSize,10);isNaN(this.ZY)&&(this.ZY=25);this.Fw=parseInt(this.qa.maxCount,10);isNaN(this.Fw)&&
(this.Fw=500);c=this.qa.expanded;Array.isArray(c)?this.cr=c:("all"===c&&(this.cm=[]),this.cr=[]);this.Zq=[]};a.b.g("FlattenedTreeDataSource.prototype.Init",{Init:a.ua.prototype.Init});a.ua.prototype.handleEvent=function(c,b){return a.ua.u.handleEvent.call(this,c,b)};a.b.g("FlattenedTreeDataSource.prototype.handleEvent",{handleEvent:a.ua.prototype.handleEvent});a.ua.prototype.Wg=function(){delete this.Zq;delete this.cr;delete this.cm;this.uf.off("change");this.uf.Wg&&this.uf.Wg()};a.b.g("FlattenedTreeDataSource.prototype.Destroy",
{Wg:a.ua.prototype.Wg});a.ua.prototype.Tk=function(){return this.ZY};a.ua.prototype.cM=function(){return this.cr};a.b.g("FlattenedTreeDataSource.prototype.getExpandedKeys",{cM:a.ua.prototype.cM});a.ua.prototype.bF=function(a){return null!=this.qa?this.qa[a]:null};a.b.g("FlattenedTreeDataSource.prototype.getOption",{bF:a.ua.prototype.bF});a.ua.prototype.getWrappedDataSource=function(){return this.uf};a.b.g("FlattenedTreeDataSource.prototype.getWrappedDataSource",{getWrappedDataSource:a.ua.prototype.getWrappedDataSource});
a.ua.prototype.KS=function(a){var b,d;b=this.Tk();d=this.Fw;return-1===b?-1===a?d:a:-1===a?Math.min(b,a):b};a.ua.prototype.fp=function(a,b){this.Ry()?this.lsa(a,b):this.ksa(a,b)};a.ua.prototype.ksa=function(c,b){var d,e,f,g,k,l;if(c.start>this.Fg()){d=this.VS();if(0>this.Fg()){c.count=Math.min(d,c.count);this.uf.li(null,c,{success:function(a){this.zs(a,null,0,c,0,b)}.bind(this),error:function(a){this.oq(a,b)}.bind(this)});return}if(0<d){e=this.k7();f=e.parent;g=this.uf.rh(f);k=e.index;l=e.depth;-1===
g||k<g-1?(e=this.KS(g),c.start=k+1,c.count=-1===g?Math.min(e,c.count):Math.min(d,Math.min(Math.min(e,c.count),g-c.start)),this.uf.li(f,c,{success:function(a){this.zs(a,f,l,c,g,b)}.bind(this),error:function(a){this.oq(a,b)}.bind(this)})):k===g-1?(d=new a.Xg(null,c.start),null!=b&&null!=b.success&&b.success.call(null,d)):(d=this.b6(f,l,b,d),d||(d=new a.Xg(null,c.start),null!=b&&null!=b.success&&b.success.call(null,d)));return}}this.rY(c,b)};a.ua.prototype.moveOK=function(a,b,d){return this.uf.moveOK(a,
b,d)};a.b.g("FlattenedTreeDataSource.prototype.moveOK",{moveOK:a.ua.prototype.moveOK});a.ua.prototype.move=function(a,b,d,e){this.uf.move(a,b,d,e)};a.b.g("FlattenedTreeDataSource.prototype.move",{move:a.ua.prototype.move});a.ua.prototype.VS=function(){return this.Fw-(this.Fg()+1)};a.ua.prototype.oq=function(a,b){null!=b&&null!=b.error&&b.error.call(null,a)};a.ua.prototype.zs=function(c,b,d,e,f,g){var k;k=[];c=new a.dh(c,this.jF.bind(this),e);this.Nza(c,b,d,k);-1===f&&0===c.getCount()&&null!=b&&0<
d?(k=this.b6(b,d,g))||null!=g&&null!=g.success&&g.success.call(null,c):0===k.length?null!=g&&null!=g.success&&g.success.call(null,c):(b=[],b.push(k),k={},k.callbacks=g,k.nodeSet=c,k.keys=[],this.Lca(b,k))};a.ua.prototype.rh=function(a){return this.uf.rh(a)};a.b.g("FlattenedTreeDataSource.prototype.getChildCount",{rh:a.ua.prototype.rh});a.ua.prototype.b6=function(a,b,d,e){var f,g,k,l,m,r,t,s,n;void 0===e&&(e=this.VS());this.Bxa()&&(f={queueOnly:!0});g=n=this.KS(-1);for(k=this.Fg()-1;0<=k;k--)if(l=
this.Fj(k),m=l.depth,m<b&&(a=l.parent,r=this.uf.rh(a),l=l.index,(t=-1===r)||l<r-1)){s={};s.start=l+1;t?(s.count=Math.min(e,Math.max(0,g)),f=void 0):s.count=Math.min(e,Math.min(g,r-s.start));if(0==s.count)break;this.uf.li(a,s,{success:function(b){this.zs(b,a,m,s,r,d)}.bind(this),error:function(a){this.oq(a,d)}.bind(this)},f);b=m;g=Math.max(0,g-s.count);if(t||0===m||0===g)break}void 0!=f&&this.uf.li(a,{start:s.count,count:0},{success:function(b){this.zs(b,a,m,s,r,d)}.bind(this),error:function(a){this.oq(a,
d)}.bind(this)});return g!=n};a.ua.prototype.Nza=function(a,b,d,e){var f,g,k,l;f=a.getStart();g=a.getCount();for(k=0;k<g;k++)l=a.getMetadata(f+k),l=l.key,this.BH(l,d,f+k,b),this.mD(l)&&e.push(l)};a.ua.prototype.jF=function(a,b){this.mD(a)&&!b.leaf?b.state="expanded":b.state=b.leaf?"leaf":"collapsed"};a.ua.prototype.lsa=function(a,b){var d={maxCount:this.Fw};0<=this.Fg()&&(d.start=this.Fj(this.Fg()).key);this.uf.Sz(null,{success:function(d){this.kva(d,a,b)}.bind(this),error:function(a){this.oq(a,b)}.bind(this)},
d)};a.ua.prototype.Rq=function(){return this.uf.Rq()};a.b.g("FlattenedTreeDataSource.prototype.getSortCriteria",{Rq:a.ua.prototype.Rq});a.ua.prototype.kva=function(c,b,d){var e,f,g;b.start>this.Fg()?(e=this.VS(),f=Math.min(e,b.count),c=new a.dh(c,this.jF.bind(this),null,this.cm),0<=this.Fg()?(g=this.k7(),e={index:0,found:!1,count:0},this.mV(c,null,0,g,f,e),f=e.index+1):(e={count:0},this.mV(c,null,0,null,f,e),f=0),null!=d&&null!=d.success&&(c=null!=e?0===e.count?new a.Xg(null,b.start):new a.Yg(c,f):
new a.Yg(c),d.success.call(null,c))):this.rY(b,d)};a.ua.prototype.mV=function(a,b,d,e,f,g){var k,l,m,r,t;k=a.getStart();l=a.getCount();for(m=0;m<l&&g.count!=f;m++){r=a.getMetadata(k+m);t=r.key;g.checkDepth&&e.depth===d&&(g.found=!0,g.checkDepth=!1);if(null==e||g.found)this.BH(t,d,k+m,b),g.count+=1,r.state=r.leaf?"leaf":"expanded";null==e||g.found||(t===e.key?r.leaf||this.mD(t)?g.found=!0:g.checkDepth=!0:g.index+=1);a.ni&&this.mD(t)&&(r=a.ni(m),null!=r&&this.mV(r,t,d+1,e,f,g))}};a.ua.prototype.expand=
function(a){this.ag(a)};a.b.g("FlattenedTreeDataSource.prototype.expand",{expand:a.ua.prototype.expand});a.ua.prototype.ag=function(c,b){var d,e,f,g;d=this.uf.rh(c);e=this.KS(d);f=this.Fw;if(this.Fg()+1===f&&(g=this.Uk(c),g==f-1)){this.pY(c,new a.Xg(c,0),0,b);return}0==e?this.pY(c,new a.Xg(c,0),0,b):this.uf.li(c,{start:0,count:e},{success:function(a){this.pY(c,a,d,b)}.bind(this),error:function(){this.dHa(c)}.bind(this)})};a.ua.prototype.collapse=function(a){var b,d,e,f,g;b=this.Uk(a)+1;d=this.Fj(b-
1);e=0;d=d.depth;f=this.Fg();for(g=b;g<f+1;g++){var k=this.Fj(g).depth;if(k>d)e+=1;else if(k==d)break}if(0!=e){this.Ry()?this.cm.push(a):this.EAa(a);f=[];for(d=0;d<e;d++)f.push({key:this.Fj(b+d).key,index:b+d});this.Paa(b,e);this.BN(f)}this.handleEvent("collapse",{rowKey:a})};a.b.g("FlattenedTreeDataSource.prototype.collapse",{collapse:a.ua.prototype.collapse});a.ua.prototype.mD=function(a){return this.Ry()?this.cm&&0<this.cm.length?-1===this.D6(a):!0:this.cr&&0<this.cr.length?-1<this.T6(a):!1};a.ua.prototype.D6=
function(a){return this.i7(this.cm,a)};a.ua.prototype.T6=function(a){return this.i7(this.cr,a)};a.ua.prototype.i7=function(a,b){var d,e;e=-1;for(d=0;d<a.length;d++)a[d]===b&&(e=d);return e};a.ua.prototype.EAa=function(a){a=this.T6(a);-1<a&&this.cr.splice(a,1)};a.ua.prototype.yAa=function(a){a=this.D6(a);-1<a&&this.cm.splice(a,1)};a.ua.prototype.dHa=function(a){this.handleEvent("expand",{rowKey:a})};a.ua.prototype.pY=function(c,b,d,e){var f,g,k,l,m,r,t,s,n,p,q;b=new a.dh(b,this.jF.bind(this));g=f=
this.Uk(c)+1;k=b.getStart();l=b.getCount();m=this.Fj(f-1);r=m.depth+1;s=[];for(n=k;n<l;n++)k=b.getMetadata(n),t=k.key,this.mD(t)&&s.push(t),this.w9(f,k,m.key,n,r),f++;this.Ry()?this.yAa(c):-1===this.cr.indexOf(c)&&this.cr.push(c);void 0!=e&&(p=e.queue,q=e.prevNodeSetInfo);void 0!=q&&(b=new a.qj(q.nodeSet,b,c));if(0!=s.length||void 0!==p&&0!=p.length)void 0===p&&(p=[]),0<s.length&&p.push(s),void 0===q&&(q={},q.firstIndex=g,q.firstKey=c,q.keys=[]),q.nodeSet=b,q.keys.push(c),this.Lca(p,q);else{if(void 0!=
q){e=q.callbacks;if(null!=e){e.success.call(null,b);return}this.zM(q.firstIndex,q.firstKey,b)}else this.zM(g,c,b);b=this.Fw;-1===d&&l===this.Tk()||d>l||f==b?this.NR(f):this.Fg()>=b&&this.NR(b);if(void 0!=q)for(d=0;d<q.keys.length;d++)this.handleEvent("expand",{rowKey:q.keys[d]});this.handleEvent("expand",{rowKey:c})}};a.ua.prototype.Lca=function(a,b){var d,e;d=a[a.length-1];e=d.shift();0===d.length&&a.pop();this.ag(e,{prevNodeSetInfo:b,queue:a})};a.ua.prototype.w9=function(a,b,d,e,f){b=b.key;a<=this.Fg()?
this.BH(b,f,e,d,a):this.BH(b,f,e,d)};a.ua.prototype.NR=function(a,b){var d;void 0==b&&(b=this.Fg()+1-a);d=[];for(var e=0;e<b;e++)d.push({key:this.Fj(a+e).key,index:a+e});this.Paa(a,b);this.BN(d)};a.ua.prototype.Tva=function(a){var b,d,e;b=a.operation;d=a.parent;d=Array.isArray(d)?d[d.length-1]:d;e=a.index;"insert"===b?this.Cva(d,e,a.data):"delete"===b?this.Wua(d,e):"refresh"===b&&this.iwa(d)};a.ua.prototype.Cva=function(a,b,d){var e,f;e=this.Uk(a);f=this.Fj(e).depth+1;e=e+b+1;d=d.getMetadata(d.getStart());
this.w9(e,d,a,b,f)};a.ua.prototype.Wua=function(c,b){var d,e,f,g,k;d=this.Uk(c);e=this.Fj(d);d+=b;f=this.Fj(d);a.o.assert(f.parent===e&&f.depth===e.depth+1);e=d+1;for(g=this.Fg();e<=g;){k=this.Fj(e);if(k.depth!=f.depth)break;e++}this.NR(d,1)};a.ua.prototype.iwa=function(a){null==a&&this.refresh()};a.ua.prototype.Ry=function(){var a=this.uf.getCapability("fetchDescendants");return void 0!=this.cm&&null!=a&&"disable"!=a};a.ua.prototype.Bxa=function(){return"enable"===this.uf.getCapability("batchFetch")};
a.ua.prototype.refresh=function(){this.wpa()};a.ua.prototype.Uk=function(a){var b,d,e;b=this.Fg();for(d=0;d<=b;d++)if(e=this.Fj(d),e.key==a)return d;return-1};a.ua.prototype.getKey=function(a){return 0>a||a>this.Fg()?null:this.Fj(a).key};a.ua.prototype.fGa=function(){return{start:0,end:this.Fg()+1}};a.ua.prototype.YFa=function(a){var b;b=[];a=this.Uk(a);for(a=this.an(a);null!=a;)b.push(a),a=this.Uk(a),a=this.an(a);return b.reverse()};a.ua.prototype.rY=function(a,b){null!=b&&null!=b.error&&b.error.call(null)};
a.ua.prototype.zM=function(){a.o.ld()};a.ua.prototype.BN=function(){a.o.ld()};a.ua.prototype.Fg=function(){return this.Zq.length-1};a.ua.prototype.k7=function(){return this.Zq[this.Fg()]};a.ua.prototype.Fj=function(a){return this.Zq[a]};a.ua.prototype.an=function(a){a=this.Zq[a];return null!=a?a.parent:null};a.ua.prototype.BH=function(a,b,d,e,f){var g={};g.key=a;g.depth=b;g.index=d;g.parent=e;void 0===f?this.Zq.push(g):this.Zq.splice(f,0,g)};a.ua.prototype.Paa=function(a,b){this.Zq.splice(a,b)};a.ua.prototype.wpa=
function(){this.Zq.length=0};a.ua.prototype.getCapability=function(a){return this.uf.getCapability(a)};a.b.g("FlattenedTreeDataSource.prototype.getCapability",{getCapability:a.ua.prototype.getCapability});a.Na("oj.ojRowExpander",g.oj.baseComponent,{version:"1.0.0",widgetEventPrefix:"oj",options:{context:null,expand:null,collapse:null},ap:{root:"oj-rowexpander",icon:"oj-component-icon",clickable:"oj-clickable-icon-nocontext",expand:"oj-rowexpander-expand-icon",collapse:"oj-rowexpander-collapse-icon",
leaf:"oj-rowexpander-leaf-icon",lazyload:"oj-rowexpander-lazyload-icon",toucharea:"oj-rowexpander-touch-area",indent:"oj-rowexpander-indent",iconspacer:"oj-rowexpander-icon-spacer",depth0:"oj-rowexpander-depth-0",depth1:"oj-rowexpander-depth-1",depth2:"oj-rowexpander-depth-2",depth3:"oj-rowexpander-depth-3",depth4:"oj-rowexpander-depth-4",depth5:"oj-rowexpander-depth-5",depth6:"oj-rowexpander-depth-6",depth7:"oj-rowexpander-depth-7"},Mz:{fB:7,BO:53},_ComponentCreate:function(){this._super();this.element.addClass(this.ap.root);
this.h9()},h9:function(){var a=this,b;b=this.options.context;this.R="function"===typeof b.component?b.component("instance"):b.component;this.xd=b.datasource;this.depth=b.depth;this.Zk=b.state;this.qr=b.key;this.index=b.index;this.rN=b.parentKey;this.Wna();this.Vna();this.MK();"expanded"===this.Zk||"collapsed"===this.Zk?(g(this.WZ).on("touchend",function(b){b.preventDefault();a.pS()}),g(this.WZ).on("click",function(b){b.preventDefault();a.pS()}),g(this.element).on("keypress",function(b){var e=b.keyCode||
b.which;if(e===g.ui.keyCode.ENTER||e===g.ui.keyCode.SPACE)a.pS(),b.preventDefault(),b.target.focus()}),this.xM=this.y8.bind(this),g(this.R.element).on("ojkeydown",this.xM),this.Zfa=this.eva.bind(this),this.Ufa=this.Lua.bind(this),this.xd.on("expand",this.Zfa,this),this.xd.on("collapse",this.Ufa,this)):"leaf"===this.Zk&&(this.xM=this.y8.bind(this),g(this.R.element).on("ojkeydown",this.xM),g(this.icon).attr("tabindex",-1));this.Sfa=this.Aua.bind(this);g(this.R.element).on("ojbeforecurrentcell",this.Sfa)},
refresh:function(){this.element.empty();this.h9()},_destroy:function(){g(this.R.element).off("ojkeydown",this.xM);g(this.R.element).off("ojbeforecurrentcell",this.Sfa);this.xd.off("expand",this.Zfa,this);this.xd.off("collapse",this.Ufa,this);this.element.removeClass(this.ap.root);this.element.empty()},_setOption:function(a,b,d){this._super(a,b,d);"context"==a&&this.refresh()},Wna:function(){var a,b;b=this.depth-1;if(b<this.Mz.fB)this.yQ(b);else{for(a=1;a<=b/this.Mz.fB;a++)this.yQ(this.Mz.fB);a=b%
this.Mz.fB;a<this.Mz.fB&&this.yQ(a)}},yQ:function(a){a=g(document.createElement("span")).addClass(this.ap.indent).addClass(this.ap["depth"+a]);this.element.append(a)},Vna:function(){var a=g(document.createElement("div")).addClass(this.ap.iconspacer);this.WZ=g(document.createElement("div")).addClass(this.ap.toucharea);this.icon=g(document.createElement("a")).attr("href","#").attr("aria-labelledby",this.j7()).addClass(this.ap.icon).addClass(this.ap.clickable);this.element.append(a.append(this.WZ.append(this.icon)))},
Ru:function(a){this.icon.addClass(this.ap[a])},Uv:function(a){this.icon.removeClass(this.ap[a])},MK:function(){switch(this.Zk){case "leaf":this.Uv("icon");this.Uv("clickable");this.Ru("leaf");break;case "collapsed":this.Ru("expand");this.JH(!1);break;case "expanded":this.Ru("collapse");this.JH(!0);break;case "loading":this.Uv("clickable"),this.Ru("lazyload")}},yV:function(){switch(this.Zk){case "leaf":this.Uv("leaf");this.Ru("icon");this.Ru("clickable");break;case "collapsed":this.Uv("expand");break;
case "expanded":this.Uv("collapse");break;case "loading":this.Uv("lazyload"),this.Ru("clickable")}},Aua:function(a,b){var d,e;null!=b.currentCell&&(d="cell"==b.currentCell.type?b.currentCell.keys.row:b.currentCell.key,null!=b.previousValue&&(e="cell"==b.previousCurrentCell.type?b.previousCurrentCell.keys.row:b.previousCurrentCell.key),this.qr===d&&e!=d&&this.R.FK&&(d=this.F("accessibleRowDescription",{level:this.depth,num:this.index+1,total:this.xd.getWrappedDataSource().rh(this.rN)}),e="collapsed"===
this.Zk?this.F("accessibleStateCollapsed"):"expanded"===this.Zk?this.F("accessibleStateExpanded"):"",this.R.FK({context:d,state:e})))},y8:function(c,b){var d,e,f;if(this.qr===b.rowKey&&(c=c.originalEvent,d=c.keyCode||c.which,a.A.Yq(c))){if(d==g.ui.keyCode.RIGHT&&"collapsed"===this.Zk)return this.OU(),this.xd.expand(this.qr),!1;if(d==g.ui.keyCode.LEFT&&"expanded"===this.Zk)return this.OU(),this.xd.collapse(this.qr),!1;if(c.altKey&&d==this.Mz.BO&&this.R.FK){d=this.xd.YFa(this.qr);if(null!=d&&0<d.length)for(e=
[],f=0;f<d.length;f++)e.push({key:d[f],label:this.F("accessibleLevelDescription",{level:f+1})});d=this.F("accessibleRowDescription",{level:this.depth,num:this.index+1,total:this.xd.getWrappedDataSource().rh(this.rN)});this.R.FK({context:d,state:"",ancestors:e})}}return!0},OU:function(){this.yV();this.Zk="loading";this.MK()},eva:function(a){a=a.rowKey;a===this.qr&&(this.yV(),this.Zk="expanded",this.MK(),this.JH(!0),this._trigger("expand",null,{rowKey:a}))},Lua:function(a){a=a.rowKey;a===this.qr&&(this.yV(),
this.Zk="collapsed",this.MK(),this.JH(!1),this._trigger("collapse",null,{rowKey:a}))},pS:function(){var a=this.Zk;this.OU();"collapsed"===a?this.xd.expand(this.qr):"expanded"===a&&this.xd.collapse(this.qr)},JH:function(a){this.icon.attr("aria-expanded",a)},getNodeBySubId:function(a){if(null==a)return this.element?this.element[0]:null;a=a.subId;return"oj-rowexpander-disclosure"!==a&&"oj-rowexpander-icon"!==a||null==this.icon?null:this.icon.get(0)},getSubIdByNode:function(a){return a===this.icon.get(0)?
{subId:"oj-rowexpander-disclosure"}:null},Mr:function(){this._super();this.icon.attr("aria-labelledby",this.j7())},j7:function(){return this.element.parent().closest("[id]").attr("id")}});a.Components.Ua("ojRowExpander","baseComponent",{properties:{context:{type:"Object"}},methods:{getNodeBySubId:{},getSubIdByNode:{},refresh:{}},extension:{_widgetName:"ojRowExpander"}});a.Components.register("oj-row-expander",a.Components.getMetadata("ojRowExpander"))});