<template>
  <div class="archive-view">

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
    <main class="archive-main">
      <div class="archive-container">
        <!-- 左侧内容 -->
        <div class="main-content">
          <div class="archive-header-section">
            <h2 class="archive-title">
              <span class="title-word" v-for="(char, index) in titleText" :key="index" :style="{ animationDelay: `${index * 0.1}s` }">{{ char }}</span>
            </h2>
            <p class="archive-subtitle">按时间顺序查看所有文章</p>
            <div class="archive-intro">
              <p class="intro-text">这里是我的文章归档空间，记录着我在不同时期的思考和创作。每一篇文章都是我成长的足迹，希望能与你分享我的知识和感悟。</p>
            </div>
            <div class="archive-stats">
              <div class="stat-item">
                <span class="stat-number">{{ articles.length }}</span>
                <span class="stat-label">篇文章</span>
              </div>
              <div class="stat-item">
                <span class="stat-number">{{ totalCategories }}</span>
                <span class="stat-label">个分类</span>
              </div>
              <div class="stat-item">
                <span class="stat-number">{{ totalTags }}</span>
                <span class="stat-label">个标签</span>
              </div>
            </div>
            <div class="archive-decoration">
              <span class="decoration-dot"></span>
              <span class="decoration-line"></span>
              <span class="decoration-dot"></span>
            </div>
          </div>
          
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
            <button class="back-btn" @click="goBack">
              <span class="btn-icon">←</span>
              返回上一页
            </button>
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

// 标题文本
const titleText = ref('归档')

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

// 计算总分类数
const totalCategories = computed(() => {
  const categories = new Set()
  articles.value.forEach(article => {
    if (article.categories) {
      article.categories.forEach(category => {
        categories.add(category)
      })
    }
  })
  return categories.size
})

// 计算总标签数
const totalTags = computed(() => {
  // 假设文章数据中有 tags 字段，如果没有则返回 0
  const tags = new Set()
  articles.value.forEach(article => {
    if (article.tags) {
      article.tags.forEach(tag => {
        tags.add(tag)
      })
    }
  })
  return tags.size
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
  position: relative;
  overflow: hidden;
  background-image: url('https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=colorful%20vibrant%20background%20for%20life%20journal%2C%20soft%20pastel%20colors%2C%20distinct%20patterns%2C%20inviting%20atmosphere%2C%20minimalist%20design&image_size=landscape_16_9');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
}

.archive-view::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.9) 0%, rgba(255, 255, 255, 0.8) 100%);
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

.archive-main {
  padding: 40px 0 80px;
  position: relative;
  z-index: 1;
}

