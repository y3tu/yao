(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-07ba8663","chunk-2eca7df7"],{"0a08":function(t,e,n){"use strict";n.r(e);var i=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("el-dialog",{staticClass:"update-avatar",attrs:{title:t.$t("common.changeAvatar"),width:t.width,top:"50px","close-on-click-modal":!1,"close-on-press-escape":!1,visible:t.isVisible},on:{"update:visible":function(e){t.isVisible=e}}},[n("el-tabs",{staticClass:"avatar-tabs",model:{value:t.activeName,callback:function(e){t.activeName=e},expression:"activeName"}},[n("el-tab-pane",{attrs:{label:t.$t("common.hthz"),name:"first"}},[t._l(t.hthz,(function(e,i){return[n("div",{key:i,staticClass:"avatar-wrapper"},[n("img",{attrs:{alt:t.$t("common.ctc"),src:t.resolveAvatar(e)},on:{click:function(n){return t.change(e)}}})])]}))],2),t._v(" "),n("el-tab-pane",{attrs:{label:t.$t("common.al"),name:"second"}},[t._l(t.al,(function(e,i){return[n("div",{key:i,staticClass:"avatar-wrapper"},[n("img",{attrs:{alt:t.$t("common.ctc"),src:t.resolveAvatar(e)},on:{click:function(n){return t.change(e)}}})])]}))],2),t._v(" "),n("el-tab-pane",{attrs:{label:t.$t("common.lm"),name:"third"}},[t._l(t.lm,(function(e,i){return[n("div",{key:i,staticClass:"avatar-wrapper"},[n("img",{attrs:{alt:t.$t("common.ctc"),src:t.resolveAvatar(e)},on:{click:function(n){return t.change(e)}}})])]}))],2)],1)],1)},a=[],s=["default.jpg","1d22f3e41d284f50b2c8fc32e0788698.jpeg","2dd7a2d09fa94bf8b5c52e5318868b4d9.jpg","2dd7a2d09fa94bf8b5c52e5318868b4df.jpg","8f5b60ef00714a399ee544d331231820.jpeg","17e420c250804efe904a09a33796d5a10.jpg","17e420c250804efe904a09a33796d5a16.jpg","87d8194bc9834e9f8f0228e9e530beb1.jpeg","496b3ace787342f7954b7045b8b06804.jpeg","595ba7b05f2e485eb50565a50cb6cc3c.jpeg","964e40b005724165b8cf772355796c8c.jpeg","5997fedcc7bd4cffbd350b40d1b5b987.jpg","5997fedcc7bd4cffbd350b40d1b5b9824.jpg","a3b10296862e40edb811418d64455d00.jpeg","a43456282d684e0b9319cf332f8ac468.jpeg","bba284ac05b041a8b8b0d1927868d5c9x.jpg","c7c4ee7be3eb4e73a19887dc713505145.jpg","ff698bb2d25c4d218b3256b46c706ece.jpeg"],c=["cnrhVkzwxjPwAaCfPbdc.png","BiazfanxmamNRoxxVxka.png","gaOngJwsRYRaVAuXXcmB.png","WhxKECPNujWoWEFNdnJE.png","ubnKSIfAJTxIgXOKlciN.png","jZUIxmJycoymBprLOUbT.png"],o=["19034103295190235.jpg","20180414165920.jpg","20180414170003.jpg","20180414165927.jpg","20180414165754.jpg","20180414165815.jpg","20180414165821.jpg","20180414165827.jpg","20180414165834.jpg","20180414165840.jpg","20180414165846.jpg","20180414165855.jpg","20180414165909.jpg","20180414165914.jpg","20180414165936.jpg","20180414165942.jpg","20180414165947.jpg","20180414165955.jpg"],r={name:"Avatar",props:{dialogVisible:{type:Boolean,default:!1}},data:function(){return{activeName:"first",screenWidth:0,updating:!1,width:this.initWidth(),hthz:s,al:c,lm:o}},computed:{isVisible:{get:function(){return this.dialogVisible},set:function(){this.close()}}},mounted:function(){var t=this;window.onresize=function(){return function(){t.width=t.initWidth()}()}},methods:{resolveAvatar:function(t){return n("a0fc")("./".concat(t))},change:function(t){var e=this;this.updating?this.$message({message:this.$t("tips.updating"),type:"warning"}):(this.updating=!0,this.$put("upms/user/avatar",{avatar:t}).then((function(){e.$emit("success",t),e.updating=!1})).catch((function(t){console.error(t),e.$message({message:e.$t("tips.updateFailed"),type:"error"}),e.updating=!1})))},initWidth:function(){return this.screenWidth=document.body.clientWidth,this.screenWidth<991?"90%":this.screenWidth<1400?"55%":"820px"},close:function(){this.$emit("close")}}},u=r,d=(n("4e12"),n("0c7c")),l=Object(d["a"])(u,i,a,!1,null,"31df8064",null);e["default"]=l.exports},"3d86":function(t,e){t.exports="data:image/png;base64,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"},"4e12":function(t,e,n){"use strict";var i=n("a857"),a=n.n(i);a.a},"6c58":function(t,e,n){t.exports=n.p+"static/img/dingtalk.5c4c26da.png"},7338:function(t,e,n){var i={"./dingtalk.png":"6c58","./gitee.png":"7e98","./github.png":"d1b0","./microsoft.png":"3d86","./qq.png":"ee7c","./tencent_cloud.png":"fff5"};function a(t){var e=s(t);return n(e)}function s(t){var e=i[t];if(!(e+1)){var n=new Error("Cannot find module '"+t+"'");throw n.code="MODULE_NOT_FOUND",n}return e}a.keys=function(){return Object.keys(i)},a.resolve=s,t.exports=a,a.id="7338"},"7e98":function(t,e){t.exports="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAV4AAAFeCAMAAAD69YcoAAACr1BMVEXHHSPHHyXOPELXX2Pff4LijI/lmJroo6brr7Huurzwxsfz0dP12tv23N3xyMnqrK7bbnLKKzDHHiTRSU7ccnXklZjtuLn9+fn////88/Piio3IJCnQQ0jefYH78PHyzs/MMzjLLTPcdXnrsbP56uv229zKKi/IIijZZWntuLr9+vrtt7nTTlPnoaT77+/VVVrIIyneen323d7uvL7JJizggoX56On99/j67e3SS0/jkJP78fLYYmbYZGj45eXaa2/OO0Duvb/ii47TUVX23t/ghYj++/v45ubhhonIISfrrrDJJSvvv8Dyzc7NNjzQQUbOPEH66+zMMTb34OHKKS/009Twxcbss7XmnJ/ZaGz78vLnnqDNNTvstbfWW2D339/OOj/YY2fSTFHUU1jJKC7vwMLvwMHWXGH+/v734eLLLjT119jOOT7+/Pzll5rJJy3kk5b+/f356er55+f01NXtubveen7MMjf45OXaam7RR0zVVlvssrTXYGTpqavghIfIJCrfgIPuvsDccnbWW1/01tfNOD3wxMbzz9DKLDHPQEXXYWXXXWHmmp3yzM389fXpp6nVWFzrsLLmnaD34uPllpnWWl7qq63RSE3hiYzbb3PXXmLYY2jbcXTTUFXKLDLMNDrLMDXz0NH9+PjhiIv89PTPP0TVWV3RRkvcdHj55+jZZmrpqazyy8zVV1vabHDbcHT12NnUVFn67u7LLzXjjpHopafqra/yyszhh4rabXHopKfefIDHICbffoHlmZvPPkPstrjggob33+Dee3/QQkfnoKLdd3vddnr77/DnoaPgg4fz0NLmm57SSk/PPUL89vbkkpXQREnww8Xnn6HSS1D00tPNNzzZaW3vwcPTT1TjkZT12drppqjjj5LklJfuu7301dblmZzqqq3TTVJzvUFwAAAGEElEQVR4AezBAQEAEAAAIADg/10XDKgCPwAAAAAAAAAAAAAAAAAAAAAAAAAAiCmX2vqYa5/72KUH44ACAAiiY8TOxrZt2z82+q8iVdzkX+ZeCTv756pqauvq/0fYhsam5paqVsqmrb3Dvm1nV0s3ZdXT65y2r39gkFIbGnZtOzI6Nk7pTXjGnZyaxsKMX9vZuXlcLLjFXVxaxsfKolXc4fZVrKw5nbu+gZlNn7pb29jZcYm7u4dK8s7ur6CSvAeHyCTv0TEqydu3h0zyHpwgk7ynxySvytk5Msl7cUnyyvRdkbwy11Ukr8zNCckrU9ySvDJ39ySvzMMjySsz+0TyyjQ8k7w6LySvzivJq1O8Ja/OSCXJq/NO8upskbw6H93JK/RJ8up8kbw6s9/JK7RP8ur8rPDL3j1oTZIuURiOtq3dtm3btm3btm3btm3b5oWcxTOe+SL/jqrK7P1cwrt2MRUsXSMqb3EETLdIqtsdAZOmRyTl7YmA6RVJdcsiaHpHUt7UHK+hDH0QLH37RVLeuAiW/gMiqW6VgQiS0oMGSyQZgp9haLthpcJu+IiRo0ZLZBmDqEmTuNzYcS2E/tZ4REXpCRPryz+jSfAux+Rc8m9oSlp4VX7qFPl3VBle1Rst/4WmwZs00+U/Uf188KRpdflvNAOezJwlDigVvJhdQlzQHHhQY664oBYDoVd+njih+dAbukDc0ELoTRZHlABqi8QVLYbWwHHiiDpAbYm4oqX68UYXVzQKWsvEGS23vLUo9YLSGHFHK6C0UtzRQCitEmdUH0pp6oszagalOeKOokNptbijNVBaK2R4Uvo6cUfrobRB3NFYKC0Vd7QRStXFHaWE0iYh5mVePeYl5mVe5iXmDXrezVtCK0vuXybv1rXbtiPU+gxb3emXyLtjJ8Ik8a7A552SF+FTfnfQ82ZHOC3eE+y8exFexeoGOe+UfQizUUHOux/hNiLIeQ8g7DoEOO9BhN2hAOdtjrA7HOC8tRF2KQOct1LA83K9G7lePeblerle5mVevjlwvczLvHxz4HqZl3mZl++9XC/zMi/zMi/zMi/zMi/z8nsv12vtCPMyL/MyL/OaYF4DzHsUhpi3JwwxbzUYYt4MCLt1Ac4r+WGIeZuUhx3mld75YId55dhx2GFeOdG+NMwwr0iJPCdPnfYgyW+yFz9TCJ4kDWReA7POVmJeS7nyM6+l3AmgdY553VVnXlM5mdfSeV6ybekClCozr8J+y7u8M28nKI1nXoVjUFrDvJbPWq/CvAoboJSVeRUqQ+ki8ypcgs5iYV6Fy9DZx7waE6CTmXk1WkLnAPMqFMkHnTrMq9ANSl2ZV2EqlK4wr8Js6OS7yLzuktWCTkJxx7yjoHSVed3VHQOla8zrbi+01jCvs2jXoVQoOfM6Wwe7t17m7TcGWjd8mre6hNzF+FC76dO8MSTkbkFtmPg0720JtTvQW+3XvKclxCqngV4nv+a9m15CKfc9eDAsml/z4r6E0IAH8OKh+DZvpi4SKnWn34Unj/ybF0dzSUi0uF8b3uQUH+dFzsdiL/rK8vAqna/z4snTuWLp2ajs++Dd0AH+zgvUev70xpGfbuOlky/iNphzF1HTUfyb1wdeMq+h2cK8hlYxr6FXwryG2jKvoWLJmddQBWFeO72Eee3k28W8hi4L89rJ0YJ5DaUU5rWTWJjXTqbXzGvojTCvnUnCvHY6D2ZeO/leCvPauS/Ma2eJMK+dtxeZ107/FsK8Zko1EuY1s3ONMK+Z0jOEec3sHC/Ma6b8O2FeM2VSCPOaGZZBmNdM88fCvGYazBXmtdLnfXJhXivtuooZ5v1wRZjXSp+Pg4V5rXQuLFaYd+DKKcK8VkakEN84Ap8pU/Ki+McC+MrddIPFTzLAR9p9yiU+Mxx+sfPzZvGdy/CHhnsHiw/1S4vId/xhBrHB7w6Lv7zJLf51EpGrz6vP43OLv21Mi0hU+uukyickAE582xdRky2/7Xud+U2SS3BkWH8kZdgd2b/qx80OLYLUVeN/7cGBAAAAAACQ/2sjqKqqqqqqqqqqqqqqqqqqqqqqqqqqqgLc9v3N7Mig4AAAAABJRU5ErkJggg=="},"7faf":function(t,e,n){"use strict";n.r(e);var i=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("el-card",{staticStyle:{"margin-bottom":"20px"}},[n("div",{staticClass:"clearfix",attrs:{slot:"header"},slot:"header"},[n("span",[t._v(t._s(t.$t("common.aboutMe")))])]),t._v(" "),n("div",{staticClass:"user-profile"},[n("div",{staticClass:"box-center"},[n("pan-thumb",{attrs:{image:t.avatar,height:"100px",width:"100px",hoverable:!1}},[n("el-link",{staticClass:"change-avatar",attrs:{type:"primary"},on:{click:function(e){t.dialogVisible=!0}}},[t._v(t._s(t.$t("common.changeAvatar")))])],1)],1),t._v(" "),n("div",{staticClass:"box-center"},[n("div",{staticClass:"user-name text-center"},[t._v(t._s(t.user.username))]),t._v(" "),n("div",{staticClass:"user-role text-center text-muted"},[n("span",[t._v(t._s(t.user.departmentName?t.user.departmentName:t.$t("common.noDepartment")))]),t._v(" · "),n("span",[t._v(t._s(t.user.roleName?t.user.roleName:t.$t("common.noRole")))])])])]),t._v(" "),n("div",{staticClass:"user-bio"},[n("div",{staticClass:"user-education user-bio-section"},[n("div",{staticClass:"user-bio-section-header"},[n("el-icon",{staticClass:"el-icon-tickets"}),n("span",[t._v(t._s(t.$t("table.user.desc")))])],1),t._v(" "),n("div",{staticClass:"user-bio-section-body"},[n("div",{staticClass:"text-muted"},[t._v("\n          "+t._s(t.user.description?t.user.description:t.$t("tips.nothing"))+"\n        ")])])]),t._v(" "),n("div",{staticClass:"user-education user-bio-section"},[n("div",{staticClass:"user-bio-section-header"},[n("el-icon",{staticClass:"el-icon-connection"}),n("span",[t._v(t._s(t.$t("table.user.social")))])],1),t._v(" "),n("div",{staticClass:"user-bio-section-body"},[n("div",{staticClass:"text-muted"},[t._l(t.logo,(function(e,i){return[n("div",{key:i,staticClass:"logo-wrapper"},[e.bind?n("img",{class:{radius:e.radius},attrs:{src:t.resolveLogo(e.img),alt:"",title:t.$t("common.unbind")},on:{click:function(n){return t.unbind(e.name)}}}):n("img",{staticClass:"unbind",class:{radius:e.radius},attrs:{src:t.resolveLogo(e.img),alt:"",title:t.$t("common.bind")},on:{click:function(n){return t.bind(e.name)}}})])]}))],2)])])]),t._v(" "),n("avatar",{attrs:{"dialog-visible":t.dialogVisible},on:{close:function(e){t.dialogVisible=!1},success:t.changeSuccess}})],1)},a=[],s=(n("1c01"),n("58b2"),n("8e6e"),n("d25f"),n("456d"),n("bd86")),c=(n("7f7f"),n("ac6a"),n("f3e2"),function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"pan-item",style:{zIndex:t.zIndex,height:t.height,width:t.width}},[n("div",{staticClass:"pan-info"},[n("div",{staticClass:"pan-info-roles-container"},[t._t("default")],2)]),t._v(" "),n("img",{staticClass:"pan-thumb",attrs:{src:t.image}})])}),o=[],r=(n("c5f6"),{name:"PanThumb",props:{image:{type:String,required:!0},zIndex:{type:Number,default:1},width:{type:String,default:"150px"},height:{type:String,default:"150px"}}}),u=r,d=(n("cb53"),n("0c7c")),l=Object(d["a"])(u,c,o,!1,null,"07f96e6a",null),A=l.exports,g=n("0a08"),b=n("83d6");function p(t,e){var n=Object.keys(t);if(Object.getOwnPropertySymbols){var i=Object.getOwnPropertySymbols(t);e&&(i=i.filter((function(e){return Object.getOwnPropertyDescriptor(t,e).enumerable}))),n.push.apply(n,i)}return n}function m(t){for(var e=1;e<arguments.length;e++){var n=null!=arguments[e]?arguments[e]:{};e%2?p(Object(n),!0).forEach((function(e){Object(s["a"])(t,e,n[e])})):Object.getOwnPropertyDescriptors?Object.defineProperties(t,Object.getOwnPropertyDescriptors(n)):p(Object(n)).forEach((function(e){Object.defineProperty(t,e,Object.getOwnPropertyDescriptor(n,e))}))}return t}var f={components:{PanThumb:A,Avatar:g["default"]},props:{user:{type:Object,default:function(){return{}}}},data:function(){return{dialogVisible:!1,socialLoginUrl:b["socialLoginUrl"],logo:[{img:"gitee.png",name:"gitee",bind:!1,radius:!0},{img:"github.png",name:"github",bind:!1,radius:!0},{img:"tencent_cloud.png",name:"tencent_cloud",bind:!1,radius:!0},{img:"qq.png",name:"qq",bind:!1,radius:!1},{img:"dingtalk.png",name:"dingtalk",bind:!1,radius:!0},{img:"microsoft.png",name:"microsoft",bind:!1,radius:!1}],oauthType:"",page:{width:.5*window.screen.width,height:.5*window.screen.height}}},computed:{avatar:function(){return n("a0fc")("./".concat(this.user.avatar))}},mounted:function(){this.findUserConnections()},destroyed:function(){window.removeEventListener("message",this.resolveBindResult)},methods:{changeSuccess:function(t){this.dialogVisible=!1,this.$message({message:this.$t("tips.updateSuccess"),type:"success"}),this.user.avatar=t,this.$store.commit("account/setUser",this.user)},resolveLogo:function(t){return n("7338")("./".concat(t))},findUserConnections:function(){var t=this;this.$get("auth/social/connections/".concat(this.user.username)).then((function(e){var n=e.data;n.forEach((function(e){t.logo.forEach((function(t){t.name===e.providerName.toLowerCase()&&(t.bind=!0)}))}))}))},bind:function(t){this.oauthType=t;var e="".concat(this.socialLoginUrl,"/").concat(t,"/bind");window.open(e,"newWindow","height=".concat(this.page.height,", width=").concat(this.page.width,", top=10%, left=10%, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no, status=no")),window.addEventListener("message",this.resolveBindResult,!1)},resolveBindResult:function(t){var e=this,n=t.data.data;n.token=null,this.$post("auth/social/bind",m({},n,{bindUsername:this.user.username})).then((function(t){e.logo.forEach((function(t){t.name===e.oauthType&&(t.bind=!0)})),e.$message({message:e.$t("common.bindSuccess"),type:"success"})}))},unbind:function(t){var e=this;this.$confirm(this.$t("common.confirmUnbind"),this.$t("common.tips"),{confirmButtonText:this.$t("common.confirm"),cancelButtonText:this.$t("common.cancel"),type:"warning"}).then((function(){e.$delete("auth/social/unbind",{bindUsername:e.user.username,oauthType:t}).then((function(){e.logo.forEach((function(e){e.name===t&&(e.bind=!1)})),e.$message({message:e.$t("common.unbindSuccess"),type:"success"})}))})).catch((function(){}))}}},v=f,h=(n("b30b"),Object(d["a"])(v,i,a,!1,null,"55a0f7ec",null));e["default"]=h.exports},"9e62":function(t,e,n){},a857:function(t,e,n){},b30b:function(t,e,n){"use strict";var i=n("e8dd"),a=n.n(i);a.a},cb53:function(t,e,n){"use strict";var i=n("9e62"),a=n.n(i);a.a},d1b0:function(t,e,n){t.exports=n.p+"static/img/github.1991b9f7.png"},e8dd:function(t,e,n){},ee7c:function(t,e,n){t.exports=n.p+"static/img/qq.24758d96.png"},fff5:function(t,e,n){t.exports=n.p+"static/img/tencent_cloud.edcd8ad5.png"}}]);