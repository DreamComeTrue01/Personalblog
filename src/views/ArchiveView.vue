<template>
  <div class="archive-view">
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
          <router-link to="/archive" class="nav-link active">归档</router-link>
          <router-link to="/about" class="nav-link">关于</router-link>
          <router-link to="/message" class="nav-link">留言板</router-link>
        </nav>
        <div class="nav-right">
          <button class="search-btn" aria-label="搜索">🔍</button>
        </div>
      </div>
    </header>

    <!-- 中央内容区 -->
    <main class="archive-main">
      <div class="archive-container">
        <!-- 左侧内容 -->
        <div class="main-content">
          <h2 class="archive-title">归档</h2>
          <p class="archive-subtitle">按时间顺序查看所有文章</p>
          
          <div class="archive-years">
            <div v-for="(yearPosts, year) in groupedArticles" :key="year" class="archive-year">
              <h3 class="year-title">{{ year }}</h3>
              <div class="year-months">
                <div v-for="(monthPosts, month) in yearPosts" :key="month" class="archive-month">
                  <h4 class="month-title">{{ monthNames[parseInt(month) - 1] }}</h4>
                  <ul class="month-articles">
                    <li v-for="article in monthPosts" :key="article.id" class="article-item">
                      <router-link :to="`/article/${article.id}`" class="article-link">
                        <span class="article-title">{{ article.title }}</span>
                        <span class="article-date">{{ formatDate(article.date) }}</span>
                      </router-link>
                    </li>
                  </ul>
                </div>
              </div>
            </div>
          </div>
          
          <!-- 返回按钮 -->
          <div class="back-container">
            <button class="back-btn" @click="goBack">← 返回上一页</button>
          </div>
        </div>
        
        <!-- 右侧个人简介 -->
        <div class="right-sidebar">
          <!-- 公告栏 -->
          <div class="sidebar-module announcement">
            <h3 class="module-title">小窝公告栏</h3>
            <p class="announcement-content">天行健，君子以自强不息</p>
          </div>
          
          <!-- 导航菜单 -->
          <div class="sidebar-module navigation">
            <h3 class="module-title">导航菜单</h3>
            <ul class="nav-menu">
              <li><router-link to="/" class="nav-item">首页</router-link></li>
              <li><router-link to="/blog" class="nav-item">文章</router-link></li>
              <li><router-link to="/life" class="nav-item">说说</router-link></li>
              <li><router-link to="/archive" class="nav-item">归档</router-link></li>
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
                  <img :src="userSettings.avatar" alt="Avatar" class="avatar-img">
                </div>
              </div>
              <h4 class="profile-name">{{ userSettings.username }}</h4>
              <p class="profile-desc">{{ userSettings.description }}</p>
              
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
              <div class="features-list csdn-style">
                <div class="feature-item csdn-item" @click="activeFeature = 'article'">
                  <span class="feature-icon">📝</span>
                  <span class="feature-text">文章管理</span>
                </div>
                <div class="feature-item csdn-item" @click="activeFeature = 'category'">
                  <span class="feature-icon">📁</span>
                  <span class="feature-text">分类管理</span>
                </div>
                <div class="feature-item csdn-item" @click="activeFeature = 'tag'">
                  <span class="feature-icon">🏷️</span>
                  <span class="feature-text">标签管理</span>
                </div>
                <div class="feature-item csdn-item" @click="activeFeature = 'profile'">
                  <span class="feature-icon">👤</span>
                  <span class="feature-text">个人设置</span>
                </div>
                <div class="feature-item csdn-item" @click="activeFeature = 'site'">
                  <span class="feature-icon">⚙️</span>
                  <span class="feature-text">站点设置</span>
                </div>
              </div>
            </div>
          </div>
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

// 侧边栏状态
const activeTab = ref('profile')
const isDropdownOpen = ref(false)
const activeFeature = ref('article')

// 用户设置
const userSettings = ref({
  avatar: 'https://i.pravatar.cc/150?img=68',
  username: '小窝',
  description: '记录生活，分享知识'
})

// 月份名称
const monthNames = ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月']

