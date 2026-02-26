<template>
  <div class="portal-view" data-route="/" :class="currentMode">
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
          <router-link to="/message" class="nav-link">留言板</router-link>
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
          <div class="sidebar-module announcement" style="animation-delay: 0.1s;">
            <h3 class="module-title">小窝公告栏</h3>
            <p class="announcement-content">天行健，君子以自强不息</p>
          </div>
          
          <!-- 导航菜单 -->
          <div class="sidebar-module navigation" style="animation-delay: 0.2s;">
            <h3 class="module-title">导航菜单</h3>
            <ul class="nav-menu">
              <li><a href="#" class="nav-item">首页</a></li>
              <li><a href="#" class="nav-item">文章</a></li>
              <li><a href="#" class="nav-item">说说</a></li>
              <li><a href="#" class="nav-item">归档</a></li>
            </ul>
          </div>
          
          <!-- 个人简介 -->
          <div class="sidebar-module profile" style="animation-delay: 0.3s;">
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
                  <span class="stat-number">{{ articles.length }}</span>
                  <span class="stat-label">文章</span>
                </div>
                <div class="stat-item">
                  <span class="stat-number">{{ categories.length }}</span>
                  <span class="stat-label">分类</span>
                </div>
                <div class="stat-item">
                  <span class="stat-number">{{ tags.length }}</span>
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
              
              <!-- 功能操作界面 -->
              <div class="feature-content" v-if="activeFeature">
                <!-- 文章管理 -->
                <div v-if="activeFeature === 'article'" class="feature-panel csdn-panel">
                  <!-- 文章列表 -->
                  <div v-if="!isEditing" class="csdn-article-management">
                    <h4 class="panel-title csdn-panel-title">文章管理</h4>
                    <div class="panel-content csdn-panel-content">
                      <button class="action-btn csdn-primary" @click="handleNewArticle">+ 新建文章</button>
                      <div class="article-list-admin csdn-article-list">
                        <div v-for="article in articles" :key="article.id" class="article-item csdn-article-item">
                          <div class="article-info csdn-article-info">
                            <h5>{{ article.title }}</h5>
                            <p>{{ formatDate(article.date) }}</p>
                          </div>
                          <div class="article-actions csdn-article-actions">
                            <button class="action-btn csdn-edit" @click="handleEditArticle(article)">编辑</button>
                            <button class="action-btn csdn-delete" @click="openDeleteModal(article)">删除</button>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                  
                  <!-- 编辑文章界面 -->
                  <div v-else class="csdn-article-editor">
                    <h4 class="panel-title csdn-panel-title">{{ currentArticleTitle ? '编辑文章' : '新建文章' }}</h4>
                    <div class="panel-content csdn-panel-content csdn-editor-content">
                      <!-- 文章标题 -->
                      <div class="editor-section">
                        <input 
                          type="text" 
                          v-model="currentArticleTitle" 
                          placeholder="请输入文章标题" 
                          class="csdn-editor-title"
                        >
                      </div>
                      
                      <!-- 拖放上传区域 -->
                      <div 
                        class="editor-section drag-drop-zone" 
                        @dragover.prevent @dragenter.prevent @drop.prevent="handleFileDrop"
                      >
                        <div class="drag-drop-content" :class="{ 'dragging': isDragging }">
                          <span class="drag-drop-icon">📄</span>
                          <h5>拖放MD文件到此处上传</h5>
                          <p>或者 <label class="browse-label">
                            <input type="file" accept=".md,.markdown" @change="handleFileSelect" style="display: none;">
                            浏览文件
                          </label></p>
                          <p class="drag-drop-hint">支持 .md 和 .markdown 文件</p>
                        </div>
                      </div>
                      
                      <!-- 文章内容（Markdown编辑器） -->
                      <div class="editor-section">
                        <textarea 
                          v-model="articleContent" 
                          placeholder="请输入文章内容（支持Markdown格式）" 
                          class="csdn-editor-textarea"
                          rows="15"
                        ></textarea>
                      </div>
                      
                      <!-- 操作按钮 -->
                      <div class="editor-actions">
                        <button class="action-btn csdn-primary" @click="handleSaveArticle">保存文章</button>
                        <button class="action-btn csdn-cancel" @click="handleCancelEdit">取消</button>
                      </div>
                    </div>
                  </div>
                </div>
                
                <!-- 分类管理 -->
                <div v-if="activeFeature === 'category'" class="feature-panel csdn-panel">
                  <!-- 分类列表 -->
                  <div v-if="!isEditingCategory" class="csdn-category-management">
                    <h4 class="panel-title csdn-panel-title">分类管理</h4>
                    <div class="panel-content csdn-panel-content">
                      <button class="action-btn csdn-primary" @click="handleNewCategory">+ 新建分类</button>
                      <div class="category-list csdn-category-list">
                        <div v-for="category in categories" :key="category.id" class="category-item csdn-category-item">
                          <span>{{ category.name }}</span>
                          <div class="category-actions csdn-category-actions">
                            <button class="action-btn csdn-edit" @click="handleEditCategory(category)">编辑</button>
                            <button class="action-btn csdn-delete" @click="handleDeleteCategory(category)">删除</button>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                  
                  <!-- 编辑分类界面 -->
                  <div v-else class="csdn-category-editor">
                    <h4 class="panel-title csdn-panel-title">{{ currentCategory.id ? '编辑分类' : '新建分类' }}</h4>
                    <div class="panel-content csdn-panel-content csdn-editor-content">
                      <!-- 分类名称 -->
                      <div class="editor-section">
                        <input 
                          type="text" 
                          v-model="currentCategory.name" 
                          placeholder="请输入分类名称" 
                          class="csdn-editor-title"
                        >
                      </div>
                      
                      <!-- 操作按钮 -->
                      <div class="editor-actions">
                        <button class="action-btn csdn-primary" @click="handleSaveCategory">保存分类</button>
                        <button class="action-btn csdn-cancel" @click="handleCancelCategoryEdit">取消</button>
                      </div>
                    </div>
                  </div>
                </div>
                
                <!-- 标签管理 -->
                <div v-if="activeFeature === 'tag'" class="feature-panel csdn-panel">
                  <!-- 标签列表 -->
                  <div v-if="!isEditingTag" class="csdn-tag-management">
                    <h4 class="panel-title csdn-panel-title">标签管理</h4>
                    <div class="panel-content csdn-panel-content">
                      <button class="action-btn csdn-primary" @click="handleNewTag">+ 新建标签</button>
                      <div class="tag-list csdn-tag-list">
                        <div v-for="tag in tags" :key="tag.id" class="tag-item csdn-tag-item">
                          <span>{{ tag.name }}</span>
                          <div class="tag-actions csdn-tag-actions">
                            <button class="action-btn csdn-edit" @click="handleEditTag(tag)">编辑</button>
                            <button class="action-btn csdn-delete" @click="handleDeleteTag(tag)">删除</button>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                  
                  <!-- 编辑标签界面 -->
                  <div v-else class="csdn-tag-editor">
                    <h4 class="panel-title csdn-panel-title">{{ currentTag.id ? '编辑标签' : '新建标签' }}</h4>
                    <div class="panel-content csdn-panel-content csdn-editor-content">
                      <!-- 标签名称 -->
                      <div class="editor-section">
                        <input 
                          type="text" 
                          v-model="currentTag.name" 
                          placeholder="请输入标签名称" 
                          class="csdn-editor-title"
                        >
                      </div>
                      
                      <!-- 操作按钮 -->
                      <div class="editor-actions">
                        <button class="action-btn csdn-primary" @click="handleSaveTag">保存标签</button>
                        <button class="action-btn csdn-cancel" @click="handleCancelTagEdit">取消</button>
                      </div>
                    </div>
                  </div>
                </div>
                
                <!-- 个人设置 -->
                <div v-if="activeFeature === 'profile'" class="feature-panel csdn-panel">
                  <h4 class="panel-title csdn-panel-title">个人设置</h4>
                  <div class="panel-content csdn-panel-content">
                    <div class="setting-item">
                      <label>用户名</label>
                      <input type="text" v-model="userSettings.username" class="csdn-editor-title">
                    </div>
                    <div class="setting-item">
                      <label>个人描述</label>
                      <input type="text" v-model="userSettings.description" class="csdn-editor-title">
                    </div>
                    <div class="setting-item">
                      <label>头像</label>
                      <div class="avatar-upload">
                        <div class="current-avatar">
                          <img :src="userSettings.avatar" alt="Avatar">
                        </div>
                        <div class="avatar-upload-actions">
                          <button class="action-btn csdn-edit" @click="triggerFileInput">选择图片</button>
                          <input 
                            type="file" 
                            ref="fileInput" 
                            accept="image/*" 
                            @change="handleAvatarUpload" 
                            style="display: none;"
                          >
                        </div>
                      </div>
                    </div>
                    <div class="editor-actions">
                      <button class="action-btn csdn-primary" @click="handleSaveProfile">保存设置</button>
                      <button class="action-btn csdn-cancel" @click="handleCancelProfileEdit">取消</button>
                    </div>
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
        <div class="progress-icon" aria-label="滚动进度" @click="scrollToTop">
          <div class="progress-circle" :style="{ '--progress': scrollProgress + '%' }">
            <span class="progress-text">{{ Math.round(scrollProgress) }}%</span>
          </div>
        </div>
        <button class="icon-btn" aria-label="返回顶部" @click="scrollToTop">
          <span class="icon">↑</span>
        </button>
        <button class="icon-btn" aria-label="主题切换" @click="toggleMode">
          <span class="icon" v-if="currentMode === 'light'">☀️</span>
          <span class="icon" v-else-if="currentMode === 'dark'">🌙</span>
          <span class="icon" v-else>⚫</span>
        </button>
        <button class="icon-btn" aria-label="设置" @click="openSettings">
          <span class="icon">⚙️</span>
        </button>
      </div>
    </section>

    <!-- 删除确认模态框 -->
    <div v-if="showDeleteModal" class="modal-overlay" @click="closeDeleteModal">
      <div class="modal-content" @click.stop>
        <h3 class="modal-title">确认删除</h3>
        <p class="modal-message">确定要删除文章: {{ articleToDelete?.title }}吗？</p>
        <div class="modal-actions">
          <button class="action-btn" @click="closeDeleteModal">取消</button>
          <button class="action-btn danger" @click="confirmDelete">确认删除</button>
        </div>
      </div>
    </div>
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
const scrollProgress = ref(0)
// 功能管理
const activeFeature = ref('')
// 编辑文章相关
const isEditing = ref(false)
const currentArticleTitle = ref('')
const articleContent = ref('')
const currentArticleId = ref(null)
// 拖放上传相关
const isDragging = ref(false)
const fileInput = ref(null)
// 删除确认模态框相关
const showDeleteModal = ref(false)
const articleToDelete = ref(null)
// 导入头像图片
import avatarImage from '@/assets/images/avatar.png'

