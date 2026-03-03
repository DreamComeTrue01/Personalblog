<template>
  <div class="portal-view" data-route="/" :class="currentMode">
    <!-- 通知组件 -->
    <div v-if="showNotification" class="notification" :class="notificationType">
      {{ notificationMessage }}
    </div>
    
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
              <li><router-link to="/" class="nav-item">首页</router-link></li>
              <li><router-link to="/blog" class="nav-item">文章</router-link></li>
              <li><router-link to="/life" class="nav-item">说说</router-link></li>
              <li><router-link to="/archive" class="nav-item">归档</router-link></li>
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
              <div v-if="!isAuthenticated" class="auth-required">
                <div class="auth-message">
                  <h4>需要登录</h4>
                  <p>请登录后访问功能管理</p>
                  <button class="action-btn csdn-primary" @click="login">登录</button>
                </div>
                
                <!-- 登录表单 -->
                <div v-if="showLoginForm" class="login-form">
                  <h4>管理员登录</h4>
                  <div class="form-group">
                    <label for="password">密码</label>
                    <input type="password" id="password" v-model="loginPassword" placeholder="请输入管理员密码" class="form-input">
                  </div>
                  <div class="form-actions">
                    <button class="action-btn csdn-primary" @click="submitLogin">登录</button>
                    <button class="action-btn csdn-cancel" @click="cancelLogin">取消</button>
                  </div>
                </div>
              </div>
              <div v-else class="authenticated-content">
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
                  <div class="feature-item csdn-item" @click="logout">
                    <span class="feature-icon">🚪</span>
                    <span class="feature-text">退出登录</span>
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
                          <div class="editor-toolbar">
                            <button class="toolbar-btn" @click="triggerImageUpload" title="上传图片">
                              🖼️ 上传图片
                            </button>
                            <button class="toolbar-btn" @click="openImageManager" title="管理图片">
                              📷 管理图片
                            </button>
                          </div>
                          <textarea 
                            v-model="articleContent" 
                            placeholder="请输入文章内容（支持Markdown格式）" 
                            class="csdn-editor-textarea"
                            rows="15"
                          ></textarea>
                          <input 
                            type="file" 
                            ref="imageInput" 
                            accept="image/*" 
                            @change="handleImageUpload" 
                            style="display: none;"
                          >
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
        <button class="icon-btn" aria-label="音乐" @click="toggleMusic">
          <span class="icon" v-if="isMusicPlaying">🎵</span>
          <span class="icon" v-else>🎶</span>
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
    
    <!-- 图片管理模态框 -->
    <div v-if="showImageManager" class="modal-overlay" @click="closeImageManager">
      <div class="modal-content image-manager-modal" @click.stop>
        <h3 class="modal-title">图片管理</h3>
        <div class="image-list">
          <div v-for="(image, index) in articleImages" :key="index" class="image-item">
            <div class="image-preview">
              <img :src="image.url" :alt="image.alt" class="preview-img">
            </div>
            <div class="image-controls">
              <div class="size-controls">
                <div class="size-group">
                  <label>宽度:</label>
                  <input type="number" v-model.number="image.width" class="size-input" @input="previewImageSize(index)">
                  <input type="range" v-model.number="image.width" class="size-slider" min="100" max="1000" @input="previewImageSize(index)">
                </div>
                <div class="size-group">
                  <label>高度:</label>
                  <input type="number" v-model.number="image.height" class="size-input" @input="previewImageSize(index)">
                  <input type="range" v-model.number="image.height" class="size-slider" min="100" max="1000" @input="previewImageSize(index)">
                </div>
              </div>
              <div class="preset-sizes">
                <button class="preset-btn" @click="setPresetSize(index, 300, 200)">300x200</button>
                <button class="preset-btn" @click="setPresetSize(index, 500, 300)">500x300</button>
                <button class="preset-btn" @click="setPresetSize(index, 800, 500)">800x500</button>
                <button class="preset-btn" @click="resetImageSize(index)">重置</button>
              </div>
              <div class="image-actions">
                <button class="action-btn csdn-primary" @click="updateImageSize(index)">更新尺寸</button>
                <button class="action-btn csdn-delete" @click="removeImage(index)">删除图片</button>
              </div>
            </div>
          </div>
          <div v-if="articleImages.length === 0" class="no-images">
            文章中暂无图片
          </div>
        </div>
        <div class="modal-actions">
          <button class="action-btn" @click="closeImageManager">关闭</button>
        </div>
      </div>
    </div>
    
    <!-- 设置模态框 -->
    <div v-if="showSettingsModal" class="modal-overlay" @click="closeSettingsModal">
      <div class="modal-content settings-modal" @click.stop>
        <h3 class="modal-title">设置</h3>
        <div class="settings-content">
          <!-- 音乐设置 -->
          <div class="setting-section">
            <h4 class="setting-section-title">音乐设置</h4>
            <div class="setting-item">
              <label>音乐音量</label>
              <div class="volume-control">
                <input type="range" v-model.number="musicVolume" class="volume-slider" min="0" max="1" step="0.05" @input="updateMusicVolume">
                <span class="volume-value">{{ Math.round(musicVolume * 100) }}%</span>
              </div>
            </div>
            <div class="setting-item">
              <label>自动播放音乐</label>
              <div class="toggle-switch">
                <input type="checkbox" id="autoPlayMusic" v-model="autoPlayMusic">
                <label for="autoPlayMusic" class="toggle-label"></label>
              </div>
            </div>
          </div>
          
          <!-- 主题设置 -->
          <div class="setting-section">
            <h4 class="setting-section-title">主题设置</h4>
            <div class="theme-options">
              <button class="theme-option" :class="{ active: currentMode === 'light' }" @click="currentMode = 'light'; localStorage.setItem('blogMode', 'light'); window.dispatchEvent(new Event('storage'))">
                <span class="theme-icon">☀️</span>
                <span class="theme-name">浅色模式</span>
              </button>
              <button class="theme-option" :class="{ active: currentMode === 'dark' }" @click="currentMode = 'dark'; localStorage.setItem('blogMode', 'dark'); window.dispatchEvent(new Event('storage'))">
                <span class="theme-icon">🌙</span>
                <span class="theme-name">深色模式</span>
              </button>
              <button class="theme-option" :class="{ active: currentMode === 'black' }" @click="currentMode = 'black'; localStorage.setItem('blogMode', 'black'); window.dispatchEvent(new Event('storage'))">
                <span class="theme-icon">⚫</span>
                <span class="theme-name">黑色模式</span>
              </button>
            </div>
          </div>
          
          <!-- 显示设置 -->
          <div class="setting-section">
            <h4 class="setting-section-title">显示设置</h4>
            <div class="setting-item">
              <label>字体大小</label>
              <div class="font-size-options">
                <button class="font-size-btn" :class="{ active: fontSize === 'small' }" @click="fontSize = 'small'; applyFontSize()">小</button>
                <button class="font-size-btn" :class="{ active: fontSize === 'medium' }" @click="fontSize = 'medium'; applyFontSize()">中</button>
                <button class="font-size-btn" :class="{ active: fontSize === 'large' }" @click="fontSize = 'large'; applyFontSize()">大</button>
              </div>
            </div>
            <div class="setting-item">
              <label>动画效果</label>
              <div class="toggle-switch">
                <input type="checkbox" id="enableAnimations" v-model="enableAnimations">
                <label for="enableAnimations" class="toggle-label"></label>
              </div>
            </div>
          </div>
          
          <!-- 通知设置 -->
          <div class="setting-section">
            <h4 class="setting-section-title">通知设置</h4>
            <div class="setting-item">
              <label>启用通知</label>
              <div class="toggle-switch">
                <input type="checkbox" id="enableNotifications" v-model="enableNotifications">
                <label for="enableNotifications" class="toggle-label"></label>
              </div>
            </div>
            <div class="setting-item">
              <label>评论通知</label>
              <div class="toggle-switch">
                <input type="checkbox" id="commentNotifications" v-model="commentNotifications">
                <label for="commentNotifications" class="toggle-label"></label>
              </div>
            </div>
          </div>
          
          <!-- 语言设置 -->
          <div class="setting-section">
            <h4 class="setting-section-title">语言设置</h4>
            <div class="setting-item">
              <label>网站语言</label>
              <select v-model="language" class="setting-select">
                <option value="zh-CN">简体中文</option>
                <option value="en-US">English</option>
                <option value="ja-JP">日本語</option>
              </select>
            </div>
          </div>
          
          <!-- 时间格式设置 -->
          <div class="setting-section">
            <h4 class="setting-section-title">时间格式</h4>
            <div class="setting-item">
              <label>日期格式</label>
              <select v-model="dateFormat" class="setting-select">
                <option value="YYYY-MM-DD">2026-02-24</option>
                <option value="MM-DD-YYYY">02-24-2026</option>
                <option value="DD/MM/YYYY">24/02/2026</option>
              </select>
            </div>
          </div>
          
          <!-- 文章阅读设置 -->
          <div class="setting-section">
            <h4 class="setting-section-title">文章阅读</h4>
            <div class="setting-item">
              <label>文章排序</label>
              <select v-model="articleSort" class="setting-select">
                <option value="latest">最新优先</option>
                <option value="oldest">最早优先</option>
                <option value="popular">热门优先</option>
              </select>
            </div>
          </div>
          
          <!-- 侧边栏设置 -->
          <div class="setting-section">
            <h4 class="setting-section-title">侧边栏</h4>
            <div class="setting-item">
              <label>显示公告栏</label>
              <div class="toggle-switch">
                <input type="checkbox" id="showAnnouncement" v-model="showAnnouncement">
                <label for="showAnnouncement" class="toggle-label"></label>
              </div>
            </div>
            <div class="setting-item">
              <label>显示导航菜单</label>
              <div class="toggle-switch">
                <input type="checkbox" id="showNavigation" v-model="showNavigation">
                <label for="showNavigation" class="toggle-label"></label>
              </div>
            </div>
            <div class="setting-item">
              <label>显示个人简介</label>
              <div class="toggle-switch">
                <input type="checkbox" id="showProfile" v-model="showProfile">
                <label for="showProfile" class="toggle-label"></label>
              </div>
            </div>
          </div>
          
          <!-- 数据管理 -->
          <div class="setting-section">
            <h4 class="setting-section-title">数据管理</h4>
            <div class="setting-item">
              <label>清理缓存</label>
              <button class="action-btn csdn-primary" @click="clearCache">清理</button>
            </div>
            <div class="setting-item">
              <label>数据导出</label>
              <button class="action-btn csdn-primary" @click="exportData">导出</button>
            </div>
            <div class="setting-item">
              <label>数据导入</label>
              <button class="action-btn csdn-primary" @click="triggerImport">导入</button>
              <input type="file" ref="importInput" accept=".json" @change="importData" style="display: none;">
            </div>
          </div>
          
          <!-- 快捷键设置 -->
          <div class="setting-section">
            <h4 class="setting-section-title">快捷键</h4>
            <div class="keyboard-shortcuts">
              <div class="shortcut-item">
                <span class="shortcut-action">新建文章</span>
                <span class="shortcut-key">Ctrl + N</span>
              </div>
              <div class="shortcut-item">
                <span class="shortcut-action">搜索</span>
                <span class="shortcut-key">Ctrl + F</span>
              </div>
              <div class="shortcut-item">
                <span class="shortcut-action">返回顶部</span>
                <span class="shortcut-key">Ctrl + ↑</span>
              </div>
              <div class="shortcut-item">
                <span class="shortcut-action">切换主题</span>
                <span class="shortcut-key">Ctrl + T</span>
              </div>
            </div>
          </div>
          
          <!-- 隐私设置 -->
          <div class="setting-section">
            <h4 class="setting-section-title">隐私设置</h4>
            <div class="setting-item">
              <label>允许数据分析</label>
              <div class="toggle-switch">
                <input type="checkbox" id="allowAnalytics" v-model="enableNotifications">
                <label for="allowAnalytics" class="toggle-label"></label>
              </div>
            </div>
            <div class="setting-item">
              <label>允许Cookie</label>
              <div class="toggle-switch">
                <input type="checkbox" id="allowCookies" v-model="enableNotifications">
                <label for="allowCookies" class="toggle-label"></label>
              </div>
            </div>
          </div>
          
          <!-- 关于 -->
          <div class="setting-section">
            <h4 class="setting-section-title">关于</h4>
            <div class="about-info">
              <p><strong>Dream's Blog</strong></p>
              <p>版本: 1.0.0</p>
              <p>一个个人博客网站，记录生活和学习</p>
              <button class="action-btn csdn-primary" @click="showAboutDetails = !showAboutDetails">查看更多</button>
              <div v-if="showAboutDetails" class="about-details">
                <p>© 2026 Dream's Blog. All rights reserved.</p>
                <p>使用 Vue 3 + Vite 构建</p>
                <p>支持 Markdown 编辑</p>
                <p>响应式设计，适配各种设备</p>
              </div>
            </div>
          </div>
        </div>
        <div class="modal-actions">
          <button class="action-btn" @click="closeSettingsModal">取消</button>
          <button class="action-btn csdn-primary" @click="applySettings">保存设置</button>
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
// 图片上传相关
const imageInput = ref(null)
// 导入数据相关
const importInput = ref(null)
// 图片管理相关
const showImageManager = ref(false)
const articleImages = ref([])
// 音乐播放相关
const isMusicPlaying = ref(false)
let audio = null
// 删除确认模态框相关
const showDeleteModal = ref(false)
const articleToDelete = ref(null)
// 导入头像图片
import avatarImage from '@/assets/images/avatar.png'

