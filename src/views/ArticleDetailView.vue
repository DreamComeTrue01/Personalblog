<template>
  <div class="article-detail">
    <!-- Toast消息组件 -->
    <div v-if="showMessage.value" :class="['toast', messageType.value]">
      {{ message.value }}
    </div>
    
    <!-- 页面标题 -->
    <div class="page-header">
      <h1 class="page-title">{{ article.title }}</h1>
    </div>
    
    <div class="article-container">
      <!-- 左侧内容 -->
      <div class="main-content">
        <!-- 文章头部信息 -->
        <div class="article-header">
          <div class="article-meta">
            <span class="article-date">{{ formatDate(article.date) }}</span>
            <span class="article-categories">
              {{ article.categories.join(', ') }}
            </span>
          </div>
          <div class="article-stats">
            <span class="stat-item">❤️ {{ article.stats.likes }}</span>
            <span class="stat-item">💬 {{ article.stats.comments }}</span>
            <span class="stat-item">👁️ {{ article.stats.views || 0 }}</span>
          </div>
        </div>
        
        <!-- 作者信息卡片 -->
        <div class="author-card">
          <div class="author-avatar">
            <img src="@/assets/images/avatar.png" alt="作者头像" class="avatar-img">
          </div>
          <div class="author-info">
            <h3 class="author-name">Dream</h3>
            <p class="author-bio">记录生活，分享知识</p>
            <div class="author-social">
              <a href="#" class="social-link" title="GitHub">🐱</a>
              <a href="#" class="social-link" title="Twitter">🐦</a>
              <a href="#" class="social-link" title="Email">📧</a>
            </div>
          </div>
        </div>
        
        <!-- 文章内容 -->
        <div class="article-content" v-html="articleContent"></div>
        
        <!-- 文章标签 -->
        <div class="article-tags">
          <h3 class="tags-title">文章标签</h3>
          <div class="tags-list">
            <span v-for="(category, index) in article.categories" :key="index" class="tag-item">
              {{ category }}
            </span>
            <span v-if="article.categories.length === 0" class="tag-item empty-tag">
              暂无标签
            </span>
          </div>
        </div>
        
        <!-- 文章底部 -->
        <div class="article-footer">
          <div class="footer-share">
            <span class="share-title">分享本文：</span>
            <div class="share-buttons">
              <button class="share-btn" @click="shareArticle('wechat')" title="微信分享">📱</button>
              <button class="share-btn" @click="shareArticle('weibo')" title="微博分享">💬</button>
              <button class="share-btn" @click="shareArticle('email')" title="邮件分享">📧</button>
            </div>
          </div>
          <div class="footer-actions">
            <button :class="['action-btn', 'like-btn', { 'liked': isLiked }]" @click="likeArticle">{{ isLiked ? '💖 已点赞' : '❤️ 点赞' }} ({{ article.stats.likes }})</button>
            <button class="action-btn comment-btn" @click="toggleComment">💬 评论 ({{ article.stats.comments }})</button>
          </div>
        </div>
        
        <!-- 评论区域 -->
        <div class="comment-section" v-if="showComment">
          <h3 class="comment-title">评论区 ({{ article.stats.comments }})</h3>
          
          <!-- 评论表单 -->
          <div class="comment-form">
            <div class="form-group">
              <label for="comment-name">昵称</label>
              <input type="text" id="comment-name" v-model="commentForm.name" placeholder="请输入您的昵称" class="form-input">
            </div>
            <div class="form-group">
              <label for="comment-content">评论内容</label>
              <textarea id="comment-content" v-model="commentForm.content" placeholder="请输入您的评论" class="form-textarea" rows="4"></textarea>
            </div>
            <button class="form-submit" @click="submitComment">发表评论</button>
          </div>
          
          <!-- 评论列表 -->
          <div class="comment-list">
            <div v-for="(comment, index) in comments" :key="index" class="comment-item">
              <div class="comment-header">
                <span class="comment-author">{{ comment.name }}</span>
                <span class="comment-time">{{ formatDate(comment.time) }}</span>
              </div>
              <div class="comment-body">{{ comment.content }}</div>
            </div>
            <div v-if="comments.length === 0" class="no-comments">暂无评论，快来发表第一条评论吧！</div>
          </div>
        </div>
        
        <!-- 相关文章 -->
        <div class="related-articles">
          <h3 class="related-title">相关文章</h3>
          <div class="related-list">
            <div v-for="(related, index) in relatedArticles" :key="index" class="related-item" @click="router.push(`/article/${related.id}`)">
              <h4 class="related-item-title">{{ related.title }}</h4>
              <p class="related-item-date">{{ formatDate(related.date) }}</p>
            </div>
          </div>
        </div>
        
        <!-- 阅读进度条 -->
        <div class="reading-progress">
          <div class="progress-info">
            <span class="progress-label">阅读进度</span>
            <span class="progress-percentage">{{ Math.round(scrollProgress * 100) }}%</span>
          </div>
          <div class="progress-bar">
            <div class="progress-fill" :style="{ width: scrollProgress * 100 + '%' }"></div>
          </div>
        </div>
        
        <!-- 返回按钮 -->
        <div class="back-container">
          <button class="back-btn" @click="goBack">← 返回上一页</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()



