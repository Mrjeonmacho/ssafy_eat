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
.wishlist-view-container {
  font-family: 'Noto Sans KR', sans-serif;
  background-color: #f8f9fa;
  margin: 0;
  padding: 20px;
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: flex-start;
}

.container {
  max-width: 800px;
  width: 100%;
  margin: 20px auto;
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
h2 {
  text-align: center;
  color: #343a40;
  margin-bottom: 25px;
  font-size: 2em;
  font-weight: 700;
}
.wishlist-items-wrapper {
  min-height: 100px;
}
.wishlist-item {
  border: 1px solid #e9ecef;
  border-radius: 8px;
  padding: 15px;
  margin-bottom: 15px;
  background-color: #ffffff;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
}
.wishlist-item h3 {
  margin: 0 0 5px 0;
  color: #007bff;
  font-size: 1.2em;
  font-weight: 700;
}
.wishlist-item p {
  margin: 0;
  color: #6c757d;
  font-size: 0.9em;
}
.remove-button {
  background-color: #dc3545;
  color: white;
  border: none;
  padding: 8px 15px;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.2s ease;
  font-size: 0.9em;
  font-weight: 700;
}
.remove-button:hover {
  background-color: #c82333;
  transform: translateY(-1px);
}
.no-items {
  text-align: center;
  color: #777;
  font-size: 1.1em;
  padding: 30px;
  background-color: #f0f2f5;
  border-radius: 8px;
  margin-bottom: 20px;
}
.back-button {
  display: block;
  width: fit-content;
  margin: 20px auto 0;
  padding: 10px 20px;
  background-color: #6c757d;
  color: white;
  text-decoration: none;
  border-radius: 5px;
  transition: background-color 0.3s ease;
  font-weight: 700;
}
.back-button:hover {
  background-color: #5a6268;
}
</style>
