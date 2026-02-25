<template>
  <div class="article-detail">
    <!-- 文章封面图 -->
    <div class="article-cover" :style="coverStyle">
      <div class="cover-overlay">
        <h1 class="cover-title">{{ article.title }}</h1>
        <div class="cover-meta">
          <span class="cover-date">{{ formatDate(article.date) }}</span>
          <span class="cover-categories">
            {{ article.categories.join(', ') }}
          </span>
        </div>
      </div>
    </div>
    
    <div class="article-container">
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
      
      <!-- 文章内容 -->
      <div class="article-content" v-html="articleContent"></div>
      
      <!-- 文章底部 -->
      <div class="article-footer">
        <div class="footer-share">
          <span class="share-title">分享本文：</span>
          <div class="share-buttons">
            <button class="share-btn">📱</button>
            <button class="share-btn">💬</button>
            <button class="share-btn">📧</button>
          </div>
        </div>
        <div class="footer-actions">
          <button class="action-btn like-btn">❤️ 点赞</button>
          <button class="action-btn comment-btn">💬 评论</button>
        </div>
      </div>
      
      <!-- 相关文章 -->
      <div class="related-articles">
        <h3 class="related-title">相关文章</h3>
        <div class="related-list">
          <div v-for="(related, index) in relatedArticles" :key="index" class="related-item">
            <h4 class="related-item-title">{{ related.title }}</h4>
            <p class="related-item-date">{{ formatDate(related.date) }}</p>
          </div>
        </div>
      </div>
      
      <!-- 返回按钮 -->
      <div class="back-container">
        <button class="back-btn" @click="goBack">← 返回列表</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()
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

// 计算封面图样式
const coverStyle = computed(() => {
  const coverUrl = article.value.cover || 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=misty%20forest%20with%20sunlight%20streaming%20through%20trees%2C%20peaceful%20natural%20landscape&image_size=landscape_16_9'
  return {
    backgroundImage: `url('${coverUrl}')`
  }
})

// 加载文章数据
const loadArticle = () => {
  const articleId = parseInt(route.params.id)
  const savedArticles = localStorage.getItem('articles')
  
  // 初始化默认文章对象，确保所有属性都存在
  article.value = {
    title: '文章不存在',
    date: new Date().toISOString().split('T')[0],
    categories: [],
    stats: { likes: 0, comments: 0 },
    content: ''
  }
  articleContent.value = '<p>抱歉，您访问的文章不存在或已被删除。</p>'
  
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
        .filter(a => a.id !== articleId)
        .sort((a, b) => new Date(b.date) - new Date(a.date))
        .slice(0, 3)
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
      // 检查是否已经是标题
      if (!paragraph.startsWith('<h1>') && !paragraph.startsWith('<h2>') && !paragraph.startsWith('<h3>')) {
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

// 返回列表
const goBack = () => {
  router.push('/')
}

onMounted(() => {
  loadArticle()
})
</script>

<style scoped>
.article-detail {
  width: 100%;
  min-height: 100vh;
  background: #f8f9fa;
}

/* 文章封面图 */
.article-cover {
  width: 100%;
  height: 400px;
  background-size: cover;
  background-position: center;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}

.cover-overlay {
  background: rgba(0, 0, 0, 0.5);
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-align: center;
  padding: 0 20px;
}

.cover-title {
  font-size: 36px;
  font-weight: bold;
  color: white;
  margin-bottom: 16px;
  line-height: 1.3;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.5);
}

.cover-meta {
  display: flex;
  gap: 20px;
  color: rgba(255, 255, 255, 0.9);
  font-size: 14px;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.5);
}

.article-container {
  max-width: 800px;
  margin: 0 auto;
  background: white;
  border-radius: 8px;
  padding: 40px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  margin-top: -50px;
  position: relative;
  z-index: 10;
}

/* 文章头部信息 */
.article-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 32px;
  padding-bottom: 20px;
  border-bottom: 1px solid #eaeaea;
  flex-wrap: wrap;
  gap: 16px;
}

.article-meta {
  font-size: 14px;
  color: #909399;
  display: flex;
  gap: 20px;
  flex-wrap: wrap;
}

.article-stats {
  display: flex;
  gap: 20px;
  font-size: 14px;
  color: #909399;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 6px;
}

.article-content {
  font-size: 16px;
  color: #333;
  line-height: 1.8;
  margin-bottom: 40px;
}

.article-content h1 {
  font-size: 24px;
  font-weight: bold;
  margin: 32px 0 20px 0;
  color: #333;
}

.article-content h2 {
  font-size: 20px;
  font-weight: bold;
  margin: 24px 0 16px 0;
  color: #444;
}

.article-content h3 {
  font-size: 18px;
  font-weight: bold;
  margin: 20px 0 12px 0;
  color: #555;
}

.article-content p {
  margin-bottom: 20px;
  text-align: justify;
}

.article-content img {
  max-width: 100%;
  height: auto;
  border-radius: 8px;
  margin: 20px 0;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

/* 文章底部 */
.article-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 32px 0;
  border-top: 1px solid #eaeaea;
  border-bottom: 1px solid #eaeaea;
  margin-bottom: 40px;
  flex-wrap: wrap;
  gap: 20px;
}

.footer-share {
  display: flex;
  align-items: center;
  gap: 12px;
}

.share-title {
  font-size: 14px;
  color: #666;
}

.share-buttons {
  display: flex;
  gap: 8px;
}

.share-btn {
  width: 32px;
  height: 32px;
  border: 1px solid #eaeaea;
  border-radius: 50%;
  background: white;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
}

.share-btn:hover {
  background: #f8f9fa;
  transform: translateY(-2px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.footer-actions {
  display: flex;
  gap: 12px;
}

.action-btn {
  padding: 8px 16px;
  border: 1px solid #eaeaea;
  border-radius: 4px;
  background: white;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 14px;
  display: flex;
  align-items: center;
  gap: 6px;
}

.like-btn:hover {
  background: #ffebee;
  border-color: #ffcdd2;
  color: #c62828;
}

.comment-btn:hover {
  background: #e3f2fd;
  border-color: #bbdefb;
  color: #1565c0;
}

/* 相关文章 */
.related-articles {
  margin-bottom: 40px;
}

.related-title {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 20px;
  color: #333;
  padding-bottom: 12px;
  border-bottom: 2px solid #4a6fa5;
}

.related-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.related-item {
  padding: 16px;
  border: 1px solid #eaeaea;
  border-radius: 8px;
  transition: all 0.3s ease;
  cursor: pointer;
}

.related-item:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
}

.related-item-title {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 8px;
  color: #333;
  line-height: 1.4;
}

.related-item-date {
  font-size: 12px;
  color: #909399;
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
  .article-cover {
    height: 300px;
  }
  
  .cover-title {
    font-size: 28px;
  }
  
  .article-container {
    padding: 24px;
    margin-top: -30px;
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
  }
  
  .footer-share {
    width: 100%;
    justify-content: space-between;
  }
  
  .related-item-title {
    font-size: 14px;
  }
  
  .back-btn {
    width: 100%;
    justify-content: center;
  }
}

@media (max-width: 480px) {
  .article-cover {
    height: 250px;
  }
  
  .cover-title {
    font-size: 24px;
  }
  
  .article-container {
    padding: 20px;
  }
  
  .article-content {
    font-size: 15px;
  }
}
</style>