// 权限验证相关
const isAuthenticated = ref(localStorage.getItem('isAuthenticated') === 'true')
const showLoginForm = ref(false)
const loginPassword = ref('')

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

// 设置相关
const showSettingsModal = ref(false)
const musicVolume = ref(0.25)
const autoPlayMusic = ref(false)
const fontSize = ref('medium')
const enableAnimations = ref(true)
const enableNotifications = ref(true)
const commentNotifications = ref(true)
const showAboutDetails = ref(false)
const language = ref('zh-CN')
const dateFormat = ref('YYYY-MM-DD')
const articleSort = ref('latest')
const showAnnouncement = ref(true)
const showNavigation = ref(true)
const showProfile = ref(true)

// 通知相关
const showNotification = ref(false)
const notificationMessage = ref('')
const notificationType = ref('success')

// 显示通知
const showToast = (message, type = 'success') => {
  notificationMessage.value = message
  notificationType.value = type
  showNotification.value = true
  
  // 3秒后自动关闭通知
  setTimeout(() => {
    showNotification.value = false
  }, 3000)
}

// 打开设置
const openSettings = () => {
  // 同步当前音乐音量
  if (audio) {
    musicVolume.value = audio.volume
  }
  showSettingsModal.value = true
}

// 关闭设置
const closeSettingsModal = () => {
  showSettingsModal.value = false
}

