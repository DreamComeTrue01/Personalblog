<template>
  <div class="home-view">
    <div class="home-container">
      <!-- 左侧文章列表 -->
      <div class="article-list">
        <article v-for="(article, index) in articles" :key="article.id" class="article-card" :class="`card-${index % 4 + 1}`" @click="navigateToArticle(article.id)">
          <h2 class="article-title">{{ article.title }}</h2>
          <div class="article-meta">
            <span class="article-date">{{ article.date }}</span>
            <span class="article-categories">
              {{ article.categories.join(', ') }}
            </span>
          </div>
          <p v-if="article.excerpt" class="article-excerpt">{{ article.excerpt }}</p>
          <!-- 底部数据 -->
          <div class="article-footer">
            <span class="footer-item">📅 {{ article.date }}</span>
            <span class="footer-item">❤️ {{ article.stats.likes }}</span>
            <span class="footer-item">💬 {{ article.stats.comments }}</span>
            <span class="footer-item">👁️ {{ article.stats.views || 0 }}</span>
          </div>
        </article>
      </div>
      

    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
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
        categories: ['生活'],
        excerpt: '2025 年终总结。诈尸一下，马上就是 2026 年了，哈哈哈。过得太快了吧。期间还想着水水文章的，结果完全没有了本科期间的热情，能写个年终总结就不错了。',
        stats: {
          likes: 9,
          comments: 12,
          views: 0
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
          comments: 8,
          views: 0
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
          comments: 15,
          views: 0
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
          comments: 23,
          views: 0
        }
      }
    ]
    articles.value = defaultArticles
    localStorage.setItem('articles', JSON.stringify(defaultArticles))
  }
}

// 导航到文章详情页
const navigateToArticle = (id) => {
  router.push(`/article/${id}`)
}

onMounted(() => {
  loadArticles()
  // 监听localStorage变化，实时更新文章列表
  window.addEventListener('storage', loadArticles)
})
</script>

<style scoped>
.home-view {
  width: 100%;
}

.home-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.article-list {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.article-card {
  border-radius: 12px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
  padding: 24px;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  cursor: pointer;
  position: relative;
  overflow: hidden;
  opacity: 0;
  transform: translateY(30px) scale(0.9);
  animation: fadeInUp 0.8s cubic-bezier(0.4, 0, 0.2, 1) forwards;
}

.article-card:hover {
  transform: translateY(-8px) scale(1.02);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.15);
}

@keyframes fadeInUp {
  to {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}

.article-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: linear-gradient(90deg, #4a6fa5, #6b8cce);
  transform: scaleX(0);
  transition: transform 0.3s ease;
}

.article-card:hover::before {
  transform: scaleX(1);
}

.article-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.12);
  transform: translateY(-2px);
}

/* 卡片颜色 */
.card-1 {
  background: #ffffff;
  border-left: 4px solid #4a6fa5;
  box-shadow: 0 4px 16px rgba(74, 111, 165, 0.15);
  animation-delay: 0.1s;
}

.card-2 {
  background: #ffffff;
  border-left: 4px solid #28a745;
  box-shadow: 0 4px 16px rgba(40, 167, 69, 0.15);
  animation-delay: 0.2s;
}

.card-3 {
  background: #ffffff;
  border-left: 4px solid #ffc107;
  box-shadow: 0 4px 16px rgba(255, 193, 7, 0.15);
  animation-delay: 0.3s;
}

.card-4 {
  background: #ffffff;
  border-left: 4px solid #dc3545;
  box-shadow: 0 4px 16px rgba(220, 53, 69, 0.15);
  animation-delay: 0.4s;
}

.app-container.dark .card-1 {
  background: #181818;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.2);
}

.app-container.dark .card-2 {
  background: #181818;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.2);
}

.app-container.dark .card-3 {
  background: #181818;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.2);
}

.app-container.dark .card-4 {
  background: #181818;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.2);
}

.app-container.black .card-1 {
  background: #111111;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.3);
}

