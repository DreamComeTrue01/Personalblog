<template>
  <div class="life-view">

    <!-- 背景装饰 -->
    <div class="background-decoration">
      <div class="decor-circle decor-circle-1"></div>
      <div class="decor-circle decor-circle-2"></div>
      <div class="decor-circle decor-circle-3"></div>
      <div class="decor-circle decor-circle-4"></div>
      <div class="decor-clouds">
        <div class="cloud cloud-1"></div>
        <div class="cloud cloud-2"></div>
        <div class="cloud cloud-3"></div>
      </div>
      <div class="decor-stars">
        <div v-for="i in 20" :key="i" class="star" :style="{ left: Math.random() * 100 + '%', top: Math.random() * 100 + '%', animationDelay: Math.random() * 5 + 's', animationDuration: (Math.random() * 3 + 2) + 's' }"></div>
      </div>
    </div>
    


    <!-- 中央内容区 -->
    <main class="life-main">
      <div class="life-container">
        <div class="life-header-section">
          <h2 class="life-title">
            <span class="title-word" v-for="(char, index) in titleText" :key="index" :style="{ animationDelay: `${index * 0.1}s` }">{{ char }}</span>
          </h2>
          <p class="life-subtitle">记录生活中的点滴，分享日常的感悟</p>
          <div class="life-intro">
            <p class="intro-text">这里是我的碎碎念空间，记录生活中的小确幸、小思考和小感悟。每一篇碎碎念都是我生活的真实写照，希望能与你分享我的喜怒哀乐，一起感受生活的美好与温暖。</p>
          </div>
          <div class="life-stats">
            <div class="stat-item">
              <span class="stat-number">{{ lifePosts.length }}</span>
              <span class="stat-label">条碎念</span>
            </div>
            <div class="stat-item">
              <span class="stat-number">{{ totalLikes }}</span>
              <span class="stat-label">次点赞</span>
            </div>
            <div class="stat-item">
              <span class="stat-number">{{ totalComments }}</span>
              <span class="stat-label">条评论</span>
            </div>
          </div>
          <div class="life-decoration">
            <span class="decoration-dot"></span>
            <span class="decoration-line"></span>
            <span class="decoration-dot"></span>
          </div>
        </div>
        
        <div class="life-posts">
          <div v-for="(post, index) in lifePosts" :key="post.id" class="life-post" :style="{ animationDelay: `${index * 0.1}s` }" @click="handlePostClick(post.id)">
            <div class="life-post-content">
              <p>{{ post.content }}</p>
              <div class="life-post-meta">
                <div class="meta-left">
                  <span class="life-post-date">{{ formatDate(post.date) }}</span>
                  <span class="life-post-views">
                    <span class="views-icon">👁️</span>
                    <span class="views-count">{{ post.views }}</span>
                  </span>
                </div>
                <div class="meta-right">
                  <button class="life-post-reply" @click.stop="toggleReplyForm(post.id)">
                    <span class="reply-icon">💬</span>
                    <span class="reply-text">回复</span>
                    <span class="reply-count">{{ post.replies ? post.replies.length : 0 }}</span>
                  </button>
                  <button class="life-post-likes" @click.stop="likePost(post.id)" :class="{ 'liked': post.isLiked }">
                    <span class="like-icon">❤️</span>
                    <span class="like-count">{{ post.likes }}</span>
                  </button>
                </div>
              </div>
              
              <!-- 回复表单 -->
              <div v-if="replyFormVisible[post.id]" class="reply-form" @click.stop>
                <form @submit.prevent="submitReply(post.id)">
                  <div class="form-group">
                    <input type="text" v-model="replyForms[post.id].name" placeholder="你的昵称" required class="form-input">
                  </div>
                  <div class="form-group">
                    <textarea v-model="replyForms[post.id].content" placeholder="写下你的回复..." rows="3" required class="form-textarea"></textarea>
                  </div>
                  <div class="form-actions">
                    <button type="button" class="cancel-btn" @click.stop="toggleReplyForm(post.id)">取消</button>
                    <button type="submit" class="submit-reply-btn">提交回复</button>
                  </div>
                </form>
              </div>
              
              <!-- 回复列表 -->
              <div v-if="post.replies && post.replies.length > 0" class="replies-list" @click.stop>
                <h4 class="replies-title">回复 ({{ post.replies.length }})</h4>
                <div v-for="(reply, index) in post.replies" :key="index" class="reply-item">
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
const isScrolled = ref(false)

