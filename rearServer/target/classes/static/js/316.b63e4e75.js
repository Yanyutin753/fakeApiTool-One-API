"use strict";(self["webpackChunkfakeapitoolweb"]=self["webpackChunkfakeapitoolweb"]||[]).push([[316],{8316:function(e,a,t){t.r(a),t.d(a,{default:function(){return U}});var o=t(6773),l=(t(487),t(3164)),s=(t(6335),t(6252));const r=e=>((0,s.dD)("data-v-6d590448"),e=e(),(0,s.Cn)(),e),n={class:"content"},d={class:"login-container"},c=["src"],m=r((()=>(0,s._)("h1",null,"fakeApiTool用户登录",-1))),i={class:"container"},u={style:{display:"block",transform:"translate(0vw, 2vh)"}},v={style:{display:"block",transform:"translate(0vw, 2vh)","margin-top":"5vh"}},p={style:{display:"block",transform:"translate(0.5vw, 2.5vh)","margin-top":"5vh"}},h={style:{display:"block",transform:"translate(0vw, 0.5vh)","margin-top":"5vh"}},g=(0,s.uE)('<div class="bottom" data-v-6d590448><div style="text-align:center;justify-content:flex-end;" data-v-6d590448><h2 data-v-6d590448> 欢迎大家来扩展 <a href="https://github.com/Yanyutin753/fakeApiTool-One-API" data-v-6d590448>fakeApiToolv0.1.0-oneApi </a> 获取token <a href="https://chat.openai.com/api/auth/session" data-v-6d590448>官网地址 </a><a href="https://ai.fakeopen.com/auth" data-v-6d590448>Pandora地址 </a> 安装One ApI<a href="https://github.com/songquanpeng/one-api" data-v-6d590448> 地址</a></h2></div></div>',1);function f(e,a,t,r,f,b){const k=l.gN,w=(0,s.up)("h9"),y=o.XZ;return(0,s.wg)(),(0,s.iD)("div",n,[(0,s._)("div",d,[(0,s._)("img",{src:r.image,alt:"Your Image"},null,8,c),m]),(0,s._)("div",i,[(0,s._)("div",u,[(0,s.Wm)(k,{modelValue:r.username,"onUpdate:modelValue":a[0]||(a[0]=e=>r.username=e),name:"用户名",label:"用户名",placeholder:"用户名",class:"userName"},null,8,["modelValue"])]),(0,s._)("div",v,[(0,s.Wm)(k,{modelValue:r.password,"onUpdate:modelValue":a[1]||(a[1]=e=>r.password=e),type:"password",name:"密码",label:"密码",placeholder:"密码",class:"userName"},null,8,["modelValue"])]),(0,s._)("div",p,[(0,s.Wm)(y,{class:"remember",modelValue:r.checked,"onUpdate:modelValue":a[2]||(a[2]=e=>r.checked=e),"checked-color":"#0ea27e","icon-size":"1vw"},{default:(0,s.w5)((()=>[(0,s.Wm)(w,{style:{"font-size":"14px",transform:"translateX(7px)"}},{default:(0,s.w5)((()=>[(0,s.Uk)("记住密码")])),_:1})])),_:1},8,["modelValue"])]),(0,s._)("div",h,[(0,s._)("input",{type:"submit",onClick:a[3]||(a[3]=(...e)=>r.submit&&r.submit(...e)),value:"登录",class:"userName"})])]),g])}var b=t(2262),k=t(2201),w=t(6154),y=t(451),I=t(1348),_={setup(){const e=(0,k.tv)(),a=(0,b.iH)(""),t=(0,b.iH)(""),o=(0,b.iH)(""),l=y;(0,s.bv)((()=>{const e=localStorage.getItem("savedUsername"),l=localStorage.getItem("savedPassword"),s=localStorage.getItem("savedRemember");"true"===s&&(a.value=e||"",t.value=l||"",o.value=!0)}));const r=()=>{o.value?(localStorage.setItem("savedUsername",a.value),localStorage.setItem("savedPassword",t.value),localStorage.setItem("savedRemember","true")):(localStorage.removeItem("savedUsername"),localStorage.removeItem("savedPassword"),localStorage.removeItem("savedRemember")),w.Z.post(`/api/login?userName=${a.value}&password=${t.value}`).then((a=>{if(1==a.data.code){console.log("登录成功",a.data.data);const t=a.data.data;localStorage.setItem("jwtToken",t),(0,I.z8)("登录成功！"),setTimeout((()=>{e.replace("/")}),1e3)}else console.error("登录失败"),(0,I.z8)("账号或密码错误！")})).catch((e=>{console.error("登录时出现错误:",e),(0,I.z8)("账号或密码错误！")}))};return{username:a,password:t,image:l,checked:o,submit:r}}},S=t(3744);const V=(0,S.Z)(_,[["render",f],["__scopeId","data-v-6d590448"]]);var U=V}}]);
//# sourceMappingURL=316.b63e4e75.js.map