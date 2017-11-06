import Vue from 'vue'
import Router from 'vue-router'
import Players from '@/components/Players'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Players',
      component: Players
    }
  ]
})