// 标题文本
const titleText = ref('碎碎念')

// 碎碎念数据
const lifePosts = ref([
  {
    id: 1,
    content: '今天天气真好，适合出去走走。',
    date: '2026-02-26',
    likes: 12,
    views: 128,
    replies: []
  },
  {
    id: 2,
    content: '学习Vue3真的很有趣，Composition API非常强大。',
    date: '2026-02-25',
    likes: 8,
    views: 95,
    replies: []
  },
  {
    id: 3,
    content: '今天吃了顿好吃的，心情愉悦。',
    date: '2026-02-24',
    likes: 5,
    views: 67,
    replies: []
  },
  {
    id: 4,
    content: '工作虽然辛苦，但是很充实。',
    date: '2026-02-23',
    likes: 10,
    views: 89,
    replies: []
  },
  {
    id: 5,
    content: '今天看了一部好电影，推荐给大家。',
    date: '2026-02-22',
    likes: 15,
    views: 142,
    replies: []
  }
])

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

// 计算属性
const totalLikes = computed(() => {
  return lifePosts.value.reduce((sum, post) => sum + (post.likes || 0), 0)
})

const totalWords = computed(() => {
  return lifePosts.value.reduce((sum, post) => sum + (post.content ? post.content.length : 0), 0)
})

const totalComments = computed(() => {
  return lifePosts.value.reduce((sum, post) => sum + (post.replies ? post.replies.length : 0), 0)
})

// 回复功能相关
const replyFormVisible = ref({})
const replyForms = ref({})

// 点赞功能
const likePost = (postId) => {
  const post = lifePosts.value.find(p => p.id === postId)
  if (post) {
    if (post.isLiked) {
      post.likes = Math.max(0, (post.likes || 0) - 1)
      post.isLiked = false
    } else {
      post.likes = (post.likes || 0) + 1
      post.isLiked = true
    }
  }
}

// 增加阅读次数
const incrementViews = (postId) => {
  const post = lifePosts.value.find(p => p.id === postId)
  if (post) {
    post.views = (post.views || 0) + 1
  }
}

// 处理文章点击
const handlePostClick = (postId) => {
  // 跳转到详情页
  router.push(`/life/${postId}`)
}

// 切换回复表单显示/隐藏
const toggleReplyForm = (postId) => {
  replyFormVisible.value[postId] = !replyFormVisible.value[postId]
  if (replyFormVisible.value[postId]) {
    // 初始化回复表单数据
    replyForms.value[postId] = {
      name: '',
      content: ''
    }
  }
}

// 提交回复
const submitReply = (postId) => {
  const post = lifePosts.value.find(p => p.id === postId)
  if (post && replyForms.value[postId]) {
    const newReply = {
      name: replyForms.value[postId].name,
      content: replyForms.value[postId].content,
      date: new Date().toISOString().split('T')[0]
    }
    if (!post.replies) {
      post.replies = []
    }
    post.replies.push(newReply)
    replyFormVisible.value[postId] = false
  }
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
.life-view {
  width: 100%;
  min-height: 100vh;
  position: relative;
  overflow: hidden;
  background-image: url('https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=colorful%20vibrant%20background%20for%20life%20journal%2C%20soft%20pastel%20colors%2C%20distinct%20patterns%2C%20inviting%20atmosphere%2C%20minimalist%20design&image_size=landscape_16_9');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
}

.life-view::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.7) 0%, rgba(255, 255, 255, 0.6) 100%);
  z-index: 0;
}

/* 背景装饰 */
.background-decoration {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 0;
}

.decor-circle {
  position: absolute;
  border-radius: 50%;
  animation: float 6s ease-in-out infinite;
}

.decor-circle-1 {
  width: 200px;
  height: 200px;
  top: 10%;
  left: 5%;
  background: radial-gradient(circle, rgba(236, 72, 153, 0.1) 0%, transparent 70%);
  animation-delay: 0s;
}

.decor-circle-2 {
  width: 150px;
  height: 150px;
  top: 60%;
  right: 8%;
  background: radial-gradient(circle, rgba(139, 92, 246, 0.1) 0%, transparent 70%);
  animation-delay: 2s;
}

