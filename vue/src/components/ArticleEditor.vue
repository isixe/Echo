<template>
  <ckeditor
    :editor="editor"
    v-model="editorData"
    :config="editorConfig"
    @ready="onReady"
  ></ckeditor>
</template>

<script setup>
import { uploadPic } from '@/api/file'
import { message } from 'ant-design-vue'
import Editor from 'ckeditor5-custom-build/build/ckeditor'

const editor = Editor
const editorData = defineModel('editorData')
const editorConfig = {
  removePlugins: ['MediaEmbedToolbar']
}

class ImageUploadAdapter {
  constructor(loader) {
    this.loader = loader
  }
  async upload() {
    const formData = new FormData()
    formData.append('file', await this.loader.file)
    const data = await uploadPic(formData).then((res) => {
      message.success('上传成功')
      return res.data
    })

    return {
      default: data
    }
  }
}

const onReady = (editor) => {
  editor.plugins.get('FileRepository').createUploadAdapter = (loader) => {
    return new ImageUploadAdapter(loader)
  }
}
</script>
