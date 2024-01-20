import Clipboard from 'clipboard'
import { message } from 'ant-design-vue'

const clipboardSuccess = (text) => message.success(`复制${text}成功`)
const clipboardError = (text) => message.error(`复制${text}失败`)

export default function handleClipboard(text, event) {
  const clipboard = new Clipboard(event.target, {
    text: () => text
  })
  clipboard.on('success', () => {
    clipboardSuccess(text)
    clipboard.destroy()
  })
  clipboard.on('error', () => {
    clipboardError(text)
    clipboard.destroy()
  })
  clipboard.onClick(event)
}