.decor-circle-3 {
  width: 100px;
  height: 100px;
  top: 30%;
  right: 15%;
  background: radial-gradient(circle, rgba(59, 130, 246, 0.1) 0%, transparent 70%);
  animation-delay: 1s;
}

.decor-circle-4 {
  width: 80px;
  height: 80px;
  top: 70%;
  left: 12%;
  background: radial-gradient(circle, rgba(74, 179, 111, 0.1) 0%, transparent 70%);
  animation-delay: 3s;
}

.decor-clouds {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

.cloud {
  position: absolute;
  background: rgba(255, 255, 255, 0.6);
  border-radius: 50px;
  animation: float 15s ease-in-out infinite;
}

.cloud-1 {
  width: 150px;
  height: 60px;
  top: 20%;
  left: 10%;
  animation-delay: 0s;
}

.cloud-2 {
  width: 120px;
  height: 50px;
  top: 40%;
  right: 15%;
  animation-delay: 3s;
}

.cloud-3 {
  width: 100px;
  height: 40px;
  top: 70%;
  left: 30%;
  animation-delay: 6s;
}

.decor-stars {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

.star {
  position: absolute;
  width: 2px;
  height: 2px;
  background: #8b5cf6;
  border-radius: 50%;
  animation: twinkle 3s ease-in-out infinite;
}



/* 中央内容区 */
.life-main {
  padding: 40px 0 80px;
  position: relative;
  z-index: 1;
}

.life-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 0 20px;
}

.life-header-section {
  text-align: center;
  margin-bottom: 60px;
  position: relative;
  z-index: 1;
  padding: 20px 20px;
}

.life-title {
  font-size: 64px;
  font-weight: bold;
  margin-bottom: 32px;
  font-family: 'Ma Shan Zheng', cursive;
  display: inline-block;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.2);
  background: linear-gradient(135deg, #8b5cf6 0%, #ec4899 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  padding: 28px 56px;
  border-radius: 60px;
  box-shadow: 0 12px 32px rgba(139, 92, 246, 0.5);
  transition: all 0.3s ease;
  animation: titleFloat 1s ease-out;
  background-color: white;
  border: 3px solid rgba(139, 92, 246, 0.4);
  position: relative;
  z-index: 1;
  outline: 2px solid rgba(255, 255, 255, 0.8);
  outline-offset: 2px;
}

.life-title:hover {
  transform: scale(1.05);
  box-shadow: 0 16px 40px rgba(139, 92, 246, 0.6);
  border-color: rgba(139, 92, 246, 0.6);
}

.title-word {
  display: inline-block;
  opacity: 0;
  transform: translateY(20px);
  animation: fadeInUp 0.6s ease-out both;
}

.life-subtitle {
  font-size: 18px;
  color: #666;
  margin-bottom: 32px;
  animation: fadeInUp 0.8s ease-out 0.2s both;
}

.life-intro {
  max-width: 700px;
  margin: 0 auto 32px;
  animation: fadeInUp 0.8s ease-out 0.3s both;
}

.intro-text {
  font-size: 16px;
  color: #666;
  line-height: 1.6;
  text-align: center;
  padding: 20px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
  border: 1px solid #e9ecef;
  position: relative;
  z-index: 1;
}

.life-stats {
  display: flex;
  justify-content: center;
  gap: 30px;
  margin: 32px 0;
  animation: fadeInUp 0.8s ease-out 0.4s both;
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
  z-index: 1;
}

.stat-item::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 4px;
  background: linear-gradient(90deg, #8b5cf6 0%, #ec4899 100%);
}

.stat-item:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
  border-color: #8b5cf6;
}

.stat-number {
  display: block;
  font-size: 28px;
  font-weight: bold;
  color: #8b5cf6;
  margin-bottom: 6px;
  font-family: 'Montserrat', sans-serif;
  transition: all 0.3s ease;
}

.stat-item:hover .stat-number {
  color: #ec4899;
}

.stat-label {
  display: block;
  font-size: 13px;
  color: #666;
  font-weight: 500;
  transition: all 0.3s ease;
}

.stat-item:hover .stat-label {
  color: #8b5cf6;
}