// 个人设置相关
const userSettings = ref({
  username: 'Dream',
  description: '少年立志青云间，不负凌云万丈才',
  avatar: avatarImage
})
// 分类管理相关
const categories = ref([
  { id: 1, name: '生活' },
  { id: 2, name: 'Learn' },
  { id: 3, name: '建站' }
])
const isEditingCategory = ref(false)
const currentCategory = ref({ id: null, name: '' })
// 标签管理相关
const tags = ref([
  { id: 1, name: 'Vue' },
  { id: 2, name: 'TypeScript' },
  { id: 3, name: '前端' }
])
const isEditingTag = ref(false)
const currentTag = ref({ id: null, name: '' })
// 文章管理相关
const articles = ref([])

// 加载文章数据
const loadArticles = () => {
  const savedArticles = localStorage.getItem('articles')
  if (savedArticles) {
    articles.value = JSON.parse(savedArticles)
  } else {
    // 初始化默认数据
    const defaultArticles = [
      {
        id: 1, 
        title: '2025年终总结', 
        date: '2026-02-24', 
        content: '# 2025年终总结\n\n这是2025年的年终总结内容...',
        categories: ['生活'],
        excerpt: '2025 年终总结。诈尸一下，马上就是 2026 年了，哈哈哈。过得太快了吧。期间还想着水水文章的，结果完全没有了本科期间的热情，能写个年终总结就不错了。',
        stats: { likes: 9, comments: 12, views: 0 }
      },
      {
        id: 2, 
        title: '实习小记', 
        date: '2025-02-18', 
        content: '# 实习小记\n\n这是实习期间的记录...',
        categories: ['生活', 'Learn'],
        excerpt: '月记。最近想趁着没啥课，导师管的松的时候出去找个实习，结果一个也没找到，2333。',
        stats: { likes: 5, comments: 8, views: 0 }
      }
    ]
    articles.value = defaultArticles
    saveArticles()
  }
}

