<template>
  <div class="portal-view" data-route="/">
    <!-- 导航栏 -->
    <header class="portal-nav" :class="{ 'scrolled': isScrolled }">
      <div class="nav-content">
        <div class="nav-left">
          <h1 class="logo">Dream<span>blog</span></h1>
        </div>
        <nav class="main-nav">
          <router-link to="/" class="nav-link">首页</router-link>
          <router-link to="/blog" class="nav-link">文章</router-link>
          <router-link to="/blog" class="nav-link">碎碎念</router-link>
          <router-link to="/blog" class="nav-link">关于</router-link>
          <router-link to="/blog" class="nav-link">留言板</router-link>
        </nav>
        <div class="nav-right">
          <button class="search-btn" aria-label="搜索">🔍</button>
        </div>
      </div>
    </header>

    <!-- 中央内容区 -->
    <main class="portal-main">
      <div class="hero-section" :class="{ 'scrolled': isScrolled }">
        <h2 class="hero-title">
          <span v-for="(char, index) in titleText" :key="index" 
                :style="{ 
                  opacity: char.visible ? 1 : 0, 
                  transform: char.visible ? 'translateY(0)' : 'translateY(20px)',
                  color: getTitleCharColor(index)
                }"
                :data-char="char.char"
                class="title-char">
            {{ char.char }}
          </span>
        </h2>
        <p class="hero-subtitle">
          <span v-for="(char, index) in subtitleText" :key="index" 
                :style="{ 
                  opacity: char.visible ? 1 : 0, 
                  transform: char.visible ? 'translateY(0)' : 'translateY(20px)',
                  color: getSubtitleCharColor(index)
                }"
                class="subtitle-char">
            {{ char.char }}
          </span>
        </p>
        <div class="scroll-down" aria-label="向下滚动">
          <div class="arrow"></div>
        </div>
      </div>
    </main>

    <!-- 个人博客区域 -->
    <section class="blog-section" ref="blogSection">
      <div class="blog-container">
        <!-- 左侧侧边栏 -->
        <aside class="left-sidebar">
          <!-- 公告栏 -->
          <div class="sidebar-module announcement">
            <h3 class="module-title">小窝公告栏</h3>
            <p class="announcement-content">天行健，君子以自强不息</p>
          </div>
          
          <!-- 导航菜单 -->
          <div class="sidebar-module navigation">
            <h3 class="module-title">导航菜单</h3>
            <ul class="nav-menu">
              <li><a href="#" class="nav-item">首页</a></li>
              <li><a href="#" class="nav-item">文章</a></li>
              <li><a href="#" class="nav-item">说说</a></li>
              <li><a href="#" class="nav-item">归档</a></li>
            </ul>
          </div>
          
          <!-- 个人简介 -->
          <div class="sidebar-module profile">
            <!-- 标签页切换 -->
            <div class="profile-tabs">
              <button class="tab-btn" :class="{ active: activeTab === 'profile' }" @click="activeTab = 'profile'">个人简介</button>
              <button class="tab-btn" :class="{ active: activeTab === 'features' }" @click="activeTab = 'features'">功能</button>
            </div>
            
            <!-- 个人简介内容 -->
            <div v-if="activeTab === 'profile'" class="tab-content">
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
                
                <!-- 下拉链接 -->
                <div class="dropdown-link">
                  <button class="link-item dropdown-toggle" @click="toggleDropdown">
                    <span class="link-icon">🔗</span>
                    <span class="link-text">Links</span>
                    <span class="dropdown-arrow" :class="{ 'rotated': isDropdownOpen }">▼</span>
                  </button>
                  <div class="dropdown-menu" :class="{ 'open': isDropdownOpen }">
                    <a href="#" class="dropdown-item" title="Music">
                      <span class="link-icon">🎵</span>
                      <span class="link-text">Music</span>
                    </a>
                    <a href="#" class="dropdown-item" title="Bilibili">
                      <span class="link-icon">📺</span>
                      <span class="link-text">Bilibili</span>
                    </a>
                  </div>
                </div>
              </div>
            </div>
            
            <!-- 功能内容 -->
            <div v-if="activeTab === 'features'" class="tab-content">
              <div class="features-list">
                <div class="feature-item" @click="activeFeature = 'article'">
                  <span class="feature-icon">📝</span>
                  <span class="feature-text">文章管理</span>
                </div>
                <div class="feature-item" @click="activeFeature = 'category'">
                  <span class="feature-icon">📁</span>
                  <span class="feature-text">分类管理</span>
                </div>
                <div class="feature-item" @click="activeFeature = 'tag'">
                  <span class="feature-icon">🏷️</span>
                  <span class="feature-text">标签管理</span>
                </div>
                <div class="feature-item" @click="activeFeature = 'profile'">
                  <span class="feature-icon">👤</span>
                  <span class="feature-text">个人设置</span>
                </div>
                <div class="feature-item" @click="activeFeature = 'site'">
                  <span class="feature-icon">⚙️</span>
                  <span class="feature-text">站点设置</span>
                </div>
              </div>
              
              <!-- 功能操作界面 -->
              <div class="feature-content" v-if="activeFeature">
                <!-- 文章管理 -->
                <div v-if="activeFeature === 'article'" class="feature-panel">
                  <h4 class="panel-title">文章管理</h4>
                  <div class="panel-content">
                    <button class="action-btn primary" @click="handleNewArticle">+ 新建文章</button>
                    <div class="article-list-admin">
                      <div class="article-item">
                        <div class="article-info">
                          <h5>2025年终总结</h5>
                          <p>2月24日 , 2026</p>
                        </div>
                        <div class="article-actions">
                          <button class="action-btn edit" @click="handleEditArticle('2025年终总结')">编辑</button>
                          <button class="action-btn delete" @click="handleDeleteArticle('2025年终总结')">删除</button>
                        </div>
                      </div>
                      <div class="article-item">
                        <div class="article-info">
                          <h5>实习小记</h5>
                          <p>2月18日 , 2025</p>
                        </div>
                        <div class="article-actions">
                          <button class="action-btn edit" @click="handleEditArticle('实习小记')">编辑</button>
                          <button class="action-btn delete" @click="handleDeleteArticle('实习小记')">删除</button>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                
                <!-- 分类管理 -->
                <div v-if="activeFeature === 'category'" class="feature-panel">
                  <h4 class="panel-title">分类管理</h4>
                  <div class="panel-content">
                    <button class="action-btn primary" @click="handleNewCategory">+ 新建分类</button>
                    <div class="category-list">
                      <div class="category-item">
                        <span>生活</span>
                        <div class="category-actions">
                          <button class="action-btn edit" @click="handleEditCategory('生活')">编辑</button>
                          <button class="action-btn delete" @click="handleDeleteCategory('生活')">删除</button>
                        </div>
                      </div>
                      <div class="category-item">
                        <span>Learn</span>
                        <div class="category-actions">
                          <button class="action-btn edit" @click="handleEditCategory('Learn')">编辑</button>
                          <button class="action-btn delete" @click="handleDeleteCategory('Learn')">删除</button>
                        </div>
                      </div>
                      <div class="category-item">
                        <span>建站</span>
                        <div class="category-actions">
                          <button class="action-btn edit" @click="handleEditCategory('建站')">编辑</button>
                          <button class="action-btn delete" @click="handleDeleteCategory('建站')">删除</button>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                
                <!-- 标签管理 -->
                <div v-if="activeFeature === 'tag'" class="feature-panel">
                  <h4 class="panel-title">标签管理</h4>
                  <div class="panel-content">
                    <button class="action-btn primary" @click="handleNewTag">+ 新建标签</button>
                    <div class="tag-list">
                      <div class="tag-item">
                        <span>Vue</span>
                        <div class="tag-actions">
                          <button class="action-btn edit" @click="handleEditTag('Vue')">编辑</button>
                          <button class="action-btn delete" @click="handleDeleteTag('Vue')">删除</button>
                        </div>
                      </div>
                      <div class="tag-item">
                        <span>TypeScript</span>
                        <div class="tag-actions">
                          <button class="action-btn edit" @click="handleEditTag('TypeScript')">编辑</button>
                          <button class="action-btn delete" @click="handleDeleteTag('TypeScript')">删除</button>
                        </div>
                      </div>
                      <div class="tag-item">
                        <span>前端</span>
                        <div class="tag-actions">
                          <button class="action-btn edit" @click="handleEditTag('前端')">编辑</button>
                          <button class="action-btn delete" @click="handleDeleteTag('前端')">删除</button>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                
                <!-- 个人设置 -->
                <div v-if="activeFeature === 'profile'" class="feature-panel">
                  <h4 class="panel-title">个人设置</h4>
                  <div class="panel-content">
                    <div class="setting-item">
                      <label>用户名</label>
                      <input type="text" value="Dream" class="setting-input">
                    </div>
                    <div class="setting-item">
                      <label>个人描述</label>
                      <input type="text" value="少年立志青云间，不负凌云万丈才" class="setting-input">
                    </div>
                    <div class="setting-item">
                      <label>头像</label>
                      <div class="avatar-upload">
                        <div class="current-avatar">
                          <img src="@/assets/images/avatar.png" alt="Avatar">
                        </div>
                        <button class="action-btn" @click="handleAvatarUpload">更换头像</button>
                      </div>
                    </div>
                    <button class="action-btn primary save" @click="handleSaveProfile">保存设置</button>
                  </div>
                </div>
                
                <!-- 站点设置 -->
                <div v-if="activeFeature === 'site'" class="feature-panel">
                  <h4 class="panel-title">站点设置</h4>
                  <div class="panel-content">
                    <div class="setting-item">
                      <label>站点标题</label>
                      <input type="text" value="Dream's blog" class="setting-input">
                    </div>
                    <div class="setting-item">
                      <label>站点描述</label>
                      <input type="text" value="Life is a coding, I will debug it." class="setting-input">
                    </div>
                    <div class="setting-item">
                      <label>主题模式</label>
                      <select class="setting-select">
                        <option>浅色模式</option>
                        <option>深色模式</option>
                        <option>黑色模式</option>
                      </select>
                    </div>
                    <button class="action-btn primary save" @click="handleSaveSite">保存设置</button>
                  </div>
                </div>
              </div>
            </div>
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
        <button class="icon-btn" aria-label="设置" @click="openSettings">
          <span class="icon">⚙️</span>
        </button>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import HomeView from './HomeView.vue'