.app-container.black .card-2 {
  background: #111111;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.3);
}

.app-container.black .card-3 {
  background: #111111;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.3);
}

.app-container.black .card-4 {
  background: #111111;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.3);
}

.article-title {
  font-size: 20px;
  font-weight: 600;
  color: #333333;
  margin-bottom: 12px;
  transition: all 0.3s ease;
  line-height: 1.3;
}

.article-card:hover .article-title {
  color: #4a6fa5;
  text-shadow: 0 1px 2px rgba(74, 111, 165, 0.1);
}

.article-meta {
  font-size: 13px;
  color: #6c757d;
  margin-bottom: 16px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 12px;
  padding-bottom: 12px;
  border-bottom: 1px solid #e9ecef;
}

.article-date {
  white-space: nowrap;
  display: flex;
  align-items: center;
  gap: 6px;
}

.article-date::before {
  content: '📅';
  font-size: 14px;
}

.article-categories {
  color: #4a6fa5;
  font-weight: 500;
}

.article-excerpt {
  font-size: 15px;
  color: #495057;
  line-height: 1.7;
  margin-top: 16px;
  margin-bottom: 20px;
  text-align: justify;
}

.app-container.dark .article-title {
  color: #e9ecef;
}

.app-container.dark .article-card:hover .article-title {
  color: #6b8cce;
  text-shadow: 0 1px 2px rgba(107, 140, 206, 0.2);
}

.app-container.dark .article-meta {
  color: #999999;
  border-bottom: 1px solid #333333;
}

.app-container.dark .article-excerpt {
  color: #ced4da;
}

.app-container.black .article-title {
  color: #e0e0e0;
}

.app-container.black .article-card:hover .article-title {
  color: #6b8cce;
}

.app-container.black .article-meta {
  color: #888888;
  border-bottom: 1px solid #222222;
}

.app-container.black .article-excerpt {
  color: #bdbdbd;
}

/* 底部数据 */
.article-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 16px;
  border-top: 1px solid #e9ecef;
  font-size: 13px;
  color: #6c757d;
  flex-wrap: wrap;
  gap: 12px;
}

.footer-item {
  display: flex;
  align-items: center;
  gap: 6px;
  transition: all 0.3s ease;
  padding: 4px 12px;
  border-radius: 16px;
  background-color: rgba(0, 0, 0, 0.05);
}

.footer-item:hover {
  color: #4a6fa5;
  background-color: rgba(74, 111, 165, 0.1);
  transform: translateY(-2px);
}

.app-container.dark .article-footer {
  border-top: 1px solid #333333;
  color: #999999;
}

.app-container.dark .footer-item {
  background-color: rgba(255, 255, 255, 0.05);
}

.app-container.dark .footer-item:hover {
  background-color: rgba(107, 140, 206, 0.1);
}

.app-container.black .article-footer {
  border-top: 1px solid #222222;
  color: #888888;
}

.app-container.black .footer-item {
  background-color: rgba(255, 255, 255, 0.03);
}

.app-container.black .footer-item:hover {
  background-color: rgba(107, 140, 206, 0.1);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .home-container {
    padding: 10px;
  }
  
  .article-card {
    padding: 12px;
  }
  
  .article-title {
    font-size: 16px;
  }
  
  .article-meta {
    font-size: 11px;
  }
  
  .article-excerpt {
    font-size: 13px;
  }
}

/* 主题模式适配 */
:deep(.app-container.dark) .article-card {
  background-color: #181818;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
}

:deep(.app-container.dark) .article-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
}

:deep(.app-container.dark) .article-meta,
:deep(.app-container.dark) .article-excerpt {
  color: #888888;
}

:deep(.app-container.black) .article-card {
  background-color: #111111;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.3);
}

:deep(.app-container.black) .article-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.4);
}

:deep(.app-container.black) .article-meta,
:deep(.app-container.black) .article-excerpt {
  color: #777777;
}
</style>