// 保存文章数据
const saveArticles = () => {
  localStorage.setItem('articles', JSON.stringify(articles.value))
  // 触发 storage 事件，通知其他组件更新
  window.dispatchEvent(new Event('storage'))
}

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
  // 触发storage事件，通知其他组件更新
  window.dispatchEvent(new Event('storage'))
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
// 格式化日期函数
const formatDate = (dateString) => {
  const date = new Date(dateString)
  const month = date.getMonth() + 1
  const day = date.getDate()
  const year = date.getFullYear()
  return `${month}月${day}日 , ${year}`
}

const handleNewArticle = () => {
  currentArticleTitle.value = ''
  articleContent.value = ''
  isEditing.value = true
}

const handleEditArticle = (article) => {
  currentArticleTitle.value = article.title
  articleContent.value = article.content
  // 保存当前编辑的文章ID
  currentArticleId.value = article.id
  isEditing.value = true
}

const handleSaveArticle = () => {
  if (currentArticleTitle.value.trim() === '') {
    alert('请输入文章标题')
    return
  }
  if (articleContent.value.trim() === '') {
    alert('请输入文章内容')
    return
  }

  if (currentArticleId.value) {
    // 编辑现有文章
    const index = articles.value.findIndex(article => article.id === currentArticleId.value)
    if (index !== -1) {
      articles.value[index] = {
        ...articles.value[index],
        title: currentArticleTitle.value,
        content: articleContent.value
      }
      alert(`文章 "${currentArticleTitle.value}" 已更新`)
      saveArticles()
    }
  } else {
    // 新建文章
    const newId = articles.value.length > 0 ? Math.max(...articles.value.map(article => article.id)) + 1 : 1
    const newArticle = {
      id: newId,
      title: currentArticleTitle.value,
      date: new Date().toISOString().split('T')[0], // 生成当前日期
      content: articleContent.value,
      categories: ['生活'], // 默认分类
      excerpt: articleContent.value.substring(0, 100) + '...', // 生成摘要
      stats: { likes: 0, comments: 0 } // 默认统计数据
    }
    articles.value.push(newArticle)
    alert(`文章 "${newArticle.title}" 已创建`)
    saveArticles()
  }

  isEditing.value = false
  currentArticleId.value = null // 重置编辑状态
}

const handleCancelEdit = () => {
  isEditing.value = false
  currentArticleId.value = null // 重置编辑状态
}

// 打开删除确认模态框
const openDeleteModal = (article) => {
  console.log('Opening delete modal for article:', article.title)
  articleToDelete.value = article
  showDeleteModal.value = true
}

// 关闭删除确认模态框
const closeDeleteModal = () => {
  console.log('Closing delete modal')
  showDeleteModal.value = false
  articleToDelete.value = null
}

// 确认删除文章
const confirmDelete = () => {
  if (articleToDelete.value) {
    console.log('Confirming delete for article:', articleToDelete.value.title)
    articles.value = articles.value.filter(a => a.id !== articleToDelete.value.id)
    alert(`文章: ${articleToDelete.value.title} 已删除`)
    saveArticles()
    closeDeleteModal()
  }
}

