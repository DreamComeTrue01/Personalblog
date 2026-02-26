<template>
  <div :class="['app-container', currentMode]">
    <!-- 门户网站页面 -->
    <template v-if="$route.path === '/'">
      <router-view />
    </template>
    
    <!-- 文章详情页 -->
    <template v-else-if="$route.path.startsWith('/article/')">
      <router-view />
    </template>
    
    <!-- 博客页面 -->
    <template v-else>
      <!-- 顶部导航栏 -->
      <header class="top-nav">
        <div class="nav-content">
          <div class="nav-left">
            <button class="back-btn" @click="goBack">← 返回</button>
            <h1 class="blog-name">Dream's blog</h1>
            <p class="blog-slogan">Life is a coding, I will debug it.</p>
          </div>
          <div class="nav-right">
            <nav class="main-nav">
              <router-link to="/blog" class="nav-link">杂谈</router-link>
              <router-link to="/life" class="nav-link">生活</router-link>
              <router-link to="/learn" class="nav-link">Learn</router-link>
              <router-link to="/build" class="nav-link">建站</router-link>
            </nav>
            <button class="mode-toggle" @click="toggleMode" aria-label="切换主题模式">
              <span v-if="currentMode === 'light'" class="mode-icon sun">☀️</span>
              <span v-else-if="currentMode === 'dark'" class="mode-icon moon">🌙</span>
              <span v-else class="mode-icon black">⚫</span>
            </button>
          </div>
          <!-- 移动端汉堡菜单 -->
          <button class="mobile-menu-toggle" @click="toggleSidebar" aria-label="菜单">
            <div class="hamburger"></div>
          </button>
        </div>
      </header>

      <!-- 主体内容区 -->
      <main class="main-content">
        <div class="content-wrapper">
          <!-- 左侧主内容 -->
          <section class="main-section">
            <router-view />
          </section>
          
          <!-- 右侧侧边栏 -->
          <aside class="sidebar" :class="{ 'sidebar-open': isSidebarOpen }">
            <!-- 博主头像模块 -->
            <div class="sidebar-module">
              <div class="avatar-container">
                <img src="@/assets/images/avatar.png" alt="Avatar" class="avatar-img">
              </div>
              <h4 class="profile-name">Dream</h4>
              <p class="avatar-desc">记录生活，分享知识</p>
            </div>
            
            <!-- 分类列表 -->
            <div class="sidebar-module">
              <h3 class="module-title">分类</h3>
              <ul class="category-list">
                <li><router-link to="/blog" class="category-item">杂谈</router-link></li>
                <li><router-link to="/life" class="category-item">生活</router-link></li>
                <li><router-link to="/learn" class="category-item">Learn</router-link></li>
                <li><router-link to="/build" class="category-item">建站</router-link></li>
              </ul>
            </div>
            
            <!-- 标签云 -->
            <div class="sidebar-module">
              <h3 class="module-title">标签</h3>
              <div class="tag-cloud">
                <span class="tag">Vue</span>
                <span class="tag">TypeScript</span>
                <span class="tag">前端</span>
                <span class="tag">生活</span>
                <span class="tag">学习</span>
                <span class="tag">实习</span>
                <span class="tag">考研</span>
                <span class="tag">建站</span>
              </div>
            </div>
            
            <!-- 时间归档 -->
            <div class="sidebar-module">
              <h3 class="module-title">归档</h3>
              <ul class="archive-list">
                <li><router-link to="/archive/2026/02" class="archive-item">2026年2月</router-link></li>
                <li><router-link to="/archive/2025/12" class="archive-item">2025年12月</router-link></li>
                <li><router-link to="/archive/2025/04" class="archive-item">2025年4月</router-link></li>
              </ul>
            </div>
          </aside>
        </div>
      </main>

      <!-- 页脚 -->
      <footer class="footer">
        <p class="copyright">© 2026 Echo's blog 保留所有权利</p>
      </footer>
    </template>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

// 主题模式
const currentMode = ref('light')
// 侧边栏状态（移动端）
const isSidebarOpen = ref(false)

// 切换主题模式
const toggleMode = () => {
  if (currentMode.value === 'light') {
    currentMode.value = 'dark'
  } else if (currentMode.value === 'dark') {
    currentMode.value = 'black'
  } else {
    currentMode.value = 'light'
  }
  // 保存到localStorage
  localStorage.setItem('blogMode', currentMode.value)
}

// 切换侧边栏（移动端）
const toggleSidebar = () => {
  isSidebarOpen.value = !isSidebarOpen.value
}

// 返回上一页
const goBack = () => {
  router.back()
}

