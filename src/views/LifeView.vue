<template>
  <div class="life-view">
    <!-- 导航栏 -->
    <header class="portal-nav" :class="{ 'scrolled': isScrolled }">
      <div class="nav-content">
        <div class="nav-left">
          <h1 class="logo">Dream<span>blog</span></h1>
        </div>
        <nav class="main-nav">
          <router-link to="/" class="nav-link">首页</router-link>
          <router-link to="/blog" class="nav-link">文章</router-link>
          <router-link to="/life" class="nav-link active">碎碎念</router-link>
          <router-link to="/about" class="nav-link">关于</router-link>
          <router-link to="/message" class="nav-link">留言板</router-link>
        </nav>
        <div class="nav-right">
          <button class="search-btn" aria-label="搜索">🔍</button>
        </div>
      </div>
    </header>

    <!-- 中央内容区 -->
    <main class="life-main">
      <div class="life-container">
        <h2 class="life-title">碎碎念</h2>
        <p class="life-subtitle">记录生活中的点滴</p>
        
        <div class="life-posts">
          <div v-for="post in lifePosts" :key="post.id" class="life-post">
            <div class="life-post-content">
              <p>{{ post.content }}</p>
              <div class="life-post-meta">
                <span class="life-post-date">{{ formatDate(post.date) }}</span>
                <span class="life-post-likes">❤️ {{ post.likes }}</span>
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
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()
const isScrolled = ref(false)

// 碎碎念数据
const lifePosts = ref([
  {
    id: 1,
    content: '今天天气真好，适合出去走走。',
    date: '2026-02-26',
    likes: 12
  },
  {
    id: 2,
    content: '学习Vue3真的很有趣，Composition API非常强大。',
    date: '2026-02-25',
    likes: 8
  },
  {
    id: 3,
    content: '今天吃了顿好吃的，心情愉悦。',
    date: '2026-02-24',
    likes: 5
  },
  {
    id: 4,
    content: '工作虽然辛苦，但是很充实。',
    date: '2026-02-23',
    likes: 10
  },
  {
    id: 5,
    content: '今天看了一部好电影，推荐给大家。',
    date: '2026-02-22',
    likes: 15
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
  background: #f8f9fa;
}

.life-main {
  padding: 60px 0;
}

.life-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 0 20px;
}

.life-title {
  font-size: 36px;
  font-weight: bold;
  color: #333;
  text-align: center;
  margin-bottom: 16px;
}

.life-subtitle {
  font-size: 18px;
  color: #666;
  text-align: center;
  margin-bottom: 40px;
}

.life-posts {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.life-post {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.life-post:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.life-post-content p {
  font-size: 16px;
  line-height: 1.6;
  color: #333;
  margin-bottom: 16px;
}

.life-post-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 14px;
  color: #999;
}

.life-post-likes {
  cursor: pointer;
  transition: color 0.3s ease;
}

.life-post-likes:hover {
  color: #ff6b6b;
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
    padding: 16px;
  }
  
  .life-post-content p {
    font-size: 14px;
  }
  
  .back-btn {
    padding: 10px 24px;
    font-size: 14px;
  }
}
</style>