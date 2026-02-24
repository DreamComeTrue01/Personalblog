<template>
  <div class="home-view">
    <!-- 文章列表 -->
    <div class="article-list">
      <article v-for="(article, index) in articles" :key="article.id" class="article-card" :class="`card-${index % 4 + 1}`">
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
        </div>
      </article>
    </div>
  </div>
</template>

<script setup>
// 模拟文章数据
const articles = [
  {
    id: 1,
    title: '2025年终总结',
    date: '2月24日 , 2026 | 18:24',
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
    date: '2月18日 , 2025 | 11:16',
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
    date: '1月2日 , 2025 | 14:23',
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
    date: '10月21日 , 2022 | 18:09',
    categories: ['建站', 'Learn'],
    excerpt: '关于 Argon 主题的相关美化文档。新增 WP 用户头像插件。',
    stats: {
      likes: 18,
      comments: 23
    }
  }
]
</script>

<style scoped>
.home-view {
  width: 100%;
}

.article-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.article-card {
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  padding: 16px;
  transition: all 0.3s ease;
  cursor: pointer;
  backdrop-filter: blur(5px);
}

.article-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.12);
  transform: translateY(-2px);
}

/* 卡片颜色 */
.card-1 {
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.9), rgba(248, 249, 250, 0.95));
  border-left: 4px solid #4a6fa5;
}

.card-2 {
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.9), rgba(248, 249, 250, 0.95));
  border-left: 4px solid #28a745;
}

.card-3 {
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.9), rgba(248, 249, 250, 0.95));
  border-left: 4px solid #ffc107;
}

.card-4 {
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.9), rgba(248, 249, 250, 0.95));
  border-left: 4px solid #dc3545;
}

.article-title {
  font-size: 18px;
  font-weight: bold;
  color: inherit;
  margin-bottom: 8px;
  transition: all 0.3s ease;
}

.article-card:hover .article-title {
  color: #5e72e4;
}

.article-meta {
  font-size: 12px;
  color: #909399;
  margin-bottom: 12px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 8px;
}

.article-date {
  white-space: nowrap;
}

.article-categories {
  color: #5e72e4;
}

.article-excerpt {
  font-size: 14px;
  color: #666666;
  line-height: 1.6;
  margin-top: 8px;
  margin-bottom: 16px;
}

/* 底部数据 */
.article-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 12px;
  border-top: 1px solid rgba(0, 0, 0, 0.1);
  font-size: 12px;
  color: #909399;
}

.footer-item {
  display: flex;
  align-items: center;
  gap: 4px;
  transition: color 0.3s ease;
}

.footer-item:hover {
  color: #4a6fa5;
}

/* 响应式设计 */
@media (max-width: 768px) {
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

:deep(.app-container.dark) .article-meta {
  color: #666666;
}

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

:deep(.app-container.black) .article-meta {
  color: #555555;
}

:deep(.app-container.black) .article-excerpt {
  color: #777777;
}
</style>