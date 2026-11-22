<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
const api = axios.create({ baseURL: 'http://localhost:8080/api/users' })
const users = ref([])
const form = ref({ name: '', email: '' })
const editingId = ref(null)
const error = ref('')
const token = ref('')
const username = ref('')
const password = ref('')
const question = ref('')
const answer = ref('')
const sources = ref([])
const asking = ref(false)

api.interceptors.request.use(config => {
  if (token.value) config.headers.Authorization = `Bearer ${token.value}`
  return config
})
function reset() { editingId.value = null; form.value = { name: '', email: '' } }
function showError(e) { error.value = `请求失败：${e.response?.status ?? '网络错误'}，请检查输入和服务状态` }
async function load() {
  try { users.value = (await api.get('')).data; error.value = '' }
  catch (e) { showError(e) }
}
function edit(u) { editingId.value = u.id; form.value = { name: u.name, email: u.email } }
async function save() {
  try {
    if (editingId.value === null) await api.post('', form.value)
    else await api.put(`/${editingId.value}`, form.value)
    reset(); await load()
  } catch (e) { showError(e) }
}
async function remove(id) {
  if (!confirm('确定删除该用户吗？')) return
  try { await api.delete(`/${id}`); if (editingId.value === id) reset(); await load() }
  catch (e) { showError(e) }
}
async function login() {
  try {
    const response = await axios.post('http://localhost:8080/api/auth/login',
      { username: username.value, password: password.value })
    token.value = response.data.token
    password.value = ''
    await load()
  } catch (e) { password.value = ''; showError(e) }
}
function logout() {
  token.value = ''; users.value = []; reset(); error.value = ''
  question.value = ''; answer.value = ''; sources.value = []
}
async function askAI() {
  if (!question.value.trim() || asking.value) return
  asking.value = true; answer.value = ''; sources.value = []; error.value = ''
  try {
    const r = await axios.post('http://localhost:8080/api/ai/ask',
      { question: question.value },
      { headers: { Authorization: `Bearer ${token.value}` } })
    answer.value = r.data.answer; sources.value = r.data.sources
  } catch (e) { showError(e) }
  finally { asking.value = false }
}
</script>

<template>
<main>
<h1>用户管理</h1>
<p class="hint">在此维护用户信息</p>
<form v-if="!token" @submit.prevent="login">
  <input v-model="username" autocomplete="username" placeholder="账号" required>
  <input v-model="password" type="password" autocomplete="current-password" placeholder="密码" required>
  <button type="submit">登录</button>
</form>
<button v-else type="button" @click="logout">退出登录</button>
<p v-if="error" role="alert" class="err">{{ error }}</p>
<form @submit.prevent="save">
  <label>姓名 <input v-model.trim="form.name" maxlength="50" required></label>
  <label>邮箱 <input v-model.trim="form.email" type="email" maxlength="100" required></label>
  <button type="submit">{{ editingId === null ? '新增' : '保存修改' }}</button>
  <button type="button" @click="reset">清空</button>
</form>
<table>
  <thead><tr><th>ID</th><th>姓名</th><th>邮箱</th><th>操作</th></tr></thead>
  <tbody><tr v-for="u in users" :key="u.id">
    <td>{{ u.id }}</td><td>{{ u.name }}</td><td>{{ u.email }}</td>
    <td><button @click="edit(u)">编辑</button><button @click="remove(u.id)">删除</button></td>
  </tr></tbody>
</table>
<section v-if="token" class="ai">
  <h2>学习搭子</h2>
  <form @submit.prevent="askAI">
    <label>课程问题 <input v-model.trim="question" maxlength="500" required></label>
    <button type="submit" :disabled="asking">{{ asking ? '正在回答' : '提问' }}</button>
  </form>
  <p v-if="answer" class="answer">{{ answer }}</p>
  <p v-if="sources.length" class="sources">检索片段：{{ sources.join(', ') }}</p>
</section>
</main>
</template>

<style scoped>
main { max-width: 850px; margin: 2rem auto; font-family: "Microsoft YaHei", sans-serif; }
h1 { margin-bottom: .2rem; }
.hint { color: #666; margin-top: 0; }
form { display: flex; flex-wrap: wrap; gap: .7rem; margin-bottom: 1rem; align-items: center; }
table { border-collapse: collapse; width: 100%; margin-top: .5rem; }
th, td { border: 1px solid #ccc; padding: .5rem; text-align: left; }
button { margin-right: .3rem; }
.err { color: #b00020; font-weight: bold; }
.ai { margin-top: 2rem; padding: 1rem; border: 1px solid #ddd; border-radius: 8px; background: #fafafa; }
.answer { white-space: pre-wrap; background: #fff; padding: .7rem; border: 1px solid #eee; }
.sources { color: #555; font-size: .9rem; }
</style>