// 文章数据
const articles = ref([
  {
    id: 1,
    title: '2025年终总结',
    date: '2026-02-24',
    categories: ['生活'],
    content: '2025 年终总结。诈尸一下，马上就是 2026 年了，哈哈哈。过得太快了吧。期间还想着水水文章的，结果完全没有了本科期间的热情，能写个年终总结就不错了。'
  },
  {
    id: 2,
    title: '实习小记',
    date: '2025-02-18',
    categories: ['生活', 'Learn'],
    content: '月记。最近想趁着没啥课，导师管的松的时候出去找个实习，结果一个也没找到，2333。'
  },
  {
    id: 3,
    title: '2024年终总结',
    date: '2025-01-02',
    categories: ['生活'],
    content: '光阴似骏马加鞭，浮世似落花流水。大学生活结束了～考完了，感觉这一年白费了，害，后面再说吧，下面就是准备没有工资还要交钱的实习了，以及毕设，顺便整理下博客吧。'
  },
  {
    id: 4,
    title: 'Argon主题博客美化',
    date: '2022-10-21',
    categories: ['建站', 'Learn'],
    content: '关于 Argon 主题的相关美化文档。新增 WP 用户头像插件。'
  }
])

// 按年份和月份分组文章
const groupedArticles = computed(() => {
  const grouped = {}
  
  articles.value.forEach(article => {
    const date = new Date(article.date)
    const year = date.getFullYear().toString()
    const month = (date.getMonth() + 1).toString().padStart(2, '0')
    
    if (!grouped[year]) {
      grouped[year] = {}
    }
    
    if (!grouped[year][month]) {
      grouped[year][month] = []
    }
    
    grouped[year][month].push(article)
  })
  
  // 按年份倒序排序
  const sortedGrouped = {}
  Object.keys(grouped).sort((a, b) => b - a).forEach(year => {
    sortedGrouped[year] = grouped[year]
    
    // 按月份倒序排序
    const sortedMonths = {}
    Object.keys(sortedGrouped[year]).sort((a, b) => b - a).forEach(month => {
      sortedMonths[month] = sortedGrouped[year][month]
    })
    sortedGrouped[year] = sortedMonths
  })
  
  return sortedGrouped
})

