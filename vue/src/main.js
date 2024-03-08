import 'normalize.css/normalize.css'
import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import { autoAnimatePlugin } from '@formkit/auto-animate/vue'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import lazyPlugin from 'vue3-lazy'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'

import App from './App.vue'
import router from './router'

const app = createApp(App)
const pina = createPinia().use(piniaPluginPersistedstate)

app.use(pina)
app.use(router)
app.use(autoAnimatePlugin)
app.component('font-awesome-icon', FontAwesomeIcon)
app.use(lazyPlugin, {
  loading: '../src/assets/png/placeholder.png',
  error: '../src/assets/png/placeholder.png'
})

app.mount('#app')
