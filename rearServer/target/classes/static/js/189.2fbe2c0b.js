"use strict";(self["webpackChunkfakeapitoolweb"]=self["webpackChunkfakeapitoolweb"]||[]).push([[189],{9189:function(e,a,l){l.r(a),l.d(a,{default:function(){return I}});var o=l(6773),t=(l(1958),l(368),l(6742),l(3164)),s=(l(991),l(6252));const r=e=>((0,s.dD)("data-v-f19eda7a"),e=e(),(0,s.Cn)(),e),d={class:"content"},n=r((()=>(0,s._)("h1",null,"用户登录",-1))),u={class:"container"},c=r((()=>(0,s._)("br",null,null,-1))),m=r((()=>(0,s._)("br",null,null,-1)));function v(e,a,l,r,v,i){const p=t.gN,b=(0,s.up)("h9"),g=o.XZ;return(0,s.wg)(),(0,s.iD)("div",d,[n,(0,s._)("div",u,[(0,s.Wm)(p,{modelValue:r.username,"onUpdate:modelValue":a[0]||(a[0]=e=>r.username=e),name:"用户名",label:"用户名",placeholder:"用户名"},null,8,["modelValue"]),(0,s.Wm)(p,{modelValue:r.password,"onUpdate:modelValue":a[1]||(a[1]=e=>r.password=e),type:"password",name:"密码",label:"密码",placeholder:"密码"},null,8,["modelValue"]),(0,s.Wm)(g,{class:"remember",modelValue:r.checked,"onUpdate:modelValue":a[2]||(a[2]=e=>r.checked=e),"checked-color":"black","icon-size":"15px"},{default:(0,s.w5)((()=>[(0,s.Wm)(b,{style:{"font-size":"13px",transform:"translateX(10px)"}},{default:(0,s.w5)((()=>[(0,s.Uk)("记住密码")])),_:1})])),_:1},8,["modelValue"]),c,m,(0,s._)("input",{type:"submit",onClick:a[3]||(a[3]=(...e)=>r.submit&&r.submit(...e)),value:"登录"})])])}var i=l(2262),p=l(2201),b=l(6154),g=l(7289),w=l(1348),f={setup(){const e=(0,p.tv)(),a=(0,i.iH)(""),l=(0,i.iH)(""),o=(0,i.iH)(""),t=g;(0,s.bv)((()=>{const e=localStorage.getItem("savedUsername"),t=localStorage.getItem("savedPassword"),s=localStorage.getItem("savedRemember");"true"===s&&(a.value=e||"",l.value=t||"",o.value=!0)}));const r=()=>{o.value?(localStorage.setItem("savedUsername",a.value),localStorage.setItem("savedPassword",l.value),localStorage.setItem("savedRemember","true")):(localStorage.removeItem("savedUsername"),localStorage.removeItem("savedPassword"),localStorage.removeItem("savedRemember")),b.Z.post(`/api/login?userName=${a.value}&password=${l.value}`).then((a=>{if(1==a.data.code){console.log("登录成功",a.data.data);const l=a.data.data;localStorage.setItem("jwtToken",l),(0,w.z8)("登录成功！"),setTimeout((()=>{e.replace("/")}),1e3)}else console.error("登录失败"),(0,w.z8)("账号或密码错误！")})).catch((e=>{console.error("登录时出现错误:",e),(0,w.z8)("账号或密码错误！")}))};return{username:a,password:l,image:t,checked:o,submit:r}}},k=l(3744);const h=(0,k.Z)(f,[["render",v],["__scopeId","data-v-f19eda7a"]]);var I=h}}]);
//# sourceMappingURL=189.2fbe2c0b.js.map