// 初始化主题模式
onMounted(() => {
  const savedMode = localStorage.getItem('blogMode')
  if (savedMode) {
    currentMode.value = savedMode
  }
})
</script>

<style>
/* 全局样式重置 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
  line-height: 1.6;
  transition: all 0.3s ease;
}

/* 主题模式样式 */
.app-container.light {
  background: url('@/assets/images/backgrounds/light-bg.jpg');
  background-size: cover;
  background-attachment: fixed;
  background-position: center;
  color: #333333;
}

.app-container.dark {
  background: url('@/assets/images/backgrounds/dark-bg.jpg');
  background-size: cover;
  background-attachment: fixed;
  background-position: center;
  color: #eaeaea;
}

.app-container.black {
  background: url('@/assets/images/backgrounds/black-bg.jpg');
  background-size: cover;
  background-attachment: fixed;
  background-position: center;
  color: #e0e0e0;
}

/* 根路径特殊样式 - 移除背景色以显示森林背景 */
.app-container:has(> [data-route="/"]) {
  background: url('https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=misty%20forest%20with%20sunlight%20streaming%20through%20trees%2C%20peaceful%20natural%20landscape&image_size=landscape_16_9') no-repeat center center fixed;
  background-size: cover;
}

/* 顶部导航栏 */
.top-nav {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 60px;
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.98), rgba(240, 244, 250, 0.98));
  backdrop-filter: blur(15px);
  border-bottom: 1px solid rgba(173, 216, 230, 0.5);
  z-index: 999;
  transition: all 0.3s ease;
  box-shadow: 0 2px 16px rgba(100, 149, 237, 0.15);
}

.app-container.dark .top-nav {
  background: linear-gradient(135deg, rgba(20, 20, 30, 0.98), rgba(24, 24, 36, 0.98));
  border-bottom: 1px solid rgba(70, 130, 180, 0.3);
  box-shadow: 0 2px 16px rgba(0, 0, 0, 0.3);
}

.app-container.black .top-nav {
  background: linear-gradient(135deg, rgba(10, 10, 20, 0.98), rgba(17, 17, 25, 0.98));
  border-bottom: 1px solid rgba(50, 80, 120, 0.3);
  box-shadow: 0 2px 16px rgba(0, 0, 0, 0.4);
}

