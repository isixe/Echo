<template>
  <div style="border: 1px solid #ccc">
    <Toolbar
      style="border-bottom: 1px solid #ccc"
      :editor="editorRef"
      :defaultConfig="toolbarConfig"
    />
    <Editor
      style="height: 500px; overflow-y: hidden"
      v-model="valueHtml"
      :defaultConfig="editorConfig"
      @onCreated="handleCreated"
    />
  </div>
</template>

<script setup>
import '@wangeditor/editor/dist/css/style.css'

import { uploadPic } from '@/api/file'
import { message } from 'ant-design-vue'

import { Boot } from '@wangeditor/editor'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import markdownModule from '@wangeditor/plugin-md'
Boot.registerModule(markdownModule)

const editorRef = shallowRef()
const valueHtml = defineModel()

const toolbarConfig = {}

toolbarConfig.excludeKeys = ['insertVideo', 'uploadVideo', 'editVideoSize']

const editorConfig = {
  placeholder: '请输入内容...',
  MENU_CONF: {}
}

editorConfig.MENU_CONF['uploadImage'] = {
  async customUpload(file, insertFn) {
    const formData = new FormData()
    formData.append('file', file)
    const url = await uploadPic(formData).then((res) => {
      message.success('上传成功')
      return res.data
    })
    insertFn(url)
  }
}

onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor == null) return
  editor.destroy()
})

const handleCreated = (editor) => {
  editorRef.value = editor
}
</script>