// 旧的删除文章函数（保留作为备份）
const handleDeleteArticle = (article) => {
  console.log('Delete article called:', article.title)
  const confirmed = confirm(`确定要删除文章: ${article.title}吗？`)
  console.log('Confirm result:', confirmed)
  if (confirmed) {
    console.log('Deleting article:', article.title)
    articles.value = articles.value.filter(a => a.id !== article.id)
    alert(`文章: ${article.title} 已删除`)
    saveArticles()
  } else {
    console.log('Delete cancelled for article:', article.title)
  }
}

// 拖放上传处理函数
const handleFileDrop = (event) => {
  isDragging.value = false
  const files = event.dataTransfer.files
  if (files.length > 0) {
    processFile(files[0])
  }
}

const handleFileSelect = (event) => {
  const files = event.target.files
  if (files.length > 0) {
    processFile(files[0])
  }
}

const processFile = (file) => {
  // 检查文件类型
  if (!file.name.endsWith('.md') && !file.name.endsWith('.markdown')) {
    alert('请上传 .md 或 .markdown 文件')
    return
  }

  // 读取文件内容
  const reader = new FileReader()
  reader.onload = (e) => {
    const content = e.target.result
    articleContent.value = content
    
    // 自动提取标题（如果文件以 # 开头）
    const titleMatch = content.match(/^#\s+(.+)$/m)
    if (titleMatch && !currentArticleTitle.value) {
      currentArticleTitle.value = titleMatch[1]
    }
    
    // 预留后端接口调用（后期实现）
    // uploadFileToBackend(file)
  }
  reader.onerror = () => {
    alert('文件读取失败')
  }
  reader.readAsText(file)
}

// 预留后端上传接口（后期实现）
const uploadFileToBackend = (file) => {
  // 示例：后期实现的后端上传逻辑
  /*
  const formData = new FormData()
  formData.append('file', file)
  
  fetch('/api/upload', {
    method: 'POST',
    body: formData
  })
  .then(response => response.json())
  .then(data => {
    console.log('文件上传成功:', data)
  })
  .catch(error => {
    console.error('文件上传失败:', error)
  })
  */
  console.log('预留后端上传接口：', file.name)
}

const handleNewCategory = () => {
  currentCategory.value = { id: null, name: '' }
  isEditingCategory.value = true
}

const handleEditCategory = (category) => {
  currentCategory.value = { ...category }
  isEditingCategory.value = true
}

const handleSaveCategory = () => {
  if (currentCategory.value.name.trim() === '') {
    alert('请输入分类名称')
    return
  }

  if (currentCategory.value.id) {
    // 编辑现有分类
    const index = categories.value.findIndex(cat => cat.id === currentCategory.value.id)
    if (index !== -1) {
      categories.value[index] = { ...currentCategory.value }
      alert(`分类 "${currentCategory.value.name}" 已更新`)
      // 预留后端接口调用（后期实现）
      // updateCategoryToBackend(currentCategory.value)
    }
  } else {
    // 新建分类
    const newId = categories.value.length > 0 ? Math.max(...categories.value.map(cat => cat.id)) + 1 : 1
    const newCategory = {
      id: newId,
      name: currentCategory.value.name
    }
    categories.value.push(newCategory)
    alert(`分类 "${newCategory.name}" 已创建`)
    // 预留后端接口调用（后期实现）
    // createCategoryToBackend(newCategory)
  }

  isEditingCategory.value = false
}

const handleCancelCategoryEdit = () => {
  isEditingCategory.value = false
}

const handleDeleteCategory = (category) => {
  if (confirm(`确定要删除分类: ${category.name}吗？`)) {
    categories.value = categories.value.filter(cat => cat.id !== category.id)
    alert(`分类: ${category.name} 已删除`)
    // 预留后端接口调用（后期实现）
    // deleteCategoryFromBackend(category.id)
  }
}

// 预留后端接口函数（后期实现）
const createCategoryToBackend = (category) => {
  // 示例：后期实现的后端创建逻辑
  /*
  fetch('/api/categories', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(category)
  })
  .then(response => response.json())
  .then(data => {
    console.log('分类创建成功:', data)
  })
  .catch(error => {
    console.error('分类创建失败:', error)
  })
  */
  console.log('预留后端创建分类接口：', category)
}

const updateCategoryToBackend = (category) => {
  // 示例：后期实现的后端更新逻辑
  /*
  fetch(`/api/categories/${category.id}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(category)
  })
  .then(response => response.json())
  .then(data => {
    console.log('分类更新成功:', data)
  })
  .catch(error => {
    console.error('分类更新失败:', error)
  })
  */
  console.log('预留后端更新分类接口：', category)
}

const deleteCategoryFromBackend = (categoryId) => {
  // 示例：后期实现的后端删除逻辑
  /*
  fetch(`/api/categories/${categoryId}`, {
    method: 'DELETE'
  })
  .then(response => {
    if (response.ok) {
      console.log('分类删除成功')
    } else {
      throw new Error('删除失败')
    }
  })
  .catch(error => {
    console.error('分类删除失败:', error)
  })
  */
  console.log('预留后端删除分类接口：', categoryId)
}

const handleNewTag = () => {
  currentTag.value = { id: null, name: '' }
  isEditingTag.value = true
}

const handleEditTag = (tag) => {
  currentTag.value = { ...tag }
  isEditingTag.value = true
}

const handleSaveTag = () => {
  if (currentTag.value.name.trim() === '') {
    alert('请输入标签名称')
    return
  }

  if (currentTag.value.id) {
    // 编辑现有标签
    const index = tags.value.findIndex(t => t.id === currentTag.value.id)
    if (index !== -1) {
      tags.value[index] = { ...currentTag.value }
      alert(`标签 "${currentTag.value.name}" 已更新`)
      // 预留后端接口调用（后期实现）
      // updateTagToBackend(currentTag.value)
    }
  } else {
    // 新建标签
    const newId = tags.value.length > 0 ? Math.max(...tags.value.map(t => t.id)) + 1 : 1
    const newTag = {
      id: newId,
      name: currentTag.value.name
    }
    tags.value.push(newTag)
    alert(`标签 "${newTag.name}" 已创建`)
    // 预留后端接口调用（后期实现）
    // createTagToBackend(newTag)
  }

  isEditingTag.value = false
}

const handleCancelTagEdit = () => {
  isEditingTag.value = false
}

const handleDeleteTag = (tag) => {
  if (confirm(`确定要删除标签: ${tag.name}吗？`)) {
    tags.value = tags.value.filter(t => t.id !== tag.id)
    alert(`标签: ${tag.name} 已删除`)
    // 预留后端接口调用（后期实现）
    // deleteTagFromBackend(tag.id)
  }
}

// 预留后端接口函数（后期实现）
const createTagToBackend = (tag) => {
  // 示例：后期实现的后端创建逻辑
  /*
  fetch('/api/tags', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(tag)
  })
  .then(response => response.json())
  .then(data => {
    console.log('标签创建成功:', data)
  })
  .catch(error => {
    console.error('标签创建失败:', error)
  })
  */
  console.log('预留后端创建标签接口：', tag)
}

const updateTagToBackend = (tag) => {
  // 示例：后期实现的后端更新逻辑
  /*
  fetch(`/api/tags/${tag.id}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(tag)
  })
  .then(response => response.json())
  .then(data => {
    console.log('标签更新成功:', data)
  })
  .catch(error => {
    console.error('标签更新失败:', error)
  })
  */
  console.log('预留后端更新标签接口：', tag)
}

const deleteTagFromBackend = (tagId) => {
  // 示例：后期实现的后端删除逻辑
  /*
  fetch(`/api/tags/${tagId}`, {
    method: 'DELETE'
  })
  .then(response => {
    if (response.ok) {
      console.log('标签删除成功')
    } else {
      throw new Error('删除失败')
    }
  })
  .catch(error => {
    console.error('标签删除失败:', error)
  })
  */
  console.log('预留后端删除标签接口：', tagId)
}

const handleSaveProfile = () => {
  // 保存设置到localStorage
  localStorage.setItem('userSettings', JSON.stringify(userSettings.value))
  // 预留后端接口调用（后期实现）
  // updateUserSettingsToBackend(userSettings.value)
  alert('个人设置已保存并同步')
}

const handleCancelProfileEdit = () => {
  // 重新加载设置
  loadUserSettings()
  alert('已取消更改')
}

const handleSaveSite = () => {
  alert('站点设置已保存')
}

const triggerFileInput = () => {
  // 触发文件选择对话框
  fileInput.value.click()
}

const handleAvatarUpload = (event) => {
  const file = event.target.files[0]
  if (!file) return
  
  // 检查文件类型
  if (!file.type.startsWith('image/')) {
    alert('请选择图片文件')
    return
  }
  
  // 检查文件大小（限制为 5MB）
  if (file.size > 5 * 1024 * 1024) {
    alert('图片大小不能超过 5MB')
    return
  }
  
  // 创建 FileReader 读取文件
  const reader = new FileReader()
  reader.onload = (e) => {
    // 使用 data URL 作为头像
    userSettings.value.avatar = e.target.result
    alert('头像已更新')
    // 预留后端接口调用（后期实现）
    // uploadAvatarToBackend(file)
  }
  reader.onerror = () => {
    alert('图片读取失败')
  }
  reader.readAsDataURL(file)
}

// 加载用户设置
const loadUserSettings = () => {
  const savedSettings = localStorage.getItem('userSettings')
  if (savedSettings) {
    const saved = JSON.parse(savedSettings)
    // 确保头像使用正确的引用
    userSettings.value = {
      ...saved,
      avatar: avatarImage // 始终使用导入的头像图片
    }
  }
}

// 预留后端接口函数（后期实现）
const updateUserSettingsToBackend = (settings) => {
  // 示例：后期实现的后端更新逻辑
  /*
  fetch('/api/user/settings', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(settings)
  })
  .then(response => response.json())
  .then(data => {
    console.log('个人设置更新成功:', data)
  })
  .catch(error => {
    console.error('个人设置更新失败:', error)
  })
  */
  console.log('预留后端更新个人设置接口：', settings)
}

// 预留后端头像上传接口（后期实现）
const uploadAvatarToBackend = (file) => {
  // 示例：后期实现的后端上传逻辑
  /*
  const formData = new FormData()
  formData.append('avatar', file)
  
  fetch('/api/user/avatar', {
    method: 'POST',
    body: formData
  })
  .then(response => response.json())
  .then(data => {
    console.log('头像上传成功:', data)
    // 更新用户设置中的头像 URL
    userSettings.value.avatar = data.avatarUrl
  })
  .catch(error => {
    console.error('头像上传失败:', error)
  })
  */
  console.log('预留后端上传头像接口：', file.name)
}

onMounted(() => {
  // 加载用户设置
  loadUserSettings()
  
  // 加载文章数据
  loadArticles()
  
  // 启动文字动画
  animateText()

  // 监听滚动，实现滚动检测和进度计算
  window.addEventListener('scroll', () => {
    const scrollY = window.scrollY
    // 检测是否滚动超过阈值
    if (scrollY > window.innerHeight * 0.3) {
      isScrolled.value = true
    } else {
      isScrolled.value = false
    }
    
    // 计算滚动进度
    const totalHeight = document.documentElement.scrollHeight - window.innerHeight
    const progress = (scrollY / totalHeight) * 100
    scrollProgress.value = Math.min(Math.max(progress, 0), 100)
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
  background: url('@/assets/images/backgrounds/light-bg.jpg');
  background-size: cover;
  background-attachment: fixed;
  background-position: center;
  color: #333333;
}

.portal-view.dark {
  background: url('@/assets/images/backgrounds/dark-bg.jpg');
  background-size: cover;
  background-attachment: fixed;
  background-position: center;
  color: #eaeaea;
}

.portal-view.black {
  background: url('@/assets/images/backgrounds/black-bg.jpg');
  background-size: cover;
  background-attachment: fixed;
  background-position: center;
  color: #e0e0e0;
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
  transition: all 0.5s cubic-bezier(0.4, 0, 0.2, 1);
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  transform: translateY(0);
}

/* 滚动时的导航栏样式 */
.portal-nav.scrolled {
  background-color: rgba(0, 0, 0, 0.8);
  backdrop-filter: blur(15px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.3);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.5);
  transform: translateY(-5px);
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

.nav-link.active {
  color: #4a6fa5;
  font-weight: 600;
}

.nav-link.active::after {
  width: 100%;
  background-color: #4a6fa5;
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
  background: transparent;
  border-radius: 8px;
}

.portal-view.dark .blog-container {
  background: transparent;
}

.portal-view.black .blog-container {
  background: transparent;
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
  border-bottom: 1px solid #e0e0e0;
  padding-bottom: 8px;
  margin-bottom: 12px;
}

.portal-view.dark .module-title {
  border-bottom: 1px solid #333333;
}

.portal-view.black .module-title {
  border-bottom: 1px solid #222222;
}

/* 侧边栏模块 */
.sidebar-module {
  background: #ffffff;
  border-radius: 8px;
  padding: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  opacity: 0;
  transform: translateX(-30px) scale(0.9);
  animation: fadeInLeft 0.8s cubic-bezier(0.4, 0, 0.2, 1) forwards;
}

.sidebar-module:hover {
  transform: translateX(5px) scale(1.02);
  box-shadow: 0 6px 24px rgba(0, 0, 0, 0.15);
}

@keyframes fadeInLeft {
  to {
    opacity: 1;
    transform: translateX(0) scale(1);
  }
}

.portal-view.dark .sidebar-module {
  background: #181818;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.3);
}

.portal-view.black .sidebar-module {
  background: #111111;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.4);
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
  justify-content: space-between;
  margin: 16px 0;
  padding: 12px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 6px;
}

.stat-item {
  flex: 1;
  text-align: center;
  padding: 0 10px;
}

.stat-number {
  display: block;
  font-size: 20px;
  font-weight: bold;
  color: white;
  margin-bottom: 4px;
  min-height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.stat-label {
  display: block;
  font-size: 12px;
  color: rgba(255, 255, 255, 0.8);
  min-height: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
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

/* CSDN风格样式 */
.features-list.csdn-style {
  gap: 4px;
  margin-bottom: 16px;
}

.feature-item.csdn-item {
  padding: 12px 16px;
  background: rgba(255, 255, 255, 0.15);
  border-radius: 6px;
  color: white;
  transition: all 0.2s ease;
  cursor: pointer;
  border: 1px solid transparent;
  font-size: 14px;
  font-weight: 500;
}

.feature-item.csdn-item:hover {
  background: rgba(255, 255, 255, 0.25);
  transform: translateX(4px);
  border-color: rgba(255, 255, 255, 0.3);
}

.feature-item.csdn-item .feature-icon {
  font-size: 18px;
  margin-right: 12px;
  width: 24px;
  text-align: center;
}

.feature-item.csdn-item .feature-text {
  font-size: 14px;
  flex: 1;
}

/* CSDN风格面板 */
.feature-panel.csdn-panel {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  border: 1px solid #e0e0e0;
}

.panel-title.csdn-panel-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  padding: 16px 20px;
  background: #f5f5f5;
  border-bottom: 1px solid #e0e0e0;
  margin: 0;
}

.panel-content.csdn-panel-content {
  padding: 20px;
}

/* CSDN风格按钮 */
.action-btn.csdn-primary {
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  margin-bottom: 20px;
  background: #1890ff;
  color: white;
  box-shadow: 0 2px 4px rgba(24, 144, 255, 0.2);
}

.action-btn.csdn-primary:hover {
  background: #40a9ff;
  transform: translateY(-1px);
  box-shadow: 0 4px 8px rgba(24, 144, 255, 0.3);
}

.action-btn.csdn-edit {
  padding: 6px 12px;
  border: 1px solid #d9d9d9;
  border-radius: 4px;
  font-size: 13px;
  font-weight: 400;
  cursor: pointer;
  transition: all 0.3s ease;
  margin-right: 8px;
  background: white;
  color: #333;
}

.action-btn.csdn-edit:hover {
  border-color: #1890ff;
  color: #1890ff;
}

.action-btn.csdn-delete {
  padding: 6px 12px;
  border: 1px solid #ff4d4f;
  border-radius: 4px;
  font-size: 13px;
  font-weight: 400;
  cursor: pointer;
  transition: all 0.3s ease;
  background: white;
  color: #ff4d4f;
}

.action-btn.csdn-delete:hover {
  background: #ff4d4f;
  color: white;
}

/* CSDN风格文章列表 */
.article-list-admin.csdn-article-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.article-item.csdn-article-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  background: white;
  border-radius: 6px;
  border: 1px solid #e0e0e0;
  transition: all 0.3s ease;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
}

.article-item.csdn-article-item:hover {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
  border-color: #1890ff;
}

.article-info.csdn-article-info h5 {
  font-size: 15px;
  font-weight: 600;
  color: #333;
  margin-bottom: 8px;
  line-height: 1.4;
}

.article-info.csdn-article-info p {
  font-size: 13px;
  color: #999;
  margin: 0;
}

.article-actions.csdn-article-actions {
  display: flex;
  gap: 8px;
}

/* CSDN风格编辑器 */
.csdn-article-editor {
  width: 100%;
}

.csdn-editor-content {
  padding: 24px;
}

.editor-section {
  margin-bottom: 24px;
}

.csdn-editor-title {
  width: 100%;
  padding: 12px 16px;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  font-size: 18px;
  font-weight: 600;
  color: #333;
  transition: all 0.3s ease;
  box-sizing: border-box;
}

.csdn-editor-title:focus {
  outline: none;
  border-color: #1890ff;
  box-shadow: 0 0 0 2px rgba(24, 144, 255, 0.2);
}

.csdn-editor-textarea {
  width: 100%;
  padding: 16px;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  font-size: 14px;
  font-family: 'PingFang SC', 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
  color: #333;
  line-height: 1.6;
  resize: vertical;
  transition: all 0.3s ease;
  box-sizing: border-box;
  min-height: 400px;
}

.csdn-editor-textarea:focus {
  outline: none;
  border-color: #1890ff;
  box-shadow: 0 0 0 2px rgba(24, 144, 255, 0.2);
}

.editor-actions {
  display: flex;
  gap: 12px;
  justify-content: flex-end;
  margin-top: 24px;
  padding-top: 16px;
  border-top: 1px solid #e0e0e0;
}

.action-btn.csdn-cancel {
  padding: 10px 20px;
  border: 1px solid #d9d9d9;
  border-radius: 4px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  background: white;
  color: #333;
}

.action-btn.csdn-cancel:hover {
  border-color: #1890ff;
  color: #1890ff;
}

/* 拖放上传区域样式 */
.drag-drop-zone {
  border: 2px dashed #d9d9d9;
  border-radius: 8px;
  padding: 40px 20px;
  text-align: center;
  transition: all 0.3s ease;
  margin-bottom: 24px;
  position: relative;
}

.drag-drop-zone:hover {
  border-color: #1890ff;
  background-color: rgba(24, 144, 255, 0.05);
}

.drag-drop-zone.dragging {
  border-color: #1890ff;
  background-color: rgba(24, 144, 255, 0.1);
}

.drag-drop-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
}

.drag-drop-icon {
  font-size: 48px;
  color: #999;
  transition: color 0.3s ease;
}

.drag-drop-zone:hover .drag-drop-icon,
.drag-drop-zone.dragging .drag-drop-icon {
  color: #1890ff;
}

.drag-drop-content h5 {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin: 0;
}

.drag-drop-content p {
  font-size: 14px;
  color: #666;
  margin: 0;
}

.browse-label {
  color: #1890ff;
  font-weight: 500;
  cursor: pointer;
  text-decoration: underline;
  transition: color 0.3s ease;
}

.browse-label:hover {
  color: #40a9ff;
}

.drag-drop-hint {
  font-size: 12px;
  color: #999;
  margin-top: 8px;
}

/* CSDN风格分类管理 */
.csdn-category-management {
  width: 100%;
}

.csdn-category-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-top: 16px;
}

.csdn-category-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  background: white;
  border-radius: 6px;
  border: 1px solid #e0e0e0;
  transition: all 0.3s ease;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
}

.csdn-category-item:hover {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
  border-color: #1890ff;
}

.csdn-category-item span {
  font-size: 14px;
  font-weight: 500;
  color: #333;
}

.csdn-category-actions {
  display: flex;
  gap: 8px;
}

.csdn-category-editor {
  width: 100%;
}

/* CSDN风格标签管理 */
.csdn-tag-management {
  width: 100%;
}

.csdn-tag-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-top: 16px;
}

