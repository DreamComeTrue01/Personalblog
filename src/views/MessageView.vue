<template>
  <div class="message-view">

    <!-- 中央内容区 -->
    <main class="message-main">
      <!-- 装饰元素 -->
      <div class="decorations">
        <div class="decoration-left"></div>
        <div class="decoration-right"></div>
        <div class="decoration-top"></div>
      </div>
      <div class="message-container">
        <div class="message-header-section">
          <h2 class="message-title">
            <span class="title-word" v-for="(char, index) in titleText" :key="index" :style="{ animationDelay: `${index * 0.1}s` }">{{ char }}</span>
          </h2>
          <p class="message-subtitle">留下你的想法和建议，让我们一起交流成长</p>
          <div class="message-intro">
            <p class="intro-text">这里是一个开放的交流平台，欢迎分享你的想法、建议和故事。每一条留言都是对我的鼓励和支持，我会认真阅读并回复每一位朋友的留言。让我们一起创造一个温暖、积极的社区氛围！</p>
          </div>
          <div class="message-stats">
            <div class="stat-item">
              <span class="stat-number">{{ messages.length }}</span>
              <span class="stat-label">条留言</span>
            </div>
            <div class="stat-item">
              <span class="stat-number">{{ totalLikes }}</span>
              <span class="stat-label">次点赞</span>
            </div>
            <div class="stat-item">
              <span class="stat-number">{{ totalWords }}</span>
              <span class="stat-label">字留言</span>
            </div>
          </div>
          <div class="message-decoration">
            <span class="decoration-dot"></span>
            <span class="decoration-line"></span>
            <span class="decoration-dot"></span>
          </div>
        </div>
        
        <div class="message-content">
          <!-- 留言表单 -->
          <div class="message-form">
            <div class="form-header">
              <h3 class="form-title">发表留言</h3>
              <div class="form-icon">💬</div>
            </div>
            <div class="form-rules">
              <h4 class="rules-title">留言规则</h4>
              <ul class="rules-list">
                <li>请文明留言，尊重他人</li>
                <li>请勿发布广告、恶意攻击等不良内容</li>
                <li>鼓励分享你的想法和建议</li>
                <li>请留下真实的联系方式，以便回复</li>
              </ul>
            </div>
            <form @submit.prevent="submitMessage">
              <div class="form-group">
                <label for="name" class="form-label">
                  <span class="label-icon">👤</span>
                  昵称
                </label>
                <div class="input-container">
                  <input type="text" id="name" v-model="messageForm.name" placeholder="请输入你的昵称" required class="form-input">
                  <span class="input-icon">👤</span>
                </div>
              </div>
              <div class="form-group">
                <label for="email" class="form-label">
                  <span class="label-icon">📧</span>
                  邮箱
                </label>
                <div class="input-container">
                  <input type="email" id="email" v-model="messageForm.email" placeholder="请输入你的邮箱" required class="form-input">
                  <span class="input-icon">📧</span>
                </div>
              </div>
              <div class="form-group">
                <label for="content" class="form-label">
                  <span class="label-icon">💭</span>
                  留言内容
                </label>
                <div class="textarea-container">
                  <textarea id="content" v-model="messageForm.content" placeholder="请输入你的留言，分享你的想法..." rows="5" required class="form-textarea"></textarea>
                  <span class="textarea-icon">💭</span>
                  <div class="char-count" :class="{ 'warning': messageForm.content.length > 200 }">
                    {{ messageForm.content.length }}/300
                  </div>
                </div>
              </div>
              <button type="submit" class="submit-btn" :class="{ 'loading': isSubmitting }">
                <span v-if="!isSubmitting" class="btn-icon">✉️</span>
                <span v-else class="btn-icon loading-icon">⏳</span>
                {{ isSubmitting ? '发表中...' : '发表留言' }}
              </button>
            </form>
          </div>
          
          <!-- 留言列表 -->
          <div class="message-list">
            <div class="list-header">
              <h3 class="list-title">留言列表</h3>
              <span class="message-count">{{ messages.length }} 条留言</span>
            </div>
            <div v-if="messages.length === 0" class="no-messages">
              <div class="no-messages-icon">📝</div>
              <p>暂无留言，快来发表第一条留言吧！</p>
              <button class="no-messages-btn" @click="scrollToForm">发表第一条留言</button>
            </div>
            <div v-else class="messages">
              <div v-for="(message, index) in messages" :key="message.id" class="message-item" :style="{ animationDelay: `${index * 0.1}s` }">
                <div class="message-header">
                  <div class="message-avatar">
                    <img :src="getAvatarUrl(message.name)" :alt="message.name" class="avatar-img">
                    <div class="avatar-border"></div>
                  </div>
                  <div class="message-info">
                    <h4 class="message-name">{{ message.name }}</h4>
                    <p class="message-date">{{ formatDate(message.date) }}</p>
                  </div>
                  <div class="message-badge">{{ index + 1 }}</div>
                </div>
                <div class="message-body">
                  <p>{{ message.content }}</p>
                </div>
                <div class="message-footer">
                  <div class="message-actions">
                    <button class="action-btn like-btn" @click="likeMessage(message.id)" :class="{ 'liked': message.isLiked }">
                      <span class="action-icon">❤️</span>
                      <span class="action-text">{{ message.likes || 0 }}</span>
                    </button>
                    <button class="action-btn reply-btn" @click="toggleReplyForm(message.id)">
                      <span class="action-icon">💬</span>
                      <span class="action-text">回复</span>
                    </button>
                  </div>
                </div>
                
                <!-- 回复表单 -->
                <div v-if="replyFormVisible[message.id]" class="reply-form">
                  <form @submit.prevent="submitReply(message.id)">
                    <div class="form-group">
                      <input type="text" v-model="replyForms[message.id].name" placeholder="你的昵称" required class="form-input">
                    </div>
                    <div class="form-group">
                      <textarea v-model="replyForms[message.id].content" placeholder="写下你的回复..." rows="3" required class="form-textarea"></textarea>
                    </div>
                    <div class="form-actions">
                      <button type="button" class="cancel-btn" @click="toggleReplyForm(message.id)">取消</button>
                      <button type="submit" class="submit-reply-btn">提交回复</button>
                    </div>
                  </form>
                </div>
                
                <!-- 回复列表 -->
                <div v-if="message.replies && message.replies.length > 0" class="replies-list">
                  <h4 class="replies-title">回复 ({{ message.replies.length }})</h4>
                  <div v-for="(reply, index) in message.replies" :key="index" class="reply-item">
                    <div class="reply-header">
                      <span class="reply-name">{{ reply.name }}</span>
                      <span class="reply-date">{{ formatDate(reply.date) }}</span>
                    </div>
                    <div class="reply-content">{{ reply.content }}</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        
        <!-- 返回按钮 -->
        <div class="back-container">
          <button class="back-btn" @click="goBack">
            <span class="btn-icon">←</span>
            返回上一页
          </button>
        </div>
      </div>
    </main>

    <!-- 页脚 -->
    <footer class="portal-footer">
      <div class="footer-content">
        <div class="footer-section">
          <h3 class="footer-title">关于我</h3>
          <p class="footer-text">少年立志青云间，不负凌云万丈才</p>
        </div>
        <div class="footer-section">
          <h3 class="footer-title">联系我</h3>
          <p class="footer-text">Email: dream@example.com</p>
        </div>
        <div class="footer-section">
          <h3 class="footer-title">© 2026 Dream's Blog</h3>
          <p class="footer-text">All rights reserved</p>
        </div>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

