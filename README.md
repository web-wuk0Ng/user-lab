# user-lab（个人技能实训 实验一~五）

渐进式全栈实验工程：Spring Boot 3.5.0 + MyBatis + MySQL 8 用户 CRUD，
Vue 3 + Vite 前端，Git/CI，Spring Security + JWT 权限，RAG 问答与 AI 安全边界。

## 本机复现（实验环境实测版本）
- JDK 17.0.19 / Apache Maven 3.9.12 / Node 22.22.2
- MySQL 8.0.44 独立实验实例：127.0.0.1:3310（自建，root 空密码，仅本机）
- 后端 8080，前端 dev 5173 / preview 4173，本机 mock 模型服务 8099

## 启动顺序
1. 启动 MySQL 实验实例（lab-mysql/my.ini）并建库建表
2. backend: 设置 DB_USER/DB_PASSWORD/JWT_SECRET_B64/LLM_URL/LLM_MODEL/LLM_API_KEY 后 `mvn spring-boot:run`
3. mock-llm: `node server.js`
4. frontend: `npm ci` 然后 `npm run dev`

数据库口令、JWT 密钥只通过环境变量注入，不写入仓库。

页面说明：在此维护用户信息。
