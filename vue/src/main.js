import 'normalize.css/normalize.css'
import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import CKEditor from '@ckeditor/ckeditor5-vue'
import { autoAnimatePlugin } from '@formkit/auto-animate/vue'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'

import App from './App.vue'
import router from './router'

const app = createApp(App)
const pina = createPinia().use(piniaPluginPersistedstate)

app.use(pina)
app.use(router)
app.use(CKEditor)
app.use(autoAnimatePlugin)
app.component('font-awesome-icon', FontAwesomeIcon)

app.mount('#app')