// 标题文字
const titleText = ref('留言板')

// 留言表单
const messageForm = ref({
  name: '',
  email: '',
  content: ''
})

// 提交状态
const isSubmitting = ref(false)

// 留言数据
const messages = ref([])

// 从localStorage加载数据
const loadMessages = () => {
  const savedMessages = localStorage.getItem('messages')
  if (savedMessages) {
    messages.value = JSON.parse(savedMessages)
  } else {
    // 初始默认数据
    messages.value = [
      {
        id: 1,
        name: '访客1',
        email: 'visitor1@example.com',
        content: '这个博客很棒，继续加油！',
        date: '2026-02-25',
        likes: 5,
        isLiked: false,
        replies: []
      },
      {
        id: 2,
        name: '访客2',
        email: 'visitor2@example.com',
        content: '学习了很多知识，谢谢分享！',
        date: '2026-02-24',
        likes: 3,
        isLiked: false,
        replies: []
      },
      {
        id: 3,
        name: '访客3',
        email: 'visitor3@example.com',
        content: '期待更多精彩内容！',
        date: '2026-02-23',
        likes: 2,
        isLiked: false,
        replies: []
      }
    ]
    saveMessages()
  }
}

// 保存数据到localStorage
const saveMessages = () => {
  localStorage.setItem('messages', JSON.stringify(messages.value))
}

