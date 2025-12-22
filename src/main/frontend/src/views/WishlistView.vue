<template>
  <div class="wishlist-view-container">
    <div class="container">
      <h2>내 찜 리스트</h2>
      <div class="wishlist-items-wrapper">
        <div v-if="wishlist.length === 0" class="no-items">찜한 맛집이 없습니다.</div>
        <div v-for="item in wishlist" :key="item.id" class="wishlist-item">
          <div>
            <h3>{{ item.name }}</h3>
            <p>{{ item.cuisineType }} | {{ item.address }}</p>
          </div>
          <button @click="removeWishlistItem(item.id)" class="remove-button">찜 해제</button>
        </div>
      </div>
      <router-link to="/" class="back-button">대시보드로 돌아가기</router-link>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

interface WishlistItem {
  id: number
  name: string
  cuisineType: string
  address: string
}

const authStore = useAuthStore()
const router = useRouter()
const wishlist = ref<WishlistItem[]>([])

const fetchWishlist = async () => {
  if (!authStore.userId) {
    alert('로그인이 필요합니다.')
    router.push('/login')
    return
  }

  try {
    const response = await fetch(`/api/users/${authStore.userId}/wishlist`)
    if (!response.ok) {
      throw new Error(`찜 목록을 불러오는데 실패했습니다: ${response.statusText}`)
    }
    wishlist.value = await response.json()
  } catch (error) {
    console.error('Error fetching wishlist:', error)
    alert((error as Error).message)
  }
}

const removeWishlistItem = async (restaurantId: number) => {
  if (!authStore.userId) {
    alert('로그인이 필요합니다.')
    router.push('/login')
    return
  }

  try {
    const response = await fetch(
      `/api/users/${authStore.userId}/wishlist/${restaurantId}`,
      {
        method: 'DELETE'
      }
    )

    if (response.ok) {
      alert('찜 목록에서 맛집이 해제되었습니다.')
      await fetchWishlist() // Refresh wishlist
    } else {
      const errorText = await response.text()
      throw new Error(`찜 해제 실패: ${errorText}`)
    }
  } catch (error) {
    alert((error as Error).message)
    console.error('Error removing wishlist item:', error)
  }
}

onMounted(() => {
  fetchWishlist()
})
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@400;700&display=swap');

.wishlist-view-container {
  font-family: 'Noto Sans KR', sans-serif;
  background-color: #ecf0f1;
  min-height: 100vh;
  padding: 30px;
}

.container {
  max-width: 900px;
  margin: 0 auto;
  background: #ffffff;
  padding: 40px;
  border-radius: 15px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.08);
}

h2 {
  text-align: center;
  color: #2c3e50;
  margin-bottom: 35px;
  font-size: 2.2em;
  font-weight: 700;
}

.wishlist-items-wrapper {
  min-height: 150px;
}

.wishlist-item {
  border-bottom: 1px solid #ecf0f1;
  padding: 20px 10px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  transition: background-color 0.2s ease;
}

.wishlist-item:hover {
  background-color: #f7f9f9;
}

.wishlist-item:last-child {
  border-bottom: none;
}

.wishlist-item h3 {
  margin: 0 0 8px 0;
  color: #3498db;
  font-size: 1.3em;
  font-weight: 700;
}

.wishlist-item p {
  margin: 0;
  color: #7f8c8d;
  font-size: 1em;
}

.remove-button {
  background-color: #e74c3c;
  color: white;
  border: none;
  padding: 10px 18px;
  border-radius: 25px;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.2s ease;
  font-size: 0.9em;
  font-weight: 700;
}

.remove-button:hover {
  background-color: #c0392b;
  transform: translateY(-2px);
}

.no-items {
  text-align: center;
  color: #95a5a6;
  font-size: 1.2em;
  padding: 40px;
  background-color: #f8f9fa;
  border-radius: 10px;
  margin-bottom: 30px;
}

.back-button {
  display: block;
  width: fit-content;
  margin: 30px auto 0;
  padding: 12px 25px;
  background-color: #95a5a6;
  color: white;
  text-decoration: none;
  border-radius: 25px;
  transition: background-color 0.3s ease;
  font-weight: 700;
}

.back-button:hover {
  background-color: #7f8c8d;
}
</style>
