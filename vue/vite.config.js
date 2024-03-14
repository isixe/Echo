import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueJsx from '@vitejs/plugin-vue-jsx'

import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { AntDesignVueResolver } from 'unplugin-vue-components/resolvers'
import { codeInspectorPlugin } from 'code-inspector-plugin'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueJsx(),
    AutoImport({
      // targets to transform
      include: [
        /\.[tj]sx?$/, // .ts, .tsx, .js, .jsx
        /\.vue$/,
        /\.vue\?vue/, // .vue
        /\.md$/ // .md
      ],

      // global imports to register
      imports: [
        // presets
        'vue',
        'vue-router',
        // custom
        {
          // '@vueuse/core': [
          //   // named imports
          //   'useMouse', // import { useMouse } from '@vueuse/core',
          //   // alias
          //   ['useFetch', 'useMyFetch'] // import { useFetch as useMyFetch } from '@vueuse/core',
          // ],
          // '[package-name]': [
          //   '[import-names]',
          //   // alias
          //   ['[from]', '[alias]']
          // ]
        },
        // example type import
        {
          from: 'vue-router',
          imports: ['RouteLocationRaw'],
          type: true
        }
      ],
      // Enable auto import by filename for default module exports under directories
      defaultExportByFilename: false,

      // Auto import for module exports under directories
      // by default it only scan one level of modules under the directory
      dirs: [
        // './hooks',
        // './composables' // only root modules
        // './composables/**', // all nested modules
        // ...
      ],

      // Auto import inside Vue template
      // see https://github.com/unjs/unimport/pull/15 and https://github.com/unjs/unimport/pull/72
      vueTemplate: false,

      // Custom resolvers, compatible with `unplugin-vue-components`
      // see https://github.com/antfu/unplugin-auto-import/pull/23/
      resolvers: [
        /* ... */
      ],

      // Inject the imports at the end of other imports
      injectAtEnd: true,

      // Generate corresponding .eslintrc-auto-import.json file.
      // eslint globals Docs - https://eslint.org/docs/user-guide/configuring/language-options#specifying-globals
      eslintrc: {
        enabled: true, // Default `false`
        filepath: './.eslintrc-auto-import.json', // Default `./.eslintrc-auto-import.json`
        globalsPropValue: true // Default `true`, (true | false | 'readonly' | 'readable' | 'writable' | 'writeable')
      }
    }),
    codeInspectorPlugin({
      bundler: 'vite'
    }),
    Components({
      resolvers: [
        AntDesignVueResolver({
          importStyle: false, // css in js
          resolveIcons: true
        })
      ]
    })
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
    extensions: ['.js', '.jsx', '.tsx', '.json', '.vue']
  }
})