// 监听路由参数变化，重新加载文章数据
watch(() => route.params.id, () => {
  loadArticle()
})
const article = ref({
  title: '加载中...',
  date: new Date().toISOString().split('T')[0],
  categories: [],
  stats: { likes: 0, comments: 0, views: 0 },
  content: '',
  cover: ''
})
const articleContent = ref('<p>正在加载文章...</p>')
const relatedArticles = ref([])

// 评论相关
const showComment = ref(false)
const commentForm = ref({
  name: '',
  content: ''
})
const comments = ref([])
const message = ref('')
const showMessage = ref(false)
const messageType = ref('success') // success, error
const isLiked = ref(false)

// 阅读进度
const scrollProgress = ref(0)

// 显示消息
const showToast = (text, type = 'success') => {
  message.value = text
  messageType.value = type
  showMessage.value = true
  setTimeout(() => {
    showMessage.value = false
  }, 2000)
}



// 加载文章数据
const loadArticle = () => {
  const articleId = parseInt(route.params.id)
  let savedArticles = localStorage.getItem('articles')
  
  // 初始化默认文章对象，确保所有属性都存在
  article.value = {
    title: '文章不存在',
    date: new Date().toISOString().split('T')[0],
    categories: [],
    stats: { likes: 0, comments: 0 },
    content: ''
  }
  articleContent.value = '<p>抱歉，您访问的文章不存在或已被删除。</p>'
  
  // 如果localStorage中没有文章数据，初始化默认文章
  if (!savedArticles) {
    const defaultArticles = [
      {
        id: 1,
        title: '2025年终总结',
        date: '2026-02-24',
        categories: ['生活'],
        excerpt: '2025 年终总结。诈尸一下，马上就是 2026 年了，哈哈哈。过得太快了吧。期间还想着水水文章的，结果完全没有了本科期间的热情，能写个年终总结就不错了。',
        stats: {
          likes: 9,
          comments: 12
        }
      },
      {
        id: 2,
        title: '实习小记',
        date: '2025-02-18',
        categories: ['生活', 'Learn'],
        excerpt: '月记。最近想趁着没啥课，导师管的松的时候出去找个实习，结果一个也没找到，2333。',
        stats: {
          likes: 5,
          comments: 8
        }
      },
      {
        id: 3,
        title: '2024年终总结',
        date: '2025-01-02',
        categories: ['生活'],
        excerpt: '光阴似骏马加鞭，浮世似落花流水。大学生活结束了～考完了，感觉这一年白费了，害，后面再说吧，下面就是准备没有工资还要交钱的实习了，以及毕设，顺便整理下博客吧。',
        stats: {
          likes: 12,
          comments: 15
        }
      },
      {
        id: 4,
        title: 'Argon主题博客美化',
        date: '2022-10-21',
        categories: ['建站', 'Learn'],
        excerpt: '关于 Argon 主题的相关美化文档。新增 WP 用户头像插件。',
        stats: {
          likes: 18,
          comments: 23
        }
      }
    ]
    localStorage.setItem('articles', JSON.stringify(defaultArticles))
    savedArticles = JSON.stringify(defaultArticles)
  }
  
  if (savedArticles && !isNaN(articleId)) {
    const articles = JSON.parse(savedArticles)
    const foundArticle = articles.find(a => a.id === articleId)
    if (foundArticle) {
      // 确保 foundArticle 包含所有必要的属性
      let content = foundArticle.content !== undefined && foundArticle.content !== null ? foundArticle.content : ''
      // 如果 content 为空或简单，使用 excerpt 作为内容
      if (!content || content.length < 50) {
        content = foundArticle.excerpt !== undefined && foundArticle.excerpt !== null ? `# ${foundArticle.title}\n\n${foundArticle.excerpt}` : '# 文章内容\n\n这是文章的默认内容...'
      }
      article.value = {
        id: foundArticle.id,
        title: foundArticle.title || '无标题',
        date: foundArticle.date || new Date().toISOString().split('T')[0],
        categories: foundArticle.categories || [],
        stats: foundArticle.stats || { likes: 0, comments: 0, views: 0 },
        content: content,
        cover: foundArticle.cover || ''
      }
      // 简单的 Markdown 转 HTML
      articleContent.value = convertMarkdownToHtml(content)
      
      // 加载相关文章（排除当前文章，按日期排序取前3篇）
      relatedArticles.value = articles
        .filter(a => a.id && a.id !== articleId)
        .sort((a, b) => new Date(b.date) - new Date(a.date))
        .slice(0, 3)
      
      // 检查文章是否已经被点赞
      const likedArticles = JSON.parse(localStorage.getItem('likedArticles') || '[]');
      isLiked.value = likedArticles.includes(articleId);
      
      // 增加阅读次数
      if (article.value.stats) {
        if (!article.value.stats.views) {
          article.value.stats.views = 0;
        }
        article.value.stats.views++;
        
        // 更新 localStorage 中的数据
        const savedArticles = localStorage.getItem('articles');
        if (savedArticles) {
          const articles = JSON.parse(savedArticles);
          const index = articles.findIndex(a => a.id === articleId);
          if (index !== -1) {
            articles[index].stats.views = article.value.stats.views;
            localStorage.setItem('articles', JSON.stringify(articles));
            // 触发 storage 事件，通知其他组件更新
            window.dispatchEvent(new Event('storage'));
          }
        }
      }
      
      // 调试信息
      console.log('Related articles:', relatedArticles.value)
      console.log('All articles:', articles)
      console.log('Current article ID:', articleId)
      console.log('Is liked:', isLiked.value)
    }
  }
}