.archive-container {
  display: flex;
  gap: 30px;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.main-content {
  flex: 1;
}

.archive-header-section {
  text-align: center;
  margin-bottom: 60px;
  position: relative;
  z-index: 1;
  padding: 20px 20px;
}

.archive-title {
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

.archive-title:hover {
  transform: scale(1.05);
  box-shadow: 0 6px 20px rgba(59, 130, 246, 0.4);
}



.title-word {
  display: inline-block;
  opacity: 0;
  transform: translateY(20px);
  animation: fadeInUp 0.6s ease-out both;
}

.archive-subtitle {
  font-size: 18px;
  color: #666;
  margin-bottom: 32px;
  animation: fadeInUp 0.8s ease-out 0.2s both;
}

.archive-intro {
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

.archive-stats {
  display: flex;
  justify-content: center;
  gap: 30px;
  margin: 32px 0;
  animation: fadeInUp 0.8s ease-out 0.4s both;
}

.archive-stats .stat-item {
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

.archive-stats .stat-item::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 4px;
  background: linear-gradient(90deg, #8b5cf6 0%, #ec4899 100%);
}

.archive-stats .stat-item:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
  border-color: #8b5cf6;
}

.archive-stats .stat-number {
  display: block;
  font-size: 28px;
  font-weight: bold;
  color: #8b5cf6;
  margin-bottom: 6px;
  font-family: 'Montserrat', sans-serif;
  transition: all 0.3s ease;
}

.archive-stats .stat-item:hover .stat-number {
  color: #ec4899;
}

.archive-stats .stat-label {
  display: block;
  font-size: 13px;
  color: #666;
  font-weight: 500;
  transition: all 0.3s ease;
}

.archive-stats .stat-item:hover .stat-label {
  color: #8b5cf6;
}

.archive-decoration {
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

.archive-years {
  display: flex;
  flex-direction: column;
  gap: 30px;
  position: relative;
  z-index: 1;
  max-width: 800px;
  margin: 0 auto;
}

.archive-year {
  background: white;
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
}

.archive-year::before {
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

.archive-year:hover {
  transform: translateY(-8px);
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.15);
  border-color: #ec4899;
}

.archive-year:hover::before {
  transform: scale(1.3);
  transition: transform 0.3s ease;
}

.year-title {
  font-size: 28px;
  font-weight: bold;
  color: #333;
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 2px solid #f0f0f0;
  position: relative;
  z-index: 1;
}

.year-months {
  display: flex;
  flex-direction: column;
  gap: 24px;
  position: relative;
  z-index: 1;
}

.archive-month {
  margin-left: 20px;
}

.month-title {
  font-size: 20px;
  font-weight: 600;
  color: #666;
  margin-bottom: 16px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.month-title::before {
  content: '';
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #8b5cf6;
}

.month-articles {
  list-style: none;
  padding: 0;
  margin: 0;
}

.article-item {
  margin-bottom: 12px;
  position: relative;
}

.article-link {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  border-radius: 12px;
  color: #333;
  text-decoration: none;
  transition: all 0.3s ease;
  background: #f8f9fa;
  border-left: 4px solid #8b5cf6;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  position: relative;
  z-index: 1;
}

.article-link:hover {
  background: #f5f3ff;
  color: #8b5cf6;
  transform: translateX(8px);
  box-shadow: 0 4px 12px rgba(139, 92, 246, 0.1);
}

.article-title {
  font-size: 18px;
  font-weight: 500;
  flex: 1;
  margin-right: 16px;
}

.article-date {
  font-size: 14px;
  color: #999;
  white-space: nowrap;
}

/* 返回按钮 */
.back-container {
  margin-top: 60px;
  display: flex;
  justify-content: center;
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

.btn-icon {
  font-size: 18px;
}

/* 右侧侧边栏 */
.right-sidebar {
  width: 300px;
  display: flex;
  flex-direction: column;
  gap: 24px;
  position: relative;
  z-index: 1;
}

.sidebar-module {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 20px;
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.1);
  padding: 24px;
  backdrop-filter: blur(10px);
  transition: all 0.3s ease;
  border-left: 4px solid #8b5cf6;
  position: relative;
  overflow: hidden;
}

.sidebar-module:hover {
  transform: translateY(-8px);
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.15);
  border-color: #ec4899;
}

.sidebar-module::before {
  content: '';
  position: absolute;
  top: 0;
  right: 0;
  width: 100px;
  height: 100px;
  background: radial-gradient(circle, rgba(139, 92, 246, 0.1) 0%, transparent 70%);
  border-radius: 50%;
  z-index: 0;
}

.module-title {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 16px;
  color: #333333;
  border-bottom: 1px solid #e9ecef;
  padding-bottom: 12px;
  position: relative;
  z-index: 1;
  background: linear-gradient(135deg, #8b5cf6 0%, #ec4899 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

/* 公告栏 */
.announcement-content {
  font-size: 14px;
  color: #666666;
  text-align: center;
  padding: 16px 0;
  position: relative;
  z-index: 1;
  background: #f8f9fa;
  border-radius: 12px;
  border-left: 4px solid #8b5cf6;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

/* 导航菜单 */
.nav-menu {
  list-style: none;
  padding: 0;
  margin: 0;
  position: relative;
  z-index: 1;
}

.nav-item {
  display: block;
  padding: 12px 16px;
  color: #666666;
  text-decoration: none;
  transition: all 0.3s ease;
  font-size: 14px;
  border-radius: 8px;
  margin-bottom: 8px;
  background: #f8f9fa;
  border-left: 3px solid transparent;
}

.nav-item:hover {
  color: #8b5cf6;
  padding-left: 20px;
  background: #f5f3ff;
  border-left-color: #8b5cf6;
  box-shadow: 0 2px 8px rgba(139, 92, 246, 0.1);
}

/* 个人简介 */
.profile-tabs {
  display: flex;
  margin-bottom: 20px;
  border-bottom: 1px solid #e9ecef;
  position: relative;
  z-index: 1;
}

.tab-btn {
  flex: 1;
  padding: 12px 0;
  background: none;
  border: none;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s ease;
  color: #666666;
  position: relative;
}

.tab-btn.active {
  color: #8b5cf6;
  border-bottom: 2px solid #8b5cf6;
}

.avatar-container {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
  position: relative;
  z-index: 1;
}

.avatar {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  overflow: hidden;
  border: 3px solid rgba(139, 92, 246, 0.4);
  box-shadow: 0 4px 16px rgba(139, 92, 246, 0.2);
  transition: all 0.3s ease;
}

.avatar:hover {
  transform: scale(1.05);
  box-shadow: 0 6px 20px rgba(139, 92, 246, 0.3);
}

.avatar-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.profile-name {
  font-size: 18px;
  font-weight: bold;
  text-align: center;
  margin-bottom: 12px;
  color: #333333;
  position: relative;
  z-index: 1;
}

.profile-desc {
  font-size: 14px;
  color: #666666;
  text-align: center;
  margin-bottom: 20px;
  line-height: 1.6;
  position: relative;
  z-index: 1;
  padding: 16px;
  background: #f8f9fa;
  border-radius: 12px;
  border-left: 4px solid #8b5cf6;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

/* 统计数据 */
.profile-stats {
  display: flex;
  justify-content: space-around;
  margin-bottom: 20px;
  padding: 16px 0;
  border-top: 1px solid #e9ecef;
  border-bottom: 1px solid #e9ecef;
  position: relative;
  z-index: 1;
}

.stat-item {
  text-align: center;
  padding: 16px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
  border: 1px solid #e9ecef;
  min-width: 80px;
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
  background: linear-gradient(90deg, #8b5cf6 0%, #ec4899 100%);
}

.stat-item:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
  border-color: #8b5cf6;
}

.stat-number {
  display: block;
  font-size: 24px;
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

/* 功能链接 */
.profile-links {
  display: flex;
  flex-direction: column;
  gap: 12px;
  position: relative;
  z-index: 1;
}

.link-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 12px 16px;
  border-radius: 20px;
  text-decoration: none;
  color: #666666;
  transition: all 0.3s ease;
  font-size: 14px;
  cursor: pointer;
  background: #f8f9fa;
  border: 1px solid #e9ecef;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.link-item:hover {
  background: #8b5cf6;
  color: white;
  transform: translateY(-1px);
  box-shadow: 0 4px 8px rgba(139, 92, 246, 0.3);
  border-color: #8b5cf6;
}

.link-icon {
  font-size: 18px;
}

/* 下拉链接 */
.dropdown-link {
  position: relative;
  z-index: 1;
}

.dropdown-toggle {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #f8f9fa;
  border: 1px solid #e9ecef;
  padding: 12px 16px;
  border-radius: 20px;
  color: #666666;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 14px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.dropdown-toggle:hover {
  background: #8b5cf6;
  color: white;
  box-shadow: 0 4px 8px rgba(139, 92, 246, 0.3);
  border-color: #8b5cf6;
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
  border-radius: 12px;
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.1);
  margin-top: 8px;
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
  gap: 10px;
  padding: 12px 16px;
  text-decoration: none;
  color: #666666;
  transition: all 0.3s ease;
  font-size: 14px;
  border-bottom: 1px solid #f0f0f0;
}

.dropdown-item:last-child {
  border-bottom: none;
}

.dropdown-item:hover {
  background: #f5f3ff;
  color: #8b5cf6;
}

/* 功能内容 */
.features-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
  position: relative;
  z-index: 1;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 16px;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 14px;
  color: #666666;
  background: #f8f9fa;
  border: 1px solid #e9ecef;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.feature-item:hover {
  background: #8b5cf6;
  color: white;
  transform: translateY(-1px);
  box-shadow: 0 4px 8px rgba(139, 92, 246, 0.3);
  border-color: #8b5cf6;
}

.feature-icon {
  font-size: 18px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .archive-container {
    flex-direction: column;
    padding: 0 16px;
  }
  
  .right-sidebar {
    width: 100%;
  }
  
  .archive-title {
    font-size: 28px;
    padding: 20px 40px;
  }
  
  .archive-subtitle {
    font-size: 16px;
  }
  
  .archive-year {
    padding: 24px;
  }
  
  .year-title {
    font-size: 24px;
  }
  
  .month-title {
    font-size: 18px;
  }
  
  .article-link {
    padding: 10px 14px;
  }
  
  .article-title {
    font-size: 16px;
  }
  
  .back-btn {
    padding: 12px 28px;
    font-size: 14px;
  }
  
  .sidebar-module {
    padding: 20px;
  }
}

@media (max-width: 480px) {
  .archive-title {
    font-size: 24px;
    padding: 16px 32px;
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
    padding: 8px 12px;
  }
  
  .article-title {
    font-size: 14px;
  }
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

@keyframes twinkle {
  0%, 100% {
    opacity: 0.3;
  }
  50% {
    opacity: 1;
  }
}

/* 主题模式适配 */
:deep(.app-container.dark) .archive-year,
:deep(.app-container.dark) .sidebar-module {
  background-color: rgba(24, 24, 24, 0.95);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.3);
}

:deep(.app-container.dark) .archive-year:hover,
:deep(.app-container.dark) .sidebar-module:hover {
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.4);
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
  background: linear-gradient(135deg, #8b5cf6 0%, #ec4899 100%);
  color: white;
}

:deep(.app-container.dark) .article-link,
:deep(.app-container.dark) .announcement-content,
:deep(.app-container.dark) .nav-item,
:deep(.app-container.dark) .profile-desc,
:deep(.app-container.dark) .link-item,
:deep(.app-container.dark) .dropdown-toggle,
:deep(.app-container.dark) .feature-item {
  background: rgba(30, 30, 30, 0.8);
  border-color: #333333;
}

:deep(.app-container.dark) .stat-item {
  background: rgba(30, 30, 30, 0.8);
  border-color: #333333;
}

:deep(.app-container.dark) .article-link:hover,
:deep(.app-container.dark) .nav-item:hover,
:deep(.app-container.dark) .link-item:hover,
:deep(.app-container.dark) .dropdown-toggle:hover,
:deep(.app-container.dark) .dropdown-item:hover,
:deep(.app-container.dark) .feature-item:hover {
  background: #8b5cf6;
  color: white;
}

:deep(.app-container.black) .archive-year,
:deep(.app-container.black) .sidebar-module {
  background-color: rgba(17, 17, 17, 0.95);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.4);
}

:deep(.app-container.black) .archive-year:hover,
:deep(.app-container.black) .sidebar-module:hover {
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.5);
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

:deep(.app-container.black) .article-link,
:deep(.app-container.black) .announcement-content,
:deep(.app-container.black) .nav-item,
:deep(.app-container.black) .profile-desc,
:deep(.app-container.black) .link-item,
:deep(.app-container.black) .dropdown-toggle,
:deep(.app-container.black) .feature-item {
  background: rgba(25, 25, 25, 0.8);
  border-color: #222222;
}

:deep(.app-container.black) .stat-item {
  background: rgba(25, 25, 25, 0.8);
  border-color: #222222;
}

:deep(.app-container.black) .article-link:hover,
:deep(.app-container.black) .nav-item:hover,
:deep(.app-container.black) .link-item:hover,
:deep(.app-container.black) .dropdown-toggle:hover,
:deep(.app-container.black) .dropdown-item:hover,
:deep(.app-container.black) .feature-item:hover {
  background: #8b5cf6;
  color: white;
}
</style>