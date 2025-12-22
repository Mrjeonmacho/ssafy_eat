<template>
  <div class="map-view-container">
    <router-link to="/" class="back-to-main-btn">대시보드</router-link>

    <!-- Side Panel for Restaurant Details -->
    <div :class="['side-panel', { open: isPanelOpen }]">
      <button @click="closePanel" class="panel-close-btn">&times;</button>
      <div v-if="selectedRestaurant">
        <div class="panel-header">
          <h2 id="panel-name">{{ selectedRestaurant.name }}</h2>
          <p id="panel-cuisine">{{ selectedRestaurant.cuisineType }}</p>
        </div>
        <div class="panel-content">
          <div class="panel-section">
            <h3>⭐ 평점 정보</h3>
            <div class="info-item rating-item">
              <div class="rating-source">
                <span class="icon naver">N</span> 네이버
              </div>
              <div class="rating-value">★ {{ selectedRestaurant.naverRating.toFixed(1) }}</div>
            </div>
            <div class="info-item rating-item">
              <div class="rating-source">
                <span class="icon kakao">K</span> 카카오
              </div>
              <div class="rating-value">★ {{ selectedRestaurant.kakaoRating.toFixed(1) }}</div>
            </div>
            <div class="info-item rating-item">
              <div class="rating-source">
                <i class="fab fa-google icon"></i> 구글
              </div>
              <div class="rating-value">★ {{ selectedRestaurant.googleRating.toFixed(1) }}</div>
            </div>
          </div>
          <div class="panel-section">
            <h3>📋 상세 정보</h3>
            <div class="info-item">
              <i class="fa-solid fa-circle-info"></i>
              <span>{{ selectedRestaurant.description || '소개 정보가 없습니다.' }}</span>
            </div>
            <div class="info-item">
              <i class="fa-solid fa-location-dot"></i>
              <span>{{ selectedRestaurant.address }}</span>
            </div>
            <div class="info-item">
              <i class="fa-solid fa-phone"></i>
              <span>{{ selectedRestaurant.phone || '전화번호 정보가 없습니다.' }}</span>
            </div>
          </div>
        </div>
        <div class="panel-footer">
          <button @click="addToWishlist" class="add-to-wishlist-btn">
            <i class="fa-solid fa-heart"></i>찜하기
          </button>
        </div>
      </div>
    </div>

    <!-- Map Container -->
    <div id="map"></div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

declare global {
  interface Window {
    kakao: any
  }
}

interface RestaurantDto {
  id: number
  name: string
  address: string
  phone: string | null
  description: string | null
  cuisineType: string
  latitude: number
  longitude: number
  naverRating: number
  kakaoRating: number
  googleRating: number
}

const authStore = useAuthStore()
const router = useRouter()
const isPanelOpen = ref(false)
const selectedRestaurant = ref<RestaurantDto | null>(null)
let map: any = null

const loadKakaoMapScript = () => {
  return new Promise<void>((resolve) => {
    if (window.kakao && window.kakao.maps) {
      resolve();
      return;
    }
    const script = document.createElement('script');
    script.src = '//dapi.kakao.com/v2/maps/sdk.js?appkey=a7dd75c481a7f81f5110d20f54d8a2ca&autoload=false';
    script.onload = () => window.kakao.maps.load(resolve);
    document.head.appendChild(script);
  });
};

const initMap = () => {
  const mapContainer = document.getElementById('map');
  if (!mapContainer) return;
  const mapOption = {
    center: new window.kakao.maps.LatLng(37.5015, 127.0373),
    level: 4,
  };
  map = new window.kakao.maps.Map(mapContainer, mapOption);
};

const closePanel = () => {
  isPanelOpen.value = false
}

const fetchAndDisplayRestaurants = async () => {
  try {
    const response = await fetch('/api/restaurants')
    if (!response.ok) throw new Error('맛집 목록을 불러오는 데 실패했습니다.')
    const restaurants: RestaurantDto[] = await response.json()

    restaurants.forEach(restaurant => {
      const markerPosition = new window.kakao.maps.LatLng(
        restaurant.latitude,
        restaurant.longitude
      )
      const marker = new window.kakao.maps.Marker({
        position: markerPosition
      });
      marker.setMap(map);

      window.kakao.maps.event.addListener(marker, 'click', async () => {
        try {
          const detailResponse = await fetch(`/api/restaurants/${restaurant.id}`);
          if (!detailResponse.ok) throw new Error(`맛집 상세 정보를 불러오는 데 실패했습니다.`);
          const details: RestaurantDto = await detailResponse.json();
          selectedRestaurant.value = details;
          isPanelOpen.value = true;
        } catch (error) {
          console.error('Error fetching restaurant details:', error);
          alert((error as Error).message);
        }
      });
    });
  } catch (error) {
    console.error('Error fetching restaurants:', error)
  }
}