// 通知相关
const showNotification = ref(false)
const notificationMessage = ref('')
const notificationType = ref('success')

// 回复表单相关
const replyFormVisible = ref({})
const replyForms = ref({})

// 计算属性
const totalLikes = computed(() => {
  return messages.value.reduce((sum, message) => sum + (message.likes || 0), 0)
})

const totalWords = computed(() => {
  return messages.value.reduce((sum, message) => sum + (message.content ? message.content.length : 0), 0)
})

// 显示通知
const showToast = (message, type = 'success') => {
  notificationMessage.value = message
  notificationType.value = type
  showNotification.value = true
  
  // 3秒后自动关闭通知
  setTimeout(() => {
    showNotification.value = false
  }, 3000)
}

// 提交留言
const submitMessage = () => {
  isSubmitting.value = true
  
  // 模拟提交延迟
  setTimeout(() => {
    const newMessage = {
      id: Date.now(),
      ...messageForm.value,
      date: new Date().toISOString().split('T')[0],
      likes: 0,
      isLiked: false,
      replies: []
    }
    messages.value.unshift(newMessage)
    saveMessages()
    messageForm.value = {
      name: '',
      email: '',
      content: ''
    }
    showToast('留言发表成功！')
    isSubmitting.value = false
  }, 1000)
}

// 点赞留言
const likeMessage = (messageId) => {
  const message = messages.value.find(msg => msg.id === messageId)
  if (message) {
    if (message.isLiked) {
      message.likes = Math.max(0, (message.likes || 0) - 1)
      message.isLiked = false
      showToast('已取消点赞')
    } else {
      message.likes = (message.likes || 0) + 1
      message.isLiked = true
      showToast('点赞成功！')
    }
    saveMessages()
  }
}

// 切换回复表单显示/隐藏
const toggleReplyForm = (messageId) => {
  replyFormVisible.value[messageId] = !replyFormVisible.value[messageId]
  if (replyFormVisible.value[messageId]) {
    // 初始化回复表单数据
    replyForms.value[messageId] = {
      name: '',
      content: ''
    }
  }
}

// 提交回复
const submitReply = (messageId) => {
  const message = messages.value.find(msg => msg.id === messageId)
  if (message && replyForms.value[messageId]) {
    const newReply = {
      name: replyForms.value[messageId].name,
      content: replyForms.value[messageId].content,
      date: new Date().toISOString().split('T')[0]
    }
    if (!message.replies) {
      message.replies = []
    }
    message.replies.push(newReply)
    saveMessages()
    replyFormVisible.value[messageId] = false
    showToast('回复成功！')
  }
}

// 滚动到表单
const scrollToForm = () => {
  const formElement = document.querySelector('.message-form')
  if (formElement) {
    formElement.scrollIntoView({ behavior: 'smooth', block: 'center' })
  }
}

// 生成头像URL
const getAvatarUrl = (name) => {
  const encodedName = encodeURIComponent(name)
  return `https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=avatar%20of%20${encodedName}%2C%20simple%2C%20cartoon%2C%20clean%20background&image_size=square`
}

// 格式化日期
const formatDate = (dateString) => {
  const date = new Date(dateString)
  return date.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  })
}

// 返回上一页
const goBack = () => {
  router.back()
}

onMounted(() => {
  // 组件挂载时加载数据
  loadMessages()
})
</script>

<style scoped>
/* 导入字体 */
@import url('https://fonts.googleapis.com/css2?family=Ma+Shan+Zheng&family=Montserrat:wght@300;400;500;600;700&display=swap');