const router = useRouter()
const blogSection = ref(null)
const isScrolled = ref(false)
const currentMode = ref('light')
const activeTab = ref('profile')
const isDropdownOpen = ref(false)
// 功能管理
const activeFeature = ref('')

// 标题文字逐字动画
const titleText = ref('Dream\'s blog'.split('').map(char => ({ char, visible: false })))
const subtitleText = ref('Life is a coding, I will debug it.'.split('').map(char => ({ char, visible: false })))

// 文字逐字出现动画
const animateText = () => {
  // 先动画标题
  titleText.value.forEach((char, index) => {
    setTimeout(() => {
      char.visible = true
    }, index * 150)
  })

  // 然后动画副标题
  setTimeout(() => {
    subtitleText.value.forEach((char, index) => {
      setTimeout(() => {
        char.visible = true
      }, index * 100)
    })
  }, titleText.value.length * 150 + 500)
}

// 标题字符颜色生成（柔和护眼）
const getTitleCharColor = (index) => {
  const colors = [
    '#ffffff', // 白色
    '#f8f9fa', // 超浅灰色
    '#e9ecef', // 浅灰色
    '#dee2e6'  // 中灰色
  ]
  return colors[index % colors.length]
}

// 副标题字符颜色生成（柔和护眼彩色效果）
const getSubtitleCharColor = (index) => {
  const colors = [
    '#6c757d', // 中性灰
    '#495057', // 深灰
    '#343a40', // 近黑
    '#212529', // 黑色
    '#4a6fa5', // 柔和蓝
    '#6c757d', // 中性灰
    '#adb5bd', // 浅灰
    '#ced4da'  // 超浅灰
  ]
  return colors[index % colors.length]
}

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
  } else if (currentMode.value === 'dark') {
    currentMode.value = 'black'
  } else {
    currentMode.value = 'light'
  }
  // 保存到localStorage
  localStorage.setItem('blogMode', currentMode.value)
}

