<template>
  <div class="portal-view" data-route="/">
    <!-- 导航栏 -->
    <header class="portal-nav">
      <div class="nav-content">
        <div class="nav-left">
          <h1 class="logo">Dream<span>blog</span></h1>
        </div>
        <nav class="main-nav">
          <router-link to="/" class="nav-link">首页</router-link>
          <router-link to="/blog" class="nav-link">文章</router-link>
          <router-link to="/blog" class="nav-link">关于</router-link>
        </nav>
        <div class="nav-right">
          <button class="search-btn" aria-label="搜索">🔍</button>
        </div>
      </div>
    </header>

    <!-- 个人博客区域 -->
    <section class="blog-section">
      <div class="blog-container">
        <!-- 左侧侧边栏 -->
        <aside class="left-sidebar">
          <!-- 个人简介 -->
          <div class="sidebar-module profile">
            <div class="avatar-container">
              <div class="avatar">
                <img src="@/assets/images/avatar.png" alt="Avatar" class="avatar-img">
              </div>
            </div>
            <h4 class="profile-name">Dream</h4>
            <p class="profile-desc">少年立志青云间，不负凌云万丈才</p>
            
            <!-- 统计数据 -->
            <div class="profile-stats">
              <div class="stat-item">
                <span class="stat-number">74</span>
                <span class="stat-label">文章</span>
              </div>
              <div class="stat-item">
                <span class="stat-number">18</span>
                <span class="stat-label">分类</span>
              </div>
              <div class="stat-item">
                <span class="stat-number">18</span>
                <span class="stat-label">标签</span>
              </div>
            </div>
            
            <!-- 功能链接 -->
            <div class="profile-links">
              <a href="#" class="link-item" title="GitHub">
                <span class="link-icon">🐱</span>
                <span class="link-text">GitHub</span>
              </a>
              <a href="#" class="link-item" title="RSS">
                <span class="link-icon">📡</span>
                <span class="link-text">RSS</span>
              </a>
            </div>
          </div>
          
          <!-- 导航菜单 -->
          <div class="sidebar-module navigation">
            <h3 class="module-title">导航菜单</h3>
            <ul class="nav-menu">
              <li><a href="#" class="nav-item">首页</a></li>
              <li><a href="#" class="nav-item">文章</a></li>
              <li><a href="#" class="nav-item">归档</a></li>
              <li><a href="#" class="nav-item">关于</a></li>
            </ul>
          </div>
        </aside>
        
        <!-- 主内容区 -->
        <div class="main-content">
          <HomeView />
        </div>
      </div>
      
      <!-- 右下角图标 -->
      <div class="bottom-right-icons">
        <button class="icon-btn" aria-label="返回顶部" @click="scrollToTop">
          <span class="icon">↑</span>
        </button>
        <button class="icon-btn" aria-label="主题切换" @click="toggleMode">
          <span class="icon">🌙</span>
        </button>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import HomeView from './HomeView.vue'

const currentMode = ref('light')

// 滚动到顶部
const scrollToTop = () => {
  window.scrollTo({
    top: 0,
    behavior: 'smooth'
  })
}

// 切换主题模式
const toggleMode = () => {
  if (currentMode.value === 'light') {
    currentMode.value = 'dark'
  } else {
    currentMode.value = 'light'
  }
  // 保存到localStorage
  localStorage.setItem('blogMode', currentMode.value)
}

onMounted(() => {
  // 初始化主题模式
  const savedMode = localStorage.getItem('blogMode')
  if (savedMode) {
    currentMode.value = savedMode
  }
})
</script>

<style scoped>
/* 导入字体 */
@import url('https://fonts.googleapis.com/css2?family=Ma+Shan+Zheng&family=Montserrat:wght@300;400;500;600;700&display=swap');

.portal-view {
  width: 100%;
  min-height: 100vh;
  font-family: 'PingFang SC', 'Montserrat', sans-serif;
  background: #f5f5f5;
}

/* 导航栏 */
.portal-nav {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 999;
  background-color: #ffffff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.nav-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 40px;
  height: 60px;
  max-width: 1200px;
  margin: 0 auto;
}

.logo {
  font-size: 24px;
  font-weight: bold;
  color: #333333;
}

.logo span {
  color: #4a6fa5;
}

.main-nav {
  display: flex;
  gap: 20px;
}

.nav-link {
  font-size: 16px;
  color: #333333;
  text-decoration: none;
  transition: color 0.3s ease;
}

