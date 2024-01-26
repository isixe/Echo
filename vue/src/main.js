import 'normalize.css/normalize.css'
import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import { autoAnimatePlugin } from '@formkit/auto-animate/vue'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'

import App from './App.vue'
import router from './router'

const app = createApp(App)
const pina = createPinia().use(piniaPluginPersistedstate)

app.use(pina)
app.use(autoAnimatePlugin)
app.use(router)

app.mount('#app')