.csdn-tag-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  background: white;
  border-radius: 6px;
  border: 1px solid #e0e0e0;
  transition: all 0.3s ease;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
}

.csdn-tag-item:hover {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
  border-color: #1890ff;
}

.csdn-tag-item span {
  font-size: 14px;
  font-weight: 500;
  color: #333;
  background: rgba(24, 144, 255, 0.1);
  padding: 2px 12px;
  border-radius: 16px;
}

.csdn-tag-actions {
  display: flex;
  gap: 8px;
}

.csdn-tag-editor {
  width: 100%;
}

/* 主内容区 */
.main-content {
  flex: 7;
}

/* 文章卡片主题适配 */
.portal-view.dark .article-card {
  background: rgba(24, 24, 24, 0.9);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}

.portal-view.black .article-card {
  background: rgba(17, 17, 17, 0.9);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
}

.portal-view.dark .article-card:hover {
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.3);
}

.portal-view.black .article-card:hover {
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.4);
}

.portal-view.dark .article-title {
  color: #e9ecef;
}

.portal-view.dark .article-card:hover .article-title {
  color: #6b8cce;
  text-shadow: 0 1px 2px rgba(107, 140, 206, 0.2);
}

.portal-view.dark .article-meta {
  color: #999999;
  border-bottom: 1px solid #333333;
}