// 格式化日期
const formatDate = (dateString) => {
  const date = new Date(dateString)
  const month = date.getMonth() + 1
  const day = date.getDate()
  const year = date.getFullYear()
  return `${month}月${day}日 , ${year}`
}

// 简单的 Markdown 转 HTML
const convertMarkdownToHtml = (markdown) => {
  if (markdown === undefined || markdown === null) return '<p>无内容</p>'
  
  // 图片（支持大小调整语法：![alt](url =widthxheight) 或 ![alt](url width=300 height=200)）
  markdown = markdown.replace(/!\[(.*?)\]\((.*?)(?:\s*=\s*(\d+)x(\d+))?\)/gim, (match, alt, url, width, height) => {
    // 确保url被正确处理，特别是base64编码
    const safeUrl = url.trim();
    if (width && height) {
      return `<img src="${safeUrl}" alt="${alt}" width="${width}" height="${height}" class="markdown-image">`;
    } else {
      return `<img src="${safeUrl}" alt="${alt}" class="markdown-image">`;
    }
  })
  // 处理另一种格式：![alt](url width=300 height=200)
  markdown = markdown.replace(/!\[(.*?)\]\((.*?)\s+width=(\d+)\s+height=(\d+)\)/gim, (match, alt, url, width, height) => {
    const safeUrl = url.trim();
    return `<img src="${safeUrl}" alt="${alt}" width="${width}" height="${height}" class="markdown-image">`;
  })
  
  // 标题
  markdown = markdown.replace(/^# (.*$)/gim, '<h1>$1</h1>')
  markdown = markdown.replace(/^## (.*$)/gim, '<h2>$1</h2>')
  markdown = markdown.replace(/^### (.*$)/gim, '<h3>$1</h3>')
  
  // 段落 - 处理空行分隔的段落
  const paragraphs = markdown.split(/\n\s*\n/)
  let html = ''
  
  paragraphs.forEach(paragraph => {
    paragraph = paragraph.trim()
    if (paragraph) {
      // 检查是否已经是标题或图片
      if (!paragraph.startsWith('<h1>') && !paragraph.startsWith('<h2>') && !paragraph.startsWith('<h3>') && !paragraph.startsWith('<img')) {
        html += `<p>${paragraph}</p>`
      } else {
        html += paragraph
      }
    }
  })
  
  // 处理换行
  html = html.replace(/\n/gim, '<br>')
  
  // 如果没有生成任何内容，返回无内容
  return html || '<p>无内容</p>'
}

// 返回上一页
const goBack = () => {
  router.back()
}

// 分享文章
const shareArticle = (platform) => {
  switch (platform) {
    case 'wechat':
      alert('微信分享功能开发中...');
      break;
    case 'weibo':
      alert('微博分享功能开发中...');
      break;
    case 'email':
      const email = `mailto:?subject=${encodeURIComponent(article.value.title)}&body=${encodeURIComponent(window.location.href)}`;
      window.location.href = email;
      break;
    default:
      break;
  }
}

// 点赞文章
const likeArticle = () => {
  // 检查是否已经点赞
  const likedArticles = JSON.parse(localStorage.getItem('likedArticles') || '[]');
  const articleId = parseInt(route.params.id);
  
  if (likedArticles.includes(articleId)) {
    // 取消点赞
    article.value.stats.likes = Math.max(0, article.value.stats.likes - 1);
    
    // 更新 localStorage 中的数据
    const savedArticles = localStorage.getItem('articles');
    if (savedArticles) {
      const articles = JSON.parse(savedArticles);
      const index = articles.findIndex(a => a.id === articleId);
      if (index !== -1) {
        articles[index].stats.likes = article.value.stats.likes;
        localStorage.setItem('articles', JSON.stringify(articles));
        // 触发 storage 事件，通知其他组件更新
        window.dispatchEvent(new Event('storage'));
      }
    }
    
    // 移除点赞状态
    const updatedLikedArticles = likedArticles.filter(id => id !== articleId);
    localStorage.setItem('likedArticles', JSON.stringify(updatedLikedArticles));
    
    // 切换点赞状态
    isLiked.value = false;
    
    showToast('取消点赞成功！');
  } else {
    // 增加点赞数
    article.value.stats.likes++;
    
    // 更新 localStorage 中的数据
    const savedArticles = localStorage.getItem('articles');
    if (savedArticles) {
      const articles = JSON.parse(savedArticles);
      const index = articles.findIndex(a => a.id === articleId);
      if (index !== -1) {
        articles[index].stats.likes = article.value.stats.likes;
        localStorage.setItem('articles', JSON.stringify(articles));
        // 触发 storage 事件，通知其他组件更新
        window.dispatchEvent(new Event('storage'));
      }
    }
    
    // 记录点赞状态
    likedArticles.push(articleId);
    localStorage.setItem('likedArticles', JSON.stringify(likedArticles));
    
    // 切换点赞状态
    isLiked.value = true;
    
    showToast('点赞成功！');
  }
}

// 切换评论区域
const toggleComment = () => {
  showComment.value = !showComment.value;
  if (showComment.value) {
    loadComments();
  }
}

// 加载评论
const loadComments = () => {
  const articleId = parseInt(route.params.id);
  const savedComments = localStorage.getItem(`comments_${articleId}`);
  if (savedComments) {
    comments.value = JSON.parse(savedComments);
  } else {
    comments.value = [];
  }
  
  // 更新评论计数，确保与实际评论数一致
  const actualCommentCount = comments.value.length;
  if (article.value.stats.comments !== actualCommentCount) {
    article.value.stats.comments = actualCommentCount;
    
    // 更新 localStorage 中的文章数据
    const savedArticles = localStorage.getItem('articles');
    if (savedArticles) {
      const articles = JSON.parse(savedArticles);
      const index = articles.findIndex(a => a.id === articleId);
      if (index !== -1) {
        articles[index].stats.comments = actualCommentCount;
        localStorage.setItem('articles', JSON.stringify(articles));
        // 触发 storage 事件，通知其他组件更新
        window.dispatchEvent(new Event('storage'));
      }
    }
  }
}

// 提交评论
const submitComment = () => {
  if (!commentForm.value.name.trim()) {
    showToast('请输入昵称', 'error');
    return;
  }
  if (!commentForm.value.content.trim()) {
    showToast('请输入评论内容', 'error');
    return;
  }
  
  const articleId = parseInt(route.params.id);
  const newComment = {
    id: Date.now(),
    name: commentForm.value.name.trim(),
    content: commentForm.value.content.trim(),
    time: new Date().toISOString().split('T')[0]
  };
  
  // 添加评论
  comments.value.push(newComment);
  
  // 保存评论到 localStorage
  localStorage.setItem(`comments_${articleId}`, JSON.stringify(comments.value));
  
  // 更新评论数
  article.value.stats.comments++;
  
  // 更新 localStorage 中的文章数据
  const savedArticles = localStorage.getItem('articles');
  if (savedArticles) {
    const articles = JSON.parse(savedArticles);
    const index = articles.findIndex(a => a.id === articleId);
    if (index !== -1) {
      articles[index].stats.comments = article.value.stats.comments;
      localStorage.setItem('articles', JSON.stringify(articles));
      // 触发 storage 事件，通知其他组件更新
      window.dispatchEvent(new Event('storage'));
    }
  }
  
  // 清空表单
  commentForm.value.name = '';
  commentForm.value.content = '';
  
  showToast('评论发表成功！');
}



onMounted(() => {
  loadArticle()
  
  // 加载评论并更新评论计数
  loadComments()
  
  // 监听滚动事件，更新阅读进度
  const updateScrollProgress = () => {
    const scrollTop = window.scrollY
    const docHeight = document.documentElement.scrollHeight - window.innerHeight
    scrollProgress.value = docHeight > 0 ? scrollTop / docHeight : 0
  }
  
  window.addEventListener('scroll', updateScrollProgress)
  
  // 初始计算一次
  updateScrollProgress()
  
  // 组件卸载时移除事件监听
  onUnmounted(() => {
    window.removeEventListener('scroll', updateScrollProgress)
  })
})
</script>

<style scoped>
.article-detail {
  width: 100%;
  min-height: 100vh;
  background: url('@/assets/images/backgrounds/light-bg.jpg');
  background-size: cover;
  background-attachment: fixed;
  background-position: center;
}

/* 页面标题 */
.page-header {
  padding: 60px 20px 30px;
  text-align: center;
  background: rgba(0, 0, 0, 0.3);
  backdrop-filter: blur(10px);
  margin-bottom: 30px;
  border-radius: 0 0 20px 20px;
}

.page-title {
  font-size: 42px;
  font-weight: bold;
  color: white;
  text-shadow: 0 3px 6px rgba(0, 0, 0, 0.6);
  margin: 0;
  line-height: 1.2;
  letter-spacing: 1px;
}

.app-container.dark .page-header {
  background: rgba(0, 0, 0, 0.6);
}

.app-container.black .page-header {
  background: rgba(0, 0, 0, 0.8);
}

.app-container.dark .article-detail {
  background: url('@/assets/images/backgrounds/dark-bg.jpg');
  background-size: cover;
  background-attachment: fixed;
  background-position: center;
}

.app-container.black .article-detail {
  background: url('@/assets/images/backgrounds/black-bg.jpg');
  background-size: cover;
  background-attachment: fixed;
  background-position: center;
}



.article-container {
  max-width: 1000px;
  margin: 0 auto;
  margin-top: -70px;
  position: relative;
  z-index: 10;
}

.main-content {
  background: white;
  border-radius: 16px;
  padding: 50px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
  border: 1px solid #f0f0f0;
}



/* 文章头部信息 */
.article-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 2px solid #f0f0f0;
  flex-wrap: wrap;
  gap: 20px;
}

/* 作者信息卡片 */
.author-card {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 24px;
  background: #fafafa;
  border-radius: 16px;
  margin-bottom: 30px;
  border: 1px solid #f0f0f0;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
}

.author-card:hover {
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
  transform: translateY(-2px);
  border-color: #4a6fa5;
}

.author-avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  overflow: hidden;
  border: 3px solid #4a6fa5;
  flex-shrink: 0;
}

.avatar-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.author-info {
  flex: 1;
}

.author-name {
  font-size: 20px;
  font-weight: bold;
  color: #333;
  margin: 0 0 8px 0;
}

.author-bio {
  font-size: 16px;
  color: #666;
  margin: 0 0 12px 0;
  line-height: 1.4;
}

.author-social {
  display: flex;
  gap: 12px;
}

.social-link {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: white;
  border: 2px solid #e0e0e0;
  font-size: 18px;
  text-decoration: none;
  transition: all 0.3s ease;
  color: #333;
}

.social-link:hover {
  background: #4a6fa5;
  border-color: #4a6fa5;
  color: white;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(74, 111, 165, 0.3);
}

.article-meta {
  font-size: 15px;
  color: #666;
  display: flex;
  gap: 24px;
  flex-wrap: wrap;
  font-weight: 500;
}

.article-stats {
  display: flex;
  gap: 16px;
  font-size: 15px;
  color: #666;
  align-items: center;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 8px;
  transition: all 0.3s ease;
  padding: 8px 16px;
  border-radius: 20px;
  background: #f8f9fa;
  min-width: 90px;
  justify-content: center;
  text-align: center;
  border: 1px solid #e0e0e0;
}

.stat-item:hover {
  color: #4a6fa5;
  background: #e3f2fd;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(74, 111, 165, 0.2);
  border-color: #4a6fa5;
}

.article-content {
  font-size: 17px;
  color: #333;
  line-height: 2;
  margin-bottom: 40px;
}

.article-content h1 {
  font-size: 28px;
  font-weight: bold;
  margin: 40px 0 24px 0;
  color: #222;
  padding-bottom: 12px;
  border-bottom: 2px solid #f0f0f0;
}

.article-content h2 {
  font-size: 24px;
  font-weight: bold;
  margin: 32px 0 20px 0;
  color: #333;
  padding-bottom: 8px;
  border-bottom: 1px solid #f0f0f0;
}

.article-content h3 {
  font-size: 20px;
  font-weight: bold;
  margin: 24px 0 16px 0;
  color: #444;
}

.article-content p {
  margin-bottom: 24px;
  text-align: justify;
  font-weight: 400;
}

.article-content img,
.article-content .markdown-image {
  max-width: 100%;
  height: auto;
  border-radius: 8px;
  margin: 20px 0;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

/* 文章标签 */
.article-tags {
  margin-bottom: 30px;
  padding: 24px;
  background: #fafafa;
  border-radius: 16px;
  border: 1px solid #f0f0f0;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.tags-title {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  margin: 0 0 16px 0;
  display: flex;
  align-items: center;
  gap: 8px;
}

.tags-title::before {
  content: "🏷️";
  font-size: 16px;
}

.tags-list {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.tag-item {
  display: inline-block;
  padding: 8px 16px;
  background: white;
  border: 2px solid #4a6fa5;
  border-radius: 20px;
  font-size: 14px;
  font-weight: 500;
  color: #4a6fa5;
  transition: all 0.3s ease;
  cursor: pointer;
}

.tag-item:hover {
  background: #4a6fa5;
  color: white;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(74, 111, 165, 0.3);
}

.tag-item.empty-tag {
  border-color: #e0e0e0;
  color: #999;
}

.tag-item.empty-tag:hover {
  background: #e0e0e0;
  color: #666;
  border-color: #e0e0e0;
}

/* 文章底部 */
.article-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 30px 0;
  border-top: 2px solid #f0f0f0;
  border-bottom: 2px solid #f0f0f0;
  margin-bottom: 40px;
  flex-wrap: wrap;
  gap: 20px;
  background: #fafafa;
  border-radius: 12px;
  padding: 30px;
}

.footer-share {
  display: flex;
  align-items: center;
  gap: 16px;
}

.share-title {
  font-size: 16px;
  color: #333;
  font-weight: 500;
}

.share-buttons {
  display: flex;
  gap: 12px;
}

.share-btn {
  width: 40px;
  height: 40px;
  border: 2px solid #e0e0e0;
  border-radius: 50%;
  background: white;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
  font-size: 18px;
}

.share-btn:hover {
  background: #f8f9fa;
  transform: translateY(-3px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  border-color: #4a6fa5;
}

.share-btn:active {
  animation: sharePulse 0.6s ease-in-out;
}

@keyframes sharePulse {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.2);
  }
  100% {
    transform: scale(1);
  }
}

.footer-actions {
  display: flex;
  gap: 16px;
}

.action-btn {
  padding: 12px 24px;
  border: 2px solid #e0e0e0;
  border-radius: 25px;
  background: white;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 16px;
  display: flex;
  align-items: center;
  gap: 8px;
  position: relative;
  overflow: hidden;
  font-weight: 500;
}

.like-btn:hover {
  background: #ffebee;
  border-color: #ffcdd2;
  color: #c62828;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(255, 193, 203, 0.3);
}

.like-btn.liked {
  background: #ffebee;
  border-color: #ffcdd2;
  color: #c62828;
  box-shadow: 0 4px 12px rgba(255, 193, 203, 0.3);
}

.like-btn:active {
  animation: likePulse 0.6s ease-in-out;
}

@keyframes likePulse {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.1);
  }
  100% {
    transform: scale(1);
  }
}

