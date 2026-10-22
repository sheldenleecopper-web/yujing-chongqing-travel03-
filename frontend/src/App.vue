<template>
  <router-view />
</template>

<script setup>
</script>

<style>
/* ============ 渝境智旅 · 全局设计系统 ============ */
:root {
  /* 品牌色板：山城赤陶 × 雾都夜色 × 鎏金 */
  --yj-primary: #bf4d2e;
  --yj-primary-deep: #9c3a20;
  --yj-primary-soft: #d9764a;
  --yj-gold: #c19a5b;
  --yj-night: #182029;
  --yj-night-2: #232f3d;
  --yj-bg: #f6f2ea;
  --yj-bg-soft: #fbf8f1;
  --yj-card: #ffffff;
  --yj-ink: #2a2320;
  --yj-ink-2: #5c5148;
  --yj-muted: #9a8b7c;
  --yj-line: #ece1d1;
  --yj-radius: 14px;
  --yj-radius-sm: 10px;
  --yj-shadow-sm: 0 2px 10px rgba(122, 74, 42, 0.06);
  --yj-shadow: 0 8px 28px rgba(122, 74, 42, 0.10);
  --yj-shadow-lg: 0 18px 48px rgba(122, 74, 42, 0.16);
  --yj-font-serif: "Noto Serif SC", "Source Han Serif SC", "STSong", "SimSun", serif;
  --yj-font-sans: "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", -apple-system, "Segoe UI", sans-serif;

  /* Element Plus 主题接管 */
  --el-color-primary: #bf4d2e;
  --el-color-primary-light-3: #d2836f;
  --el-color-primary-light-5: #dfa694;
  --el-color-primary-light-7: #ecc8bb;
  --el-color-primary-light-8: #f3dad1;
  --el-color-primary-light-9: #f9ece7;
  --el-color-primary-dark-2: #9c3a20;
  --el-color-danger: #c0392b;
  --el-color-danger-light-3: #cf6a5f;
  --el-color-danger-light-5: #dd958c;
  --el-color-danger-light-7: #ebc0bb;
  --el-color-danger-light-8: #f2d5d2;
  --el-color-danger-light-9: #faeae8;
  --el-color-danger-dark-2: #9c2d21;
  --el-border-radius-base: 8px;
  --el-font-family: var(--yj-font-sans);
  --el-text-color-primary: var(--yj-ink);
  --el-text-color-regular: var(--yj-ink-2);
}

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html {
  scroll-behavior: smooth;
}

body {
  font-family: var(--yj-font-sans);
  background: var(--yj-bg);
  color: var(--yj-ink);
  -webkit-font-smoothing: antialiased;
  text-rendering: optimizeLegibility;
}

::selection {
  background: rgba(191, 77, 46, 0.18);
}

/* 细滚动条 */
::-webkit-scrollbar { width: 10px; height: 10px; }
::-webkit-scrollbar-track { background: transparent; }
::-webkit-scrollbar-thumb {
  background: #d8c9b6;
  border-radius: 6px;
  border: 2px solid var(--yj-bg);
}
::-webkit-scrollbar-thumb:hover { background: #c4b09a; }

/* 通用入场动画 */
@keyframes yj-fade-up {
  from { opacity: 0; transform: translateY(18px); }
  to { opacity: 1; transform: translateY(0); }
}
@keyframes yj-fade-in {
  from { opacity: 0; }
  to { opacity: 1; }
}
.yj-fade-up { animation: yj-fade-up 0.55s cubic-bezier(0.22, 0.61, 0.36, 1) both; }

/* Element Plus 细节统一 */
.el-button { font-weight: 500; letter-spacing: 0.3px; transition: all 0.25s ease; }
.el-button--primary {
  background: linear-gradient(135deg, var(--yj-primary) 0%, var(--yj-primary-soft) 100%);
  border: none;
  box-shadow: 0 4px 14px rgba(191, 77, 46, 0.28);
}
.el-button--primary:hover {
  background: linear-gradient(135deg, var(--yj-primary-deep) 0%, var(--yj-primary) 100%);
  transform: translateY(-1px);
  box-shadow: 0 6px 18px rgba(191, 77, 46, 0.34);
}
.el-button--danger {
  background: linear-gradient(135deg, var(--yj-primary) 0%, var(--yj-primary-soft) 100%);
  border: none;
  box-shadow: 0 4px 14px rgba(191, 77, 46, 0.28);
}
.el-button--danger:hover {
  background: linear-gradient(135deg, var(--yj-primary-deep) 0%, var(--yj-primary) 100%);
  transform: translateY(-1px);
  box-shadow: 0 6px 18px rgba(191, 77, 46, 0.34);
}
.el-button.is-plain.el-button--primary,
.el-button.is-plain.el-button--danger {
  background: var(--yj-bg-soft);
  box-shadow: none;
}
.el-card {
  border: 1px solid var(--yj-line);
  border-radius: var(--yj-radius);
  box-shadow: var(--yj-shadow-sm);
}
.el-tag { border-radius: 6px; font-weight: 500; }
.el-radio-button__inner { transition: all 0.25s ease; }
.el-tabs__item { font-weight: 500; }
.el-dialog { border-radius: var(--yj-radius); }
</style>