.message-view {
  width: 100%;
  min-height: 100vh;
  font-family: 'PingFang SC', 'Montserrat', sans-serif;
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
  position: relative;
  overflow: hidden;
  padding: 0;
}

/* 中央内容区 */
.message-main {
  padding: 40px 0 60px;
  position: relative;
  z-index: 2;
  background-image: url('https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=colorful%20vibrant%20message%20board%20background%20with%20distinct%20patterns%2C%20bold%20pastel%20colors%2C%20clear%20design%2C%20inviting%20atmosphere&image_size=landscape_16_9');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
}

.message-main::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, rgba(248, 250, 252, 0.7) 0%, rgba(226, 232, 240, 0.6) 100%);
  z-index: -1;
}

/* 装饰元素容器 */
.message-main .decorations {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: -1;
}

/* 左侧装饰元素 */
.message-main .decoration-left {
  position: absolute;
  bottom: 50px;
  left: 10%;
  width: 100px;
  height: 100px;
  background: radial-gradient(circle, rgba(74, 179, 111, 0.1) 0%, transparent 70%);
  border-radius: 50%;
  animation: float 8s ease-in-out infinite reverse;
}

/* 右侧装饰元素 */
.message-main .decoration-right {
  position: absolute;
  top: 50px;
  right: 10%;
  width: 100px;
  height: 100px;
  background: radial-gradient(circle, rgba(139, 92, 246, 0.1) 0%, transparent 70%);
  border-radius: 50%;
  animation: float 6s ease-in-out infinite;
}

/* 顶部装饰元素 */
.message-main .decoration-top {
  position: absolute;
  top: 10%;
  left: 20%;
  width: 80px;
  height: 80px;
  background: radial-gradient(circle, rgba(236, 72, 153, 0.1) 0%, transparent 70%);
  border-radius: 50%;
  animation: float 7s ease-in-out infinite;
}

@keyframes float {
  0%, 100% {
    transform: translateY(0px);
  }
  50% {
    transform: translateY(-20px);
  }
}

.message-container {
  max-width: 900px;
  margin: 0 auto;
  padding: 0 20px;
}

/* 标题部分 */
.message-header-section {
  text-align: center;
  margin-bottom: 40px;
}

.message-title {
  font-size: 48px;
  font-weight: bold;
  color: #333;
  margin-bottom: 24px;
  font-family: 'Ma Shan Zheng', cursive;
  display: inline-block;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.1);
  background: linear-gradient(135deg, #3b82f6 0%, #8b5cf6 50%, #ec4899 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  padding: 16px 32px;
  border-radius: 50px;
  box-shadow: 0 4px 16px rgba(59, 130, 246, 0.3);
  transition: all 0.3s ease;
  animation: titleFloat 1s ease-out;
}

@keyframes titleFloat {
  0% {
    opacity: 0;
    transform: translateY(-30px) scale(0.8);
  }
  100% {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}

.message-title:hover {
  transform: scale(1.05);
  box-shadow: 0 6px 20px rgba(59, 130, 246, 0.4);
}

.message-subtitle {
  font-size: 16px;
  color: #666;
  margin-bottom: 24px;
  line-height: 1.5;
}

.message-intro {
  max-width: 700px;
  margin: 0 auto 32px;
}

.intro-text {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
  text-align: center;
  padding: 16px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  border: 1px solid #e9ecef;
}

.message-stats {
  display: flex;
  justify-content: center;
  gap: 30px;
  margin: 32px 0;
}

.stat-item {
  text-align: center;
  padding: 20px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
  border: 1px solid #e9ecef;
  min-width: 120px;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.stat-item::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 4px;
  background: linear-gradient(90deg, #3b82f6 0%, #8b5cf6 100%);
}

.stat-item:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
  border-color: #3b82f6;
}

.stat-number {
  display: block;
  font-size: 28px;
  font-weight: bold;
  color: #3b82f6;
  margin-bottom: 6px;
  font-family: 'Montserrat', sans-serif;
  transition: all 0.3s ease;
}

.stat-item:hover .stat-number {
  color: #8b5cf6;
}

.stat-label {
  display: block;
  font-size: 13px;
  color: #666;
  font-weight: 500;
  transition: all 0.3s ease;
}

.stat-item:hover .stat-label {
  color: #ec4899;
}

.message-decoration {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  margin-top: 24px;
}

.decoration-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #8b5cf6;
  box-shadow: 0 0 8px rgba(139, 92, 246, 0.5);
}