// 打开设置
const openSettings = () => {
  alert('设置功能开发中...')
}

// 切换下拉菜单
const toggleDropdown = () => {
  isDropdownOpen.value = !isDropdownOpen.value
}

// 功能操作方法
const handleNewArticle = () => {
  alert('新建文章功能开发中...')
}

const handleEditArticle = (title) => {
  alert(`编辑文章: ${title}`)
}

const handleDeleteArticle = (title) => {
  if (confirm(`确定要删除文章: ${title}吗？`)) {
    alert(`文章: ${title} 已删除`)
  }
}

const handleNewCategory = () => {
  alert('新建分类功能开发中...')
}

const handleEditCategory = (category) => {
  alert(`编辑分类: ${category}`)
}

const handleDeleteCategory = (category) => {
  if (confirm(`确定要删除分类: ${category}吗？`)) {
    alert(`分类: ${category} 已删除`)
  }
}

const handleNewTag = () => {
  alert('新建标签功能开发中...')
}

const handleEditTag = (tag) => {
  alert(`编辑标签: ${tag}`)
}

const handleDeleteTag = (tag) => {
  if (confirm(`确定要删除标签: ${tag}吗？`)) {
    alert(`标签: ${tag} 已删除`)
  }
}

const handleSaveProfile = () => {
  alert('个人设置已保存')
}