.like-btn.unlike {
  animation: unlikePulse 0.6s ease-in-out;
}

@keyframes unlikePulse {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(0.9);
  }
  100% {
    transform: scale(1);
  }
}

.comment-btn:hover {
  background: #e3f2fd;
  border-color: #bbdefb;
  color: #1565c0;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(187, 222, 251, 0.3);
}

.comment-btn:active {
  animation: commentPulse 0.6s ease-in-out;
}

@keyframes commentPulse {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.1);
  }
  100% {
    transform: scale(1);
  }
}

/* Toast消息组件 */
.toast {
  position: fixed;
  top: 20px;
  right: 20px;
  padding: 16px 24px;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 500;
  color: white;
  z-index: 99999;
  animation: slideIn 0.3s ease-out;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  font-family: 'PingFang SC', 'Helvetica Neue', Arial, sans-serif;
  background: linear-gradient(135deg, #4a6fa5, #3a5a85);
  min-width: 200px;
  text-align: center;
}

@keyframes slideIn {
  from {
    transform: translateX(100%);
    opacity: 0;
  }
  to {
    transform: translateX(0);
    opacity: 1;
  }
}

.toast.success {
  background: linear-gradient(135deg, #4caf50, #45a049);
}

.toast.error {
  background: linear-gradient(135deg, #f44336, #da190b);
}

@keyframes slideIn {
  from {
    transform: translateX(100%);
    opacity: 0;
  }
  to {
    transform: translateX(0);
    opacity: 1;
  }
}

/* 相关文章 */
.related-articles {
  margin-bottom: 40px;
  background: #fafafa;
  border-radius: 16px;
  padding: 30px;
  border: 1px solid #f0f0f0;
}

.related-title {
  font-size: 22px;
  font-weight: bold;
  margin-bottom: 24px;
  color: #333;
  padding-bottom: 12px;
  border-bottom: 2px solid #4a6fa5;
  display: flex;
  align-items: center;
  gap: 10px;
}

.related-title::before {
  content: "📚";
  font-size: 20px;
}

.related-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.related-item {
  padding: 20px;
  border: 2px solid #e0e0e0;
  border-radius: 12px;
  transition: all 0.3s ease;
  cursor: pointer;
  background: white;
  z-index: 1;
  user-select: none;
  position: relative;
  overflow: hidden;
}

.related-item::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 4px;
  height: 100%;
  background: linear-gradient(180deg, #4a6fa5, #3a5a85);
  opacity: 0;
  transition: opacity 0.3s ease;
}

.related-item:hover {
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
  transform: translateY(-3px);
  border-color: #4a6fa5;
}

.related-item:hover::before {
  opacity: 1;
}

.related-item:hover .related-item-title {
  color: #4a6fa5;
}

.related-item-title {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 10px;
  color: #333;
  line-height: 1.4;
  transition: all 0.3s ease;
}

.related-item-date {
  font-size: 14px;
  color: #666;
  font-weight: 500;
  display: flex;
  align-items: center;
  gap: 6px;
}

.related-item-date::before {
  content: "📅";
  font-size: 12px;
}

/* 返回按钮 */
.back-container {
  display: flex;
  justify-content: center;
  margin-top: 40px;
  padding: 20px 0;
}

.back-btn {
  padding: 16px 40px;
  background: linear-gradient(135deg, #4a6fa5, #3a5a85);
  color: white;
  border: none;
  border-radius: 30px;
  font-size: 16px;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 10px;
  position: relative;
  overflow: hidden;
  font-weight: 500;
  box-shadow: 0 4px 12px rgba(74, 111, 165, 0.3);
}

.back-btn:hover {
  background: linear-gradient(135deg, #3a5a85, #2a4a75);
  transform: translateY(-3px);
  box-shadow: 0 8px 24px rgba(74, 111, 165, 0.4);
}

.back-btn:active {
  animation: backPulse 0.6s ease-in-out;
}

@keyframes backPulse {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(0.95);
  }
  100% {
    transform: scale(1);
  }
}

/* 阅读进度条 */
.reading-progress {
  margin-bottom: 30px;
  padding: 24px;
  background: #fafafa;
  border-radius: 16px;
  border: 1px solid #f0f0f0;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.progress-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.progress-label {
  font-size: 16px;
  font-weight: 500;
  color: #333;
  display: flex;
  align-items: center;
  gap: 8px;
}

.progress-label::before {
  content: "📖";
  font-size: 14px;
}

.progress-percentage {
  font-size: 16px;
  font-weight: 600;
  color: #4a6fa5;
}

.progress-bar {
  width: 100%;
  height: 8px;
  background: #e0e0e0;
  border-radius: 4px;
  overflow: hidden;
  position: relative;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, #4a6fa5, #3a5a85);
  border-radius: 4px;
  transition: width 0.3s ease;
  box-shadow: 0 0 10px rgba(74, 111, 165, 0.5);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .page-header {
    padding: 40px 20px 20px;
  }
  
  .page-title {
    font-size: 32px;
  }
  
  .article-container {
    padding: 10px;
    margin-top: -50px;
  }
  
  .main-content {
    padding: 30px;
  }
  
  .article-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
  
  .article-footer {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
    padding: 20px;
  }
  
  .footer-share {
    width: 100%;
    justify-content: space-between;
  }
  
  .related-articles {
    padding: 20px;
  }
  
  .related-item {
    padding: 16px;
  }
  
  .related-item-title {
    font-size: 16px;
  }
  
  .back-btn {
    width: 100%;
    justify-content: center;
  }
}

@media (max-width: 480px) {
  .page-header {
    padding: 30px 16px 16px;
  }
  
  .page-title {
    font-size: 28px;
  }
  
  .main-content {
    padding: 20px;
  }
  
  .article-content {
    font-size: 15px;
    line-height: 1.8;
  }
  
  .article-footer {
    padding: 16px;
  }
  
  .action-btn {
    padding: 10px 20px;
    font-size: 14px;
  }
  
  .share-btn {
    width: 36px;
    height: 36px;
  }
  
  .related-articles {
    padding: 16px;
  }
  
  .related-item {
    padding: 12px;
  }
  
  .back-btn {
    padding: 14px 32px;
  }
}

/* 评论区域 */
.comment-section {
  margin-top: 40px;
  padding: 32px;
  background: #f8f9fa;
  border-radius: 16px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
  border: 1px solid #f0f0f0;
}

.comment-title {
  font-size: 22px;
  font-weight: bold;
  margin-bottom: 24px;
  color: #333;
  padding-bottom: 12px;
  border-bottom: 2px solid #4a6fa5;
  display: flex;
  align-items: center;
  gap: 10px;
}

.comment-title::before {
  content: "💬";
  font-size: 20px;
}

.comment-form {
  margin-bottom: 32px;
  padding: 24px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  border: 2px solid #e0e0e0;
  transition: all 0.3s ease;
}

.comment-form:hover {
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.12);
  border-color: #4a6fa5;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 10px;
  font-size: 16px;
  font-weight: 500;
  color: #333;
  font-family: 'PingFang SC', 'Helvetica Neue', Arial, sans-serif;
}

.form-input {
  width: 100%;
  padding: 14px 20px;
  border: 2px solid #e0e0e0;
  border-radius: 8px;
  font-size: 16px;
  font-family: 'PingFang SC', 'Helvetica Neue', Arial, sans-serif;
  transition: all 0.3s ease;
  background: #fafafa;
}

.form-input:focus {
  outline: none;
  border-color: #4a6fa5;
  box-shadow: 0 0 0 3px rgba(74, 111, 165, 0.2);
  background: white;
}

.form-textarea {
  width: 100%;
  padding: 18px 20px;
  border: 2px solid #e0e0e0;
  border-radius: 8px;
  font-size: 16px;
  font-family: 'PingFang SC', 'Helvetica Neue', Arial, sans-serif;
  resize: vertical;
  transition: all 0.3s ease;
  min-height: 150px;
  line-height: 1.7;
  background: #fafafa;
}

.form-textarea:focus {
  outline: none;
  border-color: #4a6fa5;
  box-shadow: 0 0 0 3px rgba(74, 111, 165, 0.2);
  background: white;
}

.form-submit {
  padding: 14px 36px;
  background: linear-gradient(135deg, #4a6fa5, #3a5a85);
  color: white;
  border: none;
  border-radius: 25px;
  font-size: 16px;
  font-weight: 600;
  font-family: 'PingFang SC', 'Helvetica Neue', Arial, sans-serif;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(74, 111, 165, 0.3);
}

.form-submit:hover {
  background: linear-gradient(135deg, #3a5a85, #2a4a75);
  transform: translateY(-3px);
  box-shadow: 0 6px 20px rgba(74, 111, 165, 0.4);
}

.form-submit:active {
  transform: translateY(0);
}

.comment-list {
  margin-top: 32px;
}

.comment-item {
  padding: 24px;
  margin-bottom: 20px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  border: 2px solid #e0e0e0;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.comment-item::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 4px;
  height: 100%;
  background: linear-gradient(180deg, #4a6fa5, #3a5a85);
}

.comment-item:hover {
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
  border-color: #4a6fa5;
  transform: translateY(-3px);
}

.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  padding-left: 16px;
}

.comment-author {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  font-family: 'PingFang SC', 'Helvetica Neue', Arial, sans-serif;
  display: flex;
  align-items: center;
  gap: 10px;
}

.comment-author::before {
  content: "👤";
  font-size: 18px;
}

.comment-time {
  font-size: 14px;
  color: #666;
  font-family: 'PingFang SC', 'Helvetica Neue', Arial, sans-serif;
  font-weight: 500;
}

.comment-body {
  font-size: 16px;
  color: #555;
  line-height: 1.8;
  word-break: break-word;
  padding-left: 16px;
  font-family: 'PingFang SC', 'Helvetica Neue', Arial, sans-serif;
}

.no-comments {
  text-align: center;
  padding: 80px 0;
  color: #666;
  font-size: 16px;
  background: white;
  border-radius: 12px;
  border: 2px dashed #e0e0e0;
  font-family: 'PingFang SC', 'Helvetica Neue', Arial, sans-serif;
  transition: all 0.3s ease;
}

.no-comments:hover {
  border-color: #4a6fa5;
  color: #333;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.no-comments::before {
  content: "💭";
  font-size: 40px;
  display: block;
  margin-bottom: 20px;
  opacity: 0.5;
}

/* 主题模式适配 */
:deep(.app-container.dark) .main-content {
  background-color: #181818;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
}

:deep(.app-container.dark) .main-content:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
}

:deep(.app-container.dark) .article-meta {
  color: #888888;
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

:deep(.app-container.black) .main-content {
  background-color: #111111;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.3);
}

:deep(.app-container.black) .main-content:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.4);
}

:deep(.app-container.black) .article-meta {
  color: #777777;
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
</style>