.decoration-line {
  width: 80px;
  height: 2px;
  background: linear-gradient(90deg, #3b82f6 0%, #8b5cf6 100%);
  opacity: 0.8;
}

/* 内容区域 */
.message-content {
  display: flex;
  gap: 32px;
  flex-wrap: wrap;
  max-width: 1000px;
  margin: 0 auto;
  padding: 0 20px;
}

/* 留言表单 */
.message-form {
  flex: 1;
  min-width: 320px;
  border-radius: 16px;
  padding: 36px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
  border: 1px solid #e9ecef;
  background: white;
  transition: all 0.3s ease;
}

.message-form:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.1);
}

.form-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
  padding-bottom: 12px;
  border-bottom: 1px solid #e9ecef;
}

.form-title {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin: 0;
}

.form-icon {
  font-size: 20px;
}

.form-rules {
  margin-bottom: 20px;
  padding: 12px;
  background: #f8f9fa;
  border-radius: 6px;
  border-left: 3px solid #4abe6f;
}

.rules-title {
  font-size: 14px;
  font-weight: 600;
  color: #4abe6f;
  margin-bottom: 8px;
  display: flex;
  align-items: center;
  gap: 6px;
}

.rules-title::before {
  content: '📝';
  font-size: 14px;
}

.rules-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.rules-list li {
  font-size: 12px;
  color: #666;
  margin-bottom: 6px;
  display: flex;
  align-items: flex-start;
  gap: 6px;
}

.rules-list li::before {
  content: '✅';
  font-size: 12px;
  flex-shrink: 0;
  margin-top: 1px;
}

.form-group {
  margin-bottom: 20px;
}

.form-label {
  display: flex;
  align-items: center;
  font-size: 14px;
  font-weight: 600;
  color: #333;
  margin-bottom: 6px;
  gap: 6px;
}

.label-icon {
  font-size: 14px;
  width: 16px;
  text-align: center;
}

.input-container {
  position: relative;
}

.form-input {
  width: 100%;
  padding: 12px 16px;
  border: 2px solid #e9ecef;
  border-radius: 8px;
  font-size: 14px;
  transition: all 0.3s ease;
  font-family: inherit;
  background: white;
  position: relative;
  z-index: 1;
}

.form-input:focus {
  outline: none;
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.15);
}

.textarea-container {
  position: relative;
}

.form-textarea {
  width: 100%;
  padding: 12px 16px;
  border: 2px solid #e9ecef;
  border-radius: 8px;
  font-size: 14px;
  transition: all 0.3s ease;
  font-family: inherit;
  resize: vertical;
  min-height: 100px;
  line-height: 1.5;
  background: white;
  position: relative;
  z-index: 1;
}

.form-textarea:focus {
  outline: none;
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.15);
}

.char-count {
  position: absolute;
  bottom: 8px;
  right: 12px;
  font-size: 12px;
  color: #999;
}

.char-count.warning {
  color: #ff9800;
}

.submit-btn {
  width: 100%;
  padding: 14px;
  background: linear-gradient(135deg, #3b82f6 0%, #8b5cf6 100%);
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.3);
  position: relative;
  overflow: hidden;
}

.submit-btn::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
  transition: all 0.6s ease;
}

.submit-btn:hover:not(.loading) {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(59, 130, 246, 0.4);
}

.submit-btn:hover:not(.loading)::before {
  left: 100%;
}

.submit-btn.loading {
  cursor: not-allowed;
  opacity: 0.8;
}

.loading-icon {
  animation: spin 1s linear infinite;
}

@keyframes spin {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

.btn-icon {
  font-size: 16px;
}

/* 留言列表 */
.message-list {
  flex: 1.5;
  min-width: 320px;
  border-radius: 16px;
  padding: 36px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
  border: 1px solid #e9ecef;
  background: white;
  transition: all 0.3s ease;
}

.message-list:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.1);
}