// 应用设置
const applySettings = () => {
  // 保存音乐音量
  if (audio) {
    audio.volume = musicVolume.value
  }
  // 应用字体大小
  applyFontSize()
  // 保存设置到localStorage
  const settings = {
    musicVolume: musicVolume.value,
    autoPlayMusic: autoPlayMusic.value,
    fontSize: fontSize.value,
    enableAnimations: enableAnimations.value,
    enableNotifications: enableNotifications.value,
    commentNotifications: commentNotifications.value,
    language: language.value,
    dateFormat: dateFormat.value,
    articleSort: articleSort.value,
    showAnnouncement: showAnnouncement.value,
    showNavigation: showNavigation.value,
    showProfile: showProfile.value
  }
  localStorage.setItem('blogSettings', JSON.stringify(settings))
  closeSettingsModal()
  showToast('设置已保存')
}

// 实时更新音乐音量
const updateMusicVolume = () => {
  if (audio) {
    audio.volume = musicVolume.value
  }
}

// 应用字体大小
const applyFontSize = () => {
  document.documentElement.style.fontSize = fontSize.value === 'small' ? '14px' : fontSize.value === 'large' ? '18px' : '16px'
}

// 清理缓存
const clearCache = () => {
  if (confirm('确定要清理缓存吗？这将删除所有本地存储的数据。')) {
    localStorage.clear()
    showToast('缓存已清理，请刷新页面以应用更改。')
  }
}

