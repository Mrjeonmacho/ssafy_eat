<template>
  <div class="dashboard-container">
    <header class="header">
      <h1 class="header-title">EAT-SSAFY</h1>
      <div class="user-info">
        <span>{{ authStore.username }}님</span>
        <button @click="handleLogout" class="logout-button">로그아웃</button>
      </div>
    </header>

    <main class="card-grid">
      <router-link to="/map" class="card nav-card">
        <div>
          <h2 class="card-title">맛집 목록</h2>
          <p class="card-description">전체 맛집 리스트를 확인하고 새로운 곳을 찾아보세요.</p>
        </div>
        <div class="card-icon"><i class="fa-solid fa-utensils"></i></div>
      </router-link>

      <router-link to="/wishlist" class="card nav-card wishlist-card">
        <div>
          <h2 class="card-title">내 찜 목록</h2>
          <p class="card-description">내가 찜한 맛집들을 확인하고 관리하세요.</p>
        </div>
        <div class="card-icon"><i class="fa-solid fa-heart"></i></div>
      </router-link>

      <div class="card ranking-card">
        <h2 class="card-title">🏆 맛집 랭킹 Top 5</h2>
        <ul id="ranking-list" class="ranking-list">
          <li v-if="top5Restaurants.length === 0" class="ranking-item">
            <div class="restaurant-info">랭킹을 불러오는 중입니다...</div>
          </li>
          <li v-for="(r, index) in top5Restaurants" :key="r.id" class="ranking-item">
            <div :class="['rank', `top-${index + 1}`]">{{ index + 1 }}</div>
            <div class="restaurant-info">
              <div class="restaurant-name">{{ r.name }}</div>
              <div class="restaurant-rating">
                ⭐ {{ r.averageRating.toFixed(2) }} (네이버: {{ r.naverRating.toFixed(1) }}, 카카오: {{ r.kakaoRating.toFixed(1) }}, 구글: {{ r.googleRating.toFixed(1) }})
              </div>
            </div>
          </li>
        </ul>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

interface RestaurantRankingDto {
  id: number
  name: string
  naverRating: number
  kakaoRating: number
  googleRating: number
  averageRating: number
}

const authStore = useAuthStore()
const router = useRouter()
const top5Restaurants = ref<RestaurantRankingDto[]>([])

const handleLogout = () => {
  authStore.clearAuth()
  alert('로그아웃 되었습니다.')
  router.push('/login')
}

const fetchTopRestaurants = async () => {
  try {
    const response = await fetch('/api/restaurants')
    if (!response.ok) {
      throw new Error('맛집 데이터를 불러오는 데 실패했습니다.')
    }
    const restaurants: RestaurantRankingDto[] = await response.json()

    // Calculate average rating and sort
    restaurants.forEach(r => {
      const ratings = [r.naverRating, r.kakaoRating, r.googleRating].filter(
        rating => rating > 0
      )
      r.averageRating =
        ratings.length > 0 ? ratings.reduce((a, b) => a + b, 0) / ratings.length : 0
    })

    restaurants.sort((a, b) => b.averageRating - a.averageRating)

    top5Restaurants.value = restaurants.slice(0, 5)
  } catch (error) {
    console.error('Error fetching ranking:', error)
  }
}

onMounted(() => {
  if (!authStore.userId || !authStore.username) {
    alert('로그인이 필요합니다.')
    router.push('/login')
    return
  }
  fetchTopRestaurants()
})
</script>

<style scoped>

@import url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@400;700&display=swap');



:root {

  --primary-color: #3498db;

  --secondary-color: #95a5a6;

  --background-color: #ecf0f1;

  --card-bg-color: #ffffff;

  --text-color: #2c3e50;

  --light-text-color: #ffffff;

  --shadow: 0 5px 15px rgba(0, 0, 0, 0.08);

  --ranking-1: #ffd700;

  --ranking-2: #c0c0c0;

  --ranking-3: #cd7f32;

}



.dashboard-container {
  font-family: 'Noto Sans KR', sans-serif;
  background-color: var(--background-color);
  min-height: 100vh;
  padding: 30px;
  color: var(--text-color);
  display: flex;
  flex-direction: column;
  align-items: center;
}

.header, .card-grid {
    width: 100%;
    max-width: 1200px;
}



.header {

  display: flex;

  justify-content: space-between;

  align-items: center;

  margin-bottom: 40px;

}



.header-title {

  font-size: 2.8em;

  font-weight: 700;

  color: var(--primary-color);

}



.user-info {

  display: flex;

  align-items: center;

  gap: 15px;

  font-size: 1.1em;

}



.logout-button {

  background: var(--secondary-color);

  color: var(--light-text-color);

  border: none;

  padding: 10px 20px;

  border-radius: 25px;

  cursor: pointer;

  font-size: 0.9em;

  font-weight: 700;

  transition: background-color 0.3s ease;

}



.logout-button:hover {

  background: #7f8c8d;

}



.card-grid {

  display: grid;

  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));

  gap: 30px;

}



.card {

  background: var(--card-bg-color);

  border-radius: 15px;

  padding: 30px;

  box-shadow: var(--shadow);

  transition: transform 0.3s ease, box-shadow 0.3s ease;

  text-decoration: none;

  color: var(--text-color);

  display: flex;

  flex-direction: column;

}



.card:hover {

  transform: translateY(-8px);

  box-shadow: 0 12px 25px rgba(0, 0, 0, 0.1);

}



.card-title {

  font-size: 1.6em;

  font-weight: 700;

  margin: 0 0 12px 0;

}



.card-description {

  font-size: 1.05em;

  color: #7f8c8d;

  line-height: 1.6;

  flex-grow: 1;

  margin-bottom: 25px;

}



.card-icon {

  font-size: 3em;

  text-align: right;

  opacity: 0.9;

}



.nav-card .card-icon {

  color: var(--primary-color);

}



.wishlist-card .card-icon {

  color: #e74c3c;

}



.ranking-card {

  grid-column: 1 / -1;

}



.ranking-list {

  list-style: none;

  padding: 0;

  margin: 0;

}



.ranking-item {

  display: flex;

  align-items: center;

  padding: 18px 12px;

  border-bottom: 1px solid #ecf0f1;

  transition: background-color 0.2s ease;

}

.ranking-item:hover{

    background-color: #f7f9f9;

}



.ranking-item:last-child {

  border-bottom: none;

}



.rank {

  font-size: 1.6em;

  font-weight: 700;

  width: 50px;

  text-align: center;

}



.rank.top-1 { color: var(--ranking-1); }

.rank.top-2 { color: var(--ranking-2); }

.rank.top-3 { color: var(--ranking-3); }



.restaurant-info {

  flex-grow: 1;

  margin-left: 20px;

}



.restaurant-name {

  font-weight: 700;

  font-size: 1.2em;

}



.restaurant-rating {

  font-size: 0.95em;

  color: #7f8c8d;

  margin-top: 5px;

}

</style>
