<template>
  <div class="life-detail-view">

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
        <!-- 返回按钮 -->
        <div class="back-container">
          <button class="back-btn" @click="goBack">
            <span class="btn-icon">←</span>
            返回碎碎念列表
          </button>
        </div>

        <div v-if="post" class="life-detail">
          <div class="life-detail-header">
            <h1 class="life-detail-title">碎碎念</h1>
            <div class="life-detail-meta">
              <span class="life-detail-date">{{ formatDate(post.date) }}</span>
              <span class="life-detail-views">
                <span class="views-icon">👁️</span>
                <span class="views-count">{{ post.views }}</span>
              </span>
              <span class="life-detail-comments">
                <span class="comments-icon">💬</span>
                <span class="comments-count">{{ post.replies ? post.replies.length : 0 }}</span>
              </span>
            </div>
          </div>

          <div class="life-detail-content">
            <p>{{ post.content }}</p>
          </div>

          <div class="life-detail-actions">
            <button class="life-post-likes" @click="likePost(post.id)" :class="{ 'liked': post.isLiked }">
              <span class="like-icon">❤️</span>
              <span class="like-count">{{ post.likes }}</span>
            </button>
            <button class="life-post-reply" @click="toggleReplyForm">
              <span class="reply-icon">💬</span>
              <span class="reply-text">回复</span>
            </button>
          </div>

          <!-- 回复表单 -->
          <div v-if="replyFormVisible" class="reply-form">
            <form @submit.prevent="submitReply">
              <div class="form-group">
                <input type="text" v-model="replyForm.name" placeholder="你的昵称" required class="form-input">
              </div>
              <div class="form-group">
                <textarea v-model="replyForm.content" placeholder="写下你的回复..." rows="3" required class="form-textarea"></textarea>
              </div>
              <div class="form-actions">
                <button type="button" class="cancel-btn" @click="toggleReplyForm">取消</button>
                <button type="submit" class="submit-reply-btn">提交回复</button>
              </div>
            </form>
          </div>

          <!-- 回复列表 -->
          <div v-if="post.replies && post.replies.length > 0" class="replies-list">
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

        <div v-else class="loading">
          <p>加载中...</p>
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
const post = ref(null)
const replyFormVisible = ref(false)
const replyForm = ref({ name: '', content: '' })

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

// 增加阅读次数
const incrementViews = (postId) => {
  const foundPost = lifePosts.value.find(p => p.id === postId)
  if (foundPost) {
    foundPost.views = (foundPost.views || 0) + 1
    post.value = foundPost
  }
}

// 点赞功能
const likePost = (postId) => {
  const foundPost = lifePosts.value.find(p => p.id === postId)
  if (foundPost) {
    if (foundPost.isLiked) {
      foundPost.likes = Math.max(0, (foundPost.likes || 0) - 1)
      foundPost.isLiked = false
    } else {
      foundPost.likes = (foundPost.likes || 0) + 1
      foundPost.isLiked = true
    }
    post.value = foundPost
  }
}

// 切换回复表单显示/隐藏
const toggleReplyForm = () => {
  replyFormVisible.value = !replyFormVisible.value
  if (replyFormVisible.value) {
    replyForm.value = { name: '', content: '' }
  }
}

// 提交回复
const submitReply = () => {
  if (post.value) {
    const newReply = {
      name: replyForm.value.name,
      content: replyForm.value.content,
      date: new Date().toISOString().split('T')[0]
    }
    if (!post.value.replies) {
      post.value.replies = []
    }
    post.value.replies.push(newReply)
    // 更新原数据
    const foundPost = lifePosts.value.find(p => p.id === post.value.id)
    if (foundPost) {
      foundPost.replies = post.value.replies
    }
    replyFormVisible.value = false
  }
}

// 返回上一页
const goBack = () => {
  router.push('/life')
}

// 获取文章详情
const getPostDetail = () => {
  const postId = parseInt(route.params.id)
  const foundPost = lifePosts.value.find(p => p.id === postId)
  if (foundPost) {
    post.value = foundPost
    incrementViews(postId)
  }
}

onMounted(() => {
  getPostDetail()
})
</script>

<style scoped>
.life-detail-view {
  width: 100%;
  min-height: 100vh;
  position: relative;
  overflow: hidden;
  background-image: url('https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=colorful%20vibrant%20background%20for%20life%20journal%2C%20soft%20pastel%20colors%2C%20distinct%20patterns%2C%20inviting%20atmosphere%2C%20minimalist%20design&image_size=landscape_16_9');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
}

