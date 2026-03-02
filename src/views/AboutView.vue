<template>
  <div class="about-view">

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
    <main class="about-main">
      <div class="about-container">
        
        
        <div class="about-content">
          <div class="about-profile">
            <div class="profile-avatar">
              <img src="/images/avatar.jpg" alt="个人头像" class="avatar-img">
            </div>
            <div class="profile-info">
              <h3 class="profile-name">Dream</h3>
              <p class="profile-desc">少年立志青云间，不负凌云万丈才</p>
              <div class="profile-tags">
                <span class="tag">前端开发</span>
                <span class="tag">Vue爱好者</span>
                <span class="tag">技术分享</span>
              </div>
            </div>
          </div>
          
          <div class="about-sections">
            <div class="about-section">
              <h3 class="section-title">个人介绍</h3>
              <div class="section-content">
                <p>
                  你好，我是 Dream，一名热爱前端开发的程序员。我喜欢探索新的技术，分享我的学习心得和项目经验。
                  这个博客是我记录学习过程、分享技术见解的地方，希望能为你带来一些帮助和启发。
                </p>
                <p>
                  我主要使用 Vue、React 等前端框架进行开发，同时也对 Node.js、TypeScript 等技术感兴趣。
                  我相信技术的力量可以改变世界，所以我一直在努力学习和成长。
                </p>
              </div>
            </div>
            
            <div class="about-section">
              <h3 class="section-title">技术栈</h3>
              <div class="tech-stack">
                <div class="tech-item">
                  <span class="tech-name">前端</span>
                  <div class="tech-skills">
                    <span class="skill">HTML5</span>
                    <span class="skill">CSS3</span>
                    <span class="skill">JavaScript</span>
                    <span class="skill">TypeScript</span>
                    <span class="skill">Vue 3</span>
                    <span class="skill">React</span>
                  </div>
                </div>
                <div class="tech-item">
                  <span class="tech-name">后端</span>
                  <div class="tech-skills">
                    <span class="skill">Node.js</span>
                    <span class="skill">Express</span>
                    <span class="skill">MongoDB</span>
                    <span class="skill">MySQL</span>
                  </div>
                </div>
                <div class="tech-item">
                  <span class="tech-name">工具</span>
                  <div class="tech-skills">
                    <span class="skill">Git</span>
                    <span class="skill">Webpack</span>
                    <span class="skill">Vite</span>
                    <span class="skill">VS Code</span>
                  </div>
                </div>
              </div>
            </div>
            
            <div class="about-section">
              <h3 class="section-title">联系方式</h3>
              <div class="contact-info">
                <div class="contact-item">
                  <span class="contact-icon">📧</span>
                  <span class="contact-label">Email:</span>
                  <span class="contact-value">dream@example.com</span>
                </div>
                <div class="contact-item">
                  <span class="contact-icon">🐱</span>
                  <span class="contact-label">GitHub:</span>
                  <a href="#" class="contact-link">github.com/dream</a>
                </div>
                <div class="contact-item">
                  <span class="contact-icon">💬</span>
                  <span class="contact-label">微信:</span>
                  <span class="contact-value">dream_tech</span>
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
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

// 标题文本
const titleText = ref('个人简介')

// 返回上一页
const goBack = () => {
  router.back()
}
</script>

<style scoped>
/* 导入字体 */
@import url('https://fonts.googleapis.com/css2?family=Ma+Shan+Zheng&family=Montserrat:wght@300;400;500;600;700&display=swap');

.about-view {
  width: 100%;
  min-height: 100vh;
  position: relative;
  overflow: hidden;
  background-image: url('https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=colorful%20vibrant%20background%20for%20about%20page%2C%20soft%20pastel%20colors%2C%20distinct%20patterns%2C%20inviting%20atmosphere%2C%20minimalist%20design&image_size=landscape_16_9');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
}

.about-view::before {
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

/* 中央内容区 */
.about-main {
  padding: 60px 0 80px;
  position: relative;
  z-index: 1;
}

.about-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 0 20px;
  position: relative;
  z-index: 1;
}

.about-header-section {
  text-align: center;
  margin-bottom: 60px;
  position: relative;
  z-index: 1;
  padding: 20px 20px;
}