// 导出数据
const exportData = () => {
  const data = {
    articles: articles.value,
    categories: categories.value,
    tags: tags.value,
    userSettings: userSettings.value,
    settings: {
      musicVolume: musicVolume.value,
      autoPlayMusic: autoPlayMusic.value,
      fontSize: fontSize.value,
      enableAnimations: enableAnimations.value,
      enableNotifications: enableNotifications.value,
      commentNotifications: commentNotifications.value,
      language: language.value,
      dateFormat: dateFormat.value,
      articleSort: articleSort.value,
      showAnnouncement: showAnnouncement.value,
      showNavigation: showNavigation.value,
      showProfile: showProfile.value
    }
  }
  const blob = new Blob([JSON.stringify(data, null, 2)], { type: 'application/json' })
  const url = URL.createObjectURL(blob)
  const a = document.createElement('a')
  a.href = url
  a.download = `blog-data-${new Date().toISOString().split('T')[0]}.json`
  a.click()
  URL.revokeObjectURL(url)
  showToast('数据导出成功')
}

// 触发导入
const triggerImport = () => {
  if (importInput.value) {
    importInput.value.click()
  }
}

// 导入数据
const importData = (event) => {
  const file = event.target.files[0]
  if (!file) return
  
  const reader = new FileReader()
  reader.onload = (e) => {
    try {
      const data = JSON.parse(e.target.result)
      if (confirm('确定要导入数据吗？这将覆盖当前所有数据。')) {
        if (data.articles) {
          articles.value = data.articles
          saveArticles()
        }
        if (data.categories) {
          categories.value = data.categories
        }
        if (data.tags) {
          tags.value = data.tags
        }
        if (data.userSettings) {
          userSettings.value = data.userSettings
          localStorage.setItem('userSettings', JSON.stringify(userSettings.value))
        }
        if (data.settings) {
          musicVolume.value = data.settings.musicVolume || 0.25
          autoPlayMusic.value = data.settings.autoPlayMusic || false
          fontSize.value = data.settings.fontSize || 'medium'
          enableAnimations.value = data.settings.enableAnimations || true
          enableNotifications.value = data.settings.enableNotifications || true
          commentNotifications.value = data.settings.commentNotifications || true
          language.value = data.settings.language || 'zh-CN'
          dateFormat.value = data.settings.dateFormat || 'YYYY-MM-DD'
          articleSort.value = data.settings.articleSort || 'latest'
          showAnnouncement.value = data.settings.showAnnouncement || true
          showNavigation.value = data.settings.showNavigation || true
          showProfile.value = data.settings.showProfile || true
          applyFontSize()
        }
        showToast('数据导入成功，请刷新页面以应用更改。')
      }
    } catch (error) {
      showToast('数据导入失败，请确保文件格式正确。', 'error')
      console.error('数据导入失败:', error)
    }
  }
  reader.onerror = () => {
    showToast('文件读取失败', 'error')
  }
  reader.readAsText(file)
}

// 切换下拉菜单
const toggleDropdown = () => {
  isDropdownOpen.value = !isDropdownOpen.value
}

// 权限验证方法
const login = () => {
  console.log('登录按钮被点击')
  showLoginForm.value = true
}

const submitLogin = () => {
  console.log('提交登录，密码:', loginPassword.value)
  if (loginPassword.value === 'admin123') { // 临时密码，实际项目中应该使用加密存储
    isAuthenticated.value = true
    localStorage.setItem('isAuthenticated', 'true')
    showLoginForm.value = false
    loginPassword.value = ''
    console.log('登录成功，isAuthenticated:', isAuthenticated.value)
    showToast('登录成功')
  } else {
    console.log('密码错误')
    showToast('密码错误', 'error')
  }
}

const cancelLogin = () => {
  showLoginForm.value = false
  loginPassword.value = ''
}

const logout = () => {
  isAuthenticated.value = false
  localStorage.removeItem('isAuthenticated')
  activeFeature.value = ''
  showToast('已退出登录')
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
  // 处理article.content为undefined的情况
  if (article.content !== undefined && article.content !== null) {
    articleContent.value = article.content
  } else {
    // 如果没有content字段，使用excerpt作为内容
    articleContent.value = article.excerpt ? `# ${article.title}\n\n${article.excerpt}` : '# 文章内容\n\n这是文章的默认内容...'
  }
  // 保存当前编辑的文章ID
  currentArticleId.value = article.id
  isEditing.value = true
}