.portal-view.dark .article-excerpt {
  color: #ced4da;
}

.portal-view.black .article-title {
  color: #e0e0e0;
}

.portal-view.black .article-card:hover .article-title {
  color: #6b8cce;
}

.portal-view.black .article-meta {
  color: #888888;
  border-bottom: 1px solid #222222;
}

.portal-view.black .article-excerpt {
  color: #bdbdbd;
}

.portal-view.dark .article-footer {
  border-top: 1px solid #333333;
  color: #999999;
}

.portal-view.dark .footer-item {
  background-color: rgba(255, 255, 255, 0.05);
}

.portal-view.dark .footer-item:hover {
  background-color: rgba(107, 140, 206, 0.1);
}

.portal-view.black .article-footer {
  border-top: 1px solid #222222;
  color: #888888;
}

.portal-view.black .footer-item {
  background-color: rgba(255, 255, 255, 0.03);
}

.portal-view.black .footer-item:hover {
  background-color: rgba(107, 140, 206, 0.1);
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

/* 进度条图标 */
.progress-icon {
  width: 48px;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: transform 0.3s ease;
}

.progress-icon:active {
  transform: scale(0.9);
}

.progress-circle {
  position: relative;
  width: 56px;
  height: 56px;
  border-radius: 50%;
  background: conic-gradient(
    #4a6fa5 calc(var(--progress) * 1%), 
    #6b8cce calc(var(--progress) * 1%), 
    rgba(255, 255, 255, 0.1) 0%
  );
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4px 12px rgba(74, 111, 165, 0.3);
  transition: all 0.5s ease;
  animation: pulse 2s infinite, rotate 10s linear infinite;
  cursor: pointer;
}

@keyframes rotate {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

.progress-circle:hover {
  transform: scale(1.1);
  box-shadow: 0 8px 24px rgba(74, 111, 165, 0.6);
}

@keyframes pulse {
  0% {
    box-shadow: 0 4px 12px rgba(74, 111, 165, 0.3);
  }
  50% {
    box-shadow: 0 8px 24px rgba(74, 111, 165, 0.6);
  }
  100% {
    box-shadow: 0 4px 12px rgba(74, 111, 165, 0.3);
  }
}

.progress-circle::before {
  content: '';
  position: absolute;
  width: 44px;
  height: 44px;
  border-radius: 50%;
  background: white;
  z-index: 1;
}

.portal-view.dark .progress-circle::before {
  background: #181818;
}

.portal-view.black .progress-circle::before {
  background: #111111;
}

.progress-text {
  position: relative;
  z-index: 2;
  font-size: 14px;
  font-weight: bold;
  color: #4a6fa5;
  transition: all 0.3s ease;
  animation: textPulse 1s infinite;
}

.progress-text:hover {
  transform: scale(1.1);
  color: #3a5a85;
}

.portal-view.dark .progress-text,
.portal-view.black .progress-text {
  color: #6b8cce;
}

.portal-view.dark .progress-text:hover,
.portal-view.black .progress-text:hover {
  color: #4a6fa5;
}

@keyframes textPulse {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.05);
  }
  100% {
    transform: scale(1);
  }
}