.life-decoration {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  margin-top: 24px;
  animation: fadeInUp 0.8s ease-out 0.5s both;
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
  background: linear-gradient(90deg, #8b5cf6 0%, #ec4899 100%);
  opacity: 0.8;
}

.life-posts {
  display: flex;
  flex-direction: column;
  gap: 24px;
  position: relative;
  z-index: 1;
  max-width: 700px;
  margin: 0 auto;
}

.life-post {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 20px;
  padding: 40px;
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  opacity: 0;
  transform: translateY(20px);
  animation: fadeInUp 0.8s ease-out both;
  border-left: 4px solid #8b5cf6;
  position: relative;
  overflow: hidden;
  backdrop-filter: blur(10px);
}

.life-post::before {
  content: '';
  position: absolute;
  top: 0;
  right: 0;
  width: 120px;
  height: 120px;
  background: radial-gradient(circle, rgba(139, 92, 246, 0.1) 0%, transparent 70%);
  border-radius: 50%;
  z-index: 0;
}

.life-post:hover {
  transform: translateY(-8px);
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.15);
  border-color: #ec4899;
}

.life-post:hover::before {
  transform: scale(1.3);
  transition: transform 0.3s ease;
}

.life-post-content {
  position: relative;
  z-index: 1;
}

.life-post-content p {
  font-size: 18px;
  line-height: 1.8;
  color: #333;
  margin-bottom: 24px;
  font-weight: 400;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 12px;
  border-left: 4px solid #8b5cf6;
  transition: all 0.3s ease;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.life-post:hover .life-post-content p {
  background: #f5f3ff;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transform: translateX(8px);
}

.life-post-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 14px;
  color: #999;
  border-top: 1px solid #f0f0f0;
  padding-top: 16px;
  position: relative;
  z-index: 1;
  flex-wrap: wrap;
  gap: 12px;
}

.meta-left {
  display: flex;
  align-items: center;
  gap: 16px;
  flex-wrap: wrap;
}

.meta-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.life-post-date {
  font-weight: 500;
  color: #666;
}

.life-post-views {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 14px;
  color: #666;
}

.views-icon {
  font-size: 14px;
}

.life-post-reply {
  display: flex;
  align-items: center;
  gap: 6px;
  cursor: pointer;
  transition: all 0.3s ease;
  padding: 6px 14px;
  border-radius: 20px;
  border: 1px solid #e9ecef;
  background: #f8f9fa;
  color: #666;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.reply-count {
  font-size: 14px;
  font-weight: 500;
}

.life-post-reply:hover {
  background: #8b5cf6;
  color: white;
  transform: translateY(-1px);
  box-shadow: 0 4px 8px rgba(139, 92, 246, 0.3);
  border-color: #8b5cf6;
}

.life-post-likes {
  display: flex;
  align-items: center;
  gap: 6px;
  cursor: pointer;
  transition: all 0.3s ease;
  padding: 6px 14px;
  border-radius: 20px;
  border: 1px solid #e9ecef;
  background: #f8f9fa;
  color: #666;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.life-post-likes:hover {
  background: #8b5cf6;
  color: white;
  transform: translateY(-1px);
  box-shadow: 0 4px 8px rgba(139, 92, 246, 0.3);
  border-color: #8b5cf6;
}

.life-post-likes.liked {
  background: #8b5cf6;
  color: white;
  box-shadow: 0 4px 8px rgba(139, 92, 246, 0.3);
  border-color: #8b5cf6;
}

.like-icon {
  font-size: 16px;
  transition: transform 0.3s ease;
}

.life-post-likes.liked .like-icon {
  transform: scale(1.2);
}

/* 回复表单 */
.reply-form {
  margin-top: 20px;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 12px;
  border: 1px solid #e9ecef;
  position: relative;
  z-index: 1;
}

.form-group {
  margin-bottom: 12px;
}

.form-input {
  width: 100%;
  padding: 10px 14px;
  border: 1px solid #e9ecef;
  border-radius: 8px;
  font-size: 14px;
  transition: all 0.3s ease;
  font-family: inherit;
  background: white;
}

.form-input:focus {
  outline: none;
  border-color: #8b5cf6;
  box-shadow: 0 0 0 3px rgba(139, 92, 246, 0.1);
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(139, 92, 246, 0.15);
}

.form-textarea {
  width: 100%;
  padding: 12px 14px;
  border: 1px solid #e9ecef;
  border-radius: 8px;
  font-size: 14px;
  transition: all 0.3s ease;
  font-family: inherit;
  resize: vertical;
  min-height: 80px;
  line-height: 1.5;
  background: white;
}