const handleSaveSite = () => {
  alert('站点设置已保存')
}

const handleAvatarUpload = () => {
  alert('头像上传功能开发中...')
}

onMounted(() => {
  // 启动文字动画
  animateText()

  // 监听滚动，实现滚动检测
  window.addEventListener('scroll', () => {
    const scrollY = window.scrollY
    // 检测是否滚动超过阈值
    if (scrollY > window.innerHeight * 0.3) {
      isScrolled.value = true
    } else {
      isScrolled.value = false
    }
  })
  
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
  font-family: 'PingFang SC', 'Montserrat', 'Ma Shan Zheng', cursive, sans-serif;
  background: url('https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=misty%20forest%20with%20sunlight%20streaming%20through%20trees%2C%20peaceful%20natural%20landscape&image_size=landscape_16_9') no-repeat center center fixed;
  background-size: cover;
}

/* 导航栏 */
.portal-nav {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 999;
  background-color: rgba(0, 0, 0, 0.3);
  backdrop-filter: blur(10px);
  transition: all 0.3s ease;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

/* 滚动时的导航栏样式 */
.portal-nav.scrolled {
  background-color: rgba(0, 0, 0, 0.6);
  backdrop-filter: blur(10px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.2);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.3);
}

.portal-nav.scrolled .logo {
  color: #f8f9fa;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.portal-nav.scrolled .logo span {
  color: #4a6fa5;
}

.portal-nav.scrolled .nav-link {
  color: #e9ecef;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);
}

.portal-nav.scrolled .nav-link:hover {
  color: #4a6fa5;
}

.portal-nav.scrolled .search-btn {
  background: rgba(248, 249, 250, 0.2);
  color: #f8f9fa;
}

.portal-nav.scrolled .search-btn:hover {
  background: rgba(248, 249, 250, 0.3);
}

.nav-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 40px;
  height: 80px;
  max-width: 1400px;
  margin: 0 auto;
}

.logo {
  font-size: 28px;
  font-weight: bold;
  color: #f8f9fa;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.logo span {
  color: #4a6fa5;
}

.main-nav {
  display: flex;
  gap: 30px;
}

.nav-link {
  font-size: 18px;
  color: #e9ecef;
  text-decoration: none;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);
  transition: all 0.3s ease;
  position: relative;
}

.nav-link:hover {
  color: #4a6fa5;
}

.nav-link::after {
  content: '';
  position: absolute;
  bottom: -4px;
  left: 0;
  width: 0;
  height: 1px;
  background-color: #4a6fa5;
  transition: width 0.3s ease;
}

.nav-link:hover::after {
  width: 100%;
}

.search-btn {
  width: 36px;
  height: 36px;
  border: none;
  background: rgba(248, 249, 250, 0.2);
  border-radius: 50%;
  color: #f8f9fa;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.search-btn:hover {
  background: rgba(248, 249, 250, 0.3);
  transform: scale(1.1);
}

/* 中央内容区 */
.portal-main {
  position: relative;
  height: 100vh;
  overflow: hidden;
}

.hero-section {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.3);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-align: center;
  padding: 0 20px;
  transition: transform 0.1s ease-out;
}

.hero-section.scrolled {
  opacity: 1;
}

/* 文字逐字动画 */
.hero-title {
  font-size: 56px;
  font-weight: bold;
  margin-bottom: 20px;
  letter-spacing: 2px;
}