.list-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
  padding-bottom: 12px;
  border-bottom: 1px solid #e9ecef;
}

.list-title {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin: 0;
}

.message-count {
  font-size: 12px;
  color: #666;
  background: #f8f9fa;
  padding: 4px 10px;
  border-radius: 12px;
}

.no-messages {
  text-align: center;
  padding: 40px 0;
  color: #999;
}

.no-messages-icon {
  font-size: 36px;
  margin-bottom: 12px;
  opacity: 0.6;
}

.no-messages-btn {
  margin-top: 16px;
  padding: 8px 16px;
  background: #4abe6f;
  color: white;
  border: none;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.no-messages-btn:hover {
  background: #3ca55a;
}

.messages {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.message-item {
  padding: 24px;
  border-radius: 12px;
  transition: all 0.3s ease;
  background: white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  border: 1px solid #e9ecef;
  position: relative;
  overflow: hidden;
}

.message-item::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 4px;
  height: 100%;
  background: linear-gradient(180deg, #3b82f6 0%, #8b5cf6 100%);
}

.message-item::after {
  content: '';
  position: absolute;
  top: 20px;
  right: 20px;
  width: 80px;
  height: 80px;
  background: radial-gradient(circle, rgba(139, 92, 246, 0.05) 0%, transparent 70%);
  border-radius: 50%;
  z-index: 0;
}

.message-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
  border-color: #8b5cf6;
}

.message-item:hover::after {
  transform: scale(1.2);
  transition: transform 0.3s ease;
}

.message-header {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
}

.message-avatar {
  width: 56px;
  height: 56px;
  border-radius: 50%;
  overflow: hidden;
  margin-right: 16px;
  border: 3px solid #e9ecef;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  position: relative;
}

.message-avatar:hover {
  transform: scale(1.05);
  box-shadow: 0 4px 12px rgba(139, 92, 246, 0.3);
  border-color: #8b5cf6;
}

.avatar-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.message-info {
  flex: 1;
}

.message-name {
  font-size: 14px;
  font-weight: 600;
  color: #333;
  margin-bottom: 2px;
}

.message-date {
  font-size: 12px;
  color: #999;
}

.message-body {
  font-size: 15px;
  line-height: 1.6;
  color: #333;
  margin-bottom: 16px;
  padding: 16px;
  background: #f8f9fa;
  border-radius: 8px;
  border-left: 4px solid #8b5cf6;
  transition: all 0.3s ease;
}

.message-item:hover .message-body {
  background: #f5f3ff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.message-footer {
  display: flex;
  justify-content: flex-end;
}

.message-actions {
  display: flex;
  gap: 8px;
}

.like-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 6px 14px;
  background: #f8f9fa;
  border: 1px solid #e9ecef;
  border-radius: 20px;
  font-size: 13px;
  color: #666;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.like-btn:hover {
  background: #3b82f6;
  color: white;
  transform: translateY(-1px);
  box-shadow: 0 4px 8px rgba(59, 130, 246, 0.3);
}

.like-btn.liked {
  background: #3b82f6;
  color: white;
  box-shadow: 0 4px 8px rgba(59, 130, 246, 0.3);
}

.like-btn.liked:hover {
  background: #2563eb;
}

.action-icon {
  font-size: 14px;
}

/* 回复按钮 */
.reply-btn {
  background: #f8f9fa;
  border: 1px solid #e9ecef;
  color: #666;
}

.reply-btn:hover {
  background: #3b82f6;
  color: white;
}

/* 回复表单 */
.reply-form {
  margin-top: 16px;
  padding: 16px;
  background: #f8f9fa;
  border-radius: 8px;
  border: 1px solid #e9ecef;
}

.reply-form .form-group {
  margin-bottom: 12px;
}

.reply-form .form-actions {
  display: flex;
  gap: 8px;
  justify-content: flex-end;
  margin-top: 12px;
}

.cancel-btn {
  padding: 6px 12px;
  background: #f8f9fa;
  border: 1px solid #e9ecef;
  border-radius: 6px;
  font-size: 13px;
  color: #666;
  cursor: pointer;
  transition: all 0.3s ease;
}

.cancel-btn:hover {
  background: #e9ecef;
}

