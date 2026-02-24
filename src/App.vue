<template>
  <div :class="['app-container', currentMode]">
    <!-- 门户网站页面 -->
    <template v-if="$route.path === '/'">
      <router-view />
    </template>
    
    <!-- 博客页面 -->
    <template v-else>
      <!-- 顶部导航栏 -->
      <header class="top-nav">
        <div class="nav-content">
          <div class="nav-left">
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
                <div class="avatar"></div>
              </div>
              <p class="avatar-desc">一个普通的程序员</p>
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
  background-color: #f8f9fa;
  color: #333333;
}

.app-container.dark {
  background-color: #121212;
  color: #eaeaea;
}

.app-container.black {
  background-color: #000000;
  color: #e0e0e0;
}

/* 根路径特殊样式 - 移除背景色以显示森林背景 */
.app-container:has(> [data-route="/"]) {
  background-color: transparent;
}

/* 顶部导航栏 */
.top-nav {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 60px;
  background-color: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(10px);
  border-bottom: 1px solid #ebeef5;
  z-index: 999;
  transition: all 0.3s ease;
}

.app-container.dark .top-nav {
  background-color: rgba(18, 18, 18, 0.8);
  border-bottom: 1px solid #222222;
}

.app-container.black .top-nav {
  background-color: rgba(0, 0, 0, 0.8);
  border-bottom: 1px solid #222222;
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

.blog-name {
  font-size: 18px;
  font-weight: bold;
  color: inherit;
}

.blog-slogan {
  font-size: 14px;
  color: #909399;
  white-space: nowrap;
}

.app-container.dark .blog-slogan,
.app-container.black .blog-slogan {
  color: #666666;
}

.nav-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.main-nav {
  display: flex;
  gap: 20px;
}

.nav-link {
  font-size: 16px;
  color: #909399;
  text-decoration: none;
  transition: all 0.3s ease;
  position: relative;
}

.nav-link:hover {
  color: #5e72e4;
}

.nav-link::after {
  content: '';
  position: absolute;
  bottom: -4px;
  left: 0;
  width: 0;
  height: 1px;
  background-color: #5e72e4;
  transition: width 0.3s ease;
}

.nav-link:hover::after {
  width: 100%;
}

.app-container.dark .nav-link,
.app-container.black .nav-link {
  color: #666666;
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
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  padding: 16px;
  margin-bottom: 16px;
  transition: all 0.3s ease;
}

.app-container.dark .sidebar-module {
  background-color: #181818;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
}

.app-container.black .sidebar-module {
  background-color: #111111;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.3);
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
  margin-bottom: 12px;
}

.avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  background-color: #5e72e4;
}

.avatar-desc {
  text-align: center;
  font-size: 14px;
  color: #909399;
}

.app-container.dark .avatar-desc,
.app-container.black .avatar-desc {
  color: #666666;
}

/* 分类列表 */
.category-list {
  list-style: none;
}

.category-item {
  display: block;
  padding: 4px 0;
  font-size: 14px;
  color: #909399;
  text-decoration: none;
  transition: all 0.3s ease;
}

.category-item:hover {
  color: #5e72e4;
}

.app-container.dark .category-item,
.app-container.black .category-item {
  color: #666666;
}

/* 标签云 */
.tag-cloud {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.tag {
  font-size: 14px;
  color: #909399;
  cursor: pointer;
  transition: all 0.3s ease;
}

.tag:hover {
  color: #5e72e4;
}

.app-container.dark .tag,
.app-container.black .tag {
  color: #666666;
}

/* 归档列表 */
.archive-list {
  list-style: none;
}

.archive-item {
  display: block;
  padding: 4px 0;
  font-size: 14px;
  color: #909399;
  text-decoration: none;
  transition: all 0.3s ease;
}

.archive-item:hover {
  color: #5e72e4;
}

.app-container.dark .archive-item,
.app-container.black .archive-item {
  color: #666666;
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