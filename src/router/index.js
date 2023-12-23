import Vue from 'vue'
import VueRouter from 'vue-router'
import login from '../views/login.vue'
import signup from '../views/signup.vue'
import mainpage from '../views/mainPage.vue'


Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'login',
    component: login
  },
  {
    path: '/signup',
    name: 'signup',
    component: signup
  },
  {
    path : '/mainpage' ,
    name : 'mainpage',
    component : mainpage
  },
  
]

const router = new VueRouter({
  routes
})

export default router