.form-textarea:focus {
  outline: none;
  border-color: #8b5cf6;
  box-shadow: 0 0 0 3px rgba(139, 92, 246, 0.1);
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(139, 92, 246, 0.15);
}

.form-actions {
  display: flex;
  gap: 12px;
  justify-content: flex-end;
  margin-top: 16px;
}

.cancel-btn {
  padding: 8px 16px;
  background: #f8f9fa;
  border: 1px solid #e9ecef;
  border-radius: 20px;
  font-size: 14px;
  color: #666;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.cancel-btn:hover {
  background: #e9ecef;
  transform: translateY(-1px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.submit-reply-btn {
  padding: 8px 16px;
  background: #8b5cf6;
  border: 1px solid #8b5cf6;
  border-radius: 20px;
  font-size: 14px;
  color: white;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 2px 4px rgba(139, 92, 246, 0.2);
}

.submit-reply-btn:hover {
  background: #7c3aed;
  transform: translateY(-1px);
  box-shadow: 0 4px 8px rgba(139, 92, 246, 0.3);
  border-color: #7c3aed;
}

/* 回复列表 */
.replies-list {
  margin-top: 20px;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 12px;
  border: 1px solid #e9ecef;
  position: relative;
  z-index: 1;
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
  border-radius: 8px;
  margin-bottom: 8px;
  border-left: 3px solid #8b5cf6;
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
  margin-top: 60px;
  position: relative;
  z-index: 1;
}

.back-btn {
  padding: 16px 40px;
  background: linear-gradient(135deg, #8b5cf6 0%, #ec4899 100%);
  color: white;
  border: none;
  border-radius: 30px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 10px;
  box-shadow: 0 6px 20px rgba(139, 92, 246, 0.3);
  position: relative;
  overflow: hidden;
  border: 2px solid rgba(255, 255, 255, 0.3);
}

.back-btn::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.3), transparent);
  transition: all 0.6s ease;
}

.back-btn:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(139, 92, 246, 0.4);
}

.back-btn:hover::before {
  left: 100%;
}

/* 页脚 */
.portal-footer {
  background: rgba(255, 255, 255, 0.95);
  padding: 40px 20px 30px;
  border-top: 1px solid rgba(139, 92, 246, 0.2);
  position: relative;
  z-index: 1;
  backdrop-filter: blur(10px);
  margin-top: 60px;
}

.portal-footer::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 4px;
  background: linear-gradient(90deg, #8b5cf6 0%, #ec4899 100%);
}

.footer-content {
  max-width: 900px;
  margin: 0 auto;
  display: flex;
  justify-content: space-around;
  flex-wrap: wrap;
  gap: 40px;
  text-align: center;
}

.footer-section {
  min-width: 200px;
  padding: 20px;
  background: rgba(255, 255, 255, 0.8);
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.footer-section:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 16px rgba(139, 92, 246, 0.1);
}

.footer-section h3 {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  margin-bottom: 16px;
  background: linear-gradient(135deg, #8b5cf6 0%, #ec4899 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.footer-section p {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
  margin: 0;
}

/* 动画效果 */
@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes slideIn {
  from {
    width: 0;
  }
  to {
    width: 100%;
  }
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

@keyframes float {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-20px);
  }
}

@keyframes wave {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-10px);
  }
}

@keyframes twinkle {
  0%, 100% {
    opacity: 0.3;
  }
  50% {
    opacity: 1;
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .page-header {
    padding: 40px 20px;
  }
  
  .page-title {
    font-size: 36px;
  }
  
  .life-container {
    padding: 0 16px;
  }
  
  .life-title {
    font-size: 28px;
  }
  
  .life-subtitle {
    font-size: 16px;
  }
  
  .life-post {
    padding: 20px;
  }
  
  .life-post-content p {
    font-size: 14px;
  }
  
  .back-btn {
    padding: 12px 28px;
    font-size: 14px;
  }
  
  .footer-content {
    gap: 20px;
  }
}

@media (max-width: 480px) {
  .page-title {
    font-size: 28px;
  }
  
  .header-actions {
    flex-direction: column;
    align-items: center;
  }
  
  .action-btn {
    width: 200px;
    justify-content: center;
  }
  
  .life-title {
    font-size: 24px;
  }
  
  .life-post {
    padding: 16px;
  }
}
</style>