const addToWishlist = async () => {
  if (!authStore.userId) {
    alert('찜하기 기능을 사용하려면 로그인이 필요합니다.')
    router.push('/login')
    return
  }
  if (!selectedRestaurant.value?.id) {
    alert('찜할 맛집을 선택해주세요.')
    return
  }

  try {
    const response = await fetch(
      `/api/users/${authStore.userId}/wishlist/${selectedRestaurant.value.id}`,
      { method: 'POST' }
    )
    if (response.ok) {
      alert('찜 목록에 추가되었습니다!')
    } else if (response.status === 409) {
      alert('이미 찜한 식당입니다.')
    } else {
      const errorText = await response.text()
      throw new Error(errorText || '찜 목록 추가에 실패했습니다.')
    }
  } catch (error) {
    alert('찜하기 요청 중 오류가 발생했습니다.')
    console.error('Error adding to wishlist:', error)
  }
}

onMounted(async () => {
  if (!authStore.userId) {
    alert('로그인이 필요합니다.')
    router.push('/login')
    return
  }

  await loadKakaoMapScript();
  initMap();
  fetchAndDisplayRestaurants();
});
</script>

<style scoped>
/* Scoped styles from original index.html */
.map-view-container, #map {
  width: 100%;
  height: 100vh;
  position: relative;
  overflow: hidden;
}

.back-to-main-btn {
  position: fixed;
  top: 20px;
  right: 20px;
  z-index: 1001;
  background-color: #007bff;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 25px;
  text-decoration: none;
  font-size: 16px;
  font-weight: bold;
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
  transition: all 0.3s ease;
}
.back-to-main-btn:hover {
  background-color: #0056b3;
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(0,0,0,0.2);
}

.side-panel {
  position: fixed;
  left: 0;
  top: 0;
  width: 380px;
  height: 100%;
  background-color: #ffffff;
  box-shadow: 4px 0 15px rgba(0,0,0,0.1);
  transform: translateX(-100%);
  transition: transform 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  z-index: 1000;
  display: flex;
  flex-direction: column;
}
.side-panel.open {
  transform: translateX(0);
}

.panel-header {
  padding: 20px;
  border-bottom: 1px solid #e9ecef;
}
.panel-header h2 {
  font-size: 1.8em;
  font-weight: 700;
  margin: 0;
  color: #343a40;
}
.panel-header p {
  font-size: 1.1em;
  color: #6c757d;
  margin-top: 5px;
}

.panel-content {
  padding: 20px;
  flex-grow: 1;
  overflow-y: auto;
}

.panel-close-btn {
  position: absolute;
  top: 15px;
  right: 15px;
  background: none;
  border: none;
  font-size: 28px;
  cursor: pointer;
  color: #6c757d;
  transition: color 0.3s;
}
.panel-close-btn:hover {
  color: #343a40;
}

.panel-section {
  margin-bottom: 25px;
}
.panel-section h3 {
  font-size: 1em;
  font-weight: 700;
  color: #007bff;
  margin: 0 0 15px 0;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.info-item {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
  font-size: 1em;
}
.info-item i {
  width: 20px;
  text-align: center;
  margin-right: 15px;
  color: #6c757d;
  font-size: 1.1em;
}

.rating-item {
  justify-content: space-between;
}
.rating-source {
  display: flex;
  align-items: center;
  gap: 8px;
}
.rating-source .icon {
  display: inline-block;
  width: 20px;
  height: 20px;
  border-radius: 3px;
  color: white;
  text-align: center;
  font-size: 14px;
  font-weight: bold;
  line-height: 20px;
}
.rating-source .icon.naver {
  background-color: #03C75A;
}
.rating-source .icon.kakao {
  background-color: #FEE500;
  color: #191919;
}
.rating-source .fa-google {
  color: #DB4437;
}

.rating-value {
  font-weight: 700;
  color: #ffc107;
}

.panel-footer {
  padding: 20px;
  border-top: 1px solid #e9ecef;
}

.add-to-wishlist-btn {
  background: #fd7e14;
  color: white;
  padding: 15px 20px;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-size: 1.1em;
  font-weight: 700;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  transition: all 0.3s ease;
}
.add-to-wishlist-btn:hover {
  background-color: #e66a00;
  transform: translateY(-2px);
  box-shadow: 0 4px 10px rgba(253, 126, 20, 0.4);
}
</style>