const handleSaveArticle = () => {
  if (currentArticleTitle.value.trim() === '') {
    showToast('请输入文章标题', 'error')
    return
  }
  if (articleContent.value.trim() === '') {
    showToast('请输入文章内容', 'error')
    return
  }

  if (currentArticleId.value) {
    // 编辑现有文章
    const index = articles.value.findIndex(article => article.id === currentArticleId.value)
    if (index !== -1) {
      articles.value[index] = {
        ...articles.value[index],
        title: currentArticleTitle.value,
        content: articleContent.value,
        // 重新生成摘要
        excerpt: articleContent.value.substring(0, 100) + '...'
      }
      showToast(`文章 "${currentArticleTitle.value}" 已更新`)
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
    showToast(`文章 "${newArticle.title}" 已创建`)
    saveArticles()
  }

  isEditing.value = false
  currentArticleId.value = null // 重置编辑状态
}

const handleCancelEdit = () => {
  isEditing.value = false
  currentArticleId.value = null // 重置编辑状态
}

// 触发图片上传
const triggerImageUpload = () => {
  if (imageInput.value) {
    imageInput.value.click()
  }
}

// 处理图片上传
const handleImageUpload = (event) => {
  const file = event.target.files[0]
  if (!file) return
  
  // 检查文件类型
  if (!file.type.startsWith('image/')) {
    showToast('请选择图片文件', 'error')
    return
  }
  
  // 检查文件大小（限制为 5MB）
  if (file.size > 5 * 1024 * 1024) {
    showToast('图片大小不能超过 5MB', 'error')
    return
  }
  
  // 压缩图片
  const compressImage = (file, maxWidth = 800, maxHeight = 600, quality = 0.7) => {
    return new Promise((resolve, reject) => {
      const canvas = document.createElement('canvas')
      const ctx = canvas.getContext('2d')
      const img = new Image()
      
      img.onload = () => {
        // 计算压缩后的尺寸
        let width = img.width
        let height = img.height
        
        if (width > maxWidth) {
          height = (height * maxWidth) / width
          width = maxWidth
        }
        
        if (height > maxHeight) {
          width = (width * maxHeight) / height
          height = maxHeight
        }
        
        // 设置canvas尺寸
        canvas.width = width
        canvas.height = height
        
        // 绘制压缩后的图片
        ctx.drawImage(img, 0, 0, width, height)
        
        // 转换为base64编码
        const compressedDataUrl = canvas.toDataURL('image/jpeg', quality)
        resolve(compressedDataUrl)
      }
      
      img.onerror = reject
      img.src = URL.createObjectURL(file)
    })
  }
  
  // 压缩并上传图片
  compressImage(file)
    .then((compressedImageUrl) => {
      // 直接插入默认尺寸的图片
      const imageMarkdown = `![图片](${compressedImageUrl})\n\n`
      articleContent.value += imageMarkdown
      showToast('图片上传成功，已插入到文章内容中')
    })
    .catch((error) => {
      console.error('图片压缩失败:', error)
      showToast('图片上传失败，请重试', 'error')
    })
}

// 打开图片管理模态框
const openImageManager = () => {
  // 从文章内容中提取图片
  extractImages()
  showImageManager.value = true
}

// 关闭图片管理模态框
const closeImageManager = () => {
  showImageManager.value = false
}

// 从文章内容中提取图片
const extractImages = () => {
  const content = articleContent.value
  const imageMatches = content.match(/!\[(.*?)\]\((.*?)(?:\s*=\s*(\d+)x(\d+))?\)/gim)
  const images = []
  
  if (imageMatches) {
    imageMatches.forEach(match => {
      const matchResult = match.match(/!\[(.*?)\]\((.*?)(?:\s*=\s*(\d+)x(\d+))?\)/)
      if (matchResult) {
        const [, alt, url, width, height] = matchResult
        images.push({
          alt: alt || '图片',
          url: url,
          width: width ? parseInt(width) : 0,
          height: height ? parseInt(height) : 0,
          originalMatch: match
        })
      }
    })
  }
  
  articleImages.value = images
}

// 更新图片尺寸
const updateImageSize = (index) => {
  const image = articleImages.value[index]
  if (!image) return
  
  let newMarkdown
  if (image.width > 0 && image.height > 0) {
    newMarkdown = `![${image.alt}](${image.url} =${image.width}x${image.height})`
  } else {
    newMarkdown = `![${image.alt}](${image.url})`
  }
  
  // 替换文章内容中的图片Markdown
  articleContent.value = articleContent.value.replace(image.originalMatch, newMarkdown)
  
  // 更新图片的原始匹配
  image.originalMatch = newMarkdown
  
  showToast('图片尺寸已更新')
}

// 删除图片
const removeImage = (index) => {
  const image = articleImages.value[index]
  if (!image) return
  
  // 从文章内容中删除图片Markdown
  articleContent.value = articleContent.value.replace(image.originalMatch, '')
  
  // 从图片列表中删除
  articleImages.value.splice(index, 1)
  
  showToast('图片已删除')
}

// 实时预览图片尺寸
const previewImageSize = (index) => {
  const image = articleImages.value[index]
  if (!image) return
  
  // 更新预览图片的尺寸
  const previewImg = document.querySelector(`.image-item:nth-child(${index + 1}) .preview-img`)
  if (previewImg) {
    if (image.width > 0 && image.height > 0) {
      previewImg.style.width = `${image.width}px`
      previewImg.style.height = `${image.height}px`
    } else {
      previewImg.style.width = 'auto'
      previewImg.style.height = 'auto'
    }
  }
}

// 设置预设尺寸
const setPresetSize = (index, width, height) => {
  const image = articleImages.value[index]
  if (!image) return
  
  // 设置预设尺寸
  image.width = width
  image.height = height
  
  // 实时预览
  previewImageSize(index)
}

// 重置图片尺寸
const resetImageSize = (index) => {
  const image = articleImages.value[index]
  if (!image) return
  
  // 重置尺寸
  image.width = 0
  image.height = 0
  
  // 实时预览
  previewImageSize(index)
}

// 初始化音频对象
const initAudio = () => {
  if (!audio) {
    // 使用本地音乐文件
    audio = new Audio('/src/assets/music/background-music.mp3')
    audio.volume = 0.25 // 设置音量为25%
    audio.loop = true // 循环播放
    
    // 监听播放结束事件
    audio.addEventListener('ended', () => {
      isMusicPlaying.value = false
    })
  }
}

// 切换音乐播放/暂停
const toggleMusic = () => {
  initAudio()
  
  if (isMusicPlaying.value) {
    audio.pause()
    isMusicPlaying.value = false
    console.log('音乐已暂停')
  } else {
    audio.play().then(() => {
      isMusicPlaying.value = true
      console.log('音乐已播放')
    }).catch(error => {
      console.log('播放失败:', error)
      isMusicPlaying.value = false
    })
  }
  
  console.log('isMusicPlaying:', isMusicPlaying.value)
}

// 页面加载时自动播放音乐
const playMusicOnLoad = () => {
  initAudio()
  audio.play().then(() => {
    isMusicPlaying.value = true
    console.log('音乐自动播放成功')
  }).catch(error => {
    console.log('自动播放被阻止:', error)
    isMusicPlaying.value = false
    // 自动播放可能会被浏览器阻止，所以提供一个手动播放按钮
  })
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
    showToast(`文章: ${articleToDelete.value.title} 已删除`)
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
    showToast(`文章: ${article.title} 已删除`)
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
    showToast('请上传 .md 或 .markdown 文件', 'error')
    return
  }

  // 读取文件内容
  const reader = new FileReader()
  reader.onload = (e) => {
    const content = e.target.result
    
    // 处理本地图片路径
    let processedContent = content
    
    // 检测MD文件中的图片引用
    const imageMatches = content.match(/!\[(.*?)\]\(([^\)]+)\)/gim)
    if (imageMatches) {
      imageMatches.forEach(match => {
        // 提取图片路径
        const pathMatch = match.match(/!\[(.*?)\]\(([^\)]+)\)/)
        if (pathMatch && pathMatch[2]) {
          const imagePath = pathMatch[2]
          // 检查是否是本地图片路径（不是http/https开头）
          if (!imagePath.startsWith('http://') && !imagePath.startsWith('https://')) {
            // 提示用户需要上传图片
            showToast('MD文件中包含本地图片引用，请确保图片已上传到可访问的位置，并使用完整的URL路径。', 'warning')
          }
        }
      })
    }
    
    articleContent.value = processedContent
    
    // 自动提取标题（如果文件以 # 开头）
    const titleMatch = processedContent.match(/^#\s+(.+)$/m)
    if (titleMatch && !currentArticleTitle.value) {
      currentArticleTitle.value = titleMatch[1]
    }
    
    // 预留后端接口调用（后期实现）
    // uploadFileToBackend(file)
  }
  reader.onerror = () => {
    showToast('文件读取失败', 'error')
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
    showToast('请输入分类名称', 'error')
    return
  }

  if (currentCategory.value.id) {
    // 编辑现有分类
    const index = categories.value.findIndex(cat => cat.id === currentCategory.value.id)
    if (index !== -1) {
      categories.value[index] = { ...currentCategory.value }
      showToast(`分类 "${currentCategory.value.name}" 已更新`)
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
    showToast(`分类 "${newCategory.name}" 已创建`)
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
    showToast(`分类: ${category.name} 已删除`)
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
    showToast('请输入标签名称', 'error')
    return
  }

  if (currentTag.value.id) {
    // 编辑现有标签
    const index = tags.value.findIndex(t => t.id === currentTag.value.id)
    if (index !== -1) {
      tags.value[index] = { ...currentTag.value }
      showToast(`标签 "${currentTag.value.name}" 已更新`)
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
    showToast(`标签 "${newTag.name}" 已创建`)
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
    showToast(`标签: ${tag.name} 已删除`)
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
  showToast('个人设置已保存并同步')
}

const handleCancelProfileEdit = () => {
  // 重新加载设置
  loadUserSettings()
  showToast('已取消更改')
}

const handleSaveSite = () => {
  showToast('站点设置已保存')
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
    showToast('请选择图片文件', 'error')
    return
  }
  
  // 检查文件大小（限制为 5MB）
  if (file.size > 5 * 1024 * 1024) {
    showToast('图片大小不能超过 5MB', 'error')
    return
  }
  
  // 创建 FileReader 读取文件
  const reader = new FileReader()
  reader.onload = (e) => {
    // 使用 data URL 作为头像
    userSettings.value.avatar = e.target.result
    showToast('头像已更新')
    // 预留后端接口调用（后期实现）
    // uploadAvatarToBackend(file)
  }
  reader.onerror = () => {
    showToast('图片读取失败', 'error')
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
  
  // 加载保存的设置
  loadSettings()
  
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
  
  // 应用字体大小
  applyFontSize()
  
  // 页面加载时自动播放音乐
  if (autoPlayMusic.value) {
    playMusicOnLoad()
  }
})

// 加载保存的设置
const loadSettings = () => {
  const savedSettings = localStorage.getItem('blogSettings')
  if (savedSettings) {
    try {
      const settings = JSON.parse(savedSettings)
      musicVolume.value = settings.musicVolume || 0.25
      autoPlayMusic.value = settings.autoPlayMusic || false
      fontSize.value = settings.fontSize || 'medium'
      enableAnimations.value = settings.enableAnimations || true
      enableNotifications.value = settings.enableNotifications || true
      commentNotifications.value = settings.commentNotifications || true
      language.value = settings.language || 'zh-CN'
      dateFormat.value = settings.dateFormat || 'YYYY-MM-DD'
      articleSort.value = settings.articleSort || 'latest'
      showAnnouncement.value = settings.showAnnouncement || true
      showNavigation.value = settings.showNavigation || true
      showProfile.value = settings.showProfile || true
    } catch (error) {
      console.error('加载设置失败:', error)
    }
  }
}
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

/* 权限验证区域 */
.auth-required {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 300px;
  background: rgba(74, 111, 165, 0.1);
  border: 2px dashed rgba(74, 111, 165, 0.5);
  border-radius: 8px;
  backdrop-filter: blur(5px);
  padding: 40px 20px;
  position: relative;
  z-index: 1;
}

.auth-required::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, rgba(74, 111, 165, 0.05), rgba(74, 111, 165, 0.15));
  border-radius: 8px;
  z-index: -1;
}