.nav-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 100%;
  padding: 0 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.nav-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.back-btn {
  padding: 8px 16px;
  background: linear-gradient(135deg, #4a6fa5, #6b8cce);
  border: none;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 500;
  color: white;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 6px;
  box-shadow: 0 2px 8px rgba(74, 111, 165, 0.2);
}

.back-btn:hover {
  background: linear-gradient(135deg, #3a5a85, #4a6fa5);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(74, 111, 165, 0.3);
}

.app-container.dark .back-btn {
  background: linear-gradient(135deg, #333333, #444444);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.3);
}

.app-container.dark .back-btn:hover {
  background: linear-gradient(135deg, #444444, #555555);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.4);
}

.app-container.black .back-btn {
  background: linear-gradient(135deg, #1a1a1a, #2a2a2a);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.4);
}

.app-container.black .back-btn:hover {
  background: linear-gradient(135deg, #2a2a2a, #3a3a3a);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.5);
}

.blog-name {
  font-size: 20px;
  font-weight: 700;
  color: #4a6fa5;
  text-shadow: 0 1px 2px rgba(74, 111, 165, 0.1);
  transition: all 0.3s ease;
}

.blog-name:hover {
  color: #3a5a85;
  text-shadow: 0 1px 4px rgba(74, 111, 165, 0.2);
}

.blog-slogan {
  font-size: 14px;
  color: #6c757d;
  white-space: nowrap;
  font-style: italic;
  transition: all 0.3s ease;
}

.app-container.dark .blog-name {
  color: #6b8cce;
  text-shadow: 0 1px 2px rgba(107, 140, 206, 0.2);
}

.app-container.dark .blog-name:hover {
  color: #4a6fa5;
}

.app-container.dark .blog-slogan,
.app-container.black .blog-slogan {
  color: #888888;
}

.app-container.black .blog-name {
  color: #6b8cce;
}

.nav-right {
  display: flex;
  align-items: center;
  gap: 24px;
}

.main-nav {
  display: flex;
  gap: 24px;
}

.nav-link {
  font-size: 16px;
  font-weight: 500;
  color: #6c757d;
  text-decoration: none;
  transition: all 0.3s ease;
  position: relative;
  padding: 8px 12px;
  border-radius: 8px;
}

.nav-link:hover {
  color: #4a6fa5;
  background-color: rgba(74, 111, 165, 0.1);
  transform: translateY(-2px);
}

.nav-link::after {
  content: '';
  position: absolute;
  bottom: -2px;
  left: 12px;
  right: 12px;
  height: 2px;
  background: linear-gradient(90deg, #4a6fa5, #6b8cce);
  transform: scaleX(0);
  transition: transform 0.3s ease;
  border-radius: 2px;
}

.nav-link:hover::after {
  transform: scaleX(1);
}

.app-container.dark .nav-link {
  color: #999999;
}

.app-container.dark .nav-link:hover {
  color: #6b8cce;
  background-color: rgba(107, 140, 206, 0.1);
}

.app-container.dark .nav-link::after {
  background: linear-gradient(90deg, #6b8cce, #4a6fa5);
}

.app-container.black .nav-link {
  color: #888888;
}

.app-container.black .nav-link:hover {
  color: #6b8cce;
  background-color: rgba(107, 140, 206, 0.1);
}

.mode-toggle {
  width: 24px;
  height: 24px;
  border: none;
  background: transparent;
  cursor: pointer;
  border-radius: 50%;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.mode-toggle:hover {
  background-color: rgba(0, 0, 0, 0.05);
}

.app-container.dark .mode-toggle:hover,
.app-container.black .mode-toggle:hover {
  background-color: rgba(255, 255, 255, 0.1);
}

.mode-icon {
  font-size: 16px;
}

/* 移动端汉堡菜单 */
.mobile-menu-toggle {
  display: none;
  border: none;
  background: transparent;
  cursor: pointer;
}

.hamburger {
  width: 24px;
  height: 2px;
  background-color: inherit;
  position: relative;
  transition: all 0.3s ease;
}

.hamburger::before,
.hamburger::after {
  content: '';
  position: absolute;
  width: 100%;
  height: 2px;
  background-color: inherit;
  transition: all 0.3s ease;
}

.hamburger::before {
  top: -8px;
}

.hamburger::after {
  bottom: -8px;
}

/* 主体内容区 */
.main-content {
  margin-top: 60px;
  min-height: calc(100vh - 120px);
}

.content-wrapper {
  display: flex;
  gap: 20px;
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.main-section {
  flex: 7;
}

.sidebar {
  flex: 3;
  position: relative;
}

/* 侧边栏模块 */
.sidebar-module {
  background: #ffffff;
  border-radius: 12px;
  box-shadow: 0 4px 16px rgba(100, 149, 237, 0.1);
  padding: 20px;
  margin-bottom: 20px;
  transition: all 0.3s ease;
  border: 1px solid #e0e0e0;
}

.sidebar-module:hover {
  box-shadow: 0 6px 20px rgba(100, 149, 237, 0.15);
  transform: translateY(-2px);
}

.app-container.dark .sidebar-module {
  background: #181818;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.2);
  border: 1px solid #333333;
}

.app-container.dark .sidebar-module:hover {
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.3);
}

.app-container.black .sidebar-module {
  background: #111111;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.3);
  border: 1px solid #222222;
}

.app-container.black .sidebar-module:hover {
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.4);
}

.module-title {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 12px;
  color: inherit;
}

/* 头像模块 */
.avatar-container {
  display: flex;
  justify-content: center;
  margin-bottom: 16px;
  position: relative;
}

.avatar-img {
  width: 90px;
  height: 90px;
  border-radius: 50%;
  object-fit: cover;
  border: 3px solid #4a6fa5;
  box-shadow: 0 4px 12px rgba(74, 111, 165, 0.3);
  transition: all 0.3s ease;
}

.avatar-img:hover {
  transform: scale(1.05);
  box-shadow: 0 6px 16px rgba(74, 111, 165, 0.4);
}

.profile-name {
  font-size: 18px;
  font-weight: 600;
  text-align: center;
  margin-bottom: 10px;
  color: #4a6fa5;
  text-shadow: 0 1px 2px rgba(74, 111, 165, 0.1);
  transition: all 0.3s ease;
}

.profile-name:hover {
  color: #3a5a85;
  text-shadow: 0 1px 4px rgba(74, 111, 165, 0.2);
}

.avatar-desc {
  text-align: center;
  font-size: 14px;
  color: #6c757d;
  line-height: 1.5;
  margin-bottom: 16px;
  font-style: italic;
  transition: all 0.3s ease;
}

.avatar-desc:hover {
  color: #495057;
}

.app-container.dark .avatar-img {
  border-color: #6b8cce;
  box-shadow: 0 4px 12px rgba(107, 140, 206, 0.3);
}

.app-container.dark .avatar-img:hover {
  box-shadow: 0 6px 16px rgba(107, 140, 206, 0.4);
}

.app-container.dark .profile-name {
  color: #6b8cce;
  text-shadow: 0 1px 2px rgba(107, 140, 206, 0.2);
}

.app-container.dark .profile-name:hover {
  color: #4a6fa5;
}

.app-container.dark .avatar-desc {
  color: #888888;
}

.app-container.dark .avatar-desc:hover {
  color: #999999;
}

.app-container.black .avatar-img {
  border-color: #6b8cce;
  box-shadow: 0 4px 12px rgba(107, 140, 206, 0.2);
}

.app-container.black .profile-name {
  color: #6b8cce;
}

.app-container.black .avatar-desc {
  color: #777777;
}

.app-container.black .avatar-desc:hover {
  color: #888888;
}

/* 分类列表 */
.category-list {
  list-style: none;
}

.category-item {
  display: block;
  padding: 8px 12px;
  font-size: 14px;
  color: #6c757d;
  text-decoration: none;
  transition: all 0.3s ease;
  border-radius: 6px;
  margin-bottom: 6px;
}

.category-item:hover {
  color: #4a6fa5;
  background-color: rgba(74, 111, 165, 0.1);
  transform: translateX(4px);
}

.app-container.dark .category-item,
.app-container.black .category-item {
  color: #999999;
}

.app-container.dark .category-item:hover {
  color: #6b8cce;
  background-color: rgba(107, 140, 206, 0.1);
}

/* 标签云 */
.tag-cloud {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.tag {
  font-size: 14px;
  color: #6c757d;
  cursor: pointer;
  transition: all 0.3s ease;
  padding: 4px 12px;
  border-radius: 16px;
  background-color: rgba(0, 0, 0, 0.05);
}

.tag:hover {
  color: #4a6fa5;
  background-color: rgba(74, 111, 165, 0.1);
  transform: translateY(-2px);
}

.app-container.dark .tag,
.app-container.black .tag {
  color: #999999;
  background-color: rgba(255, 255, 255, 0.05);
}

.app-container.dark .tag:hover {
  color: #6b8cce;
  background-color: rgba(107, 140, 206, 0.1);
}

/* 归档列表 */
.archive-list {
  list-style: none;
}

.archive-item {
  display: block;
  padding: 8px 12px;
  font-size: 14px;
  color: #6c757d;
  text-decoration: none;
  transition: all 0.3s ease;
  border-radius: 6px;
  margin-bottom: 6px;
}

.archive-item:hover {
  color: #4a6fa5;
  background-color: rgba(74, 111, 165, 0.1);
  transform: translateX(4px);
}

.app-container.dark .archive-item,
.app-container.black .archive-item {
  color: #999999;
}

.app-container.dark .archive-item:hover {
  color: #6b8cce;
  background-color: rgba(107, 140, 206, 0.1);
}

/* 页脚 */
.footer {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-top: 1px solid #ebeef5;
  transition: all 0.3s ease;
}

.app-container.dark .footer {
  border-top: 1px solid #222222;
}

.app-container.black .footer {
  border-top: 1px solid #222222;
}

.copyright {
  font-size: 12px;
  color: #909399;
}

.app-container.dark .copyright,
.app-container.black .copyright {
  color: #666666;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .nav-left {
    gap: 12px;
  }
  
  .blog-slogan {
    display: none;
  }
  
  .main-nav {
    display: none;
  }
  
  .mobile-menu-toggle {
    display: block;
  }
  
  .content-wrapper {
    flex-direction: column;
  }
  
  .sidebar {
    position: fixed;
    top: 60px;
    right: -80%;
    width: 80%;
    height: calc(100vh - 60px);
    background-color: #ffffff;
    box-shadow: -2px 0 8px rgba(0, 0, 0, 0.1);
    padding: 20px;
    transition: right 0.3s ease;
    z-index: 998;
    overflow-y: auto;
  }
  
  .app-container.dark .sidebar {
    background-color: #181818;
    box-shadow: -2px 0 8px rgba(0, 0, 0, 0.3);
  }
  
  .app-container.black .sidebar {
    background-color: #111111;
    box-shadow: -2px 0 8px rgba(0, 0, 0, 0.4);
  }
  
  .sidebar.sidebar-open {
    right: 0;
  }
  
  .top-nav {
    height: 50px;
  }
  
  .main-content {
    margin-top: 50px;
    min-height: calc(100vh - 100px);
  }
  
  .footer {
    height: 50px;
  }
}

/* 平滑滚动 */
html {
  scroll-behavior: smooth;
}
</style>