.nav-link:hover {
  color: #4a6fa5;
}

.search-btn {
  width: 32px;
  height: 32px;
  border: none;
  background: #f0f0f0;
  border-radius: 50%;
  color: #333333;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.search-btn:hover {
  background: #e0e0e0;
  transform: scale(1.05);
}

/* 个人博客区域 */
.blog-section {
  min-height: 100vh;
  padding-top: 60px;
  position: relative;
}

/* 博客容器 */
.blog-container {
  display: flex;
  gap: 20px;
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

/* 左侧侧边栏 */
.left-sidebar {
  flex: 3;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

/* 侧边栏模块 */
.sidebar-module {
  background: #ffffff;
  border-radius: 8px;
  padding: 16px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

/* 导航菜单 */
.navigation {
  background: #f8f9fa;
}

.module-title {
  font-size: 16px;
  font-weight: bold;
  color: #333333;
  margin-bottom: 12px;
  border-bottom: 1px solid #e0e0e0;
  padding-bottom: 8px;
}

.nav-menu {
  list-style: none;
  margin-top: 8px;
}

.nav-item {
  display: block;
  padding: 8px 0;
  color: #333333;
  text-decoration: none;
  transition: all 0.3s ease;
  border-bottom: 1px solid #f0f0f0;
}

.nav-item:hover {
  color: #4a6fa5;
  padding-left: 8px;
}

/* 个人简介 */
.profile {
  background: #f8f9fa;
  text-align: center;
}

.avatar-container {
  display: flex;
  justify-content: center;
  margin: 12px 0;
}

.avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  background: #e9ecef;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #4a6fa5;
  font-size: 32px;
  font-weight: bold;
  border: 2px solid #dee2e6;
}

.avatar-img {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  object-fit: cover;
}

.profile-name {
  font-size: 18px;
  font-weight: bold;
  margin: 8px 0;
  color: #333333;
}

.profile-desc {
  font-size: 14px;
  color: #666666;
  margin-bottom: 16px;
}

/* 统计数据 */
.profile-stats {
  display: flex;
  justify-content: space-around;
  margin: 16px 0;
  padding: 12px;
  background: #ffffff;
  border-radius: 6px;
  border: 1px solid #e0e0e0;
}

.stat-item {
  text-align: center;
}

.stat-number {
  display: block;
  font-size: 18px;
  font-weight: bold;
  color: #4a6fa5;
}

.stat-label {
  display: block;
  font-size: 12px;
  color: #666666;
  margin-top: 4px;
}

/* 功能链接 */
.profile-links {
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin-top: 16px;
}

.link-item {
  display: flex;
  align-items: center;
  padding: 8px 12px;
  background: #ffffff;
  border-radius: 6px;
  color: #333333;
  text-decoration: none;
  transition: all 0.3s ease;
  border: 1px solid #e0e0e0;
}

.link-item:hover {
  background: #f0f8ff;
  border-color: #4a6fa5;
  transform: translateX(4px);
}

.link-icon {
  font-size: 16px;
  margin-right: 12px;
  width: 20px;
  text-align: center;
}

.link-text {
  font-size: 14px;
  flex: 1;
}

/* 主内容区 */
.main-content {
  flex: 7;
  background: #ffffff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

/* 右下角图标 */
.bottom-right-icons {
  position: fixed;
  bottom: 30px;
  right: 30px;
  display: flex;
  flex-direction: column;
  gap: 12px;
  z-index: 998;
}

.icon-btn {
  width: 48px;
  height: 48px;
  border: none;
  background: #ffffff;
  border-radius: 50%;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.icon-btn:hover {
  background: #f8f9fa;
  transform: translateY(-4px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}

.icon {
  font-size: 20px;
  color: #4a6fa5;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .nav-content {
    padding: 0 20px;
    height: 50px;
  }
  
  .logo {
    font-size: 20px;
  }
  
  .main-nav {
    gap: 15px;
  }
  
  .nav-link {
    font-size: 14px;
  }
  
  .blog-container {
    flex-direction: column;
    padding: 16px;
  }
  
  .left-sidebar {
    order: 2;
  }
  
  .main-content {
    order: 1;
  }
  
  .bottom-right-icons {
    bottom: 20px;
    right: 20px;
  }
  
  .icon-btn {
    width: 40px;
    height: 40px;
  }
  
  .icon {
    font-size: 16px;
  }
}
</style>