.auth-message {
  text-align: center;
  max-width: 300px;
  position: relative;
  z-index: 2;
}

.auth-message h4 {
  font-size: 20px;
  font-weight: 600;
  color: #4a6fa5;
  margin-bottom: 12px;
  text-shadow: 0 1px 2px rgba(255, 255, 255, 0.3);
}

.auth-message p {
  font-size: 14px;
  color: #333;
  margin-bottom: 24px;
  line-height: 1.5;
  text-shadow: 0 1px 1px rgba(255, 255, 255, 0.2);
}

.auth-message button {
  position: relative;
  z-index: 3;
  cursor: pointer;
}

/* 登录表单 */
.login-form {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background: rgba(255, 255, 255, 0.95);
  border: 2px solid #4a6fa5;
  border-radius: 12px;
  padding: 32px;
  width: 90%;
  max-width: 350px;
  box-shadow: 0 8px 24px rgba(74, 111, 165, 0.3);
  backdrop-filter: blur(10px);
  z-index: 10;
  animation: fadeIn 0.3s ease;
}

.login-form h4 {
  font-size: 20px;
  font-weight: 600;
  color: #4a6fa5;
  margin-bottom: 24px;
  text-align: center;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  font-size: 14px;
  font-weight: 500;
  color: #333;
  margin-bottom: 8px;
}

