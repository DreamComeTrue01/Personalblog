<template>
  <div class="message-view">
    <!-- 导航栏 -->
    <header class="portal-nav" :class="{ 'scrolled': isScrolled }">
      <div class="nav-content">
        <div class="nav-left">
          <h1 class="logo">Dream<span>blog</span></h1>
        </div>
        <nav class="main-nav">
          <router-link to="/" class="nav-link">首页</router-link>
          <router-link to="/blog" class="nav-link">文章</router-link>
          <router-link to="/life" class="nav-link">碎碎念</router-link>
          <router-link to="/archive" class="nav-link">归档</router-link>
          <router-link to="/about" class="nav-link">关于</router-link>
          <router-link to="/message" class="nav-link active">留言板</router-link>
        </nav>
        <div class="nav-right">
          <button class="search-btn" aria-label="搜索">🔍</button>
        </div>
      </div>
    </header>

    <!-- 中央内容区 -->
    <main class="message-main">
      <div class="message-container">
        <h2 class="message-title">留言板</h2>
        <p class="message-subtitle">留下你的想法和建议</p>
        
        <div class="message-content">
          <!-- 留言表单 -->
          <div class="message-form">
            <h3 class="form-title">发表留言</h3>
            <form @submit.prevent="submitMessage">
              <div class="form-group">
                <label for="name">昵称</label>
                <input type="text" id="name" v-model="messageForm.name" placeholder="请输入你的昵称" required>
              </div>
              <div class="form-group">
                <label for="email">邮箱</label>
                <input type="email" id="email" v-model="messageForm.email" placeholder="请输入你的邮箱" required>
              </div>
              <div class="form-group">
                <label for="content">留言内容</label>
                <textarea id="content" v-model="messageForm.content" placeholder="请输入你的留言" rows="5" required></textarea>
              </div>
              <button type="submit" class="submit-btn">发表留言</button>
            </form>
          </div>
          
          <!-- 留言列表 -->
          <div class="message-list">
            <h3 class="list-title">留言列表</h3>
            <div v-if="messages.length === 0" class="no-messages">
              <p>暂无留言，快来发表第一条留言吧！</p>
            </div>
            <div v-else class="messages">
              <div v-for="message in messages" :key="message.id" class="message-item">
                <div class="message-header">
                  <div class="message-avatar">
                    <img :src="getAvatarUrl(message.name)" :alt="message.name" class="avatar-img">
                  </div>
                  <div class="message-info">
                    <h4 class="message-name">{{ message.name }}</h4>
                    <p class="message-date">{{ formatDate(message.date) }}</p>
                  </div>
                </div>
                <div class="message-body">
                  <p>{{ message.content }}</p>
                </div>
              </div>
            </div>
          </div>
        </div>
        
        <!-- 返回按钮 -->
        <div class="back-container">
          <button class="back-btn" @click="goBack">← 返回上一页</button>
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
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()
const isScrolled = ref(false)

// 留言表单
const messageForm = ref({
  name: '',
  email: '',
  content: ''
})

// 留言数据
const messages = ref([
  {
    id: 1,
    name: '访客1',
    email: 'visitor1@example.com',
    content: '这个博客很棒，继续加油！',
    date: '2026-02-25'
  },
  {
    id: 2,
    name: '访客2',
    email: 'visitor2@example.com',
    content: '学习了很多知识，谢谢分享！',
    date: '2026-02-24'
  },
  {
    id: 3,
    name: '访客3',
    email: 'visitor3@example.com',
    content: '期待更多精彩内容！',
    date: '2026-02-23'
  }
])

// 提交留言
const submitMessage = () => {
  const newMessage = {
    id: Date.now(),
    ...messageForm.value,
    date: new Date().toISOString().split('T')[0]
  }
  messages.value.unshift(newMessage)
  messageForm.value = {
    name: '',
    email: '',
    content: ''
  }
  alert('留言发表成功！')
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

// 监听滚动事件
const handleScroll = () => {
  isScrolled.value = window.scrollY > 50
}

// 返回上一页
const goBack = () => {
  router.back()
}

onMounted(() => {
  window.addEventListener('scroll', handleScroll)
  return () => {
    window.removeEventListener('scroll', handleScroll)
  }
})
</script>

<style scoped>
.message-view {
  width: 100%;
  min-height: 100vh;
  background: #f8f9fa;
}

.message-main {
  padding: 60px 0;
}

.message-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 0 20px;
}

.message-title {
  font-size: 36px;
  font-weight: bold;
  color: #333;
  text-align: center;
  margin-bottom: 16px;
}

.message-subtitle {
  font-size: 18px;
  color: #666;
  text-align: center;
  margin-bottom: 40px;
}

.message-content {
  display: flex;
  gap: 40px;
  flex-wrap: wrap;
}

.message-form {
  flex: 1;
  min-width: 300px;
  background: white;
  border-radius: 8px;
  padding: 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.form-title {
  font-size: 20px;
  font-weight: bold;
  color: #333;
  margin-bottom: 20px;
  padding-bottom: 12px;
  border-bottom: 2px solid #f0f0f0;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  font-size: 14px;
  font-weight: 600;
  color: #333;
  margin-bottom: 8px;
}

.form-group input,
.form-group textarea {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  font-size: 14px;
  transition: all 0.3s ease;
}

.form-group input:focus,
.form-group textarea:focus {
  outline: none;
  border-color: #4a6fa5;
  box-shadow: 0 0 0 2px rgba(74, 111, 165, 0.1);
}

.form-group textarea {
  resize: vertical;
  min-height: 100px;
}

.submit-btn {
  width: 100%;
  padding: 12px;
  background: #4a6fa5;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.submit-btn:hover {
  background: #3a5a85;
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

.message-list {
  flex: 2;
  min-width: 300px;
  background: white;
  border-radius: 8px;
  padding: 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.list-title {
  font-size: 20px;
  font-weight: bold;
  color: #333;
  margin-bottom: 20px;
  padding-bottom: 12px;
  border-bottom: 2px solid #f0f0f0;
}

.no-messages {
  text-align: center;
  padding: 40px 0;
  color: #999;
}

.messages {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.message-item {
  padding: 20px;
  border-radius: 8px;
  background: #f9f9f9;
  transition: all 0.3s ease;
}

.message-item:hover {
  background: #f0f0f0;
  transform: translateY(-2px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.message-header {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
}

.message-avatar {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  overflow: hidden;
  margin-right: 16px;
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
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin-bottom: 4px;
}

.message-date {
  font-size: 12px;
  color: #999;
}

.message-body {
  font-size: 14px;
  line-height: 1.6;
  color: #666;
}

/* 返回按钮 */
.back-container {
  display: flex;
  justify-content: center;
  margin-top: 40px;
}

.back-btn {
  padding: 12px 32px;
  background: #4a6fa5;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 8px;
}

.back-btn:hover {
  background: #3a5a85;
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
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
    font-size: 16px;
  }
  
  .message-content {
    flex-direction: column;
  }
  
  .message-form,
  .message-list {
    min-width: 100%;
  }
  
  .message-item {
    padding: 16px;
  }
  
  .back-btn {
    padding: 10px 24px;
    font-size: 14px;
  }
}
</style>