.life-detail-view::before {
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

.back-container {
  display: flex;
  justify-content: flex-start;
  margin-bottom: 40px;
  position: relative;
  z-index: 1;
}

.back-btn {
  padding: 12px 24px;
  background: linear-gradient(135deg, #8b5cf6 0%, #ec4899 100%);
  color: white;
  border: none;
  border-radius: 30px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 8px;
  box-shadow: 0 4px 12px rgba(139, 92, 246, 0.3);
  border: 2px solid rgba(255, 255, 255, 0.3);
}

.back-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(139, 92, 246, 0.4);
}

.life-detail {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 20px;
  padding: 60px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
  position: relative;
  overflow: hidden;
  backdrop-filter: blur(10px);
  border-left: 4px solid #8b5cf6;
}

.life-detail::before {
  content: '';
  position: absolute;
  top: 0;
  right: 0;
  width: 150px;
  height: 150px;
  background: radial-gradient(circle, rgba(139, 92, 246, 0.1) 0%, transparent 70%);
  border-radius: 50%;
  z-index: 0;
}

.life-detail-header {
  text-align: center;
  margin-bottom: 40px;
  position: relative;
  z-index: 1;
}

.life-detail-title {
  font-size: 48px;
  font-weight: bold;
  margin-bottom: 24px;
  font-family: 'Ma Shan Zheng', cursive;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.2);
  background: linear-gradient(135deg, #8b5cf6 0%, #ec4899 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.life-detail-meta {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 24px;
  font-size: 16px;
  color: #666;
}

.life-detail-date {
  font-weight: 500;
}

.life-detail-views {
  display: flex;
  align-items: center;
  gap: 6px;
}

.views-icon {
  font-size: 16px;
}

.life-detail-comments {
  display: flex;
  align-items: center;
  gap: 6px;
}

.comments-icon {
  font-size: 16px;
}

.life-detail-content {
  margin-bottom: 40px;
  position: relative;
  z-index: 1;
}

.life-detail-content p {
  font-size: 20px;
  line-height: 2;
  color: #333;
  padding: 30px;
  background: #f8f9fa;
  border-radius: 12px;
  border-left: 4px solid #8b5cf6;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.life-detail-actions {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-bottom: 40px;
  position: relative;
  z-index: 1;
}

.life-post-reply {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  padding: 10px 20px;
  border-radius: 25px;
  border: 1px solid #e9ecef;
  background: #f8f9fa;
  color: #666;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  font-size: 16px;
}

.life-post-reply:hover {
  background: #8b5cf6;
  color: white;
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(139, 92, 246, 0.3);
  border-color: #8b5cf6;
}

.life-post-likes {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  padding: 10px 20px;
  border-radius: 25px;
  border: 1px solid #e9ecef;
  background: #f8f9fa;
  color: #666;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  font-size: 16px;
}

.life-post-likes:hover {
  background: #8b5cf6;
  color: white;
  transform: translateY(-2px);
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
  font-size: 18px;
  transition: transform 0.3s ease;
}

.life-post-likes.liked .like-icon {
  transform: scale(1.2);
}

/* 回复表单 */
.reply-form {
  margin-top: 30px;
  padding: 24px;
  background: #f8f9fa;
  border-radius: 12px;
  border: 1px solid #e9ecef;
  position: relative;
  z-index: 1;
}

.form-group {
  margin-bottom: 16px;
}

.form-input {
  width: 100%;
  padding: 12px 16px;
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
  padding: 14px 16px;
  border: 1px solid #e9ecef;
  border-radius: 8px;
  font-size: 14px;
  transition: all 0.3s ease;
  font-family: inherit;
  resize: vertical;
  min-height: 100px;
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
  margin-top: 30px;
  padding: 24px;
  background: #f8f9fa;
  border-radius: 12px;
  border: 1px solid #e9ecef;
  position: relative;
  z-index: 1;
}

.replies-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin-bottom: 16px;
}

.reply-item {
  padding: 16px;
  background: white;
  border-radius: 8px;
  margin-bottom: 12px;
  border-left: 3px solid #8b5cf6;
}

.reply-item:last-child {
  margin-bottom: 0;
}

.reply-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.reply-name {
  font-size: 14px;
  font-weight: 600;
  color: #333;
}

.reply-date {
  font-size: 12px;
  color: #999;
}

.reply-content {
  font-size: 14px;
  line-height: 1.4;
  color: #666;
}

.loading {
  text-align: center;
  padding: 100px 0;
  font-size: 18px;
  color: #666;
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

@keyframes float {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-20px);
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
  .life-container {
    padding: 0 16px;
  }
  
  .life-detail {
    padding: 40px 20px;
  }
  
  .life-detail-title {
    font-size: 36px;
  }
  
  .life-detail-content p {
    font-size: 16px;
    padding: 20px;
  }
  
  .back-btn {
    padding: 10px 20px;
    font-size: 13px;
  }
  
  .life-detail-actions {
    flex-direction: column;
    align-items: center;
  }
  
  .life-post-reply,
  .life-post-likes {
    width: 200px;
    justify-content: center;
  }
  
  .footer-content {
    gap: 20px;
  }
}

@media (max-width: 480px) {
  .life-detail {
    padding: 30px 16px;
  }
  
  .life-detail-title {
    font-size: 28px;
  }
  
  .life-detail-content p {
    font-size: 14px;
    padding: 16px;
  }
}
</style>