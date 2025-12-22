<template>
  <div class="auth-container">
    <h1 class="title">EAT-SSAFY</h1>
    <div class="form-container">
      <h2>회원가입</h2>
      <form @submit.prevent="handleRegister">
        <div class="form-group">
          <label for="username">아이디</label>
          <input type="text" id="username" v-model="username" required>
        </div>
        <div class="form-group">
          <label for="password">비밀번호</label>
          <input type="password" id="password" v-model="password" required>
        </div>
        <button type="submit">가입하기</button>
      </form>
      <div v-if="message" :class="['message', messageType]">{{ message }}</div>
      <p class="switch-form-link">
        <router-link to="/login">이미 계정이 있으신가요? 로그인</router-link>
      </p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const username = ref('')
const password = ref('')
const message = ref('')
const messageType = ref<'success' | 'error'>('error')
const router = useRouter()

const handleRegister = async () => {
  message.value = ''
  try {
    const response = await fetch('/api/users/register', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        username: username.value,
        password: password.value
      })
    })

    if (response.ok) {
      messageType.value = 'success'
      message.value = '회원가입 성공! 잠시 후 로그인 페이지로 이동합니다.'
      setTimeout(() => {
        router.push('/login')
      }, 2000)
    } else {
      const errorData = await response.text()
      messageType.value = 'error'
      message.value = `회원가입 실패: ${errorData}`
    }
  } catch (error) {
    messageType.value = 'error'
    message.value = '네트워크 오류가 발생했습니다. 서버 상태를 확인해주세요.'
    console.error('Registration error:', error)
  }
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@400;700&display=swap');

.auth-container {
  font-family: 'Noto Sans KR', sans-serif;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #ecf0f1;
}

.title {
  font-size: 3em;
  font-weight: 700;
  color: #3498db;
  margin-bottom: 40px;
  text-shadow: 1px 1px 2px rgba(0,0,0,0.1);
}

.form-container {
  background: #ffffff;
  padding: 50px;
  border-radius: 15px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 420px;
  text-align: center;
}

h2 {
  margin-top: 0;
  margin-bottom: 30px;
  color: #2c3e50;
  font-size: 1.8em;
  font-weight: 700;
}

.form-group {
  margin-bottom: 25px;
  text-align: left;
}

.form-group label {
  display: block;
  margin-bottom: 10px;
  color: #7f8c8d;
  font-weight: 700;
  font-size: 0.9em;
}

.form-group input {
  width: 100%;
  padding: 15px;
  border: 2px solid #dde4e6;
  border-radius: 10px;
  box-sizing: border-box;
  font-size: 1em;
  transition: all 0.3s ease;
}

.form-group input:focus {
  outline: none;
  border-color: #3498db;
  box-shadow: 0 0 0 4px rgba(52, 152, 219, 0.2);
}

button {
  background: #3498db;
  color: white;
  padding: 15px 20px;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  font-size: 1.2em;
  font-weight: 700;
  width: 100%;
  transition: background-color 0.3s ease, transform 0.2s ease;
}

button:hover {
  background-color: #2980b9;
  transform: translateY(-2px);
}

.message {
  margin-top: 25px;
  font-size: 1em;
  min-height: 24px;
}

.message.success {
  color: #27ae60;
}

.message.error {
  color: #e74c3c;
}

.switch-form-link {
  margin-top: 30px;
  font-size: 1em;
}

.switch-form-link a {
  color: #3498db;
  text-decoration: none;
  font-weight: 700;
}

.switch-form-link a:hover {
  text-decoration: underline;
}
</style>