.icon-btn {
  width: 48px;
  height: 48px;
  border: none;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 50%;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  cursor: pointer;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  display: flex;
  align-items: center;
  justify-content: center;
  backdrop-filter: blur(10px);
  transform: scale(1);
}

.icon-btn:hover {
  background: rgba(255, 255, 255, 1);
  transform: translateY(-6px) scale(1.1);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.3);
}

.icon-btn:active {
  transform: translateY(0) scale(0.9);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
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

/* 删除确认模态框样式 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 10000;
}

.modal-content {
  background-color: white;
  border-radius: 8px;
  padding: 24px;
  width: 90%;
  max-width: 400px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  animation: modalFadeIn 0.3s ease-out;
}

@keyframes modalFadeIn {
  from {
    opacity: 0;
    transform: scale(0.9);
  }
  to {
    opacity: 1;
    transform: scale(1);
  }
}

.modal-title {
  margin-top: 0;
  margin-bottom: 16px;
  font-size: 18px;
  font-weight: 600;
  color: #333;
  text-align: center;
}

.modal-message {
  margin-bottom: 24px;
  color: #666;
  text-align: center;
}

.modal-actions {
  display: flex;
  justify-content: center;
  gap: 16px;
}

.action-btn.danger {
  background-color: #f44336;
  color: white;
  border: none;
}

.action-btn.danger:hover {
  background-color: #da190b;
  transform: translateY(-2px);
}

/* 响应式设计 - 模态框 */
@media (max-width: 768px) {
  .modal-content {
    width: 95%;
    padding: 20px;
  }
  
  .modal-actions {
    flex-direction: column;
  }
  
  .modal-actions button {
    width: 100%;
  }
}
</style>