.title-char {
  display: inline-block;
  transition: all 0.5s ease;
  text-shadow: 
    0 2px 8px rgba(0, 0, 0, 0.5),
    0 0 15px rgba(255, 255, 255, 0.3);
  position: relative;
}

.title-char::after {
  content: attr(data-char);
  position: absolute;
  top: 0;
  left: 0;
  z-index: -1;
  color: rgba(255, 255, 255, 0.5);
  filter: blur(10px);
  opacity: 0.7;
}

.hero-subtitle {
  font-size: 24px;
  margin-bottom: 60px;
  max-width: 600px;
  line-height: 1.5;
}

.subtitle-char {
  display: inline-block;
  transition: all 0.5s ease;
  text-shadow: 
    0 1px 4px rgba(0, 0, 0, 0.5);
}

/* 向下滚动按钮 */
.scroll-down {
  background: rgba(0, 0, 0, 0.4);
  border: 2px solid #f8f9fa;
  border-radius: 50%;
  width: 60px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
  animation: bounce 2s infinite;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
}

.scroll-down:hover {
  background: rgba(0, 0, 0, 0.6);
  transform: scale(1.1);
}

.arrow {
  width: 24px;
  height: 24px;
  border-left: 3px solid #f8f9fa;
  border-bottom: 3px solid #f8f9fa;
  transform: rotate(-45deg);
  animation: arrowMove 1.5s infinite;
}

/* 个人博客区域 */
.blog-section {
  min-height: 100vh;
  padding-top: 80px;
  transition: all 0.5s ease;
  position: relative;
}

/* 博客容器 */
.blog-container {
  display: flex;
  gap: 20px;
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  background: rgba(255, 255, 255, 0.85);
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(10px);
  background-image: url('https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=misty%20forest%20with%20sunlight%20streaming%20through%20trees%2C%20peaceful%20natural%20landscape&image_size=landscape_16_9');
  background-size: cover;
  background-position: center;
  background-attachment: fixed;
}