.about-title {
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

.about-title:hover {
  transform: scale(1.05);
  box-shadow: 0 6px 20px rgba(59, 130, 246, 0.4);
}

.title-word {
  display: inline-block;
  opacity: 0;
  transform: translateY(20px);
  animation: fadeInUp 0.6s ease-out both;
}

.about-subtitle {
  font-size: 18px;
  color: #666;
  margin-bottom: 32px;
  animation: fadeInUp 0.8s ease-out 0.2s both;
}

.about-decoration {
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

.about-content {
  background: white;
  border-radius: 20px;
  padding: 40px;
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
  animation: fadeInUp 0.8s ease-out 0.3s both;
}

.about-content::before {
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

.about-content:hover {
  transform: translateY(-8px);
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.15);
}

.about-profile {
  display: flex;
  align-items: center;
  margin-bottom: 40px;
  padding-bottom: 32px;
  border-bottom: 2px solid #f0f0f0;
  position: relative;
  z-index: 1;
}

.profile-avatar {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  overflow: hidden;
  margin-right: 32px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  transition: all 0.3s ease;
  border: 3px solid rgba(139, 92, 246, 0.4);
}

.profile-avatar:hover {
  transform: scale(1.05);
  box-shadow: 0 6px 20px rgba(139, 92, 246, 0.3);
  border-color: rgba(139, 92, 246, 0.6);
}

.avatar-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.profile-info {
  flex: 1;
  position: relative;
  z-index: 1;
}

.profile-name {
  font-size: 24px;
  font-weight: bold;
  color: #333;
  margin-bottom: 8px;
  background: linear-gradient(135deg, #8b5cf6 0%, #ec4899 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.profile-desc {
  font-size: 16px;
  color: #666;
  margin-bottom: 16px;
  padding: 16px;
  background: #f8f9fa;
  border-radius: 12px;
  border-left: 4px solid #8b5cf6;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.profile-desc:hover {
  background: #f5f3ff;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transform: translateX(8px);
}

.profile-tags {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.tag {
  padding: 6px 16px;
  background: #f0f0f0;
  border-radius: 20px;
  font-size: 14px;
  color: #666;
  transition: all 0.3s ease;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  border: 1px solid #e9ecef;
}

.tag:hover {
  background: #8b5cf6;
  color: white;
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(139, 92, 246, 0.3);
  border-color: #8b5cf6;
}

.about-sections {
  position: relative;
  z-index: 1;
}

.about-section {
  margin-bottom: 40px;
  padding: 24px;
  background: #f8f9fa;
  border-radius: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
  border-left: 4px solid #8b5cf6;
}

.about-section:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
  border-color: #ec4899;
}

.section-title {
  font-size: 20px;
  font-weight: bold;
  color: #333;
  margin-bottom: 16px;
  padding-bottom: 8px;
  border-bottom: 1px solid #e9ecef;
  background: linear-gradient(135deg, #8b5cf6 0%, #ec4899 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.section-content {
  font-size: 16px;
  line-height: 1.6;
  color: #666;
}

.section-content p {
  margin-bottom: 16px;
}

.section-content p:last-child {
  margin-bottom: 0;
}

.tech-stack {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.tech-item {
  margin-bottom: 16px;
}

.tech-name {
  display: block;
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin-bottom: 12px;
  padding: 8px 16px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  width: fit-content;
}

.tech-skills {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.skill {
  padding: 6px 16px;
  background: white;
  border-radius: 20px;
  font-size: 14px;
  color: #666;
  transition: all 0.3s ease;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  border: 1px solid #e9ecef;
}

.skill:hover {
  background: #8b5cf6;
  color: white;
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(139, 92, 246, 0.3);
  border-color: #8b5cf6;
}

.contact-info {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.contact-item {
  display: flex;
  align-items: center;
  font-size: 16px;
  padding: 16px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
  border-left: 4px solid #8b5cf6;
}

.contact-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  border-color: #ec4899;
}

.contact-icon {
  font-size: 20px;
  margin-right: 16px;
  width: 32px;
  text-align: center;
}

.contact-label {
  width: 80px;
  font-weight: 600;
  color: #333;
}

.contact-value {
  color: #666;
  flex: 1;
}

.contact-link {
  color: #8b5cf6;
  text-decoration: none;
  transition: all 0.3s ease;
  flex: 1;
}

.contact-link:hover {
  color: #ec4899;
  text-decoration: underline;
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

/* 页脚 */
.portal-footer {
  background: rgba(255, 255, 255, 0.95);
  padding: 40px 20px 30px;
  border-top: 1px solid rgba(139, 92, 246, 0.2);
  position: relative;
  z-index: 1;
  margin-top: 60px;
}

.portal-footer::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 4px;
  background: linear-gradient(90deg, #8b5cf6 0%, #ec4899 100%);
}

.footer-content {
  max-width: 900px;
  margin: 0 auto;
  display: flex;
  justify-content: space-around;
  flex-wrap: wrap;
  gap: 40px;
  text-align: center;
}

.footer-section {
  min-width: 200px;
  padding: 20px;
  background: rgba(255, 255, 255, 0.8);
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.footer-section:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 16px rgba(139, 92, 246, 0.1);
}

.footer-section h3 {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  margin-bottom: 16px;
  background: linear-gradient(135deg, #8b5cf6 0%, #ec4899 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.footer-section p {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
  margin: 0;
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

@keyframes slideIn {
  from {
    width: 0;
  }
  to {
    width: 100%;
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

@keyframes wave {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-10px);
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

/* 响应式设计 */
@media (max-width: 768px) {
  .about-container {
    padding: 0 16px;
  }
  
  .about-title {
    font-size: 28px;
  }
  
  .about-subtitle {
    font-size: 16px;
  }
  
  .about-content {
    padding: 24px;
  }
  
  .about-profile {
    flex-direction: column;
    text-align: center;
  }
  
  .profile-avatar {
    margin-right: 0;
    margin-bottom: 20px;
  }
  
  .profile-tags {
    justify-content: center;
  }
  
  .tech-skills {
    justify-content: center;
  }
  
  .contact-item {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .contact-icon {
    margin-right: 0;
    margin-bottom: 8px;
  }
  
  .contact-label {
    width: 100%;
    margin-bottom: 4px;
  }
  
  .back-btn {
    padding: 12px 28px;
    font-size: 14px;
  }
  
  .footer-content {
    gap: 20px;
  }
}

@media (max-width: 480px) {
  .about-title {
    font-size: 24px;
  }
  
  .about-content {
    padding: 20px;
  }
  
  .about-section {
    padding: 16px;
  }
  
  .profile-avatar {
    width: 100px;
    height: 100px;
  }
}

/* 主题模式适配 */
:deep(.app-container.dark) .about-content {
  background-color: #181818;
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.2);
}

:deep(.app-container.dark) .about-content:hover {
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.3);
}

:deep(.app-container.dark) .about-title,
:deep(.app-container.dark) .about-subtitle,
:deep(.app-container.dark) .profile-name,
:deep(.app-container.dark) .section-title,
:deep(.app-container.dark) .tech-name,
:deep(.app-container.dark) .contact-label {
  color: #888888;
}

:deep(.app-container.dark) .profile-desc,
:deep(.app-container.dark) .section-content,
:deep(.app-container.dark) .contact-value,
:deep(.app-container.dark) .footer-text {
  color: #888888;
}

:deep(.app-container.dark) .tag,
:deep(.app-container.dark) .skill,
:deep(.app-container.dark) .contact-item {
  background: rgba(30, 30, 30, 0.8);
  border-color: #333333;
  color: #888888;
}

:deep(.app-container.dark) .tag:hover,
:deep(.app-container.dark) .skill:hover {
  background: #8b5cf6;
  color: white;
  border-color: #8b5cf6;
}

:deep(.app-container.dark) .back-btn {
  background: linear-gradient(135deg, #8b5cf6 0%, #ec4899 100%);
  color: white;
}

:deep(.app-container.dark) .portal-footer {
  background: rgba(24, 24, 24, 0.95);
  border-top-color: rgba(139, 92, 246, 0.3);
}

:deep(.app-container.dark) .footer-section {
  background: rgba(30, 30, 30, 0.8);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
}

:deep(.app-container.black) .about-content {
  background-color: #111111;
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.3);
}

:deep(.app-container.black) .about-content:hover {
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.4);
}

:deep(.app-container.black) .about-title,
:deep(.app-container.black) .about-subtitle,
:deep(.app-container.black) .profile-name,
:deep(.app-container.black) .section-title,
:deep(.app-container.black) .tech-name,
:deep(.app-container.black) .contact-label {
  color: #777777;
}

:deep(.app-container.black) .profile-desc,
:deep(.app-container.black) .section-content,
:deep(.app-container.black) .contact-value,
:deep(.app-container.black) .footer-text {
  color: #777777;
}

:deep(.app-container.black) .tag,
:deep(.app-container.black) .skill,
:deep(.app-container.black) .contact-item {
  background: rgba(25, 25, 25, 0.8);
  border-color: #222222;
  color: #777777;
}

:deep(.app-container.black) .tag:hover,
:deep(.app-container.black) .skill:hover {
  background: #8b5cf6;
  color: white;
  border-color: #8b5cf6;
}

:deep(.app-container.black) .portal-footer {
  background: rgba(17, 17, 17, 0.95);
  border-top-color: rgba(139, 92, 246, 0.3);
}

:deep(.app-container.black) .footer-section {
  background: rgba(25, 25, 25, 0.8);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.3);
}
</style>