.form-input {
  width: 100%;
  padding: 12px 16px;
  border: 2px solid #e0e0e0;
  border-radius: 8px;
  font-size: 14px;
  transition: all 0.3s ease;
  background: #fafafa;
}

.form-input:focus {
  outline: none;
  border-color: #4a6fa5;
  box-shadow: 0 0 0 3px rgba(74, 111, 165, 0.1);
  background: white;
}

.form-actions {
  display: flex;
  gap: 12px;
  margin-top: 24px;
}

.form-actions button {
  flex: 1;
  padding: 12px 20px;
  border: none;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
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

/* 编辑器工具栏 */
.editor-toolbar {
  display: flex;
  gap: 10px;
  margin-bottom: 10px;
  padding: 10px;
  background: #f8f9fa;
  border-radius: 8px;
  border: 1px solid #e0e0e0;
  z-index: 1;
}

.toolbar-btn {
  padding: 10px 20px;
  background: #4a6fa5;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 14px;
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 500;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.toolbar-btn:hover {
  background: #3a5a85;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(74, 111, 165, 0.3);
}

.toolbar-btn:active {
  transform: translateY(0);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
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
  
  .editor-toolbar {
    flex-wrap: wrap;
  }
  
  .toolbar-btn {
    padding: 6px 12px;
    font-size: 12px;
  }
}

/* 图片管理模态框 */
.image-manager-modal {
  max-width: 800px;
  max-height: 80vh;
  overflow-y: auto;
}

.image-list {
  margin: 20px 0;
}

.image-item {
  display: flex;
  align-items: flex-start;
  gap: 20px;
  padding: 16px;
  background: #f8f9fa;
  border-radius: 8px;
  margin-bottom: 16px;
  border: 1px solid #e0e0e0;
}

.image-preview {
  flex-shrink: 0;
  width: 120px;
  height: 120px;
  border-radius: 8px;
  overflow: hidden;
  border: 1px solid #e0e0e0;
  background: white;
  display: flex;
  align-items: center;
  justify-content: center;
}

.preview-img {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
}

.image-controls {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.size-controls {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}

.size-controls label {
  font-size: 14px;
  font-weight: 500;
  color: #333;
  min-width: 60px;
}

.size-group {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 8px;
}

.size-group label {
  font-size: 14px;
  font-weight: 500;
  color: #333;
  min-width: 60px;
}

.size-input {
  width: 100px;
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
  transition: all 0.3s ease;
  flex-shrink: 0;
}

.size-input:focus {
  outline: none;
  border-color: #4a6fa5;
  box-shadow: 0 0 0 2px rgba(74, 111, 165, 0.1);
}

.size-slider {
  flex: 1;
  height: 6px;
  border-radius: 3px;
  background: #ddd;
  outline: none;
  -webkit-appearance: none;
  transition: all 0.3s ease;
}

.size-slider::-webkit-slider-thumb {
  -webkit-appearance: none;
  appearance: none;
  width: 18px;
  height: 18px;
  border-radius: 50%;
  background: #4a6fa5;
  cursor: pointer;
  transition: all 0.3s ease;
}

.size-slider::-webkit-slider-thumb:hover {
  background: #3a5a85;
  transform: scale(1.1);
}

.size-slider::-moz-range-thumb {
  width: 18px;
  height: 18px;
  border-radius: 50%;
  background: #4a6fa5;
  cursor: pointer;
  border: none;
  transition: all 0.3s ease;
}

.size-slider::-moz-range-thumb:hover {
  background: #3a5a85;
  transform: scale(1.1);
}

.preset-sizes {
  display: flex;
  gap: 8px;
  margin: 12px 0;
  flex-wrap: wrap;
}

.preset-btn {
  padding: 6px 12px;
  background: #f8f9fa;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-weight: 500;
  color: #333;
}

.preset-btn:hover {
  background: #e9ecef;
  border-color: #4a6fa5;
  color: #4a6fa5;
  transform: translateY(-2px);
}

.preset-btn:active {
  transform: translateY(0);
}

.image-actions {
  display: flex;
  gap: 12px;
  margin-top: 8px;
}

.no-images {
  text-align: center;
  padding: 40px 0;
  color: #666;
  font-size: 16px;
  background: #f8f9fa;
  border-radius: 8px;
  border: 2px dashed #e0e0e0;
}

/* 设置模态框 */
.settings-modal {
  max-width: 500px;
  max-height: 80vh;
  overflow-y: auto;
}

.settings-content {
  margin: 20px 0;
}

.setting-section {
  margin-bottom: 24px;
  padding-bottom: 20px;
  border-bottom: 1px solid #e0e0e0;
}

.setting-section:last-child {
  border-bottom: none;
  margin-bottom: 0;
  padding-bottom: 0;
}

.setting-section-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin-bottom: 16px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.setting-section-title::before {
  content: '⚙️';
  font-size: 18px;
}

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

.volume-control {
  display: flex;
  align-items: center;
  gap: 16px;
}

.volume-slider {
  flex: 1;
  height: 6px;
  border-radius: 3px;
  background: #ddd;
  outline: none;
  -webkit-appearance: none;
  transition: all 0.3s ease;
}

.volume-slider::-webkit-slider-thumb {
  -webkit-appearance: none;
  appearance: none;
  width: 18px;
  height: 18px;
  border-radius: 50%;
  background: #4a6fa5;
  cursor: pointer;
  transition: all 0.3s ease;
}

.volume-slider::-webkit-slider-thumb:hover {
  background: #3a5a85;
  transform: scale(1.1);
}

.volume-slider::-moz-range-thumb {
  width: 18px;
  height: 18px;
  border-radius: 50%;
  background: #4a6fa5;
  cursor: pointer;
  border: none;
  transition: all 0.3s ease;
}

.volume-slider::-moz-range-thumb:hover {
  background: #3a5a85;
  transform: scale(1.1);
}

.volume-value {
  font-size: 14px;
  font-weight: 500;
  color: #4a6fa5;
  min-width: 60px;
  text-align: right;
}

/* 主题选项 */
.theme-options {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.theme-option {
  flex: 1;
  min-width: 120px;
  padding: 16px;
  background: #f8f9fa;
  border: 2px solid #e0e0e0;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  text-align: center;
  font-size: 14px;
  font-weight: 500;
  color: #333;
}

.theme-option:hover {
  border-color: #4a6fa5;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(74, 111, 165, 0.2);
}

.theme-option.active {
  background: #4a6fa5;
  border-color: #4a6fa5;
  color: white;
  box-shadow: 0 4px 12px rgba(74, 111, 165, 0.3);
}

.theme-icon {
  display: block;
  font-size: 24px;
  margin-bottom: 8px;
}

.theme-name {
  display: block;
  font-size: 12px;
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

/* 设置选择框样式 */
.setting-select {
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 6px;
  background: white;
  color: #333;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s ease;
  width: 100%;
  box-sizing: border-box;
}

.dark .setting-select,
.black .setting-select {
  background: #444;
  border-color: #666;
  color: #eaeaea;
}

.setting-select:hover {
  border-color: #4a6fa5;
  box-shadow: 0 0 0 2px rgba(74, 111, 165, 0.2);
}

/* 快捷键设置样式 */
.keyboard-shortcuts {
  margin-top: 10px;
}

.shortcut-item {
  display: flex;
  justify-content: space-between;
  padding: 8px 0;
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);
}

.dark .shortcut-item,
.black .shortcut-item {
  border-bottom-color: rgba(255, 255, 255, 0.1);
}

.shortcut-key {
  background: rgba(0, 0, 0, 0.05);
  padding: 2px 8px;
  border-radius: 4px;
  font-family: monospace;
  font-size: 12px;
}

.dark .shortcut-key,
.black .shortcut-key {
  background: rgba(255, 255, 255, 0.1);
}

/* 深色模式下的设置样式 */
.dark .setting-section-title,
.black .setting-section-title {
  color: #eaeaea;
}

.dark .setting-item label,
.black .setting-item label {
  color: #eaeaea;
}

.dark .setting-section,
.black .setting-section {
  border-bottom-color: rgba(255, 255, 255, 0.1);
}

/* 通知组件样式 */
.notification {
  position: fixed;
  top: 20px;
  right: 20px;
  padding: 16px 20px;
  border-radius: 8px;
  color: white;
  font-size: 14px;
  font-weight: 500;
  z-index: 10000;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  animation: slideInRight 0.3s ease-out;
  max-width: 300px;
}

.notification.success {
  background-color: #4caf50;
}

.notification.error {
  background-color: #f44336;
}

.notification.warning {
  background-color: #ff9800;
}

@keyframes slideInRight {
  from {
    transform: translateX(100%);
    opacity: 0;
  }
  to {
    transform: translateX(0);
    opacity: 1;
  }
}

/* 响应式设置模态框 */
@media (max-width: 768px) {
  .settings-modal {
    width: 95%;
    max-width: 95%;
  }
  
  .theme-options {
    flex-direction: column;
  }
  
  .theme-option {
    min-width: 100%;
  }
  
  .notification {
    left: 20px;
    right: 20px;
    max-width: none;
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