/* 左侧侧边栏 */
.left-sidebar {
  flex: 3;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

/* 模块标题 */
.module-title {
  font-size: 16px;
  font-weight: bold;
  color: inherit;
  border-bottom: 1px solid rgba(255, 255, 255, 0.3);
  padding-bottom: 8px;
  margin-bottom: 12px;
}

/* 侧边栏模块 */
.sidebar-module {
  background: rgba(255, 255, 255, 0.9);
  border-radius: 8px;
  padding: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  backdrop-filter: blur(5px);
}

/* 公告栏 */
.announcement {
  background: linear-gradient(135deg, #4a6fa5, #6b8cce);
  color: white;
}

.announcement-content {
  font-size: 14px;
  line-height: 1.5;
  margin-top: 8px;
}

/* 导航菜单 */
.navigation {
  background: linear-gradient(135deg, #6c757d, #adb5bd);
  color: white;
}

.nav-menu {
  list-style: none;
  margin-top: 8px;
}

.nav-item {
  display: block;
  padding: 8px 0;
  color: white;
  text-decoration: none;
  transition: all 0.3s ease;
  border-bottom: 1px solid rgba(255, 255, 255, 0.2);
}

.nav-item:hover {
  color: #f8f9fa;
  padding-left: 8px;
}

/* 个人简介 */
.profile {
  background: linear-gradient(135deg, #28a745, #4caf50);
  color: white;
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
  background: rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 32px;
  font-weight: bold;
  border: 2px solid rgba(255, 255, 255, 0.3);
}

.profile-name {
  text-align: center;
  font-size: 16px;
  font-weight: bold;
  margin: 8px 0;
  color: white;
}

.profile-desc {
  text-align: center;
  font-size: 14px;
  color: rgba(255, 255, 255, 0.8);
  margin-bottom: 16px;
}

/* 个人简介标签页 */
.profile-tabs {
  display: flex;
  margin-bottom: 16px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.3);
}

.tab-btn {
  flex: 1;
  padding: 8px 12px;
  background: transparent;
  border: none;
  color: rgba(255, 255, 255, 0.8);
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 14px;
  font-weight: 500;
}

.tab-btn:hover {
  color: white;
}

.tab-btn.active {
  color: white;
  border-bottom: 2px solid white;
}

/* 标签页内容 */
.tab-content {
  animation: fadeIn 0.3s ease;
}

/* 头像图片 */
.avatar-img {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  object-fit: cover;
}

/* 统计数据 */
.profile-stats {
  display: flex;
  justify-content: space-around;
  margin: 16px 0;
  padding: 12px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 6px;
}

.stat-item {
  text-align: center;
}

.stat-number {
  display: block;
  font-size: 18px;
  font-weight: bold;
  color: white;
}

.stat-label {
  display: block;
  font-size: 12px;
  color: rgba(255, 255, 255, 0.8);
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
  background: rgba(255, 255, 255, 0.1);
  border-radius: 6px;
  color: white;
  text-decoration: none;
  transition: all 0.3s ease;
}

.link-item:hover {
  background: rgba(255, 255, 255, 0.2);
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

/* 下拉链接 */
.dropdown-link {
  position: relative;
}

.dropdown-toggle {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  text-align: left;
  padding: 8px 12px;
  background: rgba(255, 255, 255, 0.1);
  border: none;
  border-radius: 6px;
  color: white;
  cursor: pointer;
  transition: all 0.3s ease;
}

.dropdown-toggle:hover {
  background: rgba(255, 255, 255, 0.2);
}

.dropdown-arrow {
  font-size: 12px;
  transition: transform 0.3s ease;
  margin-left: 8px;
}

.dropdown-arrow.rotated {
  transform: rotate(180deg);
}

.dropdown-menu {
  position: absolute;
  top: 100%;
  left: 0;
  right: 0;
  margin-top: 4px;
  background: rgba(72, 175, 83, 0.95);
  border-radius: 6px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  overflow: hidden;
  z-index: 1000;
  max-height: 0;
  opacity: 0;
  transition: all 0.3s ease;
}

.dropdown-menu.open {
  max-height: 200px;
  opacity: 1;
  animation: slideDown 0.3s ease;
}

.dropdown-item {
  display: flex;
  align-items: center;
  padding: 8px 12px;
  color: white;
  text-decoration: none;
  transition: all 0.3s ease;
}

.dropdown-item:hover {
  background: rgba(255, 255, 255, 0.1);
}

/* 下拉动画 */
@keyframes slideDown {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 功能列表 */
.features-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin-bottom: 20px;
}

.feature-item {
  display: flex;
  align-items: center;
  padding: 10px 12px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 6px;
  color: white;
  transition: all 0.3s ease;
  cursor: pointer;
  border: 1px solid transparent;
}

.feature-item:hover {
  background: rgba(255, 255, 255, 0.2);
  transform: translateX(4px);
  border-color: rgba(255, 255, 255, 0.3);
}

.feature-icon {
  font-size: 16px;
  margin-right: 12px;
  width: 20px;
  text-align: center;
}

.feature-text {
  font-size: 14px;
  flex: 1;
}

/* 功能内容区域 */
.feature-content {
  margin-top: 20px;
  animation: fadeIn 0.3s ease;
}

/* 功能面板 */
.feature-panel {
  background: rgba(255, 255, 255, 0.9);
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  backdrop-filter: blur(5px);
}

.panel-title {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  padding: 16px;
  background: rgba(74, 111, 165, 0.1);
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);
}

.panel-content {
  padding: 16px;
}

/* 操作按钮 */
.action-btn {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s ease;
  margin-bottom: 16px;
}

.action-btn.primary {
  background: #4a6fa5;
  color: white;
}

.action-btn.primary:hover {
  background: #3a5a85;
  transform: translateY(-1px);
}

.action-btn.edit {
  background: #ffc107;
  color: #333;
  margin-right: 8px;
}

.action-btn.edit:hover {
  background: #e0a800;
}

.action-btn.delete {
  background: #dc3545;
  color: white;
}

.action-btn.delete:hover {
  background: #c82333;
}

.action-btn.save {
  margin-top: 20px;
  width: 100%;
}

/* 文章管理列表 */
.article-list-admin {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.article-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px;
  background: rgba(255, 255, 255, 0.8);
  border-radius: 6px;
  border: 1px solid rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.article-item:hover {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transform: translateY(-1px);
}

.article-info h5 {
  font-size: 14px;
  font-weight: bold;
  color: #333;
  margin-bottom: 4px;
}

.article-info p {
  font-size: 12px;
  color: #999;
}

.article-actions {
  display: flex;
  gap: 8px;
}

/* 分类管理列表 */
.category-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.category-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 12px;
  background: rgba(255, 255, 255, 0.8);
  border-radius: 6px;
  border: 1px solid rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.category-item:hover {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.category-item span {
  font-size: 14px;
  color: #333;
}

.category-actions {
  display: flex;
  gap: 8px;
}

/* 标签管理列表 */
.tag-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.tag-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 12px;
  background: rgba(255, 255, 255, 0.8);
  border-radius: 6px;
  border: 1px solid rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.tag-item:hover {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.tag-item span {
  font-size: 14px;
  color: #333;
  background: rgba(74, 111, 165, 0.1);
  padding: 2px 8px;
  border-radius: 12px;
}

.tag-actions {
  display: flex;
  gap: 8px;
}

/* 设置项 */
.setting-item {
  margin-bottom: 16px;
}

.setting-item label {
  display: block;
  font-size: 14px;
  font-weight: 500;
  color: #333;
  margin-bottom: 8px;
}

.setting-input {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
  transition: all 0.3s ease;
}

.setting-input:focus {
  outline: none;
  border-color: #4a6fa5;
  box-shadow: 0 0 0 2px rgba(74, 111, 165, 0.1);
}

.setting-select {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
  background: white;
  cursor: pointer;
  transition: all 0.3s ease;
}

.setting-select:focus {
  outline: none;
  border-color: #4a6fa5;
  box-shadow: 0 0 0 2px rgba(74, 111, 165, 0.1);
}

/* 头像上传 */
.avatar-upload {
  display: flex;
  align-items: center;
  gap: 16px;
}

.current-avatar {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  overflow: hidden;
  border: 2px solid #ddd;
}

.current-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .feature-item {
    padding: 8px 10px;
  }
  
  .feature-icon {
    font-size: 14px;
    margin-right: 8px;
  }
  
  .feature-text {
    font-size: 13px;
  }
  
  .panel-title {
    font-size: 14px;
    padding: 12px;
  }
  
  .panel-content {
    padding: 12px;
  }
  
  .action-btn {
    padding: 6px 12px;
    font-size: 13px;
  }
  
  .article-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }
  
  .article-actions {
    align-self: flex-end;
  }
  
  .avatar-upload {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .current-avatar {
    width: 80px;
    height: 80px;
  }
}

/* 淡入动画 */
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}



/* 主内容区 */
.main-content {
  flex: 7;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(5px);
  background-image: url('https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=misty%20forest%20with%20sunlight%20streaming%20through%20trees%2C%20peaceful%20natural%20landscape&image_size=landscape_16_9');
  background-size: cover;
  background-position: center;
  background-attachment: fixed;
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
  background: rgba(255, 255, 255, 0.9);
  border-radius: 50%;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  backdrop-filter: blur(10px);
}

.icon-btn:hover {
  background: rgba(255, 255, 255, 1);
  transform: translateY(-4px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.2);
}

.icon {
  font-size: 20px;
  color: #4a6fa5;
}

/* 模块标题 */
.module-title {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 12px;
  color: inherit;
  border-bottom: 1px solid rgba(255, 255, 255, 0.3);
  padding-bottom: 8px;
}

/* 动画效果 */
@keyframes bounce {
  0%, 20%, 50%, 80%, 100% {
    transform: translateY(0);
  }
  40% {
    transform: translateY(10px);
  }
  60% {
    transform: translateY(5px);
  }
}

@keyframes arrowMove {
  0% {
    opacity: 0;
    transform: rotate(-45deg) translate(-10px, -10px);
  }
  50% {
    opacity: 1;
    transform: rotate(-45deg) translate(0, 0);
  }
  100% {
    opacity: 0;
    transform: rotate(-45deg) translate(10px, 10px);
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .nav-content {
    padding: 0 20px;
    height: 60px;
  }
  
  .logo {
    font-size: 24px;
  }
  
  .main-nav {
    gap: 15px;
  }
  
  .nav-link {
    font-size: 16px;
  }
  
  .hero-title {
    font-size: 40px;
  }
  
  .hero-subtitle {
    font-size: 20px;
  }
  
  .scroll-down {
    width: 50px;
    height: 50px;
  }
  
  .arrow {
    width: 16px;
    height: 16px;
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