.submit-reply-btn {
  padding: 6px 12px;
  background: #3b82f6;
  border: none;
  border-radius: 6px;
  font-size: 13px;
  color: white;
  cursor: pointer;
  transition: all 0.3s ease;
}

.submit-reply-btn:hover {
  background: #2563eb;
}

/* 回复列表 */
.replies-list {
  margin-top: 16px;
  padding: 16px;
  background: #f8f9fa;
  border-radius: 8px;
  border: 1px solid #e9ecef;
}

.replies-title {
  font-size: 14px;
  font-weight: 600;
  color: #333;
  margin-bottom: 12px;
}

.reply-item {
  padding: 12px;
  background: white;
  border-radius: 6px;
  margin-bottom: 8px;
  border-left: 3px solid #3b82f6;
}

.reply-item:last-child {
  margin-bottom: 0;
}

.reply-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 6px;
}

.reply-name {
  font-size: 13px;
  font-weight: 600;
  color: #333;
}

.reply-date {
  font-size: 11px;
  color: #999;
}

.reply-content {
  font-size: 13px;
  line-height: 1.4;
  color: #666;
}

/* 返回按钮 */
.back-container {
  display: flex;
  justify-content: center;
  margin-top: 40px;
}

.back-btn {
  padding: 12px 24px;
  background: linear-gradient(135deg, #8b5cf6 0%, #ec4899 100%);
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 6px;
}

.back-btn:hover {
  background: linear-gradient(135deg, #7c3aed 0%, #db2777 100%);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(139, 92, 246, 0.3);
}

/* 通知组件样式 */
.notification {
  position: fixed;
  top: 80px;
  right: 20px;
  padding: 12px 16px;
  border-radius: 6px;
  color: white;
  font-size: 14px;
  font-weight: 500;
  z-index: 10000;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
  max-width: 300px;
  animation: slideInRight 0.3s ease-out;
}

.notification.success {
  background: #4abe6f;
}

.notification.error {
  background: #dc3545;
}

.notification.warning {
  background: #ffc107;
}

@keyframes slideInRight {
  from {
    transform: translateX(100%);
    opacity: 0;
  }
  to {
    transform: translateX(0);
    opacity: 1;
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .message-container {
    padding: 0 16px;
  }
  
  .message-title {
    font-size: 28px;
  }
  
  .message-subtitle {
    font-size: 14px;
  }
  
  .message-stats {
    flex-wrap: wrap;
    gap: 16px;
  }
  
  .stat-item {
    flex: 1;
    min-width: 100px;
    padding: 12px;
  }
  
  .stat-number {
    font-size: 20px;
  }
  
  .message-content {
    flex-direction: column;
  }
  
  .message-form,
  .message-list {
    min-width: 100%;
    padding: 20px;
  }
  
  .message-item {
    padding: 16px;
  }
  
  .back-btn {
    padding: 10px 20px;
    font-size: 14px;
  }
  
  .notification {
    left: 20px;
    right: 20px;
    max-width: none;
  }
}

@media (max-width: 480px) {
  .message-form,
  .message-list {
    padding: 16px;
  }
  
  .form-header,
  .list-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }
  
  .message-avatar {
    width: 40px;
    height: 40px;
  }
  
  .message-title {
    font-size: 24px;
  }
}

/* 页脚样式 */
.portal-footer {
  background: white;
  padding: 40px 0;
  margin-top: 60px;
  border-top: 1px solid #e9ecef;
  box-shadow: 0 -4px 16px rgba(0, 0, 0, 0.05);
  position: relative;
  overflow: hidden;
}

.portal-footer::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 4px;
  background: linear-gradient(90deg, #3b82f6 0%, #2563eb 100%);
}

.footer-content {
  display: flex;
  justify-content: space-around;
  flex-wrap: wrap;
  gap: 20px;
  max-width: 900px;
  margin: 0 auto;
  padding: 0 20px;
}

.footer-section {
  text-align: center;
  min-width: 180px;
  padding: 10px 0;
}

.footer-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin-bottom: 12px;
  line-height: 1.4;
}

.footer-text {
  font-size: 13px;
  color: #666;
  line-height: 1.6;
  margin: 6px 0;
}
</style>