// 格式化日期
const formatDate = (dateString) => {
  const date = new Date(dateString)
  return date.toLocaleDateString('zh-CN', {
    month: 'numeric',
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

// 切换下拉菜单
const toggleDropdown = () => {
  isDropdownOpen.value = !isDropdownOpen.value
}

onMounted(() => {
  window.addEventListener('scroll', handleScroll)
  return () => {
    window.removeEventListener('scroll', handleScroll)
  }
})
</script>

<style scoped>
.archive-view {
  width: 100%;
  min-height: 100vh;
  background: #f8f9fa;
}

.archive-main {
  padding: 60px 0;
}

.archive-container {
  display: flex;
  gap: 20px;
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.main-content {
  flex: 1;
}

.archive-title {
  font-size: 36px;
  font-weight: bold;
  color: #333;
  text-align: center;
  margin-bottom: 16px;
}

.archive-subtitle {
  font-size: 18px;
  color: #666;
  text-align: center;
  margin-bottom: 40px;
}

.archive-years {
  display: flex;
  flex-direction: column;
  gap: 40px;
}

.archive-year {
  background: white;
  border-radius: 8px;
  padding: 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(5px);
}

.year-title {
  font-size: 24px;
  font-weight: bold;
  color: #333;
  margin-bottom: 20px;
  padding-bottom: 12px;
  border-bottom: 2px solid #f0f0f0;
}

.year-months {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.archive-month {
  margin-left: 20px;
}

.month-title {
  font-size: 18px;
  font-weight: 600;
  color: #666;
  margin-bottom: 12px;
}

.month-articles {
  list-style: none;
  padding: 0;
  margin: 0;
}

.article-item {
  margin-bottom: 8px;
}

.article-link {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 12px;
  border-radius: 4px;
  color: #333;
  text-decoration: none;
  transition: all 0.3s ease;
}

.article-link:hover {
  background-color: #f5f5f5;
  color: #4a6fa5;
}

.article-title {
  font-size: 16px;
  font-weight: 500;
}

.article-date {
  font-size: 14px;
  color: #999;
}

/* 返回按钮 */
.back-container {
  margin-top: 40px;
  display: flex;
  justify-content: center;
}

.back-btn {
  background-color: #f8f9fa;
  border: 1px solid #dee2e6;
  border-radius: 4px;
  padding: 8px 16px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 6px;
}

.back-btn:hover {
  background-color: #e9ecef;
  border-color: #adb5bd;
}

/* 右侧侧边栏 */
.right-sidebar {
  width: 300px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.sidebar-module {
  background: rgba(255, 255, 255, 0.9);
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  padding: 16px;
  backdrop-filter: blur(5px);
}

.module-title {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 12px;
  color: #333333;
  border-bottom: 1px solid #e9ecef;
  padding-bottom: 8px;
}

/* 公告栏 */
.announcement-content {
  font-size: 14px;
  color: #666666;
  text-align: center;
  padding: 12px 0;
}

/* 导航菜单 */
.nav-menu {
  list-style: none;
  padding: 0;
  margin: 0;
}

.nav-item {
  display: block;
  padding: 8px 0;
  color: #666666;
  text-decoration: none;
  transition: all 0.3s ease;
  font-size: 14px;
}

.nav-item:hover {
  color: #5e72e4;
  padding-left: 8px;
}

/* 个人简介 */
.profile-tabs {
  display: flex;
  margin-bottom: 16px;
  border-bottom: 1px solid #e9ecef;
}

.tab-btn {
  flex: 1;
  padding: 8px 0;
  background: none;
  border: none;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s ease;
  color: #666666;
}

.tab-btn.active {
  color: #5e72e4;
  border-bottom: 2px solid #5e72e4;
}

.avatar-container {
  display: flex;
  justify-content: center;
  margin-bottom: 16px;
}

.avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  overflow: hidden;
  border: 2px solid #e9ecef;
}

.avatar-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.profile-name {
  font-size: 16px;
  font-weight: bold;
  text-align: center;
  margin-bottom: 8px;
  color: #333333;
}

.profile-desc {
  font-size: 14px;
  color: #666666;
  text-align: center;
  margin-bottom: 16px;
  line-height: 1.4;
}

/* 统计数据 */
.profile-stats {
  display: flex;
  justify-content: space-around;
  margin-bottom: 16px;
  padding: 12px 0;
  border-top: 1px solid #e9ecef;
  border-bottom: 1px solid #e9ecef;
}

.stat-item {
  text-align: center;
}

.stat-number {
  display: block;
  font-size: 18px;
  font-weight: bold;
  color: #5e72e4;
}

.stat-label {
  display: block;
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
}

/* 功能链接 */
.profile-links {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.link-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 12px;
  border-radius: 4px;
  text-decoration: none;
  color: #666666;
  transition: all 0.3s ease;
  font-size: 14px;
  cursor: pointer;
}

.link-item:hover {
  background-color: #f8f9fa;
  color: #5e72e4;
}

.link-icon {
  font-size: 16px;
}

/* 下拉链接 */
.dropdown-link {
  position: relative;
}

.dropdown-toggle {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: none;
  border: none;
  padding: 8px 12px;
  border-radius: 4px;
  color: #666666;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 14px;
}

.dropdown-toggle:hover {
  background-color: #f8f9fa;
  color: #5e72e4;
}

.dropdown-arrow {
  font-size: 12px;
  transition: transform 0.3s ease;
}

.dropdown-arrow.rotated {
  transform: rotate(180deg);
}

.dropdown-menu {
  position: absolute;
  top: 100%;
  left: 0;
  right: 0;
  background: white;
  border: 1px solid #e9ecef;
  border-radius: 4px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  margin-top: 4px;
  z-index: 100;
  max-height: 0;
  overflow: hidden;
  transition: max-height 0.3s ease;
}

.dropdown-menu.open {
  max-height: 200px;
}

.dropdown-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 12px;
  text-decoration: none;
  color: #666666;
  transition: all 0.3s ease;
  font-size: 14px;
}

.dropdown-item:hover {
  background-color: #f8f9fa;
  color: #5e72e4;
}

/* 功能内容 */
.features-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 14px;
  color: #666666;
}

.feature-item:hover {
  background-color: #f8f9fa;
  color: #5e72e4;
}

.feature-icon {
  font-size: 16px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .archive-container {
    flex-direction: column;
    padding: 10px;
  }
  
  .right-sidebar {
    width: 100%;
  }
  
  .archive-title {
    font-size: 28px;
  }
  
  .archive-subtitle {
    font-size: 16px;
  }
  
  .archive-year {
    padding: 20px;
  }
  
  .year-title {
    font-size: 20px;
  }
  
  .month-title {
    font-size: 16px;
  }
  
  .article-link {
    font-size: 14px;
  }
  
  .back-btn {
    padding: 10px 24px;
    font-size: 14px;
  }
}

/* 主题模式适配 */
:deep(.app-container.dark) .archive-year,
:deep(.app-container.dark) .sidebar-module {
  background-color: #181818;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
}

:deep(.app-container.dark) .archive-year:hover,
:deep(.app-container.dark) .sidebar-module:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
}

:deep(.app-container.dark) .archive-title,
:deep(.app-container.dark) .archive-subtitle,
:deep(.app-container.dark) .year-title,
:deep(.app-container.dark) .month-title,
:deep(.app-container.dark) .article-title,
:deep(.app-container.dark) .module-title,
:deep(.app-container.dark) .announcement-content,
:deep(.app-container.dark) .nav-item,
:deep(.app-container.dark) .tab-btn,
:deep(.app-container.dark) .profile-name,
:deep(.app-container.dark) .profile-desc,
:deep(.app-container.dark) .stat-label,
:deep(.app-container.dark) .link-item,
:deep(.app-container.dark) .dropdown-toggle,
:deep(.app-container.dark) .dropdown-item,
:deep(.app-container.dark) .feature-item {
  color: #888888;
}

:deep(.app-container.dark) .article-date {
  color: #666666;
}

:deep(.app-container.dark) .profile-stats {
  border-top-color: #333333;
  border-bottom-color: #333333;
}

:deep(.app-container.dark) .profile-tabs {
  border-bottom-color: #333333;
}

:deep(.app-container.dark) .back-btn {
  background-color: #222222;
  border-color: #333333;
  color: #888888;
}

:deep(.app-container.dark) .back-btn:hover {
  background-color: #333333;
  border-color: #444444;
}

:deep(.app-container.dark) .article-link:hover,
:deep(.app-container.dark) .link-item:hover,
:deep(.app-container.dark) .dropdown-toggle:hover,
:deep(.app-container.dark) .dropdown-item:hover,
:deep(.app-container.dark) .feature-item:hover {
  background-color: #222222;
}

:deep(.app-container.black) .archive-year,
:deep(.app-container.black) .sidebar-module {
  background-color: #111111;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.3);
}

:deep(.app-container.black) .archive-year:hover,
:deep(.app-container.black) .sidebar-module:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.4);
}

