import { defineStore } from 'pinia'
import { ref } from 'vue'
import { useRouter } from 'vue-router'

export const useAuthStore = defineStore('auth', () => {
  const userId = ref<string | null>(localStorage.getItem('userId'))
  const username = ref<string | null>(localStorage.getItem('username'))
  const router = useRouter() // router cannot be used directly in defineStore

  // Actions
  function setAuth(id: string, name: string) {
    userId.value = id
    username.value = name
    localStorage.setItem('userId', id)
    localStorage.setItem('username', name)
  }

  function clearAuth() {
    userId.value = null
    username.value = null
    localStorage.removeItem('userId')
    localStorage.removeItem('username')
  }

  // No direct router usage in store. Components will use router after calling store actions.

  return { userId, username, setAuth, clearAuth }
})
