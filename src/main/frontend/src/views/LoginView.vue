<template>
  <div class="auth-container">
    <h1 class="title">EAT-SSAFY</h1>
    <div class="form-container">
      <h2>로그인</h2>
      <form @submit.prevent="handleLogin">
        <div class="form-group">
          <label for="username">아이디</label>
          <input type="text" id="username" v-model="username" required>
        </div>
        <div class="form-group">
          <label for="password">비밀번호</label>
          <input type="password" id="password" v-model="password" required>
        </div>
        <button type="submit">로그인</button>
      </form>
      <div v-if="message" :class="['message', messageType]">{{ message }}</div>
      <p class="switch-form-link">
        <router-link to="/register">아직 계정이 없으신가요? 회원가입</router-link>
      </p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth' // Pinia Auth Store

const username = ref('')
const password = ref('')
const message = ref('')
const messageType = ref<'success' | 'error'>('error')
const router = useRouter()
const authStore = useAuthStore() // Initialize auth store

const handleLogin = async () => {
  message.value = ''
  try {
    const response = await fetch('/api/users/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        username: username.value,
        password: password.value
      })
    })

    if (response.ok) {
      const data = await response.json()
      authStore.setAuth(data.userId, data.username) // Use auth store
      
      messageType.value = 'success'
      message.value = '로그인 성공! 잠시 후 대시보드로 이동합니다.'
      
      setTimeout(() => {
        router.push('/')
      }, 1500)
    } else {
      const errorData = await response.text()
      messageType.value = 'error'
      message.value = `로그인 실패: ${errorData}`
    }
  } catch (error) {
    messageType.value = 'error'
    message.value = '네트워크 오류가 발생했습니다. 서버 상태를 확인해주세요.'
    console.error('Login error:', error)
  }
}
</script>

<style scoped>
.auth-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f8f9fa;
}
.title {
  font-size: 2.5em;
  font-weight: 700;
  color: #007bff;
  margin-bottom: 30px;
}
.form-container {
  background: #ffffff;
  padding: 40px;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.08);
  width: 100%;
  max-width: 400px;
  text-align: center;
}
h2 {
  margin-top: 0;
  margin-bottom: 25px;
  color: #343a40;
}
.form-group {
  margin-bottom: 20px;
  text-align: left;
}
.form-group label {
  display: block;
  margin-bottom: 8px;
  color: #495057;
  font-weight: 700;
}
.form-group input {
  width: 100%;
  padding: 12px 15px;
  border: 1px solid #ced4da;
  border-radius: 8px;
  box-sizing: border-box;
  font-size: 1em;
  transition: border-color 0.3s ease, box-shadow 0.3s ease;
}
.form-group input:focus {
  outline: none;
  border-color: #007bff;
  box-shadow: 0 0 0 3px rgba(0, 123, 255, 0.25);
}
button {
  background-color: #007bff;
  color: white;
  padding: 12px 20px;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-size: 1.1em;
  font-weight: 700;
  width: 100%;
  transition: background-color 0.3s ease;
}
button:hover {
  background-color: #0056b3;
}
.message {
  margin-top: 20px;
  font-size: 0.9em;
  min-height: 20px;
}
.message.success { color: green; }
.message.error { color: red; }
.switch-form-link {
  margin-top: 25px;
  font-size: 0.9em;
}
.switch-form-link a {
  color: #007bff;
  text-decoration: none;
  font-weight: 700;
}
.switch-form-link a:hover {
  text-decoration: underline;
}
</style>