:deep(.app-container.black) .archive-title,
:deep(.app-container.black) .archive-subtitle,
:deep(.app-container.black) .year-title,
:deep(.app-container.black) .month-title,
:deep(.app-container.black) .article-title,
:deep(.app-container.black) .module-title,
:deep(.app-container.black) .announcement-content,
:deep(.app-container.black) .nav-item,
:deep(.app-container.black) .tab-btn,
:deep(.app-container.black) .profile-name,
:deep(.app-container.black) .profile-desc,
:deep(.app-container.black) .stat-label,
:deep(.app-container.black) .link-item,
:deep(.app-container.black) .dropdown-toggle,
:deep(.app-container.black) .dropdown-item,
:deep(.app-container.black) .feature-item {
  color: #777777;
}

:deep(.app-container.black) .article-date {
  color: #555555;
}

:deep(.app-container.black) .profile-stats {
  border-top-color: #222222;
  border-bottom-color: #222222;
}

:deep(.app-container.black) .profile-tabs {
  border-bottom-color: #222222;
}

:deep(.app-container.black) .back-btn {
  background-color: #1a1a1a;
  border-color: #222222;
  color: #777777;
}

:deep(.app-container.black) .back-btn:hover {
  background-color: #222222;
  border-color: #333333;
}

:deep(.app-container.black) .article-link:hover,
:deep(.app-container.black) .link-item:hover,
:deep(.app-container.black) .dropdown-toggle:hover,
:deep(.app-container.black) .dropdown-item:hover,
:deep(.app-container.black) .feature-item:hover {
  background-color: